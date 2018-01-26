package org.oj.controller;

import email.SendMail;
import org.apache.ibatis.session.SqlSession;
import org.oj.controller.beans.MessageBean;
import org.oj.controller.beans.PageBean;
import org.oj.database.*;
import org.oj.model.javaBean.LanguageBean;
import org.oj.model.javaBean.UserBean;
import utils.Consts;
import utils.Tools;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import static org.apache.commons.codec.digest.DigestUtils.sha1Hex;

/**
 * Created by xanarry on 18-1-2.
 */
@WebServlet(name = "userServlet",
        urlPatterns = {
                "/user",
                "/user-list",
                "/user-edit",
                "/user-chart",
                "/user-delete",
                "/retrieve-password",
                "/send-retrieve-password-email"
        }
    )


public class userServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("post " + request.getRequestURL());

        String pathInfo = request.getPathInfo();
        if (pathInfo.equals("/user-edit"))                  editUserPost(request, response);
        if (pathInfo.equals("/send-retrieve-password-email"))    sendRetrievePasswordEmailPost(request, response);
        if (pathInfo.equals("/retrieve-password"))          retrievePasswordPost(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("get " + request.getRequestURL());
        String uri = request.getRequestURI();

        if (uri.equals("/user"))         showUserGet(request, response);
        if (uri.equals("/user-list"))    userListGet(request, response);//直接访问根部, 返回用户列表
        if (uri.equals("/user-chart"))   userChartGet(request, response);//直接访问根部, 返回用户列表
        if (uri.equals("/user-delete"))  deleteUserGet(request, response);
        if (uri.equals("/retrieve-password")) retrievePasswordGet(request, response);
    }

    private void userListGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strPage = request.getParameter("page");
        int page =  strPage != null ? Integer.parseInt(strPage) : 1;

        SqlSession sqlSession = DataSource.getSqlSesion();
        TableUser tableUser = sqlSession.getMapper(TableUser.class);

        List<UserBean> userList = tableUser.getUserList((page - 1) * Consts.COUNT_PER_PAGE, Consts.COUNT_PER_PAGE);
        int recordCount = tableUser.getCount();

        //获取分页信息
        PageBean pageBean = Utils.getPagination(recordCount, page, request);

        sqlSession.close();

        request.setAttribute("userList", userList);
        request.setAttribute("pageInfo", pageBean);
        request.getRequestDispatcher("/WEB-INF/jsp/user/user-list.jsp").forward(request, response);
    }

    private void userChartGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strPage = request.getParameter("page");
        int page =  strPage != null ? Integer.parseInt(strPage) : 1;

        SqlSession sqlSession = DataSource.getSqlSesion();
        TableUser tableUser = sqlSession.getMapper(TableUser.class);

        List<UserBean> userList = tableUser.getChart((page - 1) * Consts.COUNT_PER_PAGE, Consts.COUNT_PER_PAGE);
        int recordCount = tableUser.getCount();
        //获取分页信息
        PageBean pageBean = Utils.getPagination(recordCount, page, request);

        sqlSession.close();

        request.setAttribute("userList", userList);
        request.setAttribute("pageInfo", pageBean);
        request.getRequestDispatcher("/WEB-INF/jsp/user/user-chart.jsp").forward(request, response);
    }


    protected void sendRetrievePasswordEmailPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = request.getParameter("inputUserName");
        String email = request.getParameter("inputEmail");
        SqlSession sqlSession = DataSource.getSqlSesion();
        TableUser tableUser = sqlSession.getMapper(TableUser.class);
        UserBean userBean = tableUser.getUserByEmail(email);
        sqlSession.close();

        String jsonPattern = "{\"userNameCheck\":%s, \"emailCheck\":%s, \"sendMail\":%s}";
        String json = "{\"userNameCheck\": false, \"emailCheck\":false, \"sendMail\":false}";
        if (userBean != null && userBean.getUserName().equals(userName)) {
            String code = Tools.saltBase64Encode(new Date().getTime() + "#" + email);
            String url = new String(request.getRequestURL()).replace(new String(request.getRequestURI()), "") + "/retrieve-password?pattern=" + code;
            SendMail.sendMail(url, email);
            json = String.format(jsonPattern, true, true, true);
        } else {
            json = String.format(jsonPattern, false, false, true);
        }
        Utils.responseJson(response, json);
    }


    private void showUserGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strUserID = request.getParameter("userID");
        if (strUserID != null) {
            Integer userID = Integer.parseInt(strUserID);
            SqlSession sqlSession = DataSource.getSqlSesion();
            TableUser user = sqlSession.getMapper(TableUser.class);
            UserBean userBean = user.getUserByID(userID);
            List<LanguageBean> languages = sqlSession.getMapper(TableLanguage.class).getLanguageList();
            sqlSession.close();

            System.out.println(userBean);

            if (userBean != null) {
                request.setAttribute("user", userBean);
                request.setAttribute("languages", languages);
                request.getRequestDispatcher("/WEB-INF/jsp/user/user-information.jsp").forward(request, response);
            } else {
                MessageBean messageBean = new MessageBean("错误", "错误", "用户ID不存在!", "/", "回到首页");
                Utils.sendErrorMsg(response, messageBean);
            }
        } else {
            MessageBean messageBean = new MessageBean("错误", "错误", "用户ID不存在!", "/", "回到首页");
            Utils.sendErrorMsg(response, messageBean);
        }
    }


    private void deleteUserGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strUserID = request.getParameter("userID");
        if (strUserID != null) {
            Integer userID = Integer.parseInt(strUserID);
            SqlSession sqlSession = DataSource.getSqlSesion();

            //删除用户本身信息
            TableUser user = sqlSession.getMapper(TableUser.class);
            UserBean userBean = user.getUserByID(userID);
            String userName = userBean.getUserName();

            /*数据库中定义的触发器将会删除和该用户相关的: 提交信息, 比赛信息, 讨论信息*/
            user.deleteUserById(userID);

            sqlSession.commit();
            sqlSession.close();

            MessageBean messageBean = new MessageBean("提示", "提示", userName + " 已经被删除!", "/", "回到首页");
            Utils.sendErrorMsg(response, messageBean);
        }

    }

    private void editUserPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strUserID = request.getParameter("inputUserID");
        Integer userID = Integer.parseInt(strUserID);
        String userName = request.getParameter("inputUserName");
        String oldPassword = request.getParameter("inputOldPassword");
        String password = request.getParameter("inputPassword");
        String bio = request.getParameter("inputBio");
        String sendCode = request.getParameter("inputSendCode");
        String preferLanguage = request.getParameter("inputPreferLanguage");

        SqlSession sqlSession = DataSource.getSqlSesion();
        TableUser user = sqlSession.getMapper(TableUser.class);

        UserBean userBean = user.getUserByID(userID);

        boolean userNameExist = user.checkUserNameExist(userName) && !userName.equals(userBean.getUserName());
        boolean oldPasswordOk = userBean.getPassword().equals(sha1Hex(oldPassword));

        if (!userNameExist && oldPasswordOk) {

            userBean.setUserName(userName);
            userBean.setPassword(sha1Hex(password));
            userBean.setBio(bio);
            userBean.setSendCode(sendCode != null && sendCode.equals("true"));
            userBean.setPreferLanguage(preferLanguage);

            System.out.println("ok, new user info: " + userBean);

            user.updateUser(userBean);
            sqlSession.commit();
            userNameExist = false;
            oldPasswordOk = true;

        }

        sqlSession.close();

        String jsonPattern = "{\"userNameExist\" : %s, \"correctOldPassword\" :%s}";
        String json = String.format(jsonPattern, userNameExist, oldPasswordOk);
        Utils.responseJson(response, json);
    }


    private void retrievePasswordGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("pattern");
        System.out.println(code);
        if (code != null && code.length() > 10) {
            String msg = Tools.saltBase64Decode(code);
            System.out.println(msg);
            String[] c = msg.split("#");
            System.out.println(c[0] + " " + c[1]);
            if (new Date().getTime() - Long.parseLong(c[0]) > 30 * 60 * 1000) {
                MessageBean messageBean = new MessageBean();
                messageBean.setTitle("错误");
                messageBean.setHeader("错误");
                messageBean.setMessage("该链接已经失效!");
                messageBean.setUrl("/");
                messageBean.setLinkText("返回首页");
                Utils.sendErrorMsg(response, messageBean);
                return;
            }
            request.setAttribute("email", c[1]);
        }
        request.getRequestDispatcher("/WEB-INF/jsp/user/user-retrieve-password.jsp").forward(request, response);
    }

    private void retrievePasswordPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("inputRetrieveEmail");
        String password = request.getParameter("inputNewPassword");

        SqlSession sqlSession = DataSource.getSqlSesion();
        TableUser tableUser = sqlSession.getMapper(TableUser.class);
        UserBean userBean = tableUser.getUserByEmail(email);
        userBean.setPassword(sha1Hex(password.getBytes()));

        tableUser.updateUser(userBean);
        sqlSession.commit();
        sqlSession.close();


        MessageBean messageBean = new MessageBean();
        messageBean.setTitle("消息");
        messageBean.setHeader("消息");
        messageBean.setMessage("密码设置成功");
        messageBean.setUrl("/");
        messageBean.setLinkText("返回首页");
        Utils.sendErrorMsg(response, messageBean);
    }
}
