
package acme.features.administrator.aircraft;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.aircraft.AircraftStatus;

@GuiService
public class AdministratorAircraftUpdateService extends AbstractGuiService<Administrator, Aircraft> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorAircraftRepository repository;

	//	@Autowired
	//	private AirlineRepository				airlineRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Aircraft aircraft;
		int id;

		id = super.getRequest().getData("id", int.class);
		aircraft = this.repository.findAircraftById(id);

		super.getBuffer().addData(aircraft);
	}

	@Override
	public void bind(final Aircraft aircraft) {
		int airlineId = super.getRequest().getData("airline", int.class);
		//		Airline airline = this.airlineRepository.findOneById(airlineId);
		//		aircraft.setAirline(airline);

		super.bindObject(aircraft, "model", "regNumber", "capacity", "cargoWeight", "aircraftStatus", "details");
	}

	@Override
	public void validate(final Aircraft aircraft) {
		boolean confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final Aircraft aircraft) {
		this.repository.save(aircraft);
	}

	@Override
	public void unbind(final Aircraft aircraft) {
		SelectChoices statusChoices = SelectChoices.from(AircraftStatus.class, aircraft.getAircraftStatus());
		//		Collection<Airline> airlines = this.airlineRepository.findAllAirlines();
		//		SelectChoices airlineChoices = SelectChoices.from(airlines, "name", aircraft.getAirline());

		Dataset dataset = super.unbindObject(aircraft, "model", "regNumber", "capacity", "cargoWeight", "aircraftStatus", "details");
		dataset.put("statuses", statusChoices);
		//		dataset.put("airline", airlineChoices.getSelected().getKey());
		//		dataset.put("airlines", airlineChoices);
		dataset.put("confirmation", false);

		super.getResponse().addData(dataset);
	}
}
