
package acme.features.assistanceagent.trackinglog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.tracking_logs.TrackingLog;
import acme.realms.assistanceAgent.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogListService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		int claimId;
		int agentId;
		Claim claim;
		boolean status;

		claimId = super.getRequest().getData("claimId", int.class);
		agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		claim = this.repository.findClaimById(claimId);
		status = claim != null && claim.getAssistanceAgent().getId() == agentId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<TrackingLog> trackingLogs;
		int claimId;
		claimId = super.getRequest().getData("claimId", int.class);
		Claim claim = this.repository.findClaimById(claimId);
		super.state(claim != null, "*", "assistance-agent.tracking-log.error.non-existent-claim");

		trackingLogs = this.repository.findAllTrackingLogsByClaimId(claimId);

		super.getBuffer().addData(trackingLogs);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		Dataset dataset;

		dataset = super.unbindObject(trackingLog, "lastUpdateMoment", "resolutionPercentage", "status");
		super.addPayload(dataset, trackingLog, "step", "resolution", "published");

		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<TrackingLog> trackingLogs) {
		int claimId;
		Claim claim;
		final boolean showCreate;

		claimId = super.getRequest().getData("claimId", int.class);
		claim = this.repository.findClaimById(claimId);
		showCreate = !claim.isTransient() && super.getRequest().getPrincipal().hasRealm(claim.getAssistanceAgent());

		super.getResponse().addGlobal("claimId", claimId);
		super.getResponse().addGlobal("showCreate", showCreate);
	}

}
