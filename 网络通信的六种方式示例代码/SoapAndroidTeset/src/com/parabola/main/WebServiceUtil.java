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
 * �����ʹ����һ�ַ�ʽ��ȡ��Ϣ����Ҫ����д����ķ�ʽ��ͬ
 * @author Song Shi Chao
 *
 */
public class WebServiceUtil {
	// �����ռ�
	private static final String serviceNameSpace = "http://WebXml.com.cn/";
	// ����URL
	private static final String serviceURL = "http://www.webxml.com.cn/webservices/weatherwebservice.asmx";
	// ���÷���(���֧�ֵĳ���)
	private static final String getSupportCity = "getSupportCity";
	// ���ó��еķ���(��Ҫ������)
	private static final String getWeatherbyCityName = "getWeatherbyCityName";
	// ����ʡ����ֱϽ�еķ���(���֧�ֵ�ʡ�ݻ�ֱϽ��)
	private static final String getSupportProvince = "getSupportProvince";

	/**
	 * @return�����б� 
	 */
	public static List<String> getCityList() {
		// ʵ����SoapObject����
		SoapObject request = new SoapObject(serviceNameSpace, getSupportCity);
		// ������л���Envelope
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.bodyOut = request;
		(new MarshalBase64()).register(envelope);
		// Android�������
		AndroidHttpTransport transport = new AndroidHttpTransport(serviceURL);
		transport.debug = true;
		// ����
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
		// ʵ����SoapObject����
		SoapObject request = new SoapObject(serviceNameSpace,
				getSupportProvince);
		// ������л���Envelope
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.bodyOut = request;
		(new MarshalBase64()).register(envelope);
		// Android�������
		AndroidHttpTransport transport = new AndroidHttpTransport(serviceURL);
		transport.debug = true;
		// ����
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
	 * ����XML
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
	 * ��ȡ����
	 * @param soapObject
	 **/
	private void parseWeather(SoapObject soapObject) {
		// String date=soapObject.getProperty(6);
	}
}
