<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: xanarry
  Date: 18-1-26
  Time: 上午12:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<ul class="pagination justify-content-center">
<c:choose>
    <c:when test="${pageInfo.currentPageVal == 1}">
        <li class="page-item disabled"><span class="page-link">首页</span></li>
        <li class="page-item disabled"><span class="page-link">上一页</span></li>
    </c:when>
    <c:otherwise>
        <li class="page-item"><a class="page-link" href="${pageInfo.url}page=${1}">首页</a></li>
        <li class="page-item"><a class="page-link" href="${pageInfo.url}page=${pageInfo.currentPageVal - 1}">上一页</a></li>
    </c:otherwise>
</c:choose>

<c:forEach  begin="${pageInfo.currentPageVal - 5 >= 1 ? pageInfo.currentPageVal - 5 : 1}" end="${pageInfo.currentPageVal + 5 <= pageInfo.maxPageVal ? pageInfo.currentPageVal + 5 : pageInfo.maxPageVal}" step="1" var="curPage" varStatus="pos">
    <c:choose>
        <c:when test="${pageInfo.currentPageVal == curPage}"><li class="page-item active"><span class="page-link">${curPage}</span></li></c:when>
        <c:otherwise><li class="page-item"><a class="page-link" href="${pageInfo.url}page=${curPage}">${curPage}</a></li></c:otherwise>
    </c:choose>
</c:forEach>

<c:choose>
    <c:when test="${pageInfo.currentPageVal == pageInfo.maxPageVal}">
        <li class="page-item disabled"><span class="page-link">下一页</span></li>
        <li class="page-item disabled"><span class="page-link">末页</span></li>
    </c:when>
    <c:otherwise>
        <li class="page-item"><a class="page-link" href="${pageInfo.url}page=${pageInfo.currentPageVal + 1}">下一页</a></li>
        <li class="page-item"><a class="page-link" href="${pageInfo.url}page=${pageInfo.maxPageVal}">末页</a></li>
    </c:otherwise>
</c:choose>
</ul>