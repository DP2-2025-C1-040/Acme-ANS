
package acme.features.crew.activityLog;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activity_log.ActivityLog;
import acme.entities.assignment.FlightAssignment;
import acme.entities.leg.Leg;
import acme.realms.crew.FlightCrewMembers;

@GuiService
public class CrewActivityLogCreateService extends AbstractGuiService<FlightCrewMembers, ActivityLog> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private CrewActivityLogRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
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
		super.bindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel", "flightAssignment");
	}

	@Override
	public void validate(final ActivityLog activityLog) {
		// Obtener la Leg asociada
		FlightAssignment flightAssignment = activityLog.getFlightAssignment();
		if (flightAssignment != null && flightAssignment.getLeg() != null) {
			Leg leg = flightAssignment.getLeg();

			// Verificar si la Leg ha aterrizado (scheduleDeparture debe ser más antigua que el momento actual)
			if (leg.getScheduledDeparture() != null) {
				// Comparar la fecha de la salida programada con el momento actual
				Date scheduledDeparture = leg.getScheduledDeparture();
				Date currentMoment = MomentHelper.getCurrentMoment();

				if (scheduledDeparture.after(currentMoment))
					// Si la fecha de salida es posterior al momento actual, establecer error
					super.state(false, "leg", "acme.validation.activityLog.leg.notLanded");
			} else
				// Si no tiene fecha de salida programada, marcar el error
				super.state(false, "leg", "javax.validation.constraints.NotNull.message");
		} else
			// Si no hay FlightAssignment o Leg, marcar el error
			super.state(false, "flightAssignment", "javax.validation.constraints.NotNull.message");
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
