<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
    <link rel="stylesheet" href="/css/oj.css">

    <script src="/js/jquery-3.2.1.min.js"></script>
    <script src="/js/bootstrap/popper.min.js"></script>
    <script src="/js/bootstrap/bootstrap.min.js"></script>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/navbar.jsp"/>
<div class="container custom-container">
    <h4>${tableTitle}</h4>
    <div class="card">
        <table class="table table-striped text-center">
            <thead>
            <tr>
                <th>排名</th>
                <th>用户名</th>
                <th>个性签名</th>
                <th>注册日期</th>
                <th>通过/提交</th>
            </tr>
            </thead>
            <tbody>
            <c:if test="${fn:length(userList) > 0}">
                <c:set var="lastRank" value="1"/>
                <c:set var="lastAccepted" value="${userList[0].accepted}"/>
                <c:set var="lastSubmitted" value="${userList[0].submitted}"/>
            </c:if>
            <c:forEach items="${userList}" var="user" varStatus="pos">
            <tr>
                <c:choose>
                <c:when test="${user.accepted == lastAccepted and user.submitted == lastSubmitted}">
                <td>${lastRank}</td>
                </c:when>

                <c:otherwise>
                <td>${pos.count+(pageInfo.currentPageVal-1)*pageInfo.countPerPage}</td>
                    <c:set var="lastRank" value="${pos.count+(pageInfo.currentPageVal-1)*pageInfo.countPerPage}"/>
                    <c:set var="lastAccepted" value="${user.accepted}"/>
                    <c:set var="lastSubmitted" value="${user.submitted}"/>
                </c:otherwise>
                </c:choose>

                <td><a href="/user?userID=${user.userID}">${user.userName}</a></td>
                <td>${user.bio}</td>
                    <jsp:useBean id="registerTime" class="java.util.Date"/>
                    <c:set target="${registerTime}" property="time" value="${user.registerTime}"/>
                <td><fmt:formatDate pattern="yyyy/MM/dd" value="${registerTime}"/></td>
                <td>${user.accepted}/${user.submitted}</td>
                </c:forEach>
            </tbody>
        </table>
    </div>
    <br>
    <c:if test="${not empty pageInfo}">
        <jsp:include page="/WEB-INF/jsp/pagination.jsp"/>
    </c:if>
    <jsp:include page="/WEB-INF/jsp/footer.jsp"/>
</body>
</html>
