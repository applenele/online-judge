<%--
  Created by IntelliJ IDEA.
  User: xanarry
  Date: 17-12-28
  Time: 上午10:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>404</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="renderer" content="webkit">
    <meta http-equiv="Cache-Control" content="no-siteapp"/>
    <link rel="stylesheet" href="css/bootstrap/bootstrap.min.css">

    <style>
        .error-template {
            padding: 40px 15px;
            text-align: center;
        }

        .error-actions {
            margin-top: 15px;
            margin-bottom: 15px;
        }

        .error-actions .btn {
            margin-right: 10px;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="text-center">
        <div class="error-template">
            <h1>Oops!</h1>
            <h2>页面不存在</h2>
            <div class="error-details">
                非常抱歉, 无法提供您请求的页面<br>
            </div>
            <div class="error-actions">
                <a href="/" class="btn btn-primary"><i class="icon-home icon-white"></i> 点此回首页 </a>
            </div>
        </div>
    </div>
</div>
</body>
</html>
