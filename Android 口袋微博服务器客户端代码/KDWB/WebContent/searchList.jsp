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
		<center>请输入搜索关键字</center>
	<%
		}
		else{		//关键字参数不为空
			ArrayList<User> userList = DBUtil.searchFriendByName(keyword);		//执行搜索
			if(userList.size() == 0){				//没有搜索到相关内容
	%>
			<center>对不起，找不到您想搜索的结果！</center>
	<%
			}
			else{		//搜索结果不为空
				for(User u:userList){		//遍历搜索结果
	%>
			<table>
				<tr>
					<td rowspan="3">
						<img alt="<%=u.u_name %>" src="head.jsp?hid=<%=u.h_id %>" width="48" height="48">
					</td>
					<td>
						<%=u.u_name %>
	<%	
					User user = (User)session.getAttribute("user");		//获取session中数据
					String me = user.u_no;
					if(!DBUtil.checkIfMyFriend(me,u.u_no)){				//如果不是我的好友
	%>
						<a href="MyServlet?action=makeFriend&me=<%=me %>&stranger=<%=u.u_no %>&keyword=<%=keyword %>">添加好友</a>
						<img src="img/add.png" width="24" height="24"/>
	<%
					}
					else{
	%>
						[已是我的好友]
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