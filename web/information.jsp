<%--
  Created by IntelliJ IDEA.
  User: xanarry
  Date: 18-1-1
  Time: 下午11:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${msg.title}</title>
    <link rel="stylesheet" href="css/bootstrap/bootstrap.min.css">

    <script src="js/jquery-3.2.1.min.js"></script>
    <script src="js/bootstrap/popper.min.js"></script>
    <script src="js/bootstrap/bootstrap.min.js"></script>
</head>

<body>
<jsp:include page="navbar.jsp"/>
<div class="container" style="margin-top: 70px">
    <div class="card">
        <div class="card-header">
            <h2>${msg.header}</h2>
        </div>
        <div class="card-body text-center">
            <h3>${msg.message}</h3>
            <br>
            <br>
            <a class="btn btn-lg btn-success" href="${msg.url}">${msg.linkText}</a>
        </div>
    </div>
</div>

<jsp:include page="footer.jsp"/>
</body>
</html>
