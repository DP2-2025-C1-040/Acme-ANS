
package acme.features.assistanceagent.claim;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.claims.ClaimType;
import acme.entities.leg.Leg;
import acme.realms.assistanceAgent.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimUpdateService extends AbstractGuiService<AssistanceAgent, Claim> {

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

		claimId = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimById(claimId);
		agentId = claim == null ? null : super.getRequest().getPrincipal().getActiveRealm().getId();
		status = claim != null && !claim.getPublished() && claim.getAssistanceAgent().getId() == agentId;

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

	@Override
	public void bind(final Claim claim) {
		super.bindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "leg", "status");
	}

	//Validar que los atributos de entrada cumplen requisitos
	@Override
	public void validate(final Claim claim) {
		;
	}

	@Override
	public void perform(final Claim claim) {
		this.repository.save(claim);
	}

	//el bug temporal esta aqui tambien 
	@Override
	public void unbind(final Claim claim) {
		Collection<Leg> legs;
		SelectChoices typesChoices;
		SelectChoices legsChoices;
		Dataset dataset;
		Date actualMoment;

		actualMoment = MomentHelper.getCurrentMoment();

		typesChoices = SelectChoices.from(ClaimType.class, claim.getType());
		//legs = this.repository.findAllPublishedLegsBefore(actualMoment);
		//legs = this.repository.findAllPublishedLegs();
		legs = this.repository.findAllLeg();
		legsChoices = SelectChoices.from(legs, "flightNumber", claim.getLeg());

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "published");
		dataset.put("types", typesChoices);
		dataset.put("leg", legsChoices.getSelected().getKey());
		dataset.put("legs", legsChoices);
		dataset.put("status", claim.getStatus());

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
