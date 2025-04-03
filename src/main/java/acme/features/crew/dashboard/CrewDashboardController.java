
package acme.features.crew.dashboard;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.forms.CrewDashboard;
import acme.realms.crew.FlightCrewMembers;

@GuiController
public class CrewDashboardController extends AbstractGuiController<FlightCrewMembers, CrewDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CrewDashboardShowService showService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
	}

}
