  package com.ly.bean;

import java.io.InputStream;

import java.util.ArrayList;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.ly.handler.AttentionHandler;
import com.ly.handler.FriendListHandler;

public class AttentionBean {
public ArrayList<String[]> attention(InputStream in){
		
		
		SAXParserFactory sf = SAXParserFactory.newInstance();
		ArrayList<String[]> lista=null;
		try {
			XMLReader xr =  sf.newSAXParser().getXMLReader();
			AttentionHandler atb = new AttentionHandler();
			
			xr.setContentHandler(atb);
			
			xr.parse(new InputSource(in));
			lista=atb.getList();
//			if(atb.getError()!=null)
//			{
//				lista=null;
//				//System.out.println("error:((()))");
//			}
//			else
//			{
//			lista=atb.getList();	
//			//System.out.println(lista.size()+":attentions");
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return lista;
	}
}
