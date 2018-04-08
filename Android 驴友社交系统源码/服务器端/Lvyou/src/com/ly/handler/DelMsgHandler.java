package com.ly.handler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class DelMsgHandler extends DefaultHandler {
	private String val="";
	private String ueid;
	
	

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
		if(qName.equals("uid")){
			this.ueid=val;
			System.out.println(ueid);
		}
	
		val="";
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
		val+=new String(ch, start, length);
		super.characters(ch, start, length);
	}
}
