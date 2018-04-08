<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<link href="global.css" type="text/css" rel="stylesheet"/>
<script type="text/javascript">
function iFrameAdapter(obj,table)
{	//alert(obj.name);
	var cwin=obj;
	if(document.getElementById)
	{
		if (cwin && !window.opera)
		{	
			if (cwin.contentDocument && cwin.contentDocument.body.offsetHeight)
				cwin.height = cwin.contentDocument.body.offsetHeight; 
			else if(cwin.Document && cwin.Document.body.scrollHeight)
				cwin.height = cwin.Document.body.scrollHeight;
			mainTableheight=obj.document.body.scrollHeight+10;
		}
	}
}
function changeBack(type){
	if(type == 1){
		document.getElementById('tab').className="tab1";
	}
	else if(type == 2){
		document.getElementById('tab').className="tab2";
	}
}
</script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>联系人</title>
</head>
<body class="bodyBack">
	<table id="tab" name="mainTable" border="0" cellpadding="7" cellspacing="3" height="100%" class="tab1">
		<tr>
			<td>
				<a href="friend.jsp" target="contactsChild" onclick="changeBack(1);">我的好友</a>
			</td>
			<td>
				<a href="visitor.jsp" target="contactsChild" onclick="changeBack(2);">最近访客</a>
			</td>
		</tr>
	</table>
	<table>
		<tr>
			<td colspan="2" height="100%">
				<iframe id="contactsChild" name="contactsChild" width="100%" src="friend.jsp" height="600px"  scrolling="auto" frameborder="0"
					>
				</iframe>
			</td>
		</tr>
	</table>
</body>
</html>