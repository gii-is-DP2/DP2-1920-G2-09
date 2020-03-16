<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
	<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<petclinic:layout pageName="prescriptions">

    <h2>Prescription Information</h2>

  <table class="table table-striped">
        <tr>
            <th>Title</th>
            <td><b><c:out value="${prescription.name}"/></b></td>
        </tr>
        <tr>
            <th>Date</th>
            <td><petclinic:localDate date="${prescription.dateInicio}"
										pattern="yyyy-MM-dd" /></td>
        </tr>
        <tr>
            <th>Veterianrian</th>
            <td><b><c:out value="${prescription.vet.firstName} ${prescription.vet.lastName}"/></b></td>
        </tr>
        <tr>
            <th>Description</th>
            <td><c:out value="${prescription.description}"/></td>
        </tr>
    </table>
  
</petclinic:layout>
