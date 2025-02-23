
package acme.entities;

import java.util.Date;

import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;

public class FlightAssignment extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@OneToOne
	@Automapped
	private FlightCrewMembers	flightCrewMember;

	//@Mandatory
	//@OneToOne
	//@Automapped
	//private Leg leg;

	@Mandatory
	@Automapped
	private Enum<Duty>			duty;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				moment;

	@Mandatory
	@Automapped
	private Enum<CurrentStatus>	currentStatus;

	@Optional
	@ValidString
	@Automapped
	private String				remarks;

}
