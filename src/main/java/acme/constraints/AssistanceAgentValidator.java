
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.realms.AssistanceAgent;

public class AssistanceAgentValidator extends AbstractValidator<ValidAssistanceAgent, AssistanceAgent> {

	// Internal state ---------------------------------------------------------

	// Initialiser ------------------------------------------------------------

	@Override
	public void initialise(final ValidAssistanceAgent annotation) {
		assert annotation != null;
	}

	// AbstractValidator interface --------------------------------------------

	@Override
	public boolean isValid(final AssistanceAgent assistanceAgent, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result = true;

		if (assistanceAgent == null || assistanceAgent.getEmployeeCode() == null || assistanceAgent.getIdentity() == null) {
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
			result = !super.hasErrors(context);

		} else {

			DefaultUserIdentity identity = assistanceAgent.getIdentity();
			String employeeCode = assistanceAgent.getEmployeeCode();
			String name = identity.getName();
			String surname = identity.getSurname();

			char employeeCodeFirstChar = Character.toUpperCase(employeeCode.charAt(0));
			char employeeCodeSecondChar = Character.toUpperCase(employeeCode.charAt(1));
			char nameFirstChar = Character.toUpperCase(name.charAt(0));

			String[] surnameParts = surname.split(" ");
			if (surnameParts.length > 1) {
				char surnameFirstChar = Character.toUpperCase(surnameParts[0].charAt(0));
				char surnameSecondChar = Character.toUpperCase(surnameParts[1].charAt(0));
				char employeeCodeThirdChar = Character.toUpperCase(employeeCode.charAt(2));

				if (!(employeeCodeFirstChar == nameFirstChar && employeeCodeSecondChar == surnameFirstChar && employeeCodeThirdChar == surnameSecondChar))
					super.state(context, false, "EmployeeCode", "The employeeCode is incorrectly");
			} else {
				char surnameFirstChar = Character.toUpperCase(surname.charAt(0));

				if (!(employeeCodeFirstChar == nameFirstChar && employeeCodeSecondChar == surnameFirstChar))
					super.state(context, false, "EmployeeCode", "The employeeCode is incorrectly");
			}
			result = !super.hasErrors(context);
		}
		return result;
	}
}
