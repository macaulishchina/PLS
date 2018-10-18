<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="top.macaulish.pls.pojo.db.UserEntity" %>
<%--
  Created by IntelliJ IDEA.
  User: hu
  Date: 2018/10/10
  Time: 19:13
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
    <link href="<c:url value="/static/css/main.css"/>" rel="stylesheet"/>

    <link rel="icon" href="<c:url value="/static/image/duola-a-meng/duola-a-meng_67.png"/>">
    <title>PlS-Upload</title>
</head>
<body>

<nav id="toolbarApp" class="navbar navbar-dark fixed-top bg-dark flex-md-nowrap p-0 shadow">
    <a class="navbar-brand col-md-2 mr-0" href="<c:url value="/"/>"><span data-feather="arrow-left"></span>&nbsp;Pre-label
        Service</a>
    <input class="form-control form-control-dark w-100" type="text" placeholder="Search" aria-label="Search">
    <button class="btn btn-dark"><%=((UserEntity) session.getAttribute("user")).getUsername()%>
    </button>
    <input hidden id="hiddenTaskGuid" value='${taskGuid}'/>
    <input hidden id="hiddenUserName" value='<%=((UserEntity)session.getAttribute("user")).getUsername()%>'/>
    <ul class="navbar-nav px-3">
        <li class="nav-item text-nowrap">
            <a v-on:click="logout()" class="nav-link" href="<c:url value="/"/>"><span data-feather="log-out"></span></a>
        </li>
    </ul>
</nav>

<div id="uploadApp" class="container-fluid mt-5">
    <div class="row">
        <main class="offset-1 col-10">
            <div class="d-flex justify-content-start flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3">
                <h1 class="h2">Uploading Files</h1>
                <div class="align-content-center">
                    <input class="btn btn-lg" id="fileupload" type="file" name="file" multiple>
                </div>
            </div>
            <div class="p-2 mt-5 mb-5">
                <div id="progress" class="progress">
                    <div class="progress-bar progress-bar-striped progress-bar-animated" role="progressbar"
                         style="width: 0%" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100">
                    </div>
                </div>

            </div>
            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center">
                <h2>Files Information</h2>
            </div>

            <div class="table-responsive">
                <table class="table table-striped table-sm">
                    <thead>
                    <tr>
                        <th>file name</th>
                        <th>file path</th>
                        <th>file size</th>
                        <th>update time</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr v-for="file in files">
                        <td>{{file.name}}</td>
                        <td>{{file.path}}</td>
                        <td>{{file.size}}</td>
                        <td>{{file.updateTime}}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </main>
    </div>
</div>


<script src="<c:url value="/static/dist/js/jquery-3.3.1.js"/>"></script>
<script src="<c:url value="/static/dist/js/popper.js"/>"></script>
<script src=<c:url value="/static/dist/js/bootstrap.js"/>></script>
<!--  Vue javascript -->
<script src="<c:url value="/static/dist/js/vue.js"/>"></script>
<!-- Custom javascript -->
<script src="<c:url value="/static/js/upload.js"/>"></script>
<%--Icon feather--%>
<script src="<c:url value="/static/dist/js/feather.js"/>"></script>
<%--upload file--%>
<script src="<c:url value="/static/dist/js/jquery.ui.widget.js"/>"></script>
<script src="<c:url value="/static/dist/js/jquery.iframe-transport.js"/>"></script>
<script src="<c:url value="/static/dist/js/jquery.fileupload.js"/>"></script>
<script>
    feather.replace()
</script>
</body>
</html>
