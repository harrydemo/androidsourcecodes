package com.androidmediaplayer.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LrcProcessorUtils {

	@SuppressWarnings("rawtypes")
	public static ArrayList<ArrayList> process(String lrcPath) throws Exception {
		ArrayList<Long> times = new ArrayList<Long>();
		ArrayList<String> lrcs = new ArrayList<String>();
		ArrayList<Long> timeToTime = new ArrayList<Long>();

		String temp = null;
		Long timeMill = null;
		String msg = null;
		InputStream inputStream = null;
		BufferedInputStream bufferedInputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		ArrayList<ArrayList> arrayList = new ArrayList<ArrayList>();

		inputStream = new FileInputStream(lrcPath);
		bufferedInputStream = new BufferedInputStream(inputStream);
		inputStreamReader = new InputStreamReader(bufferedInputStream, "UTF-8");
		bufferedReader = new BufferedReader(inputStreamReader);
		while ((temp = bufferedReader.readLine()) != null) {
			if("".equals(temp.trim())){
				continue;
			}
			timeMill = time2Long(temp);
			times.add(timeMill);
			int end = temp.indexOf("]");
			if("".equals(temp.substring(end + 1).trim())){
				msg = "......";
			}else{
				msg = temp.substring(end + 1);
			}
			lrcs.add(msg.trim());
		}
		int timesSize = times.size();
		long timeTemp;
		for (int i = 0; i < timesSize - 1; i++) {
			timeTemp = times.get(i + 1) - times.get(i);
			timeToTime.add(timeTemp);
		}
		timeToTime.add(-1l);
		arrayList.add(times);
		arrayList.add(lrcs);
		arrayList.add(timeToTime);
		bufferedReader.close();
		inputStreamReader.close();
		bufferedInputStream.close();
		inputStream.close();
		return arrayList;
	}

	/**
	 * 将分钟，秒全部转换成毫秒
	 * @param timeStr
	 * @return
	 */
	private static Long time2Long(String timeStr) throws Exception {
		if(null == timeStr || timeStr.trim().equals("")){
			throw new RuntimeException("lrc文件有问题");
		}
		int start = timeStr.indexOf("[");
		String min = timeStr.substring(start + 1, start + 3);
		String sec = timeStr.substring(start + 4, start + 6);
		String mill = timeStr.substring(start + 7, start + 9);
		return Long.parseLong(min) * 60 * 1000 + Long.parseLong(sec) * 1000
				+ Long.parseLong(mill) * 10;
	}

}
