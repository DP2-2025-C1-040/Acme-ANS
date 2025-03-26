
package acme.features.crew.assignment;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.assignment.CurrentStatus;
import acme.entities.assignment.Duty;
import acme.entities.assignment.FlightAssignment;
import acme.entities.leg.Leg;
import acme.realms.crew.AvailabilityStatus;
import acme.realms.crew.FlightCrewMemberRepository;
import acme.realms.crew.FlightCrewMembers;

@GuiService
public class CrewAssignmentCreateService extends AbstractGuiService<FlightCrewMembers, FlightAssignment> {

	@Autowired
	private FlightCrewMemberRepository	repository;

	@Autowired
	private CrewAssignmentRepository	assignmentRepository;


	@Override
	public void authorise() {
		int userId = super.getRequest().getPrincipal().getActiveRealm().getId();
		FlightCrewMembers crewMember = this.repository.findById(userId).orElse(null);
		boolean isLeadAttendant = this.repository.existsByFlightCrewMemberAndDuty(crewMember, Duty.LEAD_ATTENDANT);
		super.getResponse().setAuthorised(isLeadAttendant);
	}

	@Override
	public void load() {
		// Crear una nueva asignación de vuelo
		FlightAssignment assignment = new FlightAssignment();

		// Obtener el miembro de la tripulación logueado
		int userId = super.getRequest().getPrincipal().getActiveRealm().getId();
		FlightCrewMembers crewMember = this.repository.findById(userId).orElse(null);

		// Asignar el flightCrewMember al FlightAssignment
		assignment.setFlightCrewMember(crewMember);

		// Agregar la asignación de vuelo al buffer
		super.getBuffer().addData(assignment);
	}

	@Override
	public void bind(final FlightAssignment assignment) {
		super.bindObject(assignment, "duty", "moment", "currentStatus", "remarks", "flightCrewMember", "leg");
	}

	@Override
	public void validate(final FlightAssignment assignment) {
		boolean isAvailable, isNotAssignedMultipleLegs, isValidDuty;

		// Verificar si el miembro de la tripulación tiene el estado "AVAILABLE"
		isAvailable = assignment.getFlightCrewMember().getAvailabilityStatus() == AvailabilityStatus.AVAILABLE;
		super.state(isAvailable, "flightCrewMember", "flight-crew-members.flight-assignment.form.error.not-available");

		// Verificar si el miembro de la tripulación no está asignado a múltiples legs
		isNotAssignedMultipleLegs = this.repository.countByFlightCrewMemberAndLegNotNull(assignment.getFlightCrewMember()) == 0;
		super.state(isNotAssignedMultipleLegs, "flightCrewMember", "flight-crew-members.flight-assignment.form.error.assigned-multiple-legs");

		// Verificar si el Duty es válido (por ejemplo, que sea un "LEAD ATTENDANT" para permitir ciertas operaciones)
		isValidDuty = assignment.getDuty() == Duty.LEAD_ATTENDANT;
		super.state(isValidDuty, "duty", "flight-crew-members.flight-assignment.form.error.invalid-duty");

		// Verificar si el leg no ha ocurrido (para asegurarse de que no se pueda publicar una asignación en legs ya ocurridos)
		boolean isLegFuture = assignment.getLeg().getScheduledArrival().after(new Date());
		super.state(isLegFuture, "leg", "flight-crew-members.flight-assignment.form.error.leg-already-occurred");
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		SelectChoices duties = SelectChoices.from(Duty.class, assignment.getDuty());
		SelectChoices statuses = SelectChoices.from(CurrentStatus.class, assignment.getCurrentStatus());

		// Obtener todas las legs disponibles
		Collection<Leg> legs = this.assignmentRepository.findAllLegs(); // Asegúrate de que tienes acceso a las Legs

		// Convertir las Legs en SelectChoices
		SelectChoices legChoices = new SelectChoices();

		// Recorrer las legs y crear una etiqueta personalizada
		for (Leg leg : legs) {
			String key = Integer.toString(leg.getId());  // Usar el ID de Leg como clave
			String label = leg.getFlightNumber() + " - " + leg.getOriginCity();  // Etiqueta personalizada con origen y destino

			// Agregar la Leg como opción en SelectChoices
			legChoices.add(key, label, false);
		}

		Dataset dataset = super.unbindObject(assignment, "duty", "moment", "currentStatus", "remarks", "flightCrewMember", "leg");

		// Agregar las opciones al dataset
		dataset.put("duties", duties);
		dataset.put("statuses", statuses);
		dataset.put("legs", legChoices);  // Agregar las legs al dataset

		super.getResponse().addData(dataset);
	}
}
