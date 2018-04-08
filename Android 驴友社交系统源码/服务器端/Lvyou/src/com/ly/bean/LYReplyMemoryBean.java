package com.ly.bean;

import java.io.InputStream;
import java.sql.DriverManager;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;


import com.ly.handler.LYReplyMemoryHandler;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

public class LYReplyMemoryBean {
	public String replymemory(InputStream in){
		SAXParserFactory sf = SAXParserFactory.newInstance();
		StringBuilder sb = new StringBuilder();
		try {
			XMLReader xr =  sf.newSAXParser().getXMLReader();
			LYReplyMemoryHandler fmh = new LYReplyMemoryHandler();
			xr.setContentHandler(fmh);
			
			xr.parse(new InputSource(in));
			String uid=fmh.getUid();
			String mid=fmh.getMid();
			String content=fmh.getContent();
			
		
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/fx_donkey","root","123456");
			Statement st = (Statement) con.createStatement();	
			if(!uid.equals("null")&& !content.equals("")){
				System.out.println(uid);
			 st.executeUpdate("insert into fxmreply_table(mreply_content,u_id,memory_id) values('"+content+"',"+uid+","+mid+")");
			}
			 Statement st2 = (Statement) con.createStatement();
			 ResultSet rs2 = (ResultSet) st.executeQuery("select r.mreply_content,r.mreply_id,r.memory_id,u.u_name from fxuser_table u,fxmreply_table r,fxmenmory_table xm where u.u_id=r.u_id and xm.memory_id="+mid+" and r.memory_id="+mid+"");
			 sb.append("<replys>");
				
				 int type=0;
				while(rs2.next())
				{	
					type=1;
					sb.append("<reply>");
					sb.append("<content>");
					sb.append(""+rs2.getString(1));
					sb.append("</content>");
					sb.append("<mrid>");
					sb.append(""+rs2.getString(2));
					sb.append("</mrid>");
					sb.append("<mid>");
					sb.append(""+rs2.getString(3));
					sb.append("</mid>");
					sb.append("<name>");
					sb.append(""+rs2.getString(4));
					sb.append("</name>");
					sb.append("</reply>");
				}
				if(type==0)
				{
					sb.append("<error>");
					sb.append("0");
					sb.append("</error>");
				}
			
				sb.append("</replys>");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return sb.toString();
	}
}
