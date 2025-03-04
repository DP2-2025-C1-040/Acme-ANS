
package acme.entities.aircraft;

import java.util.Objects;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.checkerframework.common.aliasing.qual.Unique;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;

public class Aircraft extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString
	@Length(max = 50, message = "Model must be at most 50 characters long")
	private String				model;

	@Mandatory
	@ValidString
	@Length(max = 50, message = "Registration Number must be at most 50 characters long")
	@Unique
	private String				regNumber;

	@Mandatory
	@ValidNumber
	private Integer				capacity;

	@Mandatory
	@ValidNumber
	@Range(min = 2000, max = 50000)
	private Integer				cargoWeight;

	@Mandatory
	@Enumerated(EnumType.STRING)
	private AircraftStatus		status;

	@Mandatory
	@ValidString
	private String				details;


	public enum AircraftStatus {
		ACTIVE, MAINTENANCE
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(this.capacity, this.cargoWeight, this.details, this.model, this.regNumber, this.status);
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
		Aircraft other = (Aircraft) obj;
		return Objects.equals(this.capacity, other.capacity) && Objects.equals(this.cargoWeight, other.cargoWeight) && Objects.equals(this.details, other.details) && Objects.equals(this.model, other.model) && Objects.equals(this.regNumber, other.regNumber)
			&& this.status == other.status;
	}

}
