<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="admin">

	<table id="adminTable" class="table table-striped">
		<thead>
			<tr>
				<th style="width: 150px;">Action</th>
				<th style="width: 200px;">Link</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td>AÑADIR PRODUCTO</td>
				<td><spring:url value="/products/new" var="ownerUrl">
					</spring:url> <a href="${fn:escapeXml(ownerUrl)}">Añadir Producto</a></td>
			</tr>
			<tr>
				<td>AÑADIR PASEO</td>
				<td><spring:url value="/walks/new" var="walkUrl">
					</spring:url> <a href="${fn:escapeXml(walkUrl)}">Añadir Paseo</a></td>
			</tr>
			<tr>
				<td>VER PEDIDOS</td>
				<td><spring:url value="/orders/list" var="orderUrl">
					</spring:url> <a href="${fn:escapeXml(orderUrl)}">Ver pedidos</a></td>
			</tr>
		</tbody>
	</table>

</petclinic:layout>