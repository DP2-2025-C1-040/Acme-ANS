
package acme.entities.leg;

import java.beans.Transient;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.client.helpers.SpringHelper;
import acme.constraints.ValidFlightNumber;
import acme.entities.aircraft.Aircraft;
import acme.entities.airline.Airline;
import acme.entities.airport.Airport;
import acme.entities.flight.Flight;
import acme.entities.flight.FlightRepository;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

@ValidFlightNumber
public class Leg extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long serialVersionUID = 1L;

	// Enumerate -------------------------------------------------------------


	public enum LegStatus {

		ON_TIME, DELAYED, CANCELLED, LANDED
	}

	// Attributes -------------------------------------------------------------


	// Queda abierto a posible Custom Constraint
	// Se utiliza un validador ValidFlightNumber para comprobar que las primeras letras coinciden con las de la aerolinea
	// Añadir una relación en el rol de AirlineManager @ManyToOne con Airline
	@Mandatory
	@Column(unique = true)
	private String		flightNumber;

	@Mandatory
	private LegStatus	status;

	// Derived attributes -----------------------------------------------------


	@Transient
	public Date getScheduledDeparture() {
		FlightRepository repository = SpringHelper.getBean(FlightRepository.class);
		return repository.findScheduledDeparture(this.getId());
	};

	@Transient
	public Date getScheduledArrival() {
		FlightRepository repository = SpringHelper.getBean(FlightRepository.class);
		return repository.findScheduledArrival(this.getId());
	};

	@Transient
	public Double getDuration() {
		if (this.getScheduledDeparture() == null || this.getScheduledArrival() == null)
			return null;

		if (this.getScheduledDeparture().after(this.getScheduledArrival()))
			throw new IllegalArgumentException("Scheduled departure cannot be after scheduled arrival");

		long durationInMilliseconds = this.getScheduledArrival().getTime() - this.getScheduledDeparture().getTime();
		return durationInMilliseconds / (1000.0 * 60 * 60); // Convertir milisegundos a horas
	};

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
