
package acme.realms.assistanceAgent;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface AssistanceAgentRepository extends AbstractRepository {

	@Query("Select a from AssistanceAgent a Where a.employeeCode =:employeeCode")
	AssistanceAgent findAgentyByEmployeeCode(String employeeCode);

}
