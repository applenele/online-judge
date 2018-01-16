package org.oj.controller;

import org.apache.ibatis.session.SqlSession;
import org.oj.database.DataSource;
import org.oj.database.TableProblem;
import org.oj.model.javaBean.ProblemBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by xanarry on 18-1-4.
 */
@WebServlet(
        name = "problemServlet",
        urlPatterns = {
                "/add-problem",
                "/edit-problem",
                "/problem-list",
                "/problem"
        }
)

public class problemServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("post: " + request.getRequestURL());
        if (request.getRequestURI().equals("/add-problem")) addProblem(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("get: " + request.getRequestURL());
        if (request.getRequestURI().equals("/add-problem")) {
            request.getRequestDispatcher("/edit-problem.jsp").forward(request, response);
        }

        if (request.getRequestURI().equals("/edit-problem")) {
            String strProblemID = request.getParameter("problemID");
            if (strProblemID != null) {
                int problemID = Integer.parseInt(strProblemID);
                SqlSession sqlSession = DataSource.getSqlSesion();
                TableProblem tableProblem = sqlSession.getMapper(TableProblem.class);
                ProblemBean problemBean = tableProblem.getProblemByID(problemID);
                request.setAttribute("problem", problemBean);
                sqlSession.close();
                request.getRequestDispatcher("/edit-problem.jsp").forward(request, response);
            } else {
                response.sendRedirect("/problem-list");
            }

        }

        if (request.getRequestURI().equals("/problem")) showProblem(request, response);
        if (request.getRequestURI().equals("/problem-list")) getProblems(request, response);
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
            response.sendRedirect("/problem-list");
        } else {
            problemBean.setProblemID(Integer.parseInt(strProblemID));
            tableProblem.updateProblemByID(problemBean);
            sqlSession.commit();
            sqlSession.close();
            response.sendRedirect("/problem?problemID=" + strProblemID);
        }

    }


    private void getProblems(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strStart = request.getParameter("start");
        String strCount = request.getParameter("count");
        int start = strStart != null ? Integer.parseInt(strStart) : 0;
        int count = strCount != null ? Integer.parseInt(strCount) : 100;

        System.out.println("start: " + start + " count: " + count);

        SqlSession sqlSession = DataSource.getSqlSesion();
        TableProblem tableProblem = sqlSession.getMapper(TableProblem.class);
        List<ProblemBean> problemBeanList = tableProblem.getProblemsOrderByCrateTime(start, count);
        sqlSession.close();

        request.setAttribute("problemList", problemBeanList);
        request.getRequestDispatcher("/problem-list.jsp").forward(request, response);
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
        request.getRequestDispatcher("/problem.jsp").forward(request, response);
    }

}

