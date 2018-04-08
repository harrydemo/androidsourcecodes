package com.ly.bean;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.ly.handler.TogetherHandler;





public class TogetherBean {
	public void textwrite(InputStream in){
		SAXParserFactory sf = SAXParserFactory.newInstance();
		
		try {
			XMLReader xr = sf.newSAXParser().getXMLReader();
			TogetherHandler th = new TogetherHandler();
			xr.setContentHandler(th);
			xr.parse(new InputSource(in));
			String id = th.getId();
			String time = th.getTime();
			String content = th.getContent();
			String title =th.getTitle();
			String gtime = th.getGtime();
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/fx_donkey","root","123456");
			Statement st = con.createStatement();
			int i = st.executeUpdate("insert into fxraveltogether_table(u_id,raveltogether_content,raveltogether_time,raveltogether_title,raveltogether_gtime) values("+id+",'"+content+"','"+time+"','"+title+"','"+gtime+"')");
			
				
			
		
		
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		
		
		
	}
}
