package com.ly.handler;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ShowRegisterHandler extends DefaultHandler {
	private String error;
	//private String []loginfo;
	private String val="";
	//private ArrayList<String[]> list = new ArrayList<String[]>();
	private String pic,uname,email,sex,phone,job,address,circle,gz;
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCircle() {
		return circle;
	}
	public void setCircle(String circle) {
		this.circle = circle;
	}
	public String getGz() {
		return gz;
	}
	public void setGz(String gz) {
		this.gz = gz;
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
		if(qName.equals("pic")){
			this.pic=val;
			
		}
		if(qName.equals("uname")){
			this.uname=val;
		}
		if(qName.equals("email")){
			this.email=val;
		}
		if(qName.equals("sex")){
			this.sex=val;
		}
		if(qName.equals("phone")){
			this.phone=val;
		}
		if(qName.equals("job")){
			this.job=val;
		}
		if(qName.equals("address")){
			this.address=val;
		}
		if(qName.equals("circle")){
			this.circle=val;
		}
		if(qName.equals("gz")){
			this.gz=val;
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
