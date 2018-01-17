package org.oj.database;

import org.apache.ibatis.annotations.Param;
import org.oj.model.javaBean.ContestBean;

import java.util.List;

/**
 * Created by xanarry on 18-1-1.
 */
public interface TableContest {
    public int addContest(@Param("contest") ContestBean contest);

    public void deleteContest(@Param("contestID") Integer contestID);

    public void updateContest(@Param("contest") ContestBean contest);

    public void getContestByID(@Param("contestID") Integer contestID);

    public void getContestByTitle(@Param("title") String title);

    public List<ContestBean> getContestList(@Param("start") Integer start, @Param("count") Integer count);
}
