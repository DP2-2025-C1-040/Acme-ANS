
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
import acme.realms.crew.FlightCrewMembers;

@GuiService
public class CrewActivityLogUpdateService extends AbstractGuiService<FlightCrewMembers, ActivityLog> {

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
		super.bindObject(activityLog, "typeOfIncident", "description", "severityLevel", "flightAssignment");
	}

	@Override
	public void validate(final ActivityLog activityLog) {
		;
	}

	@Override
	public void perform(final ActivityLog activityLog) {
		activityLog.setRegistrationMoment(MomentHelper.getCurrentMoment());
		this.repository.save(activityLog);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		int memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Collection<FlightAssignment> assignments;
		SelectChoices choices = new SelectChoices();

		assignments = this.repository.findFlightAssignmentsByCrewMember(memberId);

		choices.add("0", "----", activityLog.getFlightAssignment() == null);

		for (FlightAssignment assignment : assignments) {
			String key = Integer.toString(assignment.getId());
			String label = assignment.getMoment() + " - " + assignment.getDuty() + " - " + assignment.getCurrentStatus() + " - " + assignment.getLeg().getFlightNumber();
			boolean isSelected = assignment.equals(activityLog.getFlightAssignment());

			choices.add(key, label, isSelected);
		}

		Dataset dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel", "flightAssignment");
		dataset.put("flightAssignment", activityLog.getFlightAssignment() != null ? choices.getSelected().getKey() : "0");
		dataset.put("assignments", choices);
		dataset.put("draftMode", activityLog.getDraftMode());

		super.getResponse().addData(dataset);
	}
}
