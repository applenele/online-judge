<%--
  Created by IntelliJ IDEA.
  User: xanarry
  Date: 18-1-6
  Time: 下午1:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>测试点</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">


    <link rel="stylesheet" href="css/bootstrap/bootstrap.min.css">

    <script src="js/jquery-3.2.1.min.js"></script>
    <script src="js/bootstrap/popper.min.js"></script>
    <script src="js/bootstrap/bootstrap.min.js"></script>
</head>
<body>
<jsp:include page="/navbar.jsp"/>
<div class="container" style="margin-top: 70px">
    <h3 class="text-center"><a href="/problem?problemID=${problemID}">${problem.title}</a></h3>

    <div class="card">
        <h5 class="card-header">测试点${testPointID.testPointID}</h5>
        <div class="card-body">
            <%--<h4 class="card-title">Special title treatment</h4>--%>
            <form class="form-group" action="/add-test-point" method="post">
                <label>输入数据</label>
                <textarea class="form-control pre-scrollable" name="inputInputData" readonly rows="7"
                          style="font-family: Consolas">${inputText}</textarea>

                <br>
                <label>输入数据</label>
                <textarea class="form-control pre-scrollable" name="inputOutputData" readonly rows="7"
                          style="font-family: Consolas">${outputText}</textarea>
                <br>
                <div class="text-center">
                    <a class="btn btn-primary" href="/test-point-list?problemID=${problem.problemID}">返回数据列表</a>
                </div>
            </form>
        </div>
    </div>
</div>
<jsp:include page="/footer.jsp"/>
</div>
</body>
</html>


