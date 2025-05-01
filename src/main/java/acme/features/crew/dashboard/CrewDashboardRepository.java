
package acme.features.crew.dashboard;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.realms.crew.FlightCrewMembers;

@Repository
public interface CrewDashboardRepository extends AbstractRepository {

	@Query("SELECT f.leg.destinationCity FROM FlightAssignment f WHERE f.flightCrewMember.id = :id ORDER BY f.leg.scheduledDeparture DESC")
	List<String> findLastFiveDestinationsByCrewMemberId(int id, Pageable pageable);

	@Query("SELECT CASE WHEN a.severityLevel BETWEEN 0 AND 3 THEN '0-3' WHEN a.severityLevel BETWEEN 4 AND 7 THEN '4-7' WHEN a.severityLevel BETWEEN 8 AND 10 THEN '8-10' END AS severityRange, COUNT(a) " + "FROM ActivityLog a "
		+ "WHERE a.flightAssignment.flightCrewMember.id = :id GROUP BY severityRange")
	List<Object[]> findLegsByIncidentSeverity(int id);

	@Query("SELECT DISTINCT f.flightCrewMember FROM FlightAssignment f WHERE f.leg.id = (SELECT MAX(l.id) FROM Leg l WHERE l.flight.id = f.leg.flight.id) ORDER BY f.flightCrewMember.id")
	List<FlightCrewMembers> findCrewMembersInLastLeg(int id);

	@Query("SELECT f.currentStatus, f FROM FlightAssignment f WHERE f.flightCrewMember.id = :id")
	List<Object[]> findFlightAssignmentsByStatus(int id);

	@Query("SELECT COUNT(f), f.moment FROM FlightAssignment f WHERE f.flightCrewMember.id = ?1 AND f.moment BETWEEN ?2 AND ?3 GROUP BY f.moment ORDER BY f.moment")
	List<Object[]> findFlightAssignmentsPerMoment(int id, Date startDate, Date endDate);

	@Query("SELECT f FROM FlightCrewMembers f WHERE f.id = :id")
	FlightCrewMembers findFlightCrewMemberById(int id);

}
