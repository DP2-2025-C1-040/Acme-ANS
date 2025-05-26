

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code ="assistance-agent.tracking-log.list.label.lastUpdateMoment" path ="lastUpdateMoment" width ="20%"/>
	<acme:list-column code ="assistance-agent.tracking-log.list.label.status" path ="status" width ="30%"/>
	<acme:list-column code ="assistance-agent.tracking-log.list.label.resolutionPercentage" path ="resolutionPercentage" width ="20%"/>
	<acme:list-payload path="payload"/>	
</acme:list>

<acme:button code="assistance-agent.tracking-log.list.button.create" action ="/assistance-agent/tracking-log/create?claimId=${claimId}"/>