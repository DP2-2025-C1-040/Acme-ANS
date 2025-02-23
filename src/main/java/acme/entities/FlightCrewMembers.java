
package acme.entities;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FlightCrewMembers extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long			serialVersionUID	= 1L;

	@Mandatory
	@Column(unique = true)
	@ValidString(pattern = "^[A-Z]{2-3}\\d{6}$")
	private String						employeeCode;

	@Mandatory
	@ValidString(pattern = "^\\+?\\d{6,15}$")
	@Automapped
	private String						phoneNumber;

	@Mandatory
	@ValidString
	@Automapped
	private String						languageSkills;

	@Mandatory
	@Automapped
	private Enum<AvailabilityStatus>	availabilityStatus;

	// @Mandatory
	// @Automapped
	// private Airlane airlane;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money						salary;

	@Optional
	@Automapped
	@ValidNumber(min = 0)
	private Integer						yearsOfExperience;

}
