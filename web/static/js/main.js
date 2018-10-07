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
            var username = toolbarApp.userName;
            var settings = {
                "async": true,
                "crossDomain": true,
                "url": "/task/" + username + "/query?update=false",
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
                } else {
                    alert(response.data)
                }
            });
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
        showTaskButton: function (id) {
            return true
        },
        onTaskButtonClick: function (id) {
            confirm(id)
        }

    }
});
