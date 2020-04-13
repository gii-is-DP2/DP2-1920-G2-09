<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
	<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<petclinic:layout pageName="products">

    <h2>Product Information</h2>

 	<div align="center"><img id="imgProducto" src=" <c:out value="${product.urlImage}" />" width="20%" height="20%"></div>
    <table class="table table-striped">
           
        <tr>
            <th>Name</th>
            <td id ="NombreProducto"><c:out value="${product.name}"/></td>
        </tr>
        <tr>
            <th>Description</th>
            <td id="DescripcionProducto"><c:out value="${product.description}"/></td>
        </tr>
        <tr>
            <th>Unit Price</th>
            <td id="PrecioProducto"><c:out value="${product.unitPrice}"/></td>
        </tr>
        <tr>
            <th>Stock</th>
            <td id="stockProducto"><c:out value="${product.stock}"/></td>
        </tr>
        <tr>
            <th>Category</th>
            <td id="categoryProducto"><c:out value="${product.category}"/></td>
        </tr>
        
         <tr>
            <th>Rating</th>
            <td id="RatingProducto"><fmt:formatNumber minFractionDigits="2" type="number" maxFractionDigits="2" value="${rating}"/></td>
        </tr>
        <tr>
            <th>Available?</th>
            <td id ="avaliableProducto"><c:choose><c:when test="${product.available == true }">
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
	
		<sec:authorize access="hasAnyAuthority('owner')">
	 <form:form modelAttribute="item" class="form-horizontal" id="add-product-to-shoppingCart" action="/products/add-item/${productId}">
		<c:if test="${product.available == true}">
			<petclinic:inputNumberField label="Quantity" name="quantity" value="${item.quantity}" min="1"/>
    		<button class="btn btn-default">Add to Shopping Cart</button>
    	</c:if>
	</form:form>
	</sec:authorize>
	<c:if test="${ not empty errorMessage }">
	<div class="alert alert-danger" role="alert">
  <c:out   value="${errorMessage}" ></c:out>
</div>
</c:if>
	
<!--  COMENTARIOS Y VALORACIONES -->
 	 <sec:authorize access="hasAnyAuthority('veterinarian','owner')">
 	 <c:if test = "${not empty OKmessage}">
   <div class="alert-success" role="alert">
  <c:out value = "${OKmessage}"/>
</div>
</c:if>
	<sec:authorize access="isAuthenticated()">
	<form:form modelAttribute="productComent" class="form-horizontal" id="add-productComent-form" action ="/products/${product.id}/add-product-coment/">
	<petclinic:inputField label="Title" name="title"/>
	<petclinic:inputField label="Description" name="description"/>
	<petclinic:selectField label="Rating" name="rating" size="6" names="${[0,1,2,3,4,5]}"></petclinic:selectField>
	<button class="btn btn-default" type="submit">Submit Comment </button>
</form:form>
</sec:authorize>
</sec:authorize>
    <br/>
    <br/>
    <br/>
    
    
<!--  LISTA DE COMENTARIOS -->

<c:if test = "${ not empty OKDeletemessage}">
   <div class="alert-success" role="alert">
  <c:out value = "${OKDeletemessage}"/>
</div>
</c:if>
<div class="row bootstrap snippets">
    <div class="col-md-6 col-md-offset-2 col-sm-12">
        <div class="comment-wrapper">
            <div class="panel panel-info">
                <div class="panel-heading">
                    Comment panel
                </div>
                <div id="comentarios" class="panel-body">
                    <ul class="media-list">
                    <c:forEach items="${coments}" var = "coment">
                    <c:if test="${coment.highlight == true }">
                        <li class="media">
                            <div class="media-body-vet">
                            
                                <span class="text-muted pull-right">
                                    <small  id= "FechaPrioritario"class="text-muted"><c:out value = "${coment.postDate}" /></small>
                                </span>
                                <strong id="UsernamePrioritario" class="text-success"><c:out value = "${coment.user.username}" /></strong> <br>
                                <strong id="TituloPrioritario" class ="text-info"><c:out value = "${coment.title}" /> </strong> <br>
											<p id="DescriptionPrioritario">
												<c:out value="${coment.description}" />
											</p>
										</div>
                        </li>
                        </c:if>
                        </c:forEach>
                        <c:forEach items="${coments}" var = "coment">
                        <c:if test="${coment.highlight == false }">
                        <li class="media">
                            <div class="media-body">
										<span class="text-muted pull-right"> <small id="fechaComentario"
											class="text-muted"><c:out value="${coment.postDate}" /></small>
											<sec:authorize access="hasAnyAuthority('admin')">
												<spring:url
													value="/products/{productId}/delete-product-coment/{productComentId}"
													var="productComentDeleteUrl">
													<spring:param name="productId" value="${product.id}" />
													<spring:param name="productComentId" value="${coment.id}" />
												</spring:url>
												<a href="${fn:escapeXml(productComentDeleteUrl)}">Eliminar Comentario</a>
											</sec:authorize>
										</span> <strong id="usuarioComentario" class="text-success"><c:out value = "${coment.user.username}" /></strong> <br>
                                <strong  id="tituloComentario" class ="text-info"><c:out value = "${coment.title}" /> </strong> <br>
                                <p id="descriptionComentario"> <c:out value = "${coment.description}" /></p>
                            </div>
                        </li>
                        </c:if>
                        </c:forEach>
                    </ul>
                </div>
            </div>
        </div>

    </div>
</div>
    
    
</petclinic:layout>
