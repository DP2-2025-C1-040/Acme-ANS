
package acme.entities;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Airport extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long		serialVersionUID	= 1L;

	@Mandatory
	@ValidString(max = 50)
	private String					name;

	@Mandatory
	@ValidString(min = 3, max = 3, pattern = "[A-Z]{3}")
	@Column(unique = true)
	private String					iataCode;

	@Mandatory
	private Enum<OperationalScope>	operationalScope;

	@Mandatory
	@ValidString(max = 50)
	private String					city;

	@Mandatory
	@ValidString(max = 50)
	private String					country;

	@Optional
	@ValidUrl
	private String					webSite;

	@Optional
	@ValidEmail
	private String					emailAdress;

	@Optional
	@ValidString(pattern = "^\\+?\\d{6,15}$")
	private String					contactPhoneNumber;

	// Runways


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(this.city, this.contactPhoneNumber, this.country, this.emailAdress, this.iataCode, this.name, this.operationalScope, this.webSite);
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
		Airport other = (Airport) obj;
		return Objects.equals(this.city, other.city) && Objects.equals(this.contactPhoneNumber, other.contactPhoneNumber) && Objects.equals(this.country, other.country) && Objects.equals(this.emailAdress, other.emailAdress)
			&& Objects.equals(this.iataCode, other.iataCode) && Objects.equals(this.name, other.name) && Objects.equals(this.operationalScope, other.operationalScope) && Objects.equals(this.webSite, other.webSite);
	}

}
