<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: xanarry
  Date: 17-12-28
  Time: 下午10:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>添加比赛</title>

    <link rel="stylesheet" href="/css/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" href="/plugin/datetimepicker/css/tempusdominus-bootstrap-4.min.css"/>
    <link rel="stylesheet" href="/css/font-awesome.css">
    <link rel="stylesheet" href="/css/oj.css">

    <script src="/js/jquery-3.2.1.min.js"></script>
    <script src="/js/bootstrap/popper.min.js"></script>
    <script src="/js/bootstrap/bootstrap.min.js"></script>

    <script src="/plugin/ckeditor/ckeditor.js"></script>
    <script src="/plugin/datetimepicker/js/moment-with-locales.min.js"></script>
    <script src="/plugin/datetimepicker/js/tempusdominus-bootstrap-4.min.js"></script>

    <script>
        $(function () {
            $('#inputStartTime').datetimepicker({
                locale: 'zh-cn',
                format: "YYYY/MM/DD HH:mm"
            });

            $('#inputEndTime').datetimepicker({
                locale: 'zh-cn',
                format: "YYYY/MM/DD HH:mm"
            });

            $('#inputRegisterStartTime').datetimepicker({
                locale: 'zh-cn',
                format: "YYYY/MM/DD HH:mm"
            });

            $('#inputRegisterEndTime').datetimepicker({
                locale: 'zh-cn',
                format: "YYYY/MM/DD HH:mm"
            });
        });

        function checkForm() {
            $('textarea.ckeditor').each(function () {
                var $textarea = $(this);
                $textarea.val(CKEDITOR.instances[$textarea.attr('id')].getData());
            });


            var desc = $("#inputContestDesc").val();
            if (desc.length > 400) {
                alert("比赛描述不能超过400字");
                return false;
            }


            var title = $("#inputTitle").val();
            var strStartTime = $("#inputStartTime").val();
            var strEndTime = $("#inputEndTime").val();
            var strRegisterStartTime = $("#inputRegisterStartTime").val();
            var strRegisterEndTime = $("#inputRegisterEndTime").val();

            console.log(title);
            console.log(strStartTime);
            console.log(strEndTime);
            console.log(strRegisterStartTime);
            console.log(strRegisterEndTime);


            if (title.length == 0 || strStartTime.length == 0 || strEndTime.length == 0 || strRegisterStartTime.length == 0 || strRegisterEndTime.length == 0) {
                alert("输入有空");
                return false;
            }


            var startTime = new Date(strStartTime + ":00").getTime();
            var endTime = new Date(strEndTime + ":00").getTime();

            var registerStartTime = new Date(strRegisterStartTime + ":00").getTime();
            var registerEndTime = new Date(strRegisterEndTime + ":00").getTime();

            var current = new Date().getTime();


            if (startTime >= endTime) {
                alert("比赛开始时间必须在比赛结束时间之前");
                return false;
            }


            if (registerStartTime >= registerEndTime) {
                alert("报名开始时间必须在报名结束时间之前");
                return false;
            } else if (registerEndTime > endTime) {
                alert("报名结束时间不能在比赛结束时间之后");
                return false;
            }

            /*if (registerStartTime < current || startTime < current) {
                alert("时间不能在现在之前");
                return false;
            }*/

            return true;
        }

    </script>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/navbar.jsp"/>

