package org.oj.controller.beans;

import java.util.TreeMap;

public class RankBean {
    private int userID;
    private String userName;
    private int AC_Count;
    private long totalTimeConsume;
    private TreeMap<Integer, UserProblemStatisticBean> problems;

    public RankBean() {}

    public RankBean(int userID, String userName, int AC_Count, long totalTimeConsume, TreeMap<Integer, UserProblemStatisticBean> problems) {
        this.userID = userID;
        this.userName = userName;
        this.AC_Count = AC_Count;
        this.totalTimeConsume = totalTimeConsume;
        this.problems = problems;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAC_Count() {
        return AC_Count;
    }

    public void setAC_Count(int AC_Count) {
        this.AC_Count = AC_Count;
    }

    public long getTotalTimeConsume() {
        return totalTimeConsume;
    }

    public void setTotalTimeConsume(long totalTimeConsume) {
        this.totalTimeConsume = totalTimeConsume;
    }

    public TreeMap<Integer, UserProblemStatisticBean> getProblems() {
        return problems;
    }

    public void setProblems(TreeMap<Integer, UserProblemStatisticBean> problems) {
        this.problems = problems;
    }

    @Override
    public String toString() {
        return "RankBean{" +
                "userID=" + userID +
                ", userName='" + userName + '\'' +
                ", AC_Count=" + AC_Count +
                ", totalTimeConsume=" + totalTimeConsume +
                ", problems=" + problems +
                '}';
    }
}
