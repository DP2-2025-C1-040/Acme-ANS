
package acme.features.customer.bookings;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.flight.Flight;

@Repository
public interface CustomerBookingRepository extends AbstractRepository {

	@Query("SELECT b FROM Booking b WHERE b.customer.id = :id")
	Collection<Booking> findAllMyBookings(int id);

	@Query("SELECT b FROM Booking b WHERE b.id = :id")
	Booking findBookingById(int id);

	@Query("SELECT f FROM Flight f")
	Collection<Flight> findAllFlights();
}
