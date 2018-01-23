package org.oj.controller;

import org.apache.ibatis.session.SqlSession;
import org.oj.controller.beans.MessageBean;
import org.oj.database.DataSource;
import org.oj.database.TableDiscuss;
import org.oj.model.javaBean.DiscussBean;
import utils.Consts;

import javax.print.DocFlavor;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@WebServlet(name = "DiscussServlet",
        urlPatterns = {
                "/post-discuss",
                "/post-original-discuss",
                "/delete-discuss",
                "/discuss",
                "/discuss-detail",
                "/set-first"
            }
        )
public class DiscussServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        System.out.println("get: " + request.getRequestURI());

        if (uri.equals("/post-discuss"))            postDiscuss(request, response, false);
        if (uri.equals("/post-original-discuss"))   postDiscuss(request, response, true);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        System.out.println("get: " + request.getRequestURI());

        if (uri.equals("/delete-discuss")) deleteDiscuss(request, response);
        if (uri.equals("/discuss")) getDiscuss(request, response);
        if (uri.equals("/set-first")) discussSetFirst(request, response);
        if (uri.equals("/post-original-discuss")) request.getRequestDispatcher("/discuss-edit.jsp").forward(request, response);
        if (uri.equals("/discuss-detail")) getDiscussDetail(request, response);
    }


    private void postDiscuss(HttpServletRequest request, HttpServletResponse response, boolean isOriginal) throws ServletException, IOException {
        DiscussBean discussBean = new DiscussBean();
        if (!isOriginal) {
            //如果是回复某一个消息, 则需要设置回复的是哪一楼消息, 设置直接回复楼ID和间接楼ID
            String strDirectFID = request.getParameter("inputDirectFID");
            Integer directFID = strDirectFID != null && strDirectFID.length() > 0 ? Integer.parseInt(strDirectFID) : null;

            String strRootID = request.getParameter("inputRootID");
            Integer rootID = strRootID != null && strRootID.length() > 0 ? Integer.parseInt(strRootID) : null;
            //回复消息不需要设置以下theme and title
        } else {
            String theme = request.getParameter("inputTheme");
            String title = request.getParameter("inputTitle");

            if (theme == null || theme.length() == 0) {
                discussBean.setTheme("管理员");
            } else {
                discussBean.setTheme(theme);
            }

            discussBean.setTitle(title);
        }

        //设置消息类型
        String strType = request.getParameter("inputType");
        Integer type = strType != null && strType.length() > 0 ? Integer.parseInt(strType) : null;
        discussBean.setType(type);

        //如果消息是关于题目或者比赛的, 设置题目或者比赛的ID
        if (type != Consts.FOR_MESSAGE) {
            String strPorcID = request.getParameter("inputPorcID");
            Integer porcID = strPorcID != null && strPorcID.length() > 0 ? Integer.parseInt(strPorcID) : null;
            discussBean.setPorcID(porcID);
        }

        //无论什么样的消息, 都必须要设置内容
        String content= request.getParameter("inputContent");

        Integer userID = null;
        String userName = null;

        //从登录信息中提取用户发布用户的信息
        for (Cookie c : request.getCookies()) {
            if (c.getName().equals("userID")) {
                userID = Integer.parseInt(c.getValue());
            }
            if (c.getName().equals("userName")) {
                userName = c.getValue();
            }
        }

        //如果用户没用登录, 禁止发布消息
        if (userID == null || userName == null) {
            MessageBean messageBean = new MessageBean();
            messageBean.setTitle("错误");
            messageBean.setHeader("错误信息");
            messageBean.setMessage("请登录后再提交信息");
            messageBean.setLinkText("");
            messageBean.setUrl("#");

            Utils.sendErrorMsg(request, response, messageBean);
            return;
        }

        discussBean.setType(type);
        discussBean.setContent(content);
        discussBean.setPostTime(new Date().getTime());
        discussBean.setUserID(userID);
        discussBean.setUserName(userName);
        discussBean.setReply(0);
        discussBean.setWatch(0);


        SqlSession sqlSession = DataSource.getSqlSesion();
        TableDiscuss tableDiscuss = sqlSession.getMapper(TableDiscuss.class);
        tableDiscuss.insertDiscuss(discussBean);

        sqlSession.commit();
        sqlSession.close();

        response.sendRedirect("/discuss");
    }

    private void deleteDiscuss(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private void getDiscuss(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strType = request.getParameter("type");
        String strPorcID = request.getParameter("porcID");


        Integer type = null, porcID = null;

        if (strType != null && strType.length() > 0) {
            type = Integer.parseInt(strType);
        }

        if (strPorcID != null && strPorcID.length() > 0) {
            porcID = Integer.parseInt(strPorcID);
        }

        SqlSession sqlSession = DataSource.getSqlSesion();
        TableDiscuss tableDiscuss = sqlSession.getMapper(TableDiscuss.class);
        List<DiscussBean> discussList = tableDiscuss.getDiscussListByPorcID(type, porcID);
        sqlSession.close();


        request.setAttribute("discussList", discussList);
        request.getRequestDispatcher("/discuss-list.jsp").forward(request, response);
    }


    private void discussSetFirst(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private void getDiscussDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String strPostID = request.getParameter("postID");
        Integer postID = strPostID != null && strPostID.length() > 0 ? Integer.parseInt(strPostID) : null;


        SqlSession sqlSession = DataSource.getSqlSesion();
        TableDiscuss tableDiscuss = sqlSession.getMapper(TableDiscuss.class);
        DiscussBean discussBean = tableDiscuss.getDiscussByPostID(postID);
        tableDiscuss.addWatch(postID);
        sqlSession.commit();

        List<DiscussBean> replyList = tableDiscuss.getDiscussListByRootID(postID);
        sqlSession.close();


        request.setAttribute("discuss", discussBean);
        request.setAttribute("replyList", replyList);
        System.out.println(discussBean);
        System.out.println(replyList);
        request.getRequestDispatcher("/discuss-reply.jsp").forward(request, response);
    }
}
