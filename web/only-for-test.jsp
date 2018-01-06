<%--
  Created by IntelliJ IDEA.
  User: xanarry
  Date: 17-12-27
  Time: 上午12:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <title>Bootstrap Example</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">


    <link rel="stylesheet" href="css/bootstrap/bootstrap.min.css">

    <script src="js/jquery-3.2.1.min.js"></script>
    <script src="js/bootstrap/popper.min.js"></script>
    <script src="js/bootstrap/bootstrap.min.js"></script>

    <script>
        function getValidateCode() {
            $("#validateCode").attr("src","ajaxGetValidateCode?r=" + Math.random());
        }
    </script>

</head>
<body>
<jsp:include page="navbar.jsp"/>
<div class="container" style="margin-top:70px">
    <form datatype="">

    </form>
</div>

<jsp:include page="footer.jsp"/>
</body>
</html>

