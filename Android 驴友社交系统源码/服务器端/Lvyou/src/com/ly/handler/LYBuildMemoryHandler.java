package com.ly.handler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class LYBuildMemoryHandler extends DefaultHandler {
	private String val="";
	private String uid,title,address,content,time,tagtitle,tagtype,tagcontent;
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getTagtitle() {
		return tagtitle;
	}
	public void setTagtitle(String tagtitle) {
		this.tagtitle = tagtitle;
	}
	public String getTagtype() {
		return tagtype;
	}
	public void setTagtype(String tagtype) {
		this.tagtype = tagtype;
	}
	public String getTagcontent() {
		return tagcontent;
	}
	public void setTagcontent(String tagcontent) {
		this.tagcontent = tagcontent;
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
		if(qName.equals("uid")){
			this.uid=val;
		}
		if(qName.equals("title")){
			this.title=val;
		}
		if(qName.equals("address")){
			this.address=val;
		}
		if(qName.equals("content")){
			this.content=val;
			
		}
		if(qName.equals("time")){
			this.time=val;
			
		}
		if(qName.equals("tagtitle")){
			this.tagtitle=val;
			
		}
		if(qName.equals("tagtype")){
			this.tagtype=val;
			
		}
		if(qName.equals("tagcontent")){
			this.tagcontent=val;
			
		}
		val="";
		super.endElement(uri, localName, qName);
	}
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		val+=new String(ch, start, length);
		super.characters(ch, start, length);
	}
}
