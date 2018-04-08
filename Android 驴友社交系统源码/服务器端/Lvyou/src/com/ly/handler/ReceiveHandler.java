package com.ly.handler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ReceiveHandler extends DefaultHandler {
	private String val ="";
	private String count;
@Override
public void startElement(String arg0, String arg1, String arg2,
		Attributes arg3) throws SAXException {
	// TODO Auto-generated method stub
	super.startElement(arg0, arg1, arg2, arg3);
}
@Override
	public void endElement(String arg0, String arg1, String arg2)
			throws SAXException {
		// TODO Auto-generated method stub
	if(arg2.equals("flag")){
		this.count=val;
		
	}
	val="";
		super.endElement(arg0, arg1, arg2);
	}
public String getCount() {
	return count;
}
public void setCount(String count) {
	this.count = count;
}
@Override
	public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
		// TODO Auto-generated method stub
	val+=new String(arg0,arg1,arg2);
		super.characters(arg0, arg1, arg2);
	}
}
