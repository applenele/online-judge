package filter;


import judge.JudgeClient;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


/**
 * Created by xanarry on 17-3-29.
 */

@WebListener()
public class servletListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("创建judge client");
        JudgeClient judgeClient = new JudgeClient();
        if (!judgeClient.isAlive()) {
            judgeClient.start();
            System.out.println("启动judge client线程");
        }
        servletContextEvent.getServletContext().setAttribute("judgeClient", judgeClient);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
