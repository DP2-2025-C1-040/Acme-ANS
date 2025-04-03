
package acme.features.assistanceagent.claim;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
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


	//revisar
	@Override
	public void authorise() {
		Claim claim;
		int claimId;
		int agentId;
		boolean status;

		claimId = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimById(claimId);
		agentId = super.getRequest().getPrincipal().getActiveRealm().getId();

		status = claim != null && claim.getAssistanceAgent().getId() == agentId;

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Claim claim;
		int claimId;

		claimId = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimById(claimId);

		super.getBuffer().addData(claim);

	}

	//Revisar este metodo ya que hay cosas inconclusa 
	//CUANDO SE ARREGLE EL BUG TEMPORAL DE LAS LEGS SE USARA LA LINEA COMENTADA
	@Override
	public void unbind(final Claim claim) {

		Dataset dataset;
		ClaimStatus indicator;
		Collection<Leg> legs;
		SelectChoices typesChoices;
		SelectChoices legsChoices;

		Date actualMoment;

		actualMoment = MomentHelper.getCurrentMoment();

		indicator = claim.getStatus();
		typesChoices = SelectChoices.from(ClaimType.class, claim.getType());
		legs = this.repository.findAllLeg();
		legsChoices = SelectChoices.from(legs, "flightNumber", claim.getLeg());

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "status", "published");
		dataset.put("types", typesChoices);
		dataset.put("leg", legsChoices.getSelected().getKey());
		dataset.put("legs", legsChoices);

		super.getResponse().addData(dataset);

	}
}
