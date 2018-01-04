<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: xanarry
  Date: 17-12-28
  Time: 下午7:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${userName}</title>
    <link rel="stylesheet" href="css/bootstrap/bootstrap.min.css">

    <script src="js/jquery-3.2.1.min.js"></script>
    <script src="js/bootstrap/popper.min.js"></script>
    <script src="js/bootstrap/bootstrap.min.js"></script>

    <script>
        function editUser() {
            var inputUserID = $("#inputUserID").val();
            var inputNewUserName = $("#inputNewUserName").val();
            var inputOldPassword = $("#inputOldPassword").val();
            var inputNewPassword = $("#inputNewPassword").val();
            var inputConfirmPassword = $("#inputConfirmPassword").val();
            var inputBio = $("#inputBio").val();
            var inputSendCode = $('#inputSendCode option:selected').val();
            var inputPreferLanguage = $('#inputPreferLanguage option:selected').val();

            console.log(inputSendCode + " " + inputPreferLanguage);

            //alert(inputPreferLanguage + " " + inputSendCode);

            var flag = 0;
            $("#newPasswordTip").show();

            if (inputNewPassword.length < 3) {
                $("#newPasswordTip").show(); flag++;
            } else {
                $("#newPasswordTip").hide();
            }

            if (inputConfirmPassword != inputNewPassword) {
                $("#confirmPasswordTip").show(); flag++;
            } else {
                $("#confirmPasswordTip").hide();
            }

            if (flag == 0) {
                $.ajax({
                    type: 'POST',
                    url: '/edit-user',
                    data: {
                        'inputUserID': inputUserID,
                        'inputUserName': inputNewUserName,
                        'inputOldPassword': inputOldPassword,
                        'inputPassword': inputNewPassword,
                        'inputBio': inputBio,
                        'inputSendCode': inputSendCode,
                        'inputPreferLanguage': inputPreferLanguage
                    },
                    dataType: "json",
                    success: function (data) {
                        console.log(JSON.stringify(data));
                        //"{userNameExist : %s, correctOldPassword :%s}"
                        if (data.userNameExist == true) {
                            $("#newUserNameTip").show();
                            return;
                        } else {
                            $("#newUserNameTip").hide();
                        }

                        if (data.correctOldPassword == false) {
                            $("#oldPasswordTip").show();
                            return;
                        } else {
                            $("#oldPasswordTip").hide();
                        }

                        window.location.href = "/user?userID=" + inputUserID;

                    },
                    error: function (e) {
                        alert("ajax error occured" + JSON.stringify(e));
                    }
                })
            }
        }
        $(function () {
            $('#loginModal').on('show.bs.modal', function () {
                getValidateCode();
                $("#newUserNameTip").hide();
                $("#oldPasswordTip").hide();
                $("#newPasswordTip").hide();
                $("#confirmPasswordTip").hide();
            })
        })
    </script>
</head>
<body>
<jsp:include page="navbar.jsp"/>

