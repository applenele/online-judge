package org.oj.controller;

import org.oj.controller.beans.MessageBean;
import org.oj.controller.beans.PageBean;
import utils.Consts;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class Utils {
    public static void sendErrorMsg(HttpServletResponse response, MessageBean messageBean) throws IOException {
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

    public static String urlWithoutPageInfo(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        Map<String, String[]> params = request.getParameterMap();
        if (params.size() == 0) {
            return url + "?"; //没有参数, 准备添加参数
        }

        boolean hasParam = false;
        boolean hasPageParam = false;
        for (String key : params.keySet()) {
            if (!key.equals("page")) {
                if (!hasParam) {
                    url += ("?" + key + "=" + params.get(key)[0]);//这里的构造可能有漏洞
                    hasParam = true;
                } else {
                    url += ("&" + key + "=" + params.get(key)[0]);
                }
            } else {
                hasPageParam = true;
            }
        }
        if (params.size() == 1 && hasPageParam) {
            return url + "?";//有唯一的page参数, 去除之后应该跟?
        } else {
            return url + "&";//有参数, 在后面最佳参数
        }
    }

    public static PageBean getPagination(int recordCount, int currentPage,  HttpServletRequest request) {
        PageBean pageBean = new PageBean();
        int maxPageVal = recordCount / Consts.COUNT_PER_PAGE;
        if (recordCount % Consts.COUNT_PER_PAGE != 0) {
            maxPageVal++; //整数页还有预项, 增加一页
        }
        pageBean.setMaxPageVal(maxPageVal);
        pageBean.setCurrentPageVal(currentPage);
        pageBean.setRecordCount(recordCount);
        pageBean.setCountPerPage(Consts.COUNT_PER_PAGE);
        pageBean.setUrl(Utils.urlWithoutPageInfo(request));
        System.out.println("after: " + pageBean.getUrl());
        return pageBean;
    }

    public static HashMap<String, String> getCookieMap(HttpServletRequest request) {
        HashMap<String, String> cookieMap = new HashMap<>(4);
        if (request.getCookies() != null) {
            for (Cookie c : request.getCookies()) {
                if (c.getName().equals("userID") || c.getName().equals("userName") || c.getName().equals("userType")) {
                    cookieMap.put(c.getName(), c.getValue());
                }
            }
        }
        return cookieMap;
    }
}
