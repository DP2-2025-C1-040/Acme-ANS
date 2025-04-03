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
	<acme:input-textbox code="customer.booking.form.label.locatorCode" path="locatorCode"/>
	<acme:input-moment code="customer.booking.form.label.purchaseMoment" path="purchaseMoment"/>
	<acme:input-select code="customer.booking.form.label.type" path="travelClass" choices="${travelClasses}"/>
	<acme:input-money code="customer.booking.form.label.price" path="price"/>
	<acme:input-textbox code="customer.booking.form.label.emailAddress" path="lastNibble"/>
	<acme:input-select code="customer.booking.form.label.flight" path="flight" choices="${flights}"/>
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update')}">
			<acme:input-checkbox code="customer.booking.form.label.confirmation" path="confirmation"/>
			<acme:submit code="customer.booking.form.button.update" action="/customer/booking/update"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:input-checkbox code="customer.booking.form.label.confirmation" path="confirmation"/>
			<acme:submit code="customer.booking.form.button.create" action="/customer/booking/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>