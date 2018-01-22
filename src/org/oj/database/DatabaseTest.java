package org.oj.database;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.oj.controller.beans.RankBean;
import org.oj.model.javaBean.*;
import utils.Tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by xanarry on 17-4-22.
 */
public class DatabaseTest {
    public static void main(String[] argv) throws IOException {
        String resource = "org/oj/database/mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);

        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            TableContest tableContest = sqlSession.getMapper(TableContest.class);
            TableContestUser tableContestUser = sqlSession.getMapper(TableContestUser.class);
            TableContestProblem tableContestProblem = sqlSession.getMapper(TableContestProblem.class);
            ViewSubmitRecord viewSubmitRecord = sqlSession.getMapper(ViewSubmitRecord.class);

            ContestBean contestBean = tableContest.getContestByID(19);
            List<ContestUserBean> users = tableContestUser.getContestUserList(19);
            List<ContestProblemBean> problems = tableContestProblem.getContestProblemList(19);
            List<ViewSubmitRecordBean> submits = viewSubmitRecord.getSubmitRecordListOrderedByDate(19,0,1000);


           List<RankBean> rankList = Tools.calculateRank(contestBean, users, problems, submits);
           System.out.println(rankList);

            List<ContestProblemBean> prolemList = Tools.getContestProblemStatistic(submits, problems);
            System.out.println(prolemList);
        } finally {
            sqlSession.close();
        }
    }
}

