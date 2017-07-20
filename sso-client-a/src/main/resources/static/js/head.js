var SSO_CONS = {
    SSO_LOGIN: 'http://sso.dongly.com:8080',
    SSO_REGISTER: 'http://sso.dongly.com:8080/register',
    SSO_LOGOUT: 'http://sso.dongly.com:8080/user/logout/',
    SSO_COOKIE_NAME: 'SSO_TOKEN',
    SSO_DOMAIN: 'dongly.com',
    SSO_TOKEN: 'http://sso.dongly.com:8080/user/token/'
}


var TT = TAOTAO = {
    checkLogin: function () {
        var _ticket = Cookies.get(SSO_CONS.SSO_COOKIE_NAME);
        if (!_ticket) {
            var start = '<h1>';
            var body = "<a href=" + SSO_CONS.SSO_LOGIN + ">登录</a>" + '&nbsp;&nbsp;&nbsp;&nbsp;' +
                "<a href=" + SSO_CONS.SSO_REGISTER + ">注册</a>";
            var end = "</h1>";
            $("#login-bar").html(start + body + end);
            return;
        }
        $.ajax({
            url: SSO_CONS.SSO_TOKEN + _ticket,
            dataType: "jsonp",
            type: "GET",
            success: function (result) {
                if (result.status == 200) {
                    var username = result.data;
                    var start = "<h1>";
                    var html = username + "，欢迎来到sso-client-a！<a href='#'" +
                        " id='link-logout'>[退出]</a>";
                    var end = "</h1>";
                    $("#login-bar").html(start + html + end);
                }
            }
        });
    }
}

$(function () {
    // 查看是否已经登录，如果已经登录查询登录信息
    TT.checkLogin();
     // Cookies.remove('SSO_TOKEN', {path: '/', domain: 'dongly.com'})
    $('#login-bar').on('click', '#link-logout', function () {
        var _ticket = Cookies.get(SSO_CONS.SSO_COOKIE_NAME);
        if (!_ticket) {
            return;
        }
        $.ajax({
            url: SSO_CONS.SSO_LOGOUT + _ticket,
            dataType: "jsonp",
            type: "GET",
            success: function (result) {
                if (result.status == 200) {
                    var start = '<h1>';
                    var body = "<a href=" + SSO_CONS.SSO_LOGIN + ">登录</a>" +
                        "<a href=" + SSO_CONS.SSO_REGISTER + ">注册</a>";
                    var end = "</h1>";
                    $("#login-bar").html(start + body + end);
                    Cookies.remove(SSO_CONS.SSO_COOKIE_NAME, {path: '/', domain: SSO_CONS.SSO_DOMAIN})
                }
            }
        });
    });
    
});