<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="prescriptions">
    <h2>Prescriptions</h2>

    <table id="prescriptionsTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 30%;">Date</th>
            <th style="width: 30%">Title</th>
            <th style="width: 30%">Veterinarian</th>
            <th>       </th>
        </tr>
        </thead>
        <tbody>

        <c:forEach items="${selections}" var="p">
       
            <tr>
               
                <td>
                    <div id="Fecha"><c:out value="${p.dateInicio}"/></div>
                </td>
                <td>
                	<div id="Titulo"><c:out	value="${p.name}"/></div>
                	</td>
                <td>
                   	<div id="Veterinario"><c:out value="${p.vet.firstName} ${p.vet.lastName}"/> </div>
                </td>
               
                <td>
                   	<spring:url value="/owners/{ownerId}/pets/{petId}/prescriptions/{prescriptionId}" var="viewUrl">
							<spring:param name="prescriptionId" value="${p.id}" />
							<spring:param name="ownerId" value="${p.pet.owner.id}" />
							<spring:param name="petId" value="${p.pet.id}" />
						</spring:url> <a href="${fn:escapeXml(viewUrl)}"
						class="btn btn-default">View Details</a>
					</td>
                
                
            </tr>
             
        </c:forEach>
       
        </tbody>
    </table>
</petclinic:layout>
