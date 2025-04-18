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

<acme:form>
	<acme:input-textbox  code = "administrator.aircraft.form.label.model"			 path = "model"/>
	<acme:input-textbox  code = "administrator.aircraft.form.label.regNumber"		 path = "regNumber"/>
	<acme:input-integer  code = "administrator.aircraft.form.label.capacity"		 path = "capacity"/>
	<acme:input-integer  code = "administrator.aircraft.form.label.cargoWeight" 	 path = "cargoWeight"/>
	<jstl:choose>
		<jstl:when test="${_command == 'create'}">
			<acme:input-select code="administrator.aircraft.form.label.aircraftStatus" path="aircraftStatus" choices="${statuses}" />
		</jstl:when>
		<jstl:otherwise>
			<acme:input-select code="administrator.aircraft.form.label.aircraftStatus" path="aircraftStatus" choices="${statuses}" readonly="true" />
		</jstl:otherwise>
	</jstl:choose>
	<acme:input-textarea code = "administrator.aircraft.form.label.details" 		 path = "details"/>
	<acme:input-select	 code =	"administrator.aircraft.form.label.airline" 		 path = "airline" choices="${airlines}" />

	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|disable')}">
			<acme:input-checkbox code="administrator.aircraft.form.label.confirmation" path="confirmation"/>
			<acme:submit code="administrator.aircraft.form.button.update" action="/administrator/aircraft/update"/>
			<acme:submit code="administrator.aircraft.form.button.disable" action="/administrator/aircraft/disable"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:input-checkbox code="administrator.aircraft.form.label.confirmation" path="confirmation"/>
			<acme:submit code="administrator.aircraft.form.button.create" action="/administrator/aircraft/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>
