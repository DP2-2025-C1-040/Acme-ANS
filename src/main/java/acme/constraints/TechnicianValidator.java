
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.realms.technician.Technician;
import acme.realms.technician.TechnicianRepository;

public class TechnicianValidator extends AbstractValidator<ValidTechnician, Technician> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianRepository repository;


	// ConstraintValidator interface ------------------------------------------
	@Override
	protected void initialise(final ValidTechnician annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Technician technician, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		String licenseNumber = technician.getLicenseNumber();

		{
			boolean uniqueTechnician;
			Technician existingTechnician;

			existingTechnician = this.repository.findTechnicianBylicenseNumber(licenseNumber);
			uniqueTechnician = existingTechnician == null || existingTechnician.equals(technician);

			super.state(context, uniqueTechnician, "licenseNumber", "acme.validation.technician.duplicated-license-number.message");
		}

		result = !super.hasErrors(context);

		return result;
	}

}
