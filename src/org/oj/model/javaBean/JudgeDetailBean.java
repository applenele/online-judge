package org.oj.model.javaBean;

/**
 * Created by xanarry on 17-12-30.
 */
public class JudgeDetailBean {
    private int submitID;
    private short testPoint;
    private int timeConsume;
    private int memConsume;
    private short returnVal;
    private String result;

    public JudgeDetailBean() {}

    public JudgeDetailBean(int submitID, short testPoint, int timeConsume, int memConsume, short returnVal, String result) {
        this.submitID = submitID;
        this.testPoint = testPoint;
        this.timeConsume = timeConsume;
        this.memConsume = memConsume;
        this.returnVal = returnVal;
        this.result = result;
    }

    public int getSubmitID() {
        return submitID;
    }

    public void setSubmitID(int submitID) {
        this.submitID = submitID;
    }

    public short getTestPoint() {
        return testPoint;
    }

    public void setTestPoint(short testPoint) {
        this.testPoint = testPoint;
    }

    public int getTimeConsume() {
        return timeConsume;
    }

    public void setTimeConsume(int timeConsume) {
        this.timeConsume = timeConsume;
    }

    public int getMemConsume() {
        return memConsume;
    }

    public void setMemConsume(int memConsume) {
        this.memConsume = memConsume;
    }

    public short getReturnVal() {
        return returnVal;
    }

    public void setReturnVal(short returnVal) {
        this.returnVal = returnVal;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "JudgeDetailBean{" +
                "submitID=" + submitID +
                ", testPoint=" + testPoint +
                ", timeConsume=" + timeConsume +
                ", memConsume=" + memConsume +
                ", returnVal=" + returnVal +
                ", result='" + result + '\'' +
                '}';
    }
}
