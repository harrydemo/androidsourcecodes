<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK" import="wpf.ConstantUtil,wpf.User"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<LINK href="global.css" type="text/css" rel="stylesheet"/>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>д��־</title>
</head>
	<script type="text/javascript">
		function check(action){
			if(document.form1.content.value == "")
			{
				alert("�����Ϣ��д����!");
				return;
			}
			if(action == "new_diary")
			{
				if(document.form1.title.value =="")
				{
					alert("�����Ϣ��д����!");
					return;
				}	
			}
				document.form1.action.value = action;
				document.form1.submit();
		}
	</script>
<body  class="bodyBack">
<%
	request.setCharacterEncoding(ConstantUtil.CHAR_ENCODING);
	String result = (String)request.getAttribute("result");
	User user = (User)session.getAttribute("user");
	if(result == null){ 
%>

	<form action="MyServlet" method="post" name="form1"> 
		<table align="center" border="0" width="590px" height="155px" style="padding: 0em; margin: 0em;"  cellspacing="0" cellpadding="0" background="img/aaaaa.jpg">  
			<tr>
				<td align="left" valign="bottom" colspan="3">
					<br/>
					&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
					<input name="title" class="f2_input"/><br/>    
					&nbsp&nbsp<TEXTAREA class="status-update-textarea" id="sendinfo" name="content" rows="3" cols="60"></TEXTAREA> 
				</td>
			</tr>
			<tr height="18px"></tr>
			<tr valign="baseline">
				<td width="19px"></td>
				<td align="left">
					<a name="seeDiary" href="MyServlet?action=seeDiary" target="content">�鿴�ռ�</a>
					<a name="seeAlbum" href="MyServlet?action=seeAlbum" target="content">�鿴���</a>
				<td align="right">
					<input class="btn" type="button" value="��������" onclick="check('new_state')"/>
					<input class="btn" type="button" value="�����ռ�" onclick="check('new_diary')"/>
				</td>
				<td width="50px"></td>
			</tr>	
		</table>
		<input type="hidden" name="action" value="new_diary" />
	</form>
<%
	}
	else {
		
%>
	<%= result%>
	<a href="write.jsp">����</a>
	<script type="text/javascript">
		window.parent.frames.item('login').location.reload();
	</script>
<%
	}
%>
</body>
</html>