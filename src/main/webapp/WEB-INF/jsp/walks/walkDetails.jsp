<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

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
    
    
    
</petclinic:layout>
