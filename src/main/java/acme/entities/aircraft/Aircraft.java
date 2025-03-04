
package acme.entities.aircraft;

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
}
