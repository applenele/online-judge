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
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


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
            <div class="btn-group" role="group">
                <c:choose>
                    <c:when test="${not empty cookie.get('userType')}">
                        <a class="btn" href="/submit?problemID=${problem.problemID}">提交</a>
                        <a class="btn" href="/discuss-list?type=0&porcID=${problem.problemID}">讨论</a></button>
                    </c:when>
                    <c:otherwise>
                        <span class="btn">提交</span>
                        <span class="btn">讨论</span>
                    </c:otherwise>
                </c:choose>
                <a class="btn" href="/record-list?problemID=${problem.problemID + 1000}">状态</a></button>
                <c:if test="${not empty cookie.get('userType') and cookie.get('userType').value > 0}">
                    <a class="btn" href="/problem-edit?problemID=${problem.problemID}">编辑</a></button>
                    <a class="btn" href="/test-point-list?problemID=${problem.problemID}">数据</a></button>
                </c:if>
            </div>
        </div>
    </div>


    <h4>题目描述</h4>
    <blockquote class="card modal-body">
        ${problem.desc}
    </blockquote>

    <h4>输入</h4>
    <blockquote class="card modal-body">
        ${problem.inputDesc}
    </blockquote>

    <h4>输出</h4>
    <blockquote class="card modal-body">
        ${problem.outputDesc}
    </blockquote>


    <div class="row">
        <div class="col-sm-6">
            <h4>输入样例</h4>
            <blockquote class="card">
                <pre class="pre-scrollable" style="height: 180px">${problem.inputSample}</pre>
            </blockquote>
        </div>

        <div class="col-sm-6">
            <h4>输出样例</h4>
            <blockquote class="card">
                <pre class="pre-scrollable" style="height: 180px">${problem.outputSample}</pre>
            </blockquote>
        </div>
    </div>

    <c:if test="${problem.hint != null && fn:length(problem.hint) > 0}">
        <h4>提示</h4>
        <blockquote class="card modal-body">${problem.hint}</blockquote>
    </c:if>

    <c:if test="${problem.hint != null && fn:length(problem.source) > 0}">
        <h4>来源</h4>
        <blockquote class="card modal-body">${problem.source}</blockquote>
    </c:if>

    <c:if test="${problem.hint != null && fn:length(problem.background) > 0}">
        <h4>背景</h4>
        <blockquote class="card modal-body">${problem.background}</blockquote>
    </c:if>

    <div class="text-center">
        <div class="btn-group" role="group">
            <c:choose>
                <c:when test="${not empty cookie.get('userType')}">
                    <a class="btn" href="/submit?problemID=${problem.problemID}">提交</a>
                    <a class="btn" href="/discuss-list?type=0&porcID=${problem.problemID}">讨论</a></button>
                </c:when>
                <c:otherwise>
                    <span class="btn">提交</span>
                    <span class="btn">讨论</span>
                </c:otherwise>
            </c:choose>
            <a class="btn" href="/record-list?problemID=${problem.problemID + 1000}">状态</a></button>
            <c:if test="${not empty cookie.get('userType') and cookie.get('userType').value > 0}">
                <a class="btn" href="/problem-edit?problemID=${problem.problemID}">编辑</a></button>
                <a class="btn" href="/test-point-list?problemID=${problem.problemID}">数据</a></button>
            </c:if>
        </div>
    </div>

</div>
<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
</body>
</html>
