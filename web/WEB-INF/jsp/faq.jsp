<%--
  Created by IntelliJ IDEA.
  User: xanarry
  Date: 17-12-28
  Time: 上午10:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>FAQ</title>
    <link rel="stylesheet" href="/css/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" href="/css/oj.css">

    <script src="/js/jquery-3.2.1.min.js"></script>
    <script src="/js/bootstrap/popper.min.js"></script>
    <script src="/js/bootstrap/bootstrap.min.js"></script>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/navbar.jsp"/>
<div class="container custom-container">
    <div class="card">
        <div class="card-header">
            <h5>FAQ</h5>
        </div>
        <div class="card-body">
            <p>
                Question: <br><span class="text-muted">系统支持哪几种编程语言?</span><br>
                Answer: <br>C/C++/Java/Python2?Python3
            </p>

            <p>
                Question: <br><span class="text-muted">各语言的编译选项是怎样的?</span><br>
                Answer:<br>
                C:  gcc source.c -o main -ansi -fno-asm -O2 -Wall -lm --static -DONLINE_JUDGE<br>
                C++: g++ source.cpp -o main -ansi -fno-asm -O2 -Wall -lm --static -DONLINE_JUDGE<br>
                Java: javac Main.java
            </p>

            <p>
                Question: <br><span class="text-muted">各种运行状态是什么含义?</span><br>
                Answer:
                <br>Queuing: 代码已经提交到队列, 正在等待测评
                <br>Compiling: 代码正在编译
                <br>Running: 编译成功, 正在进行测评
                <br>Acepted: 代码正确
                <br>Presentation Error: 格式错误. 一般来说结果是正确的, 但是多输出或者少输出了空格, 换行等符号
                <br>Wrong Answer: 答案错误. 你需要检查你的代码
                <br>Runtime Error: 运行时错误. 引发的原因有很多,包括但不仅限于使用未初始化的指针, 数组越界, 堆栈溢出, 除数为零, 或者使用了非法系统调用
                <br>Time Limit Exceeded: 超出时间限制. 可能是代码中包含死循环,也可能是算法不够优化
                <br>Memory Limit Exceeded: 内存超出限制. 你使用的内存太多了, 可能由于使用了过大的数组, 或是忘记释放使用过的内存导致内存泄漏
                <br>Output Limit Exceeded: 你的输出超出了系统的限制
                <br>Compilation Error: 编译错误. 请检查代码在自己的机器上能否正常编译, 以及是否选错了语言, 在比赛中编译错误不会增加罚时
                <br>System Error: 系统内部故障
            </p>

        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
</body>
</html>
