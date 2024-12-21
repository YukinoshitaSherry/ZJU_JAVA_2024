package components;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtils {

    // 连接资源
    private static Connection conn = null;
    // 获取连接的方法
    public static Connection getConn() {
        // 处理异常方式3种：try/catch  throws   throw
        try {
            // 1.注册JDBC驱动
            // 异常：类加载异常 ClassNotFoundException
            Class.forName("com.mysql.cj.jdbc.Driver");
            // 2.获取数据库连接
            // url: 需要连接的数据库
            // user: 数据库用户名
            // password:数据库的密码
            // 异常 ：SQLException
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/chat_db?"
                                                   + "useUnicode=true&"
                                                   + "characterEncoding=UTF-8&"
                                                   + "serverTimezone=Asia/Shanghai&"
                                                   + "useSSL=false&"
                                                   + "allowPublicKeyRetrieval=true",
                                               "root", "csq1006yzy"); // change your password
            if (conn != null) {
                System.out.println("数据库连接成功: " + conn);
            }
        } catch (Exception e) {
            System.out.println("数据库连接异常: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return conn;
    }

    // 关闭连接
    public static void close(Connection conn, Statement stat, ResultSet rs) {
        // 6.关闭资源（先开后关）
        try {
            if (rs != null) {
                rs.close();
            }
            if (stat != null) {
                stat.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
