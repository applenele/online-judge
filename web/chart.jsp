<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: xanarry
  Date: 17-12-28
  Time: 下午6:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>排行榜</title>
    <link rel="stylesheet" href="css/bootstrap/bootstrap.min.css">

    <script src="js/jquery-3.2.1.min.js"></script>
    <script src="js/bootstrap/popper.min.js"></script>
    <script src="js/bootstrap/bootstrap.min.js"></script
</head>
<body>
<jsp:include page="navbar.jsp"/>
<div class="container" style="margin-top: 70px">
    <div class="card">
        <table class="table table-striped">
            <thead>
            <tr>
                <th>排名</th>
                <th>用户名</th>
                <th>个性签名</th>
                <th class="text-right">通过题数</th>
                <th>提交次数</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach begin="0" end="50" step="1" var="cnt">
                <tr>
                    <td>${cnt}</td>
                    <td><a href="#">xanarry${cnt}</a></td>
                    <td>love love love</td>
                    <td class="text-right">78</td>
                    <td>342</td>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <br>
    <nav aria-label="Page navigation example">
        <ul class="pagination justify-content-center">
            <li class="page-item"><a class="page-link" href="#">Previous</a></li>
            <li class="page-item"><a class="page-link" href="#">1</a></li>
            <li class="page-item"><a class="page-link" href="#">2</a></li>
            <li class="page-item"><a class="page-link" href="#">3</a></li>
            <li class="page-item"><a class="page-link" href="#">Next</a></li>
        </ul>
    </nav>
    <jsp:include page="footer.jsp"/>
</body>
</html>
