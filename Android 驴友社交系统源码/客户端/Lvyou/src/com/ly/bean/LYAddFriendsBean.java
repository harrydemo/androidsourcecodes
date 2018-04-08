package com.ly.bean;

import java.io.InputStream;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.ly.handler.LYAddAttentionHandler;
import com.ly.handler.LYAddFriendsHandler;



public class LYAddFriendsBean {
	String result="";
	public String addfriends( InputStream in){
		SAXParserFactory sf = SAXParserFactory.newInstance();
//		StringBuilder sb = new StringBuilder();
		try {
			
			XMLReader xr = sf.newSAXParser().getXMLReader();
			LYAddFriendsHandler fah = new LYAddFriendsHandler();
			
			xr.setContentHandler(fah);
			
			
			xr.parse(new InputSource(in));
			
			if(fah.getError()!=null){
				result="error";
			}else{
				
			}
		}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return result;
	}

	
}
