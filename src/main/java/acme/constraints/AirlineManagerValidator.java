
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.AirlineManager;

@Validator
public class AirlineManagerValidator extends AbstractValidator<ValidAirlineManager, AirlineManager> {

	// Internal state ---------------------------------------------------------

	//	@Autowired
	//	private AirlineManagerRepository repository;

	// ConstraintValidator interface ------------------------------------------

	@Override
	protected void initialise(final ValidAirlineManager annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final AirlineManager airlineManager, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result = true;

		{
			// Obtenemos la identidad del AirlineManager que queremos comparar
			DefaultUserIdentity identity = airlineManager.getUserAccount().getIdentity();
			String managerId = airlineManager.getManagerId();

			// Validar formato del managerId
			String regex = "^[A-Z]{2,3}\\d{6}$";
			boolean validFormat = managerId.matches(regex);

			String fullName = identity.getFullName();

			// Separar el nombre y los apellidos por ", " (formato esperado: "Apellido(s), Nombre(s)")
			String[] parts = fullName.split(", ");
			String lastNameInitial = parts[0].substring(0, 1).toUpperCase();
			String firstNameInitial = parts[1].substring(0, 1).toUpperCase();
			String expectedInitials = lastNameInitial + firstNameInitial;

			String managerInitials = managerId.substring(0, managerId.length() >= 3 ? 3 : 2);
			boolean validInitials = expectedInitials == managerInitials;

			if (identity == null || identity.getFullName() == null || managerId == null && !validFormat && !validInitials)
				result = false;

			super.state(context, validFormat, "airlineManager", "acme.validation.leg.flight-number.message");
			super.state(context, validInitials, "airlineManager", "acme.validation.leg.flight-number.message");
		}

		result = !super.hasErrors(context);

		return result;
	}

}
