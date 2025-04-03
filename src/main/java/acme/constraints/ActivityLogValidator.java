
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.activity_log.ActivityLog;
import acme.entities.leg.Leg;

@Validator
public class ActivityLogValidator extends AbstractValidator<ValidActivityLog, ActivityLog> {

	// ConstraintValidator interface ------------------------------------------
	@Override
	protected void initialise(final ValidActivityLog annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final ActivityLog activityLog, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result = true;

		if (activityLog == null || activityLog.getFlightAssignment().getLeg() == null) {
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
			result = false;
		} else {

			Leg leg = activityLog.getFlightAssignment().getLeg();

			if (leg.getScheduledDeparture() != null) {

				boolean isLegLanded = MomentHelper.isBefore(leg.getScheduledDeparture(), MomentHelper.getCurrentMoment());

				if (!isLegLanded) {
					super.state(context, false, "leg", "acme.validation.activityLog.leg.notLanded");
					result = false;
				}
			} else {

				super.state(context, false, "leg", "javax.validation.constraints.NotNull.message");
				result = false;
			}
		}

		return result;
	}
}
