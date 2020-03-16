<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

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
        <tr>
            <th>Available?</th>
            <td><c:choose><c:when test="${product.available == true }">
            Yes
            </c:when>
            <c:otherwise>Not now</c:otherwise>
            </c:choose></td>
        </tr>
    </table>
    <sec:authorize access="hasAnyAuthority('admin')">
    <spring:url value="{productId}/edit" var="editUrl">
        <spring:param name="productId" value="${product.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Edit Product</a>
	</sec:authorize>
	<sec:authorize access="isAuthenticated()">
		<c:if test="${product.available == false}">
   			<spring:url value="{productId}/edit" var="buyUrl">
        	<spring:param name="productId" value="${product.id}"/>
    		</spring:url>
    		<a href="${fn:escapeXml(buyUrl)}" class="btn btn-default">Buy Product</a>
    	</c:if>
	</sec:authorize>
    <br/>
    <br/>
    <br/>
</petclinic:layout>
