package org.oj.model.javaBean;

/**
 * Created by xanarry on 17-12-30.
 */
public class ContestUserBean {
    private int contestID;
    private int userID;

    public ContestUserBean() {}

    public ContestUserBean(int contestID, int userID) {
        this.contestID = contestID;
        this.userID = userID;
    }

    public int getContestID() {
        return contestID;
    }

    public void setContestID(int contestID) {
        this.contestID = contestID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "ContestUserBean{" +
                "contestID=" + contestID +
                ", userID=" + userID +
                '}';
    }
}
