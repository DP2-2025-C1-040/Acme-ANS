
package acme.constraints;

import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.helpers.MomentHelper;
import acme.entities.services.Service;

public class PromoCodeValidator extends AbstractValidator<ValidPromoCode, Service> {

	// Internal state ---------------------------------------------------------

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
			Date currentDate = MomentHelper.getCurrentMoment();
			String currentYear = MomentHelper.format(currentDate, "yy");

			boolean isValidYear = promoCode.endsWith(currentYear);
			super.state(context, isValidYear, "promoCode", "Invalid promo code|Código de promoción inválido.");

			result = !super.hasErrors(context);
		}
		return result;
	}
}
