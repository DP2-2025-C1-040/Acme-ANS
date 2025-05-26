<%--
- list.jsp
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

<acme:list>
	<acme:list-column code="customer.passenger.list.label.fullName" path="fullName" width="50%"/>
	<acme:list-column code="customer.passenger.list.label.passportNumber" path="passportNumber" width="25%"/>
	<acme:list-column code="customer.passenger.list.label.specialNeeds" path="specialNeeds" width="25%"/>
</acme:list>

<acme:button code="customer.passenger.list.button.create" action="/customer/passenger/create"/>
<jstl:if test="${bookingId != null}">
	<acme:button code="customer.booking-record.form.button.link" action="/customer/booking-record/create?bookingId=${bookingId}"/>
	<acme:button code="customer.booking-record.form.button.unlink" action="/customer/booking-record/delete?bookingId=${bookingId}"/>
</jstl:if>