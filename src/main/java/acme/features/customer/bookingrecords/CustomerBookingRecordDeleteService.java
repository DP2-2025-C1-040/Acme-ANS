
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
public class CustomerBookingRecordDeleteService extends AbstractGuiService<Customer, BookingRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRecordRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int bookingId;
		int passengerId;
		Booking booking;
		Passenger passenger;

		// Comprueba pertenencia y si está en draftMode
		bookingId = super.getRequest().getData("bookingId", int.class);
		booking = this.repository.findBookingById(bookingId);
		status = booking != null && super.getRequest().getPrincipal().hasRealm(booking.getCustomer()) && booking.getDraftMode();

		if (super.getRequest().hasData("passenger")) {
			passengerId = super.getRequest().getData("passenger", int.class);
			passenger = this.repository.findPassengerById(passengerId);
			status = status && passenger != null && super.getRequest().getPrincipal().hasRealm(passenger.getCustomer()) && !this.repository.notExistsThisPassengerAndBooking(bookingId, passengerId);
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
		Passenger passenger;
		int passengerId;

		passengerId = super.getRequest().getData("passenger", int.class);
		passenger = this.repository.findPassengerById(passengerId);

		bookingRecord.setPassenger(passenger);
	}

	@Override
	public void validate(final BookingRecord bookingRecord) {

	}

	@Override
	public void perform(final BookingRecord bookingRecord) {
		BookingRecord toDelete;

		toDelete = this.repository.findByBookingIdAndPassengerId(bookingRecord.getBooking().getId(), bookingRecord.getPassenger().getId());
		this.repository.delete(toDelete);
	}

	@Override
	public void unbind(final BookingRecord bookingRecord) {
		Collection<Passenger> passengers;
		SelectChoices choices;
		Dataset dataset;
		int bookingId;

		bookingId = super.getRequest().getData("bookingId", int.class);
		passengers = this.repository.findAllPassengersInBookingId(bookingId);
		choices = SelectChoices.from(passengers, "fullName", bookingRecord.getPassenger());

		dataset = super.unbindObject(bookingRecord, "passenger");
		dataset.put("passengers", choices);
		dataset.put("tag", bookingRecord.getBooking().getFlight().getTag());
		dataset.put("locatorCode", bookingRecord.getBooking().getLocatorCode());
		dataset.put("bookingId", bookingId);

		super.getResponse().addData(dataset);
	}
}