<div class="container" style="margin-top: 70px">
    <div class="card">
        <div class="card-header"><h5>个人信息</h5></div>
        <div class="card-body">

            <div class="row">
                <div class="col-sm-2"></div>
                <div class="col-sm-8">
                    <table class="table">
                        <tbody>
                        <%--
                         <tr>
                             <th class="text-right">用户ID: </th>
                             <td class="text-left">${user.userID}</td>
                         </tr>
                         --%>
                        <tr>
                            <th class="text-right">用户名:</th>
                            <td class="text-left">${user.userName}</td>
                        </tr>
                        <tr>
                            <th class="text-right">邮箱:</th>
                            <td class="text-left">${user.email}</td>
                        </tr>
                        <tr>
                            <th class="text-right">用户类型:</th>
                            <c:if test="${user.userType == 0}">
                                <td class="text-left">普通用户</td>
                            </c:if>
                            <c:if test="${user.userType == 1}">
                                <td class="text-left">高级用户</td>
                            </c:if>
                            <c:if test="${user.userType == 2}">
                                <td class="text-left">系统管理员</td>
                            </c:if>

                        </tr>
                        <tr>
                            <th class="text-right">个性签名:</th>
                            <td class="text-left">${user.bio}</td>
                        </tr>
                        <tr>
                            <th class="text-right">注册时间:</th>
                            <jsp:useBean id="registerTime" class="java.util.Date"/>
                            <c:set target="${registerTime}" property="time" value="${user.registerTime}"/>
                            <td class="text-left"><fmt:formatDate pattern="yyyy-MM-dd hh:mm:ss"
                                                                  value="${registerTime}"/></td>
                        </tr>
                        <tr>
                            <th class="text-right">最后登录时间:</th>
                            <jsp:useBean id="lastLoginTime" class="java.util.Date"/>
                            <c:set target="${lastLoginTime}" property="time" value="${user.lastLoginTime}"/>
                            <td class="text-left"><fmt:formatDate pattern="yyyy-MM-dd hh:mm:ss"
                                                                  value="${lastLoginTime}"/></td>
                        </tr>
                        <tr>
                            <th class="text-right">偏好语言:</th>
                            <td class="text-left">${user.preferLanguage}</td>
                        </tr>
                        <tr>
                            <th class="text-right">通过/提交:</th>
                            <td class="text-left">${user.accepted}/${user.submitted}</td>
                        </tr>
                        <tr>
                            <th class="text-right">发送代码到邮箱:</th>
                            <c:choose>
                                <c:when test="${user.sendCode == true}">
                                    <td class="text-left">是</td>
                                </c:when>
                                <c:otherwise>
                                    <td class="text-left">否</td>
                                </c:otherwise>
                            </c:choose>
                        </tr>
                        </tbody>
                    </table>
                    <div class="text-center">
                        <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#editModal">
                            修改信息
                        </button>
                        <button class="btn btn-danger" data-toggle="modal" data-target="#deleteModal">删除</button>
                    </div>
                    <div class="col-sm-2"></div>
                </div>
            </div>
        </div>
    </div>

    <div class="modal fade" id="deleteModal">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">重要提示</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <p>信息即将永久性删除, 是否继续?</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">返回</button>
                    <a href="/delete-user?userID=${user.userID}" class="btn btn-danger">是的,删除</a>
                </div>
            </div>
        </div>
    </div>

    <div class="modal fade" id="editModal" tabindex="-1" role="dialog" aria-labelledby="editModalLabel"
         aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">修改信息</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form method="post" id="editForm" action="/edit-user">
                        <input id="inputUserID" value="${user.userID}" hidden>
                        <div class="input-group">
                            <span class="input-group-addon">&nbsp;&nbsp;&nbsp;&nbsp;用户名</span>
                            <input name="userName" type="text" value="${user.userName}" id="inputNewUserName" placeholder="用户名"
                                   class="form-control">
                        </div>
                        <label class="offset-sm-1" id="newUserNameTip" style="display: none; color: red">用户名已经存在</label>
                        <br>


                        <div class="input-group">
                            <span class="input-group-addon">&nbsp;&nbsp;&nbsp;&nbsp;旧密码</span>
                            <input type="password" value="" placeholder="旧密码" id="inputOldPassword" class="form-control">
                        </div>
                        <label class="offset-sm-1" id="oldPasswordTip" style="display: none; color: red">密码错误</label>
                        <br>


                        <div class="input-group">
                            <span class="input-group-addon">&nbsp;&nbsp;&nbsp;&nbsp;新密码</span>
                            <input  type="password" value="" placeholder="输入新密码" id="inputNewPassword" class="form-control">
                        </div>
                        <label class="offset-sm-1" id="newPasswordTip" style="display: none; color: red">密码不少于6位</label>
                        <br>


                        <div class="input-group">
                            <span class="input-group-addon">确认密码</span>
                            <input  type="password" value="" id="inputConfirmPassword" placeholder="确认以上新密码"
                                   class="form-control">
                        </div>
                        <label class="offset-sm-1" id="confirmPasswordTip" style="display: none; color: red">两次输入的密码不匹配</label>
                        <br>


                        <div class="input-group">
                            <span class="input-group-addon">个性签名</span>
                            <input type="text" value="${user.bio}" id="inputBio" placeholder="不超过50个字"
                                   class="form-control">
                        </div>
                        <label class="offset-sm-1" id="bioTip" style="display: none; color: red">个性签名不超过50个字</label>
                        <br>


                        <div class="input-group">
                            <span class="input-group-addon">发送代码</span>
                            <select class="form-control" name="sendCode" id="inputSendCode">
                                <option <c:if test="${user.sendCode == true}"> selected </c:if> value="true">是</option>
                                <option <c:if test="${user.sendCode == false}"> selected </c:if> value="false">否</option>
                            </select>
                        </div>
                        <br>

                        <div class="input-group">
                            <div class="input-group-addon">语言偏好</div>
                            <select class="form-control" name="preferenceLanguage" id="inputPreferLanguage">
                                <c:forEach items="${languages}" var="lang">
                                    <option <c:if test="${lang.language == user.preferLanguage}"> selected </c:if>
                                            value="${lang.language}">${lang.language}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" onclick="editUser()">保存修改</button>
                </div>
            </div>
        </div>
    </div>
    <jsp:include page="footer.jsp"/>
</body>
</html>
