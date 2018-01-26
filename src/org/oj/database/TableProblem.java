package org.oj.database;

import org.apache.ibatis.annotations.Param;
import org.oj.model.javaBean.ProblemBean;

import java.util.List;

/**
 * Created by xanarry on 18-1-1.
 */
public interface TableProblem extends BaseFunction {
    //insert
    int addProblem(@Param("problem") ProblemBean problem);

    //delete
    void deleteProblemByID(@Param("problemID") int problemID);



    //update
    void updateProblemByID(@Param("problem") ProblemBean problem);

    void updateSubmittedTimes(@Param("problemID") int problemID);

    void updateAcceptedTimes(@Param("problemID") int problemID);


    //select
    ProblemBean getProblemByID(@Param("problemID") int problemID);

    List<ProblemBean> getProblemsOrderByID(@Param("start") int start, @Param("count") int count);

    List<ProblemBean> getProblemsOrderByCrateTime(@Param("start") int start, @Param("count") int count);

    List<ProblemBean> getProblemsOrderByAccepted(@Param("start") int start, @Param("count") int count);

    List<ProblemBean> getProblemsOrderBySubmitted(@Param("start") int start, @Param("count") int count);

    List<ProblemBean> getProblemsOrderByAcceptedRate(@Param("start") int start, @Param("count") int count);

    /*搜索题目*/
    List<ProblemBean> searchProblem(@Param("problemKeyword") String problemKeyword, @Param("start") int start, @Param("count") int count);
    int getSearchResultCount(@Param("problemKeyword") String problemKeyword);

}
