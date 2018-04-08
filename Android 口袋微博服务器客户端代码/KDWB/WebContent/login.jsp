<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK" import="java.sql.*,java.util.*,wpf.ConstantUtil.*,wpf.User"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="wpf.ConstantUtil"%>
<html>
<LINK href="global.css" type="text/css" rel="stylesheet"/>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title></title>
</head>
<body onload="window.parent.frames['friend'].location.reload()" class="bodyBack">
<%	
	User user = (User)session.getAttribute("user");
	if(user == null){
%>
		<form action="MyServlet" method="post">
			<table border="0">
				<tr>
					<td colspan="2" class="arinfo3" align="center">登录口袋微博</td>
				</tr>
				<tr>
					<td>用户名：</td>
					<td><input name="u_no" size="15" class="f2_input"/></td>
				</tr>
				<tr>
					<td>密&nbsp;&nbsp;码：</td>
					<td><input type="password" name="u_pwd" size="15" class="f2_input"/></td>
				</tr>
				<tr>
					<td><input class="btn" type="submit" value="登录"/></td> 
					<td><a href="register.jsp" target="content">注册</a></td>
				</tr>
			</table>
			<input type="hidden" name="action" value="login" />
		</form>
<%
		String result = (String)request.getAttribute("loginResult");
		if(result != null){
%>
			<font color="#ff0000" size="2.8"><%=result %></font>
<%
		}
	}
	else{				//显示登录成功信息
		
%>
	
	<form action="/MyServlet">
	<DIV id=nav class="arinfo">
		<table border="0">
			<tr>
				<td rowspan="2"><img src="head.jsp?hid=<%=user.h_id %>" width="48" height="48" /></td>
				<td><b><%= user.u_name %></b></td>
			</tr>
			<tr bgcolor="#99ccff">
				<td align="right" colspan="2">
					<a href="MyServlet?action=modify" target="content"><b>修改资料</b></a>
					<a href="MyServlet?action=logout"><b>注销</b></a>
				</td>
			</tr>
			<tr>
				<td colspan="2"><i><b>心情：</b></i><%=user.u_state %></td>
			</tr>
			<tr>
				<td>
				
				
				</td>
			</tr>
		</table>
		</DIV>
	</form>
	<br/>
	<form action="MyServlet" target="content">
		<input type="text" name="keyword" size="12" class="f2_input"/>
		<input class="btn" type="submit" value="搜索好友" />
		<input type="hidden" name="action" value="searchFriend" />
	</form>
	
	<script type="text/javascript">
		window.parent.frames.item('friend').location.reload();
	</script>

<%
	}
%>

</body>
</html>