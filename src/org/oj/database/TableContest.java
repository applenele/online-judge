package org.oj.database;

import org.apache.ibatis.annotations.Param;
import org.oj.model.javaBean.ContestBean;

import java.util.List;

/**
 * Created by xanarry on 18-1-1.
 */
public interface TableContest extends BaseFunction {
    int addContest(@Param("contest") ContestBean contest);

    void deleteContest(@Param("contestID") Integer contestID);

    void updateContest(@Param("contest") ContestBean contest);

    ContestBean getContestByID(@Param("contestID") Integer contestID);

    void getContestByTitle(@Param("title") String title);

    List<ContestBean> getContestList(@Param("start") Integer start, @Param("count") Integer count);

}
