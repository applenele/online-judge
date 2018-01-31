<%--
  Created by IntelliJ IDEA.
  User: xanarry
  Date: 18-1-30
  Time: 下午1:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${msg.title}</title>
    <link rel="stylesheet" href="/css/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" href="/css/oj.css">

    <script src="/js/jquery-3.2.1.min.js"></script>
    <script src="/js/bootstrap/popper.min.js"></script>
    <script src="/js/bootstrap/bootstrap.min.js"></script>
</head>

<body>
<jsp:include page="../navbar.jsp"/>
<div class="container custom-container">
    <div class="card">
        <div class="card-header">配置信息</div>
        <div class="card-body">
            <div class="row">
                <div class="col-sm-2"></div>
                <div class="col-sm-8">
                    <table class="table">
                        <tbody>
                        <tr>
                            <th class="text-right">Judge Server:</th>
                            <th class="text-left">${configuration.serverAddress}:${configuration.serverPort}</th>
                        </tr>
                        <tr>
                            <th class="text-right">timeout:</th>
                            <th class="text-left">${configuration.timeout}</th>
                        </tr>
                        <tr>
                            <th class="text-right">运行目录:</th>
                            <th class="text-left">${configuration.runningBaseDir}</th>
                        </tr>
                        <tr>
                            <th class="text-right">测试数据:</th>
                            <th class="text-left" style="vertical-align: middle">${configuration.testPointBaseDir}</th>
                        </tr>
                        <tr>
                            <th class="text-right">每页记录数:</th>
                            <th class="text-left" style="vertical-align: middle">${configuration.countPerPage}</th>
                        </tr>
                        </tbody>
                    </table>
                    <div class="text-center">
                        <label class="text-muted">变更请修改配置文件:config.json, 然后重启web服务器</label>
                    </div>
                    <div class="col-sm-2"></div>
                </div>
            </div>
        </div>
    </div>
    <br>
</div>

<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
</body>
</html>
