
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

@Repository
public interface CrewAssignmentRepository extends AbstractRepository {

	@Query("SELECT f FROM FlightAssignment f WHERE f.flightCrewMember.id = :id")
	Collection<FlightAssignment> findAllAssignmentsByMemberId(int id);

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.flightCrewMember.id = :memberId AND fa.leg.scheduledArrival < :moment")
	Collection<FlightAssignment> findAllAssignmentsByMemberIdBeforeNow(int memberId, Date moment);

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.flightCrewMember.id = :memberId AND fa.leg.scheduledDeparture > :moment")
	Collection<FlightAssignment> findAllAssignmentsByMemberIdAfterNow(int memberId, Date moment);

	@Query("SELECT f FROM FlightAssignment f WHERE f.id = :id")
	FlightAssignment findFlightAssignmentById(int id);

	@Query("SELECT l FROM Leg l WHERE l.draftMode = false AND l.scheduledDeparture > :currentMoment AND l.airline.id = :airlineId AND l.flight.draftMode = false")
	Collection<Leg> findUpcomingPublishedLegs(Date currentMoment, int airlineId);

	@Query("SELECT l FROM Leg l WHERE l.id = :id")
	Leg findLegById(int id);

	@Query("SELECT a FROM ActivityLog a WHERE a.flightAssignment.id = :id")
	Collection<ActivityLog> findActivityLogsByFlightAssignmentId(int id);

	@Query("SELECT COUNT(fa) FROM FlightAssignment fa WHERE fa.leg = :leg AND fa.duty = :duty AND fa.id <> :excludedId")
	long countByLegAndDuty(Leg leg, Duty duty, int excludedId);

}
