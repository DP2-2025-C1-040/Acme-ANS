<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>
<acme:form readonly="${readonly}">
    <acme:input-moment code="flight-crew-members.activity-log.form.label.registrationMoment" path="registrationMoment" readonly="true"/>
    <acme:input-textbox code="flight-crew-members.activity-log.form.label.typeOfIncident" path="typeOfIncident"/>    
    <acme:input-textarea code="flight-crew-members.activity-log.form.label.description" path="description"/>
    <acme:input-double code="flight-crew-members.activity-log.form.label.severityLevel" path="severityLevel"/>
    <acme:input-select code="flight-crew-members.activity-log.form.label.flightAssignment" path="flightAssignment" choices="${assignments}"/>

    <jstl:choose>
       	<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
            <acme:submit code="flight-crew-members.activity-log.form.button.update" action="/flight-crew-members/activity-log/update"/>
            <acme:submit code="flight-crew-members.activity-log.form.button.delete" action="/flight-crew-members/activity-log/delete"/>
            <acme:submit code="flight-crew-members.activity-log.form.button.publish" action="/flight-crew-members/activity-log/publish"/>
        </jstl:when>
        <jstl:when test="${_command == 'create'}">
            <acme:submit code="flight-crew-members.activity-log.form.button.create" action="/flight-crew-members/activity-log/create"/>
        </jstl:when>
    </jstl:choose>
</acme:form>




