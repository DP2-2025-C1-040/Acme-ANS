
package acme.entities.technicians;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Technician {
	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Column(unique = true)
	@Pattern(regexp = "^[A-Z]{2,3}\\d{6}$")
	@Automapped
	private String				licenseNumber;

	@Mandatory
	@Pattern(regexp = "^\\+?\\d{6,15}$")
	@Automapped
	private String				phoneNumber;

	@Mandatory
	@Length(min = 1, max = 50)
	@Automapped
	private String				specialisation;

	@Mandatory
	@Automapped
	private boolean				hasPassedHealthTest;

	@Mandatory
	@Automapped
	private Integer				yearsOfExperience;

	@Optional
	@Automapped
	@Length(max = 255)
	private String				certifications;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
