
package acme.features.customer.passenger;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.passengers.BookingRecord;
import acme.entities.passengers.Passenger;
import acme.realms.Customer;

@Repository
public interface CustomerPassengerRepository extends AbstractRepository {

	@Query("SELECT p FROM Passenger p WHERE p.id = :id")
	Passenger findPassengerById(int id);

	@Query("SELECT p FROM Passenger p WHERE p.customer.id = :id")
	Collection<Passenger> findAllMyPassengers(int id);

	@Query("SELECT c FROM Customer c WHERE c.id = :id")
	Customer findCustomerById(int id);

	@Query("SELECT c FROM Customer c WHERE c.id = :id")
	Customer findOneCustomerById(int id);

	@Query("SELECT b FROM Booking b WHERE b.id = :id")
	Booking findBookingById(int id);

	@Query("SELECT br.passenger FROM BookingRecord br WHERE br.booking.id = :id")
	Collection<Passenger> findAllPassengersByBookingId(int id);

	@Query("SELECT br FROM BookingRecord br WHERE br.passenger.id = :id")
	Collection<BookingRecord> findAllBookingRecordsByPassengerId(int id);

	@Query("SELECT p FROM Passenger p WHERE p.passportNumber = :passportNumber AND p.customer.id = :customerId")
	Passenger findPassengerByPassportNumber(String passportNumber, int customerId);
}
