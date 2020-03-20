<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="walks">

	<h2>
        <c:if test="${walk['new']}">New </c:if> Walk
    </h2>
    
    <form:form modelAttribute="walk" class="form-horizontal" id="add-walk-form">
        <div class="form-group has-feedback">
            <petclinic:inputField label="Name" name="name"/>
            <petclinic:inputField label="Description" name="description" />
            <petclinic:inputField label="Map URL" name="map"/>
        </div>
        
        
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
            	<c:choose>
                    <c:when test="${walk['new']}">
                        <button class="btn btn-default" type="submit">Add Walk</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit">Update Walk</button>
                    </c:otherwise>
                </c:choose>                  
            </div>
        </div>
    </form:form>
</petclinic:layout>