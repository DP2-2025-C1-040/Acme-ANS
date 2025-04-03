
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

<h2>
    <acme:print code="flight-crew-member.dashboard.form.title.general-indicators"/>
</h2>

<table class="table table-sm">
    <tr>
        <th scope="row">
            <acme:print code="flight-crew-member.dashboard.form.label.last-five-destinations"/>
        </th>
        <td>
			<acme:print value="${lastFiveDestinations}"/>
		</td>
    </tr>
    <tr>
    	<th scope="row">
        <acme:print code="flight-crew-member.dashboard.form.label.legs-by-incident-severity"/>
    	</th>
    	<td>
        <ul>
            <jstl:forEach var="entry" items="${legsByIncidentSeverity}">
                <li>${entry.key}: ${entry.value}</li>
            </jstl:forEach>
        </ul>
    	</td>
	</tr>

    <tr>
    <th scope="row">
        <acme:print code="flight-crew-member.dashboard.form.label.last-leg-crew-members"/>
    </th>
    	<td>
        	<ul>
            	<jstl:forEach var="crewMember" items="${lastLegCrewMembers}">
                	<li>
                    	<strong>Employee Code:</strong> ${crewMember.employeeCode} <br />
                    	<strong>Phone Number:</strong> ${crewMember.phoneNumber} <br />
                    	<strong>Language Skills:</strong> ${crewMember.languageSkills} <br />
                	</li>
            	</jstl:forEach>
        	</ul>
    	</td>
	</tr>

    <tr>
        <th scope="row">
            <acme:print code="flight-crew-member.dashboard.form.label.flight-assignments-by-status"/>
        </th>
        <td>
        <ul>
            <jstl:forEach var="entry" items="${flightAssignmentsByStatus}">
                <li>${entry.key}: ${entry.value}</li>
            </jstl:forEach>
        </ul>
    	</td>
    </tr>
	<tr>
        <th scope="row">
            <acme:print code="flight-crew-member.dashboard.form.label.flight-assignment-stats-last-month"/>
        </th>
        <td>
			<jstl:forEach var="entry" items="${flightAssignmentStatsLastMonth}">
    			<li>${entry.key}: ${entry.value}</li>
			</jstl:forEach>
		</td>
    </tr>
</table>

<acme:return/>
