<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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

<title>添加或修改数据</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">


<link rel="stylesheet" href="PublicStyle.css" type="text/css"></link>
<script type="text/javascript" src="js/jquery.js">
	
</script>
<Script type="text/javascript">
	$(function() {
		$("#button1")
				.click(
						function() {
							if ($("#txttitle").val() == "") {
								alert("请输入标题");
								return;
							}
							if ($("#txtcontent").val() == "") {
								alert("请输入简介");
								return;
							}
							if ($("#txtlink").val() == "") {
								alert("请输入链接");
								return;
							}

							var param = {
								Action : "edit",
								title : $("#txttitle").val(),
								content : $("#txtcontent").val(),
								link : $("#txtlink").val(),
								id : getUrlParamValue("id")
							};

							$.get(
											"servlet/ServletService?ran="
													+ Math.random(),
											param,
											function(data) {
												if (data != "") {
													location.href = "/WebLucene2/servlet/GridServlet?Action=getlist&currentpage=0";
												} else {
													alert("保存失败");
												}
											});
						});
	})

	function getUrlParamValue(name) {
		var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
		var r = window.location.search.substr(1).match(reg);
		if (r != null) {
			return unescape(r[2]);
		} else {
			return 0;
		}
	}
	$(function(){
	if(getUrlParamValue("id")!=0){
	$.get("servlet/ServletService?Ran=" + Math.random(), {
			Action : "getone",
			id : getUrlParamValue("id")
			
		}, function(data) {
			data=eval("("+data+")");
               $("#txttitle").val(data[0].Title);
               $("#txtcontent").val(data[0].content);
               $("#txtlink").val(data[0].Link);
		});
	}
	});
</Script>

</head>


<body>
	<div class="body">


		<div class="box">
			<table width="600px" style="margin: 0px auto; margin-left: 300px;"
				cellspacing="0px" id="mytable" class="FormTable">
				<tr>
					<td></td>
					<td class="td_right">搜索数据</td>
				</tr>
				<tr>
					<td>标题：</td>
					<td class="td_right"><input style='width:300px;' id="txttitle"
						type="text" class="txt" value=""></td>
				</tr>
				<tr>
					<td>简介：</td>
					<td class="td_right"><textarea rows="5" cols="60"
							id="txtcontent"></textarea></td>
				<tr>

					<td>链接</td>
					<td class="td_right"><input style='width:300px;' id="txtlink"
						type="text" class="txt" value=""></td>
				</tr>
				<tr>
					<td></td>
					<td class="td_right" valign="top"><input id="button1"
						type="button" class="btnClass_100px_A" value="保存"></td>
				</tr>
			</table>
		</div>
	</div>

	</div>
</body>
</html>
