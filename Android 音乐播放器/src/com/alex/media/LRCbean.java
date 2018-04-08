package com.alex.media;

public class LRCbean {
	private int beginTime=0;
	public int getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(int beginTime) {
		this.beginTime = beginTime;
	}
	public int getLineTime() {
		return lineTime;
	}
	public void setLineTime(int lineTime) {
		this.lineTime = lineTime;
	}
	public String getLrcBody() {
		return lrcBody;
	}
	public void setLrcBody(String lrcBody) {
		this.lrcBody = lrcBody;
	}
	private int  lineTime=0;
	private String lrcBody = null;
	
}
