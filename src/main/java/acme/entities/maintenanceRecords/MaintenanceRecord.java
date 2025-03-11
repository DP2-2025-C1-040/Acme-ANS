
package acme.entities.maintenanceRecords;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
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
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date				maintenanceDate;

	@Mandatory
	@Valid
	@Automapped
	private RecordStatus		status;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date				nextInspection;

	@Mandatory
	@ValidMoney
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
