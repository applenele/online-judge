<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fnt" uri="http://java.sun.com/jsp/jstl/functions" %>
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
    <title>发送邮件</title>

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
        <c:choose>
            <c:when test="${not empty user}"><div class="card-header">发送邮件给:${user.userName}</div></c:when>
            <c:otherwise><div class="card-header">发送邮件给:用户列表(${fnt:length(emailList)})</div></c:otherwise>
        </c:choose>

        <div class="card-body">
            <form action="/send-email" method="post">
                <div class="form-group">
                    <label>邮箱</label>
                    <c:choose>
                        <c:when test="${not empty user}">
                            <input type="text" name="inputEmailAddress" class="form-control" value="${user.email}" readonly placeholder="目标邮件地址">
                        </c:when>
                        <c:otherwise>
                            <input type="text" name="inputEmailAddress" class="form-control" value="${emailList}" readonly>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="form-group">
                    <label>主题</label>
                    <input type="text" name="inputEmailAddress" class="form-control" value="oj系统邮件" readonly placeholder="oj系统邮件">
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
<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
</body>
</html>
