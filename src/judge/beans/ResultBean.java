package judge.beans;

import java.util.List;

/**
 * Created by xanarry on 18-1-8.
 */

/*
{
    "submitID":1,
    "errorMessage":1,
    "compileResult":1,
    "compilerOutput":"compile error",
    "result":
    [
        {"testPointID":1,"timeConsume":234,"memConsume":3421,"returnVal":0,"result":"accepted"},
        {"testPointID":1,"timeConsume":234,"memConsume":3421,"returnVal":0,"result":"accepted"},
        {"testPointID":1,"timeConsume":234,"memConsume":3421,"returnVal":0,"result":"accepted"},
        {"testPointID":1,"timeConsume":234,"memConsume":3421,"returnVal":0,"result":"accepted"}
    ]
}
*/


public class ResultBean {
    private int submitID;
    private String errorMessage;
    private int    compileResult;
    private String compilerOutput;
    private List<TestPointResultBean> judgeDetail;

    public ResultBean() {
    }

    public ResultBean(int submitID, String errorMessage, int compileResult, String compilerOutput, List<TestPointResultBean> judgeDetail) {
        this.submitID = submitID;
        this.errorMessage = errorMessage;
        this.compileResult = compileResult;
        this.compilerOutput = compilerOutput;
        this.judgeDetail = judgeDetail;
    }

    public int getSubmitID() {
        return submitID;
    }

    public void setSubmitID(int submitID) {
        this.submitID = submitID;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getCompileResult() {
        return compileResult;
    }

    public void setCompileResult(int compileResult) {
        this.compileResult = compileResult;
    }

    public String getCompilerOutput() {
        return compilerOutput;
    }

    public void setCompilerOutput(String compilerOutput) {
        this.compilerOutput = compilerOutput;
    }

    public List<TestPointResultBean> getJudgeDetail() {
        return judgeDetail;
    }

    public void setJudgeDetail(List<TestPointResultBean> judgeDetail) {
        this.judgeDetail = judgeDetail;
    }

    @Override
    public String toString() {
        return "ResultBean{" +
                "submitID=" + submitID +
                ", errorMessage='" + errorMessage + '\'' +
                ", compileResult=" + compileResult +
                ", compilerOutput='" + compilerOutput + '\'' +
                ", judgeDetail=" + judgeDetail +
                '}';
    }
}
