
package acme.features.assistanceagent.claim;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claims.Claim;
import acme.entities.leg.Leg;

@Repository
public interface AssistanceAgentClaimRepository extends AbstractRepository {

	@Query("SELECT c FROM Claim c")
	Collection<Claim> findAllClaims();

	@Query("SELECT c FROM Claim c WHERE c.assistanceAgent.id = :assistanceAgentId")
	Collection<Claim> findAllClaimsByAssistanceAgentId(int assistanceAgentId);

	@Query("SELECT c FROM Claim c WHERE c.id = :claimId")
	Claim findClaimById(int claimId);

	@Query("SELECT c.leg FROM Claim c")
	Collection<Leg> findAllLeg();
}
