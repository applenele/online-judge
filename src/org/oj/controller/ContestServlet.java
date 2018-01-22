package org.oj.controller;

import javafx.util.Pair;
import judge.JudgeClient;
import org.apache.ibatis.session.SqlSession;
import org.oj.controller.beans.MessageBean;
import org.oj.controller.beans.RankBean;
import org.oj.database.*;
import org.oj.model.javaBean.*;
import utils.Tools;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
                "/contest-detail",
                "/register-contest",
                "/contest-rank",
                "/ajax-check-contest-register"
            }
        )

public class ContestServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        System.out.println("post: " + request.getRequestURL());

        if (uri.equals("/add-contest")) addContest(request, response, false);
        if (uri.equals("/edit-contest")) editContestPost(request, response);
        if (uri.equals("/edit-contest-problem")) editContestProblemPost(request, response);
        if (uri.equals("/ajax-check-contest-register")) ajaxCheckContestRegister(request, response);

    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        System.out.println("get: " + request.getRequestURL());

        if (uri.equals("/add-contest"))             request.getRequestDispatcher("/contest-edit.jsp").forward(request, response);
        if (uri.equals("/edit-contest-problem"))    editContestProblemGet(request, response);
        if (uri.equals("/delete-contest-problem"))  deleteContestProblemGet(request, response);
        if (uri.equals("/delete-contest"))          deleteContest(request, response);
        if (uri.equals("/contest-list"))            getContestList(request, response);
        if (uri.equals("/edit-contest"))            editContestGet(request, response);
        if (uri.equals("/contest-overview"))        getContestOverview(request, response);
        if (uri.equals("/contest-detail"))          getContestDetail(request, response);
        if (uri.equals("/register-contest"))        registerContest(request, response);
        if (uri.equals("/contest-rank"))            getContestRank(request, response);
    }


    private void getContestRank(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strContestID = request.getParameter("contestID");
        int contestID = -1;
        if (strContestID != null && strContestID.length() > 0) {
            contestID = Integer.parseInt(strContestID);
        }

        if (contestID > 0) {
            SqlSession sqlSession = DataSource.getSqlSesion();
            TableContest tableContest = sqlSession.getMapper(TableContest.class);
            TableContestUser tableContestUser = sqlSession.getMapper(TableContestUser.class);
            TableContestProblem tableContestProblem = sqlSession.getMapper(TableContestProblem.class);
            ViewSubmitRecord viewSubmitRecord = sqlSession.getMapper(ViewSubmitRecord.class);

            ContestBean contestBean = tableContest.getContestByID(contestID);
            List<ContestUserBean> users = tableContestUser.getContestUserList(contestID);
            List<ContestProblemBean> problems = tableContestProblem.getContestProblemList(contestID);
            List<ViewSubmitRecordBean> submits = viewSubmitRecord.getSubmitRecordListOrderedByDate(contestID,0,100000);

            sqlSession.close();

            List<RankBean> rankList = Tools.calculateRank(contestBean, users, problems, submits);

            List<ContestProblemBean> problemOverview = Tools.getContestProblemStatistic(submits, problems);
            problemOverview.sort(new Comparator<ContestProblemBean>() {
                @Override
                public int compare(ContestProblemBean o1, ContestProblemBean o2) {
                    return o1.getInnerID().compareTo(o2.getInnerID());
                }
            });

            request.setAttribute("contest", contestBean);
            request.setAttribute("problemOverview", problemOverview);
            request.setAttribute("rankList", rankList);
            request.getRequestDispatcher("/contest-rank.jsp").forward(request, response);
        } else {
            MessageBean messageBean = new MessageBean("错误", "错误信息", "遇到不可靠参数", "/edit-contest-problem?contestID=" + strContestID, "返回");
            Utils.sendErrorMsg(request, response, messageBean);
        }
    }



    private void ajaxCheckContestRegister(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strContestID = request.getParameter("inputContestID");
        System.out.println(strContestID);

        String jsonPatter = "{\"contestID\" : %s, \"userID\" :%s, \"registered\": %s}";
        String json = "";
        if (strContestID != null && strContestID.length() > 0) {
            int contestID = Integer.parseInt(strContestID);
            //{"contestID" : %s, "userID" :%s, "registered": %s}
            int userID = -1;
            for (Cookie c : request.getCookies()) {
                if (c.getName().equals("userID")) {
                    userID = Integer.parseInt(c.getValue());
                    break;
                }
            }
            SqlSession sqlSession = DataSource.getSqlSesion();
            TableContestUser tableContestUser = sqlSession.getMapper(TableContestUser.class);
            boolean registered = tableContestUser.checkUserRegistered(contestID, userID);
            System.out.println("check result contestregisterd: " + registered);
            sqlSession.close();
            json = String.format(jsonPatter, contestID, userID, registered);
        } else {
            json = String.format(jsonPatter, -1, -1, false);
        }
        Utils.responseJson(response, json);
    }

    private void addContest(HttpServletRequest request, HttpServletResponse response, boolean isUpdate) throws ServletException, IOException {
        Integer contestID = null;
        if (isUpdate) {
            String strContestID = request.getParameter("inputContestID");
            contestID = Integer.parseInt(strContestID);
        }

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

        for (Cookie c : request.getCookies()) {
            if (c.getName().equals("userName")) {
                sponsor = c.getValue();
            }
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
            contestBean.setContestID(contestID);
            tableContest.updateContest(contestBean);
            response.sendRedirect("contest-overview?contestID=" + contestBean.getContestID());

        } else {
            tableContest.addContest(contestBean);
            response.sendRedirect("edit-contest-problem?contestID=" + contestBean.getContestID());
        }
        sqlSession.commit();
        sqlSession.close();
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
        int contestID = -1;

        if (strContestID != null && strContestID.length() > 0) {
            contestID = Integer.parseInt(strContestID);
        }

        if (contestID > 0) {
            SqlSession sqlSession = DataSource.getSqlSesion();
            TableContest tableContest = sqlSession.getMapper(TableContest.class);
            TableContestProblem tableContestProblem = sqlSession.getMapper(TableContestProblem.class);
            TableContestUser tableContestUser = sqlSession.getMapper(TableContestUser.class);

            ContestBean contestBean = tableContest.getContestByID(contestID);
            List<ContestProblemBean> problemList = tableContestProblem.getContestProblemList(contestID);


            /*
             *Cookie userIDCookie = new Cookie("userID", userBean.getUserID() + "");
             *Cookie userNameCookie = new Cookie("userName", userBean.getUserName());
             */
            String strLoginedUserID = null;
            boolean isRegistered = false;

            for (Cookie c : request.getCookies()) {
                if (c.getName().equals("userID")) {
                    strLoginedUserID = c.getValue();
                    break;
                }
            }

            if (strLoginedUserID != null) {
                int loginedUserID = Integer.parseInt(strLoginedUserID);
                System.out.println("check user registered: " + loginedUserID);
                isRegistered = tableContestUser.checkUserRegistered(contestID, loginedUserID);
                System.out.println("check result: " + isRegistered);
            } else {
                MessageBean messageBean = new MessageBean();
                messageBean.setTitle("错误");
                messageBean.setHeader("错误信息");
                messageBean.setMessage("请登录系统后再注册比赛");
                messageBean.setUrl("/");
                messageBean.setLinkText("/返回首页");

                sqlSession.close();

                Utils.sendErrorMsg(request, response, messageBean);
                return;
            }


            sqlSession.close();

            request.setAttribute("contest", contestBean);
            request.setAttribute("problemList", problemList);
            request.setAttribute("isRegistered", isRegistered);

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
                SqlSession sqlSession = DataSource.getSqlSesion();
                TableContestProblem tableContestProblem = sqlSession.getMapper(TableContestProblem.class);
                TableContest tableContest = sqlSession.getMapper(TableContest.class);
                TableProblem tableProblem = sqlSession.getMapper(TableProblem.class);
                TableLanguage tableLanguage = sqlSession.getMapper(TableLanguage.class);

                int contestID = Integer.parseInt(strContestID);
                ContestBean contestBean = tableContest.getContestByID(contestID);
                ProblemBean problemBean = null;

                List<ContestProblemBean> problemList = tableContestProblem.getContestProblemList(Integer.parseInt(strContestID));
                if (curProblem != null) {
                    for (ContestProblemBean t : problemList) {
                        if (t.getInnerID().equals(curProblem)) {
                            problemBean = tableProblem.getProblemByID(t.getProblemID());
                            break;
                        }
                    }
                }

                if (problemBean == null) {//如果上面查找指定题目ID失败, 那么查找题目列表中的第一个
                    problemBean = tableProblem.getProblemByID(problemList.get(0).getProblemID());
                }
                List<LanguageBean> languages = tableLanguage.getLanguageList();

                sqlSession.close();

                request.setAttribute("contest", contestBean);
                request.setAttribute("problemList", problemList);
                request.setAttribute("problem", problemBean);
                request.setAttribute("languages", languages);
                request.getRequestDispatcher("/contest-detail.jsp").forward(request, response);
                return;
        }
        Utils.sendErrorMsg(request, response, messageBean);
    }

    private void editContestGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

    private void registerContest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strContestID = request.getParameter("contestID");
        int contestID = -1;
        if (strContestID != null && strContestID.length() > 0) {
            contestID = Integer.parseInt(strContestID);
        }

        String strLoginedUserID = null;
        for (Cookie c : request.getCookies()) {
            if (c.getName().equals("userID")) {
                strLoginedUserID = c.getValue();
                break;
            }
        }

        if (contestID != -1 || strLoginedUserID != null) {
            int userID = Integer.parseInt(strLoginedUserID);
            SqlSession sqlSession = DataSource.getSqlSesion();
            TableContestUser tableContestUser = sqlSession.getMapper(TableContestUser.class);
            if (!tableContestUser.checkUserRegistered(contestID, userID)) {
                tableContestUser.addUser(new ContestUserBean(contestID, userID));
                sqlSession.commit();
                sqlSession.close();
            }

            response.sendRedirect("/contest-overview?contestID=" + contestID);
        } else {
            MessageBean messageBean = new MessageBean();
            messageBean.setTitle("错误");
            messageBean.setHeader("错误信息");
            if (contestID == -1) {
                messageBean.setMessage("比赛参数错误");
            } else {
                messageBean.setMessage("请登录系统后再注册比赛");
            }
            messageBean.setUrl("/");
            messageBean.setLinkText("/返回首页");
            Utils.sendErrorMsg(request, response, messageBean);
        }
    }
}
