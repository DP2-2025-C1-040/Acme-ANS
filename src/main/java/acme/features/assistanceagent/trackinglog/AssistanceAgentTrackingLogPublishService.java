
package acme.features.assistanceagent.trackinglog;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.StringHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.tracking_logs.TrackingLog;
import acme.entities.tracking_logs.TrackingLogStatus;
import acme.realms.assistanceAgent.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogPublishService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		TrackingLog trackingLog;
		int trackingLogId;
		int agentId;
		boolean status;

		trackingLogId = super.getRequest().getData("id", int.class);
		trackingLog = this.repository.findTrackingLogById(trackingLogId);
		agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		status = trackingLog != null && !trackingLog.isTransient() && trackingLog.getClaim().getAssistanceAgent().getId() == agentId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrackingLog trackingLog;
		int trackingLogId;

		trackingLogId = super.getRequest().getData("id", int.class);
		trackingLog = this.repository.findTrackingLogById(trackingLogId);

		super.getBuffer().addData(trackingLog);
	}

	@Override
	public void bind(final TrackingLog trackingLog) {
		super.bindObject(trackingLog, "lastUpdateMoment", "step", "resolutionPercentage", "status", "resolution");
	}

	@Override
	public void validate(final TrackingLog trackingLog) {
		boolean status;

		Claim claimStatus = trackingLog.getClaim();
		status = claimStatus != null && claimStatus.getPublished();

		super.state(status, "*", "acme.validation.tracking-log.unpublished.message");

		//		Claim claim;
		//		int claimId;
		//		claimId = super.getRequest().getData("claimId", int.class);
		//		claim = this.repository.findClaimById(claimId);

		Claim claim = trackingLog.getClaim();
		int claimId = claim.getId();

		Collection<TrackingLog> claimTrackingLogs = this.repository.findAllTrackingLogsByClaimId(claimId);

		if (!super.getBuffer().getErrors().hasErrors("indicator")) {
			boolean bool1;
			boolean bool2;

			if (!super.getBuffer().getErrors().hasErrors("resolutionPercentage")) {
				bool1 = trackingLog.getStatus() == TrackingLogStatus.PENDING && trackingLog.getResolutionPercentage() < 100;
				bool2 = trackingLog.getStatus() != TrackingLogStatus.PENDING && trackingLog.getResolutionPercentage() == 100;
				super.state(bool1 || bool2, "status", "assistanceAgent.tracking-log.form.error.indicator-pending");
			}

		}

		if (!super.getBuffer().getErrors().hasErrors("resolutionPercentage")) {

			Double maxResolutionPercentage;
			double finalMaxResolutionPercentage;

			maxResolutionPercentage = this.repository.findMaxResolutionPercentageByClaimId(trackingLog.getId(), trackingLog.getClaim().getId());
			finalMaxResolutionPercentage = maxResolutionPercentage != null ? maxResolutionPercentage : 0.0;

			super.state(trackingLog.getResolutionPercentage() >= finalMaxResolutionPercentage, "resolutionPercentage", "assistanceAgent.tracking-log.form.error.less-than-max-resolution-percentage");
		}

		if (!super.getBuffer().getErrors().hasErrors("lastUpdateMoment"))

			super.state(claim.getRegistrationMoment().before(trackingLog.getLastUpdateMoment()), "lastUpdateMoment", "assistanceAgent.tracking-log.form.error.date-not-valid");

		if (!claimTrackingLogs.isEmpty())
			if (!super.getBuffer().getErrors().hasErrors("lastUpdateMoment")) {

				Date maxLastUpdateMoment;

				maxLastUpdateMoment = this.repository.findMaxLastUpdateMomentByClaimId(trackingLog.getId(), trackingLog.getClaim().getId());

				super.state(maxLastUpdateMoment.before(trackingLog.getLastUpdateMoment()), "lastUpdateMoment", "assistanceAgent.tracking-log.form.error.last-moment-update-not-valid");
			}

		if (!super.getBuffer().getErrors().hasErrors("resolution")) {

			boolean requiresResolutionReason = trackingLog.getStatus() == TrackingLogStatus.ACCEPTED || trackingLog.getStatus() == TrackingLogStatus.REJECTED;
			boolean hasResolutionReason = !StringHelper.isBlank(trackingLog.getResolution());

			if (requiresResolutionReason)
				super.state(hasResolutionReason, "resolution", "assistanceAgent.tracking-log.form.error.resolution-required");
		}

		if (!super.getBuffer().getErrors().hasErrors("resolutionPercentage")) {

			Long countLogsWith100 = this.repository.countTrackingLogsForExceptionalCase(claimId);

			super.state(countLogsWith100 < 2, "resolutionPercentage", "assistanceAgent.tracking-log.form.error.message.completed");

		}

		if (!super.getBuffer().getErrors().hasErrors("lastUpdateMoment")) {
			Date maxLastUpdateMoment = this.repository.findMaxLastUpdateMomentByClaimId(trackingLog.getId(), trackingLog.getClaim().getId());

			if (maxLastUpdateMoment != null)
				super.state(!trackingLog.getLastUpdateMoment().before(maxLastUpdateMoment), "lastUpdateMoment", "assistanceAgent.tracking-log.form.error.last-log-moment-update-not-valid");
		}

	}

	@Override
	public void perform(final TrackingLog trackingLog) {
		trackingLog.setPublished(true);
		this.repository.save(trackingLog);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		SelectChoices statusChoices;
		Dataset dataset;

		statusChoices = SelectChoices.from(TrackingLogStatus.class, trackingLog.getStatus());

		dataset = super.unbindObject(trackingLog, "lastUpdateMoment", "step", "resolutionPercentage", "status", "resolution", "published");
		dataset.put("claimId", trackingLog.getClaim().getId());
		dataset.put("status", statusChoices);

		super.getResponse().addData(dataset);
	}

}
