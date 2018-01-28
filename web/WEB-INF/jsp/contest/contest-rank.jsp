<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: xanarry
  Date: 18-1-18
  Time: 下午7:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${contest.title}</title>

    <link rel="stylesheet" href="/css/bootstrap/bootstrap.min.css">

    <script src="/js/jquery-3.2.1.min.js"></script>
    <script src="/js/bootstrap/popper.min.js"></script>
    <script src="/js/bootstrap/bootstrap.min.js"></script>
</head>

<body>
<jsp:include page="/navbar.jsp"/>
<div class="container" style="margin-top: 70px">
    <h2 align="center"><a href="/contest-overview?contestID=${contest.contestID}">${contest.title}</a></h2>

    <div class="card">
        <div class="card-header">比赛排名</div>
        <div class="card-body">
            <div class="card">
                <table class="table table-sm table-striped text-center">
                    <thead>
                    <tr>
                        <th>排名</th>
                        <th>用户名</th>
                        <th>AC题数</th>
                        <th>罚时</th>
                        <c:forEach items="${problemOverview}" var="problem">
                            <th><a href="/contest-detail?contestID=${contest.contestID}&problemID=${problem.innerID}">${problem.innerID}(${problem.accepted}/${problem.submitted})</a></th>
                        </c:forEach>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${rankList}" var="rankBean" varStatus="pos">
                        <tr>
                            <td style="vertical-align: middle">${pos.count}</td>
                            <td style="vertical-align: middle"><a href="/user?userID=${rankBean.userID}"
                                   class="myuser-base myuser-violet">${rankBean.userName}</a></td>
                            <td style="vertical-align: middle">${rankBean.AC_Count}</td>
                            <td style="vertical-align: middle"><fmt:formatNumber type="number" value="${rankBean.totalTimeConsume / 1000}"
                                                  maxFractionDigits="0" groupingUsed="false"/></td>
                            <c:forEach items="${problemOverview}" var="problem">
                                <c:choose>
                                    <%--如果当前题目有当前用户的提交信息,那么显示数据--%>
                                    <c:when test="${rankBean.problems.get(problem.problemID) != null}">
                                        <c:choose>
                                            <c:when test="${rankBean.problems.get(problem.problemID).firstAccepted}">
                                                <td style="background-color: #a6f3a6">
                                                    +${rankBean.problems.get(problem.problemID).tryTimes}<br>${rankBean.problems.get(problem.problemID).timeConsume}
                                                </td>
                                            </c:when>
                                            <c:when test="${rankBean.problems.get(problem.problemID).accepted}">
                                                <td style="background-color: #dbffdb;">
                                                    +${rankBean.problems.get(problem.problemID).tryTimes}<br>${rankBean.problems.get(problem.problemID).timeConsume}
                                                </td>
                                            </c:when>
                                            <c:otherwise>
                                                <c:choose>
                                                    <c:when test="${rankBean.problems.get(problem.problemID).tryTimes != 0}">
                                                        <td style="vertical-align: middle" class="alert-danger">-${rankBean.problems.get(problem.problemID).tryTimes}</td>
                                                    </c:when>
                                                    <c:otherwise><td style="vertical-align: middle"></td></c:otherwise>
                                                </c:choose>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:when>
                                    <c:otherwise>
                                        <td style="vertical-align: middle"></td>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
</div>
<jsp:include page="/footer.jsp"/>
</body>
</html>