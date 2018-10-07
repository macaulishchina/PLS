var loginApp = new Vue({
    el: '#loginApp',
    data: {
        username: "",
        password: ""
    },
    methods: {
        login: function () {
            var settings = {
                "async": true,
                "crossDomain": true,
                "url": "/user/login",
                "method": "POST",
                "headers": {
                    "Content-Type": "application/x-www-form-urlencoded",
                    "Cache-Control": "no-cache",
                    "Accept": "application/json",
                    "Postman-Token": "7f57c814-99f6-41bc-9ffa-6a6d679ac9da"
                },
                "data": {
                    "username": this.username,
                    "password": this.password
                }
            };

            $.ajax(settings).done(function (response) {
                console.log(response);
                if (response.state === "success") {
                    window.location.reload()
                } else {
                    alert(response.data)
                }
            });
        },
        reload: function () {
            window.location.reload(true)
        }
    }
});

$('form').submit(function (event) {
    event.preventDefault();
    loginApp.login()
});