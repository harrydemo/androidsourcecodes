package com.ly.handler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class LYAddMsgHandler extends DefaultHandler {
private String uid;
private String ueid;
private String time;
private String content;
private String val="";	
	public String getUid() {
	return uid;
}
public void setUid(String uid) {
	this.uid = uid;
}
public String getUeid() {
	return ueid;
}
public void setUeid(String ueid) {
	this.ueid = ueid;
}
public String getTime() {
	return time;
}
public void setTime(String time) {
	this.time = time;
}
public String getContent() {
	return content;
}
public void setContent(String content) {
	this.content = content;
}



//	 @Override
//	public void startDocument() throws SAXException {
//		// TODO Auto-generated method stub
//		super.startDocument();
//	}
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub

		super.startElement(uri, localName, qName, attributes);
	}
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		if(qName.equals("hostid")){
			this.uid=val;
			
		}
		if(qName.equals("otherid")){
			this.ueid=val;
			
		}
		if(qName.equals("time")){
			this.time=val;
			
		}
		if(qName.equals("content")){
			this.content=val;
			
		}
		
		
		
		
	val="";
		super.endElement(uri, localName, qName);
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		
		val+= new String(ch,start,length);
		super.characters(ch, start, length);
		
	}
}
