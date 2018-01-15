package judge.beans;

import java.util.List;

/**
 * Created by xanarry on 18-1-8.
 */

/*
{
    "submitID":1,
    "systemError":1,
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
    private int systemError;
    private int compileStatus;
    private String compileResult;
    private List<TestPointResultBean> detail;

    public ResultBean() {
    }

    public ResultBean(int submitID, int systemError, int compileStatus, String compileResult, List<TestPointResultBean> detail) {
        this.submitID = submitID;
        this.systemError = systemError;
        this.compileStatus = compileStatus;
        this.compileResult = compileResult;
        this.detail = detail;
    }

    public int getSubmitID() {
        return submitID;
    }

    public void setSubmitID(int submitID) {
        this.submitID = submitID;
    }

    public int getSystemError() {
        return systemError;
    }

    public void setSystemError(int systemError) {
        this.systemError = systemError;
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

    public List<TestPointResultBean> getDetail() {
        return detail;
    }

    public void setDetail(List<TestPointResultBean> detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "ResultBean{" +
                "submitID=" + submitID +
                ", systemError=" + systemError +
                ", compileStatus=" + compileStatus +
                ", compileResult='" + compileResult + '\'' +
                ", detail=" + detail +
                '}';
    }
}
