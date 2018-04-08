package com.search.ip;

import java.io.Serializable;

public class Ip implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -41209166676316404L;
	
	private String ip;
	private String location;
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	public String toString(){
		return this.ip+this.location;
	}

}
