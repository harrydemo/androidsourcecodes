package com.bao;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 登陆servlet
 * @author Fly
 * @Email jianbao_216@163.com
 *
 */
public class LoginServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//super.doGet(req, resp);
		System.out.println("=========doGet========");
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//super.doPost(req, resp);
		PrintWriter pw = resp.getWriter();
		System.out.println("=========doPost========");
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		System.out.println("=========username========"+username);
		System.out.println("=========password========"+password);
		
		//实际为数据库查询，模拟登陆成功
		if("test".equals(username) && "test".equals(password)){
			pw.println(true);
		}else{
			pw.println(false);
		}
	}
	
	

}
