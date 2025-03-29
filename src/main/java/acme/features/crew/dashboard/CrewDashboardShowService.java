
package acme.features.crew.dashboard;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.assignment.FlightAssignment;
import acme.entities.leg.LegStatus;
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

		int crewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId(); // Suponiendo que obtenemos el ID del miembro de la tripulación.

		// Cargar los últimos destinos
		List<String> lastFiveDestinations = this.repository.findLastFiveDestinationsByCrewMemberId(crewMemberId);
		dashboard.setLastFiveDestinations(lastFiveDestinations);

		// Cargar los registros de incidentes por severidad
		Map<String, Integer> legsByIncidentSeverity = this.repository.findLegsByIncidentSeverity(crewMemberId);
		dashboard.setLegsByIncidentSeverity(legsByIncidentSeverity);

		// Cargar los miembros de la tripulación en la última pierna de vuelo
		List<FlightCrewMembers> lastLegCrewMembers = this.repository.findCrewMembersInLastLeg(crewMemberId);
		dashboard.setLastLegCrewMembers(lastLegCrewMembers);

		// Cargar las asignaciones de vuelo por estado
		Map<LegStatus, List<FlightAssignment>> flightAssignmentsByStatus = this.repository.findFlightAssignmentsByStatus(crewMemberId);
		dashboard.setFlightAssignmentsByStatus(flightAssignmentsByStatus);

		// Cargar las estadísticas del último mes
		//		Date startDate = this.getStartDateForLastMonth();
		//		Date endDate = this.getEndDateForLastMonth();
		//		Map<String, Double> flightAssignmentStatsLastMonth = this.repository.findFlightAssignmentStatsLastMonth(crewMemberId, startDate, endDate);
		//		dashboard.setFlightAssignmentStatsLastMonth(flightAssignmentStatsLastMonth);

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

	//	private Date getStartDateForLastMonth() {
	//		// Implementa lógica para obtener la fecha de inicio del mes anterior.
	//	}
	//
	//	private Date getEndDateForLastMonth() {
	//		// Implementa lógica para obtener la fecha de fin del mes anterior.
	//	}
}
