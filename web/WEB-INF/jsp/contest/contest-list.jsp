<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--
  Created by IntelliJ IDEA.
  User: xanarry
  Date: 17-12-28
  Time: 下午11:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>比赛列表</title>

    <link rel="stylesheet" href="/css/bootstrap/bootstrap.min.css">

    <script src="/js/jquery-3.2.1.min.js"></script>
    <script src="/js/bootstrap/popper.min.js"></script>
    <script src="/js/bootstrap/bootstrap.min.js"></script>

    <script>
        function deleteContest(contestID, contestTitle) {
            $("#message").html("是否删除比赛[" + contestTitle + "]");
            $("#url").attr('href', '/delete-contest?contestID=' + contestID);
            $("#deleteModal").modal('show');
        }
    </script>
</head>
<body>
<jsp:include page="/navbar.jsp"/>
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
                <th>ID</th>
                <th>标题</th>
                <th>状态</th>
                <th>开始时间</th>
                <th>结束时间</th>
                <th>比赛赛制</th>
                <th>比赛类型</th>
                <th>举办人</th>
                <th class="text-center">操作</th>
            </tr>
            </thead>
            <tbody>
            <jsp:useBean id="time" class="java.util.Date"/>

            <jsp:useBean id="current" class="java.util.Date" />

            <%--<h1>${current.time}</h1> this timestamp--%>

            <c:forEach items="${contestList}" var="contest">
                <tr>
                    <td><a href="/contest-overview?contestID=${contest.contestID}">${contest.contestID}</a></td>
                    <td><a href="/contest-overview?contestID=${contest.contestID}">${contest.title}</a></td>
                    <c:choose>
                        <c:when test="${current.time < contest.registerStartTime}">
                            <td><span class="badge badge-info">即将到来</span></td>
                        </c:when>
                        <c:when test="${current.time > contest.startTime && current.time < contest.endTime}">
                            <td><span class="badge badge-success">进行中</span></td>
                        </c:when>
                        <c:when test="${current.time > contest.registerStartTime && current.time < contest.registerEndTime}">
                            <td><span class="badge badge-primary">报名中</span></td>
                        </c:when>
                        <c:when test="${current.time > contest.endTime}">
                            <td><span class="badge badge-secondary">已结束</span></td>
                        </c:when>
                    </c:choose>


                    <c:set target="${time}" property="time" value="${contest.startTime}"/>
                    <td><fmt:formatDate pattern="yyyy/MM/dd HH:mm:ss" value="${time}"/></td>

                    <c:set target="${time}" property="time" value="${contest.endTime}"/>
                    <td><fmt:formatDate pattern="yyyy/MM/dd HH:mm:ss" value="${time}"/></td>

                    <td>${contest.contestType}</td>

                    <c:choose>
                        <c:when test="${contest.open==true}">
                            <td><span class="badge badge-success">公开</span></td>
                        </c:when>
                        <c:otherwise>
                            <td><span class="badge badge-secondary">加密</span></td>
                        </c:otherwise>
                    </c:choose>

                    <td>${contest.sponsor}</td>
                    <td class="text-center">
                        <a href="/edit-contest?contestID=${contest.contestID}"><span class="badge badge-secondary">编辑</span></a>
                        <a href="#" onclick="deleteContest(${contest.contestID},'${contest.title}')"><span class="badge badge-danger">删除</span></a>
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

    <div class="modal fade" id="deleteModal">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">重要提示</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <p id="message"></p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">返回</button>
                    <a id="url" href="#" class="btn btn-danger">是的,删除</a>
                </div>
            </div>
        </div>
    </div>
    <jsp:include page="/footer.jsp"/>
</body>
</html>
