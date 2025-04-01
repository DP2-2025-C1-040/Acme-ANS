
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.tracking_logs.TrackingLog;
import acme.entities.tracking_logs.TrackingLogStatus;

@Validator
public class TrackingLogValidator extends AbstractValidator<ValidTrackingLog, TrackingLog> {

	@Override
	protected void initialise(final ValidTrackingLog annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final TrackingLog trackingLog, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (trackingLog == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			TrackingLogStatus trackingLogStatus = trackingLog.getStatus();
			{
				double percentage = trackingLog.getResolutionPercentage();
				boolean correctStatusUncompleted = percentage < 100.00 && trackingLogStatus.equals(TrackingLogStatus.PENDING);
				boolean correctStatusCompleted = percentage >= 100.00 && !trackingLogStatus.equals(TrackingLogStatus.PENDING);

				boolean correctStatus = correctStatusCompleted || correctStatusUncompleted;

				super.state(context, correctStatus, "*", "acme.validation.percentage.message");

			}
			{
				String resolutionDescription = trackingLog.getResolution();
				boolean correctDescriptionCompleted = !trackingLogStatus.equals(TrackingLogStatus.PENDING) && !resolutionDescription.isEmpty();
				boolean correctDescriptionUncompleted = trackingLogStatus.equals(TrackingLogStatus.PENDING) && resolutionDescription.isEmpty();

				boolean correctDescription = correctDescriptionCompleted || correctDescriptionUncompleted;

				super.state(context, correctDescription, "*", "acme.validation.resolutionDescription.message");
			}
		}

		result = !super.hasErrors(context);

		return result;
	}

}
