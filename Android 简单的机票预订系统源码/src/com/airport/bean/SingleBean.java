package com.airport.bean;

import java.io.Serializable;

public class SingleBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String key;
	private int price;
	private String Value;
	private String leaveCity;
	private String arriveCity;
	private String leavetime;
	private String endtime;
	private String disaccount;

	public String getLeavetime() {
		return leavetime;
	}

	public void setLeavetime(String leavetime) {
		this.leavetime = leavetime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getDisaccount() {
		return disaccount;
	}

	public void setDisaccount(String disaccount) {
		this.disaccount = disaccount;
	}

	public String getLeaveCity() {
		return leaveCity;
	}

	public void setLeaveCity(String leaveCity) {
		this.leaveCity = leaveCity;
	}

	public String getArriveCity() {
		return arriveCity;
	}

	public void setArriveCity(String arriveCity) {
		this.arriveCity = arriveCity;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getValue() {
		return Value;
	}

	public void setValue(String value) {
		Value = value;
	}

}
