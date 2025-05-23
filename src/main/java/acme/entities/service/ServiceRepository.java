
package acme.entities.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.helpers.RandomHelper;
import acme.client.repositories.AbstractRepository;

@Repository
public interface ServiceRepository extends AbstractRepository {

	@Query("select s from Service s where s.promoCode = :promoCode")
	Service findServiceByPromoCode(String promoCode);

	@Query("select count(s) from Service s")
	int countService();

	@Query("select s from Service s")
	List<Service> findAllService(PageRequest pageRequest);

	default Service findRandomService() {
		Service result;
		int count, index;
		PageRequest page;
		List<Service> list;

		count = this.countService();
		if (count == 0)
			result = null;
		else {
			index = RandomHelper.nextInt(0, count);

			page = PageRequest.of(index, 1, Sort.by(Direction.ASC, "id"));
			list = this.findAllService(page);
			result = list.isEmpty() ? null : list.get(0);
		}

		return result;
	}
}
