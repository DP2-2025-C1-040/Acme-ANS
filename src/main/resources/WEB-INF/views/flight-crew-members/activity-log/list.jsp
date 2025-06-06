<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="flight-crew-members.activity-log.list.label.registrationMoment" path="registrationMoment"/>
	<acme:list-column code="flight-crew-members.activity-log.list.label.typeOfIncident" path="typeOfIncident"/>
</acme:list>

<acme:button code="flight-crew-members.activity-log.form.button.create" action="/flight-crew-members/activity-log/create"/>