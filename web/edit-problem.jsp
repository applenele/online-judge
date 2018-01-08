<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: xanarry
  Date: 17-12-28
  Time: 下午8:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <c:choose>
        <c:when test="${problem != null}"><title>编辑 P${1000 + problem.problemID}</title></c:when>
        <c:otherwise><title>添加题目</title></c:otherwise>
    </c:choose>

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

            var title = $("#inputTitle").val();
            var desc = $("#inputDesc").val();
            var inputDesc = $("#inputInputDesc").val();
            var outputDesc = $("#inputOutputDesc").val();
            var inputSample = $("#inputInputSample").val();
            var outputSample = $("#inputOutputSample").val();
            var hint = $("#inputHint").val();
            var source = $("#inputSource").val();
            var staticLangTimeLimit = $("#inputStaticLangTimeLimit").val();
            var staticLangMemLimit = $("#inputStaticLangMemLimit").val();
            var dynamicLangTimeLimit = $("#inputDynamicLangTimeLimit").val();
            var dynamicLangMemLimit = $("#inputDynamicLangMemLimit").val();

            if (title.length == 0) {
                alert("标题不能为空");
                return false;
            }

            var flag = 0;
            $("textarea").each(function () {
                console.log($(this).attr("id") + ": " + $(this).val());
                if ($(this).val().length == 0) {
                    if ($(this).attr("id") != "inputHint" && $(this).attr("id") != "inputSource") {
                        flag++;
                    }
                }
            });
            if (flag == 0) {
                return true;
            } else {
                alert("除了提示和来源, 其余内容不允许为空");
                return false;
            }
        }
    </script>
</head>


<body>
<jsp:include page="navbar.jsp"/>

