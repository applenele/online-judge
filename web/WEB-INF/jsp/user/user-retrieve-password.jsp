<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: xanarry
  Date: 17-12-28
  Time: 下午3:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>找回密码</title>

    <link rel="stylesheet" href="/css/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" href="/css/oj.css">

    <script src="/js/jquery-3.2.1.min.js"></script>
    <script src="/js/bootstrap/popper.min.js"></script>
    <script src="/js/bootstrap/bootstrap.min.js"></script>
    
    <script>
        function retrievePassword() {
            var userName = $("#inputUserName").val();
            var email = $("#inputEmail").val();

            if (userName == undefined || userName.length == 0 || email == undefined || email.length == 0) {
                $("#messageModal").modal('show');
            } else {
                $("#messageModal").modal('hide');
                $.ajax({
                    type: 'POST',
                    url: "/send-retrieve-password-email",
                    data: {
                        "inputUserName": userName,
                        "inputEmail": email
                    },
                    dataType: "json",
                    success: function (data) {
                        //{"userNameCheck": true, "emailCheck", "sendMail:" true}
                        console.log(JSON.stringify(data));
                        if (data.userNameCheck == true && data.emailCheck == true) {
                            if (data.sendMail == true) {
                                $("#message").html("找回密码的邮件已经发送到您的注册邮箱, 请在30分钟内完成重置密码的操作");
                            } else {
                                $("#message").html("发送邮件失败!");
                            }
                        } else {
                            $("#message").html("用户名或者邮箱不存在!");
                        }
                        $("#messageModal").modal('show');
                    },

                    error: function (data) {
                        alert("ajax error occured!");
                    }
                });
            }


            function checkPassword() {
                var pass    = $("#inputNewPassword").val();
                var conpass = $("#cnfrpwd").val();
                if (pass != undefined && pass.length > 3) {
                    if (pass != conpass) {
                        alert("两次密码");
                        return false;
                    }
                } else {
                    alert("密码不能少于6位");
                    return false;
                }
                return true;
            }
        }
    </script>
</head>
<body>
<jsp:include page="/navbar.jsp"/>

<div class="container custom-container">
    <h3 class="text-center">${problem.title}</h3>
    <div class="card">
        <h5 class="card-header">找回密码</h5>
        <div class="card-body">
            <c:choose>
                <c:when test="${! empty email}">
                <form action="/retrieve-password" method="post" onsubmit="return checkPassword()">
                    <input name="inputRetrieveEmail" value="${email}" hidden>
                    <div class="form-group row">
                        <label for="inputEmail" class="col-sm-4 col-form-label text-right">邮箱 :</label>
                        <div class="col-sm-5">
                            <input type="text" class="form-control" name="inputEmail" value="${email}" disabled placeholder="邮箱">
                        </div>
                    </div>

                    <div class="form-group row">
                        <label class="col-sm-4 col-form-label text-right">密码 :</label>
                        <div class="col-sm-5">
                            <input type="password" class="form-control" id="rpwd" name="inputNewPassword" placeholder="密码">
                        </div>
                    </div>

                    <div class="form-group row">
                        <label class="col-sm-4 col-form-label text-right">确认密码 :</label>
                        <div class="col-sm-5">
                            <input type="password" class="form-control" id="cnfrpwd" placeholder="确认密码">
                        </div>
                    </div>


                    <div class="form-group row">
                        <label class="col-sm-4 col-form-label text-right"> </label>
                        <div class="col-sm-5">
                            <button type="submit" class="form-control btn btn-success">设置新密码</button>
                        </div>
                    </div>
                </form>
                </c:when>

                <c:otherwise>
                    <div class="form-group row">
                        <label for="inputUserName" class="col-sm-4 col-form-label text-right">用户名 :</label>
                        <div class="col-sm-5">
                            <input type="text" class="form-control" id="inputUserName" name="inputUserName" placeholder="用户名">
                        </div>
                        <label class="col-form-label" id="userNameTip" style="display: none; color: red">用户名label</label>
                    </div>

                    <div class="form-group row">
                        <label for="inputEmail" class="col-sm-4 col-form-label text-right">邮箱 :</label>
                        <div class="col-sm-5">
                            <input type="text" class="form-control" id="inputEmail" name="inputEmail" placeholder="邮箱">
                        </div>
                        <label class="col-form-label" id="emailTip" style="display: none; color: red">邮箱label</label>
                    </div>

                    <div class="form-group row">
                        <label class="col-sm-4 col-form-label text-right"> </label>
                        <div class="col-sm-5">
                            <button onclick="retrievePassword()" class="form-control btn btn-success">找回密码</button>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
    </div>
</div>

<div class="modal fade" id="messageModal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">重要提示</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <p id="message"></p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">知道了</button>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/footer.jsp"/>
</body>
</html>
