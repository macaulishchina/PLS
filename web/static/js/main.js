var toolbarApp = new Vue({
    el: '#toolbarApp',
    data: {
        userName: $("#hiddenUserName").val()
    },
    methods: {
        reload: function () {
            window.location.reload(true)
        },
        logout: function () {
            var settings = {
                "async": true,
                "crossDomain": true,
                "url": "/user/logout",
                "method": "POST",
                "headers": {
                    "Accept": "application/json",
                    "Cache-Control": "no-cache",
                    "Postman-Token": "fd7c8590-04df-4551-ae65-86151eebb0a7"
                }
            };

            $.ajax(settings).done(function (response) {
                console.log(response);
            });
        }
    }
});

var mainApp = new Vue({
    el: '#mainApp',
    data: {
        navModule: "task",
        ppKey: "private",
        taskFilterKey: "all",
        taskQueryAuto: false,
        taskQueryOffline: false,
        timer1: null,
        privateTasks: [],
        publicTasks: [],
        displayTasks: []
    },
    methods: {
        filterLocalData: function () {
            var _this = this;
            this.displayTasks = [];
            if (this.ppKey === "private") {
                if (this.taskFilterKey === "all") {
                    this.privateTasks.forEach(function (task, index) {
                        _this.displayTasks.push(task)
                    })
                } else {
                    this.privateTasks.forEach(function (task, index) {
                        if (task.state.toLowerCase() === _this.taskFilterKey) {
                            _this.displayTasks.push(task)
                        }
                    })
                }

            } else if (this.ppKey === "public") {
                if (this.taskFilterKey === "all") {
                    this.publicTasks.forEach(function (task, index) {
                        _this.displayTasks.push(task)
                    })
                } else {
                    this.publicTasks.forEach(function (task, index) {
                        if (task.state.toLowerCase() === _this.taskFilterKey) {
                            _this.displayTasks.push(task)
                        }
                    })
                }
            }
        },
        reloadTasks: function () {
            var _this = this;
            console.log("reloadTask() called,update = " + !_this.taskQueryOffline);
            var username = toolbarApp.userName;
            var settings = {
                "async": true,
                "crossDomain": true,
                "url": "/task/" + username + "/query?update=" + (!mainApp.taskQueryOffline),
                "method": "GET",
                "headers": {
                    "Accept": "application/json",
                    "Cache-Control": "no-cache",
                    "Postman-Token": "39b95a52-ae47-44b2-8dc6-da1f7546c708"
                }
            };

            $.ajax(settings).done(function (response) {
                if (response.state === "success") {
                    _this.privateTasks = response.data.private;
                    _this.publicTasks = response.data.publish;
                    _this.filterLocalData();
                    _this.updateDisplayTask();
                } else {
                    _this.taskQueryAuto = false;
                    window.clearInterval(_this.timer1);
                    alert(response.data)
                }
            });
        },
        updateDisplayTask: function () {
            var _this = this;
            _this.displayTasks.forEach(function (value) {
                var settings = {
                    "async": true,
                    "crossDomain": true,
                    "url": "/task/" + toolbarApp.userName + "/query/progress/" + value.guid,
                    "method": "POST",
                    "headers": {
                        "Accept": "application/json",
                        "cache-control": "no-cache",
                        "Postman-Token": "15d5ecc1-814b-450e-b2da-ebf372504232"
                    }
                };

                $.ajax(settings).done(function (response) {
                    console.log(response);
                    if (response.state === "success") {
                        _this.displayTasks.forEach(function (value) {
                            if (value.guid === response.data.taskGuid) {
                                value.state = response.data.taskState;
                                if (value.state === "handling") {
                                    value.progress = response.data.process;
                                } else {
                                    value.progress = 100;
                                }
                                _this.filterLocalData()
                            }
                        })
                    }
                });
            });
        },
        launchTask: function (taskGuid) {
            var _this = this;
            var settings = {
                "async": true,
                "crossDomain": true,
                "url": "/task/" + toolbarApp.userName + "/start/" + taskGuid,
                "method": "POST",
                "headers": {
                    "Accept": "application/json",
                    "cache-control": "no-cache",
                    "Postman-Token": "8638f171-527e-4203-bf04-05a23f6cb042"
                }
            };

            $.ajax(settings).done(function (response) {
                if (response.state === "success") {
                    _this.reloadTasks()
                } else {
                    alert("failed to launch the task")
                }
            });
        },
        pauseTask: function (taskGuid) {
            var _this = this;
            var settings = {
                "async": true,
                "crossDomain": true,
                "url": "/task/" + toolbarApp.userName + "/pause/" + taskGuid,
                "method": "POST",
                "headers": {
                    "Accept": "application/json",
                    "cache-control": "no-cache",
                    "Postman-Token": "541e413d-7c7f-4094-95ac-63208916fbea"
                }
            };

            $.ajax(settings).done(function (response) {
                if (response.state === "success") {
                    _this.reloadTasks()
                } else {
                    alert("failed to pause the task")
                }
            });
        },
        resumeTask: function (taskGuid) {
            var _this = this;
            var settings = {
                "async": true,
                "crossDomain": true,
                "url": "/task/" + toolbarApp.userName + "/resume/" + taskGuid,
                "method": "POST",
                "headers": {
                    "Accept": "application/json",
                    "cache-control": "no-cache",
                    "Postman-Token": "2b2baae3-7fde-40e9-a909-f724022a84c5"
                }
            };

            $.ajax(settings).done(function (response) {
                if (response.state === "success") {
                    _this.reloadTasks()
                } else {
                    alert("failed to resume the task")
                }
            });
        },
        stopTask: function (taskGuid) {
            var _this = this;
            var settings = {
                "async": true,
                "crossDomain": true,
                "url": "/task/" + toolbarApp.userName + "/stop/" + taskGuid,
                "method": "POST",
                "headers": {
                    "Accept": "application/json",
                    "cache-control": "no-cache",
                    "Postman-Token": "3b1b7fba-442c-47b5-ba49-5808cc866cc7"
                }
            };

            $.ajax(settings).done(function (response) {
                if (response.state === "success") {
                    _this.reloadTasks()
                } else {
                    alert("failed to stop the task")
                }
            });
        },
        deleteTask: function (taskGuid) {
            var _this = this;
            var settings = {
                "async": true,
                "crossDomain": true,
                "url": "/task/" + toolbarApp.userName + "/delete/" + taskGuid,
                "method": "POST",
                "headers": {
                    "Accept": "application/json",
                    "cache-control": "no-cache",
                    "Postman-Token": "c953ca12-0625-42bc-97e1-26d84708bf29"
                }
            };

            $.ajax(settings).done(function (response) {
                if (response.state === "success") {
                    _this.reloadTasks()
                } else {
                    alert("failed to delete the task")
                }
            });
        },
        downloadTask: function (taskGuid) {
            window.location.href = "/task/" + toolbarApp.userName + "/download/result/" + taskGuid;
        },
        navTaskModule: function () {
            this.navModule = "task"
        },
        navModelModule: function () {
            this.navModule = "model"
        },
        showTaskModule: function () {
            return (this.navModule === "task")
        },
        showModelModule: function () {
            return (this.navModule === "model")
        },
        changeFilter: function (filterKey) {
            this.taskFilterKey = filterKey;
            this.filterLocalData()
        },
        changePP: function (ppKey) {
            this.ppKey = ppKey;
            this.filterLocalData()
        },
        showTaskButton: function (action, task) {
            switch (action) {
                case "task_add_file" :
                    return $.inArray(task.state, ["ready", "new", "error"]) >= 0;
                case "task_launch" :
                    return $.inArray(task.state, ["ready", "stop", "error"]) >= 0;
                case "task_pause" :
                    return task.state === "handling";
                case "task_resume" :
                    return task.state === "pause";
                case "task_stop" :
                    return task.state === "handling";
                case "task_download" :
                    return task.state === "finish";
                case "task_delete" :
                    return task.state !== "handling";
                default:
                    return false
            }
        },
        onTaskButtonClick: function (action, task) {
            switch (action) {
                case "add_files" :
                    window.open("/upload/" + task.guid);
                    break;
                case "task_launch" :
                    this.launchTask(task.guid);
                    break;
                case "task_pause" :
                    this.pauseTask(task.guid);
                    break;
                case "task_resume" :
                    this.resumeTask(task.guid);
                    break;
                case "task_stop" :
                    this.stopTask(task.guid);
                    break;
                case "task_download" :
                    this.downloadTask(task.guid);
                    break;
                case "task_delete" :
                    this.deleteTask(task.guid);
                    break;
                default:
            }
        },
        onAutoButtonClick: function () {
            window.clearInterval(mainApp.timer1);
            if (!(mainApp.taskQueryAuto)) {
                mainApp.timer1 = window.setInterval(mainApp.updateDisplayTask, 1000)
            }
        },
        cssContent: function (state) {
            var cssContent = "";
            switch (state.toLowerCase()) {
                case "uploading":
                    cssContent = "progress-bar progress-bar-striped progress-bar-animated";
                    break;
                case "pause":
                case "ready":
                case "new":
                    cssContent = "progress-bar progress-bar-striped";
                    break;
                case "handling":
                    cssContent = "progress-bar progress-bar-striped progress-bar-animated bg-warning";
                    break;
                case "stop":
                    cssContent = "progress-bar bg-dark";
                    break;
                case "finish":
                    cssContent = "progress-bar bg-success";
                    break;
                default:
                    cssContent = ""
            }
            return cssContent;
        },
        progressWidthStyle: function (progress) {
            var style = {};
            style.width = progress + "%";
            return style;
        }
    }
});

var newTaskFrame = new Vue({
    el: "#newTaskFrame",
    data: {
        models: [],
        sourceTypes: [12, 123],
        resultTypes: [],
        taskName: "",
        modelGuid: "",
        sourceType: "",
        resultType: "",
        ifPublic: false,
        creator: "",
        submitEnable: 0
    },
    methods: {
        initTaskProp: function () {
            this.submitEnable = 0;
            this.loadFormContent();
            this.taskName = "";
            this.modelGuid = "";
            this.sourceType = "";
            this.resultType = "";
            this.ifPublic = false;
            this.creator = toolbarApp.userName
            $('#newTaskSubmit').hide()
        },
        loadFormContent: function () {
            var _this = this;
            var models = {
                "async": true,
                "crossDomain": true,
                "url": "/model/query",
                "method": "GET",
                "headers": {
                    "Accept": "application/json",
                    "Cache-Control": "no-cache",
                    "Postman-Token": "37f91c02-f589-4ba0-b226-94712d8396a3"
                }
            };
            var sourceTypes = {
                "async": true,
                "crossDomain": true,
                "url": "/task/source/types",
                "method": "GET",
                "headers": {
                    "Accept": "application/json",
                    "cache-control": "no-cache",
                    "Postman-Token": "7e00219c-dba7-4824-ba96-f644f508e170"
                }
            };
            var resultType = {
                "async": true,
                "crossDomain": true,
                "url": "/task/result/types",
                "method": "GET",
                "headers": {
                    "Accept": "application/json",
                    "cache-control": "no-cache",
                    "Postman-Token": "5d1b77bc-a3b5-4752-83ee-c5e158704720"
                }
            };
            $.ajax(models).done(function (response) {
                console.log(response);
                if (response.state === "success") {
                    _this.models = response.data;
                    _this.checkContent()
                } else {
                    alert("failed to  load the models")
                }
            });
            $.ajax(sourceTypes).done(function (response) {
                console.log(response.data);
                if (response.state === "success") {
                    _this.checkContent()
                    _this.sourceTypes = response.data
                } else {
                    alert("failed to load the source types")
                }
            });
            $.ajax(resultType).done(function (response) {
                console.log(response);
                if (response.state === "success") {
                    _this.checkContent();
                    _this.resultTypes = response.data;
                } else {
                    alert("failed to  load the result types")
                }
            });
        },
        checkContent: function () {
            this.submitEnable++;
            if (this.submitEnable >= 3) {
                $('#newTaskSubmit').show()
            }
        },
        createTask: function () {
            var settings = {
                "async": true,
                "crossDomain": true,
                "url": "/task/" + toolbarApp.userName + "/create?"
                + "taskName=" + this.taskName
                + "&modelGuid=" + this.modelGuid
                + "&sourceType=" + this.sourceType
                + "&resultType=" + this.resultType
                + "&publish=" + (this.ifPublic === true ? 1 : 0),
                "method": "POST",
                "headers": {
                    "Accept": "application/json",
                    "cache-control": "no-cache",
                    "Postman-Token": "d538d5ea-262e-424e-9678-1bff1658fe5d"
                }
            };

            $.ajax(settings).done(function (response) {
                if (response.state === "success") {
                    mainApp.reloadTasks()
                } else {
                    alert("failed to create the task")
                }
            });
        }
    }
});

window.onload = function onWindowLoad() {
    mainApp.reloadTasks();
    window.clearInterval(mainApp.timer1);
    mainApp.taskQueryAuto = true;
    mainApp.timer1 = window.setInterval(mainApp.updateDisplayTask, 1000)
};

$('#newTaskForm').submit(function (event) {
    event.preventDefault();
    newTaskFrame.createTask();
    $('#newTaskFrame').modal('hide');
});
