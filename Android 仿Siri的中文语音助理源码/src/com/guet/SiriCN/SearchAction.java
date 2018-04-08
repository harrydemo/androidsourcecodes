/*
 *  http://www.guet.edu.cn/
 *  by hmg25 20111212
 *  Just For Learning
 */
package com.guet.SiriCN;

import android.content.Intent;
import android.net.Uri;

public class SearchAction {
	 MainActivity mActivity;
	 String mKeyword;
	 String searchEngine;
	
	 public  SearchAction(String name,String engine,MainActivity activity)
	  {
		mKeyword = name;
	    mActivity=activity;
	    searchEngine=engine;
	  }
	 
	 public void Search(){		 
		startWebSearch();	
	 }
	
	 private void startWebSearch()
	  {
		 Intent intent = new Intent();
		if(searchEngine.contains("°Ù¶È")){
			intent.setAction(Intent.ACTION_VIEW);	
			intent.setData(Uri.parse("http://m.baidu.com/s?word="+mKeyword)); 
		}else{
		intent.setAction("android.intent.action.WEB_SEARCH");	
	    intent.putExtra("query", mKeyword);	 
		}
	    mActivity.startActivity(intent);
	  }
}
