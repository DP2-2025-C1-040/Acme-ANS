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
                	<li>
                    	<acme:print value="${entry.key}"/>: <acme:print value="${entry.value}"/>
                	</li>
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
                    	<strong>Employee Code:</strong> <acme:print value="${crewMember.employeeCode}"/> <br />
                    	<strong>Phone Number:</strong> <acme:print value="${crewMember.phoneNumber}"/> <br />
                    	<strong>Language Skills:</strong> <acme:print value="${crewMember.languageSkills}"/> <br />
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
                	<li>
                    	<acme:print value="${entry.key}"/>: <acme:print value="${entry.value}"/>
                	</li>
            	</jstl:forEach>
        	</ul>
    	</td>
    </tr>

	<tr>
        <th scope="row">
            <acme:print code="flight-crew-member.dashboard.form.label.flight-assignment-stats-last-month"/>
        </th>
        <td>
			<ul>
    			<jstl:forEach var="entry" items="${flightAssignmentStatsLastMonth}">
        			<li>
            			<acme:print value="${entry.key}"/>: <acme:print value="${entry.value}"/>
        			</li>
    			</jstl:forEach>
			</ul>
		</td>
    </tr>
</table>

<acme:return/>

