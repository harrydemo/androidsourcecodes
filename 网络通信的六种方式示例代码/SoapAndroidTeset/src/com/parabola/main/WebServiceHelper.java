package com.parabola.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

/**
 * ������������У������ĵ�˵����URL��ַ
 * �Ȼ�ȡ֧�ֵ�ʡ����Ϣ
 * �ٸ���ĳһ��ʡ�ݻ�ȡ������Ϣ
 * �ٸ��ݳ�����Ϣ��ȡ�������
 * @author Song Shi Chao
 *
 */
public class WebServiceHelper {
	public static final String TAG = "WebServiceHelper";
	
	// WSDL�ĵ��е������ռ�
	private static final String targetNameSpace = "http://WebXml.com.cn/";
	// WSDL�ĵ��е�URL
	private static final String WSDL = "http://webservice.webxml.com.cn/WebServices/WeatherWebService.asmx?wsdl";	
	// ��Ҫ���õķ�����(��ñ�����Ԥ��Web Services֧�ֵ��ޡ�������ʡ�ݺͳ�����Ϣ)
	private static final String getSupportProvince = "getSupportProvince";
	// ��Ҫ���õķ�����(��ñ�����Ԥ��Web Services֧�ֵĳ�����Ϣ,����ʡ�ݲ�ѯ���м��ϣ�������)
	private static final String getSupportCity = "getSupportCity";
	// ���ݳ��л�������Ʋ�ѯ���δ��������������������ڵ�����ʵ��������������ָ��
	private static final String getWeatherbyCityName = "getWeatherbyCityName";

	/**
	 * �������µ��ĵ����ӿں�����ˡ�������
	 * ����ݣ�������ʡ�ݺͳ�����Ϣ 
	 * @return
	 */
	public List<String> getProvince() {
		List<String> provinces = new ArrayList<String>();
	 
		SoapObject soapObject = new SoapObject(targetNameSpace, getSupportProvince);
		// request.addProperty("����", "����ֵ");���õķ������������ֵ�����ݾ�����Ҫ��ѡ�ɲ�ѡ��
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapObject);
		// envelope.bodyOut=request;
		AndroidHttpTransport httpTranstation = new AndroidHttpTransport(WSDL);
		// ����HttpTransportSE httpTranstation=new HttpTransportSE(WSDL);
		
