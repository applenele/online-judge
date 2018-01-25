package org.oj.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by xanarry on 18-1-2.
 */
@MultipartConfig
@WebServlet(name = "OnlyForTestServlet", urlPatterns = {"/test/*"})/*这里设置的是servlet path*/
public class OnlyForTestServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println(request.getRequestURL());// http://localhost:8080/test/user/abc/zy
        System.out.println(request.getRequestURI());// /test/user/abc/zy

        System.out.println(request.getContextPath());

        System.out.println(request.getServletPath());// /user

        System.out.println(request.getPathInfo());// /user/abc/zy

        System.out.println(request.getParameterMap());// {p=[Ljava.lang.String;@13dc4f19}
    }

}
