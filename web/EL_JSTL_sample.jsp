<%@ page import="java.util.ArrayList" %><%--
  Created by IntelliJ IDEA.
  User: xanarry
  Date: 17-3-29
  Time: 下午10:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>EL</title>
</head>
<body>

<%
    ArrayList<String> ary = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
        ary.add("ary:" + i);
    }
    request.setAttribute("ary", ary);
%>
<%--input value--%>
<c:set var="a" value="${1000}" scope="page"/>
<c:set var="b" value="${100}" scope="page"/>
<c:set var="str" value="${'hello'}" scope="page"/>
<c:set var="result" value="0" scope="page"/>

<c:out value="${a + b}" default="0" escapeXml="true"/>
<br>
<c:out value="${str > 'world'}" default="xxx" escapeXml="true"/>

<%--output value--%>
<c:out value="hello word" default="null" escapeXml="true"/>
<c:out value="${1 + 99}" default="0" escapeXml="true"/>


<%--calculate new value--%>
<c:set var="apb" value="${a + b}" scope="page"/>


<%--condition--%>
<%--数值比较--%>
<c:if var="cresult" test="${a > b}" scope="page">
    <p>${a}>${b} ${cresult}</p>
</c:if>
<%--字符串比较--%>
<c:if var="cresult" test="${str == 'hello'}">
    <p>${str}=='hello' ${cresult}</p>
</c:if>

<%--choose when otherwise--%>
<%--找出最大值--%>
<c:choose>
    <c:when test="${a > b}">
        <p>max=${a}</p>
    </c:when>

    <c:otherwise>
        <p>max=${b}</p>
    </c:otherwise>
</c:choose>

<hr>
<%--给成绩分出等级--%>
<c:set var="score" value="${70}" scope="page"/>
<c:choose>
    <c:when test="${score >= 60 && score < 80}"><p>及格</p></c:when>
    <c:when test="${score >= 80}"><p>非常优秀</p></c:when>
    <c:otherwise><p>垃圾成绩</p></c:otherwise>
</c:choose>

<hr>
<%--loop--%>
<%--foreach--%>

<%--给定起点,终点,步长--%>
<c:forEach var="i" begin="0" end="10" step="2">
    ${i}
</c:forEach>
<br>
<%--默认步长为1--%>
<c:forEach var="i" begin="0" end="10">
    ${ary[i]}
</c:forEach>
<br>

<%--以下循环中, begin与end仅仅针对标签中设置的变量, 如果直接给定的迭代对象, begin与end将是空值--%>
<c:forEach items="${ary}" var="list" begin="0" end="10" step="1" varStatus="pos">
    <p>begin:${pos.begin} end:${pos.end} count:${pos.count} index:${pos.index} current:${pos.current} first:${pos.first}
        last:${pos.last} ====> value:${list}</p>
</c:forEach>

<hr>
<%--迭代字符串分割集合--%>
<c:forTokens items="ary:0 ary:1 ary:2 ary:3 ary:4 ary:5 ary:6 ary:7 ary:8 ary:9 " delims=":" var="word" varStatus="pos">
    <p>${word}===> count:${pos.current} index:${pos.index}</p>
</c:forTokens>

<%--JSTL的fn字符串处理函数--%>
<c:set var="id" value="ID:500234199411185817" scope="page"/>
<p>contains:${fn:contains(id, "ID")}</p>
<p>birth:${fn:substring(id, 9, 17)}</p>
<p>length:${fn:length(id)}</p>


<h1>${fn:length(requestScope.get('asdf'))}</h1>


<h1>haha<c:out value="${requestScope.get('asdf')}"/></h1>
</body>
</html>
