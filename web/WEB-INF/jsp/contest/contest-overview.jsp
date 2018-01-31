<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fnt" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--
  Created by IntelliJ IDEA.
  User: xanarry
  Date: 18-1-18
  Time: 下午7:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${contest.title}</title>

    <link rel="stylesheet" href="/css/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" href="/css/oj.css">

    <script src="/js/jquery-3.2.1.min.js"></script>
    <script src="/js/bootstrap/popper.min.js"></script>
    <script src="/js/bootstrap/bootstrap.min.js"></script>

    <script>

        var SecondsTohhmmss = function(totalSeconds) {
            var hours   = Math.floor(totalSeconds / 3600);
            var minutes = Math.floor((totalSeconds - (hours * 3600)) / 60);
            var seconds = totalSeconds - (hours * 3600) - (minutes * 60);

            // round seconds
            seconds = Math.round(seconds * 100) / 100

            var result = (hours < 10 ? "0" + hours : hours);
            result += "小时" + (minutes < 10 ? "0" + minutes : minutes);
            result += "分钟" + (seconds  < 10 ? "0" + seconds : seconds);
            result += "秒";

            return hours + "小时" + minutes + "分钟" + seconds + "秒";
        }
        
        var s = new Date().getTime();

        $(function() {
            setInterval("GetTime()", 1000);
        });
        

        function GetTime() {
            var min = $("#processBar").attr('aria-valuemin');
            var max = $("#processBar").attr('aria-valuemax');
            var cur = new Date().getTime();

            var passed = cur - min;
            var total  = max - min;

            if (passed <= total) {
                $("#processBar").attr('aria-valuenow', new Date().getTime());
                $("#processBar").css('width', (passed/total)*100 + '%');

                var remainSeconds = Math.floor((total-passed) / 1000);
                $("#remainTime").html("剩余:" + SecondsTohhmmss(remainSeconds));
                if (remainSeconds < 600) {
                    $("#processBar").attr('class', 'progress-bar bg-danger');
                } else {
                    $("#processBar").attr('class', 'progress-bar bg-success');
                }

            }
        }
    </script>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/navbar.jsp"/>
