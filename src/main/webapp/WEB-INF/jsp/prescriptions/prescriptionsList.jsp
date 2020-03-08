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
            <th style="width: 150px;">Date</th>
            <th style="width: 120px">Title</th>
            <th style="width: 120px">Veterinarian</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${selections}" var="p">
            <tr>
               
                <td>
                    <c:out value="${p.date}"/>
                </td>
                <td>
                	<c:out	value="${p.name}"/>
                	</td>
                <td>
                   	Veterinario
                </td>
               
                <td>
                   	<spring:url value="/owners/{ownerId}/pets/{petId}/prescriptions/{prescriptionId}" var="viewUrl">
							<spring:param name="prescriptionId" value="${p.id}" />
							<spring:param name="ownerId" value="${p.pet.owner.id}" />
							<spring:param name="petId" value="${p.pet.id}" />
						</spring:url> <a href="${fn:escapeXml(viewUrl)}"
						class="btn btn-default">View Details</a>
					</td>
                
      
<!--
                <td> 
                    <c:out value="${owner.user.username}"/> 
                </td>
                <td> 
                   <c:out value="${owner.user.password}"/> 
                </td> 
-->
                
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>
