<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="products">
    
    <form:form modelAttribute="product" class="form-horizontal" id="add-product-form">
        <div class="form-group has-feedback">
            <petclinic:inputField label="Name" name="name"/>
            <petclinic:inputField label="Description" name="description" />
            <petclinic:inputNumberField label="Stock" name="stock" value="${product.stock}"/>
            <petclinic:inputField label="Image URL" name="urlImage"/>
            <petclinic:inputDoubleField label="Unit Price" name="unitPrice" value="${product.unitPrice}" step = "0.1"/>
            <p style="margin-left:9%" ><b> Check if it is available</b> <c:choose><c:when test="${product.available == true }">
            <input type="checkbox"  name="available" checked="checked"/>
            </c:when>
            <c:otherwise><input type="checkbox"  name="available"/></c:otherwise>
            </c:choose></p>
            <div class="control-group">
            <petclinic:selectField name="category" label="Type " names="${categories}" size="4" />
            </div>
        </div>
        <div class="form-group">
         
            <div class="col-sm-offset-2 col-sm-10">
                       <button class="btn btn-default" type="submit">Save Product</button>                   
            </div>
        </div>
    </form:form>
</petclinic:layout>
