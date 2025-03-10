
package acme.entities.airline;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;

public class Airline extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				name;

	@Mandatory
	@ValidString(min = 3, max = 3, pattern = "[A-Z]{3}")
	@Column(unique = true)
	private String				iataCode;

	@Mandatory
	@ValidUrl
	@Automapped
	private String				webSite;

	@Mandatory
	@Automapped
	private Enum<AirlineType>	type;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				foundationMoment;

	@Optional
	@ValidEmail
	@Automapped
	private String				emailAdress;

	@Optional
	@ValidString(pattern = "^\\+?\\d{6,15}$")
	@Automapped
	private String				phoneNumber;


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(this.emailAdress, this.foundationMoment, this.iataCode, this.name, this.phoneNumber, this.type, this.webSite);
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		Airline other = (Airline) obj;
		return Objects.equals(this.emailAdress, other.emailAdress) && Objects.equals(this.foundationMoment, other.foundationMoment) && Objects.equals(this.iataCode, other.iataCode) && Objects.equals(this.name, other.name)
			&& Objects.equals(this.phoneNumber, other.phoneNumber) && Objects.equals(this.type, other.type) && Objects.equals(this.webSite, other.webSite);
	}

}
