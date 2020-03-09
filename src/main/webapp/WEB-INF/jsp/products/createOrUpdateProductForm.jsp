<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="products">
    
    <form:form modelAttribute="product" class="form-horizontal" id="add-product-form" action="/products/new">
        <div class="form-group has-feedback">
            <petclinic:inputField label="Name" name="name"/>
            <petclinic:inputField label="Description" name="description" />
            <petclinic:inputField label="Stock" name="stock"/>
            <petclinic:inputField label="Image URL" name="urlImage"/>
            <petclinic:inputField label="Unit Price" name="unitPrice"/>
            <div class="control-group">
            <petclinic:selectField name="category" label="Type " names="${categories}" size="4" />
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                       <button class="btn btn-default" type="submit">Add Product</button>                   
            </div>
        </div>
    </form:form>
</petclinic:layout>
