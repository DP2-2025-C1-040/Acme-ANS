
package acme.entities.leg;

import javax.persistence.Column;
import javax.persistence.Transient;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Moment;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.entities.airport.Airport;

public class Leg extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long serialVersionUID = 1L;

	// Enumerate -------------------------------------------------------------


	public enum LegStatus {

		ON_TIME, DELAYED, CANCELLED, LANDED
	}

	// Attributes -------------------------------------------------------------


	// Queda abierto a posible Custom Constraint
	@Mandatory
	@ValidString(pattern = "[A-Z]{3}\\d{4}")
	@Column(unique = true)
	private String		flightNumber;

	@Mandatory
	@ValidMoment
	private Moment		scheduledDeparture;

	@Mandatory
	@ValidMoment
	private Moment		scheduledArrival;

	@Mandatory
	@ValidMoment
	private Moment		duration;

	@Mandatory
	private LegStatus	status;

	@Mandatory
	@Transient
	private Airport		departureAirport;

	@Mandatory
	@Transient
	private Airport		arrivalAirport;

	/*
	 * @Mandatory
	 * 
	 * @Transient
	 * private Aircraft aircraft;
	 * 
	 * @ManyToOne
	 * 
	 * @Valid
	 * 
	 * @Transient
	 * private Flight flight;
	 * 
	 * @ManyToOne
	 * 
	 * @Valid
	 * 
	 * @Transient
	 * private Airline airline;
	 */

}
