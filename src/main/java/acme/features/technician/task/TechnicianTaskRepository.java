
package acme.features.technician.task;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.tasks.Task;
import acme.realms.technician.Technician;

@Repository
public interface TechnicianTaskRepository extends AbstractRepository {

	@Query("SELECT t FROM Task t WHERE t.technician.id = :id")
	Collection<Task> findTasksByTechnicianId(int id);

	Task findTaskById(int id);

	@Query("SELECT t FROM Technician t WHERE t.userAccount.id = :userAccountId")
	Technician findTechnicianByUserAccountId(int userAccountId);

}
