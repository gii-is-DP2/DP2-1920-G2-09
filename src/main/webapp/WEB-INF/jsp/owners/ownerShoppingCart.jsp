<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<c:set var="total" value="0"></c:set>
<c:set var="totalQuantity" value="0"></c:set>
<petclinic:layout pageName="ownerShoppingCart">
	<c:if test="${ not empty shoppingCartSuccess}">
		<div class="alert-success" role="alert">
			<c:out value="${shoppingCartSuccess}" />
		</div>
	</c:if>
	<table id="itemsTable" class="table table-striped">
		<thead>
			<tr>
				<th style="width: 20%;">Product Name</th>
				<th style="width: 20%;">Product Description</th>
				<th style="width: 10%">Quantity</th>
				<th style="width: 15%">Unit Price</th>
				<th style="width: 15%">Total Price</th>
				<th style="width: 10%"></th>
				<th style="width: 5%">Change Quantity</th>
			</tr>
		</thead>
		<tbody id="items">
		
			<c:forEach items="${items}" var="item">
				
				<tr>
					<td id="nameProductoCart"><c:out value="${item.product.name}" /></td>
					<td id="descriptionProductoCart"><c:out value="${item.product.description}" /></td>

					<td id="quantityProductoCart"><c:out value="${item.quantity}" /></td>

					<td id="precioProductoCart"><c:out value="${item.unitPrice} euros" /></td>

					<td id="precioTotalCart"><c:out value="${item.unitPrice*item.quantity} euros" /></td>

					<td><spring:url value="/products/delete-item/{itemId}"
							var="deleteURL">
							<spring:param name="itemId" value="${item.id}" />
						</spring:url> <a href="${fn:escapeXml(deleteURL)}" class="btn btn-default">Remove
							Item</a></td>

					<td><form:form action="/products/edit-item/${item.id}"
							modelAttribute="item" class="form-horizontal"
							id="edit-item-shoppingCart">

							<petclinic:inputNumberField icons="false"
								max="${ item.quantity + item.product.stock}" min="1" label=""
								name="quantity" value="${item.quantity}" />
							<button class="btn btn-default">Edit Item Quantity</button>
						</form:form></td>

				</tr>

				<c:set var="total" value="${total + item.unitPrice*item.quantity}"></c:set>
				<c:set var="totalQuantity" value="${totalQuantity + item.quantity}"></c:set>
				
			</c:forEach>
		</tbody>
	</table>
	<c:if test="${ not empty errorMessage }">
		<div class="alert alert-danger" role="alert">
			<c:out value="${errorMessage}"></c:out>
		</div>
	</c:if>



	<div class="row bootstrap snippets">
		<div class="col-md-6 col-md-offset-2 col-sm-12">
			<div class="comment-wrapper">
				<div class="panel panel-info">
					<div class="panel-heading">Order Details</div>
					<div class="panel-body">
						<div class="media-body">
							<span class="text-muted pull-left"> <strong
								class="text-success"><c:out
										value="Number of products: ${totalQuantity}" /></strong> <br>

							</span> <span class="text-muted pull-right"> <strong
								class="text-success"><c:out
										value="Total Price: ${total} euros" /></strong> <br>
							</span>
							<c:if test="${ not empty items}">
							<form:form modelAttribute="shoppingCart" class="form-horizontal"
								id="buy-form" action="/shopping-cart/buy">
								<div class="form-group has-feedback"></div>
								<div class="text-muted pull-2">
									<button class="btn btn-default" type="submit">Buy
										Products</button>
								</div>
							</form:form>
							</c:if>
							<c:if test="${ not empty shoppingCartError}">
								<div class="alert-danger" role="alert">
									<c:out value="${shoppingCartError}" />
								</div>
							</c:if>
						</div>
					</div>
				</div>
			</div>

		</div>
	</div>


</petclinic:layout>
