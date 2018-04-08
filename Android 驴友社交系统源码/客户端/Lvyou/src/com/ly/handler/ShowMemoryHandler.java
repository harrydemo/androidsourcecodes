package com.ly.handler;

import java.util.ArrayList;


import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ShowMemoryHandler extends DefaultHandler {
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
		if(qName.equals("memory")){
			loginfo=new String[11];
		}
		super.startElement(uri, localName, qName, attributes);
	}
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		if(qName.equals("uname")){
			loginfo[0]=val;
			
		}
		if(qName.equals("pic")){
			loginfo[1]=val;
		}
		if(qName.equals("title")){
			loginfo[2]=val;
		}
		if(qName.equals("address")){
			loginfo[3]=val;
		}
		if(qName.equals("content")){
			loginfo[4]=val;
			}
		if(qName.equals("mid")){
			loginfo[5]=val;
			}
		if(qName.equals("uid")){
			loginfo[6]=val;
			}
		if(qName.equals("time")){
			loginfo[7]=val;
			
			}
		if(qName.equals("tagtitle")){
			loginfo[8]=val;
			
			}
		if(qName.equals("tagtype")){
			loginfo[9]=val;
			
			}
		if(qName.equals("tagcontent")){
			loginfo[10]=val;
			
			}
		if(qName.equals("memory")){
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
