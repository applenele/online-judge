package org.oj.data;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.oj.model.javaBean.LanguageBean;
import org.oj.model.javaBean.UserBean;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by xanarry on 17-4-22.
 */
public class Test {
    public static void main(String[] argv) throws IOException {
        String resource = "org/oj/data/mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);

        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {

            User user = sqlSession.getMapper(User.class);
            boolean v = user.checkEmailExist("b@gmail.com");
            System.out.println(v);


            UserBean userBean = user.getUserByEmail("b@gmail.com");
            System.out.println(userBean);

        } finally {
            sqlSession.close();
        }
    }
}

