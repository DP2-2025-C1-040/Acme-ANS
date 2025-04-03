
package acme.features.crew.activityLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activity_log.ActivityLog;
import acme.entities.assignment.FlightAssignment;
import acme.realms.crew.FlightCrewMembers;

@GuiService
public class CrewActivityLogShowService extends AbstractGuiService<FlightCrewMembers, ActivityLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CrewActivityLogRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", int.class);
		ActivityLog activityLog = this.repository.findActivityLogById(id);

		boolean status = activityLog != null && super.getRequest().getPrincipal().hasRealm(activityLog.getFlightAssignment().getFlightCrewMember());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ActivityLog activityLog;
		int id;

		id = super.getRequest().getData("id", int.class);
		activityLog = this.repository.findActivityLogById(id);

		super.getBuffer().addData(activityLog);
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
