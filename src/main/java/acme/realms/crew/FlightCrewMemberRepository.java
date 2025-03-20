
package acme.realms.crew;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface FlightCrewMemberRepository extends AbstractRepository {

	@Query("SELECT COUNT(fcm) > 0 FROM FlightCrewMembers fcm WHERE fcm.employeeCode = :employeeCode")
	boolean existsByEmployeeCode(String employeeCode);

}
