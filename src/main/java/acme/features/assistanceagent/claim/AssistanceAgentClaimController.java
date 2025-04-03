
package acme.features.assistanceagent.claim;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.claims.Claim;
import acme.realms.assistanceAgent.AssistanceAgent;

@GuiController
public class AssistanceAgentClaimController extends AbstractGuiController<AssistanceAgent, Claim> {

	//Internal state --------------------------------------------------------------

	@Autowired
	private AssistanceAgentClaimListService			listService;

	@Autowired
	private AssistanceAgentClaimShowService			showService;

	@Autowired
	private AssistanceAgentClaimListPendingService	listPendingService;

	@Autowired
	private AssistanceAgentClaimCreateService		createService;

	@Autowired
	private AssistanceAgentClaimDeleteService		deleteService;

	@Autowired
	private AssistanceAgentClaimUpdateService		updateService;

	@Autowired
	private AssistanceAgentClaimPublishService		publishService;
	//Constructors ----------------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
		super.addBasicCommand("update", this.updateService);

		super.addCustomCommand("list-pending", "list", this.listPendingService);
		super.addCustomCommand("publish", "", this.publishService);
	}
}
