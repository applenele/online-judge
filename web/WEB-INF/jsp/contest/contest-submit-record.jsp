<%--
  Created by IntelliJ IDEA.
  User: xanarry
  Date: 18-1-27
  Time: 下午3:33
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fnt" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>参赛名单</title>

    <link rel="stylesheet" href="/css/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" href="/css/oj.css">

    <script src="/js/jquery-3.2.1.min.js"></script>
    <script src="/js/bootstrap/popper.min.js"></script>
    <script src="/js/bootstrap/bootstrap.min.js"></script>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/navbar.jsp"/>
<div class="container custom-container">
    <div class="text-center"><a href="contest-overview?contestID=${contest.contestID}"><h4>${contest.title}</h4></a>
    </div>
    <div class="card">
        <div class="card-header"><h5>${tableTitle}</h5></div>
        <div class="card-body">
            <form action="/contest-record-list" method="get">
                <input hidden name="contestID" value="${contest.contestID}">
                <div class="input-group">
                    <span class="input-group-addon">用户名:</span>
                    <input name="userName" type="text" value="" class="form-control">
                    <span class="input-group-addon">题号:</span>
                    <select name="problemID" class="form-control">
                        <option value="">All</option>
                        <c:forEach items="${problemIDMaper}" var="m">
                            <option value="${m.key}">${m.value}</option>
                        </c:forEach>
                    </select>

                    <span class="input-group-addon">评测结果:</span>
                    <select name="result" class="form-control">
                        <option value="">All</option>
                        <option value="Accepted">Accepted</option>
                        <option value="Presentation Error">Presentation Error</option>
                        <option value="Time Limit Exceeded">Time Limit Exceeded</option>
                        <option value="Memory Limit Exceeded">Memory Limit Exceeded</option>
                        <option value="Wrong Answer">Wrong Answer</option>
                        <option value="Runtime Error">Runtime Error</option>
                        <option value="Output Limit Exceeded">Output Limit Exceeded</option>
                        <option value="Compilation Error">Compilation Error</option>
                        <option value="System Error">System Error</option>
                    </select>

                    <span class="input-group-addon">语言:</span>
                    <select name="language" class="form-control">
                        <option value="">All</option>
                        <option value="C">C</option>
                        <option value="C++">C++</option>
                        <option value="Java">Java</option>
                        <option value="Python2">Python2</option>
                        <option value="Python3">Python3</option>
                    </select>
                    <span class="input-group-btn"><button class="btn btn-default">查询</button></span>
                </div>
            </form>

            <div class="card">

                <table class="table table-striped text-center">
                    <thead>
                    <tr>
                        <th>用户</th>
                        <th>题目ID</th>
                        <th>题目</th>
                        <th>耗时(ms)</th>
                        <th>内存(KB)</th>
                        <th>语言</th>
                        <th>代码长度(字节)</th>
                        <th>提交时间</th>
                        <th>结果</th>
                        <c:if test="${not empty cookie.get('userType') and cookie.get('userType').value > 0}">
                            <th>重测</th>
                        </c:if>
                    </tr>
                    </thead>
                    <tbody>
                    <jsp:useBean id="submitTime" class="java.util.Date"/>
                    <c:forEach items="${recordList}" var="record">
                        <tr>
                            <td><a href="/user?userID=${record.userID}"> ${record.userName} </a></td>
                            <td><a href="/contest-detail?contestID=${contest.contestID}&curProblem=${problemIDMaper.get(record.problemID)}">${problemIDMaper.get(record.problemID)}</a></td>
                            <td><a href="/contest-detail?contestID=${contest.contestID}&curProblem=${problemIDMaper.get(record.problemID)}">${record.problemTitle} </a></td>
                            <td>${record.timeConsume}</td>
                            <td>${record.memConsume}</td>
                            <td><span class="badge badge-secondary">${record.language}</span></td>
                            <td>${record.codeLength}</td>

                            <c:set target="${submitTime}" property="time" value="${record.submitTime}"/>
                            <td><fmt:formatDate pattern="yyyy/MM/dd HH:mm:ss"
                                                                    value="${submitTime}"/></td>
                            <td>
                                <a href="/judge-detail?submitID=${record.submitID}">
                                    <c:choose>
                                        <c:when test="${record.result == 'Queuing'}"><span
                                                class="badge badge-secondary">${record.result}</span></c:when>
                                    </c:choose>
                                    <c:choose>
                                        <c:when test="${record.result == 'Compiling'}"><span
                                                class="badge badge-secondary">${record.result}</span></c:when>
                                    </c:choose>
                                    <c:choose>
                                        <c:when test="${record.result == 'Running'}"><span
                                                class="badge badge-primary">${record.result}</span></c:when>
                                    </c:choose>
                                    <c:choose>
                                        <c:when test="${record.result == 'Accepted'}"><span
                                                class="badge badge-success">${record.result}</span></c:when>
                                    </c:choose>
                                    <c:choose>
                                        <c:when test="${record.result == 'Presentation Error'}"><span
                                                class="badge badge-warning">${record.result}</span></c:when>
                                    </c:choose>
                                    <c:choose>
                                        <c:when test="${record.result == 'Wrong Answer'}"><span
                                                class="badge badge-danger">${record.result}</span></c:when>
                                    </c:choose>
                                    <c:choose>
                                        <c:when test="${record.result == 'Time Limit Exceeded'}"><span
                                                class="badge badge-warning">${record.result}</span></c:when>
                                    </c:choose>
                                    <c:choose>
                                        <c:when test="${record.result == 'Memory Limit Exceeded'}"><span
                                                class="badge badge-warning">${record.result}</span></c:when>
                                    </c:choose>
                                    <c:choose>
                                        <c:when test="${record.result == 'Output Limit Exceeded'}"><span
                                                class="badge badge-warning">${record.result}</span></c:when>
                                    </c:choose>
                                    <c:choose>
                                        <c:when test="${record.result == 'Runtime Error'}"><span
                                                class="badge badge-danger">${record.result}</span></c:when>
                                    </c:choose>
                                    <c:choose>
                                        <c:when test="${record.result == 'System Error'}"><span
                                                class="badge badge-dark">${record.result}</span></c:when>
                                    </c:choose>
                                    <c:choose>
                                        <c:when test="${record.result == 'Compilation Error'}"><span
                                                class="badge badge-warning">${record.result}</span></c:when>
                                    </c:choose>
                                </a>
                            </td>
                            <c:if test="${not empty cookie.get('userType') and cookie.get('userType').value > 0}">
                                <td><a class="badge badge-primary" href="/rejudge?submitID=${record.submitID}">重测</a></td>
                            </c:if>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
        <c:if test="${not empty pageInfo}">
            <jsp:include page="/WEB-INF/jsp/pagination.jsp"/>
        </c:if>
    </div>
    <jsp:include page="/WEB-INF/jsp/footer.jsp"/>
</body>
</html>
