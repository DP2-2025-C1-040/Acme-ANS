
package acme.features.crew.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activity_log.ActivityLog;
import acme.realms.crew.FlightCrewMembers;

@GuiService
public class CrewActivityLogDeleteService extends AbstractGuiService<FlightCrewMembers, ActivityLog> {

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
		int id = super.getRequest().getData("id", int.class);
		ActivityLog activityLog = this.repository.findActivityLogById(id);

		super.getBuffer().addData(activityLog);
	}

	@Override
	public void bind(final ActivityLog activityLog) {
		super.bindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel");
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

		Dataset dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel", "draftMode");

		if (activityLog.getFlightAssignment().getLeg().getScheduledArrival().before(MomentHelper.getCurrentMoment()))
			super.getResponse().addGlobal("showAction", true);

		super.getResponse().addData(dataset);
	}

}
