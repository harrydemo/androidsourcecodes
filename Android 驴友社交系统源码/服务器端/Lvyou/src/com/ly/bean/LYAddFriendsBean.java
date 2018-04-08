package com.ly.bean;

import java.io.InputStream;
import java.sql.DriverManager;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;


import com.ly.handler.LYAddAttentionHandler;
import com.ly.handler.LYAddFriendsHandler;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

public class LYAddFriendsBean {
	public String addFriends(InputStream in){
		SAXParserFactory sf = SAXParserFactory.newInstance();
		StringBuilder sb = new StringBuilder();
		try {
			XMLReader xr =  sf.newSAXParser().getXMLReader();
			LYAddFriendsHandler ffh = new LYAddFriendsHandler();
			xr.setContentHandler(ffh);
			
			xr.parse(new InputSource(in));
			String hostid=ffh.getHostid();
			String otherid=ffh.getOtherid();
			
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/fx_donkey","root","123456");
			Statement st = (Statement) con.createStatement();
			 ResultSet rs2 = (ResultSet) ((java.sql.Statement) st).executeQuery(" select * from fxfriend_table  where u_id="+hostid+" and ue_id="+otherid+"");
			 int type=0;
			 while(rs2.next())
				{
				 type=1;
				 sb.append("<error>");
				 sb.append("0");
				 sb.append("</error>");
				}
			 if(type==0){
				 st.executeUpdate("insert into fxfriend_table(u_id,ue_id) values("+hostid+","+otherid+")");
				 st.executeUpdate("insert into fxapply_table(u_id,ue_id) values("+hostid+","+otherid+")");
				 ResultSet rs = (ResultSet) ((java.sql.Statement) st).executeQuery(" select * from fxuser_table  where u_id="+hostid+"");
				 sb.append("<id>");
				 sb.append(rs.getString(1));
				 sb.append("</id>");
				 sb.append("<name>");
				 sb.append(rs.getString(5));
				 sb.append("</name>");
			 }
			 //			 Statement st2 = (Statement) con.createStatement();
//			 ResultSet rs2 = (ResultSet) st.executeQuery("select e.user_id,u.user_name,e.emotion_content from user_table u,emotion_table e where u.user_id=e.user_id");
//			 sb.append("<emotions>");
//			 sb.append("<emotion>");
//				if(rs2.next())
//				{
//					sb.append("<id>");
//					sb.append(""+rs2.getString(1));
//					sb.append("</id>");
//					sb.append("<name>");
//					sb.append(""+rs2.getString(2));
//					sb.append("</name>");
//					sb.append("<content>");
//					sb.append(""+rs2.getString(3));
//					sb.append("</content>");
//				}
//				else
//				{
//					sb.append("<error>");
//					sb.append("0");
//					sb.append("</error>");
//				}
//				sb.append("</emotion>");
//				sb.append("</emotions>");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString(); 
		
	}
}
