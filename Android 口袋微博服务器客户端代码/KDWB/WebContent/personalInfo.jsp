<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK" import="wpf.*,java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<LINK href="global.css" type="text/css" rel="stylesheet"/>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>修改个人资料</title>
</head>
<script type="text/javascript">
	function check(){
		var u_no = document.form2.uno.value;
		var hdes = document.form2.hdes.value;
		document.form2.action="FileUploadServlet?action=uploadHead&uno="+u_no+"&hdes="+hdes;
		return true;
	}
	function checkInfo(){
		if(document.form1.uname.value == ""){		//如果昵称为空
			alert("昵称不能为空!");
			return false;
		}
		if(document.form1.uemail.value ==""){		//电子邮件为空
			alert("邮箱地址不能为空！");
			return false;
		}
		if(document.form1.ustate.value ==""){		//心情为空
			alert("请您填写心情！");
			return false;
		}
		return true;
	}
</script>
<body >
<%
	User user = (User)session.getAttribute("user");
	if(user != null){		//用户已登录
%>
<form action="MyServlet" name="form1" method="post" onsubmit="return checkInfo();">
	<table width="100%" border="0">
		<tr bgcolor="#c5edf3">
			 <td colspan="2" class="ziti">个人资料修改</td>
		</tr>
		<tr>
			<td class="ziti" align="right" width="100">昵称：</td>
			<td><input name="uname" value="<%=user.u_name%>"  class="f2_input2"></input></td>
		</tr>
		<tr>
			<td class="ziti" align="right" width="100">电子邮件：</td>
			<td><input name="uemail" value="<%=user.u_email %>" class="f2_input2"/></td>
		</tr>
		<tr>
			<td class="ziti" align="right" width="100">心情：</td>
			<td align="left"><input name="ustate" value="<%=user.u_state %> " class="f2_input2" /></td>
		</tr>
		<tr>
			<td  width="100"></td>
			<td align="left"><input class="btn" type="submit" value="修改" /></td>
		</tr>
	<%
		request.setCharacterEncoding(ConstantUtil.CHAR_ENCODING);		//确定编码
		Integer result = (Integer)request.getAttribute("changeInfoResult");
		if(result !=null){
			if(result==1){		//修改信息成功
		%>
			<tr>
				<td colspan="2">信息修改成功！</td>
			</tr>
		</table>
		<script type="text/javascript">
			window.parent.frames.item('login').location.reload();
		</script>
		<%
			}
			else{
		%>
			<tr>
				<td>信息修改失败！</td>
			</tr>
		</table>
		<%
			}
		}
	%>

	<input type="hidden" name="action" value="changeInfo"/>
	</form>
	

	<table border="0" width="100%">
		<tr bgcolor="#c5edf3">
			<td colspan="6" class="ziti">更改头像</td>
		</tr>
		<tr>
			<td valign="middle" colspan="5" class="ziti">当前头像:
			<img src="head.jsp?hid=<%=user.h_id %>" width="48" height="48"></img></td>
		</tr>
		<tr>
			<td align="left" colspan="5" class="ziti">请选择头像:</td>
		</tr>	
	<%
			int pageNo = 1;		//页号
			request.setCharacterEncoding(ConstantUtil.CHAR_ENCODING);		//确定编码
			String pages = (String)request.getParameter("pageNo");
			if(pages != null && pages.length() >0){		//如果参数有效
				pageNo= Integer.valueOf(pages);
				
			}
			int span = 15;		//每页显示的个数
			int lineSpan = 5;	//每行显示的个数
			ArrayList<HeadInfo> headList = wpf.DBUtil.getHeadList(pageNo,span);
			int listSize = headList.size();
			if(listSize == 0){		//当前没有可用头像
	%>
		<tr><td align="left" class="ziti">当前无可用头像！</td></tr>
	<%
			}
			else{
				int lines = listSize/lineSpan + (listSize%lineSpan==0?0:1);	//计算出要绘制几行
				for(int i=0;i<lines;i++){				//i为行数
	%>
				<tr>
	<%
					for(int j=0;j<lineSpan&&(i*lineSpan+j)<listSize;j++){	//对每一行进行绘制
						String tempHid=headList.get(i*lineSpan+j).h_id;
	%>	
					<td><a href="MyServlet?action=changeHead&hid=<%=tempHid%>&uno=<%=user.u_no %>">
						<img src="head.jsp?hid=<%=tempHid%>" width="48" height="48" border="0"></img>
					</a></td>
	<%
					}
	%>
				</tr>
				<tr><td align="center" colspan="5" class="ziti">
	<%
				}
				int maxHead = DBUtil.getHeadSize();
				int maxPage = maxHead/span+(maxHead%span==0?0:1);
				
				if(pageNo>1){		//显示上一页连接
	%>
				<div class="yahoo2" ><span class="pre"><a href="personalInfo.jsp?pageNo=<%=(pageNo-1) %>">&lt;上一页</a></span>
	<%
				}
				else{				//显示不可用的上一页
	%>
				<div class="yahoo2"><span class="pre">&lt;上一页</span>
	<%
				}
				if(pageNo<maxPage){	//显示下一页链接
	%>
				<span class="next"><a href="personalInfo.jsp?pageNo=<%=(pageNo+1) %>">下一页&gt;</a></span></div>
	<%			
				}
				else{			//显示不可用的下一页
	%>
				<span class="next">下一页&gt;</span></div>
	<%
				}
	%>
				</td></tr>
	<%
			}
	%>
		
	</table>

	<form action="FileUploadServlet" name="form2" method="post" enctype="multipart/form-data" onsubmit="return check();">
		<p class="ziti">上传头像:<input type="file" name="filePath"  /></p>
		<p class="ziti">&nbsp;&nbsp;&nbsp;&nbsp;描述:<input name="hdes" class="f2_input"/>
				<input class="btn" type="submit" value="上传" /></p>
	<input type="hidden" name="uno" value="<%=user.u_no %>" />
	</form>
<%
	}
%>
	<%
		String uploadResult = (String)request.getAttribute("result");
		if(uploadResult != null){
			if(uploadResult.equals(ConstantUtil.UPLOAD_SIZE_EXCEED)){
	%>	
			<center><%=uploadResult %></center>
	<%
			}
		}
	%>
</body>
</html>