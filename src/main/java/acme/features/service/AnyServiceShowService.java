
package acme.features.service;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.service.Service;
import acme.entities.service.ServiceRepository;

@GuiService
public class AnyServiceShowService extends AbstractGuiService<Authenticated, Service> {

	@Autowired
	private ServiceRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Service randomService;

		randomService = this.repository.findRandomService();
		super.getBuffer().addData(randomService);
	}

	@Override
	public void unbind(final Service service) {

		Dataset dataset;
		dataset = super.unbindObject(service, "picture");
		super.getResponse().addData(dataset);
	}
}
