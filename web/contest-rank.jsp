<%--
  Created by IntelliJ IDEA.
  User: xanarry
  Date: 18-1-18
  Time: 下午7:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${contest.title}</title>

    <link rel="stylesheet" href="css/bootstrap/bootstrap.min.css">

    <script src="js/jquery-3.2.1.min.js"></script>
    <script src="js/bootstrap/popper.min.js"></script>
    <script src="js/bootstrap/bootstrap.min.js"></script>
</head>

<body>
<jsp:include page="navbar.jsp"/>
<div class="container" style="margin-top: 70px">
    <h1 align="center">title</h1>

    <div class="card">
        <div class="card-header">overview</div>
        <div class="card-body">
            <div class="card">
                <table class="table table-striped table-hover text-center">
                    <thead>
                    <tr>
                        <th>排名</th>
                        <th>用户名</th>
                        <th>AC题数</th>
                        <th>罚时</th>


                        <th><a href="/C/179/P/A">A(13/23)</a></th>

                        <th><a href="/C/179/P/B">B(10/27)</a></th>

                        <th><a href="/C/179/P/C">C(1/1)</a></th>

                        <th><a href="/C/179/P/D">D(1/1)</a></th>

                        <th><a href="/C/179/P/E">E(1/1)</a></th>

                        <th><a href="/C/179/P/F">F(1/1)</a></th>


                    </tr>
                    </thead>
                    <tbody>



                    <tr data-rank="1">
                        <td>1</td>
                        <td><a href="/User/念诗狂膜" class="myuser-base myuser-violet">念诗狂膜(已退役)</a></td>
                        <td>6</td>
                        <td>1028</td>


                        <td id="td_A_0" class="alert-info">+0<br>2:49:41</td>

                        <td id="td_B_0" class="alert-success rankyes">+0<br>2:50:13</td>

                        <td id="td_C_0" class="SlateFixBlack rankfirst">+0<br>2:50:51</td>

                        <td id="td_D_0" class="SlateFixBlack rankfirst">+0<br>2:51:51</td>

                        <td id="td_E_0" class="SlateFixBlack rankfirst">+0<br>2:52:31</td>

                        <td id="td_F_0" class="SlateFixBlack rankfirst">+0<br>2:53:22</td>


                    </tr>


                    <tr data-rank="2">
                        <td>2</td>
                        <td><a href="/User/Admin_lh" class="myuser-base myuser-cyan">Admin_lh</a></td>
                        <td>2</td>
                        <td>131</td>


                        <td id="td_A_1" class="alert-primary">+0<br>0:47:32</td>

                        <td id="td_B_1" class="alert-danger">+0<br>1:23:41</td>

                        <td id="td_C_1" class="SlateFixBlack "></td>

                        <td id="td_D_1" class="SlateFixBlack "></td>

                        <td id="td_E_1" class="SlateFixBlack "></td>

                        <td id="td_F_1" class="SlateFixBlack "></td>


                    </tr>


                    <tr data-rank="3">
                        <td>3</td>
                        <td><a href="/User/Uzi" class="myuser-base myuser-cyan">Uzi</a></td>
                        <td>2</td>
                        <td>163</td>


                        <td id="td_A_2" class="SlateFixBlack rankyes">+0<br>1:4:11</td>

                        <td id="td_B_2" class="SlateFixBlack rankyes">+0<br>1:39:30</td>

                        <td id="td_C_2" class="SlateFixBlack "></td>

                        <td id="td_D_2" class="SlateFixBlack "></td>

                        <td id="td_E_2" class="SlateFixBlack "></td>

                        <td id="td_F_2" class="SlateFixBlack "></td>


                    </tr>


                    <tr data-rank="4">
                        <td>4</td>
                        <td><a href="/User/无敌的我又回来了" class="myuser-base myuser-cyan">无敌的我又回来了</a></td>
                        <td>2</td>
                        <td>170</td>


                        <td id="td_A_3" class="SlateFixBlack rankyes">+0<br>1:0:29</td>

                        <td id="td_B_3" class="SlateFixBlack rankyes">+0<br>1:50:21</td>

                        <td id="td_C_3" class="SlateFixBlack "></td>

                        <td id="td_D_3" class="SlateFixBlack "></td>

                        <td id="td_E_3" class="SlateFixBlack "></td>

                        <td id="td_F_3" class="SlateFixBlack "></td>


                    </tr>


                    <tr data-rank="5">
                        <td>5</td>
                        <td><a href="/User/张家维" class="myuser-base myuser-cyan">张家维(张家维)</a></td>
                        <td>2</td>
                        <td>180</td>


                        <td id="td_A_4" class="SlateFixBlack rankyes">+1<br>1:2:8</td>

                        <td id="td_B_4" class="SlateFixBlack rankyes">+0<br>1:38:31</td>

                        <td id="td_C_4" class="SlateFixBlack "></td>

                        <td id="td_D_4" class="SlateFixBlack "></td>

                        <td id="td_E_4" class="SlateFixBlack "></td>

                        <td id="td_F_4" class="SlateFixBlack "></td>


                    </tr>


                    </tbody>
                </table>
            </div>
        </div>

        <div class="alert alert-primary" role="alert">
            This is a primary alert—check it out!
        </div>
        <div class="alert alert-secondary" role="alert">
            This is a secondary alert—check it out!
        </div>
        <div class="alert alert-success" role="alert">
            This is a success alert—check it out!
        </div>
        <div class="alert alert-danger" role="alert">
            This is a danger alert—check it out!
        </div>
        <div class="alert alert-warning" role="alert">
            This is a warning alert—check it out!
        </div>
        <div class="alert alert-info" role="alert">
            This is a info alert—check it out!
        </div>
        <div class="alert alert-light" role="alert">
            This is a light alert—check it out!
        </div>
        <div class="alert alert-dark" role="alert">
            This is a dark alert—check it out!
        </div>

        </div>
    </div>
</div>
<jsp:include page="footer.jsp"/>
</body>
</html>