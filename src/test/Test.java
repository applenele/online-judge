package test;

import utils.Tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Comparator;

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
        String t = "http://localhost:8080/record-list?inputUserName=&inputProblemID=&inputResult=Accepted&inputLanguage=";
        System.out.println(urlWithoutPageInfo(t) + "page=34");
    }
}
