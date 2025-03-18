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

<acme:form readonly="true">
	<acme:input-textbox code="administrator.airport.form.label.name" path="name"/>
	<acme:input-textbox code="administrator.airport.form.label.iataCode" path="iataCode"/>	
	<acme:input-select code="administrator.airport.form.label.operationalScope" path="operationalScope" choices="${scopes}"/>
	<acme:input-textbox code="administrator.airport.form.label.city" path="city"/>
	<acme:input-textbox code="administrator.airport.form.label.country" path="country"/>
	<acme:input-textbox code="administrator.airport.form.label.webSite" path="webSite"/>
	<acme:input-textbox code="administrator.airport.form.label.emailAddress" path="emailAddress"/>
	<acme:input-textbox code="administrator.airport.form.label.contactPhoneNumber" path="contactPhoneNumber"/>
</acme:form>