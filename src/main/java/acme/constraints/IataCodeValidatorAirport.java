
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.airport.Airport;
import acme.entities.airport.AirportRepository;

@Validator
public class IataCodeValidatorAirport extends AbstractValidator<ValidIataCodeAirport, Airport> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private AirportRepository airportRepository;


	// ConstraintValidator interface ------------------------------------------
	@Override
	protected void initialise(final ValidIataCodeAirport annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Airport airport, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result = true;

		if (airport == null || airport.getIataCode() == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");

		String iataCode = airport.getIataCode();
		Integer entityId = airport.getId();
		boolean exists = this.airportRepository.existsByIataCodeAndIdNot(iataCode, entityId);

		super.state(context, !exists, "iataCode", "acme.validation.airport.duplicated-iata-code.message");
		result = !super.hasErrors(context);
		return result;
	}
}
