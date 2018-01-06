<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--
  Created by IntelliJ IDEA.
  User: xanarry
  Date: 18-1-6
  Time: 下午1:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>测试点列表</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">


    <link rel="stylesheet" href="css/bootstrap/bootstrap.min.css">

    <script src="js/jquery-3.2.1.min.js"></script>
    <script src="js/bootstrap/popper.min.js"></script>
    <script src="js/bootstrap/bootstrap.min.js"></script>
</head>
<body>
<jsp:include page="navbar.jsp"/>

<div class="container" style="margin-top: 70px">
    <h3 class="text-center"><a href="">${problem.title}</a></h3>
    <br>
    <div class="card">
        <h5 class="card-header">所有测试点</h5>
        <c:choose>
            <c:when test="${testPointList != null && fn:length(testPointList) > 0}">
                <table class="table table-sm table-striped">
                    <thead>
                    <tr>
                        <th class="text-center">ID</th>
                        <th class="text-center">长度</th>
                        <th class="text-center">查看</th>
                        <th class="text-center">删除</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${testPointList}" var="testPoint">
                        <tr>
                            <td class="text-center">${testPoint.testPointID}</td>
                            <td class="text-center">${testPoint.inputTextLength}</td>
                            <td class="text-center"><a
                                    href="/show-test-point?problemID=${problem.problemID}&testPointID=${testPoint.testPointID}">想看看我</a>
                            </td>
                            <td class="text-center"><a
                                    href="/delete-test-point?problemID=${problem.problemID}&testPointID=${testPoint.testPointID}">删除</a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <div class="modal-body">
                    <h4 class="text-center">此题目尚未添加测试点</h4>
                </div>
            </c:otherwise>
        </c:choose>

    </div>

    <br>


    <div class="card">
        <h5 class="card-header">添加数据</h5>
        <div class="card-body">
            <%--<h4 class="card-title">Special title treatment</h4>--%>
            <form class="form-group" action="/add-test-point" method="post">
                <div class="row">
                    <div class="col-sm-6">
                        <label>输入数据</label>
                        <textarea class="form-control pre-scrollable" style="font-family: Consolas"
                                  name="inputInputData" rows="7"></textarea>
                    </div>

                    <div class="col-sm-6">
                        <label>输入数据</label>
                        <textarea class="form-control pre-scrollable" style="font-family: Consolas"
                                  name="inputOutputData" rows="7"></textarea>
                    </div>
                </div>
                <input name="inputProblemID" value="${problem.problemID}" hidden>
                <br>
                <div class="text-center">
                    <button type="submit" class="btn btn-primary">添加数据</button>
                </div>
            </form>
        </div>
    </div>
</div>

<jsp:include page="footer.jsp"/>
</body>
</html>


