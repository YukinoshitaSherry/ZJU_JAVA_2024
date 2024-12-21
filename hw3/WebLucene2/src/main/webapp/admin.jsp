<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	HttpSession session11 = request.getSession(true);
	session11.removeAttribute("LoginID");
%>
<html>
	<head>
		<base href="<%=basePath%>">

		<title>数据列表</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<link rel="stylesheet" href="css1.css" type="text/css"></link>
		<script type="text/javascript" src="js/jquery.js">
</script>
		<script type="text/javascript">
function fndelete(id) {

	if (confirm("确定删除当前记录吗？")) {
		$.get("servlet/ServletService?Ran=" + Math.random(), {
			Action : "Del",
			ID : id,
			Table : "content"
		}, function(data) {
			if (data == 1) {
				alert("恭喜您,删除成功！");
				location.href = location.href;
			} else {
				alert("对不起,删除失败,请稍后再试！");
			}

		});
	}
}
function search100() {
	if ($("#txtkeyword").val() == "") {
		location.href = "/WebLucene2/servlet/GridServlet?Action=getlist&currentpage=0";
	} else {
         location.href = "/WebLucene2/servlet/GridServlet?Action=getlist&currentpage=0"+ "&msg="
				+ encodeURI($("#txtkeyword").val());	
	}
}
</script>

	</head>
	<body>
		<div class="backbody" style="background: #FFF;">

			<div style=" padding:10px;">

				<div style="padding: 15px 5px 10px 0;">
					<table class="SearchTable" border="0" cellspacing="0"
						cellpadding="0">
						<tr>
							<td>
								标题：
							</td>
							<td>
								<input type="text" class="textbox" id="txtkeyword" />
							</td>
							<td>
								<input type="button" class="btnClass_100px_A" value="搜索"
									onclick="javascript:search100();" />
							</td>
							<td>
								<input type="button" class="btnClass_100px_A" value="添加"
									onclick="javascript:location='<%=request.getContextPath()%>/adminedit.jsp';" />
							</td>
							<td>
								<input type="button" class="btnClass_100px_A" value="返回"
									onclick="javascript:location.href='<%=request.getContextPath()%>/index.jsp';" />
							</td>
						</tr>
					</table>
					<table width="980px;" class="GridTable" border="0" cellspacing="0"
						cellpadding="0" id="GridTableID">

						<tr>
							<th>
								标题
							</th>
							<th >
								简介
							</th>
							<th>
								链接
							</th>
							<th>
								修改
							</th>
							<th class="right">
								删除
							</th>
						</tr>
						<c:forEach var="data" items="${datalist}">
							<tr>	
								<td>
									${data.Title}
								</td>
								<td>
									${data.content}
								</td>
								<td>
									<a href='${data.Link}'>${data.Link}<a>
								</td>
							   <td >
									<input 
										onclick="javascript:location.href='adminedit.jsp?id=${data.id}'" title="修改"
										type="button" value="" class=" btnGrid"
										style="background: url(images/edit.gif)" />
								</td>
								<td class="right">
									<input id='${data.id}'
										onclick='javascript:fndelete(${data.id})' title="删除"
										type="button" value="" class="btnDel  btnGrid"
										style="background: url(images/delete.gif)" />
								</td>
							</tr>
						</c:forEach>
					</table>
					<div style="margin-top: 5px;">
						<a href="servlet/GridServlet?Action=getlist&currentpage=${1}">首页</a>
						<a
							href="servlet/GridServlet?Action=getlist&currentpage=${currentpage-1}">上一页</a>
						<a
							href="servlet/GridServlet?Action=getlist&currentpage=${currentpage+1}">下一页</a>
						<a
							href="servlet/GridServlet?Action=getlist&currentpage=${pagecount}">尾页</a>
						当前页是第${currentpage}/${pagecount}页,共有${total}条记录
					</div>
				</div>
			</div>
		</div>
	</body>
</html>
