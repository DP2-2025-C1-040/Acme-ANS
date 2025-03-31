
package acme.entities.claims;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import org.springframework.data.annotation.Transient;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.entities.leg.Leg;
import acme.entities.tracking_logs.TrackingLog;
import acme.entities.tracking_logs.TrackingLogStatus;
import acme.realms.assistanceAgent.AssistanceAgent;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Claim extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				registrationMoment;

	@Mandatory
	@ValidEmail
	@Automapped
	private String				passengerEmail;

	@Mandatory
	@ValidString(min = 1, max = 255)
	@Automapped
	private String				description;

	@Mandatory
	@Valid
	@Automapped
	private ClaimType			type;

	@Mandatory
	@Valid
	@Automapped
	private Boolean				published;

	// Derived attributes -----------------------------------------------------


	@Transient
	public ClaimStatus getAccepted() {
		ClaimStatus result;
		ClaimRepository repository;
		TrackingLog trackingLog;

		repository = SpringHelper.getBean(ClaimRepository.class);
		trackingLog = repository.findLastTrackingLogByClaimId(this.getId()).stream().findFirst().orElse(null);
		if (trackingLog == null)
			result = null;
		else {
			TrackingLogStatus indicator = trackingLog.getAccepted();
			if (indicator.equals(TrackingLogStatus.ACCEPTED))
				result = ClaimStatus.ACCEPTED;
			else if (indicator.equals(TrackingLogStatus.REJECTED))
				result = ClaimStatus.REJECTED;
			else
				result = ClaimStatus.PENDING;
		}
		return result;
	}
	// Relationships ----------------------------------------------------------


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private AssistanceAgent	assistanceAgent;

	@Mandatory
	@Valid
	@ManyToOne(optional = true)
	private Leg				leg;

}
