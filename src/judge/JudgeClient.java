package judge;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import judge.beans.*;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.session.SqlSession;
import org.oj.database.*;
import org.oj.model.javaBean.*;

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


    private final String SUBMIT_STATE = "submit";
    private final String WAIT_STATE   = "wait";
    private final String FINISH_STATE = "finish";
    private final String STATE        = "state";


    //等待队列需要被多个对象同时共享, 属于此类
    private static LinkedBlockingQueue<SubmitQueueElement> submitQueue = new LinkedBlockingQueue<SubmitQueueElement>();


    private String testPointBaseDir;
    private String runningBaseDir;
    private String serverAddress;
    private int serverPort;

    public JudgeClient(ConfigurationBean configuration) {
        this.testPointBaseDir = configuration.getTestPointBaseDir();
        this.runningBaseDir = configuration.getRunningBaseDir();
        this.serverAddress = configuration.getServerAddress();
        this.serverPort = configuration.getServerPort();
        logConfig();
    }

    public JudgeClient(String serverAddress, int serverPort, String runningBaseDir, String testPointBaseDir) {
        this.testPointBaseDir = testPointBaseDir;
        this.runningBaseDir = runningBaseDir;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        logConfig();
    }

    private void logConfig() {
        System.out.println("judge client:");
        System.out.println("          server: " + serverAddress);
        System.out.println("            port: " + serverPort);
        System.out.println("  runningBaseDir: " + runningBaseDir);
        System.out.println("testPointBaseDir: " + testPointBaseDir);
    }


    @Override
    public void run() {
        super.run();
        //submitToJudgeServer函数, 死循环, 不断从阻塞提交队里中取出提交信息, 然后发送到judge Server
        while (true) {
            SubmitQueueElement submitQueueElement = null;
            try {
                submitQueueElement = submitQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
                //update result as system error
            }

            if (submitQueueElement != null) {
                submitToJudgeServer(submitQueueElement);
            }
        }
    }



    /*submit仅仅将数据提交到等待队里*/
    public void submit(SubmitRecordBean submitRecordBean, ProblemBean problemBean, String testPointDataPath) {
        /*复制必要的文件到运行目录, 包括代码文件, 测试点文件*/
        String runningDir = prepareFiles(submitRecordBean, testPointDataPath);
        if (runningDir == null) {
            //在准备文件的过程中产生了错误, 尝试删除文件
            onStateChange(submitRecordBean, JudgeClient.SYSTEM_ERROR, "复制运行文件错误!");
            return;
        }

        //创建提交到judge server的bean
        SubmitBean submitBean = new SubmitBean();
        //设置提交ID
        submitBean.setSubmitID(submitRecordBean.getSubmitID());
        //设置语言类型
        submitBean.setLanguage(submitRecordBean.getLanguage());
        //选择时间和内存的限制
        String language = submitRecordBean.getLanguage().toLowerCase();
        if (language.equals("c") || language.equals("c++") || language.equals("cpp")) {
            /*静态语言*/
            submitBean.setTimeLimit(problemBean.getStaticLangTimeLimit());
            submitBean.setMemLimit(problemBean.getStaticLangMemLimit());
        } else {
            /*动态语言*/
            submitBean.setTimeLimit(problemBean.getDynamicLangTimeLimit());
            submitBean.setMemLimit(problemBean.getDynamicLangMemLimit());
        }


        submitBean.setRunningFolder(runningDir);
        submitBean.setTestPointDataFolder(testPointDataPath);

        try {
            //提交队列中
            submitQueue.put(new SubmitQueueElement(submitRecordBean, submitBean));
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            onStateChange(submitRecordBean, JudgeClient.SYSTEM_ERROR, "提交到等待队列失败");
        }
    }



    //与judge server通信
    private void submitToJudgeServer(SubmitQueueElement queElement) {
        /*
        * this routine has three step
        * 1: 将数据提交到judge server
        * 2: 在提交数据之后和得到结果之前, 和judge server保持通信状态, 交换评测状态
        * 3: 得到最后评测结果
        * */

        /*从阻塞队列中取出提交信息*/
        SubmitRecordBean submitRecordBean = queElement.getSubmitRecordBean();/*提交记录本身*/
        SubmitBean submitBean = queElement.getSubmitBean();/*转换为json发送到judge server的对象*/

        final int BUF_SIZE = 8 * 1024;

        char[] readBuffer  = new char[BUF_SIZE];

        try {
            //创建socket并得到其输入输出流用于传输数据
            Socket clientSocket = new Socket(serverAddress, serverPort);

            //从套接字获取输入输出流
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            //发送json到judge server
            String message;
            boolean hasSubmit = false;

            /*
            * 与judge server通信的字符串格式: 状态+空格+json
            * */
            while (true) {
                if (!hasSubmit) { //first time send submit information
                    //将json发送到judge server后将状态更改为等待
                    message = SUBMIT_STATE + " " + submitBean.toJson();
                    hasSubmit = true;
                } else {
                    message = WAIT_STATE;
                }

                System.out.println("SEND: " + message);
                //发送json到server
                writer.write(message);//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                writer.flush();


                //读取server的响应信息
                reader.read(readBuffer, 0, BUF_SIZE);//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                message = new String(readBuffer);
                System.out.println("RECV: " + message);



                if (message.startsWith(FINISH_STATE)) {
                    /*如server表示这是一条结束信息, 那么不在停止和server交换信息*/
                    break;
                } else if (message.startsWith(STATE)) {
                    /*如果这是一条信息交换信息, 将服务器响应的json转换为对象*/
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    JudgeStateBean judgeStateBean = gson.fromJson(message.substring(STATE.length()).trim(), JudgeStateBean.class);
                    /*更新信息*/
                    onStateChange(submitRecordBean, judgeStateBean.getState(), "");
                }
            }


            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            ResultBean resultBean = gson.fromJson(message.substring(FINISH_STATE.length()).trim(), ResultBean.class);

            onFinishJudge(submitRecordBean, resultBean);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            onStateChange(submitRecordBean, JudgeClient.SYSTEM_ERROR, e.getMessage());
        }
    }




    public void onStateChange(SubmitRecordBean submit, String state, String detail) {
        /*评测中间状态变化, 处理以下四种情况
        * Queuing
        * Compiling
        * Running
        * System Error
        * */
        submit.setResult(state);
        submit.setJudgeTime(new Date().getTime());

        SqlSession sqlSession = DataSource.getSqlSesion();
        TableSubmitRecord tableSubmitRecord = sqlSession.getMapper(TableSubmitRecord.class);
        tableSubmitRecord.updateSubmitRecord(submit);

        /*如果是system error, 保持错误信息*/
        if (state.equals(JudgeClient.SYSTEM_ERROR)) {
            TableSystemError tableSystemError = sqlSession.getMapper(TableSystemError.class);
            tableSystemError.addErrorMessage(new SystemErrorBean(submit.getSubmitID(), detail));
            /*发生了系统错误, 无法继续评测, 删除运行目录*/
            deleteRunningDir(getRunningDir(submit.getSubmitID()));
        }
        sqlSession.commit();
        sqlSession.close();
    }

    public void onFinishJudge(SubmitRecordBean submit, ResultBean resultBean) {
        /*处理评测机正常评测完毕, 有以下几种结果
        * Compilation Error
        * Accepted
        * Presentation Error
        * Wrong Answer
        * Time Limit Exceeded
        * Memory Limit Exceeded
        * Output Limit Exceeded
        * Runtime Error
        * */

        System.out.println("get result: " + resultBean);

        SqlSession sqlSession = DataSource.getSqlSesion();
        //访问提交记录
        TableSubmitRecord tableSubmitRecord = sqlSession.getMapper(TableSubmitRecord.class);
        //访问评测详细结果
        TableJudgeDetail tableJudgeDetail = sqlSession.getMapper(TableJudgeDetail.class);
        //访问编译结果
        TableCompileInfo tableCompileInfo = sqlSession.getMapper(TableCompileInfo.class);


        SubmitRecordBean submitRecordBean = tableSubmitRecord.getSubmitRecordByID(resultBean.getSubmitID());
        List<TestPointResultBean> testPointResults = resultBean.getJudgeDetail();

        submitRecordBean.setTimeConsume(0);
        submitRecordBean.setMemConsume(0);
        submitRecordBean.setJudgeTime(new Date().getTime());

        //系统错误
        if (resultBean.getErrorMessage().trim().length() > 0) {
            submitRecordBean.setResult(SYSTEM_ERROR);
            submitRecordBean.setTimeConsume(0);
            submitRecordBean.setMemConsume(0);
        } else if (resultBean.getCompileResult() != 0) {
            //编译错误
            tableCompileInfo.insertCompileResult(new CompileInfoBean(submit.getSubmitID(), resultBean.getCompilerOutput()));

            submitRecordBean.setResult(COMPILATION_ERROR);
            submitRecordBean.setTimeConsume(0);
            submitRecordBean.setMemConsume(0);
        } else { //正常运行
            if (resultBean.getCompilerOutput().trim().length() != 0) { //代码通过,但可能有警告信息
                //保存编译信息到数据库
                tableCompileInfo.insertCompileResult(new CompileInfoBean(submit.getSubmitID(), resultBean.getCompilerOutput()));
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

        String runningDir = getRunningDir(submit.getSubmitID());
        deleteRunningDir(runningDir);
    }



    private String getRunningDir(int submitID) {
        return runningBaseDir + "/submit" + submitID;
    }


    private void deleteRunningDir(String runningDir) {
        File dir = new File(runningDir);
        if (dir.exists()) {
            try {
                FileUtils.deleteDirectory(dir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


















    //准备运行所需要的文件
    private String prepareFiles(SubmitRecordBean submitRecordBean, String testPointDataPath) {
        //创建运行目录根目录
        File file = new File(runningBaseDir);
        if (!file.exists()) { //不存在则创建目录
            if (!file.mkdir()) {
                //创建运行根目录失败
                onStateChange(submitRecordBean, JudgeClient.SYSTEM_ERROR, "创建运行根目录失败!");
                return null;
            }
        }

        //根据提交ID为每次提交创建一个目录,
        String runningFolder = runningBaseDir + "/submit" + submitRecordBean.getSubmitID();
        File f = new File(runningFolder);
        if (!f.mkdir()) {
            //创建题目运行文件夹失败
            onStateChange(submitRecordBean, JudgeClient.SYSTEM_ERROR, "创建题目(" + submitRecordBean.getProblemID() + ")的运行根目录失败!");
            return null;
        }

        //将源代码写入到运行目录: runningBaseDir/submit(problemID)/Main.*
        if (writeCodeToFile(submitRecordBean, runningFolder)) {
            //源代码写入成功之后, 复制测试点文件到运行目录
            if (copyTestPointFile(testPointDataPath, runningFolder)) {
                //文件准备就绪
                return runningFolder;//返回运行的文件夹
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

    //写入代码到文件
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

    /*复制测试点文件*/
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
