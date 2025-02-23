
package acme.entities;

import java.util.Date;

import javax.persistence.OneToOne;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;

public class FlightAssignment extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@OneToOne
	private FlightCrewMembers	flightCrewMember;

	//@Mandatory
	//@OneToOne
	//private Leg leg;

	@Mandatory
	private Enum<Duty>			duty;

	@Mandatory
	@ValidMoment(past = true)
	private Date				moment;

	@Mandatory
	private Enum<CurrentStatus>	currentStatus;

	@Optional
	@ValidString
	private String				remarks;

}
