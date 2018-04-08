package com.lfp.domain;

public class Bus {
	private Integer id;
	private String line;
	private String station;
	
	public Bus(){
		
	}
	public Bus(String line,String station){
		this.line = line;
		this.station = station;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

}
