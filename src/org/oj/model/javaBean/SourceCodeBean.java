package org.oj.model.javaBean;

/**
 * Created by xanarry on 17-12-30.
 */
public class SourceCodeBean {
    private int submitID;
    private String sourceCode;

    public SourceCodeBean() {}

    public SourceCodeBean(int submitID, String sourceCode) {
        this.submitID = submitID;
        this.sourceCode = sourceCode;
    }

    public int getSubmitID() {
        return submitID;
    }

    public void setSubmitID(int submitID) {
        this.submitID = submitID;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    @Override
    public String toString() {
        return "SourceCodeBean{" +
                "submitID=" + submitID +
                ", sourceCode='" + sourceCode + '\'' +
                '}';
    }
}
