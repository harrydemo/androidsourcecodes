package com.ly.bean;


import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;



import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.ly.handler.FriendListHandler;
import com.ly.handler.FriendsMeHandler;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

public class FriendsMeBean {
	public String friendsMe(InputStream in){
		SAXParserFactory sf = SAXParserFactory.newInstance();
		StringBuilder sb = new StringBuilder();
		try {
			XMLReader xr =  sf.newSAXParser().getXMLReader();
			
			FriendsMeHandler fmh = new FriendsMeHandler();
			xr.setContentHandler(fmh);
			xr.parse(new InputSource(in));
			String ueid = fmh.getUeid();
			int c = Integer.parseInt(fmh.getCount());
			//System.out.println("FriendListBean:"+id);
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/fx_donkey","root","123456");
			//System.out.println("==="+id.isEmpty());
			Statement st = (Statement) con.createStatement();
			 ResultSet rs = (ResultSet) ((java.sql.Statement) st).executeQuery("select u.u_name,u.u_head,m.memory_name,m.memory_address,m.memory_content,m.memory_id,u.u_id,m.memory_time from fxuser_table u,fxmenmory_table m where u.u_id= "+ueid+" and m.u_id= "+ueid+" limit "+c*5+","+"5");
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
		
		System.out.println(sb.toString());
		return sb.toString(); 
		
	}

}
