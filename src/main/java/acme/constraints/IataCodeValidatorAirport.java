
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

		boolean result = true; // Inicialmente asumimos que es válido

		// 🚨 Si el aeropuerto o el código IATA es nulo, marcar error
		if (airport == null || airport.getIataCode() == null) {
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
			result = false;
		} else {
			String iataCode = airport.getIataCode();

			// 🚨 Si el patrón no es correcto, no hacer validación personalizada
			if (!iataCode.matches("[A-Z]{3}"))
				result = true; // Evitamos ejecutar la validación personalizada
			else {
				// 🛑 Validación personalizada: Verificar si el código ya existe
				Integer entityId = airport.getId();
				boolean exists = this.airportRepository.existsByIataCodeAndIdNot(iataCode, entityId);

				if (exists) {
					super.state(context, false, "iataCode", "acme.validation.airport.duplicated-iata-code.message");
					result = false;
				}
			}
		}

		return result;
	}

}
