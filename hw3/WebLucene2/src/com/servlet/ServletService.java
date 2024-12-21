package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import bean.JdbcUtil;

public class ServletService extends HttpServlet {

	public ServletService() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	@Override
	public void destroy() {
		super.destroy();

	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String action = request.getParameter("Action");
		System.out.println(action);
		String write = "";
		// 登录验证
		if (action.equals("login")) {
			String LoginID = request.getParameter("LoginID");
			String PassWord = request.getParameter("PassWord");
			write =new JdbcUtil().executeScalar("select ID from Users where LoginID='"+LoginID+"' and Pwd='"+PassWord+"'");
		}
		if (action.equals("Del")) {
			write = Del(request);
		}
		if (action.equals("getone")) {

			ResultSet rs=new JdbcUtil().queryExectue("select * from content where ID="+request.getParameter("id"));
			List list=convertList(rs);
			write=JSONArray.fromObject(list).toString();
		}
		if (action.equals("edit")) {
			String id=request.getParameter("id");
			String link=request.getParameter("link");
			String title=getChinese(request.getParameter("title"));
			String content=getChinese(request.getParameter("content"));
			if ("0".equals(id)) {
			write="INSERT INTO content([Title],[content],[Link])VALUES('"+title+"','"+content+"','"+link+"')";	
			}else {
				write="update content set title='"+title+"',content='"+content+"',Link='"+link+"' where id="+id;
			}
			new JdbcUtil().updateExecute(write);
			write="1";
		}
		out.println(write);
		out.flush();
		out.close();
	}
	/**
	 * 取得中文
	 * 
	 * @param 原字符
	 * @return
	 */
	private String getChinese(String str) {
		if (str == null) {
			return "";
		}
		try {
			return new String(str.getBytes("ISO8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";

		}
	}
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the POST method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}
	public String Del(HttpServletRequest request) {
		int ID = Integer.valueOf(request.getParameter("ID"));
		String Table = request.getParameter("Table");
		String PK_Name = "id";
		String sql = "sp_pkeys '" + Table + "'";

	
		sql = "delete from " + Table + " where " + PK_Name + "=" + ID;
		new JdbcUtil().updateExecute(sql);
	
		return "1";

	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	/**
	 * ResultSet 转list
	 * @param rs
	 * @return
	 */
	public static List convertList(ResultSet rs) {
		List listOfRows = new ArrayList();
		try {
			ResultSetMetaData md = rs.getMetaData();
			int num = md.getColumnCount();
			while (rs.next()) {
				Map mapOfColValues = new HashMap(num);
				for (int i = 1; i <= num; i++) {
					mapOfColValues.put(md.getColumnName(i), rs.getObject(i));
				}
				listOfRows.add(mapOfColValues);
			
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return listOfRows;
	}
	@Override
	public void init() throws ServletException {
		// Put your code here
	}

}
