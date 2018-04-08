<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

  <body>
    	µÇÂ½²âÊÔ. <hr>
    	<form action="login.do" method="post">
    		<input type="text" name="username"/><br/>
    		<input type="password" name="password"/><br/>
    		<input type="submit" value="submit"/>
    		<input type="reset" value="reset"/>
    	</form>
  </body>
</html>
