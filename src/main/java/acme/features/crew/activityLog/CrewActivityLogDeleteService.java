
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
public class CrewActivityLogDeleteService extends AbstractGuiService<FlightCrewMembers, ActivityLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CrewActivityLogRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", int.class);
		ActivityLog activityLog = this.repository.findActivityLogById(id);

		boolean status = false;

		if (activityLog != null && activityLog.getDraftMode()) {
			int activeUserId = super.getRequest().getPrincipal().getActiveRealm().getId();
			boolean userOwnsActivityLog = activityLog.getFlightAssignment().getFlightCrewMember().getId() == activeUserId;

			Object assignmentData = super.getRequest().getData().get("flightAssignment");
			Object activityLogIdData = super.getRequest().getData().get("id");

			boolean assignmentIsValid = false;
			boolean idIsValid = false;

			if (assignmentData == null)
				assignmentIsValid = true;
			else if (assignmentData instanceof String assignmentKey) {
				assignmentKey = assignmentKey.trim();

				if (!assignmentKey.isEmpty())
					if (assignmentKey.equals("0"))
						assignmentIsValid = true;
					else if (assignmentKey.matches("\\d+")) {
						int assignmentId = Integer.parseInt(assignmentKey);
						FlightAssignment assignment = this.repository.findFlightAssignmentById(assignmentId);
						assignmentIsValid = assignment != null && this.repository.findAllFlightAssignments().contains(assignment);
					}
			}
			if (activityLogIdData == null)
				idIsValid = true;
			else if (activityLogIdData instanceof String idKey) {
				idKey = idKey.trim();

				if (!idKey.isEmpty() && idKey.matches("\\d+"))
					idIsValid = true;
			}

			status = userOwnsActivityLog && assignmentIsValid && idIsValid;
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		ActivityLog activityLog = this.repository.findActivityLogById(id);

		super.getBuffer().addData(activityLog);
	}

	@Override
	public void bind(final ActivityLog activityLog) {
		super.bindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel", "flightAssignment");
	}

	@Override
	public void validate(final ActivityLog activityLog) {
		;
	}

	@Override
	public void perform(final ActivityLog activityLog) {
		this.repository.delete(activityLog);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		int memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Collection<FlightAssignment> assignments;
		SelectChoices choices;

		assignments = this.repository.findFlightAssignmentsByCrewMember(memberId);

		choices = SelectChoices.from(assignments, "moment", activityLog.getFlightAssignment());

		Dataset dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel", "flightAssignment");
		dataset.put("flightAssignment", choices.getSelected().getKey());
		dataset.put("assignments", choices);

		super.getResponse().addData(dataset);
	}

}
