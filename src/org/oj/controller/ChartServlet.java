package org.oj.controller;

import org.apache.ibatis.session.SqlSession;
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

@WebServlet(name = "ChartServlet", urlPatterns = "/chart")
public class ChartServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        if (uri.equals("/chart")) getChart(request, response);
    }


    private void getChart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strPage = request.getParameter("page");
        int page = strPage != null && strPage.length() > 0 ? Integer.parseInt(strPage) : 1;

        SqlSession sqlSession = DataSource.getSqlSesion();
        TableUser tableUser = sqlSession.getMapper(TableUser.class);
        List<UserBean> userList = tableUser.getChart((page - 1) * 100, 100);
        request.setAttribute("userList", userList);
        request.getRequestDispatcher("/chart.jsp").forward(request, response);
    }
}
