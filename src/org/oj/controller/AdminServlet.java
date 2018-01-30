package org.oj.controller;


import email.SendMail;
import judge.beans.ConfigurationBean;
import org.apache.ibatis.session.SqlSession;
import org.oj.controller.beans.MessageBean;
import org.oj.database.DataSource;
import org.oj.database.TableUser;
import org.oj.model.javaBean.UserBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@WebServlet(name = "AdminServlet",
        urlPatterns = {
                "/admin",
                "/send-email",
                "/configuration"
            }
        )
public class AdminServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        System.out.println("get: " + request.getRequestURL());


        if (uri.equals("/send-email")) sendMailPost(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        System.out.println("get: " + request.getRequestURL());

        if (uri.equals("/send-email")) sendMailGet(request, response);
        if (uri.equals("/admin")) request.getRequestDispatcher("/WEB-INF/jsp/admin/admin.jsp").forward(request, response);
        if (uri.equals("/configuration")) configurationGet(request, response);
    }



    private void sendMailPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strEmailAddress = request.getParameter("inputEmailAddress");
        strEmailAddress = strEmailAddress.replace("[", "").replace("]", "");
        String[] emailAddresses = strEmailAddress.replace("[", "").replace("]", "").split(",");
        String content = request.getParameter("inputContent");
        String subject = request.getParameter("inputSubject");

        MessageBean messageBean;

        System.out.println(emailAddresses.toString() + " subject " + content);
        if (SendMail.sendMail(emailAddresses, subject, content)) {
            messageBean = new MessageBean("消息", "消息", "邮件发送成功!", "", "");
        } else {
            messageBean = new MessageBean("消息", "消息", "邮件发送失败!", "", "");
        }
        Utils.sendErrorMsg(response, messageBean);
    }

    private void sendMailGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String toAll = request.getParameter("toAll");
        if (toAll != null) {
            SqlSession sqlSession = DataSource.getSqlSesion();
            TableUser tableUser = sqlSession.getMapper(TableUser.class);
            List<String> emailList = tableUser.getUserEmailList();
            sqlSession.close();

            request.setAttribute("emailList", emailList);
            request.getRequestDispatcher("/WEB-INF/jsp/admin/send-email.jsp").forward(request, response);
        } else {
            String strUserID = request.getParameter("userID");
            Integer userID = strUserID != null && strUserID.length() > 0 ? Integer.parseInt(strUserID) : null;

            if (userID != null) {
                SqlSession sqlSession = DataSource.getSqlSesion();
                TableUser tableUser = sqlSession.getMapper(TableUser.class);
                UserBean userBean = tableUser.getUserByID(userID);

                sqlSession.close();

                request.setAttribute("user", userBean);
                request.getRequestDispatcher("/WEB-INF/jsp/admin/send-email.jsp").forward(request, response);
            } else {
                MessageBean messageBean = new MessageBean("消息", "消息", "邮件发送成功!", "", "");
                Utils.sendErrorMsg(response, messageBean);
            }
        }
    }

    private void configurationGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ConfigurationBean configuration = (ConfigurationBean) getServletContext().getAttribute("configuration");
        request.setAttribute("configuration", configuration);
        request.getRequestDispatcher("/WEB-INF/jsp/admin/configuration.jsp").forward(request, response);
    }
}
