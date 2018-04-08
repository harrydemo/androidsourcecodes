package com.ly.bean;


import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;



import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;


import com.ly.handler.AttentionHandler;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

public class AttentionBean {
	public String friendlist(InputStream in){
		SAXParserFactory sf = SAXParserFactory.newInstance();
		StringBuilder sb = new StringBuilder();
		try {
			XMLReader xr =  sf.newSAXParser().getXMLReader();
			

			AttentionHandler ah = new AttentionHandler();
			xr.setContentHandler(ah);
			xr.parse(new InputSource(in));
			
			String id=ah.getId();
			
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/fx_donkey","root","123456");
			if (id.equals("null")){
				sb.append("<error>");
				sb.append("1");
				sb.append("</error>");
			}else{
			
			 Statement st2 = (Statement) con.createStatement();
			 
			 ResultSet rs2 = (ResultSet) ((java.sql.Statement) st2).executeQuery(" select f.attention_id,f.host_id,u1.u_name,f.other_id,u2.u_name from fxattention_table f,fxuser_table u1,fxuser_table u2 where f.host_id="+id+" and u1.u_id="+id+" and f.other_id=u2.u_id");
			 sb.append("<attentions>");
			
			 int type=0;
				while(rs2.next())
				{	
					type=1;
					
					sb.append("<aid>");
					sb.append(""+rs2.getString(1));
					sb.append("</aid>");
					
					sb.append("<hid>");
					sb.append(""+rs2.getString(2));
					sb.append("</hid>");
					
					sb.append("<hname>");
					sb.append(""+rs2.getString(3));
					sb.append("</hname>");
					
					sb.append("<oid>");
					sb.append(""+rs2.getString(4));
					sb.append("</oid>");
					
					sb.append("<oname>");
					sb.append(""+rs2.getString(5));
					sb.append("</oname>");
					/*System.out.println("aid: "+""+rs2.getString(1));
					System.out.println("hid: "+""+rs2.getString(2));
					System.out.println("hname: "+""+rs2.getString(3));
					System.out.println("oid: "+""+rs2.getString(4));
					System.out.println("oname: "+""+rs2.getString(5));*/
					
					
				}
				if(type==0)
				{
					sb.append("<error>");
					sb.append("0");
					sb.append("</error>");
				}
				
				sb.append("</attentions>");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString(); 
		
	}

}
