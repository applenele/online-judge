<%--
  Created by IntelliJ IDEA.
  User: xanarry
  Date: 17-12-27
  Time: 下午6:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script>
    function getValidateCode() {
        $("#validateCode").attr("src", "/ajaxGetValidateCode?r=" + Math.random());
    }

    function login() {

        var inputEmail = $("#inputLoginEmail").val();
        var inputPassword = $("#inputLoginPassword").val();
        var inputRememberMe = $('#rememberMe:checked').val();
        var inputValidateCode = $("#inputLoginValidateCode").val();
        console.log("click login: " + inputRememberMe);
        $.ajax({
            type: 'POST',
            url: '/login',
            data: {
                inputEmail: inputEmail,
                inputPassword: inputPassword,
                inputRememberMe: inputRememberMe,
                inputValidateCode: inputValidateCode
            },
            dataType: "json",
            success: function (data) {
                console.log(JSON.stringify(data));
                //"{userExist : %s, correctPassword : %s, correctValidateCode :%s}"

                if (data.userExist == false) {
                    $("#emailTip").show();
                } else {
                    $("#emailTip").hide();
                }

                if (data.correctPassword == false) {
                    $("#passwordTip").show();
                } else {
                    $("#passwordTip").hide();
                }

                if (data.correctValidateCode == false) {
                    $("#validateCodeTip").show();
                } else {
                    $("#validateCodeTip").hide();
                }

                if (data.userExist == true && data.correctPassword == true && data.correctValidateCode) {
                    window.location.href = "/";
                }

            },
            error: function (e) {
                alert("ajax error occured" + JSON.stringify(e));
            }
        })
    }

    $(function () {
        $('#loginModal').on('show.bs.modal', function () {
            getValidateCode();
            $("#validateCodeTip").hide();
            $("#passwordTip").hide();
            $("#emailTip").hide();
        })

    })
</script>

<nav class="navbar navbar-expand-sm navbar-light bg-light fixed-top">
    <div class="container">
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item active">
                    <a class="nav-link" href="/">首页</a>
                </li>
                <li class="nav-item"><span class="nav-link"></span></li>
                <li class="nav-item">
                    <a class="nav-link" href="/problem-list">题库</a>
                </li>
                <li class="nav-item"><span class="nav-link"></span></li>
                <li class="nav-item">
                    <a class="nav-link" href="/contest-list">比赛</a>
                </li>
                <li class="nav-item"><span class="nav-link"></span></li>
                <li class="nav-item">
                    <a class="nav-link" href="#">讨论</a>
                </li>
                <li class="nav-item"><span class="nav-link"></span></li>
                <li class="nav-item">
                    <a class="nav-link" href="/record-list">提交记录</a>
                </li>

                <li class="nav-item"><span class="nav-link"></span></li>

                <li class="nav-item">
                    <a class="nav-link" href="/chart">排行榜</a>
                </li>

                <li class="nav-item"><span class="nav-link"></span></li>
                <li class="nav-item"><span class="nav-link"></span></li>
                <form class="form-inline my-2 my-lg-0">
                    <input class="form-control mr-sm-2" type="search" placeholder="搜索题目编号,标题" aria-label="Search">
                    <button class="btn btn-outline-success my-2 my-sm-0" type="submit">搜索</button>
                </form>
            </ul>

            <c:choose>
                <%--用户已经登录--%>
                <c:when test="${cookie.containsKey('userID')}">
                    <ul class="navbar-nav">
                        <li class="nav-item">
                            <a href="/user?userID=${cookie.get('userID').value}" class="nav-link"><span
                                    class=""></span>${cookie.get("userName").value}</a>
                        </li>
                        <li class="nav-item"><span class="nav-link"></span></li>
                        <li class="nav-item"><a href="/logout" class="nav-link"><span class=""></span>退出</a></li>
                    </ul>
                </c:when>

                <%--游客--%>
                <c:otherwise>
                    <ul class="navbar-nav">
                        <li class="nav-item">
                            <a href="#" class="nav-link" data-toggle="modal" data-target="#loginModal"><span
                                    class=""></span>登录</a>
                        </li>
                        <li class="nav-item"><span class="nav-link"></span></li>
                        <li class="nav-item"><a href="/register" class="nav-link"><span class=""></span>注册</a></li>
                    </ul>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</nav>

<div class="modal fade" id="loginModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel">登录</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>


            <form class="modal-body" method="post" action="/login">
                <div class="input-group">
                    <span class="input-group-addon">邮&nbsp;&nbsp;&nbsp;&nbsp;箱</span>
                    <input type="text" class="form-control" id="inputLoginEmail" name="inputLoginEmail" placeholder="邮箱地址">
                </div>
                <label class="offset-8" id="emailTip" style="display: none; color: red">邮箱不存在</label>
                <br>


                <div class="input-group">
                    <span class="input-group-addon">密&nbsp;&nbsp;&nbsp;&nbsp;码</span>
                    <input type="password" class="form-control" id="inputLoginPassword" name="inputLoginPassword"
                           placeholder="登录密码">
                    <br>
                </div>
                <label class="offset-8" id="passwordTip" style="display: none; color: red">密码错误</label>
                <br>

                <div class="input-group">
                    <span class="input-group-addon">验证码</span>
                    <input type="text" class="form-control" id="inputLoginValidateCode" name="inputLoginValidateCode"
                           placeholder="输入验证码中的结果">
                </div>
                <label class="offset-8" id="validateCodeTip" style="display: none; color: red">验证码错误</label>
                <br>

                <div class="input-group">
                    <img id="validateCode" src="" alt="" height="38px">
                    <span>&nbsp;</span>
                    <a class="btn btn-light" onclick="getValidateCode()">点此换一张图片</a>
                </div>

                <br>
                <div class="form-check">
                    <label class="form-check-label">
                        <input type="checkbox" value="1" id="rememberMe" name="rememberMe" class="form-check-input">记住我一周
                    </label>
                </div>

                <div class="text-center">
                    <a href="#" class="btn btn-primary" onclick="login()">点此登录</a>
                    <br><br>
                    <a href="/retrieve-password">忘记密码?</a>
                </div>
            </form>
        </div>
    </div>
</div>



