<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	

<petclinic:layout pageName="Products">

	  
    	
    	
    	<div class="form-group" >
    	
            <div class="control-group" id="lastName" style= "padding-left: 30px">
                <label class="col-sm-2 control-label">Product Category:</label>
           
            
           
                 <c:forEach  items="${products}" var="product" >   
                		<a href="/products/filterByCategory?category= <c:out value="${product.category}"/> ">
                    	<c:out value="${product.category}"/></a>&nbsp;&nbsp;&nbsp;&nbsp;
                 </c:forEach> 
            </div>
          
        </div>
        
        <br>
        <br>
    	
   <form:form  modelAttribute="product" action="/products/filter" method="get" class="form-horizontal"
               id="search-product-form">
        <div class="form-group">
            <div class="control-group" id="lastName">
                <label class="col-sm-2 control-label">Product Name:</label>
                <div class="col-sm-10">
                    <form:input class="form-control" path="name" size="30" maxlength="80"/>
                    <span class="help-inline"><form:errors path="*"/></span>
                </div>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button type="submit" class="btn btn-default">Search Product</button>
            </div>
        </div>

    </form:form>
    
    
                     

    
    <div id="productList">
<c:if test="${not empty noItemsMessage }">
		<div  id ="Mensaje no hay producto" class="alert alert-warning" role="alert">   <c:out value="${noItemsMessage }"></c:out> </div>		</c:if>
<c:forEach  items="${products}" var="product">
	<div id= "infoProducto" class="gallery">
   		<spring:url value="/products/{productId}" var="productUrl">
   			<spring:param name="productId" value="${product.id}"/>
    	</spring:url>
    	<a href="${fn:escapeXml(productUrl)}">
    	<img src=" <c:out value="${product.urlImage}" />" width="20%" height="20%">
  		</a>
  		<div  class = "texto-producto">
    		<c:out value="${product.name}"/><br>
     		Price: <c:out value="${product.unitPrice}"/> euros <br>
    		Stock: <c:out value="${product.stock}"/> 
    		<c:if test="${product.stock > 0}">
    			Available
    		</c:if>
    		<c:if test="${product.stock == 0}">
    			Unavailable
    		</c:if>
    	</div>
	</div>
	
</c:forEach>

</div>


<div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
            <a href="/products/all">
                <button type="button" class="btn btn-default"> 
                
                    Volver</button>
            </a>
           	 </div>
        </div>

</petclinic:layout>

