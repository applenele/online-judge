package org.oj.model.javaBean;

/**
 * Created by xanarry on 17-12-30.
 */
public class SubmitRecordBean {
    private int submitID;
    private int userID;
    private int problemID;
    private int contestID;
    private String result;
    private String language;
    private String sourceCode;
    private int codeLength;
    private int timeConsume;
    private int memConsume;
    private long submitTime;
    private long judgeTime;

    public SubmitRecordBean() {
    }

    public SubmitRecordBean(int submitID, int userID, int problemID, int contestID, String result, String language, String sourceCode, int codeLength, int timeConsume, int memConsume, long submitTime, long judgeTime) {
        this.submitID = submitID;
        this.userID = userID;
        this.problemID = problemID;
        this.contestID = contestID;
        this.result = result;
        this.language = language;
        this.sourceCode = sourceCode;
        this.codeLength = codeLength;
        this.timeConsume = timeConsume;
        this.memConsume = memConsume;
        this.submitTime = submitTime;
        this.judgeTime = judgeTime;
    }

    public int getSubmitID() {
        return submitID;
    }

    public void setSubmitID(int submitID) {
        this.submitID = submitID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getProblemID() {
        return problemID;
    }

    public void setProblemID(int problemID) {
        this.problemID = problemID;
    }

    public int getContestID() {
        return contestID;
    }

    public void setContestID(int contestID) {
        this.contestID = contestID;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public int getCodeLength() {
        return codeLength;
    }

    public void setCodeLength(int codeLength) {
        this.codeLength = codeLength;
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

    public long getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(long submitTime) {
        this.submitTime = submitTime;
    }

    public long getJudgeTime() {
        return judgeTime;
    }

    public void setJudgeTime(long judgeTime) {
        this.judgeTime = judgeTime;
    }

    @Override
    public String toString() {
        return "SubmitRecordBean{" +
                "submitID=" + submitID +
                ", userID=" + userID +
                ", problemID=" + problemID +
                ", contestID=" + contestID +
                ", result='" + result + '\'' +
                ", language='" + language + '\'' +
                ", sourceCode='" + sourceCode + '\'' +
                ", codeLength=" + codeLength +
                ", timeConsume=" + timeConsume +
                ", memConsume=" + memConsume +
                ", submitTime=" + submitTime +
                ", judgeTime=" + judgeTime +
                '}';
    }
}
