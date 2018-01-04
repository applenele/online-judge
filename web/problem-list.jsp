<%--
  Created by IntelliJ IDEA.
  User: xanarry
  Date: 17-12-27
  Time: 下午10:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html>
<head>
    <title>题目列表</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="css/bootstrap/bootstrap.min.css">

    <script src="js/jquery-3.2.1.min.js"></script>
    <script src="js/bootstrap/popper.min.js"></script>
    <script src="js/bootstrap/bootstrap.min.js"></script>
</head>
<body>
<jsp:include page="navbar.jsp"/>
<div class="container" style="margin-top: 70px">

    <nav aria-label="Page navigation example">
        <ul class="pagination justify-content-center">
            <li class="page-item"><a class="page-link" href="#">Previous</a></li>
            <li class="page-item"><a class="page-link" href="#">1</a></li>
            <li class="page-item"><a class="page-link" href="#">2</a></li>
            <li class="page-item"><a class="page-link" href="#">3</a></li>
            <li class="page-item"><a class="page-link" href="#">Next</a></li>
        </ul>
    </nav>

    <div class="card">
        <table class="table table-striped">
            <thead>
            <tr>
                <th>状态</th>
                <th>题号</th>
                <th>题目名称</th>
                <th>通过率</th>
                <th class="text-center">操作</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach begin="0" end="50" step="1">
                <tr>
                    <td><span class="badge badge-success">已通过</span></td>
                    <td>1000</td>
                    <td>题目名称题目名称题目名称题目名称</td>
                    <td>67%</td>
                    <td class="text-center">
                        <span class="badge badge-light">记录</span>
                        <span class="badge badge-secondary">数据</span>
                        <span class="badge badge-primary">编辑</span>
                    </td>
                </tr>
                <tr>
                    <td><span class="badge badge-secondary">已尝试</span></td>
                    <td>1000</td>
                    <td>asdfasdfasdf</td>
                    <td>67%</td>
                    <td class="text-center">
                        <span class="badge badge-light">记录</span>
                        <span class="badge badge-secondary">数据</span>
                        <span class="badge badge-primary">编辑</span>
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td>1000</td>
                    <td>题目名称题目名称</td>
                    <td>67%</td>
                    <td class="text-center">
                        <span class="badge badge-secondary">记录</span>
                        <span class="badge badge-secondary">数据</span>
                        <span class="badge badge-secondary">编辑</span>
                    </td>
                </tr>
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
