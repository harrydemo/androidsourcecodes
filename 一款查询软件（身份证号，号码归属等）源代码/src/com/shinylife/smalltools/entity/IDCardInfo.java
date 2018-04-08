package com.shinylife.smalltools.entity;

public class IDCardInfo {
	private String code;
	private String location;
	private String birthday;
	private String gender;
	public String getCode() {
		return code;
	}
	public String getLocation() {
		return location;
	}
	public String getBirthday() {
		return birthday;
	}
	public String getGender() {
		return gender;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
}
