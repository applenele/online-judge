package org.oj.database;

import org.apache.ibatis.annotations.Param;
import org.oj.model.javaBean.ProblemBean;

import java.util.List;

/**
 * Created by xanarry on 18-1-1.
 */
public interface TableProblem {
    //insert
    public int addProblem(@Param("problem") ProblemBean problem);

    //delete
    public void deleteProblemByID(@Param("problemID") int problemID);



    //update
    public void updateProblemByID(@Param("problem") ProblemBean problem);

    public void updateSubmittedTimes(@Param("problemID") int problemID);

    public void updateAcceptedTimes(@Param("problemID") int problemID);


    //select
    public ProblemBean getProblemByID(@Param("problemID") int problemID);

    public List<ProblemBean> getProblemsOrderByID(@Param("start") int start, @Param("count") int count);

    public List<ProblemBean> getProblemsOrderByCrateTime(@Param("start") int start, @Param("count") int count);

    public List<ProblemBean> getProblemsOrderByAccepted(@Param("start") int start, @Param("count") int count);

    public List<ProblemBean> getProblemsOrderBySubmitted(@Param("start") int start, @Param("count") int count);

    public List<ProblemBean> getProblemsOrderByAcceptedRate(@Param("start") int start, @Param("count") int count);

}
