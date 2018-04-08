package com.androidmediaplayer.utils;

import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.androidmediaplayer.model.Mp3Info;


public class XML_Mp3ListContentHandler extends DefaultHandler {

	private List<Mp3Info> mp3Infos = null;
	private Mp3Info mp3Info = null;
	private String tagName = null;
	
	public XML_Mp3ListContentHandler(List<Mp3Info> mp3Infos) {
		this.mp3Infos = mp3Infos;
	}

	public List<Mp3Info> getMp3Infos() {
		return mp3Infos;
	}

	public void setMp3Infos(List<Mp3Info> mp3Infos) {
		this.mp3Infos = mp3Infos;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String temp = new String(ch,start,length);
		if(tagName.equals("filename.id")){
			mp3Info.setAudio_id(temp);
		}else if(tagName.equals("mp3.name")){
			mp3Info.setMp3Name(temp);
		}else if(tagName.equals("mp3.size")){
			mp3Info.setMp3Size(temp);
		}else if(tagName.equals("lrc.name")){
			mp3Info.setLrcName(temp);
		}else if(tagName.equals("mp3.artist")){
			mp3Info.setArtist(temp);
		}
	}

	@Override
	public void endDocument() throws SAXException {
		
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if(qName.equals("resource")){
			mp3Infos.add(mp3Info);
		}
		tagName = "";
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		this.tagName = localName;
		if(tagName.equals("resource")){
			this.mp3Info = new Mp3Info();
		}
	}

}
