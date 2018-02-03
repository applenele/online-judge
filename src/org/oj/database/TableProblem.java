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

    void updateSubmittedTimes(@Param("problemID") Integer problemID);

    void updateAcceptedTimes(@Param("problemID") Integer problemID);


    //select
    ProblemBean getProblemByID(@Param("problemID") Integer problemID);

    List<ProblemBean> getProblemsOrderByID(@Param("start") Integer start, @Param("count") Integer count);
    List<ProblemBean> getProblesOrderByIDForLogin(@Param("userID") Integer userID, @Param("result") String result, @Param("start") Integer start, @Param("count") Integer count);


    List<ProblemBean> getProblemsOrderByAccepted(@Param("start") Integer start, @Param("count") Integer count);

    List<ProblemBean> getProblemsOrderBySubmitted(@Param("start") Integer start, @Param("count") Integer count);

    List<ProblemBean> getProblemsOrderByAcceptedRate(@Param("start") Integer start, @Param("count") Integer count);

    /*搜索题目*/
    List<ProblemBean> searchProblem(@Param("problemID") Integer problemID, @Param("problemKeyword") String problemKeyword, @Param("start") Integer start, @Param("count") Integer count);
    int getSearchResultCount(@Param("problemID") Integer problemID, @Param("problemKeyword") String problemKeyword);

}
