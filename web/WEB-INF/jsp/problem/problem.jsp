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
<%@ taglib prefix="fnt" uri="http://java.sun.com/jsp/jstl/functions" %>


<html>
<head>
    <title>${problem.title}</title>
    <link rel="stylesheet" href="css/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" href="/css/oj.css">

    <script src="js/jquery-3.2.1.min.js"></script>
    <script src="js/bootstrap/popper.min.js"></script>
    <script src="js/bootstrap/bootstrap.min.js"></script>
</head>

<body>
<jsp:include page="/WEB-INF/jsp/navbar.jsp"/>

<div class="container custom-container">
    <h1 class="text-center">${problem.title}</h1>
    <table align="center">
        <tbody>
        <tr>
            <td><b>时间限制:</b>${problem.staticLangTimeLimit}ms </td>
            <td> </td>
            <td><b>内存限制:</b>${problem.staticLangMemLimit}KB</td>
        </tr>
        </tbody>
    </table>
    <div class="card-body">
        <div class="text-center">
            <div class="btn-group" role="group" aria-label="...">
                <a class="btn" href="/submit?problemID=${problem.problemID}">提交</a>
                <a class="btn" href="/discuss-list?type=0&porcID=${problem.problemID}">讨论</a></button>
                <a class="btn" href="/record-list?problemID=${problem.problemID}">状态</a></button>
                <a class="btn" href="/problem-edit?problemID=${problem.problemID}">编辑</a></button>
                <a class="btn" href="/test-point-list?problemID=${problem.problemID}">数据</a></button>
            </div>
        </div>
    </div>


    <h3>题目描述</h3>
    <blockquote class="card modal-body">
        ${problem.desc}
    </blockquote>

    <h3>输入</h3>
    <blockquote class="card modal-body">
        ${problem.inputDesc}
    </blockquote>

    <h3>输出</h3>
    <blockquote class="card modal-body">
        ${problem.outputDesc}
    </blockquote>


    <div class="row">
        <div class="col-sm-6">
            <h3>输入样例</h3>
            <blockquote class="card">
                <pre class="pre-scrollable" style="height: 180px">${problem.inputSample}</pre>
            </blockquote>
        </div>

        <div class="col-sm-6">
            <h3>输出样例</h3>
            <blockquote class="card">
                <pre class="pre-scrollable" style="height: 180px">${problem.outputSample}</pre>
            </blockquote>
        </div>
    </div>

    <h3>提示</h3>
    <blockquote class="card modal-body">
        ${problem.hint != null && fnt:length(problem.hint) > 0 ? problem.hint : '无'}
    </blockquote>

    <h3>来源</h3>
    <blockquote class="card modal-body">
        ${problem.source != null && fnt:length(problem.source) > 0 ? problem.source : '无'}
    </blockquote>
    <br/>

    <div class="text-center">
        <div class="btn-group" role="group" aria-label="...">
            <a class="btn" href="/submit?problemID=${problem.problemID}">提交</a>
            <a class="btn" href="/discuss-list?type=0&porcID=${problem.problemID}">讨论</a></button>
            <a class="btn" href="/record-list?problemID=${problem.problemID}">状态</a></button>
            <a class="btn" href="/problem-edit?problemID=${problem.problemID}">编辑</a></button>
            <a class="btn" href="/test-point-list?problemID=${problem.problemID}">数据</a></button>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
</body>
</html>
