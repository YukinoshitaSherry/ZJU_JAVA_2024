<%@ page contentType="text/html;charset=utf-8" language="java"%>
<html>
	<head>
		<%
			String path = request.getContextPath();
		%>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<title>数据搜索</title>

		<style type="text/css">
.highlight {
	background: yellow;
	color: #CC0033;
}

#div2 {
	text-align: left;
	margin-top: 5px;
	width: 900px;
	border: 1px solid #ddd;
	text-align: left;
	font-size: 12px;
}

.history {
	text-align: left;
}

.history span {
	color: #363;
	font-size: 12px;
	margin-left: 10px;
	float: left;
}

.a_link {
	width: 30px;
	line-height: 30px;
	height: 30px;
	border: 1px solid #CCC;
	text-align: center;
	display: block;
	float: left;
	cursor: pointer;
	color: #333;
	margin-left: 10px;
}

.a_link:hover {
	background: #F2F2F2;
}

.a_cur {
	background: #ffa405;
	width: 30px;
	line-height: 30px;
	height: 30px;
	border: 1px solid #fe8101;
	text-align: center;
	display: block;
	float: left;
	cursor: pointer;
	color: #999;
	margin-left: 10px;
	text-decoration: none;
}
</style>
		<script type="text/javascript" src="<%=path%>/js/jquery.js">
</script>


		<script type="text/javascript">
$(function() {
	getSearchHis();
})
var firstText = "";
var len = 0;
var total = 0;
var curindex = 0;
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
		$("#div2").append("路径：C:\\luceneFile\\" + URL);
		$("#div2").append('<br />');
		len += 1;
	}

}
//获取开始时间
function showTime() {
	d = new Date();
	var s = "";
	s += d.getYear() + "-";
	s += (d.getMonth() + 1) + "-";
	s += d.getDate() + " ";
	s += d.getHours() + ":";
	s += d.getMinutes() + ":";
	s += d.getSeconds() + ":";
	s += d.getMilliseconds();
	return s;
}

function search100(index) {
	if (document.getElementById("checkText").value == "") {
		alert("请输入关键字！");
		return;
	}
	var d1 = showTime();
	$("#div3").html("");
	var ln = "";
	$
			.getJSON(
					"servlet/SearchKey?action=SearchKey&t="
							+ new Date().getTime(),
					{
						keyword : encodeURI(document
								.getElementById("checkText").value)
					},
					function(data) {
						ln = data.length;
						if (data.length > 0) {
							var start = index * 3;
							var end = Math.min(data.length, start + 3);
							for ( var i = start; i < end; i++) {
								var div = '<div class="b_context_box">';
								div += ' <div class="div_title"><a target="_blank" style="color:#3366FF;text-decoration:underline;" href="'
										+ data[i].link
										+ '">'
										+ data[i].title
										+ '</a></div>'
								div += ' <div class="div_content">' + data[i].content + '</div>'
								div += '</div>';
								$("#div3").append(div);
								var ar = document.getElementById("checkText").value
										.replace(/\s+/g, ' ').split(" ");
								for ( var jj = 0; jj < ar.length; jj++) {
									$("#div3").highlight(ar[jj]);
								}

							}
							if (data.length > 3) {
								var link = '<div style="padding:10px 0 0 10px;">';
								var m = Math.ceil(data.length / 3);
								for ( var i = 0; i < m; i++) {
									if (index == i) {
										link += '<a href="javascript:void(0);" onclick="javascript:void(0);" class="a_cur">' + (i + 1) + '</a>';
									} else {
										link += '<a href="javascript:void(0);" onclick="javascript:search100('
												+ i
												+ ');" class="a_link">'
												+ (i + 1) + '</a>';
									}
								}
								link += '<div>';
								$("#div3").append(link);
							}
						}
						var sjc = parseInt(d2
								.substring(d2.lastIndexOf(':') + 1))
								- parseInt(d1
										.substring(d1.lastIndexOf(':') + 1));
						$("#div2").html("搜索到:" + ln + " 条记录,用时:" + sjc + " 毫秒");

						getSearchHis();

					});
	var d2 = showTime();

}
function getSearchHis() {
	$.getJSON("servlet/SearchKey?action=Searchword&t=" + new Date().getTime(),
			{}, function(json) {
				var s = "";
				for ( var i = 0; i < json.length; i++) {
					s += "<span>" + json[i].KeyWord + "</span>";
				}
				$("#div_his").html(s);
				//处理搜索历史关键字

		});
}
</script>
		<link rel="stylesheet" href="PublicStyle.css" type="text/css"></link>
		<script type="text/javascript" src="js/jquery.js">
</script>
		<script type="text/javascript" src="js/jquery.highlight.js">
</script>
		<link rel="stylesheet" href="css.css" type="text/css"></link>
	</head>

	<body style="padding: 20px;">
<a href='login.jsp' style='margin:20px'>登录</a>
<style>
  .ss{
    font-weight: bold;
    color: #def;
    text-shadow: 0 0 1px currentColor,-1px -1px 1px #000,0 -1px 1px #000,1px -1px 1px #000,1px 0 1px #000,1px 1px 1px #000,0 1px 1px #000,-1px 1px 1px #000,-1px 0 1px #000;
  }
</style>
<h1 class="ss" style="width:600px;text-align: center;font-size:20px;">欢迎您的搜索</h1>

		<div class="search_box head_search">
		    
			<div class="search_input left">
				<input type="text" id="checkText" name="checkText" value=""
					class="input_text ac_input" />
			</div>
			<div class="search_button right">
				<input type="button" onClick="search100(0)"
					class="input_button large_size" value="搜索" />
			</div>
            
			<div class="clearfix">
			</div>
		</div>

		<div class="clearfix">
		</div>
		 
		<fieldset
			style="margin-left: 50px;margin-top:20px; border: 1px solid #CCC; width: 550px; padding: 4px;">
			<legend>
				搜索历史
			</legend>
			<div id="div_his" class="history"></div>
		</fieldset>
		<div id="div2"
			style="line-height: 40px; margin-left: 50px; width: 550px; height: 40px; color: #F00; font-size: 24px;"></div>
		<div id="div3"
			style="margin-left: 50px; color: #666; font-size: 14px;"></div>


	</body>
</html>
