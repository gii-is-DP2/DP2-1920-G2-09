<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="vets">
    <h2>
        <c:if test="${vet['new']}">Nuevo </c:if> Veterinario
    </h2>
    <form:form modelAttribute="vet" class="form-horizontal" id="add-vet-form">
        <div class="form-group has-feedback">
            <petclinic:inputField label="Nombre" name="firstName"/>
            <petclinic:inputField label="Apellidos" name="lastName"/>
            <petclinic:selectFieldVet label="Especialidades" name="specialties" size="1" names="${specialties}" itemLabel="name" itemValue="id"/>
            	<hr>
            	<petclinic:inputField label="Usuario" name="user.username"/>
            	<petclinic:passwordField label="Contraseña" name="user.password"/>
            	
       			<div class="form-group">
       			
            		<div class="col-sm-offset-2 col-sm-10">
                		<c:choose>
                   			<c:when test="${vet['new']}">
                       			<button class="btn btn-default" type="submit">Añadir veterinario</button>
                   			</c:when>
                		 	<c:otherwise>
                       			<button class="btn btn-default" type="submit">Actualizar veterinario</button>
                   			</c:otherwise>
                		</c:choose>
            		</div>
        		</div>
        	
   		</div>
   		</form:form>
   
</petclinic:layout>