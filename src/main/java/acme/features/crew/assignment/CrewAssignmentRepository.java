
package acme.features.crew.assignment;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.assignment.FlightAssignment;
import acme.entities.leg.Leg;
import acme.entities.leg.LegStatus;

@Repository
public interface CrewAssignmentRepository extends AbstractRepository {

	@Query("SELECT f FROM FlightAssignment f WHERE f.flightCrewMember.id = :id AND f.leg.status = :status")
	Collection<FlightAssignment> findAllAssignmentsByMemberIdAndStatus(int id, LegStatus status);

	@Query("SELECT f FROM FlightAssignment f WHERE f.id = :id")
	FlightAssignment findFlightAssignmentById(int id);

	@Query("SELECT l FROM Leg l")
	Collection<Leg> findAllLegs();

}
