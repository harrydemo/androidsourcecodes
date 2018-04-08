package com.android.haven.adapter;

import android.graphics.drawable.Drawable;

/**
 * @auther hujh
 * @version 2010-10-26 ÏÂÎç11:18:02
 */

public class ContactEntity {
	private long id;
	private String lookupKey;
	private String name;
	private String phoneNum;
	private String telNum;
	private Drawable img;
	private boolean checked;
	
	
	public ContactEntity(){
		
	}
	
	public ContactEntity(long id,String lookupKey,String name,String phoneNum,String telNum,Drawable img,boolean checked){
		this.id = id;
		this.lookupKey = lookupKey;
		this.name = name;
		this.phoneNum = phoneNum;
		this.telNum = telNum;
		this.img = img;
		this.checked = checked;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getTelNum() {
		return telNum;
	}
	public void setTelNum(String telNum) {
		this.telNum = telNum;
	}
	public Drawable getImg() {
		return img;
	}
	public void setImg(Drawable img) {
		this.img = img;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLookupKey() {
		return lookupKey;
	}

	public void setLookupKey(String lookupKey) {
		this.lookupKey = lookupKey;
	}
	
}
