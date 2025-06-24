
package acme.features.crew.assignment;

import java.util.Collection;
import java.util.Date;

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
import acme.realms.crew.FlightCrewMemberRepository;
import acme.realms.crew.FlightCrewMembers;

@GuiService
public class CrewAssignmentUpdateService extends AbstractGuiService<FlightCrewMembers, FlightAssignment> {

	@Autowired
	private FlightCrewMemberRepository	repository;

	@Autowired
	private CrewAssignmentRepository	assignmentRepository;


	@Override
	public void authorise() {
		boolean status = false;

		String rawId = super.getRequest().getData("id", String.class);
		FlightAssignment assignment = null;

		if (rawId != null && rawId.trim().matches("\\d+")) {
			int id = Integer.parseInt(rawId.trim());
			assignment = this.assignmentRepository.findFlightAssignmentById(id);
		}

		if (assignment != null && assignment.getDraftMode()) {
			int activeUserId = super.getRequest().getPrincipal().getActiveRealm().getId();
			boolean userOwnsAssignment = assignment.getFlightCrewMember().getId() == activeUserId;

			Object legData = super.getRequest().getData().get("leg");
			Object assignmentIdData = super.getRequest().getData().get("id");

			boolean legIsValid = false;
			boolean idIsValid = false;

			// Validación de leg (como ya hacías)
			if (legData == null)
				legIsValid = true;
			else if (legData instanceof String legKey) {
				legKey = legKey.trim();
				if (!legKey.isEmpty())
					if (legKey.equals("0"))
						legIsValid = true;
					else if (legKey.matches("\\d+")) {
						int legId = Integer.parseInt(legKey);
						Leg leg = this.assignmentRepository.findLegById(legId);
						legIsValid = leg != null;
					}
			}

			// Validación de id como string numérico
			if (assignmentIdData == null)
				idIsValid = true;
			else if (assignmentIdData instanceof String idKey) {
				idKey = idKey.trim();
				idIsValid = !idKey.isEmpty() && idKey.matches("\\d+");
			}

			status = userOwnsAssignment && legIsValid && idIsValid;
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightAssignment assignment;
		int id;

		id = super.getRequest().getData("id", int.class);
		assignment = this.assignmentRepository.findFlightAssignmentById(id);

		super.getBuffer().addData(assignment);
	}

	@Override
	public void bind(final FlightAssignment assignment) {
		super.bindObject(assignment, "duty", "currentStatus", "remarks", "leg");
	}

	@Override
	public void validate(final FlightAssignment assignment) {
		int userId = super.getRequest().getPrincipal().getActiveRealm().getId();
		FlightCrewMembers crewMember = this.repository.findById(userId);

		Collection<FlightAssignment> existingAssignments = this.assignmentRepository.findAllAssignmentsByMemberId(crewMember.getId());

		Leg currentLeg = assignment.getLeg();
		boolean overlaps = false;

		if (currentLeg != null) {
			Date currentStart = currentLeg.getScheduledDeparture();
			Date currentEnd = currentLeg.getScheduledArrival();

			for (FlightAssignment otherAssignment : existingAssignments) {
				if (otherAssignment.getId() == assignment.getId())
					continue;

				Leg otherLeg = otherAssignment.getLeg();
				if (otherLeg == null)
					continue;

				Date otherStart = otherLeg.getScheduledDeparture();
				Date otherEnd = otherLeg.getScheduledArrival();

				boolean doOverlap = !(currentEnd.before(otherStart) || currentStart.after(otherEnd));
				if (doOverlap) {
					overlaps = true;
					break;
				}
			}

			super.state(!overlaps, "leg", "flight-crew-members.flight-assignment.form.error.assigned-multiple-legs");
		}

		Leg selectedLeg = assignment.getLeg();
		if (selectedLeg != null) {
			int currentAssignmentId = assignment.getId();
			long pilotCount = this.assignmentRepository.countByLegAndDuty(selectedLeg, Duty.PILOT, currentAssignmentId);
			long coPilotCount = this.assignmentRepository.countByLegAndDuty(selectedLeg, Duty.COPILOT, currentAssignmentId);

			boolean isPilotAssigned = pilotCount > 0;
			boolean isCoPilotAssigned = coPilotCount > 0;

			super.state(!(isPilotAssigned && assignment.getDuty() == Duty.PILOT), "duty", "flight-crew-members.flight-assignment.form.error.duty-pilot-assigned");
			super.state(!(isCoPilotAssigned && assignment.getDuty() == Duty.COPILOT), "duty", "flight-crew-members.flight-assignment.form.error.duty-copilot-assigned");
		}
	}

	@Override
	public void perform(final FlightAssignment assignment) {
		assignment.setMoment(MomentHelper.getCurrentMoment());
		this.repository.save(assignment);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Collection<Leg> legs;
		SelectChoices choices = new SelectChoices();
		SelectChoices duties;
		SelectChoices statuses;

		FlightCrewMembers member = (FlightCrewMembers) super.getRequest().getPrincipal().getActiveRealm();
		legs = this.assignmentRepository.findUpcomingPublishedLegs(MomentHelper.getCurrentMoment(), member.getAirline().getId());

		choices.add("0", "----", assignment.getLeg() == null);
		for (Leg leg : legs) {
			String key = Integer.toString(leg.getId());
			String label = leg.getFlightNumber() + " - " + leg.getOriginCity() + " - " + leg.getDestinationCity() + " - " + leg.getFlight().getTag();
			boolean isSelected = leg.equals(assignment.getLeg());
			choices.add(key, label, isSelected);
		}

		duties = SelectChoices.from(Duty.class, assignment.getDuty());
		statuses = SelectChoices.from(CurrentStatus.class, assignment.getCurrentStatus());

		Dataset dataset = super.unbindObject(assignment, "duty", "moment", "currentStatus", "remarks", "leg", "draftMode");

		// Guardamos el valor seleccionado para que el usuario vea lo que intentó poner
		dataset.put("leg", assignment.getLeg() != null ? choices.getSelected().getKey() : "0");
		dataset.put("legs", choices);
		dataset.put("duties", duties);
		dataset.put("statuses", statuses);

		// Recuperamos el original para los datos readonly
		FlightAssignment original = this.assignmentRepository.findFlightAssignmentById(assignment.getId());

		// Datos del crew member (readonly)
		FlightCrewMembers crewMember = original.getFlightCrewMember();
		dataset.put("flightCrewMember.employeeCode", crewMember.getEmployeeCode());
		dataset.put("flightCrewMember.phoneNumber", crewMember.getPhoneNumber());
		dataset.put("flightCrewMember.languageSkills", crewMember.getLanguageSkills());
		dataset.put("flightCrewMember.availabilityStatus", crewMember.getAvailabilityStatus());
		dataset.put("flightCrewMember.salary", crewMember.getSalary());
		dataset.put("flightCrewMember.yearsOfExperience", crewMember.getYearsOfExperience());
		dataset.put("flightCrewMember.airline", crewMember.getAirline());

		// Datos del leg (readonly)
		if (original.getLeg() != null) {
			Leg leg = original.getLeg();
			dataset.put("leg.flightNumber", leg.getFlightNumber());
			dataset.put("leg.status", leg.getStatus());
			dataset.put("leg.scheduledDeparture", leg.getScheduledDeparture());
			dataset.put("leg.scheduledArrival", leg.getScheduledArrival());
			dataset.put("leg.originCity", leg.getOriginCity());
			dataset.put("leg.destinationCity", leg.getDestinationCity());
			dataset.put("leg.departureAirport", leg.getDepartureAirport());
			dataset.put("leg.arrivalAirport", leg.getArrivalAirport());
			dataset.put("leg.aircraft", leg.getAircraft());
			dataset.put("leg.flight", leg.getFlight().getTag());
			dataset.put("leg.airline", leg.getAirline());
		}

		super.getResponse().addData(dataset);
	}

}
