
package acme.features.crew.dashboard;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.assignment.FlightAssignment;
import acme.entities.leg.LegStatus;
import acme.realms.crew.FlightCrewMembers;

@Repository
public interface CrewDashboardRepository extends AbstractRepository {

	@Query("SELECT DISTINCT f.leg.destinationCity FROM FlightAssignment f WHERE f.flightCrewMember.id = :id ORDER BY f.leg.scheduledDeparture DESC")
	List<String> findLastFiveDestinationsByCrewMemberId(int id);

	@Query("SELECT CASE " + "WHEN a.severityLevel BETWEEN 0 AND 3 THEN '0-3' " + "WHEN a.severityLevel BETWEEN 4 AND 7 THEN '4-7' " + "WHEN a.severityLevel BETWEEN 8 AND 10 THEN '8-10' " + "END AS severityRange, COUNT(f) " + "FROM FlightAssignment f "
		+ "JOIN f.activityLogs a " + "WHERE f.flightCrewMember.id = :id " + "GROUP BY severityRange")
	Map<String, Integer> findLegsByIncidentSeverity(int id);

	@Query("SELECT f.flightCrewMember FROM FlightAssignment f WHERE f.leg.id = (SELECT MAX(l.id) FROM Leg l WHERE l.flight.id = f.leg.flight.id) ORDER BY f.flightCrewMember.id")
	List<FlightCrewMembers> findCrewMembersInLastLeg(int id);

	@Query("SELECT f.status, f FROM FlightAssignment f WHERE f.flightCrewMember.id = :id GROUP BY f.status")
	Map<LegStatus, List<FlightAssignment>> findFlightAssignmentsByStatus(int id);

	@Query("SELECT AVG(f.count), MIN(f.count), MAX(f.count), STDDEV(f.count) FROM (SELECT COUNT(f) AS count FROM FlightAssignment f WHERE f.flightCrewMember.id = :id AND f.scheduledDeparture >= :startDate AND f.scheduledDeparture <= :endDate GROUP BY f.scheduledDeparture) f")
	Map<String, Double> findFlightAssignmentStatsLastMonth(int id, Date startDate, Date endDate);

}
