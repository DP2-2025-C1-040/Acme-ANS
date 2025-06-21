
package acme.features.crew.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
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
		boolean status = false;
		String idRaw = super.getRequest().getData("id", String.class);
		int id = -1;

		// Validar que el ID sea un número entero positivo
		if (idRaw != null && idRaw.trim().matches("\\d+")) {
			id = Integer.parseInt(idRaw.trim());
			ActivityLog activityLog = this.repository.findActivityLogById(id);

			if (activityLog != null && activityLog.getDraftMode()) {
				int activeUserId = super.getRequest().getPrincipal().getActiveRealm().getId();
				boolean userOwnsActivityLog = activityLog.getFlightAssignment().getFlightCrewMember().getId() == activeUserId;
				status = userOwnsActivityLog;
			}
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ActivityLog activityLog = new ActivityLog();
		int flightAssignmentId;

		flightAssignmentId = super.getRequest().getData("flightAssignmentId", int.class);
		FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(flightAssignmentId);

		activityLog.setRegistrationMoment(MomentHelper.getCurrentMoment());
		activityLog.setFlightAssignment(flightAssignment);
		activityLog.setDraftMode(true);
		super.getBuffer().addData(activityLog);
	}

	@Override
	public void bind(final ActivityLog activityLog) {
		super.bindObject(activityLog, "typeOfIncident", "description", "severityLevel");
	}

	@Override
	public void validate(final ActivityLog activityLog) {
		if (activityLog.getFlightAssignment() != null && activityLog.getFlightAssignment().getLeg() != null) {
			Leg leg = activityLog.getFlightAssignment().getLeg();

			if (leg.getScheduledDeparture() != null) {
				boolean isLegLanded = MomentHelper.isBefore(leg.getScheduledDeparture(), MomentHelper.getCurrentMoment());

				super.state(isLegLanded, "*", "acme.validation.activityLog.leg.notLanded");
			}
		}
	}

	@Override
	public void perform(final ActivityLog activityLog) {
		this.repository.save(activityLog);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		Dataset dataset;

		dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel", "draftMode");

		dataset.put("flightAssignmentId", super.getRequest().getData("flightAssignmentId", int.class));

		super.getResponse().addData(dataset);
	}

}
