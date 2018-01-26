package org.oj.controller;


import email.SendMail;
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



@WebServlet(name = "AdminServlet",
        urlPatterns = {
                "/admin",
                "/send-email"
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
    }



    private void sendMailPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String emailAddress = request.getParameter("inputEmailAddress");
        String content = request.getParameter("inputContent");

        MessageBean messageBean;

        System.out.println(emailAddress + " subject " + content);
        if (SendMail.sendMail(emailAddress, "oj系统邮件", content)) {
            messageBean = new MessageBean("消息", "消息", "邮件发送成功!", "", "");
        } else {
            messageBean = new MessageBean("消息", "消息", "邮件发送失败!", "", "");
        }
        Utils.sendErrorMsg(response, messageBean);
    }

    private void sendMailGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

    private void asd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        System.out.println("get: " + request.getRequestURL());

        if (uri.equals("/send-mail")) request.getRequestDispatcher("/WEB-INF/jsp/admin/send-email.jsp").forward(request, response);
        if (uri.equals("/admin")) request.getRequestDispatcher("/WEB-INF/jsp/admin/admin.jsp").forward(request, response);
    }
}
