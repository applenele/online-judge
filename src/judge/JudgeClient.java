package judge;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.util.Pair;
import judge.beans.JudgeStateBean;
import judge.beans.ResultBean;
import judge.beans.SubmitBean;
import judge.beans.TestPointResultBean;
import org.apache.ibatis.session.SqlSession;
import org.oj.database.Database;
import org.oj.database.JudgeDetail;
import org.oj.database.Problem;
import org.oj.database.SubmitRecord;
import org.oj.model.javaBean.JudgeDetailBean;
import org.oj.model.javaBean.ProblemBean;
import org.oj.model.javaBean.SubmitRecordBean;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by xanarry on 18-1-7.
 */
class QueElement {
    private SubmitRecordBean submitRecordBean;
    private SubmitBean submitBean;

    public QueElement() {}

    public QueElement(SubmitRecordBean submitRecordBean, SubmitBean submitBean) {
        this.submitRecordBean = submitRecordBean;
        this.submitBean = submitBean;
    }

    public SubmitRecordBean getSubmitRecordBean() {
        return submitRecordBean;
    }

    public void setSubmitRecordBean(SubmitRecordBean submitRecordBean) {
        this.submitRecordBean = submitRecordBean;
    }

    public SubmitBean getSubmitBean() {
        return submitBean;
    }

    public void setSubmitBean(SubmitBean submitBean) {
        this.submitBean = submitBean;
    }
}

public class JudgeClient extends Thread {
    private static String QUEUING = "Queuing";
    private static String COMPILING = "Compiling";
    private static String RUNNING = "Running";
    private static String ACCEPTED = "Accepted";
    private static String PRESENTATION_ERROR = "Presentation Error";
    private static String WRONG_ANSWER = "Wrong Answer";
    private static String TIME_LIMIT_EXCEEDED = "Time Limit Exceeded";
    private static String MEMORY_LIMIT_EXCEEDED = "Memory Limit Exceeded";
    private static String OUTPUT_LIMIT_EXCEEDED = "Output Limit Exceeded";
    private static String RUNTIME_ERROR = "Runtime Error";
    private static String SYSTEM_ERROR = "System Error";
    private static String COMPILATION_ERROR = "Compilation Error";

    private LinkedBlockingQueue<QueElement> submitQueue = new LinkedBlockingQueue<QueElement>();
    private String runningBaseFolder;
    private String serverAddress;
    private int serverPort;

    public JudgeClient() {
        runningBaseFolder = "/home/xanarry/Desktop";
        serverAddress = "127.0.0.1";
        serverPort = 2345;
        //submitToJudger();
    }

    public JudgeClient(String configurationFilePath) {
        loadConfiguration(configurationFilePath);
        //submitToJudger();
    }

    private void loadConfiguration(String configurationFilePath) {
        runningBaseFolder = "/home/xanarry/Desktop";
        serverAddress = "127.0.0.1";
        serverPort = 2345;
    }

    @Override
    public void run() {
        super.run();
        submitToJudger();
    }

