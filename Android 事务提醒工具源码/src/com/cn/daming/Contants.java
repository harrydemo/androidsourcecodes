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
    	mapWeek.put("��һ", 1);
    	mapWeek.put("�ܶ�", 2);
    	mapWeek.put("����", 3);
    	mapWeek.put("����", 4);
    	mapWeek.put("����", 5);
    	mapWeek.put("����", 6);
    	mapWeek.put("����", 7);
    	mapWeek.put("���ظ�", 0);
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
		tempStr[1] = str.substring(11, str.length()); //���Ӻ���
		return tempStr;
	}
    
    public static long getNowTimeMinuties() //�õ����ڵ�ʱ��
    {
    	return System.currentTimeMillis();
    }
    
    public static boolean differSetTimeAndNowTime(long setTime)//���õ�ʱ������ڵ�ʱ��Ĳ��
    {
    	if(setTime >= getNowTimeMinuties()){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    public static long getDifferMillis(int differDays) //����
    {
    	return differDays * 24 * 60 * 60 * 1000;
    }
    
    //compare nowDay to nextDay
    public static int compareDayNowToNext(int nowDay,int nextDay){//�����ظ�
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
    
    public static int getNowWeek() //�õ������ܼ�
    {
    	Calendar nowCal = Calendar.getInstance();
    	Date nowDate = new Date(System.currentTimeMillis());
    	nowCal.setTime(nowDate); //��������Ϊ��ǰ����
    	int nowNum = nowCal.get(nowCal.DAY_OF_WEEK);//�õ��ܼ�
    	String nowNumStr = String.valueOf(nowNum);
        Contants.addNowWeek();
        int nowDayOfWeek = 0; //���ظ�
        if(nowNumStr != null){
        	nowDayOfWeek = nowWeek.get(nowNumStr); //�õ������ܼ�
        }
        return nowDayOfWeek;
    }
    
    public static int getSetDay(String str)
    {
        	if(str.equals("��һ")){
        		return 1;
        	}
        	if(str.equals("�ܶ�")){
        		return 2;
        	}
        	if(str.equals("����")){
        		return 3;
        	}
        	if(str.equals("����")){
        		return 4;
        	}
        	if(str.equals("����")){
        		return 5;
        	}
        	if(str.equals("����")){
        		return 6;
        	}
        	if(str.equals("����")){
        		return 7;
        	}
        	return 0;
    }
    
    public static int[] getDayOfNum(String[] str)//������һ�ܶ�
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
    
    public static int getResultDifferDay1(int[] in,int nowDay)//����ѭ���������͵����ܼ�
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
