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
            TableSystemError tableSystemError = sqlSession.getMapper(TableSystemError.class);
            tableSystemError.addErrorMessage(new SystemErrorBean(19, "error"));
            SystemErrorBean s = tableSystemError.getSystemErrorMessage(19);
            System.out.println(s);
        } finally {
            sqlSession.close();
        }
    }
}

