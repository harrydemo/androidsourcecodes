package com.dongzi.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;

public class loginAction extends ActionSupport implements ServletRequestAware,
		ServletResponseAware {

	private static final long serialVersionUID = 1L;
	HttpServletRequest request;
	HttpServletResponse response;

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void login() {
		try {
			// HttpServletRequest request =ServletActionContext.getRequest();
			// HttpServletResponse response=ServletActionContext.getResponse();
			this.response.setContentType("text/html;charset=utf-8");
			this.response.setCharacterEncoding("UTF-8");
			if (this.request.getParameter("username").equals("123456")) {
				this.response.getWriter().write("��ĺ����,�ձ��ˣ�");
			} else if (this.request.getParameter("username").equals("zhd")) {
				this.response.getWriter().write("û�д��Ҿ��Ƕ��Ӹ磡");
			} else {
				this.response.getWriter().write("�Ҿ��Ƕ��Ӹ磡");
			}

			// ��Ҫ���ص�ʵ��������json����
			// JSONObject json=JSONObject.fromObject(this.getUsername());
			// �����ʽ�磺{"id":1, "username":"zhangsan", "pwd":"123"}
			// System.out.println(json);
			// this.response.getWriter().write(json.toString());

			/**
			 * JSONObject json=new JSONObject(); json.put("login", "login");
			 * response.setContentType("text/html;charset=utf-8");
			 * System.out.println(json); byte[] jsonBytes =
			 * json.toString().getBytes("utf-8");
			 * response.setContentLength(jsonBytes.length);
			 * response.getOutputStream().write(jsonBytes);
			 **/
			/**
			 * JSONObject json=new JSONObject(); json.put("login", "login");
			 * byte[] jsonBytes = json.toString().getBytes("utf-8");
			 * response.setContentType("text/html;charset=utf-8");
			 * response.setContentLength(jsonBytes.length);
			 * response.getOutputStream().write(jsonBytes);
			 * response.getOutputStream().flush();
			 * response.getOutputStream().close();
			 **/
		} catch (Exception e) {
			e.printStackTrace();
		} 
		// return null; }}
	}
}