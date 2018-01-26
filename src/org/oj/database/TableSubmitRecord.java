package org.oj.database;

import org.apache.ibatis.annotations.Param;
import org.oj.model.javaBean.SubmitRecordBean;

import java.util.List;

/**
 * Created by xanarry on 18-1-1.
 */
public interface TableSubmitRecord {
    //insert
    public int addSubmitRecord(@Param("submitRecord") SubmitRecordBean submitRecord);

    //delete
    void deleteSubmitRecord(@Param("submitID") int submitID);
    void deleteSubmitRecordByUserID(@Param("userID") int userID);

    //update
    //insert
    public int updateSubmitRecord(@Param("submitRecord") SubmitRecordBean submitRecord);

    //select
    public SubmitRecordBean getSubmitRecordByID(@Param("submitID") int submitID);

    public List<SubmitRecordBean> getSubmitRecordListByProblemID(@Param("problemID") int problemID);

    public List<SubmitRecordBean> getSubmitRecordListByUserID(@Param("userID") int userID, @Param("start") int start, @Param("count") int count);

    public List<SubmitRecordBean> getSubmitRecordListByProblemUser(@Param("problemID") int problemID, @Param("userID") int userID, @Param("start") int start, @Param("count") int count);

    public List<SubmitRecordBean> getSubmitRecordListOrderedByDate(@Param("start") int start, @Param("count") int count);

    public List<SubmitRecordBean> getSubmitRecordList(@Param("contestID") Integer contestID, @Param("problemID") Integer problemID, @Param("userID") Integer userID, @Param("result") String result, @Param("language") String language, @Param("start") int start, @Param("count") int count);
}
