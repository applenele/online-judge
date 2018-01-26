<%--
  Created by IntelliJ IDEA.
  User: xanarry
  Date: 18-1-26
  Time: 下午3:53
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
<jsp:include page="/navbar.jsp"/>
<div class="container" style="margin-top: 70px">
    <div class="card">
        <div class="card-header">发送邮件给:${user.userName}</div>
        <div class="card-body">
            <form action="/send-email" method="post">
                <div class="form-group">
                    <label>邮箱</label>
                    <input type="text" name="inputEmailAddress" class="form-control" value="${user.email}" readonly placeholder="目标邮件地址">
                </div>
                <div class="form-group">
                    <label>内容</label>
                    <textarea class="form-control" id="inputContent" name="inputContent" rows="5"></textarea>
                </div>
                <div class="form-group">
                    <input type="submit" value="发送" class="btn btn-success">
                </div>
            </form>
        </div>
    </div>
</div>
<jsp:include page="/footer.jsp"/>
</body>
</html>
