package com.search.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.content.Context;

/**
 * ��װActivity�г��õ��ķ���
 * @author Administrator
 *
 */
public final class ActivityUtils {

	public static void showDialog(Context context, String button,String title, String message){
		new AlertDialog.Builder(context)
		   .setTitle(title)
		   .setMessage(message)
		   .setNeutralButton(button, null)
		   .create()
		   .show();
	}
	
	//校验str是否为空或为""
	public static boolean validateNull(String str){
		if(str == null || str.equals("")){
			return false;
		}else{
			return true;
		}
	}
	
	//校验str中是否全部是数字，用到了正则表达式
	public static boolean validateNumber(String str){
		Pattern pattern = Pattern.compile("[0-9]");
		Matcher matcher = pattern.matcher(str);
		
		return matcher.matches();
	}
	
	//校验Ip是否合法
	public static boolean validateIp(String str){
		Pattern pattern = Pattern.compile("[0-9],.");
		Matcher matcher = pattern.matcher(str);
		
		return matcher.matches();
	}
		
}
