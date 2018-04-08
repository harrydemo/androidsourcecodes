package com.ly.bean;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.ly.handler.GetTogetherHandler;



public class GetTogetherBean {
	public ArrayList<String[]> gettogether(InputStream in){
		SAXParserFactory sf = SAXParserFactory.newInstance();
		ArrayList<String[]> list = null;
		try {
			XMLReader xr = sf.newSAXParser().getXMLReader();
			GetTogetherHandler th = new GetTogetherHandler();
			xr.setContentHandler(th);
			xr.parse(new InputSource(in));
			if(th.getError()!=null){
				list=null;
			
			}else{
				list = th.getList();
				
			}
			
			
		
		
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		return list;
		
		
	}
}
