package com.php;

public class User {
	private int id ;
	private String title;
	private String date;
	private String address;
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAddress() {
		return address;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDate() {
		return date;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitle() {
		return title;
}
}
