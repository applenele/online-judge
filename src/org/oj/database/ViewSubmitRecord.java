package org.oj.database;

import org.apache.ibatis.annotations.Param;
import org.oj.model.javaBean.SubmitRecordBean;

import java.util.List;

public interface ViewSubmitRecord {
    //select
    public SubmitRecordBean getSubmitRecordByID(@Param("submitID") int submitID);

    public List<SubmitRecordBean> getSubmitRecordListByProblemID(@Param("problemID") int problemID);

    public List<SubmitRecordBean> getSubmitRecordListOrderedByDate(@Param("start") int start, @Param("count") int count);

    public List<SubmitRecordBean> getSubmitRecordListByUserID(@Param("userID") int userID, @Param("problemID") Integer problemID, @Param("result") String result, @Param("language") String language, @Param("start") int start, @Param("count") int count);

    public List<SubmitRecordBean> getSubmitRecordListByUserName(@Param("userName") String userName, @Param("problemID") Integer problemID, @Param("result") String result, @Param("language") String language, @Param("start") int start, @Param("count") int count);

}
