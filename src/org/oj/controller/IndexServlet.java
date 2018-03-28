package org.oj.controller;

import org.apache.ibatis.session.SqlSession;
import org.oj.database.DataSource;
import org.oj.database.TableContest;
import org.oj.database.TableDiscuss;
import org.oj.model.javaBean.ContestBean;
import org.oj.model.javaBean.DiscussBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "IndexServlet", urlPatterns = "/index")
public class IndexServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SqlSession sqlSession = DataSource.getSqlSesion();
        TableContest tableContest = sqlSession.getMapper(TableContest.class);
        List<ContestBean> contestBeanList = tableContest.getContestList(0, 5);

        TableDiscuss tableDiscuss = sqlSession.getMapper(TableDiscuss.class);
        List<DiscussBean> discussBeanList = tableDiscuss.getDiscussTitleList(null, null, null,0, 5);

        sqlSession.close();

        request.setAttribute("latestContest", contestBeanList);
        request.setAttribute("latestDiscuss", discussBeanList);
        request.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(request, response);
    }
}
