package com.ly.handler;

import java.io.FileOutputStream;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import sun.misc.BASE64Decoder;

import com.ly.vo.UserVO;

public class LYRegisterHandler extends DefaultHandler {
	private String val="";
	private String url;
	private UserVO uv;
	public  LYRegisterHandler(String url){
		this.url=url;
	}
	
	public UserVO getUv() {
		return uv;
	}

	public void setUv(UserVO uv) {
		this.uv = uv;
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		if(qName.equals("file")){
			uv = new UserVO();
		}
		super.startElement(uri, localName, qName, attributes);
	}
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		if(qName.equals("username")){
			uv.setU_username(val);
		}
		if(qName.equals("password")){
			uv.setU_pwd(val);
		}
		if(qName.equals("email")){
			uv.setU_email(val);
		}
		if(qName.equals("name")){
			uv.setU_name(val);
		}
		if(qName.equals("sex")){
			uv.setU_sex(val);
		}
		if(qName.equals("phone")){
			uv.setU_phone(val);
		}
		if(qName.equals("sex")){
			
		}
		if(qName.equals("headname")){
			uv.setU_headname(val);
			
		}
		if(qName.equals("head")){
		BASE64Decoder bd = new BASE64Decoder();
		try{
		byte image[] = bd.decodeBuffer(val);
		FileOutputStream out = new FileOutputStream(url+"/"+uv.getU_headname());
		out.write(image);
		out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}	
		if(qName.equals("job")){
			uv.setU_job(val);
		}
		if(qName.equals("address")){
			uv.setU_address(val);
		}
		if(qName.equals("circle")){
			uv.setU_circle(val);
		}
		if(qName.equals("guanzhu")){
			uv.setU_gz(val);
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
