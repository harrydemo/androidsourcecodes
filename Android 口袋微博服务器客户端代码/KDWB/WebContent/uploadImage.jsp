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
	{//��ʾ���������Ľ���
		document.getElementById("createAlbum").innerHTML="<input name='albumName'>"+
				"<input type='button' class='btn' value='����' onclick='createAlbum()'/>";
	}
	function createAlbum()
	{//���´��������ʱ���еļ�鹤��
		if(document.form1.albumName.value.trim() == "")
		{
			alert("������Ҫ�������������!");
			return;
		}
		document.form1.action.value = "createAlbum";
		document.form1.submit();
	}
	function check(){
		if(document.form1.photoName.value.trim()== ""){//����Ϊ��
		 	alert("����д��Ƭ���ƣ�");
		 	document.form1.photoName.focus();
		 	return false;
		}
		if(document.form1.photoDes.value.trim()== ""){//����Ϊ��
		 	alert("����д��Ƭ������");
		 	document.form1.photoDes.focus();
		 	return false;
		}		
		if(document.form1.album.value == -1){	//û��ѡ�����
			alert("��ѡ���ϴ������");
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
	if(result == null){			//��������ϴ����ҳ��
%>
<table>
	<form name="form1" action="MyServlet" method="post">
		
			<tr>
				<td>ѡ���ϴ�����᣺</td>
<%
					
					ArrayList<String []> albumList = null;
					if(user == null){
%>
						���ȵ�¼!
<%
					}
					else{
						albumList = DBUtil.getAlbumList(user.u_no);
					
%>
				<td><select name="album" size="1">
	<%
					if(albumList.size() == 0){
	%>
						<option value="-1">����û�д����κ����</option>
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
				<td><input class="btn" type="button" value="�������" onclick="showCreate()"/></td>
				<td><div id="createAlbum"></div></td>
			</tr>
			<tr>
				<td>ͼƬ���ƣ�</td>
				<td><input name="photoName" width="180" /></td>
			</tr>
			<tr>
				<td>ͼƬ������</td>
				<td colspan="4"><textarea rows="3" name="photoDes" cols="40"></textarea></td>
			</tr>
		
		<input type="hidden" name="action" value="createAlbum" />
		<input type="hidden" name="u_no" value="<%=user.u_no %>" />
	</form>
	<form action="FileUploadServlet" name="form2" method="post"  enctype="multipart/form-data" onsubmit="return check();">
			<tr>
				<td>ͼƬ·����</td>
				<td><input type="file" name="photoPath" /></td>
				<td><input class="btn" type="submit" value="�ϴ�"/></td>
			</tr>
	</form>
	</table>
<%
	}
	else{				//��ʾ�ϴ����ҳ��
%>
	<p>	<%=result %>
		<a href="uploadImage.jsp">���ؼ���</a>
		<a name="seeAlbum" href="MyServlet?action=seeAlbum" target="content">�鿴���</a></p>
<%
		
	}
%>
</body>
</html>