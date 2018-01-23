<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: xanarry
  Date: 17-12-28
  Time: 下午11:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>discuss</title>

    <link rel="stylesheet" href="css/bootstrap/bootstrap.min.css">

    <script src="js/jquery-3.2.1.min.js"></script>
    <script src="js/bootstrap/popper.min.js"></script>
    <script src="js/bootstrap/bootstrap.min.js"></script>

    <script src="plugin/ckeditor/ckeditor.js"></script>

    <script>
        function problemCheck() {
            $('textarea.ckeditor').each(function () {
                var $textarea = $(this);
                $textarea.val(CKEDITOR.instances[$textarea.attr('id')].getData());
            });
        }
    </script>
</head>


<body>
<jsp:include page="navbar.jsp"/>
<div class="container" style="margin-top: 70px">
    <div class="card">
        <div class="card-header"><h5>${discuss.title}</h5></div>
        <div class="card-body">
            <p class="card-text">${discuss.content}</p>
            <hr style="margin-top: 1.5rem; margin-bottom: 0;">
            <ul class="mb-1 list-inline">
                <li class="list-inline-item">
                    <small class="badge badge-secondary">${discuss.theme}</small>
                </li>
                <li class="list-inline-item">
                    <small class="card alert-secondary">${discuss.userName}</small>
                </li>
                <li class="list-inline-item">
                    <small class="text-muted">${discuss.watch}次查看</small>
                </li>
                <li class="list-inline-item">
                    <jsp:useBean id="postTime" class="java.util.Date"/>
                    <c:set target="${postTime}" property="time" value="${discuss.postTime}"/>
                    <small class="text-muted"><fmt:formatDate pattern="yyyy/MM/dd HH:mm:ss" value="${postTime}"/></small>
                </li>
            </ul>
        </div>
    </div>

    <br>

    <div class="card">
        <div class="card-header"><h5><a data-toggle="collapse" href="#collapseExample">点此发表您的评论</a></h5></div>
        <div class="collapse" id="collapseExample">

            <div class="card-body">
                <form class="form-group">
                    <textarea name="inputComment" id="inputComment" class="form-control"
                              style="height:150px; font-family: Consolas"></textarea>
                    <%-- <script>
                         CKEDITOR.replace('inputComment'); // 这里的 'editor1' 等于 textarea 的 id 'editor1'
                     </script>--%>

                    <br><input class="btn btn-success" type="submit" value="发布评论">
                </form>
            </div>
        </div>
    </div>

    <br>
    <div class="list-group">
        <div class="list-group-item list-group-item-action flex-column align-items-start ">
            <div class="media">
                <div class="align-self-center text-center mr-4">
                    <h5>回复列表(324)</h5>
                </div>
            </div>
        </div>


        <div class="list-group-item list-group-item-action flex-column align-items-start ">
            <div class="media">
                <div class="align-self-center text-center mr-4">
                    <h3 style="width: 50px">21</h3>
                </div>
                <div class="media-body">
                    <div class="d-flex w-100 justify-content-between">
                        <h5 class="mb-1">这里是讨论的标题</h5>
                    </div>
                    <ul class="mb-1 list-inline">
                        <li class="list-inline-item">
                            <small class="badge badge-secondary">A+B Prolem</small>
                        </li>
                        <li class="list-inline-item">
                            <small class="card alert-secondary">xanarry</small>
                        </li>
                        <li class="list-inline-item">
                            <small class="text-muted">2018-12-13 12:12:12</small>
                        </li>
                    </ul>
                </div>
                <div class="align-self-center ml-4">
                    <a href="#" class="btn-sm btn-primary">置顶</a>
                    <a href="#" class="btn-sm btn-danger">删除</a>
                </div>
            </div>
        </div>
        <div class="list-group-item list-group-item-action flex-column align-items-start ">
            <div class="media">
                <div class="align-self-center text-center mr-4">
                    <h3 style="width: 50px">213</h3>
                </div>
                <div class="media-body">
                    <div class="d-flex w-100 justify-content-between">
                        <h5 class="mb-1">这里是讨论的标题</h5>
                    </div>
                    <ul class="mb-1 list-inline">
                        <li class="list-inline-item">
                            <small class="badge badge-secondary">A+B Prolem</small>
                        </li>
                        <li class="list-inline-item">
                            <small class="card alert-secondary">xanarry</small>
                        </li>
                        <li class="list-inline-item">
                            <small class="text-muted">2018-12-13 12:12:12</small>
                        </li>
                    </ul>
                </div>
                <div class="align-self-center ml-4">
                    <a href="#" class="btn-sm btn-primary">置顶</a>
                    <a href="#" class="btn-sm btn-danger">删除</a>
                </div>
            </div>
        </div>
        <div class="list-group-item list-group-item-action flex-column align-items-start ">
            <div class="media">
                <div class="align-self-center text-center mr-4">
                    <h3 style="width: 50px">2123</h3>
                </div>
                <div class="media-body">
                    <div class="d-flex w-100 justify-content-between">
                        <h5 class="mb-1">这里是讨论的标题</h5>
                    </div>
                    <ul class="mb-1 list-inline">
                        <li class="list-inline-item">
                            <small class="badge badge-secondary">A+B Prolem</small>
                        </li>
                        <li class="list-inline-item">
                            <small class="card alert-secondary">xanarry</small>
                        </li>
                        <li class="list-inline-item">
                            <small class="text-muted">2018-12-13 12:12:12</small>
                        </li>
                    </ul>
                </div>
                <div class="align-self-center ml-4">
                    <a href="#" class="btn-sm btn-primary">置顶</a>
                    <a href="#" class="btn-sm btn-danger">删除</a>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>
</html>
