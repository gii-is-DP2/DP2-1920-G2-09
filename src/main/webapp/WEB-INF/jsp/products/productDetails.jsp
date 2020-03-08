<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="products">

    <h2>Product Information</h2>

 	<div align="center"><img src=" <c:out value="${product.urlImage}" />" width="20%" height="20%"></div>
    <table class="table table-striped">
           
        <tr>
            <th>Name</th>
            <td><c:out value="${product.name}"/></td>
        </tr>
        <tr>
            <th>Description</th>
            <td><c:out value="${product.description}"/></td>
        </tr>
        <tr>
            <th>Unit Price</th>
            <td><c:out value="${product.unitPrice}"/></td>
        </tr>
        <tr>
            <th>Stock</th>
            <td><c:out value="${product.stock}"/></td>
        </tr>
        <tr>
            <th>Category</th>
            <td><c:out value="${product.category}"/></td>
        </tr>
    </table>

    <br/>
    <br/>
    <br/>
</petclinic:layout>
