package org.oj.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * Created by xanarry on 18-1-2.
 */
@MultipartConfig
@WebServlet(name = "OnlyForTestServlet", urlPatterns = {"/test"})/*这里设置的是servlet path*/
public class OnlyForTestServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        File f = new File("./");
        for (File c: f.listFiles()) {
            System.out.println(c.getName());
        }
    }

}
