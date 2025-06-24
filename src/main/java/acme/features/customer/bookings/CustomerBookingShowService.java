
package acme.features.customer.bookings;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.TravelClassEnum;
import acme.entities.flight.Flight;
import acme.realms.Customer;

@GuiService
public class CustomerBookingShowService extends AbstractGuiService<Customer, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int bookingId;
		Booking booking;

		bookingId = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(bookingId);
		status = booking != null && super.getRequest().getPrincipal().hasRealm(booking.getCustomer());

		super.getResponse().setAuthorised(status);
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
	public void unbind(final Booking booking) {
		Collection<Flight> flights;
		SelectChoices choicesTravel;
		SelectChoices choicesFlights;
		Dataset dataset;
		Date now;

		now = MomentHelper.getCurrentMoment();
		flights = this.repository.findAllFlights(now);

		choicesFlights = SelectChoices.from(flights, "tag", booking.getFlight());
		choicesTravel = SelectChoices.from(TravelClassEnum.class, booking.getTravelClass());

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "lastNibble", "draftMode", "flight");
		dataset.put("travelClasses", choicesTravel);
		dataset.put("flights", choicesFlights);
		dataset.put("price", booking.getPrice());

		super.getResponse().addData(dataset);
	}
}
