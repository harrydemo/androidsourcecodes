package org.music.tools;

import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 解析远程服务器的音乐XML
 */
public class Mp3ListContentHandler extends DefaultHandler {
	private List<Mp3Info> mp3infos = null;
	private Mp3Info mp3info = null;
	private String tagname;

	public List<Mp3Info> getMp3infos() {
		return mp3infos;
	}

	public void setMp3infos(List<Mp3Info> mp3infos) {
		this.mp3infos = mp3infos;
	}

	public Mp3ListContentHandler(List<Mp3Info> mp3Infos) {
		this.mp3infos = mp3Infos;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String temp = new String(ch, start, length);
		if (tagname.equals("id")) {
			mp3info.setAudio_id(temp);
		} else if (tagname.equals("mp3name")) {
			mp3info.setMp3Name(temp);
		} else if (tagname.equals("artists")) {
			mp3info.setArtist(temp);
		} else if (tagname.equals("lrcname")) {
			mp3info.setLrcName(temp);
		}

	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (qName.equals("resource")) {
			mp3infos.add(mp3info);
		}
		tagname = "";
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		this.tagname = localName;
		if (tagname.equals("resource")) {
			mp3info = new Mp3Info();
		}
	}

}
