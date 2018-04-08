<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK" import="wpf.DBUtil,wpf.ConstantUtil,java.util.*,java.sql.*,java.io.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<LINK href="global.css" type="text/css" rel="stylesheet"/>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title></title>
</head>
<body>
<%
	request.setCharacterEncoding(ConstantUtil.CHAR_ENCODING);	//设置编码格式
	String pid = (String)request.getParameter("pid");			//读取图片ID
	Blob blob = DBUtil.getPhotoBlob(pid);						//调用DBUtil类的方法获得图片Blob
	if(blob == null){											//获取Blob图片失败
		response.sendRedirect("img/no_image.jpg");				//
	}
	else{													//成功获取数据
		long size = blob.length();							//获取长度
		byte [] bytes = blob.getBytes(1,(int)size);			//获取字节数组
		response.setContentType("image/jpeg");
		OutputStream outs = response.getOutputStream();		//获得输出流
		outs.write(bytes);									
		outs.flush();
		out.clear(); 										//必须加上的，否则异常
		out = pageContext.pushBody();						//返回PageContent对象
	}
%>
</body>
</html>