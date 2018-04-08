package com.android.tools;
/*
 * Copyright (C) 2011 Androd源码工作室
 * 
 * Android实战教程--网络视频类播发器
 * 
 * taobao : http://androidsource.taobao.com
 * mail : androidSource@139.com
 * QQ:    androidSource@139.com
 * 
 */
import java.util.HashMap;
import java.util.Map;

/*
 * CD数据结构
 * */
public class CDataStruct{

	private Map targes;

	//构造函数--建立Map数据类型
	public CDataStruct(String as[], String as1[]){
		targes = null;
		targes = new HashMap();
		
		//建立HashMap类型数据
		for(int i = 0; i < as.length; i++){
			Map map = targes;
			String s = as[i];
			String s1 = as1[i];
			Object obj = map.put(s, s1);
		}
		
	}

	//获取具体位置的数据
	public String GetPosition(String s){
		return (String)targes.get(s);
	}
}
