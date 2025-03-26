<%--
- form.jsp
-
- Copyright (C) 2012-2025 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form readonly="${readonly}">
    <acme:input-textbox code="flight-crew-members.activity-log.form.label.registrationMoment" path="registrationMoment"/>
    <acme:input-textbox code="flight-crew-members.activity-log.form.label.typeOfIncident" path="typeOfIncident"/>    
    <acme:input-textbox code="flight-crew-members.activity-log.form.label.description" path="description"/>
    <acme:input-textbox code="flight-crew-members.activity-log.form.label.severityLevel" path="severityLevel"/>
    
    <jstl:choose>
        <jstl:when test="${_command == 'show'}">
            <!-- Datos del FlightAssignment en solo lectura -->
            <acme:input-textbox code="flight-crew-members.activity-log.form.label.flight-assignment.duty" path="flightAssignment.duty"/>
            <acme:input-textbox code="flight-crew-members.activity-log.form.label.flight-assignment.moment" path="flightAssignment.moment"/>
            <acme:input-textbox code="flight-crew-members.activity-log.form.label.flight-assignment.currentStatus" path="flightAssignment.currentStatus"/>
            <acme:input-textbox code="flight-crew-members.activity-log.form.label.flight-assignment.remarks" path="flightAssignment.remarks"/>
        </jstl:when>
        <jstl:when test="${_command == 'create'}">
            <!-- Desplegable para seleccionar FlightAssignment -->
            <acme:input-select code="flight-crew-members.activity-log.form.label.flightAssignment" 
                               path="flightAssignment.id" 
                               choices="${flightAssignments}"/>
            <acme:submit code="flight-crew-members.activity-log.form.button.create" action="/flight-crew-members/activity-log/create"/>
        </jstl:when>
    </jstl:choose>
</acme:form>
