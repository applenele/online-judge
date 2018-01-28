<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: xanarry
  Date: 18-1-27
  Time: 下午11:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>参赛用户</title>

    <link rel="stylesheet" href="/css/bootstrap/bootstrap.min.css">

    <script src="/js/jquery-3.2.1.min.js"></script>
    <script src="/js/bootstrap/popper.min.js"></script>
    <script src="/js/bootstrap/bootstrap.min.js"></script>
</head>
<body>
<jsp:include page="/navbar.jsp"/>
<div class="container" style="margin-top: 70px">
    <h2 align="center"><a href="/contest-overview?contestID=${contest.contestID}">${contest.title}</a></h2>

    <%--在此出获取当前时间--%>
    <jsp:useBean id="current" class="java.util.Date" />
    <div class="card">
        <div class="card-header"><h5>overview</h5></div>
        <div class="card-body">
            <div class="card">
                <table class="table table-sm table-striped text-center">
                    <thead>
                    <tr>
                        <th>用户ID</th>
                        <th>用户名</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${userList}" var="user">
                        <tr>
                        <td><a href="/user?userID=${user.userID}">${user.userID}</a></td>
                        <td><a href="/user?userID=${user.userID}">${user.userName}</a></td>
                            <c:choose>
                                <c:when test="${current.time < contest.startTime - 10*60*1000}"><%--比赛前10分钟之前允许删除用户--%>
                                    <td><a href="/contest-user-delete?contestID=${contest.contestID}&userID=${user.userID}">移除比赛</a></td>
                                </c:when>
                                <c:otherwise>
                                    <td><span>移除比赛</span></td>
                                </c:otherwise>
                            </c:choose>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
            <br>
            <div class="text-center">
                <a class="btn btn-success" href="/contest-overview?contestID=${contest.contestID}">返回比赛</a>
            </div>
        </div>
    </div>
</div>


<div class="modal fade" id="msgModal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">重要提示</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <p>请登录后再报名</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" data-dismiss="modal">知道了</button>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/footer.jsp"/>
</body>
</html>
