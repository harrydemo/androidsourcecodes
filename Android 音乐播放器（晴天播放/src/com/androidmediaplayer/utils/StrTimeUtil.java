package com.androidmediaplayer.utils;


public class StrTimeUtil {
	
	// 毫秒转化成字符串时间 00:00
	public static String longTimeToString(int i) {
		StringBuilder sb = new StringBuilder();
		i = i / 1000;
		int m = i / 60;
		int s = i % 60;
		if (i >= 60) {
			if (m < 10) {
				sb.append("0");
				sb.append(String.valueOf(m));
			} else {
				sb.append(String.valueOf(m));
			}
			sb.append(":");
			if (s > 9) {
				sb.append(String.valueOf(s));
			} else {
				sb.append("0");
				sb.append(String.valueOf(s));
			}
		} else {
			sb.append("00:");
			if (s > 9) {
				sb.append(String.valueOf(s));
			} else {
				sb.append("0");
				sb.append(String.valueOf(s));
			}
		}
		return sb.toString();
	}
	
}
