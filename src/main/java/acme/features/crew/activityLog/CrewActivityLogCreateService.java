
package acme.features.crew.activityLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activity_log.ActivityLog;
import acme.entities.assignment.FlightAssignment;
import acme.features.crew.assignment.CrewAssignmentRepository;
import acme.realms.crew.FlightCrewMembers;

@GuiService
public class CrewActivityLogCreateService extends AbstractGuiService<FlightCrewMembers, ActivityLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CrewActivityLogRepository	repository;

	@Autowired
	private CrewAssignmentRepository	assignmentRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		ActivityLog activityLog = new ActivityLog();
		super.getBuffer().addData(activityLog);
	}

	@Override
	public void bind(final ActivityLog activityLog) {
		super.bindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel");

		// Obtener el parámetro flightAssignment.id
		String flightAssignmentId = (String) super.getRequest().getData("flightAssignment.id");

		if (flightAssignmentId != null && !flightAssignmentId.isEmpty()) {
			int id = Integer.parseInt(flightAssignmentId);
			FlightAssignment flightAssignment = this.assignmentRepository.findFlightAssignmentById(id);  // Asegúrate de que encuentras el flightAssignment en el repositorio

			if (flightAssignment != null)
				activityLog.setFlightAssignment(flightAssignment);
		}
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
		Dataset dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel");

		// Obtener todas las asignaciones de vuelo disponibles
		Collection<FlightAssignment> flightAssignments = this.repository.findFlightAssignmentByCrewMember(memberId);

		// Convertir la colección de FlightAssignments en SelectChoices
		SelectChoices flightAssignmentChoices = new SelectChoices();

		// Recorrer las asignaciones y crear una etiqueta personalizada
		for (FlightAssignment assignment : flightAssignments) {
			String key = Integer.toString(assignment.getId());  // Usar el ID de FlightAssignment como clave
			String label = assignment.getDuty() + " - " + assignment.getMoment();  // Combinar duty y moment en una etiqueta

			// Agregar el FlightAssignment como opción en SelectChoices
			flightAssignmentChoices.add(key, label, false);
		}

		// Agregar el objeto SelectChoices al dataset
		dataset.put("flightAssignments", flightAssignmentChoices);

		super.getResponse().addData(dataset);
	}

}
