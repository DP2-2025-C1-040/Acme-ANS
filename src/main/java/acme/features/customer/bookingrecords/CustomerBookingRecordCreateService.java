
package acme.features.customer.bookingrecords;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.passengers.BookingRecord;
import acme.entities.passengers.Passenger;
import acme.realms.Customer;

@GuiService
public class CustomerBookingRecordCreateService extends AbstractGuiService<Customer, BookingRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRecordRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		// TODO - COMO EN UPDATE. Considerar la no repetición
		boolean status;
		int bookingId;
		int passengerId;
		Booking booking;
		Passenger passenger;

		// Comprueba pertenencia y si está en draftMode
		bookingId = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(bookingId);
		status = booking != null && super.getRequest().getPrincipal().hasRealm(booking.getCustomer()) && booking.getDraftMode();

		if (super.getRequest().hasData("passenger")) {
			passengerId = super.getRequest().getData("passenger", int.class);
			passenger = this.repository.findPassengerById(passengerId);
			status = status && passenger != null && super.getRequest().getPrincipal().hasRealm(passenger.getCustomer()) && this.repository.notExistsThisPassengerAndBooking(bookingId, passengerId);
		}
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		BookingRecord bookingRecord;
		Booking booking;
		int bookingId;

		bookingId = super.getRequest().getData("bookingId", int.class);
		booking = this.repository.findBookingById(bookingId);

		bookingRecord = new BookingRecord();
		bookingRecord.setBooking(booking);

		super.getBuffer().addData(bookingRecord);

	}

	@Override
	public void bind(final BookingRecord bookingRecord) {
		super.bindObject(bookingRecord, "passenger");
	}

	@Override
	public void validate(final BookingRecord bookingRecord) {

	}

	@Override
	public void perform(final BookingRecord BookingRecord) {
		this.repository.save(BookingRecord);
	}

	@Override
	public void unbind(final BookingRecord bookingRecord) {
		Collection<Passenger> passengers;
		SelectChoices choices;
		Dataset dataset;

		passengers = this.repository.findAllPassengersNotInBookingId(0);

		dataset = super.unbindObject(bookingRecord, "passenger", "booking");
		dataset.put(null, dataset);

		super.getResponse().addData(dataset);
	}
}
