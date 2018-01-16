package org.oj.controller;

import org.apache.ibatis.session.SqlSession;
import org.oj.database.DataSource;
import org.oj.database.TableProblem;
import org.oj.database.TableTestPoint;
import org.oj.model.javaBean.ProblemBean;
import org.oj.model.javaBean.TestPointBean;
import utils.Tools;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by xanarry on 18-1-6.
 */
@WebServlet(
        name = "TestPointServlet",
        urlPatterns = {
                "/add-test-point",
                "/delete-test-point",
                "/test-point-list",
                "/show-test-point"
        }
)
public class TestPointServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("post: " + request.getRequestURL());
        if (request.getRequestURI().equals("/add-test-point")) addTestPoint(request, response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("get: " + request.getRequestURL());
        if (request.getRequestURI().equals("/test-point-list")) getTestPointList(request, response);
        if (request.getRequestURI().equals("/show-test-point")) getTestPoint(request, response);
        if (request.getRequestURI().equals("/delete-test-point")) deleteTestPoint(request, response);

    }


    private void addTestPoint(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strProblemID = request.getParameter("inputProblemID");
        String inputData = request.getParameter("inputInputData");
        String outputData = request.getParameter("inputOutputData");

        Integer problemID = Integer.parseInt(strProblemID);

        //设置文件保存路径, 网站根目录/test-point/题目ID/[1.in|1.out]
        String testPointSavePath = getServletContext().getRealPath("/test-points") + "/p" + (1000 + problemID);

        System.out.println("testPointSavePath: " + testPointSavePath);
        System.out.println("inputText: " + inputData);
        System.out.println("outputText: " + outputData);

        int testPointID = Tools.saveTestPoint(testPointSavePath, inputData, outputData);
        if (testPointID == -1) {
            System.out.println("测试点文件处理出错");
            return;
        }

        TestPointBean testPointBean = new TestPointBean();
        //设置题目ID
        testPointBean.setProblemID(Integer.parseInt(strProblemID));
        //设置测试点编号
        testPointBean.setTestPointID(testPointID);
        //设置输入文本路径
        testPointBean.setInputTextPath(getServletContext().getContextPath() + "/test-points/p" + (1000 + problemID) + "/" + testPointID + ".in");
        //设置输入文本长度
        testPointBean.setInputTextLength(inputData.length());
        //设置输出文本路径
        testPointBean.setOutputTextPath(getServletContext().getContextPath() + "/test-points/p" + (1000 + problemID) + "/" + testPointID + ".out");
        //设置输出文本长度
        testPointBean.setOutputTextLength(outputData.length());

        System.out.println("add new test point: " + testPointBean);

        SqlSession sqlSession = DataSource.getSqlSesion();
        TableTestPoint testPoint = sqlSession.getMapper(TableTestPoint.class);
        testPoint.addTestPoint(testPointBean);
        sqlSession.commit();
        sqlSession.close();
        response.sendRedirect("/test-point-list?problemID=" + problemID);
    }

    private void deleteTestPoint(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strProblemID = request.getParameter("problemID");
        String strTestPointID = request.getParameter("testPointID");

        Integer problemID = Integer.parseInt(strProblemID);
        Integer testPointID = Integer.parseInt(strTestPointID);

        String testPointSavePath = getServletContext().getRealPath("/test-points") + "/p" + (1000 + problemID);

        //删除文件
        if (Tools.deleteTestPoint(testPointSavePath, testPointID)) {
            SqlSession sqlSession = DataSource.getSqlSesion();
            //从数据库删除记录
            TableTestPoint testPoint = sqlSession.getMapper(TableTestPoint.class);
            testPoint.deleteTestPoint(problemID, testPointID);
            sqlSession.commit();
            sqlSession.close();
        } else {
            System.out.println(String.format("题目: %d, 测试点: %d, 删除失败", problemID, testPointID));
        }

        response.sendRedirect("/test-point-list?problemID=" + problemID);
    }


    private void getTestPointList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strProblemID = request.getParameter("problemID");
        Integer problemID = Integer.parseInt(strProblemID);

        System.out.println("get problemID: " + strProblemID + " testpointList");
        SqlSession sqlSession = DataSource.getSqlSesion();
        TableTestPoint testPoint = sqlSession.getMapper(TableTestPoint.class);
        TableProblem tableProblem = sqlSession.getMapper(TableProblem.class);

        List<TestPointBean> testPoints = testPoint.getTestPointList(problemID);
        ProblemBean problemBean = tableProblem.getProblemByID(problemID);

        sqlSession.close();

        System.out.println(testPoints);
        request.setAttribute("testPointList", testPoints);
        request.setAttribute("problem", problemBean);
        request.getRequestDispatcher("/test-point-list.jsp").forward(request, response);
    }

    private void getTestPoint(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strProblemID = request.getParameter("problemID");
        String strTestPointID = request.getParameter("testPointID");


        Integer problemID = Integer.parseInt(strProblemID);
        Integer testPointID = Integer.parseInt(strTestPointID);


        SqlSession sqlSession = DataSource.getSqlSesion();
        TableProblem tableProblem = sqlSession.getMapper(TableProblem.class);
        ProblemBean problemBean = tableProblem.getProblemByID(problemID);


        TableTestPoint testPoint = sqlSession.getMapper(TableTestPoint.class);
        TestPointBean testPointBean = testPoint.getTestPoint(problemID, testPointID);

        sqlSession.close();

        System.out.println("input_text_path: " + testPointBean.getInputTextPath());


        String inputText = Tools.readFileToString(getServletContext().getRealPath(testPointBean.getInputTextPath()));
        String outputText = Tools.readFileToString(getServletContext().getRealPath(testPointBean.getOutputTextPath()));

        System.out.println("inputText: " + inputText);
        System.out.println("outputText: " + outputText);

        request.setAttribute("testPoint", testPointBean);
        request.setAttribute("problem", problemBean);
        request.setAttribute("inputText", inputText);
        request.setAttribute("outputText", outputText);
        request.getRequestDispatcher("/test-point.jsp").forward(request, response);
    }


}
