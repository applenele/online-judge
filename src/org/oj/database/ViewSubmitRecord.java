package org.oj.database;

import org.apache.ibatis.annotations.Param;
import org.oj.model.javaBean.SubmitRecordBean;
import org.oj.model.javaBean.ViewSubmitRecordBean;

import java.util.List;

public interface ViewSubmitRecord extends BaseFunction {
    //select
    SubmitRecordBean getSubmitRecordByID(@Param("submitID") Integer submitID);

    List<ViewSubmitRecordBean> getSubmitRecordListByProblemID(@Param("contestID") Integer contestID, @Param("problemID") Integer problemID);

    List<ViewSubmitRecordBean> getSubmitRecordListOrderedByDate(@Param("contestID") Integer contestID, @Param("start") Integer start, @Param("count") Integer count);

    List<Integer> getUserAcceptedProblems(@Param("userID") Integer userID);

    int getCountOnCondition(@Param("contestID") Integer contestID, @Param("problemID") Integer problemID, @Param("userName") String userName, @Param("result") String result, @Param("language") String language);
    List<ViewSubmitRecordBean> getSubmitRecordList(@Param("contestID") Integer contestID, @Param("problemID") Integer problemID, @Param("userName") String userName, @Param("result") String result, @Param("language") String language, @Param("start") Integer start, @Param("count") Integer count);
}
