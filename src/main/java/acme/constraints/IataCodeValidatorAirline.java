
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

		if (airline == null || airline.getIataCode() == null) {
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
			result = false;
		} else {
			String iataCode = airline.getIataCode();

			// 🚨 Si el patrón no es correcto, evitar validación personalizada
			if (!iataCode.matches("[A-Z]{3}"))
				result = true; // Se mantiene como válido para evitar que salte la validación personalizada
			else {
				Integer entityId = airline.getId();
				boolean exists = this.airlineRepository.existsByIataCodeAndIdNot(iataCode, entityId);

				if (exists) {
					super.state(context, false, "iataCode", "acme.validation.airline.duplicated-iata-code.message");
					result = false;
				}
			}
		}

		return result;
	}

}
