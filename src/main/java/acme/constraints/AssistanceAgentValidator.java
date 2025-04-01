
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.DefaultUserIdentity;
import acme.client.components.validation.AbstractValidator;
import acme.realms.assistanceAgent.AssistanceAgent;
import acme.realms.assistanceAgent.AssistanceAgentRepository;

public class AssistanceAgentValidator extends AbstractValidator<ValidAssistanceAgent, AssistanceAgent> {

	// Internal state ---------------------------------------------------------

	@Autowired
	AssistanceAgentRepository repository;

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
			Boolean uniqueAssistanceAgent;
			AssistanceAgent existingAssistanceAgent;

			existingAssistanceAgent = this.repository.findAgentyByEmployeeCode(assistanceAgent.getEmployeeCode());
			uniqueAssistanceAgent = existingAssistanceAgent == null || existingAssistanceAgent.equals(assistanceAgent);
			super.state(context, uniqueAssistanceAgent, "employeeCode", "acme.validation.assistanceagent.employeeCode.duplicated.message");

			DefaultUserIdentity identity = assistanceAgent.getIdentity();
			String employeeCode = assistanceAgent.getEmployeeCode();
			String name = identity.getName();
			String surname = identity.getSurname();

			char employeeCodeFirstChar = Character.toUpperCase(employeeCode.charAt(0));
			char employeeCodeSecondChar = Character.toUpperCase(employeeCode.charAt(1));
			char nameFirstChar = Character.toUpperCase(name.charAt(0));
			char surnameFirstChar = Character.toUpperCase(surname.charAt(0));

			if (!(employeeCodeFirstChar == nameFirstChar && employeeCodeSecondChar == surnameFirstChar))
				super.state(context, false, "employeeCode", "acme.validation.assistanceagent.employeeCode.message");
			result = !super.hasErrors(context);
		}
		return result;
	}
}
