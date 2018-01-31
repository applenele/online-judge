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

    <link rel="stylesheet" href="/css/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" href="/css/oj.css">

    <script src="/js/jquery-3.2.1.min.js"></script>
    <script src="/js/bootstrap/popper.min.js"></script>
    <script src="/js/bootstrap/bootstrap.min.js"></script>

    <script src="/plugin/ckeditor/ckeditor.js"></script>

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
<jsp:include page="/WEB-INF/jsp/navbar.jsp"/>

<div class="container custom-container">
    <h3 class="text-center"><a href="/problem?problemID=${problem.problemID}">${problem.title}</a></h3>
    <div class="card">
        <div class="card-header">
            <h4>
                <c:choose>
                    <c:when test="${problem == null}">
                        添加题目
                    </c:when>
                    <c:otherwise>
                        编辑题目
                    </c:otherwise>
                </c:choose>
            </h4>
        </div>
        <div class="card-body">
            <form method="post" action="/problem-add" onsubmit="return problemCheck()">

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

                                <label type="text" class="form-control">静态语言</label>
                            </div>
                        </div>
                    </div>

                    <div class="col-auto">
                        <div class="input-group">
                            <span class="input-group-addon">时间限制</span>
                            <select class="form-control" name="inputStaticLangTimeLimit" id="inputStaticLangTimeLimit">
                                <c:choose>
                                    <c:when test="${not empty problem}">
                                        <option value="1000" <c:if test="${problem.staticLangTimeLimit == 1000}">selected</c:if>>1000ms</option>
                                        <option value="2000" <c:if test="${problem.staticLangTimeLimit == 2000}">selected</c:if>>2000ms</option>
                                        <option value="3000" <c:if test="${problem.staticLangTimeLimit == 3000}">selected</c:if>>3000ms</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="1000" selected>1000ms</option>
                                        <option value="2000">2000ms</option>
                                        <option value="3000">3000ms</option>
                                    </c:otherwise>
                                </c:choose>
                            </select>
                        </div>
                    </div>

                    <div class="col-auto">
                        <div class="input-group">
                            <span class="input-group-addon">内存限制</span>
                            <select class="form-control" name="inputStaticLangMemLimit" id="inputStaticLangMemLimit">
                                <c:choose>
                                    <c:when test="${not empty problem}">
                                        <option value="32768" <c:if test="${problem.staticLangMemLimit == 32768}">selected</c:if>>32MB</option>
                                        <option value="65536" <c:if test="${problem.staticLangMemLimit == 65536}">selected</c:if>>64MB</option>
                                        <option value="131072" <c:if test="${problem.staticLangMemLimit == 131072}">selected</c:if>>128MB</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="32768">32MB</option>
                                        <option value="65536" selected>64MB</option>
                                        <option value="131072">128MB</option>
                                    </c:otherwise>
                                </c:choose>
                            </select>
                        </div>
                    </div>
                </div>

                <br>
                <div class="form-row align-items-center">
                    <div class="col-auto">
                        <div class="input-group mb-2 mb-sm-0">
                            <div class="input-group mb-2 mb-sm-0">
                                <label type="text" class="form-control">动态语言</label>
                            </div>
                        </div>
                    </div>
                    <div class="col-auto">
                        <div class="input-group">
                            <span class="input-group-addon">时间限制</span>
                            <select class="form-control" name="inputDynamicLangTimeLimit" id="inputDynamicLangTimeLimit">
                                <c:choose>
                                    <c:when test="${not empty problem}">
                                        <option value="1000" <c:if test="${problem.dynamicLangTimeLimit == 1000}">selected</c:if>>1000ms</option>
                                        <option value="2000" <c:if test="${problem.dynamicLangTimeLimit == 2000}">selected</c:if>>2000ms</option>
                                        <option value="3000" <c:if test="${problem.dynamicLangTimeLimit == 3000}">selected</c:if>>3000ms</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="1000">1000ms</option>
                                        <option value="2000" selected>2000ms</option>
                                        <option value="3000">3000ms</option>
                                    </c:otherwise>
                                </c:choose>
                            </select>
                        </div>
                    </div>

                    <div class="col-auto">
                        <div class="input-group">
                            <span class="input-group-addon">内存限制</span>
                            <select class="form-control" name="inputDynamicLangMemLimit" id="inputDynamicLangMemLimit">
                                <c:choose>
                                    <c:when test="${not empty problem}">
                                        <option value="32768" <c:if test="${problem.dynamicLangMemLimit == 32768}">selected</c:if>>32MB</option>
                                        <option value="65536" <c:if test="${problem.dynamicLangMemLimit == 65536}">selected</c:if>>64MB</option>
                                        <option value="131072" <c:if test="${problem.dynamicLangMemLimit == 131072}">selected</c:if>>128MB</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="32768">32MB</option>
                                        <option value="65536">64MB</option>
                                        <option value="131072" selected>128MB</option>
                                    </c:otherwise>
                                </c:choose>
                            </select>
                        </div>
                    </div>
                </div>

                <br>
                <div class="form-row align-items-center" style="display: none">
                    <div class="col-auto">
                        <div class="form-check">
                            <label class="form-check-label">
                                <input class="form-check-input" name="inputHidden" id="inputHidden" type="checkbox">隐藏此题目
                            </label>
                        </div>
                    </div>
                </div>


                <br>

                <h4>题目描述</h4>
                <textarea name="inputDesc" id="inputDesc" class="ckeditor">${problem.desc}</textarea>

                <br>
                <h4>输入格式</h4>
                <textarea name="inputInputDesc" id="inputInputDesc" class="ckeditor">${problem.inputDesc}</textarea>

                <br>
                <h4>输出格式</h4>
                <textarea name="inputOutputDesc" id="inputOutputDesc" class="ckeditor">${problem.outputDesc}</textarea>

                <br>
                <div class="form-row">
                    <div class="col-sm-6">
                        <h4>样例输入</h4>
                        <textarea name="inputInputSample" id="inputInputSample" class="form-control"
                                  style="height:200px; font-family: Consolas"><c:out value="${problem.inputSample}"
                                                                                     escapeXml="true"></c:out></textarea>
                    </div>
                    <div class="col-sm-6">
                        <h4>样例输出</h4>
                        <textarea name="inputOutputSample" id="inputOutputSample" class="form-control"
                                  style="height:200px; font-family: Consolas"><c:out value="${problem.outputSample}"
                                                                                     escapeXml="true"></c:out></textarea>
                    </div>
                </div>

                <br>
                <h4>提示</h4>
                <textarea name="inputHint" id="inputHint" class="ckeditor">${problem.hint}</textarea>

                <br>
                <h4>来源</h4>
                <textarea name="inputSource" id="inputSource" class="ckeditor">${problem.source}</textarea>
                <br>

                <div class="text-center">
                    <a class="btn btn-primary" href="/test-point-list?problemID=${problem.problemID}">测试数据</a>
                    <c:choose>
                        <c:when test="${problem == null}">
                            <input class="btn btn-success" type="submit" value="添加题目">
                        </c:when>
                        <c:otherwise>
                            <input class="btn btn-success" type="submit" value="保存修改">
                            <a class="btn btn-danger" href="#" data-toggle="modal" data-target="#deleteModal">删除题目</a>
                        </c:otherwise>
                    </c:choose>
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
                <p>与该题目相关的所有信息即将永久性删除, 是否继续?</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">返回</button>
                <a href="/problem-delete?problemID=${problem.problemID}" class="btn btn-danger">是的,删除</a>
            </div>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
</body>
</html>
