
package acme.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.Validator;
import acme.entities.airline.Airline;
import acme.entities.airline.AirlineRepository;
import acme.entities.airport.Airport;
import acme.entities.airport.AirportRepository;

@Validator
public class IataCodeValidator implements ConstraintValidator<ValidIataCode, Object> {

	@Autowired
	private AirlineRepository	airlineRepository;

	@Autowired
	private AirportRepository	airportRepository;


	@Override
	public void initialize(final ValidIataCode annotation) {
		// No es necesario inicializar nada en este caso
	}

	@Override
	public boolean isValid(final Object entity, final ConstraintValidatorContext context) {
		if (entity == null)
			return true;

		String iataCode = switch (entity) {
		case Airline airline -> airline.getIataCode();
		case Airport airport -> airport.getIataCode();
		default -> null;
		};

		if (iataCode == null)
			return true;

		boolean existsInAirline = this.airlineRepository.existsByIataCode(iataCode);
		boolean existsInAirport = this.airportRepository.existsByIataCode(iataCode);

		boolean isValid = !(existsInAirline || existsInAirport);

		if (!isValid) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.airline.duplicated-iata-code.message").addPropertyNode("iataCode").addConstraintViolation();
		}

		return isValid;
	}
}
