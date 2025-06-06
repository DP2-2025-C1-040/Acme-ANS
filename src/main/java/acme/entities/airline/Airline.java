
package acme.entities.airline;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidIataCodeAirline;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidIataCodeAirline
public class Airline extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				name;

	@Mandatory
	@ValidString(message = "acme.validation.pattern-iata-code.message", pattern = "^[A-Z]{3}$")
	@Column(unique = true)
	private String				iataCode;

	@Mandatory
	@ValidUrl
	@Automapped
	private String				webSite;

	@Mandatory
	@Valid
	@Automapped
	private AirlineType			type;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				foundationMoment;

	@Optional
	@ValidEmail
	@Automapped
	private String				emailAddress;

	@Optional
	@ValidString(message = "{acme.validation.pattern-phone-number.message}", pattern = "^\\+?\\d{6,15}$")
	@Automapped
	private String				phoneNumber;

}
