
package acme.features.crew.activityLog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.activity_log.ActivityLog;
import acme.entities.assignment.FlightAssignment;

@Repository
public interface CrewActivityLogRepository extends AbstractRepository {

	@Query("SELECT a FROM ActivityLog a WHERE a.flightAssignment.id = :id")
	Collection<ActivityLog> findAllActivityLogsByFlightAssignmentId(int id);

	@Query("SELECT a FROM ActivityLog a WHERE a.id = :id")
	ActivityLog findActivityLogById(int id);

	@Query("SELECT f FROM FlightAssignment f WHERE f.flightCrewMember.id = :crewMemberId")
	Collection<FlightAssignment> findFlightAssignmentsByCrewMember(int crewMemberId);

	@Query("SELECT f FROM FlightAssignment f WHERE f.id = :id")
	FlightAssignment findFlightAssignmentById(int id);

}
