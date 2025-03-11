
package acme.entities.tasks;

import javax.validation.Valid;

import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;

public class Task {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Valid
	@Automapped
	private TaskType			type;

	@Mandatory
	@ValidString
	@Automapped
	private String				description;

	@Mandatory
	@ValidNumber(min = 0, max = 10)
	@Automapped
	private Integer				priority;

	@Mandatory
	@ValidNumber(min = 0)
	@Automapped
	private Integer				estimatedDuration;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	//	@Mandatory
	//	@Valid
	//	@ManyToOne(optional = false)
	//	private Technician			technician;
	//
	//	@Mandatory
	//	@Valid
	//	@ManyToOne(optional = false)
	//	private MaintenanceRecord	maintenanceRecord;
	//
	//	@Mandatory
	//	@Valid
	//	@ManyToOne(optional = false)
	//	private Aircraft			aircraft;

}
