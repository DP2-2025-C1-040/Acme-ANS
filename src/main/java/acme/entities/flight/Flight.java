
package acme.entities.flight;

import javax.persistence.Transient;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Moment;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;

public class Flight extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				tag;

	@Mandatory
	@Automapped
	private Boolean				selfTransfer;

	@Mandatory
	@ValidMoney(min = 0)
	@Automapped
	private Money				cost;

	@Mandatory
	@ValidString
	@Automapped
	private String				description;

	@Mandatory
	@ValidMoment
	@Transient
	private Moment				scheduledDeparture;

	@Mandatory
	@ValidMoment
	@Transient
	private Moment				scheduledArrival;

	@Mandatory
	@ValidString(max = 50)
	@Transient
	private String				originCity;

	@Mandatory
	@ValidString(max = 50)
	@Transient
	private String				destinationCity;

	// Derived attributes -----------------------------------------------------

	/*
	 * public Integer getNumberOfLayovers() {
	 * return legs != null && legs.size() > 1 ? legs.size() - 1 : 0;
	 * }
	 */

	// Relationships ----------------------------------------------------------

	/*
	 * @Mandatory
	 * 
	 * @Valid
	 * 
	 * @ManyToOne
	 * private List<Leg> legs;
	 */
}
