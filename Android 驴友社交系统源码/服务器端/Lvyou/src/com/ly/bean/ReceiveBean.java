package com.ly.bean;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.ly.handler.ReceiveHandler;
import com.ly.handler.ShowMemoryHandler;

public class ReceiveBean {
	public String setLog(InputStream in) {
		// TODO Auto-generated method stub
		SAXParserFactory sf = SAXParserFactory.newInstance();
		StringBuilder sb = new StringBuilder();
		sb.append("<logs>");
		
		try {
			XMLReader xr = sf.newSAXParser().getXMLReader();
			ReceiveHandler rh = new ReceiveHandler();
			xr.setContentHandler(rh);
			xr.parse(new InputSource(in));
			int c = Integer.parseInt(rh.getCount());
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/fx_donkey","root","123456");
			
			Statement st1 = con.createStatement();
			ResultSet rs = st1.executeQuery("select u.u_name,u.u_head,r.raveltogether_content,r.raveltogether_time,r.raveltogether_title,r.raveltogether_gtime from fxuser_table u,fxraveltogether_table r where u.u_id=r.u_id limit "+c*5+","+"5");
			int type = 0;
			while(rs.next()){
				
				type = 1;
				sb.append("<log>");
				sb.append("<name>");
				sb.append(rs.getString(1)+"");
				sb.append("</name>");
				sb.append("<pic>");
				sb.append(rs.getString(2)+"");
				sb.append("</pic>");
				sb.append("<content>");
				sb.append(rs.getString(3)+"");
				sb.append("</content>");
				sb.append("<time>");
				sb.append(rs.getString(4)+"");
				sb.append("</time>");
				sb.append("<title>");
				sb.append(rs.getString(5)+"");
				sb.append("</title>");
				sb.append("<gtime>");
				sb.append(rs.getString(6)+"");
				sb.append("</gtime>");
				sb.append("</log>");
			}
			if(type==0){
				sb.append("<error>");
				sb.append("0");
				sb.append("</error>");
			}
			type = 0;
			sb.append("</logs>");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sb.toString();
	}
}
