<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<petclinic:layout pageName="orders">

<c:if test="${ not empty okMessage }">
	<div class="alert-success" role="alert">
  <c:out   value="${okMessage}" ></c:out>
</div> 
</c:if>
	<table id="ordersTable" class="table table-striped">
		<thead>
			<tr>
				<th>User</th>
				<th>Order Date</th>
				<th>Total Price</th>
				<th></th>
				<sec:authorize access="hasAnyAuthority('admin')">
				<th></th>
				</sec:authorize>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${orders}" var="order">
				<tr>
					<td id="ownerOrder"><c:out value="${order.owner.firstName} ${order.owner.lastName}" /></td>
					<td id="orderDateOrder"><c:out value="${order.orderDate}" /></td>
					<td id="totalPriceOrder"><c:out value="${order.totalPrice}" /></td>
					<td id="details">
						<spring:url value="/orders/{orderId}" var="viewURL">
						<spring:param name="orderId" value="${order.id}" /></spring:url> 
						<a href="${fn:escapeXml(viewURL)}" class="btn btn-default"> View Details </a>
					</td>
					<sec:authorize access="hasAnyAuthority('admin')">
					<td id="remove">
						<spring:url value="/orders/delete/{orderId}" var="deleteURL">
						<spring:param name="orderId" value="${order.id}" /></spring:url> 
						<a href="${fn:escapeXml(deleteURL)}" class="btn btn-default"> Cancel Order </a>
					</td>
					</sec:authorize>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
	

</petclinic:layout>