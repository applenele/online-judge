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
        <h1 class="text-center">${problem.title}</h1>

        <ul class="list-inline text-center">
            <li class="list-inline-item">
                <ul class="list-inline text-center">
                    <li class="list-inline-item">提交次数:</li>
                    <li class="list-inline-item">${problem.submitted}</li>
                </ul>
            </li>
            <li class="list-inline-item">&nbsp;&nbsp;</li>
            <li class="list-inline-item">
                <ul class="list-inline text-center">
                    <li class="list-inline-item">通过次数:</li>
                    <li class="list-inline-item">${problem.accepted}</li>
                </ul>
            </li>
        </ul>
        <div class="text-center">
            <div class="btn-group" role="group" aria-label="...">
                <button type="button" class="btn btn-light"><a
                        href="/submit?problemID=${problem.problemID}">提交</a></button>
                <button type="button" class="btn btn-light"><a href="/status?problemID=${problem.problemID}">状态</a>
                </button>
                <button type="button" class="btn btn-light"><a
                        href="/edit-problem?problemID=${problem.problemID}">编辑</a></button>
                <button type="button" class="btn btn-light"><a
                        href="/test-point-list?problemID=${problem.problemID}">查看数据</a>
                </button>
            </div>
        </div>
    </div>


    <h3>题目描述</h3>
    <blockquote class="card modal-body">
        ${problem.desc}
    </blockquote>
    <br/>

    <h3>输入</h3>
    <blockquote class="card modal-body">
        ${problem.inputDesc}
    </blockquote>
    <br/>

    <h3>输出</h3>
    <blockquote class="card modal-body">
        ${problem.outputDesc}
    </blockquote>
    <br/>


    <div class="row">
        <div class="col-sm-6">
            <h3>输入样例</h3>
            <blockquote class="card">
                <pre class="pre-scrollable" style="height: 250px">${problem.inputSample}</pre>
            </blockquote>
        </div>

        <div class="col-sm-6">
            <h3>输出样例</h3>
            <blockquote class="card">
                <pre class="pre-scrollable" style="height: 250px">${problem.outputSample}</pre>
            </blockquote>
        </div>
    </div>


    <h3>提示</h3>
    <blockquote class="card modal-body">
        ${problem.hint}
    </blockquote>
    <br/>

    <h3>来源</h3>
    <blockquote class="card modal-body">
        ${problem.source}
    </blockquote>
    <br/>

    <div class="text-center">
        <div class="btn-group" role="group" aria-label="...">
            <button type="button" class="btn btn-light"><a
                    href="/submit?problemID=${problem.problemID}">提交</a></button>
            <button type="button" class="btn btn-light"><a href="/status?problemID=${problem.problemID}">状态</a>
            </button>
            <button type="button" class="btn btn-light"><a
                    href="/edit-problem?problemID=${problem.problemID}">编辑</a></button>
            <button type="button" class="btn btn-light"><a
                    href="/test-point-list?problemID=${problem.problemID}">查看数据</a>
            </button>
        </div>
    </div>

</div>
<jsp:include page="footer.jsp"/>
</body>
</html>
