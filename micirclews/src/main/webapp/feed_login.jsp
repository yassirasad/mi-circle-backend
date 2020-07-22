<%@ page session="false" %>

<!DOCTYPE html>
<html>
<head>
    <title>Login Feed</title>
    <link rel="stylesheet" href="/css/style.css">
    <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap-theme.min.css">
    <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js" ></script>
    <script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
</head>
<body>

<div class="login-marg">
        <div class="container" role="main">
            <div class="col-lg-4"></div>
            <form class="col-lg-4" method="post" action="#">
                <img src="/mi_Circle_logo.png" class="img-responsive login-img"/>
                <input type="text" class="form-control input-lg" placeholder="Username" name="username" />
                <br/>
                <input type="password" class="form-control input-lg" placeholder="Password" name="password">
                <br/>

                <%-- With JSP Scripting elements --%>
                <%--
                <% String login_msg=(String)request.getAttribute("error");
                    if(login_msg!=null){ %>
                        <li style="color:red"><%=login_msg%></li><br/>
                 <% } %>
                 --%>

                <%-- With JSTL --%>
                <c:if test="${not empty requestScope.error}">
                    <li style="color:red"> <c:out value="${requestScope.error}"/> </li><br/>
                </c:if>

                <button class="btn btn-default btn-primary btn-group" type="submit" id="login">Login</button>
            </form>
        </div>
</div>

</body>
</html>
