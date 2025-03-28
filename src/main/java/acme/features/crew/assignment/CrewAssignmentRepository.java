
package acme.features.crew.assignment;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.activity_log.ActivityLog;
import acme.entities.assignment.Duty;
import acme.entities.assignment.FlightAssignment;
import acme.entities.leg.Leg;
import acme.entities.leg.LegStatus;

@Repository
public interface CrewAssignmentRepository extends AbstractRepository {

	@Query("SELECT f FROM FlightAssignment f WHERE f.flightCrewMember.id = :id AND f.leg.status = :status")
	Collection<FlightAssignment> findAllAssignmentsByMemberIdAndStatus(int id, LegStatus status);

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.flightCrewMember.id = :memberId AND fa.leg.scheduledArrival < :moment")
	Collection<FlightAssignment> findAllAssignmentsByMemberIdBeforeNow(int memberId, Date moment);

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.flightCrewMember.id = :memberId AND fa.leg.scheduledDeparture > :moment")
	Collection<FlightAssignment> findAllAssignmentsByMemberIdAfterNow(int memberId, Date moment);

	@Query("SELECT f FROM FlightAssignment f WHERE f.id = :id")
	FlightAssignment findFlightAssignmentById(int id);

	@Query("SELECT l FROM Leg l")
	Collection<Leg> findAllLegs();

	@Query("SELECT a FROM ActivityLog a WHERE a.flightAssignment.id = :id")
	Collection<ActivityLog> findActivityLogsByFlightAssignmentId(int id);

	@Query("SELECT COUNT(fa) FROM FlightAssignment fa WHERE fa.leg = :leg AND fa.duty = :duty")
	long countByLegAndDuty(Leg leg, Duty duty);

}
