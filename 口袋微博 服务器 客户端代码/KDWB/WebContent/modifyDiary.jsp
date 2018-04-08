<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK" import="wpf.*,java.sql.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<LINK href="global.css" type="text/css" rel="stylesheet"/>
	<head>
		<title></title>
	</head>
	<body>
<%
	request.setCharacterEncoding(ConstantUtil.CHAR_ENCODING);
	Integer result = (Integer)request.getAttribute("result");
	if(result == null){
		String rid = request.getParameter("r_id");
		String rtitle = request.getParameter("r_title");
		String rcontent = request.getParameter("r_content");
%>
	<form action="MyServlet" method="post" >
		<table border="0" align="center">
			<tr bgcolor="#37579e">
				<td colspan="2">编辑日志</td>
			</tr>
			<tr>
				<td colspan="2">标题：
				<input type="text" class="f2_input" name="r_title" value="<%=rtitle %>" ></input></td>
			</tr>
			<tr>
				<td colspan="2">
					<textarea class="f2_input2" rows="15" cols="80" name="r_content"><%=rcontent %></textarea>
				</td>
			</tr>
			<tr>
				<td align="right">
					<input class="btn" type="submit" class="btn" value="重新发表" ></input>
				</td>
				<td align="left">
					<input class="btn" type="button" class="btn" value="返回" ></input>
				</td>
			</tr>
		</table>
		<input type="hidden" name="r_id" value="<%=rid %>" />"
		<input type="hidden" name="action" value="modifyDiary" />"
	</form>
<%
	}
	else{
		if(result ==1){
%>
		日志修改成功，请在日志列表中查看！
<%
		}
		else {
%>
		日志修改失败，请查看日志列表！
<%
		}
	}
%>
	</body>
</html>