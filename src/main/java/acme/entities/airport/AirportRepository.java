
package acme.entities.airport;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface AirportRepository extends AbstractRepository {

	@Query("SELECT COUNT(a) > 0 FROM Airport a WHERE a.iataCode = :iataCode AND a.id <> :id")
	boolean existsByIataCodeAndIdNot(String iataCode, int id);

}
