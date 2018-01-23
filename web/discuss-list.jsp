<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: xanarry
  Date: 17-12-28
  Time: 下午11:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>讨论</title>

    <link rel="stylesheet" href="css/bootstrap/bootstrap.min.css">

    <script src="js/jquery-3.2.1.min.js"></script>
    <script src="js/bootstrap/popper.min.js"></script>
    <script src="js/bootstrap/bootstrap.min.js"></script>
</head>

<body>
<jsp:include page="navbar.jsp"/>
<div class="container" style="margin-top: 70px">
    <div class="list-group">
        <c:forEach items="${discussList}" var="discuss">
        <div class="list-group-item list-group-item-action flex-column align-items-start ">
            <div class="media">
                <div class="align-self-center text-center mr-4">
                   <h3 style="width: 50px">${discuss.reply}</h3>
                </div>
                <div class="media-body">
                    <div class="d-flex w-100 justify-content-between">
                        <a href="discuss-detail?postID=${discuss.postID}"><h5 class="mb-1">${discuss.title}</h5></a>
                    </div>
                    <ul class="mb-1 list-inline">
                        <li class="list-inline-item">
                            <a href="/discuss?theme=${discuss.theme}"><small class="badge badge-secondary">${discuss.theme}</small></a>
                        </li>
                        <li class="list-inline-item">
                            <a href="/user?userID=${discuss.userID}"><small class="card alert-secondary">${discuss.userName}</small></a>
                        </li>
                        <li class="list-inline-item">
                            <jsp:useBean id="postTime" class="java.util.Date"/>
                            <c:set target="${postTime}" property="time" value="${discuss.postTime}"/>
                            <small class="text-muted"><fmt:formatDate pattern="yyyy/MM/dd HH:mm:ss" value="${postTime}"/></small>
                        </li>
                    </ul>
                </div>
                <div class="align-self-center ml-4">
                    <a href="#" class="btn-sm btn-primary">置顶</a>
                    <a href="#" class="btn-sm btn-danger">删除</a>
                </div>
            </div>
        </div>
        </c:forEach>

        <div class="list-group-item list-group-item-action flex-column align-items-start ">
            <div class="media">
                <div class="align-self-center text-center mr-4">
                    <h3 style="width: 50px">213</h3>
                </div>
                <div class="media-body">
                    <div class="d-flex w-100 justify-content-between">
                        <h5 class="mb-1"><a>这里是讨论的标题</a></h5>
                    </div>
                    <ul class="mb-1 list-inline">
                        <li class="list-inline-item">
                            <small class="badge badge-secondary">A+B Prolem</small>
                        </li>
                        <li class="list-inline-item">
                            <small class="card alert-secondary">xanarry</small>
                        </li>
                        <li class="list-inline-item">
                            <small class="text-muted">2018-12-13 12:12:12</small>
                        </li>
                    </ul>
                </div>
                <div class="align-self-center ml-4">
                    <a href="#" class="btn-sm btn-primary">置顶</a>
                    <a href="#" class="btn-sm btn-danger">删除</a>
                </div>
            </div>
        </div>

    </div>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>
