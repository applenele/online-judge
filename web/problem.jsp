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
    <title>Title</title>
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
    <p>27-Dec-2017 20:42:43.294 INFO [main] org.apache.catalina.startup.Catalina.load Initialization processed in 1206 ms
    27-Dec-2017 20:42:43.355 INFO [main] org.apache.catalina.core.StandardService.startInternal Starting service Catalina
    27-Dec-2017 20:42:43.355 INFO [main] org.apache.catalina.core.StandardEngine.startInternal Starting Servlet Engine: Apache Tomcat/8.5.9
    27-Dec-2017 20:42:43.371 INFO [main] org.apache.coyote.AbstractProtocol.start Starting ProtocolHandler [http-nio-8080]
    27-Dec-2017 20:42:43.381 INFO [main] org.apache.coyote.AbstractProtocol.start Starting ProtocolHandler [ajp-nio-8009]
    27-Dec-2017 20:42:43.382 INFO [main] org.apache.catalina.startup.Catalina.start Server startup in 88 ms</p>
</blockquote><br/>

<h3>输入</h3>
<blockquote class="card modal-body">
    <p>27-Dec-2017 20:42:43.294 INFO [main] org.apache.catalina.startup.Catalina.load Initialization processed in 1206 ms
    27-Dec-2017 20:42:43.355 INFO [main] org.apache.catalina.core.StandardService.startInternal Starting service Catalina
    27-Dec-2017 20:42:43.355 INFO [main] org.apache.catalina.core.StandardEngine.startInternal Starting Servlet Engine: Apache Tomcat/8.5.9
    27-Dec-2017 20:42:43.371 INFO [main] org.apache.coyote.AbstractProtocol.start Starting ProtocolHandler [http-nio-8080]
    27-Dec-2017 20:42:43.381 INFO [main] org.apache.coyote.AbstractProtocol.start Starting ProtocolHandler [ajp-nio-8009]
    27-Dec-2017 20:42:43.382 INFO [main] org.apache.catalina.startup.Catalina.start Server startup in 88 ms</p>
</blockquote><br/>

<h3>输出</h3>
<blockquote class="card modal-body"><p>27-Dec-2017 20:42:43.294 INFO [main] org.apache.catalina.startup.Catalina.load Initialization processed in 1206 ms
    27-Dec-2017 20:42:43.355 INFO [main] org.apache.catalina.core.StandardService.startInternal Starting service Catalina
    27-Dec-2017 20:42:43.355 INFO [main] org.apache.catalina.core.StandardEngine.startInternal Starting Servlet Engine: Apache Tomcat/8.5.9
    27-Dec-2017 20:42:43.371 INFO [main] org.apache.coyote.AbstractProtocol.start Starting ProtocolHandler [http-nio-8080]
    27-Dec-2017 20:42:43.381 INFO [main] org.apache.coyote.AbstractProtocol.start Starting ProtocolHandler [ajp-nio-8009]
    27-Dec-2017 20:42:43.382 INFO [main] org.apache.catalina.startup.Catalina.start Server startup in 88 ms</p>
</blockquote><br/>

<h3>输入样例</h3>
<blockquote class="card modal-body">
<pre>27-Dec-2017 20:42:43.294 INFO [main] org.apache.catalina.startup.Catalina.load Initialization processed in 1206 ms
27-Dec-2017 20:42:43.355 INFO [main] org.apache.catalina.core.StandardService.startInternal Starting service Catalina
27-Dec-2017 20:42:43.355 INFO [main] org.apache.catalina.core.StandardEngine.startInternal Starting Servlet Engine: Apache Tomcat/8.5.9
27-Dec-2017 20:42:43.371 INFO [main] org.apache.coyote.AbstractProtocol.start Starting ProtocolHandler [http-nio-8080]
27-Dec-2017 20:42:43.381 INFO [main] org.apache.coyote.AbstractProtocol.start Starting ProtocolHandler [ajp-nio-80
09]27-Dec-2017 20:42:43.382 INFO [main] org.apache.catalina.startup.Catalina.start Server startup in 88 ms</pre>
</blockquote><br/>

<h3>输出样例</h3>
<blockquote class="card modal-body"><pre>27-Dec-2017 20:42:43.294 INFO [main] org.apache.catalina.startup.Catalina.load Initialization processed in 1206 ms
27-Dec-2017 20:42:43.355 INFO [main] org.apache.catalina.core.StandardService.startInternal Starting service Catalina
27-Dec-2017 20:42:43.355 INFO [main] org.apache.catalina.core.StandardEngine.startInternal Starting Servlet Engine: Apache Tomcat/8.5.9
27-Dec-2017 20:42:43.371 INFO [main] org.apache.coyote.AbstractProtocol.start Starting ProtocolHandler [http-nio-8080]
27-Dec-2017 20:42:43.381 INFO [main] org.apache.coyote.AbstractProtocol.start Starting ProtocolHandler [ajp-nio-8009]
27-Dec-2017 20:42:43.382 INFO [main] org.apache.catalina.startup.Catalina.start Server startup in 88 ms</pre>
</blockquote><br/>

<h3>提示</h3>
<blockquote class="card modal-body">
<p>27-Dec-2017 20:42:43.294 INFO [main] org.apache.catalina.startup.Catalina.load Initialization processed in 1206 ms
    27-Dec-2017 20:42:43.355 INFO [main] org.apache.catalina.core.StandardService.startInternal Starting service Catalina
    27-Dec-2017 20:42:43.355 INFO [main] org.apache.catalina.core.StandardEngine.startInternal Starting Servlet Engine: Apache Tomcat/8.5.9
    27-Dec-2017 20:42:43.371 INFO [main] org.apache.coyote.AbstractProtocol.start Starting ProtocolHandler [http-nio-8080]
    27-Dec-2017 20:42:43.381 INFO [main] org.apache.coyote.AbstractProtocol.start Starting ProtocolHandler [ajp-nio-8009]
    27-Dec-2017 20:42:43.382 INFO [main] org.apache.catalina.startup.Catalina.start Server startup in 88 ms</p>
</blockquote><br/>

<h3>来源</h3>
<blockquote class="card modal-body">
<p>27-Dec-2017 20:42:43.294 INFO [main] org.apache.catalina.startup.Catalina.load Initialization processed in 1206 ms
            27-Dec-2017 20:42:43.355 INFO [main] org.apache.catalina.core.StandardService.startInternal Starting service Catalina
            27-Dec-2017 20:42:43.355 INFO [main] org.apache.catalina.core.StandardEngine.startInternal Starting Servlet Engine: Apache Tomcat/8.5.9
            27-Dec-2017 20:42:43.371 INFO [main] org.apache.coyote.AbstractProtocol.start Starting ProtocolHandler [http-nio-8080]
            27-Dec-2017 20:42:43.381 INFO [main] org.apache.coyote.AbstractProtocol.start Starting ProtocolHandler [ajp-nio-8009]
            27-Dec-2017 20:42:43.382 INFO [main] org.apache.catalina.startup.Catalina.start Server startup in 88 ms</p>
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