<div class="container custom-container">
    <div class="card">
        <div class="card-header"><h4>添加比赛</h4></div>
        <div class="card-body">
            <c:choose>
                <c:when test="${contest != null}">
                        <form method="post" action="/contest-edit" onsubmit="return checkForm()">
                        <input name="inputContestID" value="${contest.contestID}" hidden>
                </c:when>
                <c:otherwise>
                        <form method="post" action="/contest-add" onsubmit="return checkForm()">
                </c:otherwise>
            </c:choose>
            <form method="post" action="/contest-add" onsubmit="return checkForm()">
                <div class="input-group">
                    <span class="input-group-addon">比赛名称</span>
                    <input name="inputTitle" id="inputTitle" type="text" value="${contest.title}"
                           placeholder="标题不超过200个字符" class="form-control">
                </div>
                <br>

                <jsp:useBean id="time" class="java.util.Date"/>
                <div class="form-row align-items-center">
                    <div class="col-sm-6">
                        <div class="input-group">
                            <span class="input-group-addon">开始时间</span>
                            <c:set target="${time}" property="time"
                                   value="${contest != null ? contest.startTime : time.time}"/>
                            <input name="inputStartTime" type="text" placeholder="格式: yyyy/mm/dd hh:mm"
                                   value="<fmt:formatDate pattern="yyyy/MM/dd HH:mm" value="${time}"/>"
                                   id="inputStartTime" class="form-control" data-toggle="datetimepicker"
                                   data-target="#inputStartTime">
                        </div>
                    </div>

                    <div class="col-sm-6">
                        <div class="input-group">
                            <span class="input-group-addon">结束时间</span>
                            <c:set target="${time}" property="time"
                                   value="${contest != null ? contest.endTime : time.time}"/>
                            <input name="inputEndTime" type="text" placeholder="格式: yyyy/mm/dd hh:mm"
                                   value="<fmt:formatDate pattern="yyyy/MM/dd HH:mm" value="${time}"/>"
                                   id="inputEndTime" class="form-control" data-toggle="datetimepicker"
                                   data-target="#inputEndTime">
                        </div>
                    </div>
                </div>

                <br>
                <div class="form-row align-items-center">
                    <div class="col-sm-6">
                        <div class="input-group">
                            <span class="input-group-addon">报名开始</span>
                            <c:set target="${time}" property="time"
                                   value="${contest != null ? contest.registerStartTime : time.time}"/>
                            <input name="inputRegisterStartTime" type="text" placeholder="格式: yyyy/mm/dd hh:mm"
                                   value="<fmt:formatDate pattern="yyyy/MM/dd HH:mm" value="${time}"/>"
                                   id="inputRegisterStartTime" class="form-control" data-toggle="datetimepicker"
                                   data-target="#inputRegisterStartTime"/>
                        </div>
                    </div>

                    <div class="col-sm-6">
                        <div class="input-group">
                            <span class="input-group-addon">报名截止</span>
                            <c:set target="${time}" property="time"
                                   value="${contest != null ? contest.registerEndTime : time.time}"/>
                            <input name="inputRegisterEndTime" type="text" placeholder="格式: yyyy/mm/dd hh:mm"
                                   value="<fmt:formatDate pattern="yyyy/MM/dd HH:mm" value="${time}"/>"
                                   id="inputRegisterEndTime" class="form-control" data-toggle="datetimepicker"
                                   data-target="#inputRegisterEndTime"/>
                        </div>
                    </div>
                </div>

                <br>
                <div class="form-row align-items-center">
                    <div class="col-sm-6">
                        <div class="input-group">
                            <span class="input-group-addon">比赛密码</span>
                            <input name="inputContestPassword" type="text" class="form-control" placeholder="不填为公开" value="${contest.password}">
                        </div>
                    </div>

                    <div class="col-sm-6">
                        <div class="input-group mb-2 mb-sm-0">
                            <div class="input-group-addon">比赛赛制</div>
                            <select class="form-control" name="inputContestType">
                                <c:choose>
                                    <c:when test="${contest.contestType == 'IO'}">
                                        <option selected value="OI">OI</option>
                                        <option value="ACM">ACM</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="OI">OI</option>
                                        <option selected value="ACM">ACM</option>
                                    </c:otherwise>
                                </c:choose>
                            </select>
                        </div>
                    </div>
                </div>


                <br>
                <div class="form-row align-items-center">
                    <div class="col-sm-6">
                        <div class="input-group">
                            <span class="input-group-addon">举办人&nbsp;&nbsp;&nbsp;&nbsp;</span>
                            <c:choose>
                                <c:when test="${not empty contest}">
                                    <input name="inputSponsor" type="text" class="form-control" placeholder="默认当前登录用户" readonly value="${contest.sponsor}">
                                </c:when>
                                <c:otherwise>
                                    <input name="inputSponsor" type="text" class="form-control" placeholder="默认当前登录用户" readonly value="${cookie.get('userName').value}">
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>


                <br>
                <h4>比赛描述</h4>
                <textarea name="inputContestDesc" id="inputContestDesc" class="ckeditor"
                          style="visibility: hidden; display: none;">${contest.desc}</textarea>

                <c:if test="${problemList != null}">
                    <br>
                    <a href="/contest-problem-edit?contestID=${contest.contestID}"><h4>题目列表</h4></a>
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
                                <td class="text-center">题目名</td>
                                <td class="text-center"><a href="/problem?problemID=${1000+problem.problemID}">${1000 + problem.problemID}</a></td>
                                <td class="text-center">${problem.accepted}/${problem.submitted}</td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    <br>
                </c:if>
                <br>
                <div class="text-center">
                    <c:choose>
                        <c:when test="${contest != null}">
                            <input class="btn btn-success" type="submit" value="保存修改">
                            <a href="#" class="btn btn-danger" data-toggle="modal" data-target="#deleteModal">删除比赛</a>
                        </c:when>
                        <c:otherwise>
                            <input class="btn btn-success" type="submit" value="保存比赛">
                        </c:otherwise>
                    </c:choose>

                </div>
            </form>
        </div>
    </div>

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
                    <p>是否删除比赛: <b>${contest.title}</b>, 以及相关数据</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">返回</button>
                    <a id="url" href="/contest-delete?contestID=${contest.contestID}" class="btn btn-danger">是的,删除</a>
                </div>
            </div>
        </div>
    </div>
    <%--
    <textarea id="editor1"></textarea>
    <button onclick="setContent()">Set Content</button>
    <button onclick="getContent()">Get Content</button>
    <script>
        CKEDITOR.replace('editor1'); // 这里的 'editor1' 等于 textarea 的 id 'editor1'



        // 设置 CKEditor 中的内容
        function setContent() {
            // editor1 是上面的 id
            CKEDITOR.instances.editor1.setData('<b>This is for test</b>');
        }
        // 获取 CKEditor 中的内容
        function getContent() {
            var content = CKEDITOR.instances.editor1.getData();
            alert(content);
        }
    </script>--%>

</div>
<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
</body>
</html>
