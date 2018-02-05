<%--
  Created by IntelliJ IDEA.
  User: xanarry
  Date: 18-2-5
  Time: 下午3:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>评测机</title>
    <link rel="stylesheet" href="/css/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" href="/css/oj.css">

    <script src="/js/jquery-3.2.1.min.js"></script>
    <script src="/js/bootstrap/popper.min.js"></script>
    <script src="/js/bootstrap/bootstrap.min.js"></script>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/navbar.jsp"/>
<div class="container custom-container">
    <div class="card">
        <div class="card-header">
            <h5>注意事项</h5>
        </div>
        <div class="card-body">
            <p>
                1: 提交Java程序时, 请勿使用包, 类名请使用Main, 否则无法通过编译.<br>
                2: 对于java和Python, 代码在运行时候出错, 如除零, 溢出, 空指针, 下标越界, 系统会给Runtime Error, 但不显示具体错误信息.<br>
                3: 如果使用了系统限制的函数, 系统会给Runtime Error.<br>
            </p>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
</body>
</html>
