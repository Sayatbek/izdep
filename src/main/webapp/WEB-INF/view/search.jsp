<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>${sitename}</title>
<link rel="stylesheet" href="../css/style.css" type="text/css" />

</head>
<body onLoad="document.forms.search_frm.search_field.focus()">

	<div class="top-bar-black">
		<a href="javascript: void(0)" style="color: #fff">${search}</a> <a
			href="imageIndex" style="margin-left: 10px;">${images}</a>
	</div>

	<div class="home">
		<img src="img/logo.png" alt="${sitename}" width="618" height="147"
			border="0">
		<form class="form-wrapper cf" method="post" name="search_frm"
			action="/web/search" id="search_frm"
			onsubmit="if (document.getElementById('search_field').value.length < 1) return false;">
			<input type="text" placeholder="${request} ..." name="search_field"
				id="search_field" required> <input type="hidden" name="page"
				value="1">
			<button type="submit" name="submit" value="Search">${search}</button>
		</form>
	</div>

	<div id="footer">
		<div class="footer">
			<p>Symbat Yergali Zhansulu &copy; ${sitename} 2018</p>
			<p style="float: right; margin-right: 30px;">
				<a style="color: black;" href="mailto:yergali.zhakhan@gmail.com?Subject=Izdep%20Support" target="_top">Technical support</a>
			</p>
		</div>
	</div>

</body>

</html>