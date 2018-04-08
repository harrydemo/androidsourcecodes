package com.android.caigang.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
	
	public static String converTime(long timestamp){
		long currentSeconds = System.currentTimeMillis()/1000;
		long timeGap = currentSeconds-timestamp;//������ʱ���������
		String timeStr = null;
		if(timeGap>24*60*60){//1������
			timeStr = timeGap/(24*60*60)+"��ǰ";
		}else if(timeGap>60*60){//1Сʱ-24Сʱ
			timeStr = timeGap/(60*60)+"Сʱǰ";
		}else if(timeGap>60){//1����-59����
			timeStr = timeGap/60+"����ǰ";
		}else{//1����-59����
			timeStr = "�ո�";
		}
		return timeStr;
	}
	
	public static String getStandardTime(long timestamp){
		SimpleDateFormat sdf = new SimpleDateFormat("MM��dd�� HH:mm");
		Date date = new Date(timestamp*1000);
		sdf.format(date);
		return sdf.format(date);
	}
}
