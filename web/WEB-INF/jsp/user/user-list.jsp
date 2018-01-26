<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: xanarry
  Date: 18-1-25
  Time: 下午5:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>用户列表</title>
    <link rel="stylesheet" href="/css/bootstrap/bootstrap.min.css">

    <script src="/js/jquery-3.2.1.min.js"></script>
    <script src="/js/bootstrap/popper.min.js"></script>
    <script src="/js/bootstrap/bootstrap.min.js"></script>

    <script>
        function confirmDeleteUser(userID, userName) {
            $("#warningMessage").html("删除用户[<b>" + userName + "</b>]的所有信息, 是否继续?");
            $("#deleteUser").attr('href', '/user-delete?userID=' + userID);
            $("#deleteModal").modal('show');
        }
    </script>
</head>
<body>
<jsp:include page="/navbar.jsp"/>
<div class="container" style="margin-top: 70px">
    <div class="card">
        <table class="table table-striped">
            <thead>
            <tr>
                <th class="text-center">用户名</th>
                <th class="text-center">用户类型</th>
                <th class="text-center">个性签名</th>
                <th class="text-center">注册日期</th>
                <th class="text-center">语言偏好</th>
                <th class="text-center">通过/提交</th>
                <th class="text-center">操作</th>
            </tr>
            </thead>
            <tbody>
            <jsp:useBean id="registerTime" class="java.util.Date"/>
            <c:forEach items="${userList}" var="user" varStatus="pos">
            <tr>
                <td class="text-center"><a href="/user?userID=${user.userID}">${user.userName}</a></td>
                <td class="text-center">
                    <c:choose>
                        <c:when test="${user.userType == 2}">管理员</c:when>
                        <c:when test="${user.userType == 1}">高级用户</c:when>
                        <c:otherwise>普通用户</c:otherwise>
                    </c:choose>
                </td>
                <td class="text-center">${user.bio}</td>
                    <c:set target="${registerTime}" property="time" value="${user.registerTime}"/>
                <td class="text-center"><fmt:formatDate pattern="yyyy/MM/dd" value="${registerTime}"/></td>
                <td class="text-center">${user.preferLanguage}</td>
                <td class="text-center">${user.accepted}/${user.submitted}</td>
                <td class="text-center"><a href="#" onclick="confirmDeleteUser('${user.userID}', '${user.userName}')" class="badge badge-danger">删除</a href="#"></td>
                </c:forEach>
            </tbody>
        </table>
    </div>
    <br>

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
                    <p id="warningMessage">与该用户相关的所有信息即将永久性删除, 是否继续?</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">返回</button>
                    <a id="deleteUser" href="#" class="btn btn-danger">是的,删除</a>
                </div>
            </div>
        </div>
    </div>

    <p>pageinfo: ${pageInfo}</p>
    <c:if test="${not empty pageInfo}">
        <jsp:include page="/WEB-INF/jsp/pagination.jsp"/>
    </c:if>
    <jsp:include page="/footer.jsp"/>
</body>
</html>
