package filter;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import judge.JudgeClient;
import judge.beans.ConfigurationBean;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;


/**
 * Created by xanarry on 17-3-29.
 */

@WebListener()
public class servletListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        /*load configuration from json file*/
        String configFilePath = servletListener.class.getResource("/config.json").getFile();
        System.out.println(configFilePath);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ConfigurationBean configuration = null;
        try {
            configuration = gson.fromJson(new FileReader(configFilePath), ConfigurationBean.class);
            System.out.println("load configuration: " + configuration);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (configuration == null) {
            System.exit(0);
        }

        System.out.println("create judge client");
        JudgeClient judgeClient = new JudgeClient(configuration);
        if (!judgeClient.isAlive()) {
            judgeClient.start();
            System.out.println("start judge client thread");
        }

        servletContextEvent.getServletContext().setAttribute("configuration", configuration);
        servletContextEvent.getServletContext().setAttribute("judgeClient", judgeClient);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
