
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
	<acme:input-select   path="type" 			  code="technician.task.form.label.type" choices="${types}"  />
	<acme:input-textarea path="description" 	  code="technician.task.form.label.description"/>
    <acme:input-integer  path="priority" 		  code="technician.task.form.label.priority" /> <%--hay un error en input-integer, en el placeholder--%>
    <acme:input-integer  path="estimatedDuration" code="technician.task.form.label.estimatedDuration" />
    
    <jstl:choose>
	    <jstl:when test="${_command == 'show' and not task.draftMode}">
	        <!-- Just the back button or whatever you want -->
	    </jstl:when>
	
	    <jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') and task.draftMode}">
	        <acme:submit code="technician.task.form.button.update" 	action="/technician/task/update"/>
	        <acme:submit code="technician.task.form.button.delete" 	action="/technician/task/delete"/>
	        <acme:submit code="technician.task.form.button.publish"	action="/technician/task/publish"/>
	    </jstl:when>
	
	    <jstl:when test="${_command == 'create'}">
	        <acme:submit code="technician.task.form.button.create" 	action="/technician/task/create"/>
	    </jstl:when>
	</jstl:choose>

</acme:form>
