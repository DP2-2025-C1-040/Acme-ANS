
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

		boolean isOwner = activityLog != null && super.getRequest().getPrincipal().hasRealm(activityLog.getFlightAssignment().getFlightCrewMember());

		boolean isPublished = activityLog != null && !activityLog.getDraftMode();

		boolean status = isOwner || isPublished;

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

		assignments = this.repository.findFlightAssignmentsByCrewMember(memberId);

		FlightAssignment currentAssignment = activityLog.getFlightAssignment();
		if (currentAssignment != null && assignments.stream().noneMatch(a -> a.getId() == currentAssignment.getId()))
			assignments.add(currentAssignment);

		SelectChoices choices = new SelectChoices();

		for (FlightAssignment assignment : assignments) {
			String key = Integer.toString(assignment.getId());
			String label = assignment.getMoment() + " - " + assignment.getDuty() + " - " + assignment.getCurrentStatus() + " - " + assignment.getLeg().getFlightNumber();
			boolean isSelected = assignment.equals(currentAssignment);

			choices.add(key, label, isSelected);
		}

		choices.add("0", "----", false);

		Dataset dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel", "flightAssignment", "draftMode");

		if (choices.getSelected() != null)
			dataset.put("flightAssignment", choices.getSelected().getKey());
		else
			dataset.put("flightAssignment", "0");

		dataset.put("assignments", choices);

		super.getResponse().addData(dataset);
	}

}
