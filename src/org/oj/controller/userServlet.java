package org.oj.controller;

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
import java.util.List;

import static org.apache.commons.codec.digest.DigestUtils.sha1Hex;

/**
 * Created by xanarry on 18-1-2.
 */
@WebServlet(name = "userServlet",
        urlPatterns = {
                "/user",
                "/delete-user",
                "/edit-user",
                "/retrieve-password"
            }
        )
public class userServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("post " + request.getRequestURL());
        if (request.getRequestURI().equals("/edit-user"))    editUser(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("get " + request.getRequestURL());
        if (request.getRequestURI().equals("/delete-user"))  deleteUser(request, response);
        if (request.getRequestURI().equals("/user"))         showUser(request, response);
        if (request.getRequestURI().equals("/retrieve-password")) retrievePasswordGet(request, response);
    }


    private void showUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
                request.getRequestDispatcher("/user.jsp").forward(request, response);
            } else {
                MessageBean messageBean = new MessageBean("错误", "错误", "用户ID不存在!", "/", "回到首页");
                Utils.sendErrorMsg(request, response, messageBean);
            }
        } else {
            MessageBean messageBean = new MessageBean("错误", "错误", "用户ID不存在!", "/", "回到首页");
            Utils.sendErrorMsg(request, response, messageBean);
        }
    }


    private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strUserID = request.getParameter("userID");
        if (strUserID != null) {
            Integer userID = Integer.parseInt(strUserID);

            SqlSession sqlSession = DataSource.getSqlSesion();
            TableUser user = sqlSession.getMapper(TableUser.class);
            UserBean userBean = user.getUserByID(userID);
            String userName = userBean.getUserName();
            user.deleteUserById(userID);
            sqlSession.commit();
            sqlSession.close();

            MessageBean messageBean = new MessageBean("提示", "提示", userName + " 已经被删除!", "/", "回到首页");
            Utils.sendErrorMsg(request, response, messageBean);
        }

    }

    private void editUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        System.out.println(json);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }


    private void retrievePasswordGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
