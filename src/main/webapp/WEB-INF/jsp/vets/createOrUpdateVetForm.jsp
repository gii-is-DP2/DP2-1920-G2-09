<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="vets">
    <h2>
        Save Vet
    </h2>
    <form:form modelAttribute="vet" class="form-horizontal" id="add-vet-form">
        <div class="form-group has-feedback">
            <petclinic:inputField label="Name" name="firstName"/>
            <petclinic:inputField label="Surname" name="lastName"/>
            <petclinic:selectFieldVet label="Specialties" name="specialties" size="1" names="${specialties}" itemLabel="name" itemValue="id"/>
            	<hr>
            	<petclinic:inputField label="User" name="user.username"/>
            	<petclinic:passwordField label="Password" name="user.password"/>
            	
       			<div class="form-group">
       			
            		<div class="col-sm-offset-2 col-sm-10">
                		
                       			<button class="btn btn-default" type="submit">Save vet</button>
                   			
            		</div>
        		</div>
        	
   		</div>
   		</form:form>
   
</petclinic:layout>