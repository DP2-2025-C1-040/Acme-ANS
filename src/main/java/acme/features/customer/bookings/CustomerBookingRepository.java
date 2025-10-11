
package acme.features.customer.bookings;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.flight.Flight;
import acme.entities.passengers.BookingRecord;
import acme.realms.Customer;

@Repository
public interface CustomerBookingRepository extends AbstractRepository {

	@Query("SELECT b FROM Booking b WHERE b.customer.id = :id")
	Collection<Booking> findAllMyBookings(int id);

	@Query("SELECT b FROM Booking b WHERE b.id = :id")
	Booking findBookingById(int id);

	@Query("SELECT f FROM Flight f WHERE f.draftMode = FALSE AND not exists(SELECT l FROM Leg l WHERE l.flight.id = f.id and ((l.draftMode = true) or (:now > l.scheduledDeparture)))")
	Collection<Flight> findAllFlights(Date now);

	@Query("SELECT c FROM Customer c WHERE c.id = :id")
	Customer findOneCustomerById(int id);

	@Query("SELECT b FROM Booking b WHERE b.locatorCode = :locatorCode")
	Booking findBookingByLocatorCode(String locatorCode);

	@Query("SELECT f FROM Flight f WHERE f.id = :flightId AND f.draftMode = FALSE AND not exists(SELECT l FROM Leg l WHERE l.flight.id = f.id and ((l.draftMode = true) or (:now > l.scheduledDeparture)))")
	Flight findOneFlightPublishedById(int flightId, Date now);

	@Query("SELECT COUNT(*) FROM BookingRecord br WHERE br.booking.id = :id")
	Integer countPassengersAssociatedToBooking(int id);

	@Query("SELECT br FROM BookingRecord br WHERE br.booking.id = :id")
	Collection<BookingRecord> findAllBookingRecordsByBookingId(int id);

	@Query("SELECT COUNT(*) = 0 FROM BookingRecord br WHERE br.booking.id = :bookingId AND br.passenger.draftMode = TRUE")
	boolean countPassengersInDraftModeFor(int bookingId);

	@Query("SELECT COUNT(*) > 0 FROM BookingRecord br WHERE br.booking.id = :bookingId")
	boolean existsPassengersFor(int bookingId);
}
