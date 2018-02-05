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

    <link rel="stylesheet" href="/css/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" href="/css/oj.css">

    <script src="/js/jquery-3.2.1.min.js"></script>
    <script src="/js/bootstrap/popper.min.js"></script>
    <script src="/js/bootstrap/bootstrap.min.js"></script>

    <script>
        function warnningDelete(problemID, testPointID) {
            var url = "/delete-test-point?problemID=" + problemID + "&testPointID=" + testPointID;
            $('#deleteURL').attr('href', url);
        }
    </script>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/navbar.jsp"/>

<div class="container custom-container">
    <h3 class="text-center"><a href="/problem?problemID=${problem.problemID}">${problem.title}</a></h3>
    <br>
    <div class="card">
        <h5 class="card-header">所有测试点</h5>
        <c:choose>
            <c:when test="${testPointList != null && fn:length(testPointList) > 0}">
                <table class="table table-sm table-striped" style="margin-bottom: 0rem;">
                    <thead>
                    <tr>
                        <th class="text-center">ID</th>
                        <th class="text-center">长度</th>
                        <th class="text-center">查看</th>
                        <th class="text-center">删除</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${testPointList}" var="testPointID">
                        <tr>
                            <td class="text-center">#${testPointID.testPointID}</td>
                            <td class="text-center">${testPointID.inputTextLength}</td>
                            <td class="text-center"><a
                                    href="/show-test-point?problemID=${problem.problemID}&testPointID=${testPointID.testPointID}">想看看我</a>
                            </td>
                            <td class="text-center"><a href="#"
                                                       onclick="warnningDelete(${problem.problemID}, ${testPointID.testPointID})"
                                                       data-toggle="modal" data-target="#deleteModal">删除</a>
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
                        <label>输出数据</label>
                        <textarea class="form-control pre-scrollable" style="font-family: Consolas"
                                  name="inputOutputData" rows="7"></textarea>
                    </div>
                </div>
                <input name="inputProblemID" value="${problem.problemID}" hidden>
                <br>
                <div class="text-center">
                    <a href="/problem-edit?problemID=${problem.problemID}" class="btn btn-primary">编辑题目</a>
                    <button type="submit" class="btn btn-success">添加数据</button>
                </div>
            </form>
        </div>
    </div>
</div>


<div class="modal fade" id="deleteModal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">重要提示</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <p>测试点数据将永久删除, 是否继续?</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">返回</button>
                <a id="deleteURL" href="#" class="btn btn-danger">是的,删除</a>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
</body>
</html>


