<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK" import="wpf.*,java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<LINK href="global.css" type="text/css" rel="stylesheet"/>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�޸ĸ�������</title>
</head>
<script type="text/javascript">
	function check(){
		var u_no = document.form2.uno.value;
		var hdes = document.form2.hdes.value;
		document.form2.action="FileUploadServlet?action=uploadHead&uno="+u_no+"&hdes="+hdes;
		return true;
	}
	function checkInfo(){
		if(document.form1.uname.value == ""){		//����ǳ�Ϊ��
			alert("�ǳƲ���Ϊ��!");
			return false;
		}
		if(document.form1.uemail.value ==""){		//�����ʼ�Ϊ��
			alert("�����ַ����Ϊ�գ�");
			return false;
		}
		if(document.form1.ustate.value ==""){		//����Ϊ��
			alert("������д���飡");
			return false;
		}
		return true;
	}
</script>
<body >
<%
	User user = (User)session.getAttribute("user");
	if(user != null){		//�û��ѵ�¼
%>
<form action="MyServlet" name="form1" method="post" onsubmit="return checkInfo();">
	<table width="100%" border="0">
		<tr bgcolor="#c5edf3">
			 <td colspan="2" class="ziti">���������޸�</td>
		</tr>
		<tr>
			<td class="ziti" align="right" width="100">�ǳƣ�</td>
			<td><input name="uname" value="<%=user.u_name%>"  class="f2_input2"></input></td>
		</tr>
		<tr>
			<td class="ziti" align="right" width="100">�����ʼ���</td>
			<td><input name="uemail" value="<%=user.u_email %>" class="f2_input2"/></td>
		</tr>
		<tr>
			<td class="ziti" align="right" width="100">���飺</td>
			<td align="left"><input name="ustate" value="<%=user.u_state %> " class="f2_input2" /></td>
		</tr>
		<tr>
			<td  width="100"></td>
			<td align="left"><input class="btn" type="submit" value="�޸�" /></td>
		</tr>
	<%
		request.setCharacterEncoding(ConstantUtil.CHAR_ENCODING);		//ȷ������
		Integer result = (Integer)request.getAttribute("changeInfoResult");
		if(result !=null){
			if(result==1){		//�޸���Ϣ�ɹ�
		%>
			<tr>
				<td colspan="2">��Ϣ�޸ĳɹ���</td>
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
				<td>��Ϣ�޸�ʧ�ܣ�</td>
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
			<td colspan="6" class="ziti">����ͷ��</td>
		</tr>
		<tr>
			<td valign="middle" colspan="5" class="ziti">��ǰͷ��:
			<img src="head.jsp?hid=<%=user.h_id %>" width="48" height="48"></img></td>
		</tr>
		<tr>
			<td align="left" colspan="5" class="ziti">��ѡ��ͷ��:</td>
		</tr>	
	<%
			int pageNo = 1;		//ҳ��
			request.setCharacterEncoding(ConstantUtil.CHAR_ENCODING);		//ȷ������
			String pages = (String)request.getParameter("pageNo");
			if(pages != null && pages.length() >0){		//���������Ч
				pageNo= Integer.valueOf(pages);
				
			}
			int span = 15;		//ÿҳ��ʾ�ĸ���
			int lineSpan = 5;	//ÿ����ʾ�ĸ���
			ArrayList<HeadInfo> headList = wpf.DBUtil.getHeadList(pageNo,span);
			int listSize = headList.size();
			if(listSize == 0){		//��ǰû�п���ͷ��
	%>
		<tr><td align="left" class="ziti">��ǰ�޿���ͷ��</td></tr>
	<%
			}
			else{
				int lines = listSize/lineSpan + (listSize%lineSpan==0?0:1);	//�����Ҫ���Ƽ���
				for(int i=0;i<lines;i++){				//iΪ����
	%>
				<tr>
	<%
					for(int j=0;j<lineSpan&&(i*lineSpan+j)<listSize;j++){	//��ÿһ�н��л���
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
				
				if(pageNo>1){		//��ʾ��һҳ����
	%>
				<div class="yahoo2" ><span class="pre"><a href="personalInfo.jsp?pageNo=<%=(pageNo-1) %>">&lt;��һҳ</a></span>
	<%
				}
				else{				//��ʾ�����õ���һҳ
	%>
				<div class="yahoo2"><span class="pre">&lt;��һҳ</span>
	<%
				}
				if(pageNo<maxPage){	//��ʾ��һҳ����
	%>
				<span class="next"><a href="personalInfo.jsp?pageNo=<%=(pageNo+1) %>">��һҳ&gt;</a></span></div>
	<%			
				}
				else{			//��ʾ�����õ���һҳ
	%>
				<span class="next">��һҳ&gt;</span></div>
	<%
				}
	%>
				</td></tr>
	<%
			}
	%>
		
	</table>

	<form action="FileUploadServlet" name="form2" method="post" enctype="multipart/form-data" onsubmit="return check();">
		<p class="ziti">�ϴ�ͷ��:<input type="file" name="filePath"  /></p>
		<p class="ziti">&nbsp;&nbsp;&nbsp;&nbsp;����:<input name="hdes" class="f2_input"/>
				<input class="btn" type="submit" value="�ϴ�" /></p>
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