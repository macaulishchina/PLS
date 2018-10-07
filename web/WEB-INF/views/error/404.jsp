<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: hu
  Date: 2018/10/5
  Time: 0:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="author" content="macaulish">

    <!-- Bootstrap core CSS -->
    <link href="<c:url value="/static/dist/css/bootstrap.min.css"/>" rel="stylesheet"/>
    <!-- Custom styles -->
    <link href="<c:url value="/static/css/login.css"/>" rel="stylesheet"/>

    <link rel="icon" href="<c:url value="/static/image/duola-a-meng/duola-a-meng_67.png"/>">
    <title>404</title>
</head>
<body class="text-center">
<form class="form-login">
    <a href="<c:url value="/"/>"><img class="mb-4" src="<c:url value="/static/image/duola-a-meng/d08.png"/>" alt="404"
                                      width="200" height="200"></a>
    <h1 class="h3 mb-3 font-weight-normal">404 Not Found!</h1>
    <p class="mt-5 mb-3 text-muted">&copy; 2017-2018</p>
</form>

<script src=<c:url value="/static/dist/js/bootstrap.js"/>></script>
</body>
</html>
