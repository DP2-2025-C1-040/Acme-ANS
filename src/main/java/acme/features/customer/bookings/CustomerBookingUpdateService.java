
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
public class CustomerBookingUpdateService extends AbstractGuiService<Customer, Booking> {

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
		Booking Booking;
		int id;

		id = super.getRequest().getData("id", int.class);
		Booking = this.repository.findBookingById(id);

		super.getBuffer().addData(Booking);
	}

	@Override
	public void bind(final Booking Booking) {
		super.bindObject(Booking, "locatorCode", "purchaseMoment", "travelClass", "price", "lastNibble", "flight");
	}

	@Override
	public void validate(final Booking Booking) {
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
