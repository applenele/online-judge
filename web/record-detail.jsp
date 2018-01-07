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
    <link rel="stylesheet" href="css/bootstrap/bootstrap.min.css">

    <script src="js/jquery-3.2.1.min.js"></script>
    <script src="js/bootstrap/popper.min.js"></script>
    <script src="js/bootstrap/bootstrap.min.js"></script>
</head>
<body>
<jsp:include page="navbar.jsp"/>
<div class="container" style="margin-top: 70px">
    <div class="card">
        <h5 class="card-header">详细评测结果</h5>
        <table class="table table-sm table-striped">
            <thead>
            <tr>
                <th class="text-center">测试点</th>
                <th class="text-center">数据长度</th>
                <th class="text-center">耗时(ms)</th>
                <th class="text-center">内存(KB)</th>
                <th class="text-center">返回值</th>
                <th class="text-center">结果</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${detailList}" var="detail">
                <tr>
                    <td class="text-center">${detail.testPointID}</td>
                    <td class="text-center">${detail.inputTextLength}</td>
                    <td class="text-center">${detail.timeConsume}</td>
                    <td class="text-center">${detail.memConsume}</td>
                    <td class="text-center">${detail.returnVal}</td>
                    <td class="text-center">${detail.result}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>


    <br>
    <h4>源代码</h4>
    <pre class="form-control pre-scrollable" style="font-family: Consolas">${record.sourceCode}</pre>


    <div class="text-center">
        <a class="btn btn-primary" href="/record-list">返回记录列表</a>
    </div>


    <jsp:include page="footer.jsp"/>
</body>
</html>

