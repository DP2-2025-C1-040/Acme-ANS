
package acme.features.assistanceagent.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.claims.ClaimStatus;
import acme.entities.claims.ClaimType;
import acme.entities.leg.Leg;
import acme.realms.assistanceAgent.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimShowService extends AbstractGuiService<AssistanceAgent, Claim> {

	//Internal state ---------------------------------------------

	@Autowired
	private AssistanceAgentClaimRepository repository;

	//AbstractGuiService interface -------------------------------


	@Override
	public void authorise() {
		Claim claim;
		int claimId;
		int claimAgentId;
		int agentId;
		boolean owned;

		claimId = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimById(claimId);

		agentId = super.getRequest().getData("id", int.class);
		claimAgentId = claim.getAssistanceAgent().getUserAccount().getId();

		owned = claimAgentId == agentId;

		super.getResponse().setAuthorised(owned);

	}

	@Override
	public void load() {
		Claim claim;
		int claimId;
		Boolean published;

		claimId = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimById(claimId);
		published = claim.getPublished();

		if (published)
			super.getBuffer().addData(claim);

	}

	@Override
	public void unbind(final Claim claim) {

		Dataset dataset;
		ClaimStatus status;

		Collection<Leg> legs;

		SelectChoices choices;
		SelectChoices choices2;

		status = claim.getAccepted();
		choices = SelectChoices.from(ClaimType.class, claim.getType());
		legs = this.repository.findAllLeg();
		choices2 = SelectChoices.from(legs, "flightNumber", claim.getLeg());

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "publish");
		dataset.put("types", choices);
		dataset.put("leg", choices2.getSelected().getKey());
		dataset.put("legs", choices2);
		dataset.put("indicator", status);

		super.getResponse().addData(dataset);

	}

}
