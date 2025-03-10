
package acme.entities.aircraft;

import java.util.Objects;

import javax.persistence.Column;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;

public class Aircraft extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				model;

	@Mandatory
	@ValidString(max = 50)
	@Column(unique = true)
	private String				regNumber;

	@Mandatory
	@ValidNumber
	@Automapped
	private Integer				capacity;

	@Mandatory
	@ValidNumber(min = 2000, max = 50000)
	@Automapped
	private Integer				cargoWeight;

	@Mandatory
	@Automapped
	private AircraftStatus		status;

	@Mandatory
	@ValidString
	@Automapped
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
