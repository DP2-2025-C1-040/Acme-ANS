
package acme.features.assistanceagent.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.claims.ClaimStatus;
import acme.realms.assistanceAgent.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimListPendingService extends AbstractGuiService<AssistanceAgent, Claim> {

	//Internal state ---------------------------------------------

	@Autowired
	private AssistanceAgentClaimRepository repository;

	//AbstractGuiService interface -------------------------------


	@Override
	public void authorise() {
		Claim claim;
		int claimId;
		int agentId;
		boolean status;

		claimId = super.getRequest().getData("claimId", int.class);
		claim = this.repository.findClaimById(claimId);
		agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		status = claim != null && !claim.isTransient() && claim.getAssistanceAgent().getId() == agentId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Claim> claims;
		int assistaceAgentId;

		assistaceAgentId = super.getRequest().getPrincipal().getActiveRealm().getId();

		claims = this.repository.findAllClaimsByAssistanceAgentId(assistaceAgentId);

		super.getBuffer().addData(claims);

	}

	@Override
	public void unbind(final Claim claim) {

		Dataset dataset;
		ClaimStatus status;

		Boolean published = claim.getPublished();
		status = claim.getStatus();

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "type");
		dataset.put("status", status);
		super.addPayload(dataset, claim, "registrationMoment", "description");

		if (published == false && status == ClaimStatus.PENDING)
			super.getResponse().addData(dataset);

	}

}
