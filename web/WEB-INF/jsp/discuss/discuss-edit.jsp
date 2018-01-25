<%--
  Created by IntelliJ IDEA.
  User: xanarry
  Date: 18-1-23
  Time: 下午10:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>编辑讨论内容</title>

    <link rel="stylesheet" href="css/bootstrap/bootstrap.min.css">

    <script src="js/jquery-3.2.1.min.js"></script>
    <script src="js/bootstrap/popper.min.js"></script>
    <script src="js/bootstrap/bootstrap.min.js"></script>
</head>
<body>
<jsp:include page="navbar.jsp"/>
<div class="container" style="margin-top: 70px">
    <div class="card">
        <div class="card-header">发布新的消息</div>
        <div class="card-body">
            <form action="/post-original-discuss" method="post">
                <input id="inputType" name="inputType" value="2" hidden>
                <div class="form-group">
                    <label>标题</label>
                    <input type="text" class="form-control" name="inputTitle" id="inputTitle" placeholder="标题">
                </div>
                <div class="form-group">
                    <label>内容</label>
                    <textarea class="form-control" id="inputContent" name="inputContent" rows="5"></textarea>
                </div>
                <div class="form-group">
                    <input type="submit" value="发布" class="btn btn-success">
                </div>
            </form>
        </div>
    </div>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>
