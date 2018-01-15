package judge;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javafx.util.Pair;
import judge.beans.JudgeStateBean;
import judge.beans.ResultBean;
import judge.beans.SubmitBean;
import judge.beans.TestPointResultBean;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.session.SqlSession;
import org.oj.database.*;
import org.oj.model.javaBean.CompileInfoBean;
import org.oj.model.javaBean.JudgeDetailBean;
import org.oj.model.javaBean.ProblemBean;
import org.oj.model.javaBean.SubmitRecordBean;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by xanarry on 18-1-7.
 */

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



    //等待队列需要被多个对象同时共享, 属于此类
    private static LinkedBlockingQueue<SubmitQueueElement> submitQueue = new LinkedBlockingQueue<SubmitQueueElement>();



    private String runningBaseFolder;
    private String serverAddress;
    private int serverPort;
    private int webServerCopyFile = 1;



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


    private void submitToJudger() {
        while (true) {
            SubmitQueueElement submitQueueElement = null;
            try {
                submitQueueElement = submitQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
                //update result as system error
            }
            sendToServer(submitQueueElement);
        }
    }

    //文件处理
    private boolean writeCodeToFile(SubmitRecordBean submit, String runningFolder) {
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

        String sourceCodePath = runningFolder + "/Main" + suffix;

        try {
            PrintWriter inPrintWriter = new PrintWriter(sourceCodePath);
            inPrintWriter.write(submit.getSourceCode());
            inPrintWriter.flush();
            inPrintWriter.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }


    private boolean copyTestPointFile(String testPointDataPath, String runningFolder) {
        File[] files = new File(testPointDataPath).listFiles();
        File desc = new File(runningFolder);
        if (files == null) {
            return false;
        }

        for (File f : files) {
            if (f.getName().endsWith(".in") || f.getName().endsWith(".out")) {
                try {
                    FileUtils.copyFileToDirectory(f, desc);
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return true;
    }

    private String prepareFiles(SubmitRecordBean submitRecordBean, String testPointDataPath) {
        //创建运行目录根目录
        File file = new File(runningBaseFolder);
        if (!file.exists()) { //不存在则创建目录
            System.out.println("指定运行根目录不存在");
            if (!file.mkdir()) {
                System.out.println("创建运行根目录失败");
                return null;
            } else {
                System.out.println("创建运行根目录成功");
            }
        }

        //根据提交ID为每次提交创建一个目录
        String runningFolder = runningBaseFolder + "/submit" + submitRecordBean.getSubmitID();
        System.out.println("==========================================explicit dir: " + runningFolder);
        File f = new File(runningFolder);
        if (!f.mkdir()) {
            System.out.println("具体文件夹创建失败");
            return null;
        }

        System.out.println("write code to file");
        //将源代码写入到运行目录: runningBaseFolder/submitID/Main.*
        if (writeCodeToFile(submitRecordBean, runningFolder)) {
            if (webServerCopyFile == 1) {
                if (copyTestPointFile(testPointDataPath, runningFolder)) {
                    return runningFolder;
                }
            } else {
                return runningFolder;
            }
        }

        //如果在复制文件的过程中发生了错误, 删除文件夹
        try {
            FileUtils.deleteDirectory(new File(runningFolder));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void conductSystemError(SubmitRecordBean submitRecordBean, String runningFolder)
    {
        ResultBean resultBean = new ResultBean();
        resultBean.setSubmitID(submitRecordBean.getSubmitID());
        resultBean.setSystemError(1);
        updateResult(resultBean);

        if (runningFolder != null) {
            try {
                FileUtils.deleteDirectory(new File(runningFolder));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void submit(SubmitRecordBean submitRecordBean, ProblemBean problemBean, String testPointDataPath) {
        runningBaseFolder = "/home/xanarry/Desktop/running-dir";

        String runningFolder = prepareFiles(submitRecordBean, testPointDataPath);
        if (runningFolder == null) {
            //在准备文件的过程中产生了错误, 尝试删除文件
            conductSystemError(submitRecordBean, null);
            return;
        }


        System.out.println("generate submit Bean");

        //创建提交到judge server的bean
        SubmitBean submitBean = new SubmitBean();
        //设置提交ID
        submitBean.setSubmitID(submitRecordBean.getSubmitID());
        //设置语言类型
        submitBean.setLanguage(submitRecordBean.getLanguage());
        //选择时间和内存的限制
        String language = submitRecordBean.getLanguage().toLowerCase();
        if (language.equals("c") || language.equals("c++") || language.equals("cpp")) {
            submitBean.setTimeLimit(problemBean.getStaticLangTimeLimit());
            submitBean.setMemLimit(problemBean.getStaticLangMemLimit());
        } else {
            submitBean.setTimeLimit(problemBean.getDynamicLangTimeLimit());
            submitBean.setMemLimit(problemBean.getDynamicLangMemLimit());
        }
        submitBean.setRunningFolder(runningFolder);
        submitBean.setTestPointDataFolder(testPointDataPath);


        System.out.println("push into queue: submitBean:" + submitBean);
        try {
            //提交队列中
            submitQueue.put(new SubmitQueueElement(submitRecordBean, submitBean));
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            conductSystemError(submitRecordBean, runningFolder);
        }
    }



    //与judge server通信
    private void sendToServer(SubmitQueueElement queElement) {
        /*
        * this routine has three step
        * 1: submit code to judge server
        * 2: communicate judge state between submit code and get result
        * 3: get judge result from judge server
        * */
        SubmitRecordBean submitRecordBean = queElement.getSubmitRecordBean();
        SubmitBean submitBean = queElement.getSubmitBean();

        ResultBean sytemErrorResultBean = new ResultBean();
        sytemErrorResultBean.setSystemError(1);
        sytemErrorResultBean.setSubmitID(submitBean.getSubmitID());

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
            final String STATE        = "state";

            String state = SUBMIT_STATE;

            String responseString = "";
            while (true) {
                //send submit json to judge server
                String message = "";
                if (state.equals(SUBMIT_STATE)) { //first time send submit information
                    message = SUBMIT_STATE + " " + submitBean.toJson();
                    state = WAIT_STATE;
                } else {
                    message = WAIT_STATE;
                }

                //send message to judge server
                if (state.endsWith(SUBMIT_STATE)) {
                    System.out.println(">>>server to judge[SUBMIT]: " + message);
                } else {
                    System.out.println(">>>server to judge[WAIT]: " + message);
                }
                outputBufferedStream.write(message.getBytes("utf-8"));//>>>>>>>>>>>>>>>>
                outputBufferedStream.flush();



                //read message response from judge server
                int read = inputBufferedStream.read(readBuffer);//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                responseString = new String(readBuffer, "utf-8");
                System.out.println("<<<judge to server: " + responseString);

                if (responseString.startsWith(FINISH_STATE)) {
                    break;
                } else if (responseString.startsWith(STATE)) {

                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    JudgeStateBean judgeStateBean = gson.fromJson(responseString.substring(STATE.length()).trim(), JudgeStateBean.class);

                    if (judgeStateBean.getState().equals(JudgeClient.COMPILING))
                        submitRecordBean.setResult(JudgeClient.COMPILING);

                    if (judgeStateBean.getState().equals(JudgeClient.RUNNING))
                        submitRecordBean.setResult(JudgeClient.RUNNING);

                    updateState(submitRecordBean);
                }
            }


            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            ResultBean resultBean = gson.fromJson(responseString.substring(FINISH_STATE.length()).trim(), ResultBean.class);

            updateResult(resultBean);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            updateResult(sytemErrorResultBean);
            return;
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
        System.out.println("get result: " + resultBean);


        SqlSession sqlSession = Database.getSqlSesion();
        SubmitRecord submitRecord = sqlSession.getMapper(SubmitRecord.class);
        JudgeDetail judgeDetail = sqlSession.getMapper(JudgeDetail.class);
        CompileInfo compileInfo = sqlSession.getMapper(CompileInfo.class);


        SubmitRecordBean submitRecordBean = submitRecord.getSubmitRecordByID(resultBean.getSubmitID());
        List<TestPointResultBean> testPointResults = resultBean.getDetail();

        submitRecordBean.setTimeConsume(0);
        submitRecordBean.setMemConsume(0);
        submitRecordBean.setJudgeTime(new Date().getTime());

        //系统错误
        if (resultBean.getSystemError() == 1) {
            submitRecordBean.setResult(SYSTEM_ERROR);
            submitRecordBean.setTimeConsume(0);
            submitRecordBean.setMemConsume(0);
        } else if (resultBean.getCompileStatus() != 0) {
            //编译错误
            compileInfo.insertCompileResult(new CompileInfoBean(submitRecordBean.getSubmitID(), resultBean.getCompileResult()));

            submitRecordBean.setResult(COMPILATION_ERROR);
            submitRecordBean.setTimeConsume(0);
            submitRecordBean.setMemConsume(0);
        } else { //get running result
            int maxTimeConsume = 0;
            int maxMemConsume = 0;
            String finalResult = ACCEPTED;
            for (TestPointResultBean t : testPointResults) {
                JudgeDetailBean judgeDetailBean = new JudgeDetailBean();

                judgeDetailBean.setSubmitID(resultBean.getSubmitID());
                judgeDetailBean.setTestPointID(t.getTestPointID());
                judgeDetailBean.setTimeConsume(t.getTimeConsume());
                judgeDetailBean.setMemConsume(t.getMemConsume());
                judgeDetailBean.setReturnVal((short) 0);
                judgeDetailBean.setResult(t.getResult());

                maxTimeConsume = t.getTimeConsume() > maxTimeConsume ? t.getTimeConsume() : maxTimeConsume;
                maxMemConsume = t.getMemConsume() > maxMemConsume ? t.getMemConsume() : maxMemConsume;
                judgeDetail.insertJudgeDetail(judgeDetailBean);

                judgeDetailBean.setResult(t.getResult());
                if (!t.getResult().equals(ACCEPTED)) {
                    finalResult = t.getResult();
                }
            }

            submitRecordBean.setResult(finalResult);
            submitRecordBean.setTimeConsume(maxTimeConsume);
            submitRecordBean.setMemConsume(maxMemConsume);
        }
        submitRecordBean.setJudgeTime(new Date().getTime());
        submitRecord.updateSubmitRecord(submitRecordBean);
        sqlSession.commit();
        sqlSession.close();
    }





    public static void main(String[] argc) {
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
