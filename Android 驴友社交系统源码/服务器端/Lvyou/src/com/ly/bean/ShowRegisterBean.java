package com.ly.bean;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.ly.handler.ShowRegisterHandler;



public class ShowRegisterBean {
	public String showRegister(InputStream in) {
		// TODO Auto-generated method stub
		SAXParserFactory sf = SAXParserFactory.newInstance();
		StringBuilder sb = new StringBuilder();				
		try {
			XMLReader xr = sf.newSAXParser().getXMLReader();
			ShowRegisterHandler srh = new ShowRegisterHandler();
			xr.setContentHandler(srh);
			xr.parse(new InputSource(in));
			 String id=srh.getId();
			 System.out.println("ShowRegisterBean:"+id);
			//int c = Integer.parseInt(smh.getCount());
			//System.out.println(c);
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/fx_donkey","root","123456");
			
			Statement st1 = con.createStatement();
			ResultSet rs = st1.executeQuery("select u_head,u_name,u_email,u_sex,u_phone,u_job,u_address,u_circle,u_gz from fxuser_table  where u_id= "+id);
			int type = 0;
			sb.append("<user>");
			while(rs.next()){
				
				type = 1;
				sb.append("<pic>");
				sb.append(rs.getString(1)+"");
				sb.append("</pic>");
				sb.append("<uname>");
				sb.append(rs.getString(2)+"");
				sb.append("</uname>");
				sb.append("<email>");
				sb.append(rs.getString(3)+"");
				sb.append("</email>");
				sb.append("<sex>");
				sb.append(rs.getString(4)+"");
				sb.append("</sex>");
				sb.append("<phone>");
				sb.append(rs.getString(5)+"");
				sb.append("</phone>");
				sb.append("<job>");
				sb.append(rs.getString(6)+"");
				sb.append("</job>");
				sb.append("<address>");
				sb.append(rs.getString(7)+"");
				sb.append("</address>");
				
				sb.append("<circle>");
				sb.append(rs.getString(8)+"");
				sb.append("</circle>");
				sb.append("<gz>");
				sb.append(rs.getString(9)+"");
				sb.append("</gz>");
				
				
			}
			if(type==0){
				sb.append("<error>");
				sb.append("0");
				sb.append("</error>");
				
			}
			type = 0;
			sb.append("</user>");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sb.toString();
	}
}
