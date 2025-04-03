
package acme.features.administrator.aircraft;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.aircraft.AircraftStatus;
import acme.entities.airline.Airline;
import acme.features.administrator.airline.AdministratorAirlineRepository;

@GuiService
public class AdministratorAircraftDeleteService extends AbstractGuiService<Administrator, Aircraft> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorAircraftRepository	repository;

	@Autowired
	private AdministratorAirlineRepository	airlineRepository;

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
		int airlineId;
		Airline airline;

		airlineId = super.getRequest().getData("airline", int.class);
		airline = this.airlineRepository.findAirlineById(airlineId);

		super.bindObject(aircraft, "model", "regNumber", "capacity", "cargoWeight", "aircraftStatus", "details");
		aircraft.setAirline(airline);
	}

	@Override
	public void validate(final Aircraft aircraft) {
		boolean confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final Aircraft aircraft) {
		this.repository.delete(aircraft);
	}

	@Override
	public void unbind(final Aircraft aircraft) {
		SelectChoices choices;
		SelectChoices choicesAirlines;
		Dataset dataset;
		Collection<Airline> airlines;

		airlines = this.airlineRepository.findAllAirlines();

		choices = SelectChoices.from(AircraftStatus.class, aircraft.getAircraftStatus());
		choicesAirlines = SelectChoices.from(airlines, "name", aircraft.getAirline());

		dataset = super.unbindObject(aircraft, "model", "regNumber", "capacity", "cargoWeight", "aircraftStatus", "details", "airline");

		dataset.put("confirmation", false);
		dataset.put("statuses", choices);
		dataset.put("airlines", choicesAirlines);

		super.getResponse().addData(dataset);
	}
}
