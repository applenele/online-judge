package org.oj.controller;

import org.oj.controller.beans.MessageBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;

public class Utils {
    public static void sendErrorMsg(HttpServletRequest request, HttpServletResponse response, MessageBean messageBean) throws IOException {
        response.sendRedirect("/message?" +
                "title=" + URLEncoder.encode(messageBean.getTitle(), "utf8") +
                "&header=" + URLEncoder.encode(messageBean.getHeader(), "utf8") +
                "&message=" + URLEncoder.encode(messageBean.getMessage(), "utf8") +
                "&url=" + URLEncoder.encode(messageBean.getUrl(), "utf8") +
                "&linkText=" + URLEncoder.encode(messageBean.getLinkText(), "utf8"));
    }

    public static void responseJson(HttpServletResponse response, String json) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }
}
