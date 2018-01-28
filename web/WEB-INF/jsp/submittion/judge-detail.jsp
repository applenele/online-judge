<%--
  Created by IntelliJ IDEA.
  User: xanarry
  Date: 18-1-7
  Time: 下午1:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <title>提交记录</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="../../../css/bootstrap/bootstrap.min.css">

    <script src="../../../js/jquery-3.2.1.min.js"></script>
    <script src="../../../js/bootstrap/popper.min.js"></script>
    <script src="../../../js/bootstrap/bootstrap.min.js"></script>
</head>
<body>
<jsp:include page="../../../navbar.jsp"/>
<div class="container" style="margin-top: 70px">
    <div class="card">
        <h5 class="card-header">详细评测结果</h5>
        <table class="table table-sm table-striped">
            <thead>
            <tr>
                <th class="text-center">测试点</th>
                <th class="text-center">耗时(ms)</th>
                <th class="text-center">内存(KB)</th>
                <th class="text-center">结果</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${detailList}" var="detail">
                <tr>
                    <td class="text-center"><a
                            href="/show-test-point?problemID=${record.problemID}&testPointID=${detail.testPointID}">${detail.testPointID}</a>
                    </td>
                    <td class="text-center">${detail.timeConsume}</td>
                    <td class="text-center">${detail.memConsume}</td>
                    <td class="text-center">
                                <c:choose>
                                    <c:when test="${detail.result == 'Queuing'}"><span class="badge badge-secondary">${detail.result}</span></c:when>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${detail.result == 'Compiling'}"><span class="badge badge-secondary">${detail.result}</span></c:when>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${detail.result == 'Running'}"><span class="badge badge-primary">${detail.result}</span></c:when>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${detail.result == 'Accepted'}"><span class="badge badge-success">${detail.result}</span></c:when>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${detail.result == 'Presentation Error'}"><span class="badge badge-warning">${detail.result}</span></c:when>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${detail.result == 'Wrong Answer'}"><span class="badge badge-danger">${detail.result}</span></c:when>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${detail.result == 'Time Limit Exceeded'}"><span class="badge badge-warning">${detail.result}</span></c:when>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${detail.result == 'Memory Limit Exceeded'}"><span class="badge badge-warning">${detail.result}</span></c:when>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${detail.result == 'Output Limit Exceeded'}"><span class="badge badge-warning">${detail.result}</span></c:when>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${detail.result == 'Runtime Error'}"><span class="badge badge-danger">${detail.result}</span></c:when>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${detail.result == 'System Error'}"><span class="badge badge-dark">${detail.result}</span></c:when>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${detail.result == 'Compilation Error'}"><span class="badge badge-warning">${detail.result}</span></c:when>
                                </c:choose>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

    <br>
    <h4>源代码[${record.language}]</h4>
    <pre class="form-control pre-scrollable" style="font-family: Consolas"><c:out value="${record.sourceCode}" escapeXml="true"></c:out> </pre>

    <br>
    <h4>编译信息</h4>
    <pre class="form-control pre-scrollable" style="font-family: Consolas">${compileInfo.compileResult}</pre>


    <div class="text-center">
        <a class="btn btn-primary" href="/record-list">返回列表</a>
        <a class="btn btn-warning" href="/redudge?submitID=${record.submitID}">重新评测</a>
    </div>


    <jsp:include page="../../../footer.jsp"/>
</body>
</html>

