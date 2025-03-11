
package acme.entities.airlineManager;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;

public class AirlineManager extends AbstractRole {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	//Recomienda crear un validador para el managerId
	@Mandatory
	//@AirlineManagerValidator
	@ValidString(pattern = "^[A-Z]{2,3}\\d{6}$")
	@Automapped
	private String				managerId;

	@Mandatory
	@ValidNumber(min = 0, max = 70)
	@Automapped
	private Integer				yearsOfExperience;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				dateOfBirth;

	@Optional
	@ValidUrl
	@Automapped
	private String				imageLink;


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(this.dateOfBirth, this.imageLink, this.managerId, this.yearsOfExperience);
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
		AirlineManager other = (AirlineManager) obj;
		return Objects.equals(this.dateOfBirth, other.dateOfBirth) && Objects.equals(this.imageLink, other.imageLink) && Objects.equals(this.managerId, other.managerId) && Objects.equals(this.yearsOfExperience, other.yearsOfExperience);
	}

}
