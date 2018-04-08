package com.ly.bean;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.ly.handler.ShowMemoryHandler;
import com.ly.handler.TogetherHandler;

public class ShowMemoryBean {
	public String showMemory(InputStream in) {
		// TODO Auto-generated method stub
		SAXParserFactory sf = SAXParserFactory.newInstance();
		StringBuilder sb = new StringBuilder();				
		try {
			XMLReader xr = sf.newSAXParser().getXMLReader();
			ShowMemoryHandler smh = new ShowMemoryHandler();
			xr.setContentHandler(smh);
			xr.parse(new InputSource(in));
			
			int c = Integer.parseInt(smh.getCount());
			//System.out.println(c);
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/fx_donkey","root","123456");
			
			Statement st1 = con.createStatement();
			ResultSet rs = st1.executeQuery("select u.u_name,u.u_head,m.memory_name,m.memory_address,m.memory_content,m.memory_id,u.u_id,m.memory_time,m.memorytag_title,m.memorytag_type,m.memorytag_content from fxuser_table u,fxmenmory_table m where u.u_id=m.u_id limit "+c*5+","+"5");
			int type = 0;
			sb.append("<memorys>");
			while(rs.next()){
				
				type = 1;
				sb.append("<memory>");
				sb.append("<uname>");
				sb.append(rs.getString(1)+"");
				sb.append("</uname>");
				sb.append("<pic>");
				sb.append(rs.getString(2)+"");
				sb.append("</pic>");
				sb.append("<title>");
				sb.append(rs.getString(3)+"");
				sb.append("</title>");
				sb.append("<address>");
				sb.append(rs.getString(4)+"");
				sb.append("</address>");
				sb.append("<content>");
				sb.append(rs.getString(5)+"");
				sb.append("</content>");
				sb.append("<mid>");
				sb.append(rs.getString(6)+"");
				sb.append("</mid>");
				sb.append("<uid>");
				sb.append(rs.getString(7)+"");
				sb.append("</uid>");
				sb.append("<time>");
				sb.append(rs.getString(8)+"");
				sb.append("</time>");
				sb.append("<tagtitle>");
				sb.append(rs.getString(9)+"");
				sb.append("</tagtitle>");
				sb.append("<tagtype>");
				sb.append(rs.getString(10)+"");
				sb.append("</tagtype>");
				sb.append("<tagcontent>");
				sb.append(rs.getString(11)+"");
				sb.append("</tagcontent>");
				
				sb.append("</memory>");
			}
			if(type==0){
				sb.append("<error>");
				sb.append("0");
				sb.append("</error>");
				
			}
			type = 0;
			sb.append("</memorys>");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sb.toString();
	}
}
