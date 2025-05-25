
package acme.features.customer.bookings;

import java.util.Collection;

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

	@Query("SELECT f FROM Flight f WHERE f.draftMode = FALSE")
	Collection<Flight> findAllFlights();

	@Query("SELECT c FROM Customer c WHERE c.id = :id")
	Customer findOneCustomerById(int id);

	@Query("SELECT b FROM Booking b WHERE b.locatorCode = :locatorCode")
	Booking findBookingByLocatorCode(String locatorCode);

	@Query("SELECT f FROM Flight f WHERE f.id = :flightId AND f.draftMode = FALSE")
	Flight findOneFlightPublishedById(int flightId);

	@Query("SELECT COUNT(*) FROM BookingRecord br WHERE br.booking.id = :id")
	Integer countPassengersAssociatedToBooking(int id);

	@Query("SELECT br FROM BookingRecord br WHERE br.booking.id = :id")
	Collection<BookingRecord> findAllBookingRecordsByBookingId(int id);
}
