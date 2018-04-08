package com.cn.daming;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Contants {
	
    //repeated day of week define 
    public static Map<String, Integer> mapWeek = new HashMap<String,Integer>();
    
    public static void addMapWeek(){
    	mapWeek.put("周一", 1);
    	mapWeek.put("周二", 2);
    	mapWeek.put("周三", 3);
    	mapWeek.put("周四", 4);
    	mapWeek.put("周五", 5);
    	mapWeek.put("周六", 6);
    	mapWeek.put("周日", 7);
    	mapWeek.put("无重复", 0);
    }
    
    public static int getMapWeek(String str){ 
    	Contants.addMapWeek();
    	int dayOfMapWeek = 0;
    	if(str != null){
    		dayOfMapWeek = mapWeek.get(str);
    	}
		return dayOfMapWeek;
    }
    
    public static String[] getDatetimeString(){
		Date date = new Date();
		String[] tempStr = new String[2];
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = format.format(date);
		tempStr[0] = str.substring(0, 10);
		tempStr[1] = str.substring(11, str.length()); //分钟和秒
		return tempStr;
	}
    
    public static long getNowTimeMinuties() //得到现在的时间
    {
    	return System.currentTimeMillis();
    }
    
    public static boolean differSetTimeAndNowTime(long setTime)//设置的时间和现在的时间的差别
    {
    	if(setTime >= getNowTimeMinuties()){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    public static long getDifferMillis(int differDays) //毫秒
    {
    	return differDays * 24 * 60 * 60 * 1000;
    }
    
    //compare nowDay to nextDay
    public static int compareDayNowToNext(int nowDay,int nextDay){//周期重复
    	if(nextDay > nowDay){
    		return (nextDay-nowDay);
    	}else if(nextDay == nowDay){
    	    return 0;	
    	}else{
    		return (7-(nowDay-nextDay));
    	}
    }
    
    //turn the nowday to China`s day of Week
    public static Map<String, Integer> nowWeek = new HashMap<String,Integer>();
    public static void addNowWeek()
    {
    	nowWeek.put("1", 7);
    	nowWeek.put("2", 1);
    	nowWeek.put("3", 2);
    	nowWeek.put("4", 3);
    	nowWeek.put("5", 4);
    	nowWeek.put("6", 5);
    	nowWeek.put("7", 6);
    }
    
    public static int getNowWeek() //得到今天周几
    {
    	Calendar nowCal = Calendar.getInstance();
    	Date nowDate = new Date(System.currentTimeMillis());
    	nowCal.setTime(nowDate); //设置日历为当前日期
    	int nowNum = nowCal.get(nowCal.DAY_OF_WEEK);//得到周几
    	String nowNumStr = String.valueOf(nowNum);
        Contants.addNowWeek();
        int nowDayOfWeek = 0; //无重复
        if(nowNumStr != null){
        	nowDayOfWeek = nowWeek.get(nowNumStr); //得到今天周几
        }
        return nowDayOfWeek;
    }
    
    public static int getSetDay(String str)
    {
        	if(str.equals("周一")){
        		return 1;
        	}
        	if(str.equals("周二")){
        		return 2;
        	}
        	if(str.equals("周三")){
        		return 3;
        	}
        	if(str.equals("周四")){
        		return 4;
        	}
        	if(str.equals("周五")){
        		return 5;
        	}
        	if(str.equals("周六")){
        		return 6;
        	}
        	if(str.equals("周日")){
        		return 7;
        	}
        	return 0;
    }
    
    public static int[] getDayOfNum(String[] str)//传入周一周二
    {
    	int[] dayOfInt = new int[str.length];
    	for(int i=0;i<str.length;i++){
    		dayOfInt[i] = getSetDay(str[i]);
    	}
    	return dayOfInt;
    }
    
    public static int getResultDifferDay(int[] in,int nowDay)
    {
         int result = 0;
         for(int i=0;i<in.length;i++){
        	 if(in[i] >= nowDay){
        		 result = in[i];
        		 break;
        	 }
         }
         
         if(result == 0){
        	 result = in[0];
         }
         return result;
    }
    
    public static int getResultDifferDay1(int[] in,int nowDay)//传入循环的周数和当天周几
    {
         int result = 0;
         for(int i=0;i<in.length;i++){
        	 if(in[i] > nowDay){
        		 result = in[i];
        		 break;
        	 }
         }
         
         if(result == 0){
        	 result = in[0];
         }
         return result;
    }
}
