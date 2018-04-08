package com.ly.handler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class FriendsMeHandler extends DefaultHandler {
	private String val="";
	private String ueid;
	private String count;
	
public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
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
	if(qName.equals("ueid")){
		this.ueid=val;
	}
	if(qName.equals("count")){
		this.count=val;
	}
	val = "";
		super.endElement(uri, localName, qName);
	}

public String getUeid() {
	return ueid;
}
public void setUeid(String ueid) {
	this.ueid = ueid;
}
@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
	val+= new String(ch,start,length);
		super.characters(ch, start, length);
	}
}
