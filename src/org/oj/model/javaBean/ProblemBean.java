package org.oj.model.javaBean;

/**
 * Created by xanarry on 17-12-30.
 */
public class ProblemBean {
    private int problemID;
    private String title;
    private String desc;
    private String inputDesc;
    private String outputDesc;
    private String inputSample;
    private String outputSample;
    private String hint;
    private String source;
    private int createTime;
    private int staticLangTimeLimit;
    private int staticLangMemLimit;
    private int dynamicLangTimeLimit;
    private int dynamicLangMemLimit;
    private int accepted;
    private int submited;

    public ProblemBean() {}

    public ProblemBean(int problemID, String title, String desc, String inputDesc, String outputDesc, String inputSample, String outputSample, String hint, String source, int createTime, int staticLangTimeLimit, int staticLangMemLimit, int dynamicLangTimeLimit, int dynamicLangMemLimit, int accepted, int submited) {
        this.problemID = problemID;
        this.title = title;
        this.desc = desc;
        this.inputDesc = inputDesc;
        this.outputDesc = outputDesc;
        this.inputSample = inputSample;
        this.outputSample = outputSample;
        this.hint = hint;
        this.source = source;
        this.createTime = createTime;
        this.staticLangTimeLimit = staticLangTimeLimit;
        this.staticLangMemLimit = staticLangMemLimit;
        this.dynamicLangTimeLimit = dynamicLangTimeLimit;
        this.dynamicLangMemLimit = dynamicLangMemLimit;
        this.accepted = accepted;
        this.submited = submited;
    }

    public int getProblemID() {
        return problemID;
    }

    public void setProblemID(int problemID) {
        this.problemID = problemID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getInputDesc() {
        return inputDesc;
    }

    public void setInputDesc(String inputDesc) {
        this.inputDesc = inputDesc;
    }

    public String getOutputDesc() {
        return outputDesc;
    }

    public void setOutputDesc(String outputDesc) {
        this.outputDesc = outputDesc;
    }

    public String getInputSample() {
        return inputSample;
    }

    public void setInputSample(String inputSample) {
        this.inputSample = inputSample;
    }

    public String getOutputSample() {
        return outputSample;
    }

    public void setOutputSample(String outputSample) {
        this.outputSample = outputSample;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public int getStaticLangTimeLimit() {
        return staticLangTimeLimit;
    }

    public void setStaticLangTimeLimit(int staticLangTimeLimit) {
        this.staticLangTimeLimit = staticLangTimeLimit;
    }

    public int getStaticLangMemLimit() {
        return staticLangMemLimit;
    }

    public void setStaticLangMemLimit(int staticLangMemLimit) {
        this.staticLangMemLimit = staticLangMemLimit;
    }

    public int getDynamicLangTimeLimit() {
        return dynamicLangTimeLimit;
    }

    public void setDynamicLangTimeLimit(int dynamicLangTimeLimit) {
        this.dynamicLangTimeLimit = dynamicLangTimeLimit;
    }

    public int getDynamicLangMemLimit() {
        return dynamicLangMemLimit;
    }

    public void setDynamicLangMemLimit(int dynamicLangMemLimit) {
        this.dynamicLangMemLimit = dynamicLangMemLimit;
    }

    public int getAccepted() {
        return accepted;
    }

    public void setAccepted(int accepted) {
        this.accepted = accepted;
    }

    public int getSubmited() {
        return submited;
    }

    public void setSubmited(int submited) {
        this.submited = submited;
    }

    @Override
    public String toString() {
        return "ProblemBean{" +
                "problemID=" + problemID +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", inputDesc='" + inputDesc + '\'' +
                ", outputDesc='" + outputDesc + '\'' +
                ", inputSample='" + inputSample + '\'' +
                ", outputSample='" + outputSample + '\'' +
                ", hint='" + hint + '\'' +
                ", source='" + source + '\'' +
                ", createTime=" + createTime +
                ", staticLangTimeLimit=" + staticLangTimeLimit +
                ", staticLangMemLimit=" + staticLangMemLimit +
                ", dynamicLangTimeLimit=" + dynamicLangTimeLimit +
                ", dynamicLangMemLimit=" + dynamicLangMemLimit +
                ", accepted=" + accepted +
                ", submited=" + submited +
                '}';
    }
}
