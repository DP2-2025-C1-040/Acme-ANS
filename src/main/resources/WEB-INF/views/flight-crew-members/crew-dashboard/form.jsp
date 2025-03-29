
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
            <jstl:choose>
                <jstl:when test="${not empty lastFiveDestinations}">
                    <acme:print value="${lastFiveDestinations}"/>
                </jstl:when>
                <jstl:otherwise>
                    <p>No se encontraron destinos recientes.</p>
                </jstl:otherwise>
            </jstl:choose>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:print code="flight-crew-member.dashboard.form.label.legs-by-incident-severity"/>
        </th>
        <td>
            <jstl:choose>
                <jstl:when test="${not empty legsByIncidentSeverity}">
                    <acme:print value="${legsByIncidentSeverity}"/>
                </jstl:when>
                <jstl:otherwise>
                    <p>No se encontraron registros de incidentes.</p>
                </jstl:otherwise>
            </jstl:choose>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:print code="flight-crew-member.dashboard.form.label.last-leg-crew-members"/>
        </th>
        <td>
            <jstl:choose>
                <jstl:when test="${not empty lastLegCrewMembers}">
                    <acme:print value="${lastLegCrewMembers}"/>
                </jstl:when>
                <jstl:otherwise>
                    <p>No se encontraron miembros de la tripulación en la última pierna de vuelo.</p>
                </jstl:otherwise>
            </jstl:choose>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:print code="flight-crew-member.dashboard.form.label.flight-assignments-by-status"/>
        </th>
        <td>
            <jstl:choose>
                <jstl:when test="${not empty flightAssignmentsByStatus}">
                    <acme:print value="${flightAssignmentsByStatus}"/>
                </jstl:when>
                <jstl:otherwise>
                    <p>No se encontraron asignaciones de vuelo por estado.</p>
                </jstl:otherwise>
            </jstl:choose>
        </td>
    </tr>
    <tr>
        <th scope="row">
            <acme:print code="flight-crew-member.dashboard.form.label.flight-assignment-stats-last-month"/>
        </th>
        <td>
            <jstl:choose>
                <jstl:when test="${not empty flightAssignmentStatsLastMonth}">
                    <acme:print value="${flightAssignmentStatsLastMonth}"/>
                </jstl:when>
                <jstl:otherwise>
                    <p>No se encontraron estadísticas de asignaciones de vuelo para el último mes.</p>
                </jstl:otherwise>
            </jstl:choose>
        </td>
    </tr>
</table>

<acme:return/>
