<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
	<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<petclinic:layout pageName="order">

    <h2>Order Information</h2>

  	<table class="table table-striped">
        <tr>
            <th>User</th>
            <td><b><c:out value="${order.owner.firstName} ${order.owner.lastName}"/></b></td>
        </tr>
        <tr>
            <th>Order Date</th>
            <td><petclinic:localDate date="${order.orderDate}" pattern="yyyy-MM-dd" /></td>
        </tr>
        <tr>
            <th>Total Price</th>
            <td><b><c:out value="${order.totalPrice}"/></b></td>
        </tr>
    </table>
    
    <h4> Productos del Pedido </h4>
    
    <table class="table table-striped">
        <thead>
			<tr>
				<th>Name</th>
				<th>Quantity</th>
				<th>Unit Price</th>
				<th>Total Price</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${items}" var="item">
				<tr>
					<td id="nameProductOrder"><c:out value="${item.product.name}" /></td>

					<td id="quantityProductOrder"><c:out value="${item.quantity}" /></td>

					<td id="precioProductoOrder"><c:out value="${item.unitPrice} euros" /></td>

					<td id="precioTotalOrder"><c:out value="${item.unitPrice*item.quantity} euros" /></td>
				</tr>
			</c:forEach>
		</tbody>
    </table>
    
    <spring:url value="/orders/delete/{orderId}" var="deleteURL">
						<spring:param name="orderId" value="${order.id}" /></spring:url> 
						<a href="${fn:escapeXml(deleteURL)}" class="btn btn-default"> Cancel Order </a>
  
</petclinic:layout>
