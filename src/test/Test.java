package test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import judge.beans.ConfigurationBean;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Created by xanarry on 18-1-2.
 */
public class Test {

    public static String urlWithoutPageInfo(String url) {
        int index = url.indexOf("?");
        if (index == -1) {
            return url + "?";
        }
        String[] params = url.substring(index + 1).split("&");
        String newUrl = url.substring(0, index);

        boolean hasParam = false;
        for (String p : params) {
            if (!p.contains("page=")) {
                if (!hasParam) {
                    newUrl += ("?" + p);
                    hasParam = true;
                } else {
                    newUrl += ("&" + p);
                }
            }
        }
        return newUrl + "&";
    }


    public static void main(String[] argv) throws FileNotFoundException {
        File f = new File("./src");
        for (File c: f.listFiles()) {
            System.out.println(c.getName());
        }


        String configFilePath = "./src/config.json";
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ConfigurationBean configuration = null;
        try {
            configuration = gson.fromJson(new FileReader(configFilePath), ConfigurationBean.class);
            System.out.println(configuration);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
