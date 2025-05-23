
package acme.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import acme.entities.service.Service;
import acme.entities.service.ServiceRepository;

@ControllerAdvice
public class ServiceAdvisor {

	@Autowired
	private ServiceRepository repository;

	// Beans ------------------------------------------------------------------


	@ModelAttribute("service")
	public Service getAdvertisement() {
		Service result;

		try {
			result = this.repository.findRandomService();
		} catch (final Throwable oops) {
			result = null;
		}

		return result;
	}
}
