
package acme.features.crew.assignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.assignment.CurrentStatus;
import acme.entities.assignment.Duty;
import acme.entities.assignment.FlightAssignment;
import acme.entities.leg.Leg;
import acme.entities.leg.LegStatus;
import acme.realms.crew.AvailabilityStatus;
import acme.realms.crew.FlightCrewMembers;

@GuiService
public class CrewAssignmentShowService extends AbstractGuiService<FlightCrewMembers, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CrewAssignmentRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		FlightAssignment assignment;
		int id;

		id = super.getRequest().getData("id", int.class);
		assignment = this.repository.findFlightAssignmentById(id);

		super.getBuffer().addData(assignment);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		SelectChoices duties;
		SelectChoices statuses;
		SelectChoices availabilityStatuses;
		SelectChoices legStatuses;
		SelectChoices choices;
		Collection<Leg> legs;
		Dataset dataset;

		legs = this.repository.findAllLegs();

		choices = SelectChoices.from(legs, "flightNumber", assignment.getLeg());
		duties = SelectChoices.from(Duty.class, assignment.getDuty());
		statuses = SelectChoices.from(CurrentStatus.class, assignment.getCurrentStatus());
		availabilityStatuses = SelectChoices.from(AvailabilityStatus.class, assignment.getFlightCrewMember().getAvailabilityStatus());
		legStatuses = SelectChoices.from(LegStatus.class, assignment.getLeg().getStatus());

		dataset = super.unbindObject(assignment, "duty", "moment", "currentStatus", "remarks", "leg");

		if (assignment.getFlightCrewMember() != null) {
			FlightCrewMembers crewMember = assignment.getFlightCrewMember();
			dataset.put("flightCrewMember.id", crewMember.getId());
			dataset.put("flightCrewMember.employeeCode", crewMember.getEmployeeCode());
			dataset.put("flightCrewMember.phoneNumber", crewMember.getPhoneNumber());
			dataset.put("flightCrewMember.languageSkills", crewMember.getLanguageSkills());
			dataset.put("flightCrewMember.availabilityStatus", crewMember.getAvailabilityStatus());
			dataset.put("flightCrewMember.salary", crewMember.getSalary());
			dataset.put("flightCrewMember.yearsOfExperience", crewMember.getYearsOfExperience());
			dataset.put("flightCrewMember.airline", crewMember.getAirline().getName());
		}

		if (assignment.getLeg() != null) {
			Leg leg = assignment.getLeg();
			dataset.put("leg.id", leg.getId());
			dataset.put("leg.flightNumber", leg.getFlightNumber());
			dataset.put("leg.status", leg.getStatus());
			dataset.put("leg.scheduledDeparture", leg.getScheduledDeparture());
			dataset.put("leg.scheduledArrival", leg.getScheduledArrival());
			dataset.put("leg.originCity", leg.getOriginCity());
			dataset.put("leg.destinationCity", leg.getDestinationCity());
			dataset.put("leg.departureAirport", leg.getDepartureAirport().getName());
			dataset.put("leg.arrivalAirport", leg.getArrivalAirport().getName());
			dataset.put("leg.aircraft", leg.getAircraft().getRegNumber());
			dataset.put("leg.flight", leg.getFlight().getTag());
			dataset.put("leg.airline", leg.getAirline().getName());
		}

		dataset.put("confirmation", false);
		dataset.put("duties", duties);
		dataset.put("statuses", statuses);
		dataset.put("availabilityStatuses", availabilityStatuses);
		dataset.put("legStatuses", legStatuses);
		dataset.put("leg", choices.getSelected().getKey());
		dataset.put("legs", choices);

		super.getResponse().addData(dataset);
	}

}
