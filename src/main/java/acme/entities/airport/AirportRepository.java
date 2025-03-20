
package acme.entities.airport;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface AirportRepository extends AbstractRepository {

	@Query("SELECT COUNT(a) > 0 FROM Airport a WHERE a.iataCode = :iataCode")
	boolean existsByIataCode(@Param("iataCode") String iataCode);

}
