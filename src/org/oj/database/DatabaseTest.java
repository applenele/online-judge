package org.oj.database;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.oj.controller.beans.RankBean;
import org.oj.model.javaBean.*;
import utils.Consts;
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
            ViewSubmitRecord submitRecord = sqlSession.getMapper(ViewSubmitRecord.class);
                                 System.out.println(submitRecord.getCountOnCondition(19, null, null, null, null));
            List<ViewSubmitRecordBean> recordList = submitRecord.getSubmitRecordList(19, null, null, null, null, null, null);
            //获取分页信息null, null);
            for (ViewSubmitRecordBean p : recordList) {
                System.out.println(p + "\n\n");
            }
        } finally {
            sqlSession.close();
        }
    }
}

