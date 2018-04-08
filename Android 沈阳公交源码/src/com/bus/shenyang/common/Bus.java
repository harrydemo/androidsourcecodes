package com.bus.shenyang.common;

public class Bus {
	public Integer id;
	private String line;
	private String time;
	private String station;
	private String opposite;
	

	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getOpposite() {
		return opposite;
	}
	public void setOpposite(String opposite) {
		this.opposite = opposite;
	}
	public Bus(){
		
	}
	public Bus(String line,String time,String station,String opposite){
		this.line = line;
		this.station = station;
		this.time = time;
		this.opposite = opposite;
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
