package com.bn.d2.bill;

import java.util.Date;
/**
 * 
 * 获取系统当前日期的工具类
 *
 */
public class DateUtil
{
	public static String getCurrentDate()
	{
		String dateStr=null;
		Date date=new Date();
		date.getTime();
		int year=date.getYear()+1900;
		int month=date.getMonth()+1;
		int dt=date.getDate();
		dateStr=year+"-"+((month<10)?"0"+month:month)+"-"+((dt<10)?"0"+dt:dt);
		return dateStr;
	}
}