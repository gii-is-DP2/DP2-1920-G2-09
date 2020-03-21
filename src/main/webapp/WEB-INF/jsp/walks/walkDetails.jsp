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
    
    <!--  COMENTARIOS Y VALORACIONES -->
 	<c:if test = "${ not empty OKmessage}">
   		<div class="alert-success" role="alert">
  			<c:out value = "${OKmessage}"/>
		</div>
	</c:if>
	<sec:authorize access="isAuthenticated()">
		<form:form modelAttribute="walkComent" class="form-horizontal" id="add-walkComent-form" action ="/walks/${walk.id}/add-walk-coment/">
			<petclinic:inputField label="Title" name="title"/>
			<petclinic:inputField label="Description" name="description"/>
			<petclinic:selectField label="Rating" name="rating" size="6" names="${[0,1,2,3,4,5]}"></petclinic:selectField>
			<button class="btn btn-default" type="submit">Submit Comment </button>
		</form:form>
	</sec:authorize>

    <br/>
    <br/>
    <br/>
    
    
<!--  LISTA DE COMENATARIOS -->


<div class="row bootstrap snippets">
    <div class="col-md-6 col-md-offset-2 col-sm-12">
        <div class="comment-wrapper">
            <div class="panel panel-info">
                <div class="panel-heading">
                    Comment panel
                </div>
                <div class="panel-body">
                    <ul class="media-list">
                    <c:forEach items="${coments}" var = "coment">
                        <li class="media">
                            <div class="media-body">
                                <span class="text-muted pull-right">
                                    <small class="text-muted"><c:out value = "${coment.postDate}" /></small>
                                </span>
                                <strong class="text-success"><c:out value = "${coment.user.username}" /></strong> <br>
                                <strong class ="text-info"><c:out value = "${coment.title}" /> </strong> <br>
                                <p> <c:out value = "${coment.description}" /></p>
                            </div>
                        </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>
        </div>

    </div>
</div>
    
  
    
    
    
</petclinic:layout>
