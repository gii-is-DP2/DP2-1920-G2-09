<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<petclinic:layout pageName="walks">

    <h2>Walk Information</h2>

 	<div align="center"><img src=" <c:out value="${walk.map}" />" width="20%" height="20%"></div>
    <table class="table table-striped">
           
        <tr>
            <th>Name</th>
            <td><c:out value="${walk.name}"/></td>
        </tr>
        <tr>
            <th>Description</th>
            <td><c:out value="${walk.description}"/></td>
        </tr>
    
        <tr>
            <th>Rating</th>
            <td><fmt:formatNumber minFractionDigits="2" type="number" maxFractionDigits="2" value="${rating}"/>
        </tr>
    </table>
    

  	<sec:authorize access="hasAnyAuthority('admin')">
    <spring:url value="{walkId}/edit" var="editUrl">
        <spring:param name="walkId" value="${walk.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default">Edit Walk</a>
	</sec:authorize>
	<br/>
	<br/>
	<br/>
	
	 <sec:authorize access="hasAnyAuthority('admin')">
    <spring:url value="{walkId}/delete" var="deleteUrl">
        <spring:param name="walkId" value="${walk.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(deleteUrl)}" class="btn btn-default">Delete Walk</a>
	</sec:authorize>
    <br/>
    <br/>
    <br/>
    <br/>

	<h2>Comments</h2>

    <!--  Añadir Comentarios -->
 	<c:if test = "${ not empty OKmessage}">
   		<div class="alert-success" role="alert">
  			<c:out value = "${OKmessage}"/>
		</div>
	</c:if>
	<sec:authorize access="isAuthenticated()">
		<form:form modelAttribute="walkComent" class="form-horizontal" id="add-walkComent-form" action ="/walks/${walk.id}/add-walk-coment/" >
			<petclinic:inputField label="Title" name="title"/>
			<petclinic:inputField label="Description" name="description"/>
			<petclinic:selectField label="Rating" name="rating" size="6" names="${[0,1,2,3,4,5]}"></petclinic:selectField>
			<button class="btn btn-default" type="submit">Submit Comment </button>
		</form:form>
	</sec:authorize>


    <br/>
    <br/>
    <br/>

    
<!--  Show Comentarios -->


<table class="table table-striped">
        <c:forEach var="coment" items="${coments}">

            <tr>
                <td valign="top">
                    <dl class="dl-horizontal">
                    	<dt>Title</dt>
                        <dd><c:out value="${coment.title}"/></dd></dd>
                        <dt>Description</dt>
                        <dd><c:out value="${coment.description}"/></dd>
                        
                        
                        <dt>Name</dt>
                        <dd><c:out value="${coment.user.username}"/> </dd>
                        <dt>Post Date</dt>
                        <dd><petclinic:localDate date="${coment.postDate}" pattern="yyyy-MM-dd"/></dd>
                        
                    </dl>
                    
                </td>
				<sec:authorize access="hasAnyAuthority('admin')">	
				<td valign="top">
					<table class="table-condensed">
						<thead>
							<tr>
								<th>Delete</th>
							</tr>
						</thead>
						<tr>
							<td>
							
							<spring:url value="/walks/{walkId}/walkComents/{walkComentId}/delete"
									var="deleteComentUrl">
									<spring:param name="walkId" value="${walk.id}" />
									<spring:param name="walkComentId" value="${coment.id}" />
								</spring:url> <a href="${fn:escapeXml(deleteComentUrl)}">Delete Coment</a>
								</td>
						</tr>
					</table>
				</td></sec:authorize>
			</tr>

        </c:forEach>
    </table>
    
  

    
    
    
</petclinic:layout>
