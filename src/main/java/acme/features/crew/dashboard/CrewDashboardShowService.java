
package acme.features.crew.dashboard;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

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

		// Obtener fechas del mes anterior
		Date startDate = this.getStartDateForLastMonth();
		Date endDate = this.getEndDateForLastMonth();

		// Consultas adicionales
		// 1. Últimos cinco destinos
		List<String> lastFiveDestinations = this.repository.findLastFiveDestinationsByCrewMemberId(crewMemberId);
		dashboard.setLastFiveDestinations(lastFiveDestinations);

		// 2. Registros de incidentes por severidad
		List<Object[]> severityStats = this.repository.findLegsByIncidentSeverity(crewMemberId);
		Map<String, Integer> legsByIncidentSeverity = new HashMap<>();
		for (Object[] result : severityStats) {
			String severityRange = (String) result[0];  // El rango de severidad
			Long count = (Long) result[1];  // El conteo de registros
			legsByIncidentSeverity.put(severityRange, count.intValue());
		}
		dashboard.setLegsByIncidentSeverity(legsByIncidentSeverity);

		// 3. Miembros de la tripulación en la última pierna de vuelo
		List<FlightCrewMembers> lastLegCrewMembers = this.repository.findCrewMembersInLastLeg(crewMemberId);
		dashboard.setLastLegCrewMembers(lastLegCrewMembers);

		// 4. Asignaciones de vuelo por estado
		List<Object[]> flightAssignmentsByStatusRaw = this.repository.findFlightAssignmentsByStatus(crewMemberId);
		Map<CurrentStatus, List<FlightAssignment>> flightAssignmentsByStatus = new HashMap<>();
		for (Object[] result : flightAssignmentsByStatusRaw) {
			CurrentStatus status = (CurrentStatus) result[0];  // CurrentStatus
			FlightAssignment assignment = (FlightAssignment) result[1];  // FlightAssignment

			// Agregar la asignación de vuelo a la lista correspondiente al estado
			flightAssignmentsByStatus.computeIfAbsent(status, k -> new ArrayList<>())  // Si no existe, inicializa la lista
				.add(assignment);
		}
		dashboard.setFlightAssignmentsByStatus(flightAssignmentsByStatus);

		// 5. Estadísticas del último mes
		List<Long> counts = this.repository.findFlightAssignmentsCount(crewMemberId, startDate, endDate);
		double avg = counts.stream().mapToDouble(Long::doubleValue).average().orElse(0.0);
		double min = counts.stream().mapToDouble(Long::doubleValue).min().orElse(0.0);
		double max = counts.stream().mapToDouble(Long::doubleValue).max().orElse(0.0);
		double stddev = this.calculateStandardDeviation(counts);

		// Guardar estadísticas en el dashboard
		Map<String, Double> stats = new HashMap<>();
		stats.put("average", avg);
		stats.put("min", min);
		stats.put("max", max);
		stats.put("stddev", stddev);

		dashboard.setFlightAssignmentStatsLastMonth(stats);

		// Agregar al buffer
		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final CrewDashboard dashboard) {
		Dataset dataset = super.unbindObject(dashboard, //
			"lastFiveDestinations", "legsByIncidentSeverity", //
			"lastLegCrewMembers", "flightAssignmentsByStatus", //
			"flightAssignmentStatsLastMonth");

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

	// Método para calcular la desviación estándar
	private double calculateStandardDeviation(final List<Long> counts) {
		if (counts.isEmpty())
			return 0.0;

		double avg = counts.stream().mapToDouble(Long::doubleValue).average().orElse(0.0);
		double variance = counts.stream().mapToDouble(c -> Math.pow(c - avg, 2)).average().orElse(0.0);

		return Math.sqrt(variance);
	}
}
