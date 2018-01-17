package org.oj.model.javaBean;

/**
 * Created by xanarry on 17-12-30.
 */
public class ContestBean {
    private int contestID;
    private String title;
    private String desc;
    private long startTime;
    private long endTime;
    private long registerStartTime;
    private long registerEndTime;
    private String password;
    private boolean isPublic;
    private String sponsor;
    private String contestType;
    private long createTime;

    public ContestBean() {}

    public ContestBean(int contestID, String title, String desc, long startTime, long endTime, long registerStartTime, long registerEndTime, String password, boolean isPublic, String sponsor, String contestType, long createTime) {
        this.contestID = contestID;
        this.title = title;
        this.desc = desc;
        this.startTime = startTime;
        this.endTime = endTime;
        this.registerStartTime = registerStartTime;
        this.registerEndTime = registerEndTime;
        this.password = password;
        this.isPublic = isPublic;
        this.sponsor = sponsor;
        this.contestType = contestType;
        this.createTime = createTime;
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
                ", password='" + password + '\'' +
                ", isPublic=" + isPublic +
                ", sponsor='" + sponsor + '\'' +
                ", contestType='" + contestType + '\'' +
                ", createTime=" + createTime +
                '}';
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

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getRegisterStartTime() {
        return registerStartTime;
    }

    public void setRegisterStartTime(long registerStartTime) {
        this.registerStartTime = registerStartTime;
    }

    public long getRegisterEndTime() {
        return registerEndTime;
    }

    public void setRegisterEndTime(long registerEndTime) {
        this.registerEndTime = registerEndTime;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
