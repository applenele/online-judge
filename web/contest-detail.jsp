<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: xanarry
  Date: 18-1-18
  Time: 下午7:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${contest.title}</title>

    <link rel="stylesheet" href="css/bootstrap/bootstrap.min.css">

    <script src="js/jquery-3.2.1.min.js"></script>
    <script src="js/bootstrap/popper.min.js"></script>
    <script src="js/bootstrap/bootstrap.min.js"></script>
</head>
<body>
<jsp:include page="navbar.jsp"/>
<div class="container" style="margin-top: 70px">
    <div class="card">
        <div class="card-header">${contest.title}</div>
        <div class="progress">
            <div class="progress-bar" role="progressbar" style="width: 25%;" aria-valuenow="25" aria-valuemin="0" aria-valuemax="100">25%</div>
        </div>
        <div class="card-body">
            <div class="text-center">
                <div class="btn-group btn-group-lg" role="group" aria-label="Large button group">
                    <ul class="pagination">
                        <c:forEach items="${problemList}" var="contestProblem">
                            <li class="page-item <c:if test="${problem.problemID == contestProblem.problemID}">disabled</c:if>"><a class="page-link" href="/contest-detail?contestID=${contest.contestID}&curProblem=${contestProblem.innerID}" tabindex="-1">${contestProblem.innerID}</a></li>
                        </c:forEach>
                    </ul>
                </div>

                <h1 class="text-center">title${problem.title}</h1>

                <ul class="list-inline text-center">
                    <li class="list-inline-item">
                        <ul class="list-inline text-center">
                            <li class="list-inline-item">通过人数:</li>
                            <li class="list-inline-item">${problem.submitted}</li>
                        </ul>
                    </li>
                    <li class="list-inline-item">&nbsp;&nbsp;</li>
                    <li class="list-inline-item">
                        <ul class="list-inline text-center">
                            <li class="list-inline-item">提交次数:</li>
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
            <br>

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
            <br>

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
    </div>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>