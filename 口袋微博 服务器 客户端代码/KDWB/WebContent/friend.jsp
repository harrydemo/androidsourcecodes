<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK" import="wpf.DBUtil,wpf.User,java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<LINK href="global.css" type="text/css" rel="stylesheet"/>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�����б�</title>
</head>
<body class="bodyBack">
<%
	User user = (User)session.getAttribute("user");
	if(user != null){
		ArrayList<User> fList = DBUtil.getFiendList(user.u_no);		//��ú����б�
		if(fList.size() == 0){
%>
		����û����Ӻ��ѣ�
<%
		}
		else{
		for(User u:fList){
%>
		<table border="0" width="100%">
			<tr>
				<td rowspan="2"><img src="head.jsp?hid=<%=u.h_id %>" width="48" height="48" /></td>
				<td>
					<a href="MyServlet?action=toFriendPage&uno=<%=u.u_no %>" target="content"><%=u.u_name %></a>
				</td>
				<td align="right">
					<a href="MyServlet?action=delete&u_no=<%=user.u_no %>&u_noToDelete=<%=u.u_no %>">
					<img src="img/delete.png" width="18" height="18" border="0"/>ɾ��</a>
				</td>
			</tr>
			<tr>
				<td class="ziti2" colspan="2">���飺<%=(u.u_state.length()>5?u.u_state.substring(0,5)+"...":u.u_state) %></td>
			</tr>
		</table>
<%
			}
		}
	}
	else{
%>
		�û�δ��¼������
<%
	}
%>
</body>
</html>