
package acme.features.administrator.airline;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airline.Airline;
import acme.entities.airline.AirlineType;

@GuiService
public class AdministratorAirlineUpdateService extends AbstractGuiService<Administrator, Airline> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorAirlineRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Airline Airline;
		int id;

		id = super.getRequest().getData("id", int.class);
		Airline = this.repository.findAirlineById(id);

		super.getBuffer().addData(Airline);
	}

	@Override
	public void bind(final Airline Airline) {
		super.bindObject(Airline, "name", "iataCode", "webSite", "foundationMoment", "emailAddress", "phoneNumber", "type");
	}

	@Override
	public void validate(final Airline Airline) {
		{
			boolean confirmation;

			confirmation = super.getRequest().getData("confirmation", boolean.class);
			super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
		}
	}

	@Override
	public void perform(final Airline Airline) {
		this.repository.save(Airline);
	}

	@Override
	public void unbind(final Airline Airline) {
		SelectChoices choices;
		Dataset dataset;

		choices = SelectChoices.from(AirlineType.class, Airline.getType());

		dataset = super.unbindObject(Airline, "name", "iataCode", "webSite", "foundationMoment", "emailAddress", "phoneNumber", "type");
		dataset.put("types", choices);

		super.getResponse().addData(dataset);
	}
}
