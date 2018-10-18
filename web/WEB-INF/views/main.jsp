<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="top.macaulish.pls.pojo.db.UserEntity" %>
<%@ page import="top.macaulish.pls.kits.JsonConverter" %>
<%--
  Created by IntelliJ IDEA.
  User: hu
  Date: 2018/10/5
  Time: 0:13
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
    <title>PlS</title>
    <script>
        window.onload = function onWindowLoad() {
            mainApp.reloadTasks()
            window.clearInterval(mainApp.timer1);
            if (mainApp.taskQueryAuto) {
                mainApp.timer1 = window.setInterval(mainApp.reloadTasks, 1000)
            }
        };
    </script>
</head>
<body>

<nav id="toolbarApp" class="navbar navbar-dark fixed-top bg-dark flex-md-nowrap p-0 shadow">
    <a class="navbar-brand col-md-2 mr-0" href="<c:url value="/"/>">Pre-label Service</a>
    <input class="form-control form-control-dark w-100" type="text" placeholder="Search" aria-label="Search">
    <button class="btn btn-dark"><%=((UserEntity) session.getAttribute("user")).getUsername()%>
    </button>
    <input hidden id="hiddenUserName" value='<%=((UserEntity)session.getAttribute("user")).getUsername()%>'/>
    <ul class="navbar-nav px-3">
        <li class="nav-item text-nowrap">
            <a v-on:click="logout()" class="nav-link" href="<c:url value="/"/>"><span data-feather="log-out"></span></a>
        </li>
    </ul>
</nav>

<div id="mainApp" class="container-fluid">
    <div class="row">
        <nav class="col-md-2 d-none d-md-block bg-light sidebar">
            <div class="sidebar-sticky">
                <ul class="nav flex-column">
                    <li class="nav-item ">
                        <a v-on:click="navTaskModule()" class="nav-link active" href="#">
                            <span data-feather="inbox"></span>
                            Tasks
                        </a>
                    </li>
                    <li class="nav-item">
                        <a v-on:click="navModelModule()" class="nav-link" href="#">
                            <span data-feather="triangle"></span>
                            Models
                        </a>
                    </li>
                </ul>
            </div>
        </nav>

        <main v-if="showTaskModule()" role="main" class="col-md-10 ml-sm-auto px-4">
            <div class="d-flex justify-content-start flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3">
                <h1 class="h2">Tasks</h1>
                <div class="ml-5 btn-group btn-group-toggle" data-toggle="buttons">
                    <label v-on:click="changePP('private')" class="btn btn-secondary active" for="tag_private">
                        <input type="radio" name="pp_key" id="tag_private" autocomplete="off" checked> Private
                    </label>
                    <label v-on:click="changePP('public')" class="btn btn-secondary" for="tag_public">
                        <input type="radio" name="pp_key" id="tag_public" autocomplete="off"> Public
                    </label>
                </div>
                <div class="ml-2 btn-group btn-group-toggle" data-toggle="buttons">
                    <label v-on:click="changeFilter('all')" class="btn btn-secondary active" for="tag_all">
                        <input type="radio" name="taskFilterKey" id="tag_all" autocomplete="off" checked> All
                    </label>
                    <label v-on:click="changeFilter('new')" class="btn btn-secondary" for="tag_new">
                        <input type="radio" name="taskFilterKey" id="tag_new" autocomplete="off"> New
                    </label>
                    <label v-on:click="changeFilter('ready')" class="btn btn-secondary" for="tag_ready">
                        <input type="radio" name="taskFilterKey" id="tag_ready" autocomplete="off"> Ready
                    </label>
                    <label v-on:click="changeFilter('handling')" class="btn btn-secondary" for="tag_handling">
                        <input type="radio" name="taskFilterKey" id="tag_handling" autocomplete="off"> Handling
                    </label>
                    <label v-on:click="changeFilter('finish')" class="btn btn-secondary" for="tag_finish">
                        <input type="radio" name="taskFilterKey" id="tag_finish" autocomplete="off"> Finish
                    </label>
                    <label v-on:click="changeFilter('pause')" class="btn btn-secondary" for="tag_pause">
                        <input type="radio" name="taskFilterKey" id="tag_pause" autocomplete="off"> Pause
                    </label>
                    <label v-on:click="changeFilter('error')" class="btn btn-secondary" for="tag_error">
                        <input type="radio" name="taskFilterKey" id="tag_error" autocomplete="off"> Error
                    </label>
                    <label v-on:click="changeFilter('stop')" class="btn btn-secondary" for="tag_stop">
                        <input type="radio" name="taskFilterKey" id="tag_stop" autocomplete="off"> Stop
                    </label>
                </div>

                <button v-on:click="reloadTasks()" class="ml-2 btn btn-success">Refresh</button>
                <label for="auto_refresh" class="m-0 ml-2 btn btn-info"><input v-on:click="onAutoButtonClick()"
                                                                               v-model="taskQueryAuto" id="auto_refresh"
                                                                               type="checkbox">&nbsp;Auto</label>
                <label for="model_offline" class="m-0 ml-2 btn btn-info"><input v-model="taskQueryOffline"
                                                                                id="model_offline" type="checkbox">&nbsp;Offline</label>

            </div>
            <div class="row">
                <button onclick="newTaskFrame.initTaskProp()" type="button" class="btn btn-block btn-outline-primary"
                        data-toggle="modal" data-target="#newTaskFrame">
                    <span data-feather="plus-square"></span>&nbsp;&nbsp;New Task
                </button>
                <div class="col-12 row mt-4">
                    <div v-for="task in displayTasks" class="btn-outline-light card col-lg-4" style="color: #000;">
                        <div class="card-header">
                            <h2 class="font-weight-normal task-card-title text-center">{{ task.taskName }}</h2>
                            <div class="progress">
                                <div v-bind:class="cssContent(task.state)" role="progressbar"
                                     v-bind:style="progressWidthStyle(task.progress)" aria-valuemin="0"
                                     aria-valuemax="100">
                                </div>
                            </div>
                        </div>
                        <div class="card-body">
                            <h1 class="h2">creator:<i class="h5">{{task.creatorName}}</i></h1>
                            <h1 class="h2">time:<i class="h5">{{task.createTime}}</i></h1>
                            <h1 class="h2">state:<i class="h5">{{task.state}}</i></h1>
                            <button v-on:click="onTaskButtonClick('add_files',task)"
                                    v-if="showTaskButton('task_add_file',task)"
                                    class="btn btn-block btn-lg btn-outline-info">
                                <span class="mb-1" data-feather="plus-circle"></span>&nbsp;&nbsp;Add Files
                            </button>
                            <button v-on:click="onTaskButtonClick('task_launch',task)"
                                    v-if="showTaskButton('task_launch',task)"
                                    class="mt-1 btn btn-lg btn-success btn-block">Launch
                            </button>
                            <button v-on:click="onTaskButtonClick('task_pause',task)"
                                    v-if="showTaskButton('task_pause',task)"
                                    class="mt-1 btn btn-lg btn-info btn-block">Pause
                            </button>
                            <button v-on:click="onTaskButtonClick('task_resume',task)"
                                    v-if="showTaskButton('task_resume',task)"
                                    class="mt-1 btn btn-lg btn-success btn-block">Resume
                            </button>
                            <button v-on:click="onTaskButtonClick('task_stop',task)"
                                    v-if="showTaskButton('task_stop',task)"
                                    class="mt-1 btn btn-lg btn-danger btn-block">Stop
                            </button>
                            <button v-on:click="onTaskButtonClick('task_download',task)"
                                    v-if="showTaskButton('task_download',task)"
                                    class="mt-1 btn btn-lg btn-primary btn-block">Download
                            </button>
                            <button v-on:click="onTaskButtonClick('task_delete',task)"
                                    v-if="showTaskButton('task_delete',task)"
                                    class="mt-1 btn btn-lg btn-danger btn-block">Delete
                            </button>

                        </div>
                    </div>
                </div>
            </div>
        </main>

        <main v-else-if="showModelModule()" role="main" class="col-md-10 ml-sm-auto px-4">

        </main>
    </div>
