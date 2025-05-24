
package acme.features.customer.passangers;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.passengers.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassangerShowService extends AbstractGuiService<Customer, Passenger> {

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
		Passenger Passenger;
		int id;

		id = super.getRequest().getData("id", int.class);
		Passenger = this.repository.findPassengerById(id);

		super.getBuffer().addData(Passenger);
	}

	@Override
	public void unbind(final Passenger Passenger) {
		Dataset dataset;

		dataset = super.unbindObject(Passenger, "fullName", "email", "passportNumber", "dateOfBirdth", "specialNeeds", "draftMode");

		super.getResponse().addData(dataset);
	}
}
