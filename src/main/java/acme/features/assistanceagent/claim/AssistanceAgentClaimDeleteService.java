
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
import acme.entities.claims.ClaimType;
import acme.entities.leg.Leg;
import acme.entities.tracking_logs.TrackingLog;
import acme.realms.assistanceAgent.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimDeleteService extends AbstractGuiService<AssistanceAgent, Claim> {

	//Internal state ---------------------------------------------

	@Autowired
	private AssistanceAgentClaimRepository repository;

	//AbstractGuiService interface -------------------------------


	@Override
	public void authorise() {
		Claim claim;
		int claimId;
		AssistanceAgent assistanceAgent;
		int agentId;
		boolean status;

		claimId = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimById(claimId);

		if (claim != null) {
			assistanceAgent = claim.getAssistanceAgent();
			agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
			status = !claim.getPublished() && assistanceAgent != null && assistanceAgent.getId() == agentId;
		} else
			status = false;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Claim claim;
		int id;

		id = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimById(id);

		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim claim) {
		super.bindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "leg");
	}

	//Si esta publicado no se puede borrar
	//si tiene algun tracking log publicado no se puede borrar
	@Override
	public void validate(final Claim claim) {
		boolean claimNotPublished;
		boolean trackingLogNotPublished;
		Collection<TrackingLog> claimsTrackingLogs;

		claimsTrackingLogs = this.repository.findTrackingLogsByClaimId(claim.getId());

		trackingLogNotPublished = claimsTrackingLogs.stream().noneMatch(t -> t.published);
		claimNotPublished = !claim.getPublished();

		super.state(claimNotPublished, "published", "acme.validation.claim.delete-when-published");
		super.state(trackingLogNotPublished, "*", "acme.validation.claim.delete-trackinglogs-published");
	}

	@Override
	public void perform(final Claim claim) {
		Collection<TrackingLog> trackingLogs;

		trackingLogs = this.repository.findTrackingLogsByClaimId(claim.getId());
		this.repository.deleteAll(trackingLogs);
		this.repository.delete(claim);
	}

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

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "publish");
		dataset.put("types", typesChoices);
		dataset.put("leg", legsChoices.getSelected().getKey());
		dataset.put("legs", legsChoices);

		super.getResponse().addData(dataset);

	}
}
