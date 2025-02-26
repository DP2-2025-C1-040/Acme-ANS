
package acme.entities.crew;

import java.util.Date;
import java.util.Objects;

import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;

public class ActivityLog extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ManyToOne
	@Automapped
	private FlightCrewMembers	flightCrewMember;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				registrationMoment;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				typeOfIncident;

	@Mandatory
	@ValidString
	@Automapped
	private String				description;

	@Mandatory
	@ValidNumber(min = 0, max = 10)
	@Automapped
	private float				severityLevel;


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(this.description, this.flightCrewMember, this.registrationMoment, this.severityLevel, this.typeOfIncident);
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
		ActivityLog other = (ActivityLog) obj;
		return Objects.equals(this.description, other.description) && Objects.equals(this.flightCrewMember, other.flightCrewMember) && Objects.equals(this.registrationMoment, other.registrationMoment)
			&& Float.floatToIntBits(this.severityLevel) == Float.floatToIntBits(other.severityLevel) && Objects.equals(this.typeOfIncident, other.typeOfIncident);
	}

}
