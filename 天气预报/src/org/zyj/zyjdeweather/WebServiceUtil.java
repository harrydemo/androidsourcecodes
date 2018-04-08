package org.zyj.zyjdeweather;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;



public class WebServiceUtil {
	// 定义Web Service的命名空间
	static final String SERVICE_NS = "http://WebXml.com.cn/";
	// 定义Web Service提供服务的URL
	static final String SERVICE_URL = "http://webservice.webxml.com.cn/WebServices/WeatherWS.asmx";

	/**
	 * 获得州，国内外省份和城市信息
	 * 
	 * @return
	 */
	public static List<String> getProvinceList() {
		// 需要调用的方法名(获得本天气预报Web Services支持的洲、国内外省份和城市信息)
		String methodName = "getRegionProvince";
		// 创建HttpTransportSE传输对象
		HttpTransportSE httpTranstation = new HttpTransportSE(SERVICE_URL);
		httpTranstation.debug = true;
		// 使用SOAP1.1协议创建Envelop对象
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		// 实例化SoapObject对象
		SoapObject soapObject = new SoapObject(SERVICE_NS, methodName);
		envelope.bodyOut = soapObject;
		// 设置与.Net提供的Web Service保持较好的兼容性
		envelope.dotNet = true;
		try {
			// 调用Web Service
			httpTranstation.call(SERVICE_NS + methodName, envelope);
			if (envelope.getResponse() != null) {
				// 获取服务器响应返回的SOAP消息
				SoapObject result = (SoapObject) envelope.bodyIn;
				SoapObject detail = (SoapObject) result.getProperty(methodName
						+ "Result");
				// 解析服务器响应的SOAP消息。
				return parseProvinceOrCity(detail);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据省份获取城市列表
	 * 
	 * @param province
	 * @return
	 */
	public static List<String> getCityListByProvince(String province) {
		// 需要调用的方法名(获得本天气预报Web Services支持的城市信息,根据省份查询城市集合：带参数)
		String methodName = "getSupportCityString";
		HttpTransportSE httpTranstation = new HttpTransportSE(SERVICE_URL);
		httpTranstation.debug = true;
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		SoapObject soapObject = new SoapObject(SERVICE_NS, methodName);
		soapObject.addProperty("theRegionCode", province);
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		try {
			// 调用Web Service
			httpTranstation.call(SERVICE_NS + methodName, envelope);
			if (envelope.getResponse() != null) {
				// 获取服务器响应返回的SOAP消息
				SoapObject result = (SoapObject) envelope.bodyIn;
				SoapObject detail = (SoapObject) result.getProperty(methodName
						+ "Result");
				// 解析服务器响应的SOAP消息。
				return parseProvinceOrCity(detail);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// 解析出每个省份
	private static List<String> parseProvinceOrCity(SoapObject detail) {
		ArrayList<String> result = new ArrayList<String>();
		for (int i = 0; i < detail.getPropertyCount(); i++) {
			String str = detail.getProperty(i).toString();
			result.add(str.split(",")[0]);
		}
		return result;
	}

	public static SoapObject getWeatherByCity(String cityName) {

		// 根据城市或地区名称查询获得未来三天内天气情况、现在的天气实况、天气和生活指数
		String methodName = "getWeather";

		HttpTransportSE httpTranstation = new HttpTransportSE(SERVICE_URL);
		httpTranstation.debug = true;
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		SoapObject soapObject = new SoapObject(SERVICE_NS, methodName);
		soapObject.addProperty("theCityCode", cityName);
		envelope.bodyOut = soapObject;
		envelope.dotNet = true;
		try {
			// 调用Web Service
			httpTranstation.call(SERVICE_NS + methodName, envelope);
			if (envelope.getResponse() != null) {
				// 获取服务器响应返回的SOAP消息
				SoapObject result = (SoapObject) envelope.bodyIn;
				SoapObject detail = (SoapObject) result.getProperty(methodName
						+ "Result");
				// 解析服务器响应的SOAP消息。
				return detail;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
