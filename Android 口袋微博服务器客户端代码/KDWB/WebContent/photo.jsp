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
	request.setCharacterEncoding(ConstantUtil.CHAR_ENCODING);	//���ñ����ʽ
	String pid = (String)request.getParameter("pid");			//��ȡͼƬID
	Blob blob = DBUtil.getPhotoBlob(pid);						//����DBUtil��ķ������ͼƬBlob
	if(blob == null){											//��ȡBlobͼƬʧ��
		response.sendRedirect("img/no_image.jpg");				//
	}
	else{													//�ɹ���ȡ����
		long size = blob.length();							//��ȡ����
		byte [] bytes = blob.getBytes(1,(int)size);			//��ȡ�ֽ�����
		response.setContentType("image/jpeg");
		OutputStream outs = response.getOutputStream();		//��������
		outs.write(bytes);									
		outs.flush();
		out.clear(); 										//������ϵģ������쳣
		out = pageContext.pushBody();						//����PageContent����
	}
%>
</body>
</html>