<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fnt" uri="http://java.sun.com/jsp/jstl/functions" %>
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

    <link rel="stylesheet" href="/css/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" href="/css/font-awesome.css">
    <link rel="stylesheet" href="/css/oj.css">

    <script src="/js/jquery-3.2.1.min.js"></script>
    <script src="/js/bootstrap/popper.min.js"></script>
    <script src="/js/bootstrap/bootstrap.min.js"></script>
</head>

<body>
<jsp:include page="/WEB-INF/jsp/navbar.jsp"/>
<div class="container custom-container">
    <c:if test="${discussList == null || fnt:length(discussList) == 0}">
    <c:choose>
    <c:when test="${!empty param.type && param.type == 0}"><h3>该题目下还没有讨论, 点下面创建新的讨论!</h3></c:when>
    <c:when test="${!empty param.type && param.type == 1}"><h3>该比赛下还没有讨论, 点下面创建新的讨论!</h3></c:when>
    <c:otherwise><h3>该话题下还没有讨论!</h3></c:otherwise>
    </c:choose>
    </c:if>

    <ul class="list-inline">
        <li class="list-inline-item"><h4>${tableTitle}</h4></li>
        <c:if test="${not empty cookie.get('userID').value}">
            <li class="list-inline-item">
                <button class="btn btn-primary" data-toggle="modal" data-target="#discussModal">创建新讨论</button>
            </li>
        </c:if>
    </ul>

    <div class="list-group">
        <c:forEach items="${discussList}" var="discuss">
        <div class="list-group-item list-group-item-action flex-column align-items-start ">
            <div class="media">
                <div class="align-self-center text-center mr-4">
                    <h3 style="width: 50px;">${discuss.reply}</h3>
                </div>
                <div class="media-body">
                    <div class="d-flex w-100 justify-content-between">
                        <a href="discuss-detail?postID=${discuss.postID}"><h5 class="mb-1">${discuss.title}</h5></a>
                    </div>
                    <ul class="mb-1 list-inline">
                        <c:if test="${discuss.first > 0}">
                            <span class="badge badge-danger"><i class="fa fa-arrow-up"></i></span>
                        </c:if>
                        <li class="list-inline-item">
                            <c:choose>
                                <c:when test="${discuss.type == 0}">
                                    <a href="/discuss?type=${discuss.type}&porcID=${discuss.porcID}">
                                        <small class="badge badge-primary">${discuss.theme}</small>
                                    </a>
                                </c:when>
                                <c:when test="${discuss.type == 1}">
                                    <a href="/discuss?type=${discuss.type}&porcID=${discuss.porcID}">
                                        <small class="badge badge-success">${discuss.theme}</small>
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <a href="/discuss?type=${discuss.type}&porcID=${discuss.porcID}">
                                        <small class="badge badge-secondary">${discuss.theme}</small>
                                    </a>
                                </c:otherwise>
                            </c:choose>
                        </li>
                        <li class="list-inline-item">
                            <a href="/user?userID=${discuss.userID}">
                                <small class="card alert-secondary">${discuss.userName}</small>
                            </a>
                        </li>
                        <li class="list-inline-item">
                            <jsp:useBean id="postTime" class="java.util.Date"/>
                            <c:set target="${postTime}" property="time" value="${discuss.postTime}"/>
                            <small class="text-muted"><fmt:formatDate pattern="yyyy/MM/dd HH:mm:ss"
                                                                      value="${postTime}"/></small>
                        </li>
                    </ul>
                </div>
                <c:if test="${not empty cookie.get('userType') and cookie.get('userType').value > 0}">
                    <div class="align-self-center ml-4">
                        <c:choose>
                            <c:when test="${discuss.first == 0}"><a
                                    href="/discuss-set-first?postID=${discuss.postID}&val=1" class="btn-sm btn-primary">置顶</a></c:when>
                            <c:otherwise><a href="/discuss-set-first?postID=${discuss.postID}&val=0"
                                            class="btn-sm btn-primary">取消置顶</a></c:otherwise>
                        </c:choose>
                        <a href="/discuss-delete?postID=${discuss.postID}" class="btn-sm btn-danger">删除</a>
                    </div>
                </c:if>
            </div>
        </div>
        </c:forEach>
        <br>
        <c:if test="${not empty pageInfo}">
            <jsp:include page="/WEB-INF/jsp/pagination.jsp"/>
        </c:if>
    </div>

    <div class="modal fade" id="discussModal">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">发布新的讨论</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                </div>
                <div class="modal-body">
                    <form action="/post-original-discuss" method="post">
                        <c:choose>
                            <c:when test="${!empty param.type}"><input id="inputType" name="inputType"
                                                                       value="${param.type}" hidden></c:when>
                            <c:otherwise><input id="inputType" name="inputType" value="2" hidden></c:otherwise>
                        </c:choose>
                        <input id="inputPorcID" name="inputPorcID" value="${param.porcID}" hidden>
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
    </div>

    <jsp:include page="/WEB-INF/jsp/footer.jsp"/>
</body>
</html>
