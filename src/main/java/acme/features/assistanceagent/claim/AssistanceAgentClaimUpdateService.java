
package acme.features.assistanceagent.claim;

import acme.client.controllers.AbstractGuiController;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.realms.assistanceAgent.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimUpdateService extends AbstractGuiController<AssistanceAgent, Claim> {
}
