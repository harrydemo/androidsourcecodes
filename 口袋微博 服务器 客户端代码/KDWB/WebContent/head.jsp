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
	request.setCharacterEncoding(ConstantUtil.CHAR_ENCODING);		//���ñ����ʽ
	String hid = (String)request.getParameter("hid");
	Blob blob = DBUtil.getHeadBlob(hid);			//���ͷ������
	if(blob == null){		//��ȡͼƬ��Blobʧ��
		response.sendRedirect("/img/no_image.jpg");		//������ʾ����ҳ��
	}
	else{				//�ɹ���ȡͼƬ��Blob
		long size = blob.length();	//��ȡ����
		byte [] bytes = blob.getBytes(1,(int)size);	//����ֽ�����
		response.setContentType("image/jpeg");		//������ҳ����
		OutputStream outs = response.getOutputStream();	//��������
		outs.write(bytes);		//д������
		outs.flush();
		out.clear();		//�������
		out = pageContext.pushBody();
	}
%>
</body>
</html>