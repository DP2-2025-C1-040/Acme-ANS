
package acme.entities.trackingLogs;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.entities.claims.Claim;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TrackingLog extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidMoment(past = true)
	@Automapped
	private Date				lastUpdateMoment;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				step;

	@Mandatory
	@ValidNumber(min = 0, max = 100)
	@Automapped
	private double				resolutionPercentage;

	@Mandatory
	@Valid
	@Automapped
	private TrackingLogStatus	indicator;

	@Optional
	@ValidString(min = 0, max = 255)
	@Automapped
	private String				resolution;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne
	private Claim				claim;

}
