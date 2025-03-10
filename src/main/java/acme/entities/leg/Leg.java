
package acme.entities.leg;

import javax.persistence.Column;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Moment;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;

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

	/*
	 * private Airport departureAirport;
	 * 
	 * private Airport arrivalAirport;
	 * 
	 * private Aircraft aircraft;
	 * 
	 * 
	 * @ManyToOne
	 * private Flight flight;
	 * 
	 * @ManyToOne
	 * private Airline airline;
	 */

}
