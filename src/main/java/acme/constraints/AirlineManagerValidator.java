
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
			String fullName = identity.getFullName();

			// Separar el nombre y los apellidos por ", " (formato esperado: "Apellido(s), Nombre(s)")
			String[] parts = fullName.split(", ");
			String lastNameInitial = parts[0].substring(0, 1).toUpperCase();
			String firstNameInitial = parts[1].substring(0, 1).toUpperCase();
			String expectedInitials = firstNameInitial + lastNameInitial;
			String managerInitials = managerId.substring(0, expectedInitials.length());

			boolean validInitials = expectedInitials.equals(managerInitials);

			super.state(context, validInitials, "airlineManager", "acme.validation.leg.airline-manager-initials.message");

			boolean validFormat = false;
			// Validar formato del managerId
			if (managerId.length() == 8) {
				String regex = "^[A-Z]{2}\\d{6}$";
				validFormat = managerId.matches(regex);
			} else if (managerId.length() == 9) {
				String regex = "^[A-Z]{3}\\d{6}$";
				validFormat = managerId.matches(regex);
			}

			super.state(context, validFormat, "airlineManager", "acme.validation.leg.airline-manager-format.message");

			// Depuración
			//			System.out.println("Manager FullName: " + fullName);
			//			System.out.println("Manager ID: " + managerId);
			//			System.out.println("Expected Initials: " + expectedInitials);
			//			System.out.println("Extracted Manager Initials: " + managerInitials);
			//			System.out.println("Valid Initials: " + validInitials);
			//			System.out.println("Valid Format: " + validFormat);

		}

		result = !super.hasErrors(context);

		return result;
	}

}
