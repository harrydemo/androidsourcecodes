package com.getContacts;

import java.util.ArrayList;
import java.util.List;

public class Person {
	private String name;
	private List<String> phone;
	private String email;
	private String address;
	private String id;
	public Person()
	{
		phone = new ArrayList<String>();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getPhone() {
		return phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void addPhone(String phone){
		this.phone.add(phone);
	}
	public String getID() {
		return id;
	}
	public void setID(String _id) {
		this.id = _id;
	}
}
