
package acme.entities.flight;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.entities.leg.Leg;
import acme.realms.AirlineManager;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
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
	@ValidMoney(min = 1)
	@Automapped
	private Money				cost;

	@Mandatory
	@ValidString
	@Automapped
	private String				description;

	// Definirlo como Derived Attributes como getScheduledDeparture
	// Enlazar el atributo a un FlightRepository
	// Seguir foto del FollowUp (necesita correcciones)
	// Utilizar launcher inquirer para comprobar que las query devuelven los datos que queremos
	// Además, solo van acompañados de la anotación @Transient y nada más
	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledDeparture;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledArrival;

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

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private AirlineManager		airlineManager;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private List<Leg>			legs;

}
