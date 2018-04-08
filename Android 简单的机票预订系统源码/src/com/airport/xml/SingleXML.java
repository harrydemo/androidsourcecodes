package com.airport.xml;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.airport.bean.SingleBean;

public class SingleXML extends DefaultHandler {
	 List<SingleBean> singles=new ArrayList<SingleBean>();
	 SingleBean singbean;
	StringBuffer buffer = new StringBuffer();

	public List<SingleBean> getSingles() {
		return singles;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		buffer.append(ch, start, length);
		super.characters(ch, start, length);
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (localName.equals("ticket")) {
			singles.add(singbean);
		}else if (localName.equals("key")) {
			singbean.setKey(buffer.toString().trim());
			buffer.setLength(0);
		}else if (localName.equals("discount")) {
			singbean.setDisaccount(buffer.toString().trim());
			buffer.setLength(0);
		}else if (localName.equals("price")) {
			singbean.setPrice(Integer.parseInt(buffer.toString().trim()));
			buffer.setLength(0);
		}else if (localName.equals("Value")) {
			singbean.setValue(buffer.toString().trim());
			buffer.setLength(0);
		}else if (localName.equals("leavecity")) {
			singbean.setLeaveCity(buffer.toString().trim());
			buffer.setLength(0);
		}else if (localName.equals("arrivecity")) {
			singbean.setArriveCity(buffer.toString().trim());
			buffer.setLength(0);
		}else if (localName.equals("leavetime")) {
			singbean.setLeavetime(buffer.toString().trim());
			buffer.setLength(0);
		}else if (localName.equals("endtime")) {
			singbean.setEndtime((buffer.toString().trim()));	
			buffer.setLength(0);
			
		}
		super.endElement(uri, localName, qName);
	}

	 

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (localName.equals("key")) {
			singbean=new SingleBean();
		}
	}

}
