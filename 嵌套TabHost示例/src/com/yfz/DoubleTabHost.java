package com.yfz;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

/**
 * 本类继承了TabActivity
 * @author Administrator
 *
 */
public class DoubleTabHost extends TabActivity {
	
	 /* 注意：
	 * 对于TabHost、布局文件中必须包含
	 * TabHost、TabWidget 、FrameLayout 
	 * 如果继承TabActivity，并且通过getTabHost()方法来获取TabHost
	 * 那么三者的ID必须是android.R.id.tabhost、android.R.id.tabs、android.R.id.tabcontent
	 * 
	 * 如果继承Activity，可以通过findViewById来获取这三个组件，此时ID可自定义
	 */
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
		TabHost mTabHost = getTabHost();

		mTabHost.addTab(mTabHost.newTabSpec("Twitter").setIndicator(
				"Twitter",
				getResources().getDrawable(android.R.drawable.arrow_down_float)).setContent(
				new Intent(this, SubTab.class)));
		mTabHost.addTab(mTabHost.newTabSpec("Facebook").setIndicator(
				"Facebook",
				getResources().getDrawable(android.R.drawable.arrow_down_float)).setContent(
				new Intent(this, NormalActivity.class)));
		mTabHost.setCurrentTab(0);
    }
}