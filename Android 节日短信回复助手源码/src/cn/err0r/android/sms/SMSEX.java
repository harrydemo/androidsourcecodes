package cn.err0r.android.sms;

import android.app.Application;

public class SMSEX extends Application {
	
	String m;
	String t;
	String r;
	
	
	@Override
	public void onCreate() {
		super.onCreate();
	}


	public String getM() {
		return m;
	}


	public void setM(String m) {
		this.m = m;
	}


	public String getT() {
		return t;
	}


	public void setT(String t) {
		this.t = t;
	}


	public String getR() {
		return r;
	}


	public void setR(String r) {
		this.r = r;
	}
}