    public boolean submit(SubmitRecordBean submitRecordBean, ProblemBean problemBean, String testPointDataPath) {
        runningBaseFolder = "/home/xanarry/Desktop/running-dir";
        System.out.println("write code to file");
        String runningFolder = writeCodeToFile(submitRecordBean, runningBaseFolder);
        if (runningFolder == null) {
            return false;
        }

        System.out.println("generate submit Bean");
        SubmitBean submitBean = new SubmitBean();
        submitBean.setSubmitID(submitRecordBean.getSubmitID());
        submitBean.setLanguage(submitRecordBean.getLanguage());
        String language = submitRecordBean.getLanguage().toLowerCase();
        if (language.equals("c") || language.equals("c++") || language.equals("cpp")) {
            submitBean.setTimeLimit(problemBean.getStaticLangTimeLimit());
            submitBean.setMemLimit(problemBean.getStaticLangMemLimit());
        } else {
            submitBean.setTimeLimit(problemBean.getDynamicLangTimeLimit());
            submitBean.setMemLimit(problemBean.getDynamicLangMemLimit());
        }
        submitBean.setRunningFolder(runningBaseFolder);
        submitBean.setRunningFolder(testPointDataPath);


        System.out.println("push into queue");
        try {
            //提交队列中
            submitQueue.put(new QueElement(submitRecordBean, submitBean));
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    private void submitToJudger() {
        while (true) {
            QueElement queElement = null;
            try {
                queElement = submitQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
                //update result as system error
            }
            sendToServer(queElement);
        }
    }

    //文件处理
    private String writeCodeToFile(SubmitRecordBean submit, String runningFolder) {
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


    //与judge server通信
    private void sendToServer(QueElement queElement) {
        /*
        * this routine has three step
        * 1: submit code to judge server
        * 2: communicate judge state between submit code and get result
        * 3: get judge result from judge server
        * */
        SubmitRecordBean submitRecordBean = queElement.getSubmitRecordBean();
        SubmitBean submitBean = queElement.getSubmitBean();

        byte[] writeBuffer = new byte[8 * 1024];
        byte[] readBuffer  = new byte[8 * 1024];

        try {
            //创建socket并得到其输入输出流用于传输数据
            Socket clientSocket = new Socket(serverAddress, serverPort);

            //get inputstream and outputstream from socket
            BufferedOutputStream outputBufferedStream = new BufferedOutputStream(clientSocket.getOutputStream());
            BufferedInputStream inputBufferedStream = new BufferedInputStream(clientSocket.getInputStream());

            final String SUBMIT_STATE = "submit";
            final String WAIT_STATE   = "wait";
            final String FINISH_STATE = "finish";

            String state = SUBMIT_STATE;

            String responseString = "";
            while (true) {
                //send submit json to judge server
                String message = "";
                if (state.equals(SUBMIT_STATE)) {
                    message = SUBMIT_STATE + submitBean.toJson();
                    state = WAIT_STATE;
                } else {
                    message = WAIT_STATE;
                }

                //send message to judge server
                outputBufferedStream.write(message.getBytes("utf-8"));//>>>>>>>>>>>>>>>>
                outputBufferedStream.flush();

                //read message response from judge server
                inputBufferedStream.read(readBuffer);//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                responseString = new String(readBuffer, "utf-8");
                System.out.println("judge server response: " + responseString);

                if (responseString.startsWith(FINISH_STATE)) {
                    break;
                } else if (responseString.startsWith("state")) {
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    JudgeStateBean judgeStateBean = gson.fromJson(responseString.substring(5).trim(), JudgeStateBean.class);
                    if (judgeStateBean.getState().equals(JudgeClient.COMPILING))
                        submitRecordBean.setResult(JudgeClient.COMPILING);
                    if (judgeStateBean.getState().equals(JudgeClient.RUNNING))
                        submitRecordBean.setResult(JudgeClient.RUNNING);
                    updateState(submitRecordBean);
                }
            }


            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            ResultBean resultBean = gson.fromJson(responseString.substring(6).trim(), ResultBean.class);
            updateResult(resultBean);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void updateState(SubmitRecordBean submitRecordBean) {
        SqlSession sqlSession = Database.getSqlSesion();
        SubmitRecord submitRecord = sqlSession.getMapper(SubmitRecord.class);
        submitRecord.updateSubmitRecord(submitRecordBean);
        sqlSession.commit();
        sqlSession.close();
    }


    private void updateResult(ResultBean resultBean) {
        SqlSession sqlSession = Database.getSqlSesion();
        SubmitRecord submitRecord = sqlSession.getMapper(SubmitRecord.class);
        JudgeDetail judgeDetail = sqlSession.getMapper(JudgeDetail.class);


        SubmitRecordBean submitRecordBean = submitRecord.getSubmitRecordByID(resultBean.getSubmitID());
        List<TestPointResultBean> testPointResults = resultBean.getTestPointResult();

        int maxTimeConsume = -1;
        int maxMemConsume = -1;

        String finalResult = ACCEPTED;


        for (TestPointResultBean t : testPointResults) {
            JudgeDetailBean judgeDetailBean = new JudgeDetailBean();

            judgeDetailBean.setSubmitID(resultBean.getSubmitID());
            judgeDetailBean.setTestPointID(t.getTestPointID());
            judgeDetailBean.setTimeConsume(t.getTimeConsume());
            judgeDetailBean.setMemConsume(t.getMemConsume());
            judgeDetailBean.setReturnVal((short) 0);

            maxTimeConsume = t.getTimeConsume() > maxTimeConsume ? t.getTimeConsume() : maxTimeConsume;
            maxMemConsume  = t.getMemConsume()  > maxMemConsume  ? t.getMemConsume()  : maxMemConsume;
            judgeDetail.insertJudgeDetail(judgeDetailBean);

            judgeDetailBean.setResult(t.getResult());
            if (!t.getResult().equals(ACCEPTED)) {
                finalResult = t.getResult();
            }
        }

        submitRecordBean.setResult(finalResult);
        submitRecordBean.setTimeConsume(maxTimeConsume);
        submitRecordBean.setMemConsume(maxMemConsume);

        submitRecord.updateSubmitRecord(submitRecordBean);


        sqlSession.commit();
        sqlSession.close();
    }





    public static void main(String[] argc) {
       /* LinkedBlockingQueue<String> que = new LinkedBlockingQueue<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (;;) {
                    try {
                        System.out.println("input to que:");
                        Scanner scanner = new Scanner(System.in);
                        String val = scanner.nextLine();
                        if (val.equals("quit")) {
                            return;
                        }
                        que.put("threada " + val);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String s = null;
                    try {
                        s = que.take();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(s);
                }
            }
        }).start();*/

        JudgeClient judgeClient = new JudgeClient();
    }
}

/*
发送到服务器的json
{
    "submitID": 1,
    "language":"c++",
    "timeLimit":"%d",
    "memLimit":"%d",
    "runningBaseFolder":"/a/s/",
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
