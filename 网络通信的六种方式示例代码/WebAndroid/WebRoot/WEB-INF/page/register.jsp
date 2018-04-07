<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"/>
    
    <title>注册</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
  <div align="center">
  <s:a href="namespace/userlist.action">显示</s:a>
  <s:a href="user/usercheck.jsp">查询</s:a>
  <h1>注册</h1>
   
	<s:form action="saveUserAction" method="post">
	    姓名：<s:textfield label="姓名" name="user.name"></s:textfield> <br/>
	  性别： <s:textfield label="性别" name="user.sex"></s:textfield><br/>
	    年龄：<s:textfield label="年龄" name="user.age"></s:textfield><br/>
	    地址：<s:textfield label="地址" name="user.address"></s:textfield> <br/>
	    <s:submit value="注册"></s:submit>
	</s:form>
		
    <s:debug></s:debug>
    </div>
  </body>
</html>
