<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK" import="wpf.*,java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title></title>
</head>
<body>
	<%
		request.setCharacterEncoding(ConstantUtil.CHAR_ENCODING);
		String keyword = (String)request.getParameter("keyword");
		if(keyword == null){
	%>
		<center>�����������ؼ���</center>
	<%
		}
		else{		//�ؼ��ֲ�����Ϊ��
			ArrayList<User> userList = DBUtil.searchFriendByName(keyword);		//ִ������
			if(userList.size() == 0){				//û���������������
	%>
			<center>�Բ����Ҳ������������Ľ����</center>
	<%
			}
			else{		//���������Ϊ��
				for(User u:userList){		//�����������
	%>
			<table>
				<tr>
					<td rowspan="3">
						<img alt="<%=u.u_name %>" src="head.jsp?hid=<%=u.h_id %>" width="48" height="48">
					</td>
					<td>
						<%=u.u_name %>
	<%	
					User user = (User)session.getAttribute("user");		//��ȡsession������
					String me = user.u_no;
					if(!DBUtil.checkIfMyFriend(me,u.u_no)){				//��������ҵĺ���
	%>
						<a href="MyServlet?action=makeFriend&me=<%=me %>&stranger=<%=u.u_no %>&keyword=<%=keyword %>">��Ӻ���</a>
						<img src="img/add.png" width="24" height="24"/>
	<%
					}
					else{
	%>
						[�����ҵĺ���]
	<%
					}
	%>
					</td>
				</tr>
				<tr>
					<td><%=u.u_email %></td>
				</tr>
				<tr>
					<td><%=u.u_state %></td>
				</tr>
				</table><hr/>
				
	<%
				}
			}
		}
	%>
</body>
</html>