package com.ly.bean;

import java.io.InputStream;
import java.sql.DriverManager;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;


import com.ly.handler.LYBuildMemoryHandler;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

public class LYBuildMemoryBean {
	public void buildmemory(InputStream in){
		SAXParserFactory sf = SAXParserFactory.newInstance();
		StringBuilder sb = new StringBuilder();
		try {
			XMLReader xr =  sf.newSAXParser().getXMLReader();
			LYBuildMemoryHandler fmh = new LYBuildMemoryHandler();
			xr.setContentHandler(fmh);
			
			xr.parse(new InputSource(in));
			String uid=fmh.getUid();
			String title=fmh.getTitle();
			String address=fmh.getAddress();
			String content=fmh.getContent();			
			String time = fmh.getTime();
			String tagtitle=fmh.getTagtitle();
			String tagtype=fmh.getTagtype();
			String tagcontent=fmh.getTagcontent();
			
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/fx_donkey","root","123456");
			Statement st = (Statement) con.createStatement();			
			 st.executeUpdate("insert into fxmenmory_table(memory_content,memory_name,memory_address,u_id,memory_time,memorytag_title,memorytag_type,memorytag_content) values('"+content+"','"+title+"','"+address+"',"+uid+",'"+time+"','"+tagtitle+"','"+tagtype+"','"+tagcontent+"')");
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
