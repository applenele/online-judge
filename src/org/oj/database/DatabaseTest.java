package org.oj.database;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.oj.model.javaBean.JudgeDetailBean;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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
            JudgeDetail judgeDetail = sqlSession.getMapper(JudgeDetail.class);
            List<JudgeDetailBean> judgeDetailList = judgeDetail.getJudegeDetailBySubmitID(1);

            System.out.println(judgeDetailList);
            sqlSession.close();
        } finally {
            sqlSession.close();
        }
    }
}

