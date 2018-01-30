package org.oj.controller;

import javafx.util.Pair;
import judge.JudgeClient;
import org.apache.ibatis.session.SqlSession;
import org.oj.controller.beans.MessageBean;
import org.oj.controller.beans.PageBean;
import org.oj.controller.beans.RankBean;
import org.oj.database.*;
import org.oj.model.javaBean.*;
import utils.Consts;
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
                "/contest-add",
                "/contest-delete",
                "/contest-list",
                "/contest-edit",
                "/contest-overview",
                "/contest-detail",
                "/contest-rank",
                "/contest-register",
                "/contest-problem-edit",
                "/contest-problem-delete",
                "/contest-user-list",
                "/contest-user-delete",
                "/contest-record-list",
                "/ajax-check-contest-register"
            }
        )

public class ContestServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();

        if (uri.equals("/contest-add")) addContest(request, response, false);
        if (uri.equals("/contest-edit")) editContestPost(request, response);
        if (uri.equals("/contest-problem-edit")) editContestProblemPost(request, response);
        if (uri.equals("/contest-register"))        registerContest(request, response);
        if (uri.equals("/ajax-check-contest-register")) ajaxCheckContestRegister(request, response);

    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();

        if (uri.equals("/contest-add"))             request.getRequestDispatcher("/WEB-INF/jsp/contest/contest-edit.jsp").forward(request, response);
        if (uri.equals("/contest-problem-edit"))    editContestProblemGet(request, response);
        if (uri.equals("/contest-problem-delete"))  deleteContestProblemGet(request, response);
        if (uri.equals("/contest-delete"))          deleteContest(request, response);
        if (uri.equals("/contest-list"))            getContestList(request, response);
        if (uri.equals("/contest-edit"))            editContestGet(request, response);
        if (uri.equals("/contest-overview"))        getContestOverview(request, response);
        if (uri.equals("/contest-detail"))          getContestDetail(request, response);
        if (uri.equals("/contest-register"))        registerContest(request, response);
        if (uri.equals("/contest-rank"))            getContestRank(request, response);
        if (uri.equals("/contest-record-list"))     getContestRecordList(request, response);
        if (uri.equals("/contest-user-list"))       getContestUserList(request, response);
        if (uri.equals("/contest-user-delete"))     deleteContestUser(request, response);
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
            request.getRequestDispatcher("/WEB-INF/jsp/contest/contest-rank.jsp").forward(request, response);
        } else {
            MessageBean messageBean = new MessageBean("错误", "错误信息", "遇到不可靠参数", "/edit-contest-problem?contestID=" + strContestID, "返回");
            Utils.sendErrorMsg(response, messageBean);
        }
    }


    private void getContestRecordList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strContestID = request.getParameter("contestID");
        Integer contestID = strContestID != null && strContestID.length() > 0 ? Integer.parseInt(strContestID) : null;

        String strPage = request.getParameter("page");
        int page =  strPage != null ? Integer.parseInt(strPage) : 1;

        String userName = request.getParameter("userName");
        String strProblemID = request.getParameter("problemID");
        String result = request.getParameter("result");
        String language = request.getParameter("language");

        userName = userName != null && userName.length() > 0 ? userName : null;
        Integer problemID = strProblemID != null && strProblemID.length() > 0 ? Integer.parseInt(strProblemID) : null;
        result = result != null && result.length() > 0 ? result : null;
        language = language != null && language.length() > 0 ? language : null;

        SqlSession sqlSession = DataSource.getSqlSesion();
        ViewSubmitRecord submitRecord = sqlSession.getMapper(ViewSubmitRecord.class);
        System.out.println(contestID + " " + problemID + " " + userName + " " + result + " " + language + " " + page);
        List<ViewSubmitRecordBean> recordList = submitRecord.getSubmitRecordList(contestID, problemID, userName, result, language, (page-1)*Consts.COUNT_PER_PAGE, Consts.COUNT_PER_PAGE);
        System.out.println(recordList.size());
        for (ViewSubmitRecordBean b : recordList) {
            System.out.println(b);
        }
        //获取分页信息
        int recordCount = submitRecord.getCountOnCondition(contestID, problemID, userName, result, language);
        PageBean pageBean = Utils.getPagination(recordCount, page, request);


        TableContestProblem tableContestProblem = sqlSession.getMapper(TableContestProblem.class);
        List<ContestProblemBean> problemBeanList = tableContestProblem.getContestProblemList(contestID);

        TreeMap<Integer, String> problemIDMaper = new TreeMap<>();
        for (ContestProblemBean p : problemBeanList) {
            problemIDMaper.put(p.getProblemID(), p.getInnerID());
        }

        TableContest tableContest = sqlSession.getMapper(TableContest.class);
        ContestBean contestBean = tableContest.getContestByID(contestID);
        sqlSession.close();
        request.setAttribute("tableTitle", "比赛提交记录(" + recordCount + ")");
        request.setAttribute("contest", contestBean);
        request.setAttribute("problemIDMaper", problemIDMaper);
        request.setAttribute("recordList", recordList);
        request.setAttribute("pageInfo", pageBean);
        request.getRequestDispatcher("/WEB-INF/jsp/contest/contest-submit-record.jsp").forward(request, response);
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
            response.sendRedirect("contest-problem-edit?contestID=" + contestBean.getContestID());
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
            Utils.sendErrorMsg(response, messageBean);
        }
    }

    private void getContestList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strPage = request.getParameter("page");
        int page =  strPage != null ? Integer.parseInt(strPage) : 1;

        SqlSession sqlSession = DataSource.getSqlSesion();
        TableContest tableContest = sqlSession.getMapper(TableContest.class);

        List<ContestBean> contestBeanList = tableContest.getContestList((page - 1) * Consts.COUNT_PER_PAGE, Consts.COUNT_PER_PAGE);
        int recordCount = tableContest.getCount();

        //获取分页信息
        PageBean pageBean = Utils.getPagination(recordCount, page, request);

        sqlSession.close();
        request.setAttribute("tableTitle", "比赛列表(" + recordCount + ")");
        request.setAttribute("pageInfo", pageBean);
        request.setAttribute("contestList", contestBeanList);
        request.getRequestDispatcher("WEB-INF/jsp/contest/contest-list.jsp").forward(request, response);
    }

    private void editContestPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        addContest(request, response, true);
    }

    private void getContestOverview(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strContestID = request.getParameter("contestID");
        Integer contestID = strContestID != null && strContestID.length() > 0 ? contestID = Integer.parseInt(strContestID) : -1;

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

            HashMap<String, String> cookieMap = Utils.getCookieMap(request);

            if (cookieMap.containsKey("userID")) {
                int loginedUserID = Integer.parseInt(cookieMap.get("userID"));
                isRegistered = tableContestUser.checkUserRegistered(contestID, loginedUserID);
            }

            int userCount = tableContestUser.getContestUserCount(contestID);

            sqlSession.close();

            request.setAttribute("contest", contestBean);
            request.setAttribute("userCount", userCount);
            request.setAttribute("problemList", problemList);
            request.setAttribute("isRegistered", isRegistered);

            request.getRequestDispatcher("/WEB-INF/jsp/contest/contest-overview.jsp").forward(request, response);
        } else {
            MessageBean messageBean = new MessageBean("错误", "错误信息", "遇到不可靠参数", "/edit-contest-problem?contestID=" + strContestID, "返回");
            Utils.sendErrorMsg(response, messageBean);
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
                request.getRequestDispatcher("/WEB-INF/jsp/contest/contest-detail.jsp").forward(request, response);
                return;
        }
        Utils.sendErrorMsg(response, messageBean);
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
            request.getRequestDispatcher("/WEB-INF/jsp/contest/contest-edit.jsp").forward(request, response);
        } else {
            MessageBean messageBean = new MessageBean("错误", "错误信息", "遇到不可靠参数", "/edit-contest-problem?contestID=" + strContestID, "返回");
            Utils.sendErrorMsg(response, messageBean);
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
            response.sendRedirect("/contest-problem-edit?contestID=" + contestID);
        } else {
            MessageBean messageBean = new MessageBean("错误", "错误信息", "遇到不可靠参数", "/edit-contest-problem?contestID=" + strContestID, "返回");
            Utils.sendErrorMsg(response, messageBean);
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
            request.getRequestDispatcher("/WEB-INF/jsp/contest/contest-problem-edit.jsp").forward(request, response);
        } else {
            MessageBean messageBean = new MessageBean("错误", "错误", "该比赛不存在!", "/", "回到首页");
            Utils.sendErrorMsg(response, messageBean);
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
        contestProblemBean.setProblemID(problemID - 1000);//用户输入的是加1000后的题目号码
        contestProblemBean.setAccepted(0);
        contestProblemBean.setSubmitted(0);

        SqlSession sqlSession = DataSource.getSqlSesion();
        TableContestProblem tableContestProblem = sqlSession.getMapper(TableContestProblem.class);
        TableProblem tableProblem = sqlSession.getMapper(TableProblem.class);
        contestProblemBean.setTitle(tableProblem.getProblemByID(problemID - 1000).getTitle());//获取并设置题目标题

        tableContestProblem.addProlem(contestProblemBean);
        sqlSession.commit();
        sqlSession.close();
        response.sendRedirect("/contest-problem-edit?contestID=" + contestID);
    }

    private void registerContest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strContestID = request.getParameter("contestID");
        int contestID = strContestID != null && strContestID.length() > 0 ? Integer.parseInt(strContestID) : -1;

        HashMap<String, String> cookieMap = Utils.getCookieMap(request);

        MessageBean messageBean = new MessageBean();
        messageBean.setTitle("错误");
        messageBean.setHeader("错误信息");

        int flag = 0;
        if (contestID == -1) {
            flag++;
            messageBean.setMessage("比赛ID错误");
            messageBean.setUrl("/contest-list");
            messageBean.setLinkText("返回比赛列表");
        }

        if (!cookieMap.containsKey("userID")) {
            if (flag == 0) {
                messageBean.setMessage("请登录系统后再注册比赛");
                messageBean.setUrl("/");
                messageBean.setLinkText("返回首页");
            }
            flag++;
        }

        if (flag != 0) {
            Utils.sendErrorMsg(response, messageBean);
        } else {
            int userID = Integer.parseInt(cookieMap.get("userID"));
            SqlSession sqlSession = DataSource.getSqlSesion();
            TableContest tableContest = sqlSession.getMapper(TableContest.class);
            TableContestUser tableContestUser = sqlSession.getMapper(TableContestUser.class);

            String password = tableContest.getContestByID(contestID).getPassword();
            if (password.length() == 0) {/*开放比赛只检查是否已经报名, 防止主键重复错误*/
                if (!tableContestUser.checkUserRegistered(contestID, userID)) {
                    tableContestUser.addUser(new ContestUserBean(contestID, userID, cookieMap.get("userName")));
                    sqlSession.commit();
                    sqlSession.close();
                }
            } else {/*加密比赛检查密码*/
                String userInputPassword = request.getParameter("inputContestPassword");
                if (userInputPassword != null && !tableContestUser.checkUserRegistered(contestID, userID) && userInputPassword.equals(password)) {
                    tableContestUser.addUser(new ContestUserBean(contestID, userID, cookieMap.get("userName")));
                    sqlSession.commit();
                    sqlSession.close();
                } else {
                    messageBean.setMessage("您输入的密码不正确!");
                    messageBean.setUrl("/contest-overview?contestID=" + contestID);
                    messageBean.setLinkText("返回");
                    Utils.sendErrorMsg(response, messageBean);
                    return;
                }
            }
            response.sendRedirect("/contest-overview?contestID=" + contestID);
        }
    }



    private void getContestUserList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strContestID = request.getParameter("contestID");
        int contestID = strContestID != null && strContestID.length() > 0 ? Integer.parseInt(strContestID) : -1;
        if (contestID != -1) {
            SqlSession sqlSession = DataSource.getSqlSesion();
            TableContestUser tableContestUser = sqlSession.getMapper(TableContestUser.class);
            List<ContestUserBean> userList = tableContestUser.getContestUserList(contestID);
            TableContest tableContest = sqlSession.getMapper(TableContest.class);
            ContestBean contestBean = tableContest.getContestByID(contestID);
            sqlSession.close();
            request.setAttribute("contest", contestBean);
            request.setAttribute("userList", userList);
            request.getRequestDispatcher("/WEB-INF/jsp/contest/contest-user-list.jsp").forward(request, response);
        } else {
            MessageBean messageBean = new MessageBean("错误", "错误", "比赛ID不正确", "", "");
            Utils.sendErrorMsg(response, messageBean);
        }
    }


    private void deleteContestUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strContestID = request.getParameter("contestID");
        int contestID = strContestID != null && strContestID.length() > 0 ? Integer.parseInt(strContestID) : -1;

        String strUserID = request.getParameter("userID");
        int userID = strUserID != null && strUserID.length() > 0 ? Integer.parseInt(strUserID) : -1;

        if (contestID != -1 && userID != -1) {
            SqlSession sqlSession = DataSource.getSqlSesion();
            TableContestUser contestUser = sqlSession.getMapper(TableContestUser.class);
            contestUser.deleteUser(contestID, userID);
            sqlSession.commit();
            sqlSession.close();
            response.sendRedirect("/contest-user-list?contestID=" + contestID);
        } else {
            MessageBean messageBean = new MessageBean("错误", "错误", "比赛ID或者用户ID不正确!", "/contest-user-list?contesetID=" + contestID, "回到首页");
            Utils.sendErrorMsg(response, messageBean);
        }
    }

}
