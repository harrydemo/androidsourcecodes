<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK" import="wpf.*,java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<LINK href="global.css" type="text/css" rel="stylesheet"/>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>好友主页</title>
</head>
<script type="text/javascript">
	function iFrameAdapter(obj)
	{
		var cwin=obj;
		if(document.getElementById)
		{
			if (cwin && !window.opera)
			{	
				if (cwin.contentDocument && cwin.contentDocument.body.offsetHeight)
					cwin.height = cwin.contentDocument.body.offsetHeight; 
				else if(cwin.Document && cwin.Document.body.scrollHeight)
					cwin.height = cwin.Document.body.scrollHeight;
				
			}
		}
	}
</script>
<body class="bodyBack">
<%
	request.setCharacterEncoding(ConstantUtil.CHAR_ENCODING);
	String uno = (String)request.getParameter("uno");
	if(uno != null){
		User user = DBUtil.getUser(uno);
%>
	<table>
		<tr>
			<td rowspan="2">
				<img alt="<%=user.u_name %>" src="head.jsp?hid=<%=user.h_id%>" width="48" height="48"/>
			</td>
			<td>
				<%=user.u_name %>
			</td>
		</tr>
		<tr>
			<td>心情：<%=user.u_state %></td>
		</tr>
	</table>
	<hr/>
	<table>
		<tr>
			<td><a href="diary.jsp?u_no=<%=user.u_no %>" target="contentChild">查看日记</a></td>
			<td><a href="album.jsp?u_no=<%=user.u_no %>" target="contentChild">查看相册</a></td>
		</tr>
	</table>
	<iframe name="contentChild" width="100%" height="700px" scrolling="auto">
	
	</iframe>
<%
	}
%>
</body>
</html>