<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fnt" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: xanarry
  Date: 17-12-28
  Time: 上午10:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>提交代码</title>

    <link rel="stylesheet" href="/css/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" href="/css/oj.css">

    <script src="/js/jquery-3.2.1.min.js"></script>
    <script src="/js/bootstrap/popper.min.js"></script>
    <script src="/js/bootstrap/bootstrap.min.js"></script>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/navbar.jsp"/>

<%--
        request.setAttribute("problem", problemBean);
        request.setAttribute("languages", languages);
        request.setAttribute("user", userBean);
        request.setAttribute("submitRecords", submitRecordBeans);

--%>

<div class="container custom-container">
    <a href="/problem?problemID=${problem.problemID}"><h3 class="text-center">${problem.title}</h3></a>
    <div class="card">
        <h5 class="card-header">本题最近提交记录</h5>
        <c:choose>
            <c:when test="${recordList != null && fnt:length(recordList) > 0}">
                <table class="table table-sm table-striped">
                    <thead>
                    <tr>
                        <th class="text-center">提交ID</th>
                        <th class="text-center">题号</th>
                        <th class="text-center">耗时(ms)</th>
                        <th class="text-center">内存(KB)</th>
                        <th class="text-center">语言</th>
                        <th class="text-center">代码长度(字节)</th>
                        <th class="text-center">提交时间</th>
                        <th class="text-center">结果</th>
                    </tr>
                    </thead>
                    <tbody>
                    <jsp:useBean id="submitTime" class="java.util.Date"/>
                    <c:forEach items="${recordList}" var="record">
                        <tr>
                            <td class="text-center">${record.submitID}</td>
                            <td class="text-center">p${1000 + record.problemID}</td>
                            <td class="text-center">${record.timeConsume}</td>
                            <td class="text-center">${record.memConsume}</td>
                            <td class="text-center"><span class="badge badge-secondary">${record.language}</span></td>
                            <td class="text-center">${record.codeLength}</td>
                            <c:set target="${submitTime}" property="time" value="${record.submitTime}"/>
                            <td class="text-center"><fmt:formatDate pattern="yyyy/MM/dd HH:mm:ss"
                                                                  value="${submitTime}"/></td>
                            <td class="text-center">
                                <c:choose>
                                    <c:when test="${record.result == 'Queuing'}"><span class="badge badge-secondary">${record.result}</span></c:when>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${record.result == 'Compiling'}"><span class="badge badge-secondary">${record.result}</span></c:when>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${record.result == 'Running'}"><span class="badge badge-primary">${record.result}</span></c:when>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${record.result == 'Accepted'}"><span class="badge badge-success">${record.result}</span></c:when>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${record.result == 'Presentation Error'}"><span class="badge badge-warning">${record.result}</span></c:when>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${record.result == 'Wrong Answer'}"><span class="badge badge-danger">${record.result}</span></c:when>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${record.result == 'Time Limit Exceeded'}"><span class="badge badge-warning">${record.result}</span></c:when>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${record.result == 'Memory Limit Exceeded'}"><span class="badge badge-warning">${record.result}</span></c:when>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${record.result == 'Output Limit Exceeded'}"><span class="badge badge-warning">${record.result}</span></c:when>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${record.result == 'Runtime Error'}"><span class="badge badge-danger">${record.result}</span></c:when>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${record.result == 'System Error'}"><span class="badge badge-dark">${record.result}</span></c:when>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${record.result == 'Compilation Error'}"><span class="badge badge-warning">${record.result}</span></c:when>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <div class="modal-body">
                    <h4 class="text-center">此题目没有提交记录</h4>
                </div>
            </c:otherwise>
        </c:choose>

    </div>

    <br>

    <div class="card">
        <h5 class="card-header">提交代码</h5>
        <div class="card-body">
            <%--<h4 class="card-title">Special title treatment</h4>--%>
            <form class="form-horizontal" action="/submit" method="post">
                <input hidden name="inputProblemID" value="${problem.problemID}">
                <div class="form-group">
                    <label>源代码</label>
                    <textarea class="form-control" name="inputCode" rows="10"></textarea>
                </div>

                <div class="form-row align-items-center">
                    <div class="col-sm-3">
                        <div class="input-group mb-2 mb-sm-0">
                            <div class="input-group-addon">语言: </div>
                            <select class="form-control" name="inputLanguage">
                                <c:forEach items="${languages}" var="lang">
                                    <option <c:if test="${lang.language == user.preferLanguage}"> selected </c:if>
                                            value="${lang.language}">${lang.language}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="col-auto">
                        <button type="submit" class="btn btn-primary">提交代码</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
</body>
</html>
