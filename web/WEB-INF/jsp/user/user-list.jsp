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
    <link rel="stylesheet" href="/css/oj.css">

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
<jsp:include page="/WEB-INF/jsp/navbar.jsp"/>
<div class="container custom-container">

        <div class="row">
            <h4 class="col-2 align-self-end">${tableTitle}</h4>
            <div class="col-3 offset-7">
                <form style="margin-bottom: .5rem" action="/user-list" method="get">
                    <input type="text" name="keyword" class="form-control" placeholder="搜索用户">
                </form>
            </div>
        </div>

    <div class="card">
        <table class="table table-striped text-center" style="margin-bottom: 0rem;">
            <thead>
            <tr>
                <th>用户名</th>
                <th>用户类型</th>
                <th>个性签名</th>
                <th>注册日期</th>
                <th>语言偏好</th>
                <th>通过(题)/提交(次)</th>
                <c:if test="${not empty cookie.get('userType') and cookie.get('userType').value == 2}">
                    <th>操作</th>
                </c:if>
            </tr>
            </thead>
            <tbody>
            <jsp:useBean id="registerTime" class="java.util.Date"/>
            <c:forEach items="${userList}" var="user" varStatus="pos">
            <tr>
                <td><a href="/user?userID=${user.userID}">${user.userName}</a></td>
                <td>
                    <c:choose>
                        <c:when test="${user.userType == 2}">管理员</c:when>
                        <c:when test="${user.userType == 1}">高级用户</c:when>
                        <c:otherwise>普通用户</c:otherwise>
                    </c:choose>
                </td>
                <td>${user.bio}</td>
                    <c:set target="${registerTime}" property="time" value="${user.registerTime}"/>
                <td><fmt:formatDate pattern="yyyy/MM/dd" value="${registerTime}"/></td>
                <td>${user.preferLanguage}</td>
                <td>${user.accepted}/${user.submitted}</td>
                <c:if test="${not empty cookie.get('userType') and cookie.get('userType').value == 2}">
                <td><a href="#" onclick="confirmDeleteUser('${user.userID}', '${user.userName}')" class="badge badge-danger">删除</a></td>
                </c:if>
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

    <c:if test="${not empty pageInfo}">
        <jsp:include page="/WEB-INF/jsp/pagination.jsp"/>
    </c:if>
    <jsp:include page="/WEB-INF/jsp/footer.jsp"/>
</body>
</html>
