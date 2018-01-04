<%--
  Created by IntelliJ IDEA.
  User: xanarry
  Date: 17-12-27
  Time: 上午12:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <title>Bootstrap Example</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">


    <link rel="stylesheet" href="css/bootstrap/bootstrap.min.css">

    <script src="js/jquery-3.2.1.min.js"></script>
    <script src="js/bootstrap/popper.min.js"></script>
    <script src="js/bootstrap/bootstrap.min.js"></script>

    <script>
        function getValidateCode() {
            $("#validateCode").attr("src","ajaxGetValidateCode?r=" + Math.random());
        }
    </script>

</head>
<body>
<jsp:include page="navbar.jsp"/>
<div class="container" style="margin-top:70px">
    <div class="modal-content">
        <div class="modal-header">
            <h5 class="modal-title" id="exampleModalLabel">登录</h5>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>


        <div class="modal-body">
            <div class="input-group">
                <span class="input-group-addon">邮&nbsp;&nbsp;&nbsp;&nbsp;箱</span>
                <input type="text" class="form-control" id="inputEmail" name="inputEmail" placeholder="邮箱地址">
            </div>
            <br>
            <div class="input-group">
                <span class="input-group-addon">密&nbsp;&nbsp;&nbsp;&nbsp;码</span>
                <input type="text" class="form-control" id="inputPassword" name="inputPassword" placeholder="登录密码">
            </div>
            <br>

            <div class="input-group">
                <span class="input-group-addon">验证码</span>
                <input type="text" class="form-control" placeholder="输入左验证码">
            </div>
            <br/>
            <div class="input-group">
                <img id="validateCode" src="/ajaxGetValidateCode" height="38px">
                <span>&nbsp;</span>
                <button class="btn btn-light" onclick="getValidateCode()">点此换一张图片</button>
            </div>

            <br>
            <div class="form-check">
                <label class="form-check-label"><input type="checkbox" id="rememberMe" name="rememberMe"
                                                       class="form-check-input">记住我一周</label>
            </div>

            <div class="text-center">
                <button type="submit" class="btn btn-primary">点此登录</button>
                <br><br>
                <a href="/retrieve-password">忘记密码?</a>
            </div>
        </div>
    </div>
</div>
</body>
</html>

