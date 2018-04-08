package com.ly.bean;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.ly.handler.LYRegisterHandler;

public class LYRegisterBean {
	public String register(InputStream in){
		String result="";
		SAXParserFactory sf = SAXParserFactory.newInstance();
		
		
		
		try{
		XMLReader xr =  sf.newSAXParser().getXMLReader();
		
		LYRegisterHandler rh = new LYRegisterHandler();
		 xr.setContentHandler(rh);		
	     xr.parse(new InputSource(in));
	     result=rh.getInfo();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	
	}

}
