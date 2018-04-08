package com.sly.android.huangcun.ui;



import android.app.Activity;
import android.content.SharedPreferences;


/**
 * @param activity
 *以键值对的方式存储和读取基本数据类型。以实现Activity间的数据传递和数据共享；
 */
public class SaveTools {

	public SharedPreferences sharepre;
	
	public SaveTools(Activity activity){
		sharepre = activity.getSharedPreferences("textReader",Activity.MODE_PRIVATE);
		
	}
    public void saveInt(String name,int value){
    	SharedPreferences.Editor editor=sharepre.edit();
    	editor.putInt(name, value);
    	editor.commit();
    }
    public  void saveString(String name,String value){
    	SharedPreferences.Editor editor=sharepre.edit();
    	editor.putString(name, value);
    	editor.commit();
    }
    public  void saveBoolean(String name,boolean value){
    	SharedPreferences.Editor editor=sharepre.edit();
    	editor.putBoolean(name, value);
    	editor.commit();
    }
    public  int readInt(String name,int defaultValue){
    	return sharepre.getInt(name, defaultValue);
    }
    public  Boolean readBoolean (String name,Boolean defaultValue){
    	return sharepre.getBoolean(name, defaultValue);
    }
    public  String readString(String name,String defaultValue){
    	return sharepre.getString(name, defaultValue);
    }
}
