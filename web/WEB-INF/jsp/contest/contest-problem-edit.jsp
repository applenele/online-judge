<%--
  Created by IntelliJ IDEA.
  User: xanarry
  Date: 18-1-17
  Time: 下午10:13
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>比赛题目</title>

    <link rel="stylesheet" href="/css/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" href="/css/oj.css">

    <script src="/js/jquery-3.2.1.min.js"></script>
    <script src="/js/bootstrap/popper.min.js"></script>
    <script src="/js/bootstrap/bootstrap.min.js"></script>

    <script>
        $(document).ready(function () {
            $('#inputInnerID').bind('input propertychange', function () {
                var ele = $('#inputInnerID');
                ele.val(ele.val().toUpperCase());
                ele.focus();

            });
        });

        function checkProblemID() {
            var inputInnerID = $("#inputInnerID").val().trim();
            var inputProblemID = $("#inputProblemID").val();
            console.log(inputInnerID + " " + inputProblemID);

            if (inputInnerID.length == 0 || inputProblemID.length == 0) {
                $("#msg").html("输入不能有空");
                $("#messageModal").modal('show');
                return;
            }


            $('#contestProblemTable tr').each(function() {
                var innerID = this.cells[0].innerHTML;
                var problemID = this.cells[1].innerHTML;
                console.log(innerID + " " + problemID);
                if (inputInnerID.toUpperCase() == innerID.toUpperCase()) {
                    $("#msg").html("比赛内部ID[" + innerID  + "]已经存在, 请重新指定");
                    $("#messageModal").modal('show');
                    return;
                }
                if (inputProblemID == problemID) {
                    $("#msg").html("题目[" + problemID + "]已经添加在比赛中, 请重新指定");
                    $("#messageModal").modal('show');
                    return;
                }
            });

            $.ajax({
                type: 'POST',
                url: '/ajax-check-problem-exist',
                data: {'problemID': inputProblemID},
                dataType: "json",
                success: function (data) {
                    console.log(JSON.stringify(data));
                    //"{problemID : %s, exist :%s}"
                    console.log(data.exist);
                    console.log(data.problemID);
                    if (data.exist == true) {
                        $("#addContestProblemForm").submit();
                    } else {
                        $("#msg").html("题目[" + inputProblemID + "]不存在, 请重新指定");
                        $('#messageModal').modal('show');
                    }
                },
                error: function (e) {
                    alert("ajax error occured" + JSON.stringify(e));
                }
            });
        }
    </script>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/navbar.jsp"/>

<div class="container custom-container">
    <a href="/contest-overview?contestID=${contest.contestID}"><h3 class="text-center">${contest.title}</h3></a>

    <%--在此出获取当前时间--%>
    <jsp:useBean id="current" class="java.util.Date" />
    <div class="card">
        <div class="card-header"><h5>题目列表</h5></div>
        <div class="card-body">
            <c:choose>
                <c:when test="${fn:length(problemList) > 0}">
                    <div class="card">
                        <table class="table table-sm table-striped text-center" style="margin-bottom: 0rem;">
                            <thead>
                            <tr>
                                <th>比赛题号</th>
                                <th>题库题号</th>
                                <th>题目名称</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody id="contestProblemTable">
                            <c:forEach items="${problemList}" var="contestProblem">
                                <tr>
                                    <td><a>${contestProblem.innerID}</td>
                                    <td>${1000+contestProblem.problemID}</td>
                                    <td><a href="/problem?problemID=${contestProblem.problemID}">${contestProblem.title}</a></td>
                                    <c:choose>
                                        <c:when test="${current.time < contest.startTime - 10*60*1000}"><%--比赛前10分钟之前允许删除用户--%>
                                            <td><a href="/delete-contest-problem?contestID=${contest.contestID}&innerID=${contestProblem.innerID}">删除</a></td>
                                        </c:when>
                                        <c:otherwise>
                                            <td><span>比赛已经开始</span></td>
                                        </c:otherwise>
                                    </c:choose>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:when>
                <c:otherwise>
                    <h5>待添加题目</h5>
                </c:otherwise>
            </c:choose>

            <br>

            <form method="post" id="addContestProblemForm" action="/contest-problem-edit">
                <input name="inputContestID" value="${contest.contestID}" hidden>
                <div class="form-row align-items-center">
                    <div class="col-sm-5">
                        <div class="input-group">
                            <span class="input-group-addon">比赛题号</span>
                            <input name="inputInnerID" id="inputInnerID" type="text" class="form-control">
                        </div>
                    </div>

                    <div class="col-sm-5">
                        <div class="input-group">
                            <span class="input-group-addon">题库题号</span>
                            <input name="inputProblemID" id="inputProblemID" type="text" class="form-control">
                        </div>
                    </div>

                    <div class="col-sm-2">
                        <div class="input-group">
                            <span class="btn btn-primary" id="addProblemBtn" onclick="checkProblemID()">加入题目列表</span>
                        </div>
                    </div>
                </div>
            </form>
            <div class="text-center">
                <a href="/contest-overview?contestID=${contest.contestID}" class="btn btn-success">完成操作</a>
            </div>
        </div>

    </div>
</div>



<div class="modal fade" id="messageModal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">错误提示</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <p id="msg">输入的题目ID不存在, 请输入正确的ID</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" data-dismiss="modal">知道了</button>
            </div>
        </div>
    </div>
</div>


<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
</body>
</html>


