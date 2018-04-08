<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK" import="java.util.*,wpf.DBUtil,wpf.User,wpf.ConstantUtil"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<LINK href="global.css" type="text/css" rel="stylesheet"/>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>Insert title here</title>
</head>
<script type="text/javascript">
	String.prototype.trim = function(){
	   return this.replace(/(^\s*)|(\s*$)/g, "");
	};
	function showCreate()
	{//显示创建新相册的界面
		document.getElementById("createAlbum").innerHTML="<input name='albumName'>"+
				"<input type='button' class='btn' value='创建' onclick='createAlbum()'/>";
	}
	function createAlbum()
	{//点下创建新相册时进行的检查工作
		if(document.form1.albumName.value.trim() == "")
		{
			alert("请输入要创建的相册名称!");
			return;
		}
		document.form1.action.value = "createAlbum";
		document.form1.submit();
	}
	function check(){
		if(document.form1.photoName.value.trim()== ""){//名称为空
		 	alert("请填写照片名称！");
		 	document.form1.photoName.focus();
		 	return false;
		}
		if(document.form1.photoDes.value.trim()== ""){//描述为空
		 	alert("请填写照片描述！");
		 	document.form1.photoDes.focus();
		 	return false;
		}		
		if(document.form1.album.value == -1){	//没有选择相册
			alert("请选择上传的相册");
			return false;
		}
		var name = document.form1.photoName.value.trim();
		var des = document.form1.photoDes.value.trim();
		var xid = document.form1.album.value.trim();
		document.form2.action="FileUploadServlet?action=upload&photoName="+name+"&photoDes="+des+"&album="+xid;	
		return true;		
	}
</script>
<body class="bodyBack">

<%
	request.setCharacterEncoding(ConstantUtil.CHAR_ENCODING);
	String result = (String)request.getAttribute("result");
	User user = (User)session.getAttribute("user");
	if(result == null){			//如果不是上传结果页面
%>
<table>
	<form name="form1" action="MyServlet" method="post">
		
			<tr>
				<td>选择上传的相册：</td>
<%
					
					ArrayList<String []> albumList = null;
					if(user == null){
%>
						请先登录!
<%
					}
					else{
						albumList = DBUtil.getAlbumList(user.u_no);
					
%>
				<td><select name="album" size="1">
	<%
					if(albumList.size() == 0){
	%>
						<option value="-1">您还没有创建任何相册</option>
	<%
					}
					else{
						for(String sa[]:albumList){
	%>
							<option value="<%=sa[0] %>"><%=sa[1] %></option>
	<%
						}
					}
				}
	%>
				</select></td>
				<td><input class="btn" type="button" value="创建相册" onclick="showCreate()"/></td>
				<td><div id="createAlbum"></div></td>
			</tr>
			<tr>
				<td>图片名称：</td>
				<td><input name="photoName" width="180" /></td>
			</tr>
			<tr>
				<td>图片描述：</td>
				<td colspan="4"><textarea rows="3" name="photoDes" cols="40"></textarea></td>
			</tr>
		
		<input type="hidden" name="action" value="createAlbum" />
		<input type="hidden" name="u_no" value="<%=user.u_no %>" />
	</form>
	<form action="FileUploadServlet" name="form2" method="post"  enctype="multipart/form-data" onsubmit="return check();">
			<tr>
				<td>图片路径：</td>
				<td><input type="file" name="photoPath" /></td>
				<td><input class="btn" type="submit" value="上传"/></td>
			</tr>
	</form>
	</table>
<%
	}
	else{				//显示上传结果页面
%>
	<p>	<%=result %>
		<a href="uploadImage.jsp">返回继续</a>
		<a name="seeAlbum" href="MyServlet?action=seeAlbum" target="content">查看相册</a></p>
<%
		
	}
%>
</body>
</html>