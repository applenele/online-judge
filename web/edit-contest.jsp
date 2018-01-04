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

    <script src="js/jquery-3.2.1.min.js"></script>
    <script src="js/bootstrap/popper.min.js"></script>
    <script src="js/bootstrap/bootstrap.min.js"></script>

    <script src="plugin/ckeditor/ckeditor.js"></script>
</head>
<body>
<jsp:include page="navbar.jsp"/>

<div class="container" style="margin-top: 70px">

    <div class="card">
        <div class="card-header"><h4>添加比赛</h4></div>
        <div class="card-body">
            <form method="post">
                <div class="input-group">
                    <span class="input-group-addon">比赛名称</span>
                    <input name="title" type="text" value="" placeholder="标题不超过200个字符" class="form-control">
                </div>
                <br>

                <div class="form-row align-items-center">
                    <div class="col-sm-6">
                        <div class="input-group">
                            <span class="input-group-addon">开始时间</span>
                            <input name="timeLimit" type="text" value="1000" class="form-control">
                        </div>
                    </div>

                    <div class="col-sm-6">
                        <div class="input-group">
                            <span class="input-group-addon">结束时间</span>
                            <input name="memoryLimit" type="text" value="65535" class="form-control">
                        </div>
                    </div>
                </div>

                <br>
                <div class="form-row align-items-center">
                    <div class="col-sm-6">
                        <div class="input-group">
                            <span class="input-group-addon">报名开始</span>
                            <input name="timeLimit" type="text" value="2000" class="form-control">
                        </div>
                    </div>

                    <div class="col-sm-6">
                        <div class="input-group">
                            <span class="input-group-addon">报名截止</span>
                            <input name="memoryLimit" type="text" value="131072" class="form-control">
                        </div>
                    </div>
                </div>

                <br>
                <div class="form-row align-items-center">
                    <div class="col-sm-6">
                        <div class="input-group">
                            <span class="input-group-addon">比赛密码</span>
                            <input name="timeLimit" type="text" value="2000" class="form-control">
                        </div>
                    </div>

                    <div class="col-sm-6">
                        <div class="input-group mb-2 mb-sm-0">
                            <div class="input-group-addon">比赛赛制</div>
                            <select class="form-control" name="preferenceLanguage" id="preferenceLanguage">
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
                            <input name="memoryLimit" type="text" value="131072" class="form-control">
                        </div>
                    </div>
                </div>


                <br>

                <h3>比赛描述</h3>
                <textarea name="description" class="ckeditor" style="visibility: hidden; display: none;"></textarea>

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
                <div class="form-row align-items-center">
                    <div class="col-sm-5">
                        <div class="input-group">
                            <span class="input-group-addon">比赛题号</span>
                            <input name="timeLimit" type="text" value="1000" class="form-control">
                        </div>
                    </div>

                    <div class="col-sm-5">
                        <div class="input-group">
                            <span class="input-group-addon">题库题号</span>
                            <input name="memoryLimit" type="text" value="65535" class="form-control">
                        </div>
                    </div>

                    <div class="col-sm-2">
                        <div class="input-group">
                            <button class="btn btn-secondary">加入题目列表</button>
                        </div>
                    </div>
                </div>
                <br>
                <hr>
                <div class="text-center">
                    <input class="btn btn-success" type="submit" value="添加题目">
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
