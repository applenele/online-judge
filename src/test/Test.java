package test;

import org.apache.commons.codec.digest.DigestUtils;
/**
 * Created by xanarry on 18-1-2.
 */
public class Test {
    public static void main(String[] argv) {
        System.out.println(DigestUtils.sha1Hex("asdf").length());
    }
}
