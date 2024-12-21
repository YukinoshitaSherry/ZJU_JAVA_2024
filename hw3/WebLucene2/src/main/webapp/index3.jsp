<%@ page contentType="text/html;charset=utf-8" language="java"%>
<html>
	<head>
		<%
			String path = request.getContextPath();
		%>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<title>文件搜索</title>

		<style type="text/css">
.highlight {
	background: yellow;
	color: #CC0033;
}

#div1 {
	width: 900px;
	height: 50px;
	border: 1px solid #ddd;
	size: 12px;
	font-size: 12px;
	color: #2948FF;
	text-align: left;
}

#div2 {
	text-align: left;
	margin-top: 5px;
	width: 900px;
	border: 1px solid #ddd;
	text-align: left;
	font-size: 12px;
}

.folder {
	background: url(folder_yellow_open.png) no-repeat left center;
	padding-left: 45px;
	margin-left: 5px;
}
</style>
		<script type="text/javascript" src="<%=path%>/js/jquery.js">
</script>
		<script type="text/javascript" src="<%=path%>/js/jquery.blockUI.js">
</script>

		<script type="text/javascript">

var firstText = "";
var len = 0;
function searchfile(URL) {
	var firstText = encodeURI(document.getElementById("checkText").value);
	var url = "http://localhost:8080/WebLucene/lucene?checkText=" + firstText
			+ "&t=" + new Date().getTime();
	url += "&URL=" + URL;
	var html = $.ajax( {
		url : url,
		async : false
	}).responseText;
	//alert(html);

	if (html.replace(/(^\s*)|(\s*$)/g, "").length > 0) {
		$("#div2").append(
				"<p class='folder'>路径：C:\\luceneFile\\" + URL + "</p>");
		len += 1;
	}

}
function getFile() {
	$("#div2").html("");
	$.get("servlet/GetFilePath", {
		Action : ""
	}, function(data) {

		if (data.length > 0) {

			var u = data.split("|");

			len = 0;
			//searchfile("01.txt");
			for ( var i = 0; i < u.length; i++) {
				if (u[i].length > 0) {
					searchfile(u[i]);
				}
			}
			$("#div3").html("找到记录数：" + len);
		}
	})
}
</script>
		<link rel="stylesheet" href="PublicStyle.css" type="text/css"></link>
		<link rel="stylesheet" href="css.css" type="text/css"></link>
	</head>

	<body style="padding: 20px;">
		<div class="search_box head_search">
			<div class="search_input left">
				<input type="text" id="checkText" name="checkText" value="2"
					class="input_text ac_input" />
			</div>
			<div class="search_button right">
				<input type="button" onClick="getFile()"
					class="input_button large_size" value="搜索" />

			</div>

			<div class="clearfix">
			</div>
		</div>
		
		<div style="float: left;margin-top: 22px;margin-left: 10px;">
			<input type="button" onClick="javascript:location.href='MyJsp.jsp'"
				class="btnClass_100px_A" value="返回" />
		</div>
		<div class="clearfix">
		</div>
		<div id="div3"
			style="line-height: 40px; margin-left: 50px; height: 40px; color: #F00; font-size: 24px;"></div>
		<div id="div2"
			style="line-height: 50px; margin-left: 50px; color: #060; font-size: 24px;"></div>

	</body>
</html>
