package org.oj.database;

import org.apache.ibatis.annotations.Param;
import org.oj.model.javaBean.ContestProblemBean;

import java.util.List;

/**
 * Created by xanarry on 18-1-1.
 */
public interface TableContestProblem {
    public int addProlem(@Param("contestProblem") ContestProblemBean contestProblem);

    public void deleteProblem(@Param("contestID") Integer contestID, @Param("innerID") String innerID);

    public void deleteAllProblem(@Param("contestID") Integer contestID);

    public void updateProblem(@Param("contestProblem") ContestProblemBean contestProblem);

    public ContestProblemBean getContestProblem(@Param("contestID") Integer contestID, @Param("problemID") int problemID);

    public List<ContestProblemBean> getContestProblemList(@Param("contestID") Integer contestID);
}