<div class="container custom-container">
    <h2 align="center"><a href="/contest-overview?contestID=${contest.contestID}">${contest.title}</a></h2>

    <%--在此出获取当前时间--%>
    <jsp:useBean id="current" class="java.util.Date" />
    <div class="card">
        <div class="card-header"><h5>overview</h5></div>
        <c:if test="${current.time <= contest.endTime and current.time >= contest.startTime}">
            <div class="progress" style="height: 10px;">
                <div id="processBar" class="progress-bar bg-success" role="progressbar" style="width: 0%;" aria-valuenow="${contest.startTime}" aria-valuemin="${contest.startTime}" aria-valuemax="${contest.endTime}" ></div>
            </div>
            <label class="text-sm-right" id="remainTime" style="font-size: 10px; color: blue">剩余时间:</label>
        </c:if>
        <div class="card-body">
            <table align="center">
                <tbody>
                <tr>
                    <jsp:useBean id="time" class="java.util.Date"/>
                    <c:set target="${time}" property="time" value="${contest.registerStartTime}"/>
                    <td><b>报名时间:</b><fmt:formatDate pattern="yyyy/MM/dd HH:mm:ss" value="${time}"/></td>
                    <td></td>
                    <c:set target="${time}" property="time" value="${contest.registerEndTime}"/>
                    <td><b>报名截止:</b><fmt:formatDate pattern="yyyy/MM/dd HH:mm:ss" value="${time}"/></td>
                    <td></td>

                    <td>
                        <b>报名状态:</b>

                        <%--检查当前登录用户是否报名--%>
                        <c:choose>
                            <%--已经报名则显示已经报名--%>
                            <c:when test="${isRegistered == true}">
                                <span class="badge badge-success">已报名</span>
                            </c:when>
                            <%--如果没有报名--%>
                            <c:otherwise>
                                <%--检查比赛是否还在报名日期--%>
                                <c:choose>
                                    <c:when test="${current.time > contest.registerStartTime && current.time < contest.registerEndTime}">
                                        <%--如果还在报名日期中, 检查是否登录--%>
                                        <c:choose>
                                            <%--没有登录则要求先登录--%>
                                            <c:when test="${empty cookie.get('userID').value}">
                                                <a class="badge badge-warning" href="#" data-toggle="modal" data-target="#msgModal">我要报名</a>
                                            </c:when>
                                            <%--已经登录则根据比赛是公开还是私有判断使用什么方式报名--%>
                                            <c:otherwise>
                                                <c:choose>
                                                    <c:when test="${fnt:length(contest.password) > 0}"><%--比赛需要密码--%>
                                                        <a class="badge badge-warning" href="#" data-toggle="modal" data-target="#passwordModal">我要报名</a>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <a href="/contest-register?contestID=${contest.contestID}" class="badge badge-warning">我要报名</a>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:when>
                                    <c:otherwise>
                                        <span href="#" class="badge badge-danger">已截止</span>
                                    </c:otherwise>
                                </c:choose>
                            </c:otherwise>
                        </c:choose>

                    </td>
                </tr>
                <tr>
                    <c:set target="${time}" property="time" value="${contest.startTime}"/>
                    <td><b>开始时间:</b><fmt:formatDate pattern="yyyy/MM/dd HH:mm:ss" value="${time}"/></td>
                    <td>&nbsp;&nbsp;</td>
                    <c:set target="${time}" property="time" value="${contest.endTime}"/>
                    <td><b>结束时间:</b><fmt:formatDate pattern="yyyy/MM/dd HH:mm:ss" value="${time}"/></td>
                    <td>&nbsp;&nbsp;</td>
                    <td>
                        <b>比赛状态:</b>
                        <c:choose>
                            <c:when test="${current.time < contest.registerStartTime}">
                                <span class="badge badge-info">即将到来</span><
                            </c:when>
                            <c:when test="${current.time > contest.startTime && current.time < contest.endTime}">
                                <span class="badge badge-success">进行中</span>
                            </c:when>
                            <c:when test="${current.time > contest.registerStartTime && current.time < contest.registerEndTime}">
                                <span class="badge badge-primary">报名中</span>
                            </c:when>
                            <c:when test="${current.time > contest.endTime}">
                                <span class="badge badge-danger">已结束</span>
                            </c:when>
                        </c:choose>
                    </td>
                </tr>
                <tr>
                    <td><b>举办人:</b><a href="/user?userName=${contest.sponsor}">${contest.sponsor}</a></td>
                    <td></td>
                    <td><b>参赛人数:</b>${userCount}</td>
                    <td></td>
                    <td>
                        <b>竞赛规则:</b>
                        <c:choose>
                            <c:when test="${contest.contestType == 'ACM'}"><span class="badge badge-success">ACM</span></c:when>
                            <c:otherwise><span class="badge badge-primary">IO</span></c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                </tbody>
            </table>
            <br>
            <p>${contest.desc}</p>
            <br>
            <div class="card">
                <table class="table table-sm table-striped">
                    <thead>
                    <tr>
                        <th class="text-center">比赛题号</th>
                        <th class="text-center">题目名称</th>
                        <th class="text-center">全局ID</th>
                        <th class="text-center">通过(人)/提交(次)</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${problemList}" var="problem">
                        <td class="text-center"><a href="/contest-detail?contestID=${contest.contestID}&curProblem=${problem.innerID}">${problem.innerID}</a></td>
                        <td class="text-center"><a href="/contest-detail?contestID=${contest.contestID}&curProblem=${problem.innerID}">${problem.title}</a></td>
                            <td class="text-center">${1000 + problem.problemID}</td>
                            <td class="text-center">${problem.accepted}/${problem.submitted}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
            <br>

            <div class="row">
                <div class="col-6 offset-3">
                    <div class="text-center">
                        <a href="/discuss-list?type=1&porcID=${contest.contestID}" class="btn btn-primary">讨论</a>
                        <a href="/contest-record-list?contestID=${contest.contestID}" class="btn btn-primary">全部提交</a>
                        <a href="/contest-rank?contestID=${contest.contestID}" class="btn btn-primary">查看排名</a>
                        <a href="/contest-user-list?contestID=${contest.contestID}" class="btn btn-primary">查看用户</a>
                        <a href="/contest-edit?contestID=${contest.contestID}"><span class="btn btn-success">编辑比赛</span></a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


<div class="modal fade" id="msgModal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">重要提示</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <p>请登录后再报名</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" data-dismiss="modal">知道了</button>
            </div>
        </div>
    </div>
</div>



<div class="modal fade" id="passwordModal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">${contest.title}</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form action="/contest-register" method="post">
            <div class="modal-body">
                <div class="form-group">
                    <label>该比赛已经加密, 请输入比赛密码进入比赛</label>
                    <input hidden name="contestID" value="${contest.contestID}">
                    <input type="password" class="form-control" type="password" name="inputContestPassword" placeholder="Password">
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-secondary" data-dismiss="modal">返回</button>
                <button class="btn btn-success" type="submit">报名</button>
            </div>
            </form>
        </div>
    </div>
</div>



<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
</body>
</html>
