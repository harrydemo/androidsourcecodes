<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK" import="wpf.ConstantUtil"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<LINK href="global.css" type="text/css" rel="stylesheet"/>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>注册</title>
<script type="text/javascript">
	String.prototype.trim = function(){
	   return this.replace(/(^\s*)|(\s*$)/g, "");
	};
	function checkToSubmit()
	{
		if(document.form1.u_name.value.trim() == ""
			|| document.form1.u_email.value.trim() == ""
			|| document.form1.u_pwd.value.trim() == ""
			|| document.form1.u_pwd2.value.trim() == ""
			|| document.form1.u_state.value.trim() == "")
		{
			alert("请填将注册信息填写完整！");
			return false;
		}
		if(document.form1.u_pwd.value.trim() != document.form1.u_pwd2.value.trim())
		{
			alert("两次输入的密码不一致！");
			return false;
		}
		return true;			
	}
</script>
</head>
<body>
<%
	request.setCharacterEncoding(ConstantUtil.CHAR_ENCODING);
	String result = (String)request.getAttribute("result");
	if(result == null){		//显示注册界面
		int colorIndex = 0;
%>
		<form action="MyServlet" method="post" name="form1">
			<table	align="center" width="65%" height="100%" border="0">
				<tr >
					<td align="left" colspan="2"><font size="5" style="font-style: inherit">注册口袋微博</font></td>
				</tr>	
				<tr >
					<td align="left" colspan="2" class="arinfo2">
							欢迎注册口袋微博，把您的好友带进来更会增加您的兴趣。
					</td>
				</tr>	
				<tr><td colspan="2"><hr color="ffde9e"/></td></tr>
				<tr height="13px"></tr>							
				<tr >
					<td align="left">昵称：</td>
					<td><input name="u_name" class="f2_input2"/></td>
				</tr>
				<tr>
					<td align="left">邮箱：</td>
					<td><input name="u_email" class="f2_input2"/></td>
				</tr>
				<tr>
					<td align="left">密码：</td>
					<td><input type="password" name="u_pwd" class="f2_input2"/></td>
				</tr>		
				<tr>
					<td align="left">确认密码：</td>
					<td><input type="password" name="u_pwd2" class="f2_input2"/></td>
				</tr>	
				<tr >
					<td align="left">心情状态：</td>
					<td><input name="u_state" value="我开通了微博诶。" class="f2_input2"/></td>
				</tr>	
				<tr height="10px"></tr>	
				<tr>
					<td align="center"><input class="btn2" type="submit" value="注册" onclick="return checkToSubmit()" /></td>
					<td><input class="btn2" type="reset" value="清空" /></td>
				</tr>							
			</table>
			<input type="hidden" name="action" value="register" />
		</form>
<%
	}
	else{	//是注册后的信息返回界面
		try{
			Integer.parseInt(result);
%>
	注册成功，您的微博号码为：<%=result %>。
<%
		}catch(Exception e){
%>
	注册失败，请您重试！
<%
		}
	}
%>
	<script type="text/javascript">
		window.parent.frames.item('login').location.reload();
	</script>
</body>
</html>