package org.oj.database;

import org.apache.ibatis.annotations.Param;
import org.oj.model.javaBean.SubmitRecordBean;
import org.oj.model.javaBean.ViewSubmitRecordBean;

import java.util.List;

public interface ViewSubmitRecord {
    //select
    public SubmitRecordBean getSubmitRecordByID(@Param("submitID") Integer submitID);

    public List<ViewSubmitRecordBean> getSubmitRecordListByProblemID(@Param("contestID") Integer contestID, @Param("problemID") Integer problemID);

    public List<ViewSubmitRecordBean> getSubmitRecordListOrderedByDate(@Param("contestID") Integer contestID, @Param("start") Integer start, @Param("count") Integer count);

    public List<ViewSubmitRecordBean> getSubmitRecordListByUserID(@Param("contestID") Integer contestID, @Param("problemID") Integer problemID, @Param("userID") Integer userID,  @Param("result") String result, @Param("language") String language, @Param("start") Integer start, @Param("count") Integer count);

    public List<ViewSubmitRecordBean> getSubmitRecordListByUserName(@Param("contestID") Integer contestID, @Param("problemID") Integer problemID, @Param("userName") String userName, @Param("result") String result, @Param("language") String language, @Param("start") Integer start, @Param("count") Integer count);

}
