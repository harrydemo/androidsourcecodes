package com.search.ip;

import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;

import android.util.Log;
import android.util.Xml;

public class IpPullParser {
	
	public static Ip getData(InputStream is){
		
		XmlPullParser parser = Xml.newPullParser();
		Ip ip = null;
		
		try {
			parser.setInput(is, "UTF-8");
			int eventCode = parser.getEventType();
			
			while(eventCode != XmlPullParser.END_DOCUMENT){
				switch(eventCode){
				case XmlPullParser.START_TAG:
					String tagName = parser.getName();
					if(tagName.equalsIgnoreCase("product")){
						ip = new Ip();
					}
					else if(ip != null){
						if(tagName.equalsIgnoreCase("ip")){
							ip.setIp(parser.nextText());
						}else if(tagName.equalsIgnoreCase("location")){
							ip.setLocation(parser.nextText());
						}
					}
					break;
				}
				
				eventCode = parser.next();
				
			}
			Log.v("XML ::", ip.toString());
			return ip;
			
		} catch (Exception e) {
			
			Log.e("WeatherPullParser", e.getMessage());
			return null;
		}
		
		
	}

}
