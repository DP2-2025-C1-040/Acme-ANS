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
	<acme:input-textbox code="customer.booking-record.form.label.locatorCode" path="locatorCode" readonly="true"/>
	<acme:input-moment code="customer.booking-record.form.label.tag" path="tag" readonly="true"/>
	<acme:input-select code="customer.booking-record.form.label.passenger" path="passenger" choices="${passengers}"/>

	<jstl:choose>
		<jstl:when test="${_command == 'link'}">
			<acme:submit code="customer.booking-record.form.button.link" action="/customer/booking-record/link?bookingId=${bookingId}"/>
		</jstl:when>
		<jstl:when test="${_command == 'unlink'}">
			<acme:submit code="customer.booking-record.form.button.unlink" action="/customer/booking-record/unlink?bookingId=${bookingId}"/>
		</jstl:when>
	</jstl:choose>
</acme:form>