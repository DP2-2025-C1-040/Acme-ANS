
package acme.features.crew.activityLog;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.activity_log.ActivityLog;
import acme.realms.crew.FlightCrewMembers;

@GuiController
public class CrewActivityLogController extends AbstractGuiController<FlightCrewMembers, ActivityLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CrewActivityLogListService	listService;

	@Autowired
	private CrewActivityLogShowService	showService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
	}

}
