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
 * 在这个帮助类中，根据文档说明和URL地址
 * 先获取支持的省份信息
 * 再根据某一个省份获取城市信息
 * 再根据城市信息获取天气情况
 * @author Song Shi Chao
 *
 */
public class WebServiceHelper {
	public static final String TAG = "WebServiceHelper";
	
	// WSDL文档中的命名空间
	private static final String targetNameSpace = "http://WebXml.com.cn/";
	// WSDL文档中的URL
	private static final String WSDL = "http://webservice.webxml.com.cn/WebServices/WeatherWebService.asmx?wsdl";	
	// 需要调用的方法名(获得本天气预报Web Services支持的洲、国内外省份和城市信息)
	private static final String getSupportProvince = "getSupportProvince";
	// 需要调用的方法名(获得本天气预报Web Services支持的城市信息,根据省份查询城市集合：带参数)
	private static final String getSupportCity = "getSupportCity";
	// 根据城市或地区名称查询获得未来三天内天气情况、现在的天气实况、天气和生活指数
	private static final String getWeatherbyCityName = "getWeatherbyCityName";

	/**
	 * 根据最新的文档，接口好像变了。。。囧
	 * 获得州，国内外省份和城市信息 
	 * @return
	 */
	public List<String> getProvince() {
		List<String> provinces = new ArrayList<String>();
	 
		SoapObject soapObject = new SoapObject(targetNameSpace, getSupportProvince);
		// request.addProperty("参数", "参数值");调用的方法参数与参数值（根据具体需要可选可不选）
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapObject);
		// envelope.bodyOut=request;
		AndroidHttpTransport httpTranstation = new AndroidHttpTransport(WSDL);
		// 或者HttpTransportSE httpTranstation=new HttpTransportSE(WSDL);
		