		try {
			httpTranstation.call(targetNameSpace + getSupportProvince, envelope);
			Log.i(TAG, targetNameSpace + getSupportProvince);
			
			SoapObject result = (SoapObject) envelope.getResponse();
			// ����Խ�����н������ṹ����json����
			Log.i(TAG, ""+result.getPropertyCount());
			int count = result.getPropertyCount();
			for (int index = 0; index < count; index++) {
				provinces.add(result.getProperty(index).toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		
		return provinces;
	}

	/**
	 * ����ʡ�ݻ���ֱϽ�л�ȡ����Ԥ����֧�ֵĳ��м���
	 * �������ɹ�-->������С����С���ͷ�ȡ������� 
	 * @param province 
	 * @return
	 */
	public List<String> getCitys(String province) {
		List<String> citys = new ArrayList<String>();
		
		SoapObject soapObject = new SoapObject(targetNameSpace, getSupportCity);
		soapObject.addProperty("byProvinceName", province);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapObject);
		AndroidHttpTransport httpTransport = new AndroidHttpTransport(WSDL);
		
		try {
			httpTransport.call(targetNameSpace + getSupportCity, envelope);
			SoapObject result = (SoapObject) envelope.getResponse();
			int count = result.getPropertyCount();
			for (int index = 0; index < count; index++) {
				citys.add(result.getProperty(index).toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		return citys;
	}

	/**
	 * ���ݳ�����Ϣ��ȡ����Ԥ����Ϣ
	 * @param city
	 * @return
	 **/
	@SuppressWarnings("deprecation")
	public WeatherBean getWeatherByCity(String city) {
		 
		WeatherBean bean = new WeatherBean();
		
		SoapObject soapObject = new SoapObject(targetNameSpace, getWeatherbyCityName);
		city = city.substring(0,city.length()-7);
		Log.i(TAG, "city="+city); 
		soapObject.addProperty("theCityName", city);   // ���õķ������������ֵ�����ݾ�����Ҫ��ѡ�ɲ�ѡ��
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapObject);
		envelope.bodyOut = soapObject;
		
		AndroidHttpTransport httpTranstation = new AndroidHttpTransport(WSDL);
		// ����HttpTransportSE httpTranstation=new HttpTransportSE(WSDL);
		httpTranstation.debug = true;
		
		try {
			httpTranstation.call(targetNameSpace + getWeatherbyCityName, envelope);
			
			SoapObject result = (SoapObject) envelope.getResponse();
			if(result != null){
				Log.i(TAG, "result = " + result);
				
				// ����Խ�����н������ṹ����json����
				bean = parserWeather(result);
				
			}else{
				Log.i(TAG, "result = null");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		 
		return bean;
	}
	
	/**
	 * �����ã����ݳ������ƻ�ȡ�������
	 * @param city
	 */
	public void getWeatherByCityTest(String city){
		try {
			SoapObject msg = new SoapObject(
					"http://WebXml.com.cn/",
					"getWeatherbyCityName");
			
			city = city.substring(0,city.length()-7);
			Log.i(TAG, "city="+city); 
			msg.addProperty("theCityName", city);
			 
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.bodyOut = msg;
			envelope.dotNet = true;

			AndroidHttpTransport sendRequest = new AndroidHttpTransport(
					"http://www.webxml.com.cn/webservices/weatherwebservice.asmx");
			envelope.setOutputSoapObject(msg);
			sendRequest
					.call("http://WebXml.com.cn/getWeatherbyCityName",
							envelope);

			SoapObject result = (SoapObject) envelope.bodyIn;
			SoapObject detail = (SoapObject) result
					.getProperty("getWeatherbyCityNameResult");
			System.out.println(detail + "");

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * ������
	 * @param detail
	 */
	private void parseWeather(SoapObject detail) {
		String weatherToday = null,weatherTomorrow = null,weatherAfterday = null;
		int iconToday[] = null,iconTomorrow[] = null,iconAfterday[] = null;
//		String date = detail.getProperty(6).toString();
//		String weatherToday = "���죺" + date.split(" ")[0];
//		Log.i(TAG, weatherToday);
//		weatherToday = weatherToday + "\n������" + date.split(" ")[1];
		weatherToday = weatherToday + "\n���£�" + detail.getProperty(5).toString();
		weatherToday = weatherToday + "\n������" + detail.getProperty(7).toString() + "\n";
		Log.i(TAG, weatherToday);
		iconToday[0] = parseIcon(detail.getProperty(8).toString());
		iconToday[1] = parseIcon(detail.getProperty(9).toString());
		String weatherCurrent = detail.getProperty(10).toString();
		
//		date = detail.getProperty(13).toString();
//		String weatherTomorrow = "���죺" + date.split(" ")[0];
//		weatherTomorrow = weatherTomorrow + "\n������" + date.split(" ")[1];
		weatherTomorrow = weatherTomorrow + "\n���£�" + detail.getProperty(12).toString();
		weatherTomorrow = weatherTomorrow + "\n������" + detail.getProperty(14).toString() + "\n";
		iconTomorrow[0] = parseIcon(detail.getProperty(15).toString());
		iconTomorrow[1] = parseIcon(detail.getProperty(16).toString());
		
//		date = detail.getProperty(18).toString();
//		String weatherAfterday = "���죺" + date.split(" ")[0];
//		weatherAfterday = weatherAfterday + "\n������" + date.split(" ")[1];
		weatherAfterday = weatherAfterday + "\n���£�" + detail.getProperty(17).toString();
		weatherAfterday = weatherAfterday + "\n������" + detail.getProperty(19).toString() + "\n";
		iconAfterday[0] = parseIcon(detail.getProperty(20).toString());
		iconAfterday[1] = parseIcon(detail.getProperty(21).toString());
		} 


	/**
	 * �������صĽ�� 
	 * @param soapObject
	 **/
	protected WeatherBean parserWeather(SoapObject soapObject) {
		WeatherBean bean = new WeatherBean();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		List<Integer> icons = new ArrayList<Integer>();
		
		// ������
		bean.setCityName(soapObject.getProperty(1).toString());
		// ���м��
		bean.setCityDescription(soapObject.getProperty(
				soapObject.getPropertyCount() - 1).toString());
		// ����ʵ��+����
		bean.setLiveWeather(soapObject.getProperty(10).toString() + "\n"
				+ soapObject.getProperty(11).toString());
		// ��������
		//���ڣ�
		String date = soapObject.getProperty(6).toString();
		
		Log.i(TAG, "");
		// ---------------------------------------------------
		String weatherToday = "���죺" + date.split(" ")[0];
		weatherToday += "\n������" + date.split(" ")[1];
		weatherToday += "\n���£�" + soapObject.getProperty(5).toString();
		weatherToday += "\n������" + soapObject.getProperty(7).toString();
		weatherToday += "\n";
 
		icons.add(parseIcon(soapObject.getProperty(8).toString()));
		icons.add(parseIcon(soapObject.getProperty(9).toString()));
		map.put("weatherDay", weatherToday);
		map.put("icons", icons);
		list.add(map);
		
		// -------------------------------------------------
		map = new HashMap<String, Object>();
		date = soapObject.getProperty(13).toString();
		String weatherTomorrow = "���죺" + date.split(" ")[0];
		weatherTomorrow += "\n������" + date.split(" ")[1];
		weatherTomorrow += "\n���£�" + soapObject.getProperty(12).toString();
		weatherTomorrow += "\n������" + soapObject.getProperty(14).toString();
		weatherTomorrow += "\n";
		icons = new ArrayList<Integer>();
		icons.add(parseIcon(soapObject.getProperty(15).toString()));
		icons.add(parseIcon(soapObject.getProperty(16).toString()));
		map.put("weatherDay", weatherTomorrow);
		map.put("icons", icons);
		list.add(map);
		
		// --------------------------------------------------------------
		map = new HashMap<String, Object>();
		date = soapObject.getProperty(18).toString();
		String weatherAfterTomorrow = "���죺" + date.split(" ")[0];
		weatherAfterTomorrow += "\n������" + date.split(" ")[1];
		weatherAfterTomorrow += "\n���£�" + soapObject.getProperty(17).toString();
		weatherAfterTomorrow += "\n������" + soapObject.getProperty(19).toString();
		weatherAfterTomorrow += "\n";
		icons = new ArrayList<Integer>();
		icons.add(parseIcon(soapObject.getProperty(20).toString()));
		icons.add(parseIcon(soapObject.getProperty(21).toString()));
		map.put("weatherDay", weatherAfterTomorrow);
		map.put("icons", icons);
		list.add(map);
		
		// �ѽ���������ݷŵ�һ��list��
		bean.setList(list); 
		return bean;
	}

	// ����ͼ���ַ���
	private int parseIcon(String data) {
		// 0.gif����������0,
		int resID = 32;
		String result = data.substring(0, data.length() - 4).trim();
		// String []icon=data.split(".");
		// String result=icon[0].trim();
		// Log.e("this is the icon", result.trim());
		if (!result.equals("nothing")) {
			resID = Integer.parseInt(result.trim());
		}
		return resID;
		// return ("a_"+data).split(".")[0];
	}
}
