
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
		assignment.setDraftMode(true);

		super.getBuffer().addData(assignment);
	}

	@Override
	public void bind(final FlightAssignment assignment) {
		super.bindObject(assignment, "duty", "moment", "currentStatus", "remarks", "leg");
	}

	@Override
	public void validate(final FlightAssignment assignment) {
		int userId = super.getRequest().getPrincipal().getActiveRealm().getId();
		FlightCrewMembers crewMember = this.repository.findById(userId);

		int assignmentCount = this.repository.countByFlightCrewMember(crewMember);
		boolean isAssignedMultipleLegs = assignmentCount > 1;
		super.state(!isAssignedMultipleLegs, "*", "flight-crew-members.flight-assignment.form.error.assigned-multiple-legs");

		Leg selectedLeg = assignment.getLeg();
		if (selectedLeg != null) {
			long pilotCount = this.assignmentRepository.countByLegAndDuty(selectedLeg, Duty.PILOT);
			long coPilotCount = this.assignmentRepository.countByLegAndDuty(selectedLeg, Duty.COPILOT);

			boolean isPilotAssigned = pilotCount > 0;
			boolean isCoPilotAssigned = coPilotCount > 0;

			super.state(!(isPilotAssigned && assignment.getDuty() == Duty.PILOT), "duty", "flight-crew-members.flight-assignment.form.error.duty-pilot-assigned");
			super.state(!(isCoPilotAssigned && assignment.getDuty() == Duty.COPILOT), "duty", "flight-crew-members.flight-assignment.form.error.duty-copilot-assigned");
		}

		// Otras validaciones pueden ir aquí...
	}

	@Override
	public void perform(final FlightAssignment assignment) {
		this.repository.save(assignment);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Collection<Leg> legs;
		SelectChoices choices = new SelectChoices();
		SelectChoices duties;
		SelectChoices statuses;

		legs = this.assignmentRepository.findAllLegs();

		for (Leg leg : legs) {
			String key = Integer.toString(leg.getId());
			String label = leg.getFlightNumber() + " - " + leg.getOriginCity() + " - " + leg.getDestinationCity() + " - " + leg.getFlight().getTag();

			boolean isSelected = leg.equals(assignment.getLeg());
			choices.add(key, label, isSelected);
		}

		duties = SelectChoices.from(Duty.class, assignment.getDuty());
		statuses = SelectChoices.from(CurrentStatus.class, assignment.getCurrentStatus());

		Dataset dataset = super.unbindObject(assignment, "duty", "moment", "currentStatus", "remarks", "leg");

		if (assignment.getLeg() != null)
			dataset.put("leg", choices.getSelected().getKey());

		dataset.put("legs", choices);
		dataset.put("duties", duties);
		dataset.put("statuses", statuses);

		super.getResponse().addData(dataset);
	}

}
