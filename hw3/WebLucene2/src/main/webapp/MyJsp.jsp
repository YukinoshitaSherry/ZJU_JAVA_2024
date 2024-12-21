<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>基于Lucene的搜索引擎的研究与实现</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">

  <link rel="stylesheet" href="PublicStyle.css" type="text/css"></link></head>
  
  <body>
  <center>
  <div>
 <input type="button" onClick="javascript:location.href='index.jsp'" class="btnClass_100px_A" value="文件搜索" />
  &nbsp;&nbsp;<input type="button" onClick="javascript:location.href='index2.jsp'"  class="btnClass_100px_A" value="数据搜索" />
  </div>
  </center>
  </body>
</html>
