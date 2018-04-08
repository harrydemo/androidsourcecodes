package com.ly.bean;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


import com.ly.handler.LoginHandler;



public class LoginBean {
	String result="";
	public String password( InputStream in){
		SAXParserFactory sf = SAXParserFactory.newInstance();
//		StringBuilder sb = new StringBuilder();
		try {
			
			XMLReader xr = sf.newSAXParser().getXMLReader();
			LoginHandler vh = new LoginHandler();
			
			xr.setContentHandler(vh);
			
			
			xr.parse(new InputSource(in));
			
			if(vh.getError()!=null){
				result="error";
			}else{
				result =vh.getId()+","+vh.getName()+","+vh.getUname();
			}
		}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return result;
	}

	
}
