package com.ly.bean;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.ly.handler.LYRegisterHandler;
import com.ly.vo.UserVO;
import com.mysql.jdbc.ResultSet;

public class LYRegisterBean {
	public String register(String url,InputStream in){
		SAXParserFactory sf = SAXParserFactory.newInstance();
		StringBuilder sb= new StringBuilder();
		System.out.println(url);
		
		try{
		XMLReader xr =  sf.newSAXParser().getXMLReader();
		
		LYRegisterHandler rh = new LYRegisterHandler(url);
		 xr.setContentHandler(rh);		
	     xr.parse(new InputSource(in));
	     UserVO uv=rh.getUv();
//	     String u_name=uv.getU_name();
//	     String u_pwd=uv.getU_word();
//	     String u_email=uv.getU_email();
//	     String u_sex= uv.getU_sex();
//	     String u_picname=uv.getU_picname();
		 String u_username=uv.getU_username();
		 String u_pwd=uv.getU_pwd();
		 String u_email=uv.getU_email();
		 String u_name=uv.getU_name();
		 String u_sex=uv.getU_sex();
		 
		 String sex="男";
		 if(u_sex.equals("0")){
			 sex="女";
		 }
		 else{
			 sex="男";
		 }
		 String u_phone=uv.getU_phone();
		 String u_head=uv.getU_headname();
		 String u_job=uv.getU_job();
		 String u_address=uv.getU_address();
		 String u_circle=uv.getU_circle();
		 String u_gz=uv.getU_gz();
		 String hobby="风味小吃，人文景观，自然风景";
		 if(u_gz.equals("100"))
		 {
			 hobby="风味小吃";
		 }
		 if(u_gz.equals("010"))
		 {
			 hobby="人文景观";
		 }
		 if(u_gz.equals("001"))
		 {
			 hobby="自然风景";
		 }
		 if(u_gz.equals("101"))
		 {
			 hobby="风味小吃，自然风景";
		 }
		 if(u_gz.equals("011"))
		 {
			 hobby="人文景观，自然风景";
		 }
		 if(u_gz.equals("110"))
		 {
			 hobby="风味小吃，人文景观";
		 }
		 if(u_gz.equals("111"))
		 {
			 hobby="风味小吃，人文景观，自然风景";
		 }
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/fx_donkey","root","123456");
				Statement st= con.createStatement();
				ResultSet rs = (ResultSet) st.executeQuery("select u_username from lyuser_table where u_username='"+u_username+"'");
				

				
						
				if(rs.next()){
					sb.append("<info>");	
					sb.append("0");
					sb.append("</info>");	
				}else{
					int row = st.executeUpdate("insert into fxuser_table(u_username,u_pwd,u_email,u_name,u_sex,u_phone,u_head,u_job,u_address,u_circle," +
							"u_gz) values('"+u_username+"','"+u_pwd+"','"+u_email+"','"+u_name+"','"+sex+"','"+u_phone+"','"+u_head+"','"+u_job+"','"+u_address+"','"+u_circle+"','"+hobby+"')");	
						sb.append("<info>");	
						sb.append("1");
						sb.append("</info>");
					
				}
				
				
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				
				
			}

			
		}catch(Exception e){
			e.printStackTrace();
		}
		return sb.toString();
	}

}
