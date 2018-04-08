<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK" import="wpf.*,java.io.*,java.util.*,java.sql.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<LINK href="global.css" type="text/css" rel="stylesheet"/>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title></title>
</head>
<body>
<%
	request.setCharacterEncoding(ConstantUtil.CHAR_ENCODING);		//设置编码格式
	String hid = (String)request.getParameter("hid");
	Blob blob = DBUtil.getHeadBlob(hid);			//获得头像数据
	if(blob == null){		//获取图片的Blob失败
		response.sendRedirect("/img/no_image.jpg");		//设置显示出错页面
	}
	else{				//成功获取图片的Blob
		long size = blob.length();	//获取长度
		byte [] bytes = blob.getBytes(1,(int)size);	//获得字节数组
		response.setContentType("image/jpeg");		//设置网页内容
		OutputStream outs = response.getOutputStream();	//获得输出流
		outs.write(bytes);		//写出数据
		outs.flush();
		out.clear();		//必须加上
		out = pageContext.pushBody();
	}
%>
</body>
</html>