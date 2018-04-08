package com.ly.bean;


import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;



import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.ly.handler.FriendListHandler;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

public class ApplyBean {
	public String applys(InputStream in){
		SAXParserFactory sf = SAXParserFactory.newInstance();
		StringBuilder sb = new StringBuilder();
		try {
			XMLReader xr =  sf.newSAXParser().getXMLReader();
			
			FriendListHandler flh = new FriendListHandler();
			xr.setContentHandler(flh);
			xr.parse(new InputSource(in));
			
			String id=flh.getId();
			//System.out.println("FriendListBean:"+id);
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/fx_donkey","root","123456");
			//System.out.println("==="+id.isEmpty());
			if (id.equals("null")){
				sb.append("<error>");
				sb.append("1");
				sb.append("</error>");
			}else{
			 Statement st2 = (Statement) con.createStatement();
			
			 
			 ResultSet rs2 = (ResultSet) ((java.sql.Statement) st2).executeQuery(" select f.apply_id,f.u_id,u1.u_name,f.ue_id,u2.u_name from fxapply_table f,fxuser_table u1,fxuser_table u2 where f.u_id=u2.u_id and u1.u_id=u2.u_id and f.ue_id="+id+"");
			 sb.append("<friends>");
			//System.out.println("=============");
			 int type=0;
				while(rs2.next())
				{	
					type=1;
					
					sb.append("<fid>");
					sb.append(""+rs2.getString(1));
					sb.append("</fid>");
					
					sb.append("<uid>");
					sb.append(""+rs2.getString(2));
					sb.append("</uid>");
					
					sb.append("<uname>");
					sb.append(""+rs2.getString(3));
					sb.append("</uname>");
					
					sb.append("<ueid>");
					sb.append(""+rs2.getString(4));
					sb.append("</ueid>");
					
					sb.append("<fname>");
					sb.append(""+rs2.getString(5));
					sb.append("</fname>");
					
					/*
					System.out.println("id"+""+rs2.getString(1));
					System.out.println("uid"+""+rs2.getString(2));
					System.out.println("uname"+""+rs2.getString(3));
					System.out.println("ueid"+""+rs2.getString(4));
					System.out.println("fname"+""+rs2.getString(5));
					*/
					
				}
				if(type==0)
				{
					sb.append("<error>");
					sb.append("0");
					sb.append("</error>");
				}
				
				sb.append("</friends>");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		return sb.toString(); 
		
	}

}
