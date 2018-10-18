<%@ page import="java.util.Random" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: hu
  Date: 2018/10/4
  Time: 17:21
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
    <title>PLS Login</title>
</head>
<body class="text-center">

<form id="loginApp" class="form-login">
    <img v-on:click="reload()" class="mb-4"
         src="<%=request.getContextPath()+"/static/image/duola-a-meng/Do0"+(new Random().nextInt(8)+1)+".PNG"%>" alt=""
         width="150" height="150">
    <h1 class="h3 mb-3 font-weight-normal">Pre-label Service</h1>
    <label for="inputUsername" class="sr-only">username</label>
    <input v-model="username" type="text" id="inputUsername" class="form-control" placeholder="your username" required
           autofocus>
    <label for="inputPassword" class="sr-only">password</label>
    <input v-model="password" type="password" id="inputPassword" class="form-control" placeholder="your password"
           required>
    <button class="btn btn-lg btn-success btn-block" type="submit">Sign In</button>
    <div class="row mt-2">
        <div class="col">
            <button v-on:click="guestLogin" class="btn btn-lg btn-secondary btn-block" type="button">Guest</button>
        </div>
        <div class="col">
            <button class="btn btn-lg btn-info btn-block" type="button">Register</button>
        </div>
    </div>
    <p class="mt-5 mb-3 text-muted">&copy; 2017-2018</p>
</form>

<script src="<c:url value="/static/dist/js/jquery-3.3.1.js"/>"></script>
<script src=<c:url value="/static/dist/js/bootstrap.js"/>></script>
<!--  Vue javascript -->
<script src="<c:url value="/static/dist/js/vue.js"/>"></script>
<!-- Custom javascript -->
<script src="<c:url value="/static/js/login.js"/>"></script>
</body>
</html>
