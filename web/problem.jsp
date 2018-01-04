<%--
  Created by IntelliJ IDEA.
  User: xanarry
  Date: 17-12-27
  Time: 下午10:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<html>
<head>
    <title>${problem.title}</title>
    <link rel="stylesheet" href="css/bootstrap/bootstrap.min.css">

    <script src="js/jquery-3.2.1.min.js"></script>
    <script src="js/bootstrap/popper.min.js"></script>
    <script src="js/bootstrap/bootstrap.min.js"></script>
</head>

<body>
<jsp:include page="navbar.jsp"/>


<div class="container" style="margin-top:70px">

    <div class="card-body">
        <h1 class="text-center">题目标题</h1>

        <ul class="list-inline text-center">
            <li class="list-inline-item">
                <ul class="list-inline text-center">
                    <li class="list-inline-item">提交次数:</li>
                    <li class="list-inline-item">458</li>
                </ul>
            </li>
            <li class="list-inline-item">&nbsp;&nbsp;</li>
            <li class="list-inline-item">
                <ul class="list-inline text-center">
                    <li class="list-inline-item">通过次数:</li>
                    <li class="list-inline-item">458</li>
                </ul>
            </li>
        </ul>
        <div class="text-center">
            <div class="btn-group" role="group" aria-label="...">
                <button type="button" class="btn btn-light" onclick="javascript:window.location.href='/webtest/sumbit?pid=352'">提交</button>
                <button type="button" class="btn btn-light">状态</button>
                <button type="button" class="btn btn-light">编辑</button>
                <button type="button" class="btn btn-light">查看数据</button>
            </div>
        </div>
    </div>


<h3>题目描述</h3>
<blockquote class="card modal-body">
    ${problem.desc}
</blockquote><br/>

<h3>输入</h3>
<blockquote class="card modal-body">
    ${problem.inputDesc}
</blockquote><br/>

<h3>输出</h3>
    <blockquote class="card modal-body">
        ${problem.outputDesc}
</blockquote><br/>

<h3>输入样例</h3>
<blockquote class="card modal-body">
    <pre>${problem.inputSample}</pre>
</blockquote><br/>

<h3>输出样例</h3>
    <blockquote class="card modal-body">
        <pre>${problem.outputSample}</pre>
</blockquote><br/>

<h3>提示</h3>
<blockquote class="card modal-body">
    ${problem.hint}
</blockquote><br/>

<h3>来源</h3>
<blockquote class="card modal-body">
    ${problem.source}
</blockquote><br/>

    <div class="text-center">
        <div class="btn-group" role="group" aria-label="...">
            <button type="button" class="btn btn-light" onclick="javascript:window.location.href='/webtest/sumbit?pid=352'">提交</button>
            <button type="button" class="btn btn-light">状态</button>
            <button type="button" class="btn btn-light">编辑</button>
            <button type="button" class="btn btn-light">查看数据</button>
        </div>
    </div>

</div>
<jsp:include page="footer.jsp"/>
</body>
</html>
