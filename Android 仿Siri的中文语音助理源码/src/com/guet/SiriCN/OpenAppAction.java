/*
 *  http://www.guet.edu.cn/
 *  by hmg25 20111212
 *  Just For Learning
 */
package com.guet.SiriCN;

import java.util.List;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

public class OpenAppAction {
	  MainActivity mActivity;
	  String mAppName;
	  public OpenAppAction(String appname,MainActivity activity)
	  {
		mAppName = appname;
	    mActivity=activity;
	  }
	  
	 public void runApp()
	 {
		if((mAppName!=null)&&(mAppName.length()!=0)){
			getAppByName();
		}
	 }
	 
	 private void getAppByName(){
		 Intent intent = new Intent();
		 intent.setAction("android.intent.action.MAIN");
		 intent.addCategory("android.intent.category.LAUNCHER");
		 PackageManager pm = mActivity.getPackageManager();
		 List<ResolveInfo> installAppList = pm.queryIntentActivities(intent, 0);
		 for(ResolveInfo info :installAppList){
			   String name = info.loadLabel(pm).toString();
			   if(name.contains(mAppName)){
				   String pkgname=info.activityInfo.packageName;
				   if("com.android.contacts".equalsIgnoreCase(pkgname)){
					   Uri uri = Uri.parse("content://contacts/people");
				        Intent i= new Intent("android.intent.action.VIEW", uri);
				        mActivity.startActivity(i);  
				   }else{
				   intent = pm.getLaunchIntentForPackage(pkgname);
			       intent.addCategory("android.intent.category.LAUNCHER");
			       mActivity.startActivity(intent);				  
			       }
				   return ;
			   }
		 }
		 mActivity.speak("没有找到你所说的应用哦^_^", true);
	 }
	 
}
