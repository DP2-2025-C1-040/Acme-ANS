
package acme.realms.assistanceAgent;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AssistanceAgentRepository {

	@Query("Select a from AssistanceAgent A Where a.employeeCode= :=employeeCode")
	AssistanceAgent findAgentyByEmployeeCode(String employeeCode);

}
