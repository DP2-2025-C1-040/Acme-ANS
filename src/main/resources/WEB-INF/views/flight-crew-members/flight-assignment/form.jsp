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
    <acme:input-select code="flight-crew-members.flight-assignment.form.label.duty" path="duty" choices="${duties}"/>
    <acme:input-textbox code="flight-crew-members.flight-assignment.form.label.moment" path="moment"/>    
    <acme:input-select code="flight-crew-members.flight-assignment.form.label.currentStatus" path="currentStatus" choices="${statuses}"/>
    <acme:input-textbox code="flight-crew-members.flight-assignment.form.label.remarks" path="remarks"/>

    <jstl:choose>
    	<jstl:when test="${_command == 'show'}">
			<!-- Datos de FlightCrewMember -->
			<span style="font-weight: bold; font-size: 1.5em;">
    			<acme:print code="flight-crew-members.flight-assignment.form.label.flightCrewMember.title"/>
			</span>
			<acme:input-textbox code="flight-crew-members.flight-assignment.form.label.flightCrewMember.employeeCode" path="flightCrewMember.employeeCode"/>
    		<acme:input-textbox code="flight-crew-members.flight-assignment.form.label.flightCrewMember.phoneNumber" path="flightCrewMember.phoneNumber"/>
    		<acme:input-textbox code="flight-crew-members.flight-assignment.form.label.flightCrewMember.languageSkills" path="flightCrewMember.languageSkills"/>
    		<acme:input-textbox code="flight-crew-members.flight-assignment.form.label.flightCrewMember.availabilityStatus" path="flightCrewMember.availabilityStatus"/>
    		<acme:input-textbox code="flight-crew-members.flight-assignment.form.label.flightCrewMember.salary" path="flightCrewMember.salary"/>
	    	<acme:input-textbox code="flight-crew-members.flight-assignment.form.label.flightCrewMember.yearsOfExperience" path="flightCrewMember.yearsOfExperience"/>
	    	<acme:input-textbox code="flight-crew-members.flight-assignment.form.label.flightCrewMember.airline" path="flightCrewMember.airline"/>

    		<!-- Datos de Leg -->
    		<span style="font-weight: bold; font-size: 1.5em;">
    			<acme:print code="flight-crew-members.flight-assignment.form.label.leg.title"/>
			</span>
    		<acme:input-textbox code="flight-crew-members.flight-assignment.form.label.leg.flightNumber" path="leg.flightNumber"/>
    		<acme:input-textbox code="flight-crew-members.flight-assignment.form.label.leg.status" path="leg.status"/>
	    	<acme:input-textbox code="flight-crew-members.flight-assignment.form.label.leg.scheduledDeparture" path="leg.scheduledDeparture"/>
	    	<acme:input-textbox code="flight-crew-members.flight-assignment.form.label.leg.scheduledArrival" path="leg.scheduledArrival"/>
    		<acme:input-textbox code="flight-crew-members.flight-assignment.form.label.leg.originCity" path="leg.originCity"/>
    		<acme:input-textbox code="flight-crew-members.flight-assignment.form.label.leg.destinationCity" path="leg.destinationCity"/>
    		<acme:input-textbox code="flight-crew-members.flight-assignment.form.label.leg.departureAirport" path="leg.departureAirport"/>
    		<acme:input-textbox code="flight-crew-members.flight-assignment.form.label.leg.arrivalAirport" path="leg.arrivalAirport"/>
    		<acme:input-textbox code="flight-crew-members.flight-assignment.form.label.leg.aircraft" path="leg.aircraft"/>
    		<acme:input-textbox code="flight-crew-members.flight-assignment.form.label.leg.flight" path="leg.flight"/>
    		<acme:input-textbox code="flight-crew-members.flight-assignment.form.label.leg.airline" path="leg.airline"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:input-select code="flight-crew-members.flight-assignment.form.label.leg" path="leg" choices="${legs}"/>
			<acme:submit code="flight-crew-members.flight-assignment.form.button.create" action="/flight-crew-members/flight-assignment/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>

