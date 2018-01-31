<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: xanarry
  Date: 17-12-27
  Time: 下午9:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<br>
<footer class="footer">
    <div class="container text-center" style="margin-top: 70px">
        <ul class="list-inline">
            <li class="list-inline-item"><a href="/judges">评测机</a></li>
            <li class="list-inline-item"><a href="/WEB-INF/jsp/contact-me.jsp">联系我</a></li>
            <li class="list-inline-item"><a href="/about">关于</a></li>
            <li class="list-inline-item"><a href="/faq">FAQ</a></li>
        </ul>
        <jsp:useBean id="now" class="java.util.Date"/>
        <span class="text-muted">Copyright©<fmt:formatDate value="${now}" pattern="yyyy"/> Xanarry. All rights reserved.</span>
        <p></p>
    </div>
</footer>
