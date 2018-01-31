package org.oj.controller;

import org.oj.controller.beans.MessageBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by xanarry on 18-1-2.
 */
@WebServlet(name = "messageServlet", urlPatterns = "/message")
public class messageServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println(request.getRequestURL());
        String title = request.getParameter("title");
        String header = request.getParameter("header");
        String message = request.getParameter("message");
        String url = request.getParameter("url");
        String linkText = request.getParameter("linkText");

        MessageBean messageBean = new MessageBean(title, header, message, url, linkText);
        System.out.println(messageBean.toString());
        request.setAttribute("msg", messageBean);
        request.getRequestDispatcher("WEB-INF/jsp/information.jsp").forward(request, response);
    }
}
