package test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import judge.beans.ConfigurationBean;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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


    public static void main(String[] argv) throws IOException {
        //FileUtils.copyDirectory(new File("/home/xanarry/Workspace/oj-data/test-point/1001"), new File("/home/xanarry/Desktop/running-dir/submit5"));
        //FileUtils.copyFileToDirectory();
        System.out.println(10/0);
    }
}
