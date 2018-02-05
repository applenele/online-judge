<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fnt" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: xanarry
  Date: 17-12-28
  Time: 上午10:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>提交代码</title>

    <link rel="stylesheet" href="/css/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" href="/css/oj.css">

    <script src="/js/jquery-3.2.1.min.js"></script>
    <script src="/js/bootstrap/popper.min.js"></script>
    <script src="/js/bootstrap/bootstrap.min.js"></script>

    <link rel="stylesheet" href="/plugin/highlight/css/default.css">
    <script src="/plugin/highlight/js/highlight.pack.js"></script>

    <link rel="stylesheet" href="/plugin/codemirror/css/codemirror.css">
    <script src="/plugin/codemirror/js/codemirror.js"></script>
    <script src="/plugin/codemirror/mode/clike.js"></script>
    <script src="/plugin/codemirror/mode/python.js"></script>

    <link rel="stylesheet" href="/plugin/codemirror/css/codemirror.css">
</head>
<body>
<jsp:include page="/WEB-INF/jsp/navbar.jsp"/>

<%--
        request.setAttribute("problem", problemBean);
        request.setAttribute("languages", languages);
        request.setAttribute("user", userBean);
        request.setAttribute("submitRecords", submitRecordBeans);

--%>

<div class="container custom-container">
    <a href="/problem?problemID=${problem.problemID}"><h3 class="text-center">${problem.title}</h3></a>
    <div class="card">
        <h5 class="card-header">本题最近提交记录</h5>
        <c:choose>
            <c:when test="${recordList != null && fnt:length(recordList) > 0}">
                <table class="table table-striped text-center">
                    <thead>
                    <tr>
                        <th>提交ID</th>
                        <th>耗时(ms)</th>
                        <th>内存(KB)</th>
                        <th>语言</th>
                        <th>代码长度(字节)</th>
                        <th>提交时间</th>
                        <th>结果</th>
                    </tr>
                    </thead>
                    <tbody>
                    <jsp:useBean id="submitTime" class="java.util.Date"/>
                    <c:forEach items="${recordList}" var="record">
                        <tr>
                            <td>${record.submitID}</td>
                            <td>${record.timeConsume}</td>
                            <td>${record.memConsume}</td>
                            <td><span class="badge badge-secondary">${record.language}</span></td>
                            <td>${record.codeLength}</td>
                            <c:set target="${submitTime}" property="time" value="${record.submitTime}"/>
                            <td><fmt:formatDate pattern="yyyy/MM/dd HH:mm:ss"
                                                                  value="${submitTime}"/></td>
                            <td>
                                <c:choose>
                                    <c:when test="${record.result == 'Queuing'}"><span class="badge badge-secondary">${record.result}</span></c:when>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${record.result == 'Compiling'}"><span class="badge badge-secondary">${record.result}</span></c:when>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${record.result == 'Running'}"><span class="badge badge-primary">${record.result}</span></c:when>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${record.result == 'Accepted'}"><span class="badge badge-success">${record.result}</span></c:when>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${record.result == 'Presentation Error'}"><span class="badge badge-warning">${record.result}</span></c:when>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${record.result == 'Wrong Answer'}"><span class="badge badge-danger">${record.result}</span></c:when>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${record.result == 'Time Limit Exceeded'}"><span class="badge badge-warning">${record.result}</span></c:when>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${record.result == 'Memory Limit Exceeded'}"><span class="badge badge-warning">${record.result}</span></c:when>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${record.result == 'Output Limit Exceeded'}"><span class="badge badge-warning">${record.result}</span></c:when>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${record.result == 'Runtime Error'}"><span class="badge badge-danger">${record.result}</span></c:when>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${record.result == 'System Error'}"><span class="badge badge-dark">${record.result}</span></c:when>
                                </c:choose>
                                <c:choose>
                                    <c:when test="${record.result == 'Compilation Error'}"><span class="badge badge-warning">${record.result}</span></c:when>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <div class="modal-body">
                    <h4>此题目没有提交记录</h4>
                </div>
            </c:otherwise>
        </c:choose>

    </div>

    <br>

    <form action="/submit" method="post">
    <div class="card">
        <h5 class="card-header">提交代码</h5>
            <%--<h4 class="card-title">Special title treatment</h4>--%>
        <input hidden name="inputProblemID" value="${problem.problemID}">
        <textarea id="inputCode" name="inputCode"></textarea>
    </div>
    <br>
        <div class="form-row align-items-center">
            <div class="col-sm-3">
                <div class="input-group mb-2 mb-sm-0">
                    <div class="input-group-addon">语言: </div>
                    <select class="form-control" id="inputLanguage" name="inputLanguage" onchange="resetCodeMirror()">
                        <c:forEach items="${languages}" var="lang">
                            <option <c:if test="${lang.language == user.preferLanguage}"> selected </c:if>
                                    value="${lang.language}">${lang.language}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="col-auto">
                <button type="submit" class="btn btn-primary">提交代码</button>
            </div>
        </div>

    </form>

</div>

<script src="/plugin/codemirror/js/codemirror.js"></script>
<script src="/plugin/codemirror/mode/clike.js"></script>
<script src="/plugin/codemirror/mode/python.js"></script>
<script>
    var codeEditor = CodeMirror.fromTextArea(document.getElementById("inputCode"), {
        lineNumbers: true,
        matchBrackets: true,
        indentUnit: 4,
        mode: "text/x-c++src"
    });

    var mac = CodeMirror.keyMap.default == CodeMirror.keyMap.macDefault;
    CodeMirror.keyMap.default[(mac ? "Cmd" : "Ctrl") + "-Space"] = "autocomplete";

    function resetCodeMirror() {
        var lang = $('#inputLanguage option:selected').val();
        var lmode = "text/x-c++src";

        if (lang.toLowerCase() == 'c') {
            lmode = "text/x-csrc"
        } else if (lang.toLowerCase() == 'java') {
            lmode = "text/x-java";
        } else if (lang.toLowerCase() == 'python2') {
            lmode = {name: "python", version: 2, singleLineStringErrors: false}
        } else if (lang.toLowerCase() == 'python3') {
            lmode = {name: "python", version: 3, singleLineStringErrors: false}
        }
        codeEditor.setOption('mode', lmode);
    }
</script>


<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
</body>
</html>
