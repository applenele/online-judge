package judge;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.ibatis.session.SqlSession;
import org.oj.database.Database;
import org.oj.database.JudgeDetail;
import org.oj.database.SubmitRecord;
import org.oj.model.javaBean.JudgeDetailBean;
import org.oj.model.javaBean.SubmitRecordBean;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by xanarry on 18-1-7.
 */
public class JudgeClient {
    private Queue<SubmitRecordBean> submitQueue;
    private String runningFolder;
    private String serverAddress;
    private int serverPort;

    public JudgeClient() {
        submitQueue = new LinkedList<SubmitRecordBean>();
    }

    private void loadConfiguration(String configurationFilePath) {
        runningFolder = "/home/xanarry/Desktop";
        serverAddress = "127.0.0.1";
        serverPort = 2345;
    }

    public boolean submitCode(SubmitRecordBean submit) {
        return submitQueue.offer(submit);
    }

    //文件处理
    public String writeCodeToFile(SubmitRecordBean submit) {
        runningFolder = "/home/xanarry/Desktop/running-dir";
        System.out.println("running dir: " + runningFolder);
        File file = new File(runningFolder);
        if (!file.exists()) { //不存在则创建目录
            System.out.println("指定运行目录不存在");
            if (!file.mkdir()) {
                System.out.println("创建运行目录失败");
                return null;
            } else {
                System.out.println("创建运行目录成功");
            }
        }

        String folder = runningFolder + "/submit" + submit.getSubmitID();
        System.out.println("==========================================explicit dir: " + folder);
        File f = new File(folder);
        if (!f.mkdir()) {
            System.out.println("具体文件夹创建失败");
            return null;
        }


        String suffix = "c";
        String language = submit.getLanguage().toLowerCase();
        if (language.equals("c")) {
            suffix = ".c";
        } else if (language.equals("c++") || language.equals("cpp")) {
            suffix = ".cpp";
        } else if (language.startsWith("python")) {
            suffix = ".py";
        } else if (language.equals("java")) {
            suffix = ".java";
        } else if (language.equals("javascript")) {
            suffix = ".js";
        }

        String sourceCodePath = folder + "/Main" + suffix;

        try {
            PrintWriter inPrintWriter = new PrintWriter(sourceCodePath);
            inPrintWriter.write(submit.getSourceCode());
            inPrintWriter.flush();
            inPrintWriter.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return folder;
    }


    //对象转json
    public String submitToJson(SubmitRecordBean submit, int timeLimit, int memLimit, String runningFolder, String testPointDataFolder) {
        String jsonPattern = "{\"submitID\":%d," +
                "\"language\":\"%s\"," +
                "\"timeLimit\":%d," +
                "\"memLimit\":%d," +
                "\"runningFolder\":\"%s\"," +
                "\"testPointDataFolder\":\"%s\"}";

        String json = String.format(jsonPattern,
                submit.getSubmitID(),
                submit.getLanguage(),
                timeLimit,
                memLimit,
                runningFolder,
                testPointDataFolder
        );

        return json;
    }

    //与judge server通信
    public void sendToServer(String json) {
        //创建socket并得到其输入输出流用于传输数据
        try {
            Socket clientSocket = new Socket("127.0.0.1", 2345);
            BufferedOutputStream outputBufferedStream = new BufferedOutputStream(clientSocket.getOutputStream());
            BufferedInputStream inputBufferedStream = new BufferedInputStream(clientSocket.getInputStream());

            outputBufferedStream.write(json.getBytes("utf-8"));//>>>>>>>>>>>>>>>>
            outputBufferedStream.flush();

            byte[] buf = new byte[8 * 1024];
            inputBufferedStream.read(buf);
            String resultJson = new String(buf, "utf-8");
            System.out.println("judge result json: " + resultJson);


            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            ResultBean resultBean = gson.fromJson(resultJson.trim(), ResultBean.class);


            SqlSession sqlSession = Database.getSqlSesion();
            SubmitRecord submitRecord = sqlSession.getMapper(SubmitRecord.class);
            JudgeDetail judgeDetail = sqlSession.getMapper(JudgeDetail.class);


            SubmitRecordBean submitRecordBean = submitRecord.getSubmitRecordByID(resultBean.getSubmitID());
            List<TestPointResultBean> testPointResults = resultBean.getTestPointResult();

            int maxTimeConsume = -1;
            int maxMemConsume = -1;

            String finalResult = "Runtime Error";

            for (TestPointResultBean t : testPointResults) {
                JudgeDetailBean judgeDetailBean = new JudgeDetailBean();

                judgeDetailBean.setSubmitID(resultBean.getSubmitID());
                judgeDetailBean.setTestPointID(t.getTestPointID());
                judgeDetailBean.setTimeConsume(231);
                judgeDetailBean.setMemConsume(43333);
                judgeDetailBean.setResult("Runtime Error");
                judgeDetailBean.setReturnVal((short) 1);

                //maxTimeConsume = max();
                //maxMemConsume = max();

                judgeDetail.insertJudgeDetail(judgeDetailBean);
            }

            submitRecordBean.setResult(finalResult);
            submitRecordBean.setTimeConsume(maxTimeConsume);
            submitRecordBean.setMemConsume(maxMemConsume);

            submitRecord.updateSubmitRecord(submitRecordBean);


            sqlSession.commit();
            sqlSession.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    //处理返回结果
    public static void main(String[] argv) {
        File file = new File("null/submit1");
        if (!file.exists()) {
            System.out.println(file.mkdir());
        }
    }


}

/*
发送到服务器的json
{
    "submitID": 1,
    "language":"c++",
    "timeLimit":"%d",
    "memLimit":"%d",
    "runningFolder":"/a/s/",
    "testPointDataFolder":"/g/s"
}


返回结果json
{
    "submitID":1,
    "compileStatus":1,
    "compileResult":"compile error",
    "result":
    [
        {
            "testPointID":1,
            "result":"accepted"
        },
        {
            "testPointID":2,
            "result":"accepted"
        },
        {
            "testPointID":3,
            "result":"accepted"
        },
        {
            "testPointID":4,
            "result":"accepted"
        }
    ]
}
*/
