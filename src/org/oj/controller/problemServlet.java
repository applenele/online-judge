package org.oj.controller;

import judge.JudgeClient;
import judge.beans.ConfigurationBean;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.session.SqlSession;
import org.oj.controller.beans.MessageBean;
import org.oj.controller.beans.PageBean;
import org.oj.database.DataSource;
import org.oj.database.TableProblem;
import org.oj.model.javaBean.ProblemBean;
import org.oj.model.javaBean.UserBean;
import utils.Consts;
import utils.Tools;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xanarry on 18-1-4.
 */
@WebServlet(
        name = "problemServlet",
        urlPatterns = {
                "/problem-add",
                "/problem-edit",
                "/problem-delete",
                "/ajax-check-problem-exist",
                "/problem-list",
                "/problem-search",
                "/problem"
        }
)

public class problemServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("post: " + request.getRequestURL());
        String uri = request.getRequestURI();
        if (uri.equals("/problem-add")) addProblem(request, response);
        if (uri.equals("/ajax-check-problem-exist")) checkProblemExist(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("get: " + request.getRequestURL());
        String uri = request.getRequestURI();
        if (uri.equals("/problem-add")) request.getRequestDispatcher("/WEB-INF/jsp/problem/problem-edit.jsp").forward(request, response);

        if (uri.equals("/problem-edit")) {
            String strProblemID = request.getParameter("problemID");
            if (strProblemID != null) {
                int problemID = Integer.parseInt(strProblemID);
                SqlSession sqlSession = DataSource.getSqlSesion();
                TableProblem tableProblem = sqlSession.getMapper(TableProblem.class);
                ProblemBean problemBean = tableProblem.getProblemByID(problemID);
                request.setAttribute("problem", problemBean);
                sqlSession.close();
                request.getRequestDispatcher("/WEB-INF/jsp/problem/problem-edit.jsp").forward(request, response);
            } else {
                response.sendRedirect("/problem-list");
            }

        }

        if (uri.equals("/problem")) showProblem(request, response);
        if (uri.equals("/problem-list")) getProblems(request, response);
        if (uri.equals("/problem-delete")) problemDeleteGet(request, response);
        if (uri.equals("/problem-search")) searchProblem(request, response);
    }

    private void checkProblemExist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strProblemID = request.getParameter("problemID");

        String jsonPattern = "{\"problemID\": %s, \"exist\": %s}";
        String json = "{\"problemID\": " + strProblemID + ", \"exist\": false}";

        if (strProblemID != null && strProblemID.length() > 0) {
            Integer problemID = Integer.parseInt(strProblemID);
            SqlSession sqlSession = DataSource.getSqlSesion();
            TableProblem tableProblem = sqlSession.getMapper(TableProblem.class);
            if (tableProblem.getProblemByID(problemID - 1000) != null) {
                json = String.format(jsonPattern, problemID, true);
            }
        }
        System.out.println(json);
        Utils.responseJson(response, json);
    }

    private void addProblem(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strProblemID = request.getParameter("inputProblemID");//如果ID不为null. 则是编辑该题目

        String title = request.getParameter("inputTitle");
        String desc = request.getParameter("inputDesc");
        String inputDesc = request.getParameter("inputInputDesc");
        String outputDesc = request.getParameter("inputOutputDesc");
        String inputSample = request.getParameter("inputInputSample");
        String outputSample = request.getParameter("inputOutputSample");
        String hint = request.getParameter("inputHint");
        String source = request.getParameter("inputSource");
        String staticLangTimeLimit = request.getParameter("inputStaticLangTimeLimit");
        String staticLangMemLimit = request.getParameter("inputStaticLangMemLimit");
        String dynamicLangTimeLimit = request.getParameter("inputDynamicLangTimeLimit");
        String dynamicLangMemLimit = request.getParameter("inputDynamicLangMemLimit");


        ProblemBean problemBean = new ProblemBean();
        problemBean.setTitle(title);
        problemBean.setDesc(desc);
        problemBean.setInputDesc(inputDesc);
        problemBean.setOutputDesc(outputDesc);
        problemBean.setInputSample(inputSample);
        problemBean.setOutputSample(outputSample);
        problemBean.setHint(hint);
        problemBean.setSource(source);
        problemBean.setStaticLangTimeLimit(Integer.parseInt(staticLangTimeLimit));
        problemBean.setStaticLangMemLimit(Integer.parseInt(staticLangMemLimit));
        problemBean.setDynamicLangTimeLimit(Integer.parseInt(dynamicLangTimeLimit));
        problemBean.setDynamicLangMemLimit(Integer.parseInt(dynamicLangMemLimit));

        System.out.println("new tableProblem: " + problemBean);

        SqlSession sqlSession = DataSource.getSqlSesion();
        TableProblem tableProblem = sqlSession.getMapper(TableProblem.class);
        if (strProblemID == null) {
            tableProblem.addProblem(problemBean);
            sqlSession.commit();
            sqlSession.close();
            response.sendRedirect("/test-point-list?problemID=" + problemBean.getProblemID());
        } else {
            problemBean.setProblemID(Integer.parseInt(strProblemID));
            tableProblem.updateProblemByID(problemBean);
            sqlSession.commit();
            sqlSession.close();
            response.sendRedirect("/problem?problemID=" + strProblemID);
        }
    }

    private void problemDeleteGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strProblemID = request.getParameter("problemID");//如果ID不为null. 则是编辑该题目
        Integer problemID = strProblemID != null && strProblemID.length() > 0 ? Integer.parseInt(strProblemID) : null;
        if (problemID != null) {
            SqlSession sqlSession = DataSource.getSqlSesion();
            TableProblem tableProblem = sqlSession.getMapper(TableProblem.class);
            tableProblem.deleteProblemByID(problemID);

            String testPointSavePath = ((ConfigurationBean) getServletContext().getAttribute("configuration")).getTestPointBaseDir() + "/" + (1000 + problemID);
            //删除测试点文件
            FileUtils.deleteDirectory(new File(testPointSavePath));

            sqlSession.commit();
            sqlSession.close();
            MessageBean messageBean = new MessageBean("信息", "信息", "题目已经成功删除!", "/problem-list", "返回题目列表");
            Utils.sendErrorMsg(response, messageBean);
        } else {
            MessageBean messageBean = new MessageBean("错误", "错误", "题目参数不正确!", "/problem-list", "返回题目列表");
            Utils.sendErrorMsg(response, messageBean);
        }
    }


    private void getProblems(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strPage = request.getParameter("page");
        int page =  strPage != null ? Integer.parseInt(strPage) : 1;

        SqlSession sqlSession = DataSource.getSqlSesion();
        TableProblem tableProblem = sqlSession.getMapper(TableProblem.class);

        List<ProblemBean> problemBeanList;
        HashMap<String, String> cookieMap = Utils.getCookieMap(request);
        if (cookieMap.containsKey("userID")) {
            problemBeanList = tableProblem.getProblesOrderByIDForLogin(
                    Integer.parseInt(cookieMap.get("userID")),
                    JudgeClient.ACCEPTED,
                    (page - 1) * Consts.COUNT_PER_PAGE,
                    Consts.COUNT_PER_PAGE);
        } else {
            problemBeanList = tableProblem.getProblemsOrderByID((page - 1) * Consts.COUNT_PER_PAGE, Consts.COUNT_PER_PAGE);
        }

        int recordCount = tableProblem.getCount();
        //获取分页信息
        PageBean pageBean = Utils.getPagination(recordCount, page, request);

        sqlSession.close();
        request.setAttribute("tableTitle", "题目列表(" + recordCount + ")");
        request.setAttribute("pageInfo", pageBean);
        request.setAttribute("problemList", problemBeanList);
        request.getRequestDispatcher("/WEB-INF/jsp/problem/problem-list.jsp").forward(request, response);
    }



    private void searchProblem(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String problemKeyword = request.getParameter("inputProblemKeyword");

        String strPage = request.getParameter("page");
        int page =  strPage != null ? Integer.parseInt(strPage) : 1;

        SqlSession sqlSession = DataSource.getSqlSesion();
        TableProblem tableProblem = sqlSession.getMapper(TableProblem.class);
        List<ProblemBean> problemBeanList = tableProblem.searchProblem(problemKeyword, (page - 1) * Consts.COUNT_PER_PAGE, Consts.COUNT_PER_PAGE);
        int recordCount = tableProblem.getSearchResultCount(problemKeyword);
        //获取分页信息
        PageBean pageBean = Utils.getPagination(recordCount, page, request);

        sqlSession.close();

        request.setAttribute("tableTitle", "搜索结果(" + recordCount + ")");
        request.setAttribute("pageInfo", pageBean);
        request.setAttribute("problemList", problemBeanList);
        request.getRequestDispatcher("/WEB-INF/jsp/problem/problem-list.jsp").forward(request, response);
    }


    private void showProblem(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strProblemID = request.getParameter("problemID");
        int problemID = 0;
        if (strProblemID != null) {
            problemID = Integer.parseInt(strProblemID);
        }
        SqlSession sqlSession = DataSource.getSqlSesion();
        TableProblem tableProblem = sqlSession.getMapper(TableProblem.class);
        ProblemBean problemBean = tableProblem.getProblemByID(problemID);

        sqlSession.close();

        request.setAttribute("problem", problemBean);
        request.getRequestDispatcher("/WEB-INF/jsp/problem/problem.jsp").forward(request, response);
    }

}

