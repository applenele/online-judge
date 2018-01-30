package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

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
        servletRequest.setCharacterEncoding("utf8");
        servletResponse.setCharacterEncoding("utf8");
        filterChain.doFilter(servletRequest, servletResponse);

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String url = request.getRequestURL().toString();
        request.getMethod();
        System.out.println(request.getMethod() + ": " + url);

        HttpServletResponse response = (HttpServletResponse) servletResponse;

        /*
        if (uri.contains(".")) { //非地址请求, 如资源文件请求.css .js .jpg ....
            if (uri.endsWith(".jsp")) { //直接请求jsp文件, 重定向到jsp文件名对应的uri
                response.sendRedirect(uri.replace(".jsp", ""));
            }
            filterChain.doFilter(servletRequest, servletResponse);
        } else { //正常地址请求
            HttpSession session = request.getSession();
            if (uri.equals("/login") || uri.equals("/checkUserExist") || uri.equals("/ajaxCheckUserInfo") || uri.equals("/retrievePassword") || uri.equals("/setup")) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else if (session.getAttribute("loginName") == null) {
                //检查session是否存在, 不存在说明用户未登录, 重定向到登录页面
                response.sendRedirect("/login");
            } else {
                //System.out.println("正常页面, 通过session验证");
                filterChain.doFilter(servletRequest, servletResponse);
            }
        }*/
    }

    @Override
    public void destroy() {

    }
}
