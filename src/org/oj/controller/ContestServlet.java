package org.oj.controller;

import org.apache.ibatis.session.SqlSession;
import org.oj.database.DataSource;
import org.oj.database.TableContest;
import org.oj.database.TableContestProblem;
import org.oj.model.javaBean.ContestBean;
import org.oj.model.javaBean.ContestProblemBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet(
        name = "ContestServlet",
        urlPatterns = {
                "/add-contest",
                "/edit-contest-problem",
                "/delete-contest",
                "/contest-list",
                "/edit-contest"
            }
        )

public class ContestServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        System.out.println("get: " + request.getRequestURL());

        if (uri.equals("/add-contest")) addContest(request, response, false);
        if (uri.equals("/edit-contest-problem")) editContestProblemPost(request, response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        System.out.println("get: " + request.getRequestURL());

        if (uri.equals("/add-contest"))    request.getRequestDispatcher("/edit-contest.jsp").forward(request, response);
        if (uri.equals("/edit-contest-problem")) editContestProblemGet(request, response);
        if (uri.equals("/delete-contest")) deleteContest(request, response);
        if (uri.equals("/edit-contest"))   editContest(request, response);
        if (uri.equals("/contest-list"))   getContestList(request, response);
    }


    private void addContest(HttpServletRequest request, HttpServletResponse response, boolean isUpdate) throws ServletException, IOException {
        String title = request.getParameter("inputTitle");
        String strStartTime = request.getParameter("inputStartTime");
        String strEndTime = request.getParameter("inputEndTime");
        String strRegisterStartTime = request.getParameter("inputRegisterStartTime");
        String strRegisterEndTime = request.getParameter("inputRegisterEndTime");
        String password = request.getParameter("inputContestPassword");
        String contestType = request.getParameter("inputContestType");
        String sponsor = request.getParameter("inputSponsor");
        String desc = request.getParameter("inputContestDesc");

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        long startTime = 0, endTime = 0;
        long registerStartTime = 0, registerEndTime = 0;
        try {
             startTime = formatter.parse(strStartTime).getTime();
             endTime = formatter.parse(strEndTime).getTime();
             registerStartTime = formatter.parse(strRegisterStartTime).getTime();
             registerEndTime = formatter.parse(strRegisterEndTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ContestBean contestBean = new ContestBean();
        contestBean.setTitle(title);
        contestBean.setDesc(desc);
        contestBean.setStartTime(startTime);
        contestBean.setEndTime(endTime);
        contestBean.setRegisterStartTime(registerStartTime);
        contestBean.setRegisterEndTime(registerEndTime);
        contestBean.setPassword(password);
        contestBean.setPublic(password == null || password.length() == 0);
        contestBean.setSponsor(sponsor);
        contestBean.setContestType(contestType);
        contestBean.setCreateTime(new Date().getTime());

        System.out.println(contestBean);

        SqlSession sqlSession = DataSource.getSqlSesion();
        TableContest tableContest = sqlSession.getMapper(TableContest.class);

        if (isUpdate) {
            tableContest.updateContest(contestBean);
        } else {
            tableContest.addContest(contestBean);
        }

        sqlSession.commit();
        sqlSession.close();

        response.sendRedirect("edit-contest-problem?contestID=" + contestBean.getContestID());
    }

    private void deleteContest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strContestID = request.getParameter("contestID");
        if (strContestID != null && strContestID.length() > 0) {
            int contestID = Integer.parseInt(strContestID);
            SqlSession sqlSession = DataSource.getSqlSesion();
            TableContest tableContest = sqlSession.getMapper(TableContest.class);
            tableContest.deleteContest(contestID);
            sqlSession.commit();
            sqlSession.close();
            response.sendRedirect("/contest-list");
        } else {
            MessageBean messageBean = new MessageBean("错误", "错误", "该比赛不存在!", "/", "回到首页");
            Utils.sendErrorMsg(request, response, messageBean);
        }
    }

    private void getContestList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strPage = request.getParameter("page");
        int page = 1;
        if (strPage != null && strPage.length() > 0) {
            page = Integer.parseInt(strPage);
        }

        SqlSession sqlSession = DataSource.getSqlSesion();
        TableContest tableContest = sqlSession.getMapper(TableContest.class);
        List<ContestBean> contestBeanList = tableContest.getContestList((page - 1) * 100, 100);
        sqlSession.close();
        request.setAttribute("contestList", contestBeanList);
        request.getRequestDispatcher("/contest-list.jsp").forward(request, response);
    }

    private void editContest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        addContest(request, response, true);
    }

    private void editContestProblemGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strContestID = request.getParameter("contestID");
        if (strContestID != null && strContestID.length() > 0) {
            int contestID = Integer.parseInt(strContestID);
            SqlSession sqlSession = DataSource.getSqlSesion();
            TableContestProblem tableContestProblem = sqlSession.getMapper(TableContestProblem.class);
            tableContestProblem.getContestProblemList(contestID);
            request.setAttribute("problemList", tableContestProblem.getContestProblemList(contestID));
            sqlSession.close();
            request.getRequestDispatcher("/contest-problem.jsp").forward(request, response);
        } else {
            MessageBean messageBean = new MessageBean("错误", "错误", "该比赛不存在!", "/", "回到首页");
            Utils.sendErrorMsg(request, response, messageBean);
        }

    }

    private void editContestProblemPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //添加题目的验证有js完成, 此处不做验证
        String strContestID = request.getParameter("inputContestID");
        String innerID = request.getParameter("inputInnerID");
        String strProblemID = request.getParameter("inputProblemID");

        int contestID = Integer.parseInt(strContestID);
        int problemID = Integer.parseInt(strProblemID);

        ContestProblemBean contestProblemBean = new ContestProblemBean();
        contestProblemBean.setContestID(contestID);
        contestProblemBean.setInnerID(innerID);
        contestProblemBean.setProblemID(problemID);
        contestProblemBean.setAccepted(0);
        contestProblemBean.setSubmited(0);

        SqlSession sqlSession = DataSource.getSqlSesion();
        TableContestProblem tableContestProblem = sqlSession.getMapper(TableContestProblem.class);
        tableContestProblem.addProlem(contestProblemBean);
        sqlSession.commit();
        sqlSession.close();
        response.sendRedirect("/edit-contest-problem?contestID=" + contestID);
    }
}
