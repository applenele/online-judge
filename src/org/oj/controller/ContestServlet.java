package org.oj.controller;

import org.apache.ibatis.session.SqlSession;
import org.oj.database.DataSource;
import org.oj.database.TableContest;
import org.oj.database.TableContestProblem;
import org.oj.database.TableProblem;
import org.oj.model.javaBean.ContestBean;
import org.oj.model.javaBean.ContestProblemBean;
import org.oj.model.javaBean.ProblemBean;

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
                "/delete-contest-problem",
                "/delete-contest",
                "/contest-list",
                "/edit-contest",
                "/contest-overview",
                "/contest-detail"
            }
        )

public class ContestServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        System.out.println("post: " + request.getRequestURL());

        if (uri.equals("/add-contest")) addContest(request, response, false);
        if (uri.equals("/edit-contest")) editContestPost(request, response);
        if (uri.equals("/edit-contest-problem")) editContestProblemPost(request, response);

    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        System.out.println("get: " + request.getRequestURL());

        if (uri.equals("/add-contest"))    request.getRequestDispatcher("/contest-edit.jsp").forward(request, response);
        if (uri.equals("/edit-contest-problem")) editContestProblemGet(request, response);
        if (uri.equals("/delete-contest-problem")) deleteContestProblemGet(request, response);
        if (uri.equals("/delete-contest")) deleteContest(request, response);
        if (uri.equals("/contest-list"))   getContestList(request, response);
        if (uri.equals("/edit-contest"))   editContestGet(request, response);
        if (uri.equals("/contest-overview")) getContestOverview(request, response);
        if (uri.equals("/contest-detail"))   getContestDetail(request, response);
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

        DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");

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
        contestBean.setOpen(password == null || password.length() == 0);
        contestBean.setSponsor(sponsor);
        contestBean.setContestType(contestType);
        contestBean.setCreateTime(new Date().getTime());

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

    private void editContestPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        addContest(request, response, true);
    }

    private void getContestOverview(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strContestID = request.getParameter("contestID");
        if (strContestID != null && strContestID.length() > 0) {
            int contestID = Integer.parseInt(strContestID);
            SqlSession sqlSession = DataSource.getSqlSesion();
            TableContest tableContest = sqlSession.getMapper(TableContest.class);
            TableContestProblem tableContestProblem = sqlSession.getMapper(TableContestProblem.class);

            ContestBean contestBean = tableContest.getContestByID(contestID);
            List<ContestProblemBean> problemList = tableContestProblem.getContestProblemList(contestID);


            sqlSession.close();

            request.setAttribute("contest", contestBean);
            request.setAttribute("problemList", problemList);

            request.getRequestDispatcher("/contest-overview.jsp").forward(request, response);
        } else {
            MessageBean messageBean = new MessageBean("错误", "错误信息", "遇到不可靠参数", "/edit-contest-problem?contestID=" + strContestID, "返回");
            Utils.sendErrorMsg(request, response, messageBean);
        }
    }

    private void getContestDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strContestID = request.getParameter("contestID");
        String curProblem = request.getParameter("curProblem");

        MessageBean messageBean = new MessageBean();
        messageBean.setTitle("错误");
        messageBean.setHeader("错误信息");
        messageBean.setMessage("不合法的参数");
        if (strContestID == null || strContestID.trim().length() == 0) {
            messageBean.setUrl("/");
            messageBean.setLinkText("返回主页");
        } else {
            if (curProblem == null || curProblem.trim().length() == 0) {
                messageBean.setUrl("/contest-overview?contestID=" + strContestID);
                messageBean.setLinkText("返回比赛");
            } else {
                SqlSession sqlSession = DataSource.getSqlSesion();
                TableContestProblem tableContestProblem = sqlSession.getMapper(TableContestProblem.class);
                TableContest tableContest = sqlSession.getMapper(TableContest.class);
                TableProblem tableProblem = sqlSession.getMapper(TableProblem.class);

                int contestID = Integer.parseInt(strContestID);
                ContestBean contestBean = tableContest.getContestByID(contestID);
                ProblemBean problemBean = null;

                List<ContestProblemBean> problemList = tableContestProblem.getContestProblemList(Integer.parseInt(strContestID));
                for (ContestProblemBean t : problemList) {
                    if (t.getInnerID().equals(curProblem)) {
                        problemBean = tableProblem.getProblemByID(t.getProblemID());
                        break;
                    }
                }

                request.setAttribute("contest", contestBean);
                request.setAttribute("problemList", problemList);
                request.setAttribute("problem", problemBean);
                request.getRequestDispatcher("/contest-detail.jsp").forward(request, response);
                return;
            }
        }
        Utils.sendErrorMsg(request, response, messageBean);
    }

    private void editContestGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strContestID = request.getParameter("contestID");
        if (strContestID != null && strContestID.length() > 0) {
            int contestID = Integer.parseInt(strContestID);
            SqlSession sqlSession = DataSource.getSqlSesion();
            TableContest tableContest = sqlSession.getMapper(TableContest.class);
            ContestBean contestBean = tableContest.getContestByID(contestID);
            sqlSession.close();

            request.setAttribute("contest", contestBean);
            request.getRequestDispatcher("/contest-edit.jsp").forward(request, response);
        } else {
            MessageBean messageBean = new MessageBean("错误", "错误信息", "遇到不可靠参数", "/edit-contest-problem?contestID=" + strContestID, "返回");
            Utils.sendErrorMsg(request, response, messageBean);
        }
    }

    private void deleteContestProblemGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strContestID = request.getParameter("contestID");
        String innerID   = request.getParameter("innerID");
        if (strContestID != null && strContestID.length() > 0) {
            int contestID = Integer.parseInt(strContestID);
            SqlSession sqlSession = DataSource.getSqlSesion();
            TableContestProblem tableContestProblem = sqlSession.getMapper(TableContestProblem.class);
            tableContestProblem.deleteProblem(contestID, innerID);
            sqlSession.commit();
            sqlSession.close();
            response.sendRedirect("/edit-contest-problem?contestID=" + contestID);
        } else {
            MessageBean messageBean = new MessageBean("错误", "错误信息", "遇到不可靠参数", "/edit-contest-problem?contestID=" + strContestID, "返回");
            Utils.sendErrorMsg(request, response, messageBean);
        }
    }
    private void editContestProblemGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strContestID = request.getParameter("contestID");
        if (strContestID != null && strContestID.length() > 0) {
            int contestID = Integer.parseInt(strContestID);
            SqlSession sqlSession = DataSource.getSqlSesion();
            TableContestProblem tableContestProblem = sqlSession.getMapper(TableContestProblem.class);
            TableContest tableContest = sqlSession.getMapper(TableContest.class);

            tableContestProblem.getContestProblemList(contestID);
            ContestBean contestBean = tableContest.getContestByID(contestID);


            request.setAttribute("problemList", tableContestProblem.getContestProblemList(contestID));
            request.setAttribute("contest", contestBean);
            sqlSession.close();
            request.getRequestDispatcher("/contest-problem-edit.jsp").forward(request, response);
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
        contestProblemBean.setInnerID(innerID.trim().toUpperCase());
        contestProblemBean.setProblemID(problemID - 1000);
        contestProblemBean.setAccepted(0);
        contestProblemBean.setSubmitted(0);

        SqlSession sqlSession = DataSource.getSqlSesion();
        TableContestProblem tableContestProblem = sqlSession.getMapper(TableContestProblem.class);
        tableContestProblem.addProlem(contestProblemBean);
        sqlSession.commit();
        sqlSession.close();
        response.sendRedirect("/edit-contest-problem?contestID=" + contestID);
    }
}
