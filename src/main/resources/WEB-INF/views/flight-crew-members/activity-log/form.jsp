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

<acme:form readonly="false">
    <acme:input-textbox code="flight-crew-members.activity-log.form.label.registrationMoment" path="registrationMoment"/>
    <acme:input-textbox code="flight-crew-members.activity-log.form.label.typeOfIncident" path="typeOfIncident"/>    
    <acme:input-textbox code="flight-crew-members.activity-log.form.label.description" path="description"/>
    <acme:input-textbox code="flight-crew-members.activity-log.form.label.severityLevel" path="severityLevel"/>

    <jstl:choose>
        <jstl:when test="${_command == 'create'}">
            <!-- Seleccionar FlightAssignment en create -->
            <acme:input-select code="flight-crew-members.activity-log.form.label.flightAssignment" 
                               path="flightAssignment.id" 
                               choices="${flightAssignments}"/>
            <acme:submit code="flight-crew-members.activity-log.form.button.create" action="/flight-crew-members/activity-log/create"/>
        </jstl:when>

<%--         <jstl:when test="${acme:anyOf(_command, 'show|update')}">
            <!-- Seleccionar nuevo FlightAssignment en update -->
            <acme:input-select code="flight-crew-members.activity-log.form.label.flightAssignment" 
                               path="flightAssignment.id" 
                               choices="${flightAssignments}"/>

            <!-- Mostrar datos del FlightAssignment en solo lectura -->
            <acme:input-textbox code="flight-crew-members.activity-log.form.label.flight-assignment.duty" path="flightAssignment.duty" readonly="true"/>
            <acme:input-textbox code="flight-crew-members.activity-log.form.label.flight-assignment.moment" path="flightAssignment.moment" readonly="true"/>
            <acme:input-textbox code="flight-crew-members.activity-log.form.label.flight-assignment.currentStatus" path="flightAssignment.currentStatus" readonly="true"/>
            <acme:input-textbox code="flight-crew-members.activity-log.form.label.flight-assignment.remarks" path="flightAssignment.remarks" readonly="true"/>

            <acme:submit code="flight-crew-members.activity-log.form.button.update" action="/flight-crew-members/activity-log/update"/>
        </jstl:when> --%>
    </jstl:choose>
</acme:form>




