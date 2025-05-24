
package acme.features.customer.passangers;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.passengers.Passenger;
import acme.realms.Customer;

@Repository
public interface CustomerPassangerRepository extends AbstractRepository {

	@Query("SELECT p FROM Passenger p WHERE p.id = :id")
	Passenger findPassengerById(int id);

	@Query("SELECT p FROM Passenger p WHERE p.customer.id = :id")
	Collection<Passenger> findAllMyPassengers(int id);

	@Query("SELECT c FROM Customer c WHERE c.id = :id")
	Customer findCustomerById(int id);

}
