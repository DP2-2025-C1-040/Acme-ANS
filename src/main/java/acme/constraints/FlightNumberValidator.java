
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.leg.Leg;

@Validator
public class FlightNumberValidator extends AbstractValidator<ValidFlightNumber, Leg> {

	// Internal state ---------------------------------------------------------

	//	@Autowired
	//	private FlightRepository repository;

	// ConstraintValidator interface ------------------------------------------

	@Override
	protected void initialise(final ValidFlightNumber annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Leg leg, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result = true;

		String flightNumber = leg.getFlightNumber();

		{
			// Obtenemos el IATA Code de la aerolinea que gestiona el vuelo
			String airlineIataCode = null;
			if (leg.getFlight() != null && leg.getFlight().getAirlineManager() != null && leg.getFlight().getAirlineManager().getAirline() != null && leg.getFlight().getAirlineManager().getAirline().getIataCode() != null)
				airlineIataCode = leg.getFlight().getAirlineManager().getAirline().getIataCode();

			// Validamos que FlightNumber tiene el formato esperado
			boolean validFormat = false;
			if (flightNumber != null && airlineIataCode != null) {
				String pattern = "^" + airlineIataCode + "\\d{4}$";
				validFormat = flightNumber.matches(pattern);
			}

			super.state(context, validFormat, "flightNumber", "acme.validation.leg.flight-number.message");
		}

		result = !super.hasErrors(context);

		return result;
	}

}
