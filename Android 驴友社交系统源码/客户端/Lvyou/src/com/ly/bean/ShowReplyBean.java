package com.ly.bean;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.ly.handler.ShowReplyHandler;

;

public class ShowReplyBean {
	public ArrayList<String[]> showreply(InputStream in){
		SAXParserFactory sf = SAXParserFactory.newInstance();
		ArrayList<String[]> list = null;
		try {
			XMLReader xr = sf.newSAXParser().getXMLReader();
			ShowReplyHandler srh = new ShowReplyHandler();
			xr.setContentHandler(srh);
			xr.parse(new InputSource(in));
			if(srh.getError()!=null){
				list=null;
			
			}else{
				list = srh.getList();
				
			}					
		
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}										
		return list;	
		
	}
}
