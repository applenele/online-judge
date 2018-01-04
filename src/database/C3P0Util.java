package database;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import utils.ConstStrings;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by xanarry on 17-3-25.
 */
public class C3P0Util {
    static ComboPooledDataSource cpds = null;

    static {
        //这里有个优点，写好配置文件，想换数据库，简单
        cpds = new ComboPooledDataSource("mysql");//使用配置文件中的mysql配置

        /*
        cpds = new ComboPooledDataSource();//这是mysql数据库
        try {
            cpds.setDriverClass("com.mysql.jdbc.Driver");
            cpds.setUser("root");
            cpds.setPassword("admin");
            cpds.setJdbcUrl("jdbc:mysql://localhost:3306/qqhr_etc?characterEncoding=utf8");
            cpds.setInitialPoolSize(10);
            cpds.setMaxIdleTime(60);
            cpds.setMinPoolSize(10);
            cpds.setMaxPoolSize(100);
            cpds.setMaxStatements(200);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }

        */
    }


    /**
     * 获得数据库连接
     *
     * @return Connection
     */
    public static Connection getConnection() {
        try {
            return cpds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 数据库关闭操作
     *
     * @param conn
     * @param pst
     * @param rs
     */
    public static void close(Connection conn, PreparedStatement pst, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (pst != null) {
            try {
                pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 测试DBUtil类
     *
     * @param args
     */
    public static void main(String[] args) {
        Connection conn = getConnection();
        try {
            ResultSet resultSet = conn.createStatement().executeQuery("SELECT * FROM " + ConstStrings.TABLE_LANGUAGE);
            while (resultSet.next()) {
                System.out.println(resultSet.getString("language_id") + " " + resultSet.getString("language"));
            }
        } catch (Exception e) {
            e.getStackTrace();
            System.out.println(e.getMessage());
        }
        close(conn, null, null);
    }
}
