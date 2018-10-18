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
var uploadApp = new Vue({
    el: "#uploadApp",
    data: {
        taskGuid: $("#hiddenTaskGuid").val(),
        files: []
    },
    methods: {
        updateFilesInfo: function () {
            var _this = this;
            var settings = {
                "async": true,
                "crossDomain": true,
                "url": "/task/" + toolbarApp.userName + "/query/upload/files/" + _this.taskGuid,
                "method": "POST",
                "headers": {
                    "Accept": "application/json",
                    "cache-control": "no-cache",
                    "Postman-Token": "73819b4b-393f-4a87-a2e4-b3d8e45f0afb"
                }
            };

            $.ajax(settings).done(function (response) {
                console.log(response);
                if (response.state === "success") {
                    _this.files = response.data
                } else {
                    _this.files = []
                    alert("failed to get the files")
                }
            });
        }
    }
});


$(function () {
    $('#fileupload').fileupload({
        dataType: 'json',
        done: function (e, data) {
            $.each(data.result.files, function (index, file) {
                $('<p/>').text(file.name).appendTo(document.body);
            });
        }
    });
    $('#fileupload').fileupload({
        progressall: function (e, data) {
            var progress = parseInt(data.loaded / data.total * 100, 10);
            $('#progress .progress-bar').css('width', progress + '%');
            uploadApp.updateFilesInfo()
        }
    });
})

document.getElementById("fileupload").setAttribute("data-url", "/task/" + toolbarApp.userName + "/upload/" + uploadApp.taskGuid);
uploadApp.updateFilesInfo();