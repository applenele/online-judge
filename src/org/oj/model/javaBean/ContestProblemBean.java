package org.oj.model.javaBean;

/**
 * Created by xanarry on 17-12-30.
 */
public class ContestProblemBean {
    private int contestID;
    private int problemID;
    private String innerID;
    private int accepted;
    private int submited;

    public ContestProblemBean() {}

    public ContestProblemBean(int contestID, int problemID, String innerID, int accepted, int submited) {
        this.contestID = contestID;
        this.problemID = problemID;
        this.innerID = innerID;
        this.accepted = accepted;
        this.submited = submited;
    }

    public int getContestID() {
        return contestID;
    }

    public void setContestID(int contestID) {
        this.contestID = contestID;
    }

    public int getProblemID() {
        return problemID;
    }

    public void setProblemID(int problemID) {
        this.problemID = problemID;
    }

    public String getInnerID() {
        return innerID;
    }

    public void setInnerID(String innerID) {
        this.innerID = innerID;
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
        return "ContestProblemBean{" +
                "contestID=" + contestID +
                ", problemID=" + problemID +
                ", innerID='" + innerID + '\'' +
                ", accepted=" + accepted +
                ", submited=" + submited +
                '}';
    }
}
