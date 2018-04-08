<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK" import="java.util.*,wpf.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<LINK href="global.css" type="text/css" rel="stylesheet"/>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>Insert title here</title>
</head>
<script type="text/javascript">
	function checkToSubmit(u_no,pageNo,span){
		var xid = document.form1.album.value;
		form1.action="album.jsp?u_no="+u_no+"&pageNo="+pageNo+"&span="+span+"&xid="+xid;
		form1.submit();
	}
</script>
<body class="bodyBack">
<%
	String [] access={"公开","好友可见","仅主人可见"};
	request.setCharacterEncoding(wpf.ConstantUtil.CHAR_ENCODING);	//设置编码格式
	String u_no = (String)request.getAttribute("u_no");
	String p_id = "";
	if(u_no == null){		//如果为空
		u_no = request.getParameter("u_no");
	}
	User user = (User)session.getAttribute("user");
	if(u_no !=null){
		ArrayList<String[]> albumList = new ArrayList<String[]>();
		if(u_no.equals(user.u_no)){
			albumList = DBUtil.getAlbumList(u_no);
		}
		else{
			albumList = DBUtil.getAlbumListByAccess(u_no,user.u_no);
		}
		int pageNo = 1;
		int span = 1;		//每页显示一张
		String pages = request.getParameter("pageNo");
		if(pages != null && pages.length()>0){		//如果参数有效
			pageNo = Integer.valueOf(pages);		//读取页数
		}
		String spans = request.getParameter("span");
		if(spans != null && spans.length()>0){		//如果参数有效
			span = Integer.valueOf(spans);
		}
		String xid = request.getParameter("xid");
%>
	<table>
		<tr>
			<td>
				<form action="album.jsp" method="post" name="form1">
				选择相册：<select name="album" size="1" >
				<%
					if(albumList.size() == 0){
				%>
						<option value="-1">没有相册</option>
				<%
					}
					else{
						for(String[] sa:albumList){
					%>
						<option value="<%=sa[0] %>" <%=(sa[0].equals(xid)?"selected=\"selected\"":"") %> ><%=sa[1]%></option>
					<%						
						}
					}
				%>
				</select>
				<input class="btn" type="submit" value="确定" onclick="checkToSubmit(<%=u_no %>,<%=pageNo %>,<%=span %>)" /></form>
			</td>
	<%
			if(user.u_no.equals(u_no)){			//是用户自己的相册，可以显示上传图片功能
	%>
			<td align="right"><a href="uploadImage.jsp">上传图片</a></td>
	<%			
			}
	%>
		</tr>
	</table>
	<hr/>

	<%
		
		if(xid!=null){			//如果选择了相册
			ArrayList<PhotoInfo> photoList = DBUtil.getPhotoInfoByAlbum(xid,pageNo,span);//	获取图片列表
			int albumSize = DBUtil.getAlbumSize(xid);
			if(photoList.size() == 0){		//相册为空
	%>
			<center>该相册还未上传照片</center>
	<%
			}
			else{
	%>
	<%
	if(u_no.equals(user.u_no)){
		int accessLevel=DBUtil.getAlbumAccess(xid);
	%>
	<form action="MyServlet" method="post" >
		<table width="100%">
			<tr>
				<td align="left">相册权限：[<%=access[accessLevel] %>]</td>
				<td align="right">
		修改相册权限：<select name="album_access">
			<option value="0" <%=(accessLevel==0?"selected='selected'":"") %> ><%=access[0] %></option>
			<option value="1" <%=(accessLevel==1?"selected='selected'":"") %> ><%=access[1] %></option>
			<option value="2" <%=(accessLevel==2?"selected='selected'":"") %> ><%=access[2] %></option>
		</select>
		<input type="submit" class="btn" value="更改权限" />
		<input type="hidden" name="action" value="change_album_access" />
		<input type="hidden" name="xid" value="<%=xid %>" />
		<input type="hidden" name="u_no" value="<%=u_no %>" /></td>
			</tr>
		</table>
	</form>	
	<%
	}

				for(PhotoInfo pi:photoList){
					p_id = pi.p_id;
	%>
				<table width="100%" border="0">
					<tr>
						<td align="center" colspan="2">照片名称：<%=pi.p_name %>
		<%
						if(user.u_no.equals(u_no)){
		%>
					<a href="MyServlet?action=deletePhoto&p_id=<%=pi.p_id %>&xid=<%=xid %>&pageNo=<%=pageNo %>&u_no=<%=u_no %>">[删除]</a>
		<%
						}
		%>
						</td>
					</tr>
					<tr>
						<td align="center" colspan="2"><img src="photo.jsp?pid=<%=pi.p_id %>" width="400" height="400"></img></td>
					</tr>
					<tr>
						<td align="center" colspan="2">照片描述：<%=pi.p_des %></td>
					</tr>
					<tr>
					<%
						if(pageNo>1){		//可以上一张
					%>
					<td align="right" valign="middle">
						<a href="album.jsp?u_no=<%=u_no %>&pageNo=<%=(pageNo-1) %>&span=<%=span %>&xid=<%=xid %>">
							<img src="img/go_back.png" width="20" height="20" border="0"/>上一张
						</a>
					</td>
					<%
						}
						else{
					%>
					
					<td align="right" valign="middle">
						<img src="img/go_back_disable.png" width="20" height="20" /><u>上一张</u>
					</td>
					<%
						}
						if(pageNo<albumSize){	//可以下一张
					%>
						&nbsp;&nbsp;<td align="left" valign="middle">
							<a href="album.jsp?u_no=<%=u_no %>&pageNo=<%=(pageNo+1) %>&span=<%=span %>&xid=<%=xid %>">
								下一张<img src="img/go_forward.png" width="20" height="20" border="0"/>
							</a>
						</td>
					<%
						}
						else{
					%>
						
						<td align="left" valign="middle">
							<u>下一张</u><img src="img/go_forward_disable.png" width="20" height="20" />
						</td>
					<%
						}
					%>
					</tr>
				</table>
				<table width="74%" align="center">
					<tr>
						<td align="left" colspan="2" bgcolor="#5a76b1" class="ziti">评论列表</td>
					</tr>
	<%
			ArrayList<P_Comments> cmtList = DBUtil.getPhotoComment(p_id);
			if(cmtList.size() == 0){
				
	%>
			<tr><td align="center">暂无评论</td></tr>
	<%
			}
			else{
				for(P_Comments pc:cmtList){
	%>
				<tr>
					<td>
						<font size="2.5"><a href="MyServlet?action=toFriendPage&uno=<%=pc.u_no %>"><%=pc.u_name %></a>说：
						<%=pc.content %></font>
					</td>
					<td align="right"><font size="2.5" >[<%=pc.date %>]</font></td>
				</tr>
	<%
				}
			}
	%>
		</table>
			<form action="MyServlet" method="post">
				<table width="74%" align="center">
					<tr>
						<td align="left" colspan="2" bgcolor="#5a76b1" class="ziti">评论：</td>
					</tr>
					<tr>
						<td align="center" colspan="2"><textarea name="content" cols="60" rows="3" >我来点评一下</textarea></td>
					</tr>
					<tr>
						<td align="right" colspan="2"><input class="btn" type="submit" value="发表评论"></input></td>
					</tr>
				</table>
				<input type="hidden" name="action" value="addPhotoComment" />
				<input type="hidden" name="u_no" value="<%=user.u_no %>" />
				<input type="hidden" name="p_id" value="<%=pi.p_id %>" />
				<input type="hidden" name="xid" value="<%=xid %>" />
				<input type="hidden" name="pageNo" value="<%=pageNo %>" />
			</form>
	<%
				}
			}
		}
	}
%>
</body>
</html>