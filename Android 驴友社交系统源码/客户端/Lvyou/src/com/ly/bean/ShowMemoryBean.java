package com.ly.bean;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.ly.handler.ShowMemoryHandler;



public class ShowMemoryBean {
	public ArrayList<String[]> gettogether(InputStream in){
		SAXParserFactory sf = SAXParserFactory.newInstance();
		ArrayList<String[]> list = null;
		try {
			XMLReader xr = sf.newSAXParser().getXMLReader();
			ShowMemoryHandler smh = new ShowMemoryHandler();
			xr.setContentHandler(smh);
			xr.parse(new InputSource(in));
			if(smh.getError()!=null){
				list=null;
			
			}else{
				list = smh.getList();
				
			}					
		
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}										
		return list;	
		
	}
}
