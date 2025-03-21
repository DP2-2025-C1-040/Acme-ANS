
package acme.realms.crew;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface FlightCrewMemberRepository extends AbstractRepository {

	@Query("SELECT COUNT(f) > 0 FROM FlightCrewMembers f WHERE f.employeeCode = :employeeCode AND f.id <> :id")
	boolean existsByEmployeeCodeAndIdNot(String employeeCode, int id);

}
