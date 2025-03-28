
package acme.features.crew.assignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
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
		FlightCrewMembers crewMember = this.repository.findById(userId);
		boolean status = crewMember.getAvailabilityStatus() == AvailabilityStatus.AVAILABLE;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightAssignment assignment = new FlightAssignment();

		int userId = super.getRequest().getPrincipal().getActiveRealm().getId();
		FlightCrewMembers crewMember = this.repository.findById(userId);
		assignment.setFlightCrewMember(crewMember);
		assignment.setMoment(MomentHelper.getCurrentMoment());

		super.getBuffer().addData(assignment);
	}

	@Override
	public void bind(final FlightAssignment assignment) {
		super.bindObject(assignment, "duty", "moment", "currentStatus", "remarks", "leg");
	}

	@Override
	public void validate(final FlightAssignment assignment) {
		boolean isNotAssignedMultipleLegs, isValidDuty;

		// Verificar si el miembro de la tripulación no está asignado a múltiples legs
		isNotAssignedMultipleLegs = this.repository.countByFlightCrewMember(assignment.getFlightCrewMember()) == 0;
		super.state(!isNotAssignedMultipleLegs, "flightCrewMember", "flight-crew-members.flight-assignment.form.error.assigned-multiple-legs");

		//		// Verificar si el Duty es válido (por ejemplo, que sea un "LEAD ATTENDANT" para permitir ciertas operaciones)
		//		isValidDuty = assignment.getDuty() == Duty.LEAD_ATTENDANT;
		//		super.state(isValidDuty, "duty", "flight-crew-members.flight-assignment.form.error.invalid-duty");
		//
		//		// Verificar si el leg no ha ocurrido (para asegurarse de que no se pueda publicar una asignación en legs ya ocurridos)
		//		boolean isLegFuture = assignment.getLeg().getScheduledArrival().after(new Date());
		//		super.state(isLegFuture, "leg", "flight-crew-members.flight-assignment.form.error.leg-already-occurred");
	}

	@Override
	public void perform(final FlightAssignment assignment) {
		this.repository.save(assignment);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Collection<Leg> legs;
		SelectChoices choices;
		SelectChoices duties;
		SelectChoices statuses;

		legs = this.assignmentRepository.findAllLegs();

		choices = SelectChoices.from(legs, "flightNumber", assignment.getLeg());
		duties = SelectChoices.from(Duty.class, assignment.getDuty());
		statuses = SelectChoices.from(CurrentStatus.class, assignment.getCurrentStatus());

		Dataset dataset = super.unbindObject(assignment, "duty", "moment", "currentStatus", "remarks", "leg");
		dataset.put("leg", choices.getSelected().getKey());
		dataset.put("legs", choices);
		dataset.put("duties", duties);
		dataset.put("statuses", statuses);

		super.getResponse().addData(dataset);
	}
}