<div class="container" style="margin-top: 70px">
    <div class="card">
        <div class="card-header">
            <h4>
                <c:choose>
                    <c:when test="${problem == null}">
                        添加题目
                    </c:when>
                    <c:otherwise>
                        ${problem.title}
                    </c:otherwise>
                </c:choose>
            </h4>
        </div>
        <div class="card-body">
            <form method="post" action="/add-problem" onsubmit="return problemCheck()">

                <c:if test="${problem != null}">
                    <input id="inputProblemID" hidden name="inputProblemID" value="${problem.problemID}">
                </c:if>

                <div class="input-group">
                    <span class="input-group-addon">题目名称</span>
                    <input name="inputTitle" id="inputTitle" type="text" value="${problem.title}"
                           placeholder="标题不超过200个字符" class="form-control">
                </div>
                <br>

                <div class="form-row align-items-center">
                    <div class="col-auto">
                        <div class="input-group mb-2 mb-sm-0">
                            <div class="input-group mb-2 mb-sm-0">
                                <div class="input-group-addon">语言</div>
                                <label type="text" class="form-control">静态语言</label>
                            </div>
                        </div>
                    </div>

                    <c:choose>
                        <c:when test="${problem != null}">
                            <c:set var="staticLangTimeLimit" value="${problem.staticLangTimeLimit}" scope="page"/>
                            <c:set var="staticLangMemLimit" value="${problem.staticLangMemLimit}" scope="page"/>
                            <c:set var="dynamicLangTimeLimit" value="${problem.dynamicLangTimeLimit}" scope="page"/>
                            <c:set var="dynamicLangMemLimit" value="${problem.dynamicLangMemLimit}" scope="page"/>
                        </c:when>

                        <c:otherwise>
                            <c:set var="staticLangTimeLimit" value="1000" scope="page"/>
                            <c:set var="staticLangMemLimit" value="65535" scope="page"/>
                            <c:set var="dynamicLangTimeLimit" value="2000" scope="page"/>
                            <c:set var="dynamicLangMemLimit" value="131070" scope="page"/>
                        </c:otherwise>
                    </c:choose>
                    <div class="col-auto">
                        <div class="input-group">
                            <span class="input-group-addon">时间限制</span>
                            <input name="inputStaticLangTimeLimit" id="inputStaticLangTimeLimit" type="text"
                                   value="${staticLangTimeLimit}" placeholder="默认1000, 单位: ms" class="form-control">
                        </div>
                    </div>

                    <div class="col-auto">
                        <div class="input-group">
                            <span class="input-group-addon">内存限制</span>
                            <input name="inputStaticLangMemLimit" id="inputStaticLangMemLimit" type="text"
                                   value="${staticLangMemLimit}"
                                   placeholder="默认65535, 单位: KB" class="form-control">
                        </div>
                    </div>
                </div>

                <br>
                <div class="form-row align-items-center">
                    <div class="col-auto">
                        <div class="input-group mb-2 mb-sm-0">
                            <div class="input-group mb-2 mb-sm-0">
                                <div class="input-group-addon">语言</div>
                                <label type="text" class="form-control">动态语言</label>
                            </div>
                        </div>
                    </div>
                    <div class="col-auto">
                        <div class="input-group">
                            <span class="input-group-addon">时间限制</span>
                            <input name="inputDynamicLangTimeLimit" id="inputDynamicLangTimeLimit" type="text"
                                   value="${dynamicLangMemLimit}" placeholder="默认2000, 单位: ms" class="form-control">
                        </div>
                    </div>

                    <div class="col-auto">
                        <div class="input-group">
                            <span class="input-group-addon">内存限制</span>
                            <input name="inputDynamicLangMemLimit" id="inputDynamicLangMemLimit" type="text"
                                   value="${dynamicLangMemLimit}" placeholder="131070, 单位: KB" class="form-control">
                        </div>
                    </div>
                </div>

                <br>
                <div class="form-row align-items-center">
                    <div class="col-auto">
                        <div class="form-check">
                            <label class="form-check-label">
                                <input class="form-check-input" name="inputHidden" id="inputHidden" type="checkbox">隐藏此题目
                            </label>
                        </div>
                    </div>
                </div>


                <br>

                <h3>题目描述</h3>
                <textarea name="inputDesc" id="inputDesc" class="ckeditor">${problem.desc}</textarea>

                <br>
                <h3>输入格式</h3>
                <textarea name="inputInputDesc" id="inputInputDesc" class="ckeditor">${problem.inputDesc}</textarea>

                <br>
                <h3>输出格式</h3>
                <textarea name="inputOutputDesc" id="inputOutputDesc" class="ckeditor">${problem.outputDesc}</textarea>

                <br>
                <div class="form-row">
                    <div class="col-sm-6">
                        <h3>样例输入</h3>
                        <textarea name="inputInputSample" id="inputInputSample" class="form-control"
                                  style="height:200px; font-family: Consolas"><c:out value="${problem.inputSample}"
                                                                                     escapeXml="true"></c:out></textarea>
                    </div>
                    <div class="col-sm-6">
                        <h3>样例输出</h3>
                        <textarea name="inputOutputSample" id="inputOutputSample" class="form-control"
                                  style="height:200px; font-family: Consolas"><c:out value="${problem.outputSample}"
                                                                                     escapeXml="true"></c:out></textarea>
                    </div>
                </div>

                <br>
                <h3>提示</h3>
                <textarea name="inputHint" id="inputHint" class="ckeditor">${problem.hint}</textarea>

                <br>
                <h3>来源</h3>
                <textarea name="inputSource" id="inputSource" class="ckeditor">${problem.source}</textarea>
                <br>

                <div class="text-center">
                    <c:choose>
                        <c:when test="${problem == null}">
                            <input class="btn btn-success" type="submit" value="添加题目">
                        </c:when>
                        <c:otherwise>
                            <input class="btn btn-success" type="submit" value="保存修改">
                        </c:otherwise>
                    </c:choose>

                </div>
            </form>
        </div>
    </div>

</div>
<jsp:include page="footer.jsp"/>
</body>
</html>
