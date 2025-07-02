
package acme.features.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.passengers.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerCreateService extends AbstractGuiService<Customer, Passenger> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerPassengerRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int passengerId;

		if (super.getRequest().getMethod().equals("GET"))
			status = true;
		else {
			passengerId = super.getRequest().getData("id", int.class);
			status = passengerId == 0;
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Passenger passenger;
		Customer customer;

		customer = this.repository.findOneCustomerById(super.getRequest().getPrincipal().getActiveRealm().getId());

		passenger = new Passenger();
		passenger.setDraftMode(true);
		passenger.setCustomer(customer);
		super.getBuffer().addData(passenger);
	}

	@Override
	public void bind(final Passenger passenger) {
		super.bindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirdth", "specialNeeds");
	}

	@Override
	public void validate(final Passenger passenger) {

	}

	@Override
	public void perform(final Passenger passenger) {
		this.repository.save(passenger);
	}

	@Override
	public void unbind(final Passenger passenger) {

		Dataset dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirdth", "specialNeeds", "draftMode");

		super.getResponse().addData(dataset);
	}
}
