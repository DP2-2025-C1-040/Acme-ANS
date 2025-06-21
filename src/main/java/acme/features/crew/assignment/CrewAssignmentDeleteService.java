
package acme.features.crew.assignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activity_log.ActivityLog;
import acme.entities.assignment.CurrentStatus;
import acme.entities.assignment.Duty;
import acme.entities.assignment.FlightAssignment;
import acme.entities.leg.Leg;
import acme.realms.crew.FlightCrewMembers;

@GuiService
public class CrewAssignmentDeleteService extends AbstractGuiService<FlightCrewMembers, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CrewAssignmentRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", int.class);
		FlightAssignment assignment = this.repository.findFlightAssignmentById(id);

		boolean status = false;

		if (assignment != null && assignment.getDraftMode()) {
			int activeUserId = super.getRequest().getPrincipal().getActiveRealm().getId();
			boolean userOwnsAssignment = assignment.getFlightCrewMember().getId() == activeUserId;

			Object legData = super.getRequest().getData().get("leg");
			Object assignmentIdData = super.getRequest().getData().get("id");

			boolean legIsValid = false;
			boolean idIsValid = false;

			if (legData == null)
				legIsValid = true;
			else if (legData instanceof String legKey) {
				legKey = legKey.trim();

				if (!legKey.isEmpty())
					if (legKey.equals("0"))
						legIsValid = true;
					else if (legKey.matches("\\d+")) {
						int legId = Integer.parseInt(legKey);
						Leg leg = this.repository.findLegById(legId);
						legIsValid = leg != null;
					}
			}
			if (assignmentIdData == null)
				idIsValid = true;
			else if (assignmentIdData instanceof String idKey) {
				idKey = idKey.trim();
				if (!idKey.isEmpty() && idKey.matches("\\d+"))
					idIsValid = true;
			}
			status = userOwnsAssignment && legIsValid && idIsValid;
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightAssignment assignment;
		int id;

		id = super.getRequest().getData("id", int.class);
		assignment = this.repository.findFlightAssignmentById(id);

		super.getBuffer().addData(assignment);
	}

	@Override
	public void bind(final FlightAssignment assignment) {
		super.bindObject(assignment, "duty", "currentStatus", "remarks", "leg");
	}

	@Override
	public void validate(final FlightAssignment assignment) {
		;
	}

	@Override
	public void perform(final FlightAssignment assignment) {
		Collection<ActivityLog> activityLogs;
		activityLogs = this.repository.findActivityLogsByFlightAssignmentId(assignment.getId());
		this.repository.deleteAll(activityLogs);
		this.repository.delete(assignment);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Collection<Leg> legs;
		SelectChoices choices = new SelectChoices();
		SelectChoices duties;
		SelectChoices statuses;

		legs = this.repository.findUpcomingPublishedLegs(MomentHelper.getCurrentMoment());

		for (Leg leg : legs) {
			String key = Integer.toString(leg.getId());
			String label = leg.getFlightNumber() + " - " + leg.getOriginCity() + " - " + leg.getDestinationCity() + " - " + leg.getFlight().getTag();
			boolean isSelected = leg.equals(assignment.getLeg());

			choices.add(key, label, isSelected);
		}

		duties = SelectChoices.from(Duty.class, assignment.getDuty());
		statuses = SelectChoices.from(CurrentStatus.class, assignment.getCurrentStatus());

		Dataset dataset = super.unbindObject(assignment, "duty", "moment", "currentStatus", "remarks", "leg", "draftMode");
		dataset.put("leg", choices.getSelected().getKey());
		dataset.put("legs", choices);
		dataset.put("duties", duties);
		dataset.put("statuses", statuses);

		super.getResponse().addData(dataset);
	}

}
