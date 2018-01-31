package filter;

import com.sun.org.apache.xerces.internal.xs.StringList;
import org.oj.controller.Utils;
import org.oj.controller.beans.MessageBean;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.TreeSet;

/**
 * Created by xanarry on 17-3-27.
 */
@WebFilter(filterName = "AccessFilter", urlPatterns = "/*")
public class AccessFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        /*为请求和响应设置统一的编码方式*/
        servletRequest.setCharacterEncoding("utf8");
        servletResponse.setCharacterEncoding("utf8");

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;


        String url = request.getRequestURL().toString();
        String uri = request.getRequestURI();

        System.out.println(request.getMethod() + ": " + url);

        TreeSet<String> forAdmin = new TreeSet<>();
        forAdmin.add("/send-email");
        forAdmin.add("/configuration");
        forAdmin.add("/contest-delete");
        forAdmin.add("/problem-delete");
        forAdmin.add("/user-delete");
        forAdmin.add("/test-point-delete");
        forAdmin.add("/contest-problem-delete");
        forAdmin.add("/contest-user-delete");
        forAdmin.add("/discuss-delete");

        TreeSet<String> forAdvancedUser = new TreeSet<>();
        forAdvancedUser.add("/admin");
        forAdvancedUser.add("/user-list");
        forAdvancedUser.add("/contest-add");
        forAdvancedUser.add("/contest-edit");
        forAdvancedUser.add("/contest-problem-edit");
        forAdvancedUser.add("/discuss-set-first");
        forAdvancedUser.add("/problem-add");
        forAdvancedUser.add("/problem-edit");
        forAdvancedUser.add("/rejudge");
        forAdvancedUser.add("/add-test-point");


        HashMap<String, String> cookieMap = Utils.getCookieMap(request);
        if (forAdmin.contains(uri)) {
            if (cookieMap.size() > 0 && cookieMap.get("userType").equals("2")) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                String referer = request.getHeader("referer");
                MessageBean messageBean = new MessageBean("错误", "权限错误", "当前权限无法完成此操作!", referer != null ? referer : "/", "返回");
                Utils.sendErrorMsg(response, messageBean);
            }
        } else if (forAdvancedUser.contains(uri)) {
            if (cookieMap.size() > 0 && (cookieMap.get("userType").equals("1") || cookieMap.get("userType").equals("2"))) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                String referer = request.getHeader("referer");
                MessageBean messageBean = new MessageBean("错误", "权限错误", "当前权限无法完成此操作!", referer != null ? referer : "/", "返回");
                Utils.sendErrorMsg(response, messageBean);
            }
        } else {
            //正常页面, 通过验证
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
