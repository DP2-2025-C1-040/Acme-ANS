
package acme.features.customer.passangers;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.passengers.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassangerUpdateService extends AbstractGuiService<Customer, Passenger> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerPassangerRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Passenger passenger;
		int id;

		id = super.getRequest().getData("id", int.class);
		passenger = this.repository.findPassengerById(id);

		super.getBuffer().addData(passenger);
	}

	@Override
	public void bind(final Passenger passenger) {
		super.bindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirdth", "specialNeeds", "draftMode");
	}

	@Override
	public void validate(final Passenger passenger) {
		{
			boolean confirmation;

			confirmation = super.getRequest().getData("confirmation", boolean.class);
			super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
		}
	}

	@Override
	public void perform(final Passenger passenger) {
		this.repository.save(passenger);
	}

	@Override
	public void unbind(final Passenger Passenger) {
		Dataset dataset;

		dataset = super.unbindObject(Passenger, "fullName", "email", "passportNumber", "dateOfBirdth", "specialNeeds", "draftMode");

		super.getResponse().addData(dataset);
	}
}
