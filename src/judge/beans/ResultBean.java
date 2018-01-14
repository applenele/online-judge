package judge.beans;

import java.util.List;

/**
 * Created by xanarry on 18-1-8.
 */

/*
{
    "submitID":1,
    "compileStatus":1,
    "compileResult":"compile error",
    "result":
    [
        {"testPointID":1, "result":"accepted"},
        {"testPointID":2, "result":"accepted"},
        {"testPointID":3, "result":"accepted"},
        {"testPointID":4, "result":"accepted"}
    ]
}
*/


public class ResultBean {
    private int submitID;
    private int compileStatus;
    private String compileResult;
    private List<TestPointResultBean> testPointResult;

    public ResultBean() {
    }

    public ResultBean(int submitID, int compileStatus, String compileResult, List<TestPointResultBean> testPointResult) {
        this.submitID = submitID;
        this.compileStatus = compileStatus;
        this.compileResult = compileResult;
        this.testPointResult = testPointResult;
    }

    public int getSubmitID() {
        return submitID;
    }

    public void setSubmitID(int submitID) {
        this.submitID = submitID;
    }

    public int getCompileStatus() {
        return compileStatus;
    }

    public void setCompileStatus(int compileStatus) {
        this.compileStatus = compileStatus;
    }

    public String getCompileResult() {
        return compileResult;
    }

    public void setCompileResult(String compileResult) {
        this.compileResult = compileResult;
    }

    public List<TestPointResultBean> getTestPointResult() {
        return testPointResult;
    }

    public void setTestPointResult(List<TestPointResultBean> testPointResult) {
        this.testPointResult = testPointResult;
    }

    @Override
    public String toString() {
        return "ResultBean{" +
                "submitID=" + submitID +
                ", compileStatus=" + compileStatus +
                ", compileResult='" + compileResult + '\'' +
                ", testPointResult=" + testPointResult +
                '}';
    }
}
