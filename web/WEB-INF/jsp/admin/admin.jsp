<%--
  Created by IntelliJ IDEA.
  User: xanarry
  Date: 18-1-17
  Time: 下午1:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>管理页面</title>

    <link rel="stylesheet" href="/css/bootstrap/bootstrap.min.css">

    <script src="/js/jquery-3.2.1.min.js"></script>
    <script src="/js/bootstrap/popper.min.js"></script>
    <script src="/js/bootstrap/bootstrap.min.js"></script>
</head>
<body>
<jsp:include page="/navbar.jsp"/>
<div class="container" style="margin-top: 70px">
    <div class="row">
        <div class="col-6">
            <a href="/add-contest">
            <div class="jumbotron jumbotron-fluid alert-primary">
                <div class="container">
                    <h1 class="display-5 text-center">添加比赛</h1>
                </div>
            </div>
            </a>
        </div>
        <div class="col-6">
            <a href="/add-problem">
            <div class="jumbotron jumbotron-fluid alert-primary">
                <div class="container">
                    <h1 class="display-5 text-center">添加题目</h1>
                </div>
            </div>
            </a>
        </div>
    </div>

    <div class="row">
        <div class="col-6">
            <a href="/user-list">
            <div class="jumbotron jumbotron-fluid alert-primary">
                <div class="container">
                    <h1 class="display-5 text-center">管理用户</h1>
                </div>
            </div>
            </a>
        </div>
        <div class="col-6">
            <a href="/configuration">
            <div class="jumbotron jumbotron-fluid alert-primary">
                <div class="container">
                    <h1 class="display-5 text-center">系统设置</h1>
                </div>
            </div>
            </a>
        </div>
    </div>

    <div class="row">
        <div class="col-6">
            <a href="/post-original-discuss">
                <div class="jumbotron jumbotron-fluid alert-primary">
                    <div class="container">
                        <h1 class="display-5 text-center">发布消息</h1>
                    </div>
                </div>
            </a>
        </div>
        <div class="col-6">
            <a href="/configuration">
                <div class="jumbotron jumbotron-fluid alert-primary">
                    <div class="container">
                        <h1 class="display-5 text-center">啦啦啦</h1>
                    </div>
                </div>
            </a>
        </div>
    </div>


    <div class="jumbotron jumbotron-fluid">
        <div class="container">
            <h1 class="display-4">管理用户</h1>
        </div>
    </div>

    <div class="jumbotron jumbotron-fluid">
        <div class="container">
            <h1 class="display-4">管理用户</h1>
        </div>
    </div>
    <div class="jumbotron jumbotron-fluid">
        <div class="container">
            <h1 class="display-4">管理用户</h1>
        </div>
    </div>

</div>
<jsp:include page="/footer.jsp"/>
</body>
</html>
