package judge;

import org.oj.model.javaBean.SubmitRecordBean;
import utils.ConstStrings;

import java.util.Date;

/**
 * Created by xanarry on 18-1-7.
 */
public class JudgeTest {
    public static void main(String[] argv) {
        /*SubmitRecordBean submit = getSubmit();

        JudgeClient judgeClient = new JudgeClient();
        judgeClient.submit(submit);*/


        String json = "{\"submitID\":1,\"compileStatus\":1,\"compileResult\":\"compile error\",\"result\":[{\"testPointID\":1,\"result\":\"accepted\"},{\"testPointID\":2,\"result\":\"accepted\"},{\"testPointID\":3,\"result\":\"accepted\"},{\"testPointID\":4,\"result\":\"accepted\"}]}";


    }


    private static SubmitRecordBean getSubmit() {
        SubmitRecordBean submitRecordBean = new SubmitRecordBean();
        submitRecordBean.setProblemID(5);
        submitRecordBean.setUserID(2);
        submitRecordBean.setContestID(0);//非比赛提交的代码统一设置为0
        submitRecordBean.setResult(ConstStrings.result[0]);
        submitRecordBean.setLanguage("C");
        String code = "#inlcude<stdio.h> \n" +
                "int main()\n" +
                "{\n" +
                "    puts(\"hello\");\n" +
                "    return 0;\n" +
                "}";
        submitRecordBean.setSourceCode(code);
        submitRecordBean.setCodeLength(code.length());
        submitRecordBean.setSubmitTime(new Date().getTime());
        return submitRecordBean;
    }
}
