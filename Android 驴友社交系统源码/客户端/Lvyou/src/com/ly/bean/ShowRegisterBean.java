package com.ly.bean;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.ly.handler.ShowRegisterHandler;



public class ShowRegisterBean {
	public String showregister(InputStream in){
		SAXParserFactory sf = SAXParserFactory.newInstance();
		String result = null;
		try {
			XMLReader xr = sf.newSAXParser().getXMLReader();
			ShowRegisterHandler srh = new ShowRegisterHandler();
			xr.setContentHandler(srh);
			xr.parse(new InputSource(in));
	//		list = srh.getList();
			if(srh.getError()!=null){
				result="error";
			
			}else{
				result=srh.getPic()+","+srh.getUname()+","+srh.getEmail()+","+srh.getSex()+","+srh.getPhone()+","+srh.getJob()+","+srh.getAddress()+","+srh.getCircle()+","+srh.getGz();
				
			}					
		
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}										
		return result;	
		
	}
}
