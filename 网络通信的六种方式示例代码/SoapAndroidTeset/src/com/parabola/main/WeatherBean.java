package com.parabola.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ��װ���������JAVA BEAN
 * 
 * @author Song Shi Chao
 *
 */
public class WeatherBean {
	// ������
	public String cityName;
	// ���м��
	public String cityDescription;
	// ����ʵ��+����
	public String liveWeather;
	
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getCityDescription() {
		return cityDescription;
	}
	public void setCityDescription(String cityDescription) {
		this.cityDescription = cityDescription;
	}
	public String getLiveWeather() {
		return liveWeather;
	}
	public void setLiveWeather(String liveWeather) {
		this.liveWeather = liveWeather;
	}
	
	public void setList(List<Map<String, Object>> list) { 
		Map<String, Object> map = new HashMap<String, Object>();
		for(int i=0; i<list.size(); i++){
			 map = list.get(i);
			 WeatherBean w = new WeatherBean();
			 Object o = map.get("weatherDay");
			 System.out.println(o.toString());
		}
		
		
	}
	
}
