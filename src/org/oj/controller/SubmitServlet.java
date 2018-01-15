package org.oj.controller;

import judge.JudgeClient;
import org.apache.ibatis.session.SqlSession;
import org.oj.database.*;
import org.oj.model.javaBean.*;
import utils.ConstStrings;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

/**
 * Created by xanarry on 18-1-7.
 */
@WebServlet(
        name = "SubmitServlet",
        urlPatterns = {
                "/submit",
                "/record-list",
                "/record-detail"
        }
)
public class SubmitServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("post: " + request.getRequestURL());

        if (request.getRequestURI().equals("/submit")) postSubmit(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("get: " + request.getRequestURL());

        if (request.getRequestURI().equals("/submit")) getSubmit(request, response);
        if (request.getRequestURI().equals("/record-list")) getRecordList(request, response);
        if (request.getRequestURI().equals("/record-detail")) getRecordDetail(request, response);
    }


    private void getSubmit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strProblemID = request.getParameter("problemID");
        Integer problemID = Integer.parseInt(strProblemID);


        //检查用户是否登录, 没有登录重定向到错误页面
        String strUserID = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie c : cookies) {
            if (c.getName().equals("userID")) {
                strUserID = c.getValue();
            }
        }
        if (strUserID == null) {
            MessageBean messageBean = new MessageBean("提示", "提示", "请登录再提交代码", "/", "回到首页");
            response.sendRedirect("/message?" +
                    "title=" + URLEncoder.encode(messageBean.getTitle(), "utf8") +
                    "&header=" + URLEncoder.encode(messageBean.getHeader(), "utf8") +
                    "&message=" + URLEncoder.encode(messageBean.getMessage(), "utf8") +
                    "&url=" + URLEncoder.encode(messageBean.getUrl(), "utf8") +
                    "&linkText=" + URLEncoder.encode(messageBean.getLinkText(), "utf8"));
            return;
        }

        int userID = Integer.parseInt(strUserID);

        SqlSession sqlSession = Database.getSqlSesion();
        //获取提交题目信息
        Problem problem = sqlSession.getMapper(Problem.class);
        ProblemBean problemBean = problem.getProblemByID(problemID);

        //获取系统语言列表以供选择
        Language language = sqlSession.getMapper(Language.class);
        List<LanguageBean> languages = language.getLanguageList();

        //获取提交代码的用户信息
        User user = sqlSession.getMapper(User.class);
        UserBean userBean = user.getUserByID(userID);


        //获取用户在之前对本题目的提交记录
        SubmitRecord submitRecord = sqlSession.getMapper(SubmitRecord.class);
        List<SubmitRecordBean> submitRecordBeans = submitRecord.getSubmitRecordListByUserID(userID, 0, 3);

        sqlSession.close();

        request.setAttribute("problem", problemBean);
        request.setAttribute("languages", languages);
        request.setAttribute("user", userBean);
        request.setAttribute("recordList", submitRecordBeans);


        request.getRequestDispatcher("submit.jsp").forward(request, response);
    }


    private void postSubmit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strProblemID = request.getParameter("inputProblemID");
        String code = request.getParameter("inputCode");
        String language = request.getParameter("inputLanguage");

        String strUserID = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie c : cookies) {
            if (c.getName().equals("userID")) {
                strUserID = c.getValue();
            }
        }

        if (strUserID == null) {
            MessageBean messageBean = new MessageBean("提示", "提示", "请登录再提交代码", "/", "回到首页");
            response.sendRedirect("/message?" +
                    "title=" + URLEncoder.encode(messageBean.getTitle(), "utf8") +
                    "&header=" + URLEncoder.encode(messageBean.getHeader(), "utf8") +
                    "&message=" + URLEncoder.encode(messageBean.getMessage(), "utf8") +
                    "&url=" + URLEncoder.encode(messageBean.getUrl(), "utf8") +
                    "&linkText=" + URLEncoder.encode(messageBean.getLinkText(), "utf8"));
            return;
        }


        int problemID = Integer.parseInt(strProblemID);
        int userID = Integer.parseInt(strUserID);


        SubmitRecordBean submitRecordBean = new SubmitRecordBean();//生成提交记录
        submitRecordBean.setProblemID(problemID);
        submitRecordBean.setUserID(userID);
        submitRecordBean.setContestID(0);//非比赛提交的代码统一设置为0
        submitRecordBean.setResult(ConstStrings.result[0]);
        submitRecordBean.setLanguage(language);
        submitRecordBean.setSourceCode(code);
        submitRecordBean.setCodeLength(code.length());
        submitRecordBean.setSubmitTime(new Date().getTime());


        SqlSession sqlSession = Database.getSqlSesion();
        Problem problem = sqlSession.getMapper(Problem.class);
        ProblemBean problemBean = problem.getProblemByID(problemID); //获取提交代码所属的题目

        //获取测试点路径
        String testPointDataPath = getServletContext().getRealPath("/test-points") + "/p" + (1000 + problemID);

        //提交数据库
        SubmitRecord submitRecord = sqlSession.getMapper(SubmitRecord.class);
        submitRecord.addSubmitRecord(submitRecordBean);
        sqlSession.commit();
        sqlSession.close();


        //提交代码, 任何与提交代码到评测机的相关的代码都必须在记录写入数据库之后, 后续状态与结果的更新由,judge client完成
        //网页需要手动刷新才能看到更新
        System.out.println("submit to judge client");
        JudgeClient client = (JudgeClient) getServletContext().getAttribute("judgeClient");
        //client.getState()
        client.submit(submitRecordBean, problemBean, testPointDataPath);
        System.out.println("redirect to record list");
        response.sendRedirect("/record-list");
    }


    private void getRecordList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SqlSession sqlSession = Database.getSqlSesion();
        SubmitRecord submitRecord = sqlSession.getMapper(SubmitRecord.class);
        List<SubmitRecordBean> submitRecordBeans = submitRecord.getSubmitRecordListOrderedByDate(0, 100);
        sqlSession.close();

        request.setAttribute("recordList", submitRecordBeans);
        request.getRequestDispatcher("/record-list.jsp").forward(request, response);
    }


    private void getRecordDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strSubmitID = request.getParameter("submitID");
        if (strSubmitID == null) {
            //send error page
            return;
        }

        Integer submitID = Integer.parseInt(strSubmitID);

        SqlSession sqlSession = Database.getSqlSesion();
        JudgeDetail judgeDetail = sqlSession.getMapper(JudgeDetail.class);
        List<JudgeDetailBean> judgeDetailList = judgeDetail.getJudegeDetailBySubmitID(submitID);

        SubmitRecord submitRecord = sqlSession.getMapper(SubmitRecord.class);
        SubmitRecordBean submitRecordBean = submitRecord.getSubmitRecordByID(submitID);

        sqlSession.close();

        request.setAttribute("detailList", judgeDetailList);
        request.setAttribute("record", submitRecordBean);
        request.getRequestDispatcher("/record-detail.jsp").forward(request, response);
    }
}