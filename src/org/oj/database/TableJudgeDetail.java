package org.oj.database;

import org.apache.ibatis.annotations.Param;
import org.oj.model.javaBean.JudgeDetailBean;

import java.util.List;

/**
 * Created by xanarry on 18-1-1.
 */
public interface TableJudgeDetail {
    //insert
    public int insertJudgeDetail(@Param("judgeDetail") JudgeDetailBean judgeDetail);

    //delete
    public void deleteJudgeDetail(@Param("submitID") int submitID);

    //update rejudge may user this function
    public void updateJudegeDetail(@Param("newJudgeDetail") JudgeDetailBean newJudgeDetail);

    //select
    public List<JudgeDetailBean> getJudegeDetailBySubmitID(@Param("submitID") int submitID);

    public JudgeDetailBean getJudgeDetailByTestPointID(@Param("submitID") int submitID, @Param("testPointID") int testPointID);
}
