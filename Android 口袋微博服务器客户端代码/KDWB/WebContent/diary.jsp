<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK" import="wpf.DBUtil,wpf.ConstantUtil,wpf.Diary,wpf.User,java.util.*,wpf.Comments"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<LINK href="global.css" type="text/css" rel="stylesheet"/>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>DIARY</title>
</head>
<body class="bodyBack">
<%
	request.setCharacterEncoding(ConstantUtil.CHAR_ENCODING);
	String pages = (String)request.getParameter("pageNo");
	int pageNo = 1;
	int span = 1;
	if(pages != null && pages.length()!=0){
		pageNo = Integer.parseInt(pages);
	}
	User user = (User)session.getAttribute("user");
	String uno = (String)request.getAttribute("u_no");
	if(uno == null){
		uno = request.getParameter("u_no");
	}
	if(uno == null && user==null){		//用户还未登录
%>
	您需要先登录!
<%
	}
	else {
		ArrayList<Diary> result = DBUtil.getUserDiary(uno,pageNo,span);
		if(result.size() == 0){		//用户还没写微博日志
%>
		<center>您还没写过任何日志呢</center>
<%	
		}
		else{
		int diarySize = DBUtil.getDiarySize(uno);
			for(Diary d:result){
%>	
		<table width="100%" height="100%" background="img/diary_back.jpg">
			<tr>
				<td colspan="2" align="left" bgcolor="#37579e" class="ziti"><b>日记</b></td>
			</tr>
			<tr>
				<td align="center"><%=d.title %></td>
				<font size="2.5"><td align="right">发表于<%=d.time %>
	<%
				if(user.u_no.equals(uno)){
	%>
					<a href="MyServlet?action=toModifyDiary&r_id=<%=d.rid %>&r_title=<%=d.title %>&r_content=<%=d.content %>&u_no=<%=uno %>" >[编辑]</a>
					<a href="MyServlet?action=deleteDiary&r_id=<%=d.rid %>&u_no=<%=uno %>&pageNo=<%=pageNo %>" >[删除]</a>
	<%
				}
	%>
					
				</td></font>
			</tr>
			<tr>
				<td align="left" colspan="2"><%=d.content %></td>
			</tr>
		</table>
		<hr color="#99ccff" size="2"/>
		<table bgcolor="#eceff5" width="100%" border="0">
			<tr bgcolor="#5a76b1">
				<td colspan="2">
					<b>评论列表</b>
				</td>
			</tr>
<%
				if(d.commentList.size() == 0){		//暂无评论
%>
			<tr><td align="center"/>暂无评论</td></tr>
<%
				}
				else{
				for(Comments c:d.commentList){
%>
			<tr>
				<td><font size="2.5">
					<a href="MyServlet?action=toFriendPage&uno=<%=c.cmtNo %>"><%=c.cmtName %></a>
						说：<%=c.content %></font></td>
				<td align="right" width="135px"><font size="2.5" >[<%=c.date %>]</font></td>
			</tr>
		
<%
				}
			}
%>
		</table>
		<!-- 发表评论模块 -->
		<form name="<%=uno %>" action="MyServlet" method="post">
		<table bgcolor="#fff9e2" border="0" width="100%">
			<tr>
				<td align="left"  bgcolor="#5a76b1" width="80"><b>发表评论</b></td>
			</tr>
			<tr>
				<td><textarea name="comment" cols="73" rows="3">我来说几句</textarea></td>
			</tr>
			<tr>
				<td align="right">
					<input class="btn" type="submit" value="发表评论"/>
					<input type="hidden" name="action" value="makeComment"/>
		<%
			//获得访问者的id
			String visitor = user.u_no;
		%>
					<input type="hidden" name="u_no" value="<%=uno %>" />
					<input type="hidden" name="visitor" value="<%=visitor %>" />
					<input type="hidden" name="r_id" value="<%=d.rid %>" />
				</td>
			</tr>
			</table>
		</form>
		<hr/>
<%
			}
%>
		<table align="center">
			<tr>
<%
				if(pageNo>1){			//可以上一页
%>
				<td align="right">
					<a href="diary.jsp?u_no=<%=uno %>&pageNo=<%=(pageNo-1) %>&span=<%=span %>" >
						<img src="img/go_back2.png" width="20" height="16" border="0"/>
						上一篇
					</a>
				</td>
<%
				}
				else{
%>	
				<td align="right">
					<img src="img/go_back2_disable.png" width="20" height="16" />
					<u>上一篇</u>
				</td>
<%
				}
				int maxPage = diarySize/span+(diarySize%span==0?0:1);
				if(pageNo < maxPage){
%>
				<td align="left">
					<a href="diary.jsp?u_no=<%=uno %>&pageNo=<%=(pageNo+1) %>&span=<%=span %>">
						下一篇
						<img src="img/go_forward2.png" width="20" height="16" border="0" />
					</a>
				</td>
<%
				}
				else{
%>
				<td align="left">
					<u>下一篇</u>
					<img src="img/go_forward2_disable.png" width="20" height="16" />
				</td>
<%
				}
%>
			</tr>
		</table>
<%
		}
	}
%>
</body>
</html>