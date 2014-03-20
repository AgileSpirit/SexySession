<%@ page session="false" %>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<jsp:include page="../fragments/header.jsp"/>

<div class="container">

    <div class="row">
        <h1>Congratulations !</h1>
        <p>You have successfully signed in :-)</p>
        <div>
            <button class="btn btn-default" onclick="location.href='logout'">Sign out</button>
        </div>
    </div>

</div> <!-- /container -->

<jsp:include page="../fragments/footer.jsp"/>
