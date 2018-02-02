package org.oj.controller;

import judge.beans.ConfigurationBean;
import org.apache.ibatis.session.SqlSession;
import org.oj.database.DataSource;
import org.oj.database.TableProblem;
import org.oj.database.TableTestPoint;
import org.oj.model.javaBean.ProblemBean;
import org.oj.model.javaBean.TestPointBean;
import utils.Tools;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;

/**
 * Created by xanarry on 18-1-2.
 */
@MultipartConfig
@WebServlet(name = "OnlyForTestServlet", urlPatterns = {"/upload-problem", "/upload-test-point", "/upload-test-point-stream"})/*这里设置的是servlet path*/
public class OnlyForTestServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        if (uri.equals("/upload-problem")) addProblem(request, response);
        if (uri.equals("/upload-test-point")) addTestPoint(request, response);
        if (uri.equals("/upload-test-point-stream")) addTestPointByStream(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        File f = new File("./");
        for (File c : f.listFiles()) {
            System.out.println(c.getName());
        }
    }


    private void addProblem(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("inputTitle");
        String desc = request.getParameter("inputDesc");
        String inputDesc = request.getParameter("inputInputDesc");
        String outputDesc = request.getParameter("inputOutputDesc");
        String inputSample = request.getParameter("inputInputSample");
        String outputSample = request.getParameter("inputOutputSample");
        String hint = request.getParameter("inputHint");
        String source = request.getParameter("inputSource");
        String background = request.getParameter("inputBackground");
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
        problemBean.setBackground(background);
        problemBean.setStaticLangTimeLimit(Integer.parseInt(staticLangTimeLimit));
        problemBean.setStaticLangMemLimit(Integer.parseInt(staticLangMemLimit));
        problemBean.setDynamicLangTimeLimit(Integer.parseInt(dynamicLangTimeLimit));
        problemBean.setDynamicLangMemLimit(Integer.parseInt(dynamicLangMemLimit));
        problemBean.setCreateTime(new Date().getTime());

        SqlSession sqlSession = DataSource.getSqlSesion();
        TableProblem tableProblem = sqlSession.getMapper(TableProblem.class);
        tableProblem.addProblem(problemBean);

        sqlSession.commit();
        sqlSession.close();

        System.out.println("OK" + problemBean.getProblemID() + ": " + problemBean.getTitle());
        PrintWriter writer = response.getWriter();
        writer.write(problemBean.getProblemID() + "");
        writer.flush();
    }


    private void addTestPoint(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strProblemID = request.getParameter("inputProblemID");
        String inputData = request.getParameter("inputInputData");
        String outputData = request.getParameter("inputOutputData");

        System.out.println("pid: " + strProblemID);

        Integer problemID = Integer.parseInt(strProblemID);


        String testPointDirRealPath = ((ConfigurationBean) getServletContext().getAttribute("configuration")).getTestPointBaseDir();
        //设置文件保存路径, 网站根目录/test-point/题目ID/[1.in|1.out]
        String testPointSavePath = testPointDirRealPath + "/" + (1000 + problemID);

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
        testPointBean.setInputTextPath((1000 + problemID) + "/" + testPointID + ".in");
        //设置输入文本长度
        testPointBean.setInputTextLength(inputData.length());
        //设置输出文本路径
        testPointBean.setOutputTextPath((1000 + problemID) + "/" + testPointID + ".out");
        //设置输出文本长度
        testPointBean.setOutputTextLength(outputData.length());


        SqlSession sqlSession = DataSource.getSqlSesion();
        TableTestPoint testPoint = sqlSession.getMapper(TableTestPoint.class);
        testPoint.addTestPoint(testPointBean);

        sqlSession.commit();
        sqlSession.close();
        System.out.println("OK problem:" + problemID + " test point");

        PrintWriter writer = response.getWriter();
        writer.write(problemID + "");
        writer.flush();
    }


    private void addTestPointByStream(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strProblemID = request.getParameter("inputProblemID");
        String inputData = request.getParameter("inputInputData");
        String outputData = request.getParameter("inputOutputData");
        System.out.println("strProblemID: " + strProblemID);
        System.out.println("inputData: " + inputData);
        System.out.println("outputData: " + outputData);
        InputStream inputStream = request.getInputStream();
        byte[] buf = new byte[1024];
        int len = 0;

        StringBuffer sb = new StringBuffer();
        while (true) {
            len = inputStream.read(buf, 0, 1024);
            if (len == -1) {
                break;
            }
            sb.append(URLDecoder.decode(new String(buf), "UTF-8"));
        }
        PrintWriter writer = response.getWriter();
        //writer.print(strProblemID);
        writer.print("<html>strProblemID: " + strProblemID);
        writer.print("<hr>inputData: " + inputData);
        writer.print("<hr>outputData: " + outputData);
        writer.print("</html>");
        writer.flush();
    }
}