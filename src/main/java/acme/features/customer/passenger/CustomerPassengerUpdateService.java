
package acme.features.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.passengers.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerPassengerUpdateService extends AbstractGuiService<Customer, Passenger> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerPassengerRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int passengerId;
		Passenger passenger;

		// Comprueba pertenencia y si está en draftMode
		passengerId = super.getRequest().getData("id", int.class);
		passenger = this.repository.findPassengerById(passengerId);
		status = passenger != null && super.getRequest().getPrincipal().hasRealm(passenger.getCustomer()) && passenger.getDraftMode();

		super.getResponse().setAuthorised(status);
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
