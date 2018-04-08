  package com.ly.bean;

import java.io.InputStream;


import java.util.ArrayList;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.ly.handler.ApplyHandler;
import com.ly.handler.FriendListHandler;


public class ApplyBean {
public ArrayList<String[]> applys(InputStream in){
		
		
		SAXParserFactory sf = SAXParserFactory.newInstance();
		ArrayList<String[]> list=null;
		try {
			XMLReader xr =  sf.newSAXParser().getXMLReader();
			ApplyHandler ah = new ApplyHandler();
			xr.setContentHandler(ah);
			xr.parse(new InputSource(in));
			list=ah.getList();
			
			
//			if(flh.getError().equals("1"))
//			{
//				list=new ArrayList<String[]>();
//			}
//			else
//			{
//			list=flh.getList();	
//			//System.out.println(list.size()+":friends");
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return list;
	}
}
