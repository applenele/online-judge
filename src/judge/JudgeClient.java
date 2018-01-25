package judge;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
    public static  final String QUEUING = "Queuing";
    public static  final String COMPILING = "Compiling";
    public static  final String RUNNING = "Running";
    public static  final String ACCEPTED = "Accepted";
    public static  final String PRESENTATION_ERROR = "Presentation Error";
    public static  final String WRONG_ANSWER = "Wrong Answer";
    public static  final String TIME_LIMIT_EXCEEDED = "Time Limit Exceeded";
    public static  final String MEMORY_LIMIT_EXCEEDED = "Memory Limit Exceeded";
    public static  final String OUTPUT_LIMIT_EXCEEDED = "Output Limit Exceeded";
    public static  final String RUNTIME_ERROR = "Runtime Error";
    public static  final String SYSTEM_ERROR = "System Error";
    public static  final String COMPILATION_ERROR = "Compilation Error";



    //等待队列需要被多个对象同时共享, 属于此类
    private static LinkedBlockingQueue<SubmitQueueElement> submitQueue = new LinkedBlockingQueue<SubmitQueueElement>();



    private String runningBaseFolder;
    private String serverAddress;
    private int serverPort;
    private int webServerCopyFile = 1;

    private int vps = 0;


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
        if (vps == 0) {
            runningBaseFolder = "/home/xanarry/Desktop/running-dir";
        } else {
            runningBaseFolder = "/root/oj/running-dir";
        }
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
        char[] readBuffer  = new char[8 * 1024];

        try {
            //创建socket并得到其输入输出流用于传输数据
            Socket clientSocket = new Socket(serverAddress, serverPort);

            //get inputstream and outputstream from socket
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

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
                writer.write(message);
                writer.flush();
                //outputBufferedStream.flush();



                //read message response from judge server
                int read = reader.read(readBuffer, 0, 8192);//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                responseString = new String(readBuffer);
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
        SqlSession sqlSession = DataSource.getSqlSesion();
        TableSubmitRecord tableSubmitRecord = sqlSession.getMapper(TableSubmitRecord.class);
        tableSubmitRecord.updateSubmitRecord(submitRecordBean);
        sqlSession.commit();
        sqlSession.close();
    }


    private void updateResult(ResultBean resultBean) {
        System.out.println("get result: " + resultBean);


        SqlSession sqlSession = DataSource.getSqlSesion();
        //访问提交记录
        TableSubmitRecord tableSubmitRecord = sqlSession.getMapper(TableSubmitRecord.class);
        //访问评测详细结果
        TableJudgeDetail tableJudgeDetail = sqlSession.getMapper(TableJudgeDetail.class);
        //访问编译结果
        TableCompileInfo tableCompileInfo = sqlSession.getMapper(TableCompileInfo.class);


        SubmitRecordBean submitRecordBean = tableSubmitRecord.getSubmitRecordByID(resultBean.getSubmitID());
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
            tableCompileInfo.insertCompileResult(new CompileInfoBean(submitRecordBean.getSubmitID(), resultBean.getCompileResult()));

            submitRecordBean.setResult(COMPILATION_ERROR);
            submitRecordBean.setTimeConsume(0);
            submitRecordBean.setMemConsume(0);
        } else { //正常运行
            if (resultBean.getCompileResult().trim().length() != 0) { //代码通过,但可能有警告信息
                //保存编译信息到数据库
                tableCompileInfo.insertCompileResult(new CompileInfoBean(submitRecordBean.getSubmitID(), resultBean.getCompileResult()));
            }

            int maxTimeConsume = 0;//最大时间消耗
            int maxMemConsume = 0; //最大内存消耗
            String finalResult = ACCEPTED;

            //遍历没一组测试数据
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
                tableJudgeDetail.insertJudgeDetail(judgeDetailBean);


                judgeDetailBean.setResult(t.getResult());

                //将最终结果设置为最后一个不为通过的结果
                if (!t.getResult().equals(ACCEPTED)) {
                    finalResult = t.getResult();
                }
            }

            submitRecordBean.setResult(finalResult);
            submitRecordBean.setTimeConsume(maxTimeConsume);
            submitRecordBean.setMemConsume(maxMemConsume);
        }

        submitRecordBean.setJudgeTime(new Date().getTime());//设置评测完毕时间

        //更新提交记录
        tableSubmitRecord.updateSubmitRecord(submitRecordBean);
        sqlSession.commit();
        sqlSession.close();
    }





    public static void main(String[] argc) {
    }
}

/*
发送到服务器的json
{
    "submitID":129,
    "language":"C",
    "timeLimit":1000,
    "memLimit":65535,
    "runningFolder":"/home/xanarry/Desktop/running-dir/submit129",
    "testPointDataFolder":"/home/xanarry/Workspace/IdeaProjects/oj/out/artifacts/oj_war_exploded/test-points/p1007"
}


返回结果json
{
    "submitID": 128,
    "systemError": 0,
    "compileStatus": 0,
    "compileResult": "",
    "detail": [
        {
            "testPointID": 1,
            "timeConsume": 0,
            "memConsume": 391748,
            "result": "Memory Limit Exceede"
        },
        {
            "testPointID": 3,
            "timeConsume": 0,
            "memConsume": 391748,
            "result": "Memory Limit Exceede"
        },
        {
            "testPointID": 2,
            "timeConsume": 0,
            "memConsume": 391748,
            "result": "Memory Limit Exceede"
        }
    ]
}
*/
