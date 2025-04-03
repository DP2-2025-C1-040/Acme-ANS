
package acme.features.customer.bookings;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.TravelClassEnum;
import acme.entities.flight.Flight;
import acme.realms.Customer;

@GuiService
public class CustomerBookingCreateService extends AbstractGuiService<Customer, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Booking booking = new Booking();
		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		super.bindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "price", "lastNibble", "flight");
	}

	@Override
	public void validate(final Booking booking) {
		{
			boolean confirmation;

			confirmation = super.getRequest().getData("confirmation", boolean.class);
			super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
		}
	}

	@Override
	public void perform(final Booking Booking) {
		this.repository.save(Booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Collection<Flight> flights;
		SelectChoices choicesTravelClasses;
		SelectChoices choicesFlights;

		flights = this.repository.findAllFlights();

		choicesFlights = SelectChoices.from(flights, "tag", booking.getFlight());
		choicesTravelClasses = SelectChoices.from(TravelClassEnum.class, booking.getTravelClass());

		Dataset dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "price", "lastNibble", "draftMode", "flight");
		dataset.put("confirmation", false);
		dataset.put("travelClasses", choicesTravelClasses);
		dataset.put("flights", choicesFlights);

		super.getResponse().addData(dataset);
	}
}
