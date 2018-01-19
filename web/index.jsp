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
  <title>首页</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="css/bootstrap/bootstrap.min.css">

  <script src="js/jquery-3.2.1.min.js"></script>
  <script src="js/bootstrap/popper.min.js"></script>
  <script src="js/bootstrap/bootstrap.min.js"></script>

</head>
<body>
<jsp:include page="navbar.jsp"/>
<div class="container" style="margin-top:70px">
    <div class="jumbotron" style="background-size: cover; background-image: url('/img/b.gif');">
        <br><br><br><br><br><br><br>
        <div class="text-center">
            <a class="btn btn-lg btn-success" href="/problem-list" role="button">不如做题</a>
        </div>
    </div>

    <h4>最近比赛</h4>
    <table class="table table-striped">
        <thead>
        <tr>
            <th>ID</th>
            <th>标题</th>
            <th>状态</th>
            <th>开始时间</th>
            <th>结束时间</th>
            <th>比赛赛制</th>
            <th>比赛类型</th>
            <th>举办人</th>
            <th class="text-center">操作</th>
        </tr>
        </thead>
        <tbody>
        <jsp:useBean id="time" class="java.util.Date"/>

        <jsp:useBean id="current" class="java.util.Date" />

        <%--<h1>${current.time}</h1> this timestamp--%>

        <c:forEach items="${contestList}" var="contest">
            <tr>
                <td><a href="/contest-overview?contestID=${contest.contestID}">${contest.contestID}</a></td>
                <td><a href="/contest-overview?contestID=${contest.contestID}">${contest.title}</a></td>
                <c:choose>
                    <c:when test="${current.time < contest.registerStartTime}">
                        <td><span class="badge badge-info">即将到来</span></td>
                    </c:when>
                    <c:when test="${current.time > contest.startTime && current.time < contest.endTime}">
                        <td><span class="badge badge-success">进行中</span></td>
                    </c:when>
                    <c:when test="${current.time > contest.registerStartTime && current.time < contest.registerEndTime}">
                        <td><span class="badge badge-primary">报名中</span></td>
                    </c:when>
                    <c:when test="${current.time > contest.endTime}">
                        <td><span class="badge badge-secondary">已结束</span></td>
                    </c:when>
                </c:choose>


                <c:set target="${time}" property="time" value="${contest.startTime}"/>
                <td><fmt:formatDate pattern="yyyy/MM/dd HH:mm:ss" value="${time}"/></td>

                <c:set target="${time}" property="time" value="${contest.endTime}"/>
                <td><fmt:formatDate pattern="yyyy/MM/dd HH:mm:ss" value="${time}"/></td>

                <td>${contest.contestType}</td>

                <c:choose>
                    <c:when test="${contest.open==true}">
                        <td><span class="badge badge-success">公开</span></td>
                    </c:when>
                    <c:otherwise>
                        <td><span class="badge badge-secondary">加密</span></td>
                    </c:otherwise>
                </c:choose>

                <td>${contest.sponsor}</td>
                <td class="text-center">
                    <a href="/edit-contest?contestID=${contest.contestID}"><span class="badge badge-secondary">编辑</span></a>
                    <a href="#" onclick="deleteContest(${contest.contestID},'${contest.title}')"><span class="badge badge-danger">删除</span></a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>

