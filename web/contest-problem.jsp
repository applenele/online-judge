<%--
  Created by IntelliJ IDEA.
  User: xanarry
  Date: 18-1-17
  Time: 下午10:13
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>比赛题目</title>

    <link rel="stylesheet" href="css/bootstrap/bootstrap.min.css">

    <script src="js/jquery-3.2.1.min.js"></script>
    <script src="js/bootstrap/popper.min.js"></script>
    <script src="js/bootstrap/bootstrap.min.js"></script>
</head>
<body>
<jsp:include page="navbar.jsp"/>

<div class="container" style="margin-top: 70px">
    <h1 class="text-center">${contest.title}</h1>
    <div class="card">
        <div class="card-header">题目列表</div>
        <div class="card-body">
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

            <form method="post">
                <div class="form-row align-items-center">
                    <div class="col-sm-5">
                        <div class="input-group">
                            <span class="input-group-addon">比赛题号</span>
                            <input name="inputInnerID" type="text" class="form-control">
                        </div>
                    </div>

                    <div class="col-sm-5">
                        <div class="input-group">
                            <span class="input-group-addon">题库题号</span>
                            <input name="inputProblemID" type="text" class="form-control">
                        </div>
                    </div>

                    <div class="col-sm-2">
                        <div class="input-group">
                            <button class="btn btn-success" id="addProblemBtn">加入题目列表</button>
                        </div>
                    </div>
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
