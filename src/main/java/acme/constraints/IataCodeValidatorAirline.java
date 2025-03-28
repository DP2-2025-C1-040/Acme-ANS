
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.airline.Airline;
import acme.entities.airline.AirlineRepository;

@Validator
public class IataCodeValidatorAirline extends AbstractValidator<ValidIataCodeAirline, Airline> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private AirlineRepository airlineRepository;


	// ConstraintValidator interface ------------------------------------------
	@Override
	protected void initialise(final ValidIataCodeAirline annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Airline airline, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result = true;

		if (airline == null || airline.getIataCode() == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");

		String iataCode = airline.getIataCode();
		Integer entityId = airline.getId();

		boolean exists = this.airlineRepository.existsByIataCodeAndIdNot(iataCode, entityId);

		super.state(context, !exists, "iataCode", "acme.validation.airline.duplicated-iata-code.message");

		result = !super.hasErrors(context);

		return result;
	}
}
