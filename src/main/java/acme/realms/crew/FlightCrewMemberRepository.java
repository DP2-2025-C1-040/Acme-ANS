
package acme.realms.crew;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.assignment.Duty;
import acme.entities.leg.Leg;

@Repository
public interface FlightCrewMemberRepository extends AbstractRepository {

	@Query("SELECT COUNT(f) > 0 FROM FlightCrewMembers f WHERE f.employeeCode = :employeeCode AND f.id <> :id")
	boolean existsByEmployeeCodeAndIdNot(String employeeCode, int id);

	@Query("SELECT COUNT(fa) > 0 FROM FlightAssignment fa WHERE fa.flightCrewMember = :crewMember AND fa.duty = :duty")
	boolean existsByFlightCrewMemberAndDuty(FlightCrewMembers crewMember, Duty duty);

	@Query("SELECT f FROM FlightCrewMembers f WHERE f.id = :id")
	Optional<FlightCrewMembers> findById(int id);

	@Query("SELECT COUNT(f) FROM FlightAssignment f WHERE f.flightCrewMember = :flightCrewMember AND f.leg IS NOT NULL")
	long countByFlightCrewMemberAndLegNotNull(FlightCrewMembers flightCrewMember);

	@Query("SELECT COUNT(fa) FROM FlightAssignment fa WHERE fa.leg = :leg AND fa.duty = :duty")
	long countByLegAndDuty(Leg leg, Duty duty);

}
