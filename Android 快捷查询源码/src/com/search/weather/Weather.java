package com.search.weather;

import java.io.Serializable;

public class Weather implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 13835783478884L;
	
	private String day; //星期
	
	private String date; //日期
	
	private String low; //最低温度
	
	private String high; //最高温度
	
	private String code; //天气状况码
	
	private String text; //天气状况码,对应的天气类型

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getLow() {
		return low;
	}

	public void setLow(String low) {
		this.low = low;
	}

	public String getHigh() {
		return high;
	}

	public void setHigh(String high) {
		this.high = high;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		return sb.append(this.getDate()).append(",")
		  .append(this.getDate()).append(",")
		  .append(this.getLow()).append(",")
		  .append(this.getHigh()).append(",")
		  .append(this.getCode()).append(",")
		  .append(this.getText()).toString();
	}
	

}
