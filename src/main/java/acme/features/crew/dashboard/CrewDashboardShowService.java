
package acme.features.crew.dashboard;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.assignment.CurrentStatus;
import acme.entities.assignment.FlightAssignment;
import acme.forms.CrewDashboard;
import acme.realms.crew.FlightCrewMembers;

@GuiService
public class CrewDashboardShowService extends AbstractGuiService<FlightCrewMembers, CrewDashboard> {

	@Autowired
	private CrewDashboardRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		CrewDashboard dashboard = new CrewDashboard();
		int crewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId(); // Obtener ID del miembro de la tripulación

		Date startDate = this.getStartDateForLastMonth();
		Date endDate = this.getEndDateForLastMonth();

		List<String> lastFiveDestinations = this.repository.findLastFiveDestinationsByCrewMemberId(crewMemberId, PageRequest.of(0, 5));
		String formattedDestinations = String.join(" - ", lastFiveDestinations);
		dashboard.setLastFiveDestinations(List.of(formattedDestinations));

		List<Object[]> severityStats = this.repository.findLegsByIncidentSeverity(crewMemberId);
		Map<String, Integer> legsByIncidentSeverity = new HashMap<>();

		for (Object[] result : severityStats) {
			String severityRange = result[0].toString();
			Integer count = ((Number) result[1]).intValue();
			legsByIncidentSeverity.put(severityRange, count);
		}
		dashboard.setLegsByIncidentSeverity(new LinkedHashMap<>(legsByIncidentSeverity));

		List<FlightCrewMembers> lastLegCrewMembers = this.repository.findCrewMembersInLastLeg(crewMemberId);
		dashboard.setLastLegCrewMembers(lastLegCrewMembers);

		List<Object[]> flightAssignmentsByStatusRaw = this.repository.findFlightAssignmentsByStatus(crewMemberId);
		Map<CurrentStatus, List<Map<String, Object>>> flightAssignmentsByStatus = new HashMap<>();

		for (Object[] result : flightAssignmentsByStatusRaw) {
			CurrentStatus status = (CurrentStatus) result[0];
			FlightAssignment assignment = (FlightAssignment) result[1];

			Map<String, Object> assignmentDetails = new HashMap<>();
			assignmentDetails.put("moment", assignment.getMoment());
			assignmentDetails.put("duty", assignment.getDuty());
			assignmentDetails.put("flightNumber", assignment.getLeg().getFlightNumber());

			flightAssignmentsByStatus.computeIfAbsent(status, k -> new ArrayList<>()).add(assignmentDetails);
		}

		dashboard.setFlightAssignmentsByStatus(flightAssignmentsByStatus);

		List<Long> counts = this.repository.findFlightAssignmentsCount(crewMemberId, startDate, endDate);
		System.out.println("Counts: " + counts);

		if (counts != null && !counts.isEmpty()) {
			double avg = counts.stream().mapToDouble(Long::doubleValue).average().orElse(0.0);
			double min = counts.stream().mapToDouble(Long::doubleValue).min().orElse(0.0);
			double max = counts.stream().mapToDouble(Long::doubleValue).max().orElse(0.0);
			double stddev = this.calculateStandardDeviation(counts);

			System.out.println("Avg: " + avg + ", Min: " + min + ", Max: " + max + ", Stddev: " + stddev);

			Map<String, Double> stats = new HashMap<>();
			stats.put("average", avg);
			stats.put("min", min);
			stats.put("max", max);
			stats.put("stddev", stddev);

			System.out.println("Flight Assignment Stats Last Month: " + stats);

			dashboard.setFlightAssignmentStatsLastMonth(stats);
		}

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final CrewDashboard dashboard) {
		Dataset dataset = super.unbindObject(dashboard, "lastFiveDestinations", "legsByIncidentSeverity", "lastLegCrewMembers", "flightAssignmentsByStatus", "flightAssignmentStatsLastMonth");
		super.getResponse().addData(dataset);
	}

	private Date getStartDateForLastMonth() {
		Date currentMoment = MomentHelper.getCurrentMoment();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentMoment);
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTime();
	}

	private Date getEndDateForLastMonth() {
		Date currentMoment = MomentHelper.getCurrentMoment();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentMoment);
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);

		return calendar.getTime();
	}

	private double calculateStandardDeviation(final List<Long> counts) {
		if (counts.isEmpty())
			return 0.0;

		double avg = counts.stream().mapToDouble(Long::doubleValue).average().orElse(0.0);
		double variance = counts.stream().mapToDouble(c -> Math.pow(c - avg, 2)).average().orElse(0.0);

		return Math.sqrt(variance);
	}
}
