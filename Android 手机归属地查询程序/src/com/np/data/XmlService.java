package com.np.data;

import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;


import android.util.Xml;

public class XmlService {
	
	public static Info loadViewInfos(InputStream inputStream) throws XmlPullParserException, IOException{
		Info info = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(inputStream, "UTF-8");
		int event = parser.getEventType();
		while(event!=XmlPullParser.END_DOCUMENT){
			switch (event){
			case XmlPullParser.START_DOCUMENT:
				info = new Info();
				break;
			case XmlPullParser.START_TAG:
				if(info!=null){
					if("Mobile".equals(parser.getName())){
						info.setMobile(parser.nextText());					
					}else if("QueryResult".equals(parser.getName())){
						info.setQueryResult(parser.nextText());					
					}else if("Province".equals(parser.getName())){
						info.setProvince(parser.nextText());					
					}else if("City".equals(parser.getName())){
						info.setCity(parser.nextText());					
					}else if("AreaCode".equals(parser.getName())){
						info.setAreaCode(parser.nextText());					
					}else if("PostCode".equals(parser.getName())){
						info.setPostCode(parser.nextText());				
					}else if("Corp".equals(parser.getName())){
						info.setCorp(parser.nextText());					
					}else if("Corp".equals(parser.getName())){
						info.setCorp(parser.nextText());					
					}else if("Card".equals(parser.getName())){
						info.setCard(parser.nextText());					
					}
				}
				break;
			case XmlPullParser.END_TAG:
				break;
			}
			event = parser.next();
		}
		return info;
	}
	
}
