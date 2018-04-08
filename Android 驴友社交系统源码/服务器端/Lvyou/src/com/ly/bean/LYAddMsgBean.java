package com.ly.bean;

import java.io.InputStream;
import java.sql.DriverManager;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;


import com.ly.handler.LYAddAttentionHandler;
import com.ly.handler.LYAddFriendsHandler;
import com.ly.handler.LYAddMsgHandler;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

public class LYAddMsgBean {
	public String addMsg(InputStream in){
		SAXParserFactory sf = SAXParserFactory.newInstance();
		StringBuilder sb = new StringBuilder();
		try {
			XMLReader xr =  sf.newSAXParser().getXMLReader();
			LYAddMsgHandler fah = new LYAddMsgHandler();
			xr.setContentHandler(fah);
			
			xr.parse(new InputSource(in));
			String uid=fah.getUid();
			String ueid=fah.getUeid();
			String time=fah.getTime();
			String content = fah.getContent();
			
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/fx_donkey","root","123456");
			Statement st = (Statement) con.createStatement();
			int s = st.executeUpdate("insert into fxmsg_table(msg_content,u_id,ue_id,msg_time) values('"+content+"','"+uid+"','"+ueid+"','"+time+"')");
			
			
			
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
