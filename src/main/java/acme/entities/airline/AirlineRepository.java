
package acme.entities.airline;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface AirlineRepository extends AbstractRepository {

	@Query("SELECT COUNT(a) > 0 FROM Airline a WHERE a.iataCode = :iataCode AND a.id <> :id")
	boolean existsByIataCodeAndIdNot(String iataCode, int id);

}
