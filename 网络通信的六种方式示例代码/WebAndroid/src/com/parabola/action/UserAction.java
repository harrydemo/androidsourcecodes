package com.parabola.action;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.parabola.entity.User;
import com.parabola.service.impl.IUserServcie;

/** 
 * @���� song shi chao 
 * @QQ   421271944
 * @�������� 2011-8-22 
 * @�汾 V 1.0 
 */

public class UserAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {
	public IUserServcie userService;
	public User user = new User();
	
	private HttpServletRequest request;  
    private HttpServletResponse response;  
      
    public void setServletRequest(HttpServletRequest request) {  
        this.request = request;  
    }  
  
    public void setServletResponse(HttpServletResponse response) {  
        this.response = response;  
    }  
    
	 
	public IUserServcie getUserService() {
		return userService;
	}
 
	public void setUserService(IUserServcie userService) {
		this.userService = userService;
	}
 
	public User getUser() {
		return user;
	}
 
	public void setUser(User user) {
		this.user = user;
	}
 
	public void save(){ 
		System.out.println("---save()");
		  
		String method = request.getMethod();
		String format = request.getParameter("format");		
		String name = request.getParameter("name");
		String sex = request.getParameter("sex");
		String age = request.getParameter("age");
		String address = request.getParameter("address");
		
		user.setName(name);
		user.setSex(sex);
		user.setAge(age);
		user.setAddress(address); 
 		userService.add(user);
 		System.out.println("username1 ="+ name+sex+age+address);
 		
 		JSONArray jsonArray = new JSONArray(); 
	 	if(method.equals("GET")){ 
	       JSONObject jsonObject = new JSONObject();  
	       jsonObject.put("id", "1");  
	       jsonObject.put("title", "��������");  
	       jsonObject.put("timelength", 89);  
	         
	       JSONObject jsonObject1 = new JSONObject();  
	       jsonObject1.put("id", 2);  
	       jsonObject1.put("title", "�ٶ��뼤��");  
	       jsonObject1.put("timelength", 120);  
	         
	       JSONObject jsonObject2 = new JSONObject();  
	       jsonObject2.put("id", 3);  
	       jsonObject2.put("title", "���ν��3");  
	       jsonObject2.put("timelength", 100);  
	     
	       jsonArray.add(0, jsonObject);  
	       jsonArray.add(1, jsonObject1);  
	       jsonArray.add(2, jsonObject2); 
	       
	       response.setCharacterEncoding("UTF-8");  
	        try {
				response.getWriter().write(jsonArray.toString());
			} catch (IOException e) { 
				e.printStackTrace();
			} 
	 	}else{
	 		// �������ʽ��"POST"����ô����XML�ļ�����ʽ���ص��ͻ���
	 		toXML(); 
	 	}
	}
	
	public String register(){
		 
		return "register";
	}
	
	/**
	 * ����ƴ��һ��xml�ļ���������ֱ��д��ȥ�ģ�
	 * ��ʵ����ĿӦ���Ǵ����ݿ��ж�ȡ�����ģ��⿴�Բο������ĵ��еĵ�������ַ
	 */
	public void toXML(){
		StringBuilder sb = new StringBuilder(); 
		String s = new String();
		String[] strTemp = new String[3];
		
		try {  
			FileWriter fw = new FileWriter("songshichao.xml");
			BufferedWriter outXml = new BufferedWriter(fw);   
			outXml.write("<?xml version= \"1.0\" encoding=\"UTF-8\"?>");   
			outXml.newLine();   
			outXml.write("<people>");   
			  
			strTemp[0] = "song";    
			strTemp[1] = "shi";    
			strTemp[2] = "chao";    
			outXml.newLine();    
			outXml.write("<student>");    
			outXml.newLine();    
			outXml.write("<name>" + strTemp[0] + "</name>");    
			outXml.newLine();    
			outXml.write("<sex>" + strTemp[1] + "</sex>");    
			outXml.newLine();    
			outXml.write("<age>" + strTemp[2] + "</age>");    
			outXml.newLine();    
			outXml.write("</student>");     
			outXml.newLine();   
			outXml.write("</people>");   
			outXml.flush();  
			
	 // ������õ��ֽڶ�ȡ��������������ݣ������ġ�����Ӧ�øĳ��ַ���ȡ�������û�����������������ȡ
//			try {
//				byte[] b = new byte[30];
//				File f = new File("songshichao.xml");
//				FileInputStream ins = new FileInputStream(f);
//				int n = 0;
//				while((n = ins.read(b,0,2)) != -1){
//					s = new String(b,0,n);
//					sb.append(s);
//				}
			
			// ��������ַ�����ȡ����
//			try{
//				char[] b = new char[30];
//				File f = new File("songshichao.xml");
//				FileReader fr = new FileReader(f);
//				int n = 0;
//				while((n = fr.read(b,0,2)) != -1){
//					s = new String(b,0,n);
//					sb.append(s);
//				}
//				
//				response.setCharacterEncoding("UTF-8");  
//				System.out.println(sb);
//				response.getWriter().write(sb.toString());
//			} catch (Exception e) { 
//				e.printStackTrace();
//			}
		}catch (Exception e1) { 
			e1.printStackTrace();
		}
			 
	}
 
}
