package com.ly.bean;

import java.io.ByteArrayOutputStream;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.ly.handler.LoginHandler;



import sun.misc.BASE64Encoder;



public class LoginBean {
	
	public String password( InputStream in){
		SAXParserFactory sf = SAXParserFactory.newInstance();
		StringBuilder sb = new StringBuilder();
		try {
			
			XMLReader xr = sf.newSAXParser().getXMLReader();
			LoginHandler vh = new LoginHandler();
			
			xr.setContentHandler(vh);
			
			
			xr.parse(new InputSource(in));
			String name = vh.getName();
			String pwd = vh.getPwd();
			String count = vh.getCount();
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/fx_donkey","root","123456");
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select * from fxuser_table where u_username='"+name+"'and u_pwd='"+pwd+"'");
			
			sb.append("<user>");
			
			
			if(rs.next()){
				sb.append("<id>");
				sb.append(""+rs.getInt(1));
				sb.append("</id>");
				sb.append("<name>");
				sb.append(""+rs.getString(2));
				sb.append("</name>");
				sb.append("<sex>");
				sb.append(""+rs.getString(4));
				sb.append("</sex>");
				sb.append("<uname>");
				sb.append(""+rs.getString(5));
				sb.append("</uname>");
				
			}
			else{
				sb.append("<error>");
				sb.append("0");
				sb.append("</error>");
				if(count.equals("3")){
					sb.append("<no>");
					sb.append("1");
					sb.append("</no>");
					
				}

			}
			sb.append("</user>");
		}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return sb.toString();
	}

	
}
