<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="paymentDetails">
    
    <form:form modelAttribute="owner" class="form-horizontal" id="add-paymentDetails-form">
    <c:if test = "${ not empty OKmessage}">
   <div class="alert-success" role="alert">
  <c:out value = "${OKmessage}"/>
</div>
</c:if>
        <div class="form-group has-feedback">
            <petclinic:inputField label="Credit Card Number" name="creditCardNumber"/>
            <petclinic:inputField label="CVV" name="cvv"/>
            <petclinic:selectField  size = "3" names = "${months}" label="Expiration Month" name="expirationMonth" />
            <petclinic:selectField size = "3" names = "${years}" label="Expiration Year" name="expirationYear"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                        <button class="btn btn-default" type="submit">Save</button>
            </div>
        </div>
    </form:form>
</petclinic:layout>