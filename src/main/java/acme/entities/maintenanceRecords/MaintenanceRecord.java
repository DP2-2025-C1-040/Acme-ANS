
package acme.entities.maintenanceRecords;

import java.time.LocalDateTime;

import javax.persistence.Entity;

import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MaintenanceRecord {
	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Automapped
	private LocalDateTime		maintenanceDate;

	@Mandatory
	@Automapped
	private Status				status;

	@Mandatory
	@Automapped
	private LocalDateTime		nextInspection;

	@Mandatory
	@Automapped
	private Money				estimatedCost;

	@Optional
	@ValidString
	@Automapped
	private String				notes;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	// Commented until aircraft is done
	// @Mandatory
	// @Valid
	// @ManyToOne
	// private Aircraft			aircraft;
}
