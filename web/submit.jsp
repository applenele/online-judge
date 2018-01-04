<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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

    <link rel="stylesheet" href="css/bootstrap/bootstrap.min.css">

    <script src="js/jquery-3.2.1.min.js"></script>
    <script src="js/bootstrap/popper.min.js"></script>
    <script src="js/bootstrap/bootstrap.min.js"></script>
</head>
<body>
<jsp:include page="navbar.jsp"/>

<div class="container" style="margin-top: 70px">
        <h3 class="text-center">A+B problem</h3>
    <div class="card">
        <h5 class="card-header">最近提交记录</h5>
            <table class="table table-sm table-striped">
                <thead>
                <tr>
                    <th>提交ID</th>
                    <th>题号</th>
                    <th>耗时(ms)</th>
                    <th>内存(KB)</th>
                    <th>语言</th>
                    <th>代码长度(字节)</th>
                    <th>提交时间</th>
                    <th class="text-center">结果</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach begin="0" end="2" step="1">
                    <tr>
                        <td>1</td>
                        <td>1000</td>
                        <td>876</td>
                        <td>3425</td>
                        <td>C++</td>
                        <td>3235</td>
                        <td>2017-12-23 12:12:12</td>
                        <td class="text-center">
                            <span class="badge badge-success">accept</span>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
    </div>

    <br>

    <div class="card">
        <h5 class="card-header">提交代码</h5>
        <div class="card-body">
            <%--<h4 class="card-title">Special title treatment</h4>--%>
            <form class="form-horizontal">
                <div class="form-group">
                    <label>源代码</label>
                    <textarea class="form-control" rows="15"></textarea>
                </div>

                <div class="form-row align-items-center">
                    <div class="col-sm-3">
                        <div class="input-group mb-2 mb-sm-0">
                            <div class="input-group-addon">语言: </div>
                            <select class="form-control" name="preferenceLanguage" id="preferenceLanguage">
                                    <option>C</option>
                                    <option>C++</option>
                                    <option>Java</option>
                                    <option>Python2</option>
                                    <option>Python3</option>
                            </select>
                        </div>
                    </div>
                    <div class="col-auto">
                        <button type="submit" class="btn btn-primary">提交代码</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<jsp:include page="footer.jsp"/>
</body>
</html>
