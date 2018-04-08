package com.ly.handler;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ShowReplyHandler extends DefaultHandler {
	private String error;
	private String []loginfo;
	private String val="";
	private ArrayList<String[]> list = new ArrayList<String[]>();
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String[] getLoginfo() {
		return loginfo;
	}
	public void setLoginfo(String[] loginfo) {
		this.loginfo = loginfo;
	}
	public ArrayList<String[]> getList() {
		return list;
	}
	public void setList(ArrayList<String[]> list) {
		this.list = list;
	}
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		if(qName.equals("reply")){
			loginfo=new String[4];
		}
		super.startElement(uri, localName, qName, attributes);
	}
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		if(qName.equals("content")){
			loginfo[0]=val;
			
		}
		if(qName.equals("mrid")){
			loginfo[1]=val;
		}
		if(qName.equals("mid")){
			loginfo[2]=val;
		}
		if(qName.equals("name")){
			loginfo[3]=val;
		}
		
		if(qName.equals("reply")){
			list.add(loginfo);
		}
		if(qName.equals("error")){
		this.error=error;
		}
		val="";
		super.endElement(uri, localName, qName);
	}
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		val+=new String(ch,start,length);
		super.characters(ch, start, length);
	}
}
