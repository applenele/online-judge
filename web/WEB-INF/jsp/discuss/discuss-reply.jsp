<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: xanarry
  Date: 17-12-28
  Time: 下午11:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>discuss</title>

    <link rel="stylesheet" href="/css/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" href="/css/oj.css">

    <script src="/js/jquery-3.2.1.min.js"></script>
    <script src="/js/bootstrap/popper.min.js"></script>
    <script src="/js/bootstrap/bootstrap.min.js"></script>

    <script src="/plugin/ckeditor/ckeditor.js"></script>

    <script>
        function problemCheck() {
            $('textarea.ckeditor').each(function () {
                var $textarea = $(this);
                $textarea.val(CKEDITOR.instances[$textarea.attr('id')].getData());
            });
        }
    </script>
</head>


<body>
<jsp:include page="/WEB-INF/jsp/navbar.jsp"/>
<div class="container custom-container">
    <div class="card">
        <div class="card-header"><h5>${discuss.title}</h5></div>
        <div class="card-body">
            <p class="card-text">${discuss.content}</p>
            <hr style="margin-top: 1.5rem; margin-bottom: 0;">
            <ul class="mb-1 list-inline">
                <li class="list-inline-item">
                    <c:choose>
                        <c:when test="${discuss.type == 0}">
                            <a href="/problem?problemID=${discuss.porcID}"><small class="badge badge-secondary">${discuss.theme}</small></a>
                        </c:when>
                        <c:when test="${discuss.type == 1}">
                            <a href="/contest-overview?contestID=${discuss.porcID}"><small class="badge badge-secondary">${discuss.theme}</small></a>
                        </c:when>
                        <c:otherwise><a href="/discuss?theme=${discuss.theme}"><small class="badge badge-secondary">${discuss.theme}</small></a></c:otherwise>
                    </c:choose>

                </li>
                <li class="list-inline-item">
                    <a href="/user?userID=${discuss.userID}"><small class="card alert-secondary">${discuss.userName}</small></a>
                </li>
                <li class="list-inline-item">
                    <small class="text-muted">${discuss.watch}次查看</small>
                </li>
                <li class="list-inline-item">
                    <jsp:useBean id="postTime" class="java.util.Date"/>
                    <c:set target="${postTime}" property="time" value="${discuss.postTime}"/>
                    <small class="text-muted"><fmt:formatDate pattern="yyyy/MM/dd HH:mm:ss" value="${postTime}"/></small>
                </li>
            </ul>
        </div>
    </div>

    <br>
    <a class="btn btn-primary" href="/discuss-list">返回列表</a>
    <button class="btn btn-success" data-toggle="collapse" href="#collapseReply">发表回复</button>
    <br>
        <div class="collapse" id="collapseReply">
            <div class="card">
            <div class="card-body">
                <form class="form-group" action="/post-discuss" method="post">
                    <input hidden name="inputRootID" value="${discuss.postID}">
                    <input hidden name="inputDirectFID" value="${discuss.postID}">
                    <input hidden name="inputPorcID" value="${discuss.porcID}">
                    <input hidden name="inputType" value="${discuss.type}">
                    <textarea name="inputContent" id="inputComment" class="form-control" placeholder="在此输入您的回复内容"
                              style="height:150px; font-family: Consolas"></textarea>
                    <%-- <script>
                         CKEDITOR.replace('inputComment'); // 这里的 'editor1' 等于 textarea 的 id 'editor1'
                     </script>--%>
                    <br><input class="btn btn-success" type="submit" value="发布回复">
                </form>
            </div>
            </div>
        </div>

    <br>
    <div class="list-group">
        <div class="list-group-item list-group-item-action flex-column align-items-start ">
            <div class="media">
                <div class="align-self-center text-center mr-4">
                    <h5>回复列表(${discuss.reply})</h5>
                </div>
            </div>
        </div>

        <c:forEach items="${replyList}" var="reply">
        <div class="list-group-item list-group-item-action flex-column align-items-start ">
            <div class="media">
                <div class="media-body">
                    <div class="d-flex w-100 justify-content-between">
                        <p class="card-text" style="color: black">${reply.content}</p>
                    </div>
                    <ul class="mb-1 list-inline">
                        <li class="list-inline-item">
                            <a href="/user?userID=${reply.userID}"><small class="card alert-secondary">${reply.userName}</small></a>
                        </li>
                        <li class="list-inline-item">
                            <jsp:useBean id="replyTime" class="java.util.Date"/>
                            <c:set target="${replyTime}" property="time" value="${discuss.postTime}"/>
                            <small class="text-muted">在<fmt:formatDate pattern="yyyy/MM/dd HH:mm:ss" value="${replyTime}"/>回复</small>
                        </li>
                    </ul>


                </div>
                <div class="align-self-md-start ml-4">
                    <a href="/discuss-delete?postID=${reply.postID}" class="btn-sm btn-primary">回复</a>
                    <a href="/discuss-delete?postID=${reply.postID}" class="btn-sm btn-danger">删除</a>
                </div>
            </div>
        </div>
        </c:forEach>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
</body>
</html>
</html>
