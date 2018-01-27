<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: xanarry
  Date: 17-12-28
  Time: 下午6:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>排行榜</title>
    <link rel="stylesheet" href="/css/bootstrap/bootstrap.min.css">

    <script src="/js/jquery-3.2.1.min.js"></script>
    <script src="/js/bootstrap/popper.min.js"></script>
    <script src="/js/bootstrap/bootstrap.min.js"></script>
</head>
<body>
<jsp:include page="/navbar.jsp"/>
<div class="container" style="margin-top: 70px">
    <div class="card">
        <table class="table table-striped">
            <thead>
            <tr>
                <th class="text-center">排名</th>
                <th>用户名</th>
                <th>个性签名</th>
                <th class="text-center">注册日期</th>
                <th class="text-center">通过/提交</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${userList}" var="user" varStatus="pos">
                <tr>
                    <td class="text-center">${pos.count+(pageInfo.currentPageVal-1)*pageInfo.countPerPage}</td>
                    <td><a href="/user?userID=${user.userID}">${user.userName}</a></td>
                    <td>${user.bio}</td>
                        <jsp:useBean id="registerTime" class="java.util.Date"/>
                        <c:set target="${registerTime}" property="time" value="${user.registerTime}"/>
                    <td class="text-center"><fmt:formatDate pattern="yyyy/MM/dd" value="${registerTime}"/></td>
                    <td class="text-center">${user.accepted}/${user.submitted}</td>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <br>
    <c:if test="${not empty pageInfo}">
        <jsp:include page="/WEB-INF/jsp/pagination.jsp"/>
    </c:if>
    <jsp:include page="/footer.jsp"/>
</body>
</html>
