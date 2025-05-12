
package acme.features.crew.activityLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activity_log.ActivityLog;
import acme.entities.assignment.FlightAssignment;
import acme.realms.crew.FlightCrewMemberRepository;
import acme.realms.crew.FlightCrewMembers;

@GuiService
public class CrewActivityLogCreateService extends AbstractGuiService<FlightCrewMembers, ActivityLog> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private CrewActivityLogRepository	repository;

	@Autowired
	private FlightCrewMemberRepository	crewRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		int userId = super.getRequest().getPrincipal().getActiveRealm().getId();
		FlightCrewMembers crewMember = this.crewRepository.findById(userId);

		boolean status = false;

		if (crewMember != null) {
			Object assignmentData = super.getRequest().getData().get("flightAssignment");

			if (assignmentData == null || "".equals(assignmentData))
				status = true;
			else if (assignmentData instanceof String assignmentKey) {
				assignmentKey = assignmentKey.trim();

				if (assignmentKey.equals("0"))
					status = true;
				else if (assignmentKey.matches("\\d+")) {
					int assignmentId = Integer.parseInt(assignmentKey);
					Collection<FlightAssignment> validAssignments = this.repository.findAllFlightAssignments();
					boolean assignmentIsValid = validAssignments.stream().anyMatch(assignment -> assignment.getId() == assignmentId);
					status = assignmentIsValid;
				} else
					status = false;
			}
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ActivityLog activityLog = new ActivityLog();
		activityLog.setRegistrationMoment(MomentHelper.getCurrentMoment());
		activityLog.setDraftMode(true);
		super.getBuffer().addData(activityLog);
	}

	@Override
	public void bind(final ActivityLog activityLog) {
		super.bindObject(activityLog, "typeOfIncident", "description", "severityLevel", "flightAssignment");
	}

	@Override
	public void validate(final ActivityLog activityLog) {
		;
	}

	@Override
	public void perform(final ActivityLog activityLog) {
		this.repository.save(activityLog);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		int memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Collection<FlightAssignment> assignments;
		SelectChoices choices = new SelectChoices();

		// Obtener las asignaciones de vuelo para el miembro
		assignments = this.repository.findFlightAssignmentsByCrewMember(memberId);

		// Agregar una opción vacía si flightAssignment es null
		choices.add("0", "----", activityLog.getFlightAssignment() == null); // Opción vacía

		// Agregar todas las asignaciones a las opciones
		for (FlightAssignment assignment : assignments) {
			String key = Integer.toString(assignment.getId());
			String label = assignment.getMoment() + " - " + assignment.getDuty() + " - " + assignment.getCurrentStatus() + " - " + assignment.getLeg().getFlightNumber();
			boolean isSelected = assignment.equals(activityLog.getFlightAssignment());

			choices.add(key, label, isSelected);
		}

		// Crear el dataset con los campos a desvincular
		Dataset dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel", "flightAssignment", "draftMode");

		// Si el 'flightAssignment' es distinto de null, poner su valor en el dataset
		if (activityLog.getFlightAssignment() != null)
			dataset.put("flightAssignment", choices.getSelected().getKey());

		// Agregar la lista de asignaciones al dataset
		dataset.put("assignments", choices);

		// Enviar el dataset como respuesta
		super.getResponse().addData(dataset);
	}

}
