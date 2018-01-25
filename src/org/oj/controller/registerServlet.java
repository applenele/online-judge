package org.oj.controller;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.ibatis.session.SqlSession;
import org.oj.controller.beans.MessageBean;
import org.oj.database.DataSource;
import org.oj.database.TableLanguage;
import org.oj.database.TableUser;
import org.oj.model.javaBean.LanguageBean;
import org.oj.model.javaBean.UserBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

/**
 * Created by xanarry on 18-1-1.
 */
@WebServlet(name = "registerServlet", urlPatterns = {"/register", "/ajaxCheckRegisterInfo"})
public class registerServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("post: " + request.getRequestURL());

        if (request.getRequestURI().endsWith("ajaxCheckRegisterInfo")) {
            ajaxCheckRegisterInfo(request, response);
            return;
        }


        String userName = request.getParameter("inputUserName");
        String email    = request.getParameter("inputEmail");
        String password = request.getParameter("inputPassword");
        String bio      = request.getParameter("inputBio");
        String preferLanguage = request.getParameter("inputPreferLanguage");

        UserBean userBean = new UserBean();
        userBean.setUserName(userName);
        userBean.setEmail(email);
        userBean.setPassword(DigestUtils.sha1Hex(password));
        userBean.setRegisterTime(new Date().getTime());
        userBean.setLastLoginTime(new Date().getTime());
        userBean.setBio(bio);
        userBean.setPreferLanguage(preferLanguage);
        userBean.setSendCode(true);

        System.out.println(userBean.toString());
        SqlSession sqlSession = DataSource.getSqlSesion();
        TableUser user = sqlSession.getMapper(TableUser.class);
        int retVal = user.addNewUser(userBean);
        sqlSession.commit();
        sqlSession.close();
        System.out.println("return value: " + retVal);
        System.out.println(userBean.getUserID());

        MessageBean messageBean = new MessageBean("注册成功", "Info", "恭喜您已经成功完成注册,登录使用系统吧!", "/", "Got It");
        Utils.sendErrorMsg(response, messageBean);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("get: " + request.getRequestURL());
        TableLanguage tableLanguage = DataSource.getSqlSesion().getMapper(TableLanguage.class);
        List<LanguageBean> languageList = tableLanguage.getLanguageList();
        request.setAttribute("languageList", languageList);
        request.getRequestDispatcher("user-register.jsp").forward(request, response);
    }


    private void ajaxCheckRegisterInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = request.getParameter("inputUserName");
        String email    = request.getParameter("inputEmail");

        System.out.println("checking: " + userName + " " + email);

        SqlSession sqlSession = DataSource.getSqlSesion();
        TableUser user = sqlSession.getMapper(TableUser.class);
        Boolean userNameCheckVal = user.checkUserNameExist(userName);
        Boolean emailCheckVal    = user.checkEmailExist(email);
        sqlSession.close();

        /*
         json: {"userNameExist": true, "emailExist": true}
         */

        String jsonPattern = "{\"userNameExist\": %s, \"emailExist\": %s}";
        String json = String.format(jsonPattern, userNameCheckVal, emailCheckVal);

        Utils.responseJson(response, json);
    }
}
