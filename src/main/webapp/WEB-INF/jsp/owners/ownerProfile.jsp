<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<petclinic:layout pageName="owners">

    <h2>Owner Information</h2>


    <table class="table table-striped">
        <tr>
            <th>Name</th>
            <td><b><c:out value="${owner.firstName} ${owner.lastName}"/></b></td>
        </tr>
        <tr>
            <th>Address</th>
            <td><c:out value="${owner.address}"/></td>
        </tr>
        <tr>
            <th>City</th>
            <td><c:out value="${owner.city}"/></td>
        </tr>
        <tr>
            <th>Telephone</th>
            <td><c:out value="${owner.telephone}"/></td>
        </tr>
        
        <c:if test="${not empty owner.creditCardNumber  }">
        <tr>
            <th>Credit Card Number</th>
            <td><c:out value="${owner.creditCardNumber}"/></td>
        </tr>
        </c:if>
    </table>



    <spring:url value="{ownerId}/pets/new" var="addUrl">
        <spring:param name="ownerId" value="${owner.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(addUrl)}" class="btn btn-default">Add New Pet</a> <br>

<h1 > MY PETS</h1> 
    <c:forEach items="${owner.pets}" var="pet">
	<div class="gallery">
   		<spring:url value="/owners/{ownerId}/pets/{petId}/edit" var="petUrl">
   			<spring:param name="ownerId" value="${owner.id}"/>
   			<spring:param name="petId" value="${pet.id}"/>
    	</spring:url>
    	<a href="${fn:escapeXml(petUrl)}">
    	<img src="/resources/images/<c:out value="${pet.type}" />.png " width="20%" height="20%">
  		</a>
  		<div class = "texto-producto">
    		<b><c:out value="${pet.name}"/></b><br>
     		<c:out value="${pet.type}"/>
    	</div>
	</div>
</c:forEach>

 <table id="ownersTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 25%;">Start Date</th>
            <th style="width: 25%;">End Date</th>
            <th style="width: 25%;">Name</th>
            <th style="width: 25%;">Download (as PDF)</th>
        </tr>
        </thead>
        <tbody>

 <c:forEach items="${prescriptions}" var="pcs">

            <tr>
               
                <td>
                    <c:out value="${pcs.dateInicio}"/>
                </td>
                <td>
                    <c:out value="${pcs.dateFinal}"/>
                </td>
                <td>
                    <c:out value="${pcs.pet.name}"/>
                </td>
               <td>
                <spring:url value="/owners/profile/prescription-download/{prescriptionId}" var="addUrl">
        <spring:param name="prescriptionId" value="${pcs.id}"/>
    </spring:url>
    <a target="_blank" href="${fn:escapeXml(addUrl)}" class="btn btn-default">Download</a>
          <td>
            </tr>
        </c:forEach>

    	</tbody>
    	</table>
    	 

</petclinic:layout>
