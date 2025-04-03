
package acme.constraints;

import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.helpers.MomentHelper;
import acme.entities.services.Service;
import acme.entities.services.ServiceRepository;

public class PromoCodeValidator extends AbstractValidator<ValidPromoCode, Service> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ServiceRepository repository;


	// ConstraintValidator interface ------------------------------------------
	@Override
	protected void initialise(final ValidPromoCode annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Service service, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result = true;

		String promoCode = service.getPromoCode();

		if (promoCode != null && !promoCode.isBlank()) {
			{
				boolean uniqueService;
				Service existingService;

				existingService = this.repository.findServiceByPromoCode(promoCode);
				uniqueService = existingService == null || existingService.equals(service);

				super.state(context, uniqueService, "promoCode", "acme.validation.service.promo-code.unique.message");
			}
			{
				Date currentDate = MomentHelper.getCurrentMoment();
				String currentYear = MomentHelper.format(currentDate, "yy");

				boolean isValidYear = promoCode.endsWith(currentYear);
				super.state(context, isValidYear, "promoCode", "acme.validation.service.promo-code.year.message");
			}
		}

		result = !super.hasErrors(context);

		return result;
	}
}