</div>

<%--popup frame : new task--%>
<div id="newTaskFrame" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="taskDetailTitle"
     aria-hidden="true">
    <div class="modal-dialog modal modal-dialog-centered" role="document">
        <form id="newTaskForm" class="modal-content">
            <div class="modal-header">
                <h2 class="modal-title" id="newTaskFrameTitle">New Task</h2>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div>
                    <div class="form-group row">
                        <label for="newTaskName" class="col-md-4 col-form-label"><h5 class="form-label">Task Name</h5>
                        </label>
                        <div class="col-md-8">
                            <input type="text" title="Try to take a unique task name." v-model="taskName"
                                   class="form-control" id="newTaskName" aria-describedby="taskNameHelp"
                                   placeholder="Please input your task name" required/>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label for="preModelSelect" class="col-md-4 col-form-label"><h5 class="form-label">Model</h5>
                        </label>
                        <div class="col-md-8">
                            <select v-model="modelGuid" class="custom-select" id="preModelSelect" required>
                                <option v-for="model in models" v-bind:value="model.guid">{{model.name}}</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label for="sourceTypeInput" class="col-md-4 col-form-label"><h5 class="form-label">Source
                            Type</h5></label>
                        <div class="col-md-8">
                            <select v-model="sourceType" class="custom-select" id="sourceTypeInput" required>
                                <option v-for="type in sourceTypes" v-bind:value="type">{{type}}</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label for="resultTypeInput" class="col-md-4 col-form-label"><h5 class="form-label">Result
                            Type</h5></label>
                        <div class="col-md-8">
                            <select v-model="resultType" class="custom-select" id="resultTypeInput" required>
                                <option v-for="type in resultTypes" v-bind:value="type">{{type}}</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label for="creatorName" class="col-md-4 col-form-label"><h5 class="form-label">Creator</h5>
                        </label>
                        <div class="col-md-8">
                            <input v-model="creator" type="text" class="form-control" id="creatorName"
                                   placeholder="Input your name here" aria-describedby="creatorHelp" value="admin"
                                   readonly required>
                        </div>
                    </div>
                    <div class="form-group row">
                        <div class="form-check offset-md-4 col-md-8">
                            <label for="publicCheck" class="m-0 btn btn-light"><input v-model="ifPublic"
                                                                                      id="publicCheck" type="checkbox">&nbsp;publish
                                the task</label>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button id="newTaskSubmit" type="submit" class="btn btn-block btn-primary">Create</button>
            </div>
        </form>
    </div>
</div>

<script src="<c:url value="/static/dist/js/jquery-3.3.1.js"/>"></script>
<script src="<c:url value="/static/dist/js/popper.js"/>"></script>
<script src=<c:url value="/static/dist/js/bootstrap.js"/>></script>
<!--  Vue javascript -->
<script src="<c:url value="/static/dist/js/vue.js"/>"></script>
<!-- Custom javascript -->
<script src="<c:url value="/static/js/main.js"/>"></script>
<%--Icon feather--%>
<script src="<c:url value="/static/dist/js/feather.js"/>"></script>
<script>
    feather.replace()
</script>
</body>
</html>
