<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: xanarry
  Date: 18-1-18
  Time: 下午7:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${contest.title}</title>

    <link rel="stylesheet" href="css/bootstrap/bootstrap.min.css">

    <script src="js/jquery-3.2.1.min.js"></script>
    <script src="js/bootstrap/popper.min.js"></script>
    <script src="js/bootstrap/bootstrap.min.js"></script>
</head>
<body>
<jsp:include page="navbar.jsp"/>
<div class="container" style="margin-top: 70px">
    <h1 align="center">${contest.title}</h1>
    <div class="card">
        <div class="card-header">overview</div>
        <div class="progress">
            <div class="progress-bar" role="progressbar" style="width: 25%;" aria-valuenow="25" aria-valuemin="0" aria-valuemax="100">25%</div>
        </div>
        <div class="card-body">

            <table align="center">
                <tbody>
                <tr>
                    <jsp:useBean id="time" class="java.util.Date"/>
                    <c:set target="${time}" property="time" value="${contest.registerStartTime}"/>
                    <td><b>报名时间:</b><fmt:formatDate pattern="yyyy/MM/dd HH:mm:ss" value="${time}"/></td>
                    <td></td>
                    <c:set target="${time}" property="time" value="${contest.registerEndTime}"/>
                    <td><b>报名截止:</b><fmt:formatDate pattern="yyyy/MM/dd HH:mm:ss" value="${time}"/></td>
                    <td></td>
                    <td><b>报名状态:</b><label class="label label-success">已报名</label></td>
                </tr>
                <tr>
                    <c:set target="${time}" property="time" value="${contest.startTime}"/>
                    <td><b>开始时间:</b><fmt:formatDate pattern="yyyy/MM/dd HH:mm:ss" value="${time}"/></td>
                    <td>&nbsp;&nbsp;</td>
                    <c:set target="${time}" property="time" value="${contest.endTime}"/>
                    <td><b>结束时间:</b><fmt:formatDate pattern="yyyy/MM/dd HH:mm:ss" value="${time}"/></td>
                    <td>&nbsp;&nbsp;</td>
                    <td><b>比赛状态:</b><label class="label label-success">进行中</label></td>
                </tr>
                <tr>
                    <td><b>举办人:</b><a href="/user?userName=${contest.sponsor}">${contest.sponsor}</a></td>
                    <td></td>
                    <td><b>参赛人数:</b>待参数</td>
                    <td></td>
                    <td><b>竞赛规则:</b>${contest.contestType}</td>
                </tr>
                </tbody>
            </table>
            <br>
            <p>${contest.desc}</p>
            <br>
            <div class="card">
                <table class="table table-sm table-striped">
                    <thead>
                    <tr>
                        <th class="text-center">比赛题号</th>
                        <th class="text-center">题目名称</th>
                        <th class="text-center">全局ID</th>
                        <th class="text-center">通过(人)/提交(次)</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${problemList}" var="problem">
                        <td class="text-center"><a href="/contest-detail?contestID=${contest.contestID}&curProblem=${problem.innerID}">${problem.innerID}</a></td>
                            <td class="text-center">题目名</td>
                            <td class="text-center"><a href="/problem?problemID=${1000+problem.problemID}">${1000 + problem.problemID}</a></td>
                            <td class="text-center">${problem.accepted}/${problem.submitted}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
            <br>

            <div class="row">
                <div class="col-6 offset-3">
                    <div class="text-center">
                        <span class="btn btn-primary">编辑比赛</span>
                        <span class="btn btn-primary">全部提交</span>
                        <span class="btn btn-primary">全部用户</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>
