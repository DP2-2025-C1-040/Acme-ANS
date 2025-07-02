
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.passengers.Passenger;
import acme.features.customer.passenger.CustomerPassengerRepository;

@Validator
public class PassengerValidator extends AbstractValidator<ValidPassenger, Passenger> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerPassengerRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidPassenger annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Passenger passenger, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (passenger == null)
			result = true;
		else {
			{
				boolean uniquePassenger;
				Passenger existingPassenger;

				existingPassenger = this.repository.findPassengerByPassportNumber(passenger.getPassportNumber(), passenger.getCustomer().getId());
				uniquePassenger = existingPassenger == null || existingPassenger.equals(passenger);

				super.state(context, uniquePassenger, "passportNumber", "customer.passenger.unique.passportNumber");
			}
			result = !super.hasErrors(context);
		}

		return result;
	}
}
