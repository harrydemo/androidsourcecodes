package com.ly.bean;

import java.io.InputStream;
import java.sql.DriverManager;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;


import com.ly.handler.DeleteApplyHandler;
import com.ly.handler.DeleteFriendsHandler;
import com.ly.handler.LYBuildMemoryHandler;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

public class DeleteFriendsBean {
	public void deleteFriends(InputStream in){
		SAXParserFactory sf = SAXParserFactory.newInstance();
		
		try {
			XMLReader xr =  sf.newSAXParser().getXMLReader();
			DeleteFriendsHandler dah = new DeleteFriendsHandler();
			xr.setContentHandler(dah);
			
			xr.parse(new InputSource(in));
		
			String fid = dah.getFid();
			String aid = dah.getAid();
			
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/fx_donkey","root","123456");
			Statement st = (Statement) con.createStatement();
			st.execute("delete from fxapply_table where apply_id= "+aid+"");
			st.execute("delete from fxfriend_table where friend_id= "+fid+"");
			
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
		
	}
}
