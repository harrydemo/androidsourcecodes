package com.search.train;

import java.io.Serializable;

/**
 * 火车详细信息
 * @author chenjie
 *
 */
public class Train implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1668657542294399793L;
	
	private String checiType;
	private String cls;
	private String type;
	private String startStation;
	private String endStation;
	private String startTime;
	private String endTime;
	private String distant;
	private String duration;
	private String fromStation;
	private String toStation;
	private String reachStation;
	private String reachTime;
	private String leaveTime;
	private String passDuration;
	private String passDistant;
	private String priceYingZuo;
	private String priceYingWo;
	private String waitInterval;
	public String getCheciType() {
		return checiType;
	}
	public void setCheciType(String checiType) {
		this.checiType = checiType;
	}
	public String getCls() {
		return cls;
	}
	public void setCls(String cls) {
		this.cls = cls;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStartStation() {
		return startStation;
	}
	public void setStartStation(String startStation) {
		this.startStation = startStation;
	}
	public String getEndStation() {
		return endStation;
	}
	public void setEndStation(String endStation) {
		this.endStation = endStation;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getDistant() {
		return distant;
	}
	public void setDistant(String distant) {
		this.distant = distant;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getFromStation() {
		return fromStation;
	}
	public void setFromStation(String fromStation) {
		this.fromStation = fromStation;
	}
	public String getToStation() {
		return toStation;
	}
	public void setToStation(String toStation) {
		this.toStation = toStation;
	}
	public String getReachStation() {
		return reachStation;
	}
	public void setReachStation(String reachStation) {
		this.reachStation = reachStation;
	}
	public String getReachTime() {
		return reachTime;
	}
	public void setReachTime(String reachTime) {
		this.reachTime = reachTime;
	}
	public String getLeaveTime() {
		return leaveTime;
	}
	public void setLeaveTime(String leaveTime) {
		this.leaveTime = leaveTime;
	}
	public String getPassDuration() {
		return passDuration;
	}
	public void setPassDuration(String passDuration) {
		this.passDuration = passDuration;
	}
	public String getPassDistant() {
		return passDistant;
	}
	public void setPassDistant(String passDistant) {
		this.passDistant = passDistant;
	}
	public String getPriceYingZuo() {
		return priceYingZuo;
	}
	public void setPriceYingZuo(String priceYingZuo) {
		this.priceYingZuo = priceYingZuo;
	}
	public String getPriceYingWo() {
		return priceYingWo;
	}
	public void setPriceYingWo(String priceYingWo) {
		this.priceYingWo = priceYingWo;
	}
	public String getWaitInterval() {
		return waitInterval;
	}
	public void setWaitInterval(String waitInterval) {
		this.waitInterval = waitInterval;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		return sb.append(this.getCls()).append(",")
		  .append(this.getType()).append(",")
		  .append(this.getLeaveTime()).append(",")
		  .append(this.getReachTime()).toString();
	}

}
