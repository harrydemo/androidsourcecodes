<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK" import="wpf.ConstantUtil"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<LINK href="global.css" type="text/css" rel="stylesheet"/>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>ע��</title>
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
			alert("���ע����Ϣ��д������");
			return false;
		}
		if(document.form1.u_pwd.value.trim() != document.form1.u_pwd2.value.trim())
		{
			alert("������������벻һ�£�");
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
	if(result == null){		//��ʾע�����
		int colorIndex = 0;
%>
		<form action="MyServlet" method="post" name="form1">
			<table	align="center" width="65%" height="100%" border="0">
				<tr >
					<td align="left" colspan="2"><font size="5" style="font-style: inherit">ע��ڴ�΢��</font></td>
				</tr>	
				<tr >
					<td align="left" colspan="2" class="arinfo2">
							��ӭע��ڴ�΢���������ĺ��Ѵ�������������������Ȥ��
					</td>
				</tr>	
				<tr><td colspan="2"><hr color="ffde9e"/></td></tr>
				<tr height="13px"></tr>							
				<tr >
					<td align="left">�ǳƣ�</td>
					<td><input name="u_name" class="f2_input2"/></td>
				</tr>
				<tr>
					<td align="left">���䣺</td>
					<td><input name="u_email" class="f2_input2"/></td>
				</tr>
				<tr>
					<td align="left">���룺</td>
					<td><input type="password" name="u_pwd" class="f2_input2"/></td>
				</tr>		
				<tr>
					<td align="left">ȷ�����룺</td>
					<td><input type="password" name="u_pwd2" class="f2_input2"/></td>
				</tr>	
				<tr >
					<td align="left">����״̬��</td>
					<td><input name="u_state" value="�ҿ�ͨ��΢������" class="f2_input2"/></td>
				</tr>	
				<tr height="10px"></tr>	
				<tr>
					<td align="center"><input class="btn2" type="submit" value="ע��" onclick="return checkToSubmit()" /></td>
					<td><input class="btn2" type="reset" value="���" /></td>
				</tr>							
			</table>
			<input type="hidden" name="action" value="register" />
		</form>
<%
	}
	else{	//��ע������Ϣ���ؽ���
		try{
			Integer.parseInt(result);
%>
	ע��ɹ�������΢������Ϊ��<%=result %>��
<%
		}catch(Exception e){
%>
	ע��ʧ�ܣ��������ԣ�
<%
		}
	}
%>
	<script type="text/javascript">
		window.parent.frames.item('login').location.reload();
	</script>
</body>
</html>