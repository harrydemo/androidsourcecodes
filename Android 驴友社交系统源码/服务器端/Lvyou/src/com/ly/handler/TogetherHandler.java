package com.ly.handler;

import org.xml.sax.Attributes;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;





public class TogetherHandler extends DefaultHandler {
	
	
	private String val="";
	
	private String url;
	private String content,id,time,title,gtime;
	
	public String getGtime() {
		return gtime;
	}
	public void setGtime(String gtime) {
		this.gtime = gtime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
		if(qName.equals("id"))
		{
			this.id = val;
			
			
		}
		if(qName.equals("title"))
		{
			this.title = val;
		
		}
		
		if(qName.equals("content"))
		{
			this.content = val;
		
		}

		if(qName.equals("time"))
		{
			this.time = val;
		
		}
		if(qName.equals("gtime"))
		{
			this.gtime = val;
		
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
