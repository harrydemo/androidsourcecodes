package com.np.data;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {
	private SharedPreferences sp;
	
	private final String fileName = "dp";
	
	
	public MySharedPreferences(Context context){
		sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
	}
	
	public void putString(String key,String value){
		sp.edit().putString(key, value).commit();
	}
	
	public boolean contains(String key){
		return sp.contains(key);
	}
	
	public void getAll(){
		sp.getAll();
	}
}
