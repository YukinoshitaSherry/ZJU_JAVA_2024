package components;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Test;

public class DBUtils {

	//连接资源
	private static Connection conn = null;
	//获取连接的方法
	public static Connection getConn() {
		//处理异常方式3种：try/catch  throws   throw
		try {
			//1.注册JDBC驱动
			//异常：类加载异常 ClassNotFoundException
			Class.forName("com.mysql.jdbc.Driver");
			//2.获取数据库连接
			//url: 需要连接的数据库
			//user: 数据库用户名
			//password:数据库的密码
			//异常 ：SQLException
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/chat_db", 
					"root", "csq1006yzy"); //change your password
			System.out.println(conn);
			conn.createStatement();
		} catch (Exception e) {
			//由于后期异常较多，处理每一个异常比较麻烦
			//所以此处可以选用Excepiton 异常的父类
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}

	//关闭连接
	public static void close(
			Connection conn,
			Statement stat,
			ResultSet rs) {
		//6.关闭资源（先开后关）
		try {
			stat.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Test
	public void test() {
		
		Connection con = getConn();
		System.out.println(con);
	}
}
