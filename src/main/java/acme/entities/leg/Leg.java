
package acme.entities.leg;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidFlightNumber;
import acme.entities.aircraft.Aircraft;
import acme.entities.airline.Airline;
import acme.entities.airport.Airport;
import acme.entities.flight.Flight;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

@ValidFlightNumber
public class Leg extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Column(unique = true)
	private String				flightNumber;

	@Mandatory
	@Valid
	@Automapped
	private LegStatus			status;

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


	@Transient
	public Double getDuration() {
		if (this.getScheduledDeparture() == null || this.getScheduledArrival() == null)
			return null;

		if (this.getScheduledDeparture().after(this.getScheduledArrival()))
			throw new IllegalArgumentException("Scheduled departure cannot be after scheduled arrival");

		long durationInMilliseconds = this.getScheduledArrival().getTime() - this.getScheduledDeparture().getTime();
		return durationInMilliseconds / (1000.0 * 60 * 60); // Convertir milisegundos a horas
	}

	// Relationships ----------------------------------------------------------


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airport		departureAirport;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airport		arrivalAirport;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Aircraft	aircraft;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	@JoinColumn(name = "flightId")
	private Flight		flight;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airline		airline;

}
