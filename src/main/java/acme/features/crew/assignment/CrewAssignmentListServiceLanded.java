
package acme.features.crew.assignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.assignment.FlightAssignment;
import acme.realms.crew.FlightCrewMembers;

@GuiService
public class CrewAssignmentListServiceLanded extends AbstractGuiService<FlightCrewMembers, FlightAssignment> {

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
		Collection<FlightAssignment> assignments;
		int memberId;

		memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		assignments = this.repository.findAllAssignmentsByMemberIdBeforeNow(memberId, MomentHelper.getCurrentMoment());

		super.getBuffer().addData(assignments);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Dataset dataset;

		dataset = super.unbindObject(assignment, "duty", "moment");

		super.getResponse().addData(dataset);
	}

}
