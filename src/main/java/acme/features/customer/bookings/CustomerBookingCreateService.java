
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
public class CustomerBookingCreateService extends AbstractGuiService<Customer, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int flightId;
		Flight flight;

		if (super.getRequest().getMethod().equals("GET"))
			status = true;
		else {
			flightId = super.getRequest().getData("flight", int.class);
			flight = this.repository.findOneFlightPublishedById(flightId);
			status = flightId == 0 || flight != null;
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Booking booking;
		Customer customer;

		customer = this.repository.findOneCustomerById(super.getRequest().getPrincipal().getActiveRealm().getId());

		booking = new Booking();
		booking.setDraftMode(true);
		booking.setCustomer(customer);
		booking.setPurchaseMoment(MomentHelper.getCurrentMoment());
		super.getBuffer().addData(booking);

	}

	@Override
	public void bind(final Booking booking) {
		super.bindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "lastNibble", "flight");
	}

	@Override
	public void validate(final Booking booking) {

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
		Date now;

		now = MomentHelper.getCurrentMoment();
		flights = this.repository.findAllFlights(now);

		choicesFlights = SelectChoices.from(flights, "tag", booking.getFlight());
		choicesTravelClasses = SelectChoices.from(TravelClassEnum.class, booking.getTravelClass());

		Dataset dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "lastNibble", "draftMode", "flight");
		dataset.put("travelClasses", choicesTravelClasses);
		dataset.put("flights", choicesFlights);
		dataset.put("price", booking.getPrice());

		super.getResponse().addData(dataset);
	}
}
