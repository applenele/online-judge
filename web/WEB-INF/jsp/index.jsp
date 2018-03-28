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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fnt" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
  <title>首页</title>
  <meta charset="utf-8">
  <link rel="stylesheet" href="/css/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" href="/css/oj.css">

  <script src="/js/jquery-3.2.1.min.js"></script>
  <script src="/js/bootstrap/popper.min.js"></script>
  <script src="/js/bootstrap/bootstrap.min.js"></script>

</head>
<body>
<jsp:include page="/WEB-INF/jsp/navbar.jsp"/>
<div class="container custom-container">
    <div class="jumbotron" style="background-size: cover; background-image: url('/img/b.gif');">
        <br><br><br><br><br><br><br>
        <div class="text-center">
            <a class="btn btn-lg btn-success" href="/problem-list" role="button">不如做题</a>
        </div>
    </div>

    <h4>最近比赛</h4>
    <table class="table table-striped text-center">
        <thead>
        <tr>
            <th>ID</th>
            <th>标题</th>
            <th>状态</th>
            <th>开始时间</th>
            <th>结束时间</th>
            <th>比赛赛制</th>
            <th>比赛类型</th>
        </tr>
        </thead>
        <tbody>
        <jsp:useBean id="time" class="java.util.Date"/>
        <jsp:useBean id="current" class="java.util.Date" />
        <c:forEach items="${latestContest}" var="contest">
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
                    <c:when test="${fnt:length(contest.password) == 0}">
                        <td><span class="badge badge-success">公开</span></td>
                    </c:when>
                    <c:otherwise>
                        <td><span class="badge badge-secondary">加密</span></td>
                    </c:otherwise>
                </c:choose>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <br>
    <h4>最近讨论</h4>
    <div class="list-group">
        <c:forEach items="${latestDiscuss}" var="discuss">
            <div class="list-group-item list-group-item-action flex-column align-items-start ">
                <div class="media">
                    <div class="align-self-center text-center mr-4">
                        <h3 style="width: 50px;">${discuss.reply}</h3>
                    </div>
                    <div class="media-body">
                        <div class="d-flex w-100 justify-content-between">
                            <a href="discuss-detail?postID=${discuss.postID}"><h5 class="mb-1">${discuss.title}</h5></a>
                        </div>
                        <ul class="mb-1 list-inline">
                            <c:if test="${discuss.first > 0}">
                                <span class="badge badge-danger"><i class="fa fa-arrow-up"></i></span>
                            </c:if>
                            <li class="list-inline-item">
                                <c:choose>
                                    <c:when test="${discuss.type == 0}"><%--题目讨论--%>
                                        <a href="/discuss-list?type=${discuss.type}&porcID=${discuss.porcID}">
                                            <small class="badge badge-primary">${discuss.theme}</small>
                                        </a>
                                    </c:when>
                                    <c:when test="${discuss.type == 1}"><%--比赛讨论--%>
                                        <a href="/discuss-list?type=${discuss.type}&porcID=${discuss.porcID}">
                                            <small class="badge badge-success">${discuss.theme}</small>
                                        </a>
                                    </c:when>
                                    <c:otherwise><%--消息发布--%>
                                        <a href="/discuss-list?type=${discuss.type}&theme=${discuss.theme}">
                                            <small class="badge badge-secondary">${discuss.theme}</small>
                                        </a>
                                    </c:otherwise>
                                </c:choose>
                            </li>
                            <li class="list-inline-item">
                                <a href="/user?userID=${discuss.userID}"><small class="card alert-secondary">${discuss.userName}</small></a>
                            </li>
                            <li class="list-inline-item">
                                <jsp:useBean id="postTime" class="java.util.Date"/>
                                <c:set target="${postTime}" property="time" value="${discuss.postTime}"/>
                                <small class="text-muted"><fmt:formatDate pattern="yyyy/MM/dd HH:mm:ss" value="${postTime}"/></small>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
</body>
</html>

