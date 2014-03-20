<%@ page session="false" %>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<jsp:include page="../fragments/header.jsp"/>

<div class="container">

    <div class="row">
        <h1>Error !</h1>
        <p>An error occured...</p>
        <div>
            <button class="btn btn-default" onclick="location.href='login'">Return to login page</button>
        </div>
    </div>

</div> <!-- /container -->

<jsp:include page="../fragments/footer.jsp"/>
