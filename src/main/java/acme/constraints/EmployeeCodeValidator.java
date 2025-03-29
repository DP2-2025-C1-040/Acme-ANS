
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.crew.FlightCrewMemberRepository;
import acme.realms.crew.FlightCrewMembers;

@Validator
public class EmployeeCodeValidator extends AbstractValidator<ValidEmployeeCode, FlightCrewMembers> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private FlightCrewMemberRepository crewRepository;


	// ConstraintValidator interface ------------------------------------------
	@Override
	protected void initialise(final ValidEmployeeCode annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final FlightCrewMembers member, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result = true;

		if (member == null || member.getEmployeeCode() == null)
			return true;

		boolean exists = this.crewRepository.existsByEmployeeCodeAndIdNot(member.getEmployeeCode(), member.getId());

		super.state(context, !exists, "employeeCode", "acme.validation.flight-crew-member.duplicated-employee-code.message");

		result = !super.hasErrors(context);
		return result;
	}
}
