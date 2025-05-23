
package acme.entities.service;

import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidScore;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidPromoCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidPromoCode
public class Service extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				name;

	@Mandatory
	@ValidUrl
	@Automapped
	private String				picture;

	@Mandatory
	@ValidNumber(min = 1, max = 100, fraction = 2)
	@Automapped
	private Double				avgDwellTime;

	@Optional
	@ValidString(message = "{acme.validation.pattern-promo-code.message}", pattern = "^[A-Z]{4}-[0-9]{2}$")
	@Automapped
	private String				promoCode;

	@Optional
	@ValidScore
	@Automapped
	private Double				promoDiscount;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------
}
