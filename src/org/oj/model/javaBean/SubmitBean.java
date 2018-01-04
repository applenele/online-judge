package org.oj.model.javaBean;

/**
 * Created by xanarry on 17-12-30.
 */
public class SubmitBean {
    private int submitID;
    private String compileInfo;

    public SubmitBean(int submitID, String compileInfo) {
        this.submitID = submitID;
        this.compileInfo = compileInfo;
    }

    public SubmitBean() {}

    public int getSubmitID() {
        return submitID;
    }

    public void setSubmitID(int submitID) {
        this.submitID = submitID;
    }

    public String getCompileInfo() {
        return compileInfo;
    }

    public void setCompileInfo(String compileInfo) {
        this.compileInfo = compileInfo;
    }

    @Override
    public String toString() {
        return "SubmitBean{" +
                "submitID=" + submitID +
                ", compileInfo='" + compileInfo + '\'' +
                '}';
    }
}
