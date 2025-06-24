
package acme.features.crew.activityLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activity_log.ActivityLog;
import acme.entities.assignment.FlightAssignment;
import acme.realms.crew.FlightCrewMembers;

@GuiService
public class CrewActivityLogListService extends AbstractGuiService<FlightCrewMembers, ActivityLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CrewActivityLogRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		int flightAssignmentId;
		int flightCrewMemberId;
		FlightAssignment flightAssignment;
		boolean status;

		flightAssignmentId = super.getRequest().getData("flightAssignmentId", int.class);
		flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		flightAssignment = this.repository.findFlightAssignmentById(flightAssignmentId);

		status = flightAssignment != null && flightAssignment.getFlightCrewMember().getId() == flightCrewMemberId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<ActivityLog> activityLogs;
		int flightAssignmentId;

		flightAssignmentId = super.getRequest().getData("flightAssignmentId", int.class);
		activityLogs = this.repository.findAllActivityLogsByFlightAssignmentId(flightAssignmentId);

		super.getBuffer().addData(activityLogs);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		Dataset dataset;

		dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel", "draftMode");

		int flightAssignmentId = super.getRequest().getData("flightAssignmentId", int.class);
		FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(flightAssignmentId);

		// Show create if the assignment is completed
		if (flightAssignment.getLeg().getScheduledArrival().before(MomentHelper.getCurrentMoment()))
			super.getResponse().addGlobal("showAction", true);

		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<ActivityLog> activityLog) {
		int flightAssignmentId = super.getRequest().getData("flightAssignmentId", int.class);
		FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(flightAssignmentId);

		// Show create if the assignment is completed
		if (flightAssignment.getLeg().getScheduledArrival().before(MomentHelper.getCurrentMoment()))
			super.getResponse().addGlobal("showAction", true);

		super.getResponse().addGlobal("flightAssignmentId", flightAssignmentId);
	}

}
