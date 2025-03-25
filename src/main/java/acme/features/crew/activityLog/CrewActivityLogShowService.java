
package acme.features.crew.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
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
		super.getResponse().setAuthorised(true);
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
		Dataset dataset;

		dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel");

		if (activityLog.getFlightAssignment() != null) {
			FlightAssignment assignment = activityLog.getFlightAssignment();
			dataset.put("flightAssignment.id", assignment.getId());
			dataset.put("flightAssignment.duty", assignment.getDuty());
			dataset.put("flightAssignment.moment", assignment.getMoment());
			dataset.put("flightAssignment.currentStatus", assignment.getCurrentStatus());
			dataset.put("flightAssignment.remarks", assignment.getRemarks());
		}

		super.getResponse().addData(dataset);
	}

}
