
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
public class AssistanceAgentClaimCreateService extends AbstractGuiService<AssistanceAgent, Claim> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentClaimRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);

	}

	@Override
	public void load() {
		Claim claim;
		AssistanceAgent agent = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();

		//El momento cogeremos el actual ficticio
		Date registrationMoment = MomentHelper.getCurrentMoment();

		claim = new Claim();
		claim.setRegistrationMoment(registrationMoment);

		//		claim.setPassengerEmail("");
		//		claim.setDescription("");
		claim.setAssistanceAgent(agent);
		//		claim.setType(ClaimType.FLIGHT_ISSUES);

		claim.setPublished(false);

		super.getBuffer().addData(claim);

	}

	@Override
	public void bind(final Claim claim) {
		super.bindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "leg");

	}

	@Override
	public void validate(final Claim claim) {
		;
	}

	@Override
	public void perform(final Claim claim) {
		Date registrationMoment;

		registrationMoment = MomentHelper.getCurrentMoment();
		claim.setRegistrationMoment(registrationMoment);

		claim.setPublished(false);

		this.repository.save(claim);
	}
	//El estado de una reclamación mientras está siendo creada tiene sentido que sea pending, 
	//Además esta no puede ser publicada porque no esta completada
	//¿Hacer una validación para que no sea publicada si no esta completada?
	@Override
	public void unbind(final Claim claim) {

		Dataset dataset;
		ClaimStatus status;
		SelectChoices typesChoices;
		SelectChoices legsChoices;

		Collection<Leg> legs;
		status = ClaimStatus.PENDING;

		typesChoices = SelectChoices.from(ClaimType.class, claim.getType());
		//legs = this.repository.findAllLegsBefore(actualMoment);  tiene sentido que sea este legs pero no parece ninguna leg
		legs = this.repository.findAllLeg();
		//legs = this.repository.findAllPublishedLegs();//Preguntar si se supone que todas las escalas estan publicadas
		legsChoices = SelectChoices.from(legs, "flightNumber", claim.getLeg());

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "leg");
		dataset.put("readonly", false);
		dataset.put("types", typesChoices);
		dataset.put("legs", legsChoices);
		dataset.put("status", status);
		super.getResponse().addData(dataset);

	}
}
