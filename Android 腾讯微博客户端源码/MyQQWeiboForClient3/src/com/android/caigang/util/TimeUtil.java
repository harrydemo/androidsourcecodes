package com.android.caigang.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
	
	public static String converTime(long timestamp){
		long currentSeconds = System.currentTimeMillis()/1000;
		long timeGap = currentSeconds-timestamp;//与现在时间相差秒数
		String timeStr = null;
		if(timeGap>24*60*60){//1天以上
			timeStr = timeGap/(24*60*60)+"天前";
		}else if(timeGap>60*60){//1小时-24小时
			timeStr = timeGap/(60*60)+"小时前";
		}else if(timeGap>60){//1分钟-59分钟
			timeStr = timeGap/60+"分钟前";
		}else{//1秒钟-59秒钟
			timeStr = "刚刚";
		}
		return timeStr;
	}
	
	public static String getStandardTime(long timestamp){
		SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm");
		Date date = new Date(timestamp*1000);
		sdf.format(date);
		return sdf.format(date);
	}
}
