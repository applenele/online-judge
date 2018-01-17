<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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

    <link rel="stylesheet" href="css/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" href="plugin/datetimepicker/css/tempusdominus-bootstrap-4.min.css" />
    <link rel="stylesheet" href="css/font-awesome.css">

    <script src="js/jquery-3.2.1.min.js"></script>
    <script src="js/bootstrap/popper.min.js"></script>
    <script src="js/bootstrap/bootstrap.min.js"></script>

    <script src="plugin/ckeditor/ckeditor.js"></script>
    <script src="plugin/datetimepicker/js/moment-with-locales.min.js"></script>
    <script src="plugin/datetimepicker/js/tempusdominus-bootstrap-4.min.js"></script>

    <script>
        function checkForm() {
            $('textarea.ckeditor').each(function () {
                var $textarea = $(this);
                $textarea.val(CKEDITOR.instances[$textarea.attr('id')].getData());
            });

            var title = $("#inputTitle");
            var strStartTime = $("#inputStartTime").val();
            var strEndTime = $("#inputEndTime").val();
            var strRegisterStartTime = $("#inputRegisterStartTime").val();
            var strRegisterEndTime = $("#inputRegisterEndTime").val();




            var startTime = toTimestamp(strStartTime);
            var endTime = toTimestamp(strEndTime);

            if (startTime >= endTime) {
                alert("比赛开始时间不能在结束时间之后");
                return false;
            }

            var registerStartTime = toTimestamp(strRegisterStartTime);
            var registerEndTime = toTimestamp(strRegisterEndTime);

            if (registerStartTime >= registerEndTime) {
                alert("报名开始时间不能在结束时间之后");
                return false;
            } else if (registerEndTime >= endTime) {
                alert("报名结束时间不能在比赛结束时间之后");
                return false;
            }
            return true;
        }

    </script>
</head>
<body>
<jsp:include page="navbar.jsp"/>

<div class="container" style="margin-top: 70px">

    <div class="card">
        <div class="card-header"><h4>添加比赛</h4></div>
        <div class="card-body">
            <form method="post" action="/add-contest" <%--onsubmit="return checkForm()"--%>>
                <div class="input-group">
                    <span class="input-group-addon">比赛名称</span>
                    <input name="inputTitle" type="text" value="" placeholder="标题不超过200个字符" class="form-control">
                </div>
                <br>

                <div class="form-row align-items-center">
                    <div class="col-sm-6">
                        <div class="input-group">
                            <span class="input-group-addon">开始时间</span>
                            <input name="inputStartTime" type="text" id="inputStartTime" class="form-control" data-toggle="datetimepicker" data-target="#inputStartTime">
                            <script type="text/javascript">
                                $(function () {
                                    $('#inputStartTime').datetimepicker({
                                        locale: 'zh-cn',
                                        format: "YYYY/MM/DD HH:mm"
                                    });
                                });
                            </script>
                        </div>
                    </div>

                    <div class="col-sm-6">
                        <div class="input-group">
                            <span class="input-group-addon">结束时间</span>
                            <input name="inputEndTime" type="text" id="inputEndTime" class="form-control" data-toggle="datetimepicker" data-target="#inputEndTime">
                            <script type="text/javascript">
                                $(function () {
                                    $('#inputEndTime').datetimepicker({
                                        locale: 'zh-cn',
                                        format: "YYYY/MM/DD HH:mm"
                                    });
                                });
                            </script>
                        </div>
                    </div>
                </div>

                <br>
                <div class="form-row align-items-center">
                    <div class="col-sm-6">
                        <div class="input-group">
                            <span class="input-group-addon">报名开始</span>
                            <input name="inputRegisterStartTime" type="text" id="inputRegisterStartTime" class="form-control" data-toggle="datetimepicker" data-target="#inputRegisterStartTime"/>
                            <script type="text/javascript">
                                $(function () {
                                    $('#inputRegisterStartTime').datetimepicker({
                                        locale: 'zh-cn',
                                        format: "YYYY/MM/DD HH:mm"
                                    });
                                });
                            </script>
                        </div>
                    </div>

                    <div class="col-sm-6">
                        <div class="input-group">
                            <span class="input-group-addon">报名截止</span>
                            <input name="inputRegisterEndTime" type="text" id="inputRegisterEndTime" class="form-control" data-toggle="datetimepicker" data-target="#inputRegisterEndTime"/>
                            <script type="text/javascript">
                                $(function () {
                                    $('#inputRegisterEndTime').datetimepicker({
                                        locale: 'zh-cn',
                                        format: "YYYY/MM/DD HH:mm"
                                    });
                                });
                            </script>
                        </div>
                    </div>
                </div>

                <br>
                <div class="form-row align-items-center">
                    <div class="col-sm-6">
                        <div class="input-group">
                            <span class="input-group-addon">比赛密码</span>
                            <input name="inputContestPassword" type="password" class="form-control" placeholder="不填为公开">
                        </div>
                    </div>

                    <div class="col-sm-6">
                        <div class="input-group mb-2 mb-sm-0">
                            <div class="input-group-addon">比赛赛制</div>
                            <select class="form-control" name="inputContestType">
                                <option value="OI">OI</option>
                                <option selected value="ACM">ACM</option>
                            </select>
                        </div>
                    </div>
                </div>

                <br>
                <div class="form-row align-items-center">
                    <div class="col-sm-6">
                        <div class="input-group">
                            <span class="input-group-addon">举办人&nbsp;&nbsp;&nbsp;&nbsp;</span>
                            <input name="inputSponsor" type="text" class="form-control" placeholder="默认当前登录用户">
                        </div>
                    </div>
                </div>

                <br>

                <h3>比赛描述</h3>
                <textarea name="inputContestDesc" class="ckeditor"
                          style="visibility: hidden; display: none;"></textarea>

                <br>
                <h3>题目列表</h3>
                <div class="card">
                    <table class="table table-sm table-striped">
                        <thead>
                        <tr>
                            <th>比赛题号</th>
                            <th>题库题号</th>
                            <th>题目名称</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach begin="0" end="2" step="1">
                            <tr>
                                <td>A</td>
                                <td>1000</td>
                                <td>失败的</td>
                                <td><a href="#">删除</a></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>

                <br>
                <div class="text-center">
                    <input class="btn btn-success" type="submit" value="保存比赛">
                </div>
            </form>
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
<jsp:include page="footer.jsp"/>
</body>
</html>
