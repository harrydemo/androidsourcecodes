package com.parabola.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;
import org.xmlpull.v1.XmlPullParserException;

/**
 * 这个是使用另一种方式获取信息，主要是书写代码的方式不同
 * @author Song Shi Chao
 *
 */
public class WebServiceUtil {
	// 命名空间
	private static final String serviceNameSpace = "http://WebXml.com.cn/";
	// 请求URL
	private static final String serviceURL = "http://www.webxml.com.cn/webservices/weatherwebservice.asmx";
	// 调用方法(获得支持的城市)
	private static final String getSupportCity = "getSupportCity";
	// 调用城市的方法(需要带参数)
	private static final String getWeatherbyCityName = "getWeatherbyCityName";
	// 调用省或者直辖市的方法(获得支持的省份或直辖市)
	private static final String getSupportProvince = "getSupportProvince";

	/**
	 * @return城市列表 
	 */
	public static List<String> getCityList() {
		// 实例化SoapObject对象
		SoapObject request = new SoapObject(serviceNameSpace, getSupportCity);
		// 获得序列化的Envelope
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.bodyOut = request;
		(new MarshalBase64()).register(envelope);
		// Android传输对象
		AndroidHttpTransport transport = new AndroidHttpTransport(serviceURL);
		transport.debug = true;
		// 调用
		try {
			transport.call(serviceNameSpace + getWeatherbyCityName, envelope);
			if (envelope.getResponse() != null) {
				return parse(envelope.bodyIn.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<String> getProviceList() {
		// 实例化SoapObject对象
		SoapObject request = new SoapObject(serviceNameSpace,
				getSupportProvince);
		// 获得序列化的Envelope
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.bodyOut = request;
		(new MarshalBase64()).register(envelope);
		// Android传输对象
		AndroidHttpTransport transport = new AndroidHttpTransport(serviceURL);
		transport.debug = true;
		// 调用
		try {
			transport.call(serviceNameSpace + getWeatherbyCityName, envelope);
			
			if (envelope.getResponse() != null) {
				return null;
			}
		} catch (IOException e) { 
			e.printStackTrace();
		} catch (XmlPullParserException e) { 
			e.printStackTrace();
		}
		return null;
	}
 
	/**
	 * @param cityName
	 * @return
	 */
	public static String getWeather(String cityName) {
		return "";
	}

	/**
	 * 解析XML
	 */
	private static List<String> parse(String str) {
		String temp;
		List<String> list = new ArrayList<String>();
		if (str != null && str.length() > 0) {
			int start = str.indexOf("string");
			int end = str.lastIndexOf(";");
			temp = str.substring(start, end - 3);
			String[] test = temp.split(";");
			for (int i = 0; i < test.length; i++) {
				if (i == 0) {
					temp = test[i].substring(7);
				} else {
					temp = test[i].substring(8);
				}
				int index = temp.indexOf(",");
				list.add(temp.substring(0, index));
			}
		}
		return list;
	}

	/**
	 * 获取天气
	 * @param soapObject
	 **/
	private void parseWeather(SoapObject soapObject) {
		// String date=soapObject.getProperty(6);
	}
}
