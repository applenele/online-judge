package org.oj.model.javaBean;

/**
 * Created by xanarry on 17-12-30.
 */
public class DiscussBean {
    private int postID;
    private short type;
    private String postTitle;
    private String postContent;
    private int postTime;
    private int postUser;

    public DiscussBean() {}

    public DiscussBean(int postID, short type, String postTitle, String postContent, int postTime, int postUser) {
        this.postID = postID;
        this.type = type;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.postTime = postTime;
        this.postUser = postUser;
    }

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public int getPostTime() {
        return postTime;
    }

    public void setPostTime(int postTime) {
        this.postTime = postTime;
    }

    public int getPostUser() {
        return postUser;
    }

    public void setPostUser(int postUser) {
        this.postUser = postUser;
    }

    @Override
    public String toString() {
        return "DiscussBean{" +
                "postID=" + postID +
                ", type=" + type +
                ", postTitle='" + postTitle + '\'' +
                ", postContent='" + postContent + '\'' +
                ", postTime=" + postTime +
                ", postUser=" + postUser +
                '}';
    }
}
