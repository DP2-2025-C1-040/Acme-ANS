
package acme.features.crew.assignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.assignment.CurrentStatus;
import acme.entities.assignment.Duty;
import acme.entities.assignment.FlightAssignment;
import acme.entities.leg.Leg;
import acme.realms.crew.FlightCrewMemberRepository;
import acme.realms.crew.FlightCrewMembers;

@GuiService
public class CrewAssignmentPublishService extends AbstractGuiService<FlightCrewMembers, FlightAssignment> {

	@Autowired
	private FlightCrewMemberRepository	repository;

	@Autowired
	private CrewAssignmentRepository	assignmentRepository;


	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", int.class);
		FlightAssignment assignment = this.assignmentRepository.findFlightAssignmentById(id);

		boolean status = false;

		if (assignment != null && assignment.getDraftMode()) {
			int activeUserId = super.getRequest().getPrincipal().getActiveRealm().getId();
			boolean userOwnsAssignment = assignment.getFlightCrewMember().getId() == activeUserId;

			Object legData = super.getRequest().getData().get("leg");

			if (legData instanceof String legKey) {
				legKey = legKey.trim();

				if (legKey.equals("0"))
					status = userOwnsAssignment;
				else if (legKey.matches("\\d+")) {
					int legId = Integer.parseInt(legKey);
					Leg leg = this.assignmentRepository.findLegById(legId);
					boolean legIsValid = leg != null && this.assignmentRepository.findAllLegs().contains(leg);
					status = userOwnsAssignment && legIsValid;
				}
			}
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightAssignment assignment;
		int id;

		id = super.getRequest().getData("id", int.class);
		assignment = this.assignmentRepository.findFlightAssignmentById(id);

		super.getBuffer().addData(assignment);
	}

	@Override
	public void bind(final FlightAssignment assignment) {
		super.bindObject(assignment, "duty", "currentStatus", "remarks", "leg");
	}

	@Override
	public void validate(final FlightAssignment assignment) {
		if (assignment.getLeg() != null)
			if (assignment.getLeg().getScheduledArrival().before(MomentHelper.getCurrentMoment()))
				super.state(false, "leg", "flight-crew-members.flight-assignment.form.error.leg-already-occurred");
	}

	@Override
	public void perform(final FlightAssignment assignment) {
		assignment.setDraftMode(false);
		this.repository.save(assignment);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		// Cargar las opciones de legs desde el repositorio
		Collection<Leg> legs = this.assignmentRepository.findPublishedLegs();
		SelectChoices choices = new SelectChoices();
		SelectChoices duties;
		SelectChoices statuses;

		// Construir las opciones de 'legs' en base a la lista de 'legs'
		choices.add("0", "----", assignment.getLeg() == null); // Agregar opción por defecto
		for (Leg leg : legs) {
			String key = Integer.toString(leg.getId());
			String label = leg.getFlightNumber() + " - " + leg.getOriginCity() + " - " + leg.getDestinationCity() + " - " + leg.getFlight().getTag();
			boolean isSelected = leg.equals(assignment.getLeg());
			choices.add(key, label, isSelected);
		}

		// Obtener las opciones de 'duties' y 'statuses' de los enums correspondientes
		duties = SelectChoices.from(Duty.class, assignment.getDuty());
		statuses = SelectChoices.from(CurrentStatus.class, assignment.getCurrentStatus());

		// Desvincular el objeto 'assignment' y generar el dataset
		Dataset dataset = super.unbindObject(assignment, "duty", "moment", "currentStatus", "remarks", "leg", "draftMode");

		// Agregar al dataset las opciones seleccionadas y las listas completas
		dataset.put("leg", choices.getSelected().getKey());  // Se agrega el 'key' del 'leg' seleccionado
		dataset.put("legs", choices);  // Se agrega la lista completa de opciones de 'legs'
		dataset.put("duties", duties);  // Agregar las opciones de 'duties'
		dataset.put("statuses", statuses);  // Agregar las opciones de 'statuses'

		// Enviar el dataset como respuesta
		super.getResponse().addData(dataset);
	}

}
