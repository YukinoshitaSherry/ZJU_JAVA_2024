package bean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcUtil {
	static Connection con;

	public static Connection getConnection() {

		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String url = "jdbc:sqlserver://localhost:1433; DatabaseName=LuceneDB2";
			//String username = "sa";
			//String password = "1";
			String username = "sa";
			String password = "123456";
			con = DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}

	// 执行sql语句 增删改
	public int updateExecute(String sql) {
		int result = 0;
		try {
			Connection con = getConnection();
			Statement sta = con.createStatement();
			result = sta.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 执行sql查询语句 返回一个ResultSet
	public  ResultSet queryExectue(String sql) {
		ResultSet rs = null;
		try {
			Connection con = getConnection();
			Statement sta = con.createStatement();
			rs = sta.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	public  String executeScalar(String sql) {
		ResultSet rs = queryExectue(sql);
		String s = "";
		try {
			while (rs.next()) {
				s = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return s;
	}
}
