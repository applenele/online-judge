## 开发环境
```
Server version:   Apache Tomcat/9.0.4
OS Name:   Linux, Ubuntu 16.04
OS Version:   4.4.0-112-generic
Architecture:   amd64
Java Home:   jdk1.8.0_60/jre
JVM Version:   1.8.0_60-b27
JVM Vendor:   Oracle Corporation
```
## 评测支持语言
1. __C__
2. __C++__
3. __Java__
4. __Python2.x__
5. __Python3.x__

## 安装方法

### 预备工作
1. 运行环境要求Linux 64位系统, 内存1G以上, 可用磁盘空间5G以上.
2. 安装运行时环境, gcc, g++, Java, Python, 并设置相关环境变量.
3. 安装MySQL数据库.
4. 将系统语言设置为英文, 在__/etc/default/locale__中添加__LANG=en.US__, 防止gcc标准错误中出现非ASCII字符导致web端显示乱码.

### 安装系统
1. 设置judgeServer配置文件__oj.ini__, 默认配置文件如下, 其中java_policy与debug暂时未用到.
```
#judge server系统配置文件
#请严格按照格式配置, 否则系统会无法读取配置文件
#格式: [关键词:_值], 其中'_'表示空格
#是否开启调试模式
debug: 1
#端口, 默认端口2345
port: 2345
#被禁用的系统调用
restricted_syscalls: fork,   nanosleep,pause,reboot,   rmdir,rename,unlink,umount,chdir   connect,accept,sendto,recvfrom,sendmsg,recvmsg,shutdown,bind,listen,getsockname,getpeername,socketpair,setsockopt,getsockopt
#java安全配置文件
java_policy:
#最大输出文件限制字节
output_file_size: 4096
#oj的工作目录
oj_home: /judgeServer/you/want/to/place
```
2. 使用gcc编译, 并运行judgeServer.
3. 导入数据库定义.
4. 安装tomcat, 然后在tomcat的配置文件__/conf/server.xml__中将端口改为80, 再把tomcat POST数据的大小限制取消, 否则在网页在提交测试数据时遇到大的文本无法提交.
```
<Connector port="80" protocol="HTTP/1.1"
               connectionTimeout="20000"
               redirectPort="8443" 
               maxPostSize="-1"/>
```
4. 部署项目, 删除tomcat webapps中ROOT文件夹中的全部内容, 将war包解压到ROOT目录, 然后修改__apache-tomcat-9.0.4/webapps/ROOT/WEB-INF/classes/org/oj/database/mybatis-config.xml__, 将数据库账户, 密码填入, 再修改web服务器的配置文件__apache-tomcat-9.0.4/webapps/ROOT/WEB-INF/classes/config.json__, testPointBaseDir与 runningBaseDir请事先创建好, 其内容如下:
```
{
  "serverAddress":"127.0.0.1",
  "serverPort":2345, //评测机端口, 必须和oj.ini中的端口一致
  "testPointBaseDir":"/dir/of/test-point/data", //测试数据存放目录
  "runningBaseDir":"/dir/for/user/program",//用户提交程序的运行目录
  "timeout":10, //连接评测机的timeout, 暂时未使用
  "countPerPage":20 //网页分页每一页记录的条数
}
```
5.启动tomcat服务器, 浏览器输入localhost打开

## 项目预览
### [在线网站](http://oj.xy1234.cc/)
### 图片预览
![](https://raw.githubusercontent.com/xanarry/oj/master/demo-pics/home.png)
> 图为首页.

![](https://raw.githubusercontent.com/xanarry/oj/master/demo-pics/problem-list.png)
> 图为题目列表.

![](https://raw.githubusercontent.com/xanarry/oj/master/demo-pics/submit-record.png)
> 图为提交记录.

![](https://raw.githubusercontent.com/xanarry/oj/master/demo-pics/contest-overview.png)
> 图为比赛.

## 参考
1. [qdacm](https://qdacm.com/)
2. [hustoj](https://github.com/zhblue/hustoj)
