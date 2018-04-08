package cn.itcast.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import cn.itcast.utils.StreamTool;

public class MobileService {
	
	public static String getMobileAddress(String mobile) throws Exception{
		InputStream inStream = MobileService.class.getClassLoader().getResourceAsStream("mobilesoap.xml");
		byte[] data = StreamTool.readInputStream(inStream);		
		String xml = new String(data);
		String soap = xml.replaceAll("\\$mobile", mobile);
		
		String path = "http://webservice.webxml.com.cn/WebServices/MobileCodeWS.asmx";		
		data = soap.getBytes();//得到了xml的实体数据
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setConnectTimeout(5 *1000);
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8");
		conn.setRequestProperty("Content-Length", String.valueOf(data.length));
		OutputStream outStream = conn.getOutputStream();
		outStream.write(data);
		outStream.flush();
		outStream.close();
		if(conn.getResponseCode()==200){
			InputStream responseStream = conn.getInputStream();		
			return parseXML(responseStream);
		}
		return null;
	}
	/**
	 * 解析返回xml数据
	 * @param responseStream
	 * @return
	 * @throws Exception
	 */
	private static String parseXML(InputStream responseStream) throws Exception {
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(responseStream, "UTF-8");
		int event = parser.getEventType();
		while(event!=XmlPullParser.END_DOCUMENT){
			switch (event) {
			case XmlPullParser.START_TAG:
				if("getMobileCodeInfoResult".equals(parser.getName())){
					return parser.nextText();
				}
				break;
			}
			event = parser.next();
		}
		return null;
	}
}
