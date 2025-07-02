
package acme.features.customer.bookingrecords;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.passengers.BookingRecord;
import acme.realms.Customer;

@GuiController
public class CustomerBookingRecordController extends AbstractGuiController<Customer, BookingRecord> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private CustomerBookingRecordLinkService	createService;

	@Autowired
	private CustomerBookingRecordUnlinkService	deleteService;


	// Constructors -----------------------------------------------------------
	@PostConstruct
	protected void initialise() {
		super.addCustomCommand("link", "create", this.createService);
		super.addCustomCommand("unlink", "update", this.deleteService);
	}

}
