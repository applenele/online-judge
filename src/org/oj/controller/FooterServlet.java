package org.oj.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "FooterServlet",
            urlPatterns = {
                    "/faq",
                    "/about",
                    "/contact-me",
                    "/judge-server"
            }
        )
public class FooterServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        if (uri.equals("/faq"))  request.getRequestDispatcher("/WEB-INF/jsp/faq.jsp").forward(request, response);
        if (uri.equals("/about")) request.getRequestDispatcher("/WEB-INF/jsp/about.jsp").forward(request, response);
        if (uri.equals("/contact-me")) request.getRequestDispatcher("/WEB-INF/jsp/contact-me.jsp").forward(request, response);
        if (uri.equals("/judge-server")) request.getRequestDispatcher("/WEB-INF/jsp/judge-server.jsp").forward(request, response);
    }
}