		try {
			httpTranstation.call(targetNameSpace + getSupportProvince, envelope);
			Log.i(TAG, targetNameSpace + getSupportProvince);
			
			SoapObject result = (SoapObject) envelope.getResponse();
			// 下面对结果进行解析，结构类似json对象
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
	 * 根据省份或者直辖市获取天气预报所支持的城市集合
	 * 比如内蒙古-->【赤峰市、呼市、包头等。。。】 
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
	 * 根据城市信息获取天气预报信息
	 * @param city
	 * @return
	 **/
	@SuppressWarnings("deprecation")
	public WeatherBean getWeatherByCity(String city) {
		 
		WeatherBean bean = new WeatherBean();
		
		SoapObject soapObject = new SoapObject(targetNameSpace, getWeatherbyCityName);
		city = city.substring(0,city.length()-7);
		Log.i(TAG, "city="+city); 
		soapObject.addProperty("theCityName", city);   // 调用的方法参数与参数值（根据具体需要可选可不选）
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapObject);
		envelope.bodyOut = soapObject;
		
		AndroidHttpTransport httpTranstation = new AndroidHttpTransport(WSDL);
		// 或者HttpTransportSE httpTranstation=new HttpTransportSE(WSDL);
		httpTranstation.debug = true;
		
		try {
			httpTranstation.call(targetNameSpace + getWeatherbyCityName, envelope);
			
			SoapObject result = (SoapObject) envelope.getResponse();
			if(result != null){
				Log.i(TAG, "result = " + result);
				
				// 下面对结果进行解析，结构类似json对象
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
	 * 测试用：根据城市名称获取天气情况
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
	 * 测试用
	 * @param detail
	 */
	private void parseWeather(SoapObject detail) {
		String weatherToday = null,weatherTomorrow = null,weatherAfterday = null;
		int iconToday[] = null,iconTomorrow[] = null,iconAfterday[] = null;
//		String date = detail.getProperty(6).toString();
//		String weatherToday = "今天：" + date.split(" ")[0];
//		Log.i(TAG, weatherToday);
//		weatherToday = weatherToday + "\n天气：" + date.split(" ")[1];
		weatherToday = weatherToday + "\n气温：" + detail.getProperty(5).toString();
		weatherToday = weatherToday + "\n风力：" + detail.getProperty(7).toString() + "\n";
		Log.i(TAG, weatherToday);
		iconToday[0] = parseIcon(detail.getProperty(8).toString());
		iconToday[1] = parseIcon(detail.getProperty(9).toString());
		String weatherCurrent = detail.getProperty(10).toString();
		
//		date = detail.getProperty(13).toString();
//		String weatherTomorrow = "明天：" + date.split(" ")[0];
//		weatherTomorrow = weatherTomorrow + "\n天气：" + date.split(" ")[1];
		weatherTomorrow = weatherTomorrow + "\n气温：" + detail.getProperty(12).toString();
		weatherTomorrow = weatherTomorrow + "\n风力：" + detail.getProperty(14).toString() + "\n";
		iconTomorrow[0] = parseIcon(detail.getProperty(15).toString());
		iconTomorrow[1] = parseIcon(detail.getProperty(16).toString());
		
//		date = detail.getProperty(18).toString();
//		String weatherAfterday = "后天：" + date.split(" ")[0];
//		weatherAfterday = weatherAfterday + "\n天气：" + date.split(" ")[1];
		weatherAfterday = weatherAfterday + "\n气温：" + detail.getProperty(17).toString();
		weatherAfterday = weatherAfterday + "\n风力：" + detail.getProperty(19).toString() + "\n";
		iconAfterday[0] = parseIcon(detail.getProperty(20).toString());
		iconAfterday[1] = parseIcon(detail.getProperty(21).toString());
		} 


	/**
	 * 解析返回的结果 
	 * @param soapObject
	 **/
	protected WeatherBean parserWeather(SoapObject soapObject) {
		WeatherBean bean = new WeatherBean();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		List<Integer> icons = new ArrayList<Integer>();
		
		// 城市名
		bean.setCityName(soapObject.getProperty(1).toString());
		// 城市简介
		bean.setCityDescription(soapObject.getProperty(
				soapObject.getPropertyCount() - 1).toString());
		// 天气实况+建议
		bean.setLiveWeather(soapObject.getProperty(10).toString() + "\n"
				+ soapObject.getProperty(11).toString());
		// 其他数据
		//日期，
		String date = soapObject.getProperty(6).toString();
		
		Log.i(TAG, "");
		// ---------------------------------------------------
		String weatherToday = "今天：" + date.split(" ")[0];
		weatherToday += "\n天气：" + date.split(" ")[1];
		weatherToday += "\n气温：" + soapObject.getProperty(5).toString();
		weatherToday += "\n风力：" + soapObject.getProperty(7).toString();
		weatherToday += "\n";
 
		icons.add(parseIcon(soapObject.getProperty(8).toString()));
		icons.add(parseIcon(soapObject.getProperty(9).toString()));
		map.put("weatherDay", weatherToday);
		map.put("icons", icons);
		list.add(map);
		
		// -------------------------------------------------
		map = new HashMap<String, Object>();
		date = soapObject.getProperty(13).toString();
		String weatherTomorrow = "明天：" + date.split(" ")[0];
		weatherTomorrow += "\n天气：" + date.split(" ")[1];
		weatherTomorrow += "\n气温：" + soapObject.getProperty(12).toString();
		weatherTomorrow += "\n风力：" + soapObject.getProperty(14).toString();
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
		String weatherAfterTomorrow = "后天：" + date.split(" ")[0];
		weatherAfterTomorrow += "\n天气：" + date.split(" ")[1];
		weatherAfterTomorrow += "\n气温：" + soapObject.getProperty(17).toString();
		weatherAfterTomorrow += "\n风力：" + soapObject.getProperty(19).toString();
		weatherAfterTomorrow += "\n";
		icons = new ArrayList<Integer>();
		icons.add(parseIcon(soapObject.getProperty(20).toString()));
		icons.add(parseIcon(soapObject.getProperty(21).toString()));
		map.put("weatherDay", weatherAfterTomorrow);
		map.put("icons", icons);
		list.add(map);
		
		// 把解析后的数据放到一个list中
		bean.setList(list); 
		return bean;
	}

	// 解析图标字符串
	private int parseIcon(String data) {
		// 0.gif，返回名称0,
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
