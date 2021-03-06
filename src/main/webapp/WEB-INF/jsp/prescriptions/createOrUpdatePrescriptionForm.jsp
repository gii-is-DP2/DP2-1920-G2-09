<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


<petclinic:layout pageName="prescriptions">
    <jsp:attribute name="customScript">
        <script>
            $(function () {
                $("#dateInicio").datepicker({dateFormat: 'yy/mm/dd'});
                $("#dateFinal").datepicker({dateFormat: 'yy/mm/dd'});
            });
        </script>
    </jsp:attribute>
    <jsp:body>
        <h2><c:if test="${prescription['new']}">New </c:if>Prescription</h2>

        <b>Pet</b>
        <table class="table table-striped">
            <thead>
            <tr>
                <th>Name</th>
                <th>Birth Date</th>
                <th>Type</th>
                <th>Owner</th>
            </tr>
            </thead>
            <tr>
                <td><c:out value="${prescription.pet.name}"/></td>
                <td><petclinic:localDate date="${prescription.pet.birthDate}" pattern="yyyy/MM/dd"/></td>
                <td><c:out value="${prescription.pet.type.name}"/></td>
                <td><c:out value="${prescription.pet.owner.firstName} ${prescription.pet.owner.lastName}"/></td>
           		
            </tr>
        </table>

        <form:form modelAttribute="prescription" class="form-horizontal">
            <div class="form-group has-feedback">
                <petclinic:inputField label="Start Date" name="dateInicio"/>
                <petclinic:inputField label="End Date" name="dateFinal"/>
                <petclinic:inputField label="Title" name="name"/>
                <petclinic:inputField label="Description" name="description"/>
            </div>

            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <input type="hidden" name="petId" value="${prescription.pet.id}"/>
                    
                    <button class="btn btn-default" type="submit">Add Prescription</button>
                </div>
            </div>
        </form:form>

         <br/>
        <b>Previous Prescription</b>
        <table class="table table-striped">
            <tr>
                <th>Date</th>
                <th>Title</th>
                <th>Veterinarian</th>
            </tr>
            <c:forEach var="previa" items="${previa}" end="2" >
                
                    <tr>
                        <td><petclinic:localDate date="${previa.dateInicio}" pattern="yyyy/MM/dd"/></td>
                        <td><c:out value="${previa.name}"></c:out>
                        <td><c:out value="${previa.vet.user.username}"/></td>
                    </tr>
            </c:forEach>
        </table>
        
    </jsp:body>

</petclinic:layout>
