package org.oj.controller;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.ibatis.session.SqlSession;
import org.oj.database.Database;
import org.oj.database.User;
import org.oj.model.javaBean.UserBean;
import utils.Tools;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * Created by xanarry on 18-1-2.
 */
@WebServlet(name = "LoginAndLogoutServlet", urlPatterns = {"/ajaxGetValidateCode", "/login", "/logout"})
public class LoginAndLogoutServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getRequestURI().endsWith("login")) login(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getRequestURI().endsWith("ajaxGetValidateCode")) ajaxGetValidateCode(request, response);
        if (request.getRequestURI().endsWith("logout")) logout(request, response);
    }

    protected void ajaxGetValidateCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int a = (int) (Math.random()*10) + 1;
        int b = (int) (Math.random()*10) + 1;
        int validateCode = a + b;
        String optr = "+";
        if ((a+b) % 2 == 0) {
            optr = "*";
            validateCode = a * b;
        }
        String randStr = String.format("%d%s%d=?", a,optr, b);
        System.out.println("randStr: " + randStr + " session: " + validateCode);

        request.getSession().setAttribute("validateCode", validateCode + "");

        BufferedImage bufferedImage = Tools.getValidateCode(100, 38, randStr);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        response.setContentType("image/jpeg");

        // 将图像输出到Servlet输出流中。
        ServletOutputStream sos = response.getOutputStream();
        ImageIO.write(bufferedImage, "jpeg", sos);
        sos.close();
    }

    private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("inputEmail");
        String password = request.getParameter("inputPassword");
        String validateCode = request.getParameter("inputValidateCode");
        String srememberMe = request.getParameter("inputRememberMe");

        boolean rememberMe = srememberMe != null && srememberMe.equals("1") ? true : false;

        String sessionValidateCode = (String) request.getSession().getAttribute("validateCode");

        System.out.println("email:" + email + " password:" + password + " rememberMe:" + rememberMe + " inputValidateCode:" + validateCode + " sessionVal:" + sessionValidateCode);

        SqlSession sqlSession = Database.getSqlSesion();
        User user = sqlSession.getMapper(User.class);
        UserBean userBean = user.getUserByEmail(email);

        String jsonPattern = "{\"userExist\" : %s, \"correctPassword\" : %s, \"correctValidateCode\" :%s}";
        boolean userExist = true;
        boolean correctPassword = true;
        boolean correctValidateCode = true;

        if (userBean != null) {
            //用户存在, 检查密码
            if (userBean.getPassword().equals(DigestUtils.sha1Hex(password))) {
                //密码正确,检查验证码
                if (validateCode.equals(sessionValidateCode)) {
                    //设置cookie
                    Cookie userIDCookie = new Cookie("userID", userBean.getUserID() + "");
                    Cookie userNameCookie = new Cookie("userName", userBean.getUserName());

                    if (rememberMe) {
                        userIDCookie.setMaxAge(7 * 24 * 60 * 60);//有效期一周
                        userNameCookie.setMaxAge(7 * 24 * 60 * 60);
                    }

                    //set new login time
                    userBean.setLastLoginTime(new Date().getTime());
                    user.updateUser(userBean);

                    response.addCookie(userIDCookie);
                    response.addCookie(userNameCookie);
                    System.out.println("login ok");
                } else {
                    //验证码错误
                    correctValidateCode = false;
                }
            } else {
                //密码错误
                System.out.println("密码错误");
                correctPassword = false;
            }

        } else {
            //用户不存在
            System.out.println("用户不存在");
            userExist = false;
        }

        sqlSession.commit();
        sqlSession.close();

        String json = String.format(jsonPattern, userExist, correctPassword, correctValidateCode);
        System.out.println(json);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }


    protected void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        for (Cookie c : cookies) {
            c.setMaxAge(0);
            c.setValue("");
            response.addCookie(c);
        }

        response.sendRedirect("/");
    }

}
