
package acme.features.customer.passangers;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.passengers.Passenger;
import acme.realms.Customer;

@GuiController
public class CustomerPassangerController extends AbstractGuiController<Customer, Passenger> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerPassangerListService	listService;

	@Autowired
	private CustomerPassangerShowService	showService;

	@Autowired
	private CustomerPassangerCreateService	createService;

	@Autowired
	private CustomerPassangerUpdateService	updateService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
	}

}
