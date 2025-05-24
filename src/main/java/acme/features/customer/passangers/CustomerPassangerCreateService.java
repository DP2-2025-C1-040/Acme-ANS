
package acme.features.customer.passangers;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.passengers.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassangerCreateService extends AbstractGuiService<Customer, Passenger> {

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
		Passenger passenger = new Passenger();
		super.getBuffer().addData(passenger);
	}

	@Override
	public void bind(final Passenger passenger) {
		Customer customer = this.repository.findCustomerById(super.getRequest().getPrincipal().getAccountId());

		super.bindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirdth", "specialNeeds");
		passenger.setCustomer(customer);
	}

	@Override
	public void validate(final Passenger passenger) {

	}

	@Override
	public void perform(final Passenger Passenger) {
		this.repository.save(Passenger);
	}

	@Override
	public void unbind(final Passenger Passenger) {

		Dataset dataset = super.unbindObject(Passenger, "fullName", "email", "passportNumber", "dateOfBirdth", "specialNeeds", "draftMode");

		super.getResponse().addData(dataset);
	}
}
