
package acme.entities.airlineManager;

import javax.validation.constraints.Pattern;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Moment;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;

public class AirlineManager extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString
	@Pattern(regexp = "^[A-Z]{2,3}\\d{6}$", message = "Manager ID must follow this pattern: XX123456 or YYY123456")
	@Automapped
	private String				managerId;

	@Mandatory
	@ValidNumber
	private Integer				yearsOfExperience;

	@Mandatory
	@ValidMoment
	private Moment				dateOfBirth;

	@Optional
	@ValidUrl
	private String				imageLink;

}
