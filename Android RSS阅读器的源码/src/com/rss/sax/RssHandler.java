package com.rss.sax;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;
import org.xml.sax.helpers.DefaultHandler;

import com.rss.data.RssFeed;
import com.rss.data.RssItem;

public class RssHandler extends DefaultHandler2 {
	RssFeed rssFeed;
	RssItem rssItem;
	private static boolean a = false;
	String lastElementName = "";
	final int RSS_TITLE = 1;
	final int RSS_LINK = 2;
	final int RSS_DESCRIPTION = 3;
	final int RSS_PUBDATE = 5;
	int currentstate = 0;
	
	public RssHandler() {
		
	}
	
	public RssFeed getFeed() {
		return rssFeed;
	}
	
	public void startDocument()throws SAXException {
		System.out.println("startDocument");
		rssFeed = new RssFeed();
		rssItem = new RssItem();
	}

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if(localName.equals("channel")) {
			currentstate = 0;
			return;
		}
		
		if(localName.equals("item")) {
			rssItem = new RssItem();
			return;
		}
		
		if(localName.equals("title")) {
			currentstate = RSS_TITLE;
			return;
		}
		if(localName.equals("description")) {	
			if(a == true) {
				currentstate = RSS_DESCRIPTION;
				return;
			} else {
				a = true;
				return;
			}
		}
		if(localName.equals("link")) {
			currentstate = RSS_LINK;
			return;
		}
		if(localName.equals("pubDate")) {
			currentstate = RSS_PUBDATE;
			return;
		}
		currentstate = 0;
	}

	@Override
	public void endElement(String uri, String localName, String qName)	
			throws SAXException {
		
		if(localName.equals("item")) {
			rssFeed.addItem(rssItem);
			return;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if(length <= 5) return ;
		String theString = new String(ch, start, length);
		switch (currentstate) {
		case RSS_TITLE:
			rssItem.setTitle(theString);
			currentstate = 0;
			break;
		case RSS_LINK:
			rssItem.setLink(theString);
			currentstate = 0;
			break;
		case RSS_DESCRIPTION:
			System.out.println("RSS_DESCRIPTION=" + theString);
		
			if(a == true) {
				rssItem.setDescription(theString);
				currentstate = 0;
			}else {			
				a = true;
			}
			break;
		case RSS_PUBDATE:
			rssItem.setPubDate(theString);
			currentstate = 0;
			break;
		default:
			return;
		}

	}
}
