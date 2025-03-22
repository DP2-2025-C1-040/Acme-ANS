
package acme.features.crew;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.assignment.FlightAssignment;
import acme.realms.crew.FlightCrewMembers;

@GuiController
public class CrewAssignmentController extends AbstractGuiController<FlightCrewMembers, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CrewAssignmentListServiceLanded	landedListService;

	@Autowired
	private CrewAssignmentListServiceOnTime	onTimeListService;

	//	@Autowired
	//	private CrewAssignmentShowService		showService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.landedListService);
		//		super.addBasicCommand("list", this.onTimeListService);
		//super.addBasicCommand("show", this.showService);
		super.addCustomCommand("list-landed", "list", this.landedListService);
		super.addCustomCommand("list-on-time", "list", this.onTimeListService);
	}

}
