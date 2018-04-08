package com.search.weather;

import java.io.InputStream;

import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.util.Log;
import android.util.Xml;

/**
 * 采用Pull方式解析返回的xml文档
 * @author Administrator
 *
 */
public class WeatherPullParser {
	
	
	public static List<Weather> getData(InputStream reader){
		
		List<Weather> result = null;
		
		XmlPullParser parser = Xml.newPullParser();
		
		Weather wea = null;
		
		String tagName = null;
		
		try {
			parser.setInput(reader, "UTF-8");

			int eventCode = parser.getEventType(); //返回事件码类型
			
			while(eventCode != XmlPullParser.END_DOCUMENT){
				
				switch(eventCode){
				case XmlPullParser.START_DOCUMENT:
					//初始化
					//Log.v("DocumentStart", "Document");
					result = new ArrayList<Weather>();
					break;
				case XmlPullParser.START_TAG:
					//一个元素的开始
					tagName = parser.getName(); //获取当前标签的名称
					if(tagName.equalsIgnoreCase("forecast")){
						//Log.v("TagStart", "yweather:forecast");
						wea = new Weather();
						wea.setDay(parser.getAttributeValue(null, "day"));
						wea.setDate(parser.getAttributeValue(null, "date"));
						wea.setLow(parser.getAttributeValue(null, "low"));
						wea.setHigh(parser.getAttributeValue(null, "high"));
						wea.setText(parser.getAttributeValue(null, "text"));
						wea.setCode(parser.getAttributeValue(null, "code"));
						result.add(wea);
					}
					break;
					
				}
				eventCode = parser.next(); //解析下一个元素
			}
		} catch (Exception e) {
			Log.e("WeatherPullParser", e.getMessage());
		}
		
		return result;
	}

}
