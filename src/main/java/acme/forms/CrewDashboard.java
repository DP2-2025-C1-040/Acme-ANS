
package acme.forms;

import java.util.List;
import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.entities.assignment.CurrentStatus;
import acme.entities.assignment.FlightAssignment;
import acme.realms.crew.FlightCrewMembers;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrewDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------

	private static final long					serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	List<String>								lastFiveDestinations;
	Map<String, Integer>						legsByIncidentSeverity;
	List<FlightCrewMembers>						lastLegCrewMembers;
	Map<CurrentStatus, List<FlightAssignment>>	flightAssignmentsByStatus;
	Map<String, Double>							flightAssignmentStatsLastMonth;

}
