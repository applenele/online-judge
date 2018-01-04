package org.oj.ajax;

import utils.Tools;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;


/**
 * Created by xanarry on 18-1-1.
 */
@WebServlet(name = "CaptchaServlet", urlPatterns = "/getCaptcha")
public class CaptchaServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //do nothing
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().setAttribute("validateCode", "2");
        BufferedImage bufferedImage = Tools.getValidateCode(1, 1, "1+1=?");
        // flush it in the response
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        OutputStream responseOutputStream = response.getOutputStream();
        ImageIO.write(bufferedImage, "jpeg", responseOutputStream);
        responseOutputStream.flush();
        responseOutputStream.close();
    }
}
