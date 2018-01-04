package filter;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import static utils.ConstStrings.CONFIGURATION_BEAN;

/**
 * Created by xanarry on 17-3-29.
 */

@WebListener()
public class servletListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
