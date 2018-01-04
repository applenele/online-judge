package org.oj.model.javaBean;

/**
 * Created by xanarry on 17-12-30.
 */
public class ContestBean {
    private int contestID;
    private String title;
    private String desc;
    private int startTime;
    private int endTime;
    private int registerStartTime;
    private int registerEndTime;
    private boolean isPublic;
    private String sponsor;
    private String contestType;

    public ContestBean() {}

    public ContestBean(int contestID, String title, String desc, int startTime, int endTime, int registerStartTime, int registerEndTime, boolean isPublic, String sponsor, String contestType) {
        this.contestID = contestID;
        this.title = title;
        this.desc = desc;
        this.startTime = startTime;
        this.endTime = endTime;
        this.registerStartTime = registerStartTime;
        this.registerEndTime = registerEndTime;
        this.isPublic = isPublic;
        this.sponsor = sponsor;
        this.contestType = contestType;
    }

    public int getContestID() {
        return contestID;
    }

    public void setContestID(int contestID) {
        this.contestID = contestID;
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

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public int getRegisterStartTime() {
        return registerStartTime;
    }

    public void setRegisterStartTime(int registerStartTime) {
        this.registerStartTime = registerStartTime;
    }

    public int getRegisterEndTime() {
        return registerEndTime;
    }

    public void setRegisterEndTime(int registerEndTime) {
        this.registerEndTime = registerEndTime;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public String getContestType() {
        return contestType;
    }

    public void setContestType(String contestType) {
        this.contestType = contestType;
    }

    @Override
    public String toString() {
        return "ContestBean{" +
                "contestID=" + contestID +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", registerStartTime=" + registerStartTime +
                ", registerEndTime=" + registerEndTime +
                ", isPublic=" + isPublic +
                ", sponsor='" + sponsor + '\'' +
                ", contestType='" + contestType + '\'' +
                '}';
    }
}
