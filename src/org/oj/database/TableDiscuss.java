package org.oj.database;

import org.apache.ibatis.annotations.Param;
import org.oj.model.javaBean.DiscussBean;

import java.util.List;

/**
 * Created by xanarry on 18-1-1.
 */
public interface TableDiscuss {
    int insertDiscuss(@Param("discuss")DiscussBean discuss);


    void setAsRoot(@Param("discuss") DiscussBean discuss);
    void addWatch(@Param("postID") Integer postID);
    void addReply(@Param("postID") Integer postID);

    void deleteDiscussByPostID(@Param("postID") Integer postID);
    void deleteDiscussByRootID(@Param("rootID") Integer rootID);
    void deleteDiscussByUserD(@Param("userID") Integer userID);

    DiscussBean getDiscussByPostID(@Param("postID") Integer postID);
    List<DiscussBean> getDiscussListByRootID(@Param("rootID") Integer rootID);
    List<DiscussBean> getDiscussListByPorcID(@Param("type") Integer type, @Param("porcID") Integer porcID);
}
