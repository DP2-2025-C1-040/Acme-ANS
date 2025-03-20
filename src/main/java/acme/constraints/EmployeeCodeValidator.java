
package acme.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.Validator;
import acme.realms.crew.FlightCrewMemberRepository;
import acme.realms.crew.FlightCrewMembers;

@Validator
public class EmployeeCodeValidator implements ConstraintValidator<ValidEmployeeCode, FlightCrewMembers> {

	@Autowired
	private FlightCrewMemberRepository crewRepository;


	@Override
	public void initialize(final ValidEmployeeCode annotation) {
		// No es necesario inicializar nada en este caso
	}

	@Override
	public boolean isValid(final FlightCrewMembers member, final ConstraintValidatorContext context) {
		if (member == null || member.getEmployeeCode() == null)
			return true;

		boolean exists = this.crewRepository.existsByEmployeeCode(member.getEmployeeCode());

		if (exists) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("acme.validation.flight-crew-member.duplicated-employee-code.message").addPropertyNode("employeeCode").addConstraintViolation();
		}

		return !exists;
	}
}
