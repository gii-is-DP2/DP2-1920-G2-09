<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	

<petclinic:layout pageName="Walks">
<div id="walksImg" >
<c:forEach items="${walks}" var="walk">
<div class="gallery">
   <spring:url value="/walks/{walkId}" var="walkUrl">
   <spring:param name="walkId" value="${walk.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(walkUrl)}">
    <img id ="walkMap"  src=" <c:out value="${walk.map}" />" width="20%" height="20%">
  </a>
  <div class = "texto-walk">
    <div id="walkName"><c:out value="${walk.name}"/></div>
    
    
     <br>Description: <div id="walkDescription"><c:out value="${walk.description}"/></div> 
    </div>
</div>

</c:forEach>
</div>

  
</petclinic:layout>

