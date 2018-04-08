package com.cellcom;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;

//ע�����activity�̳е���TabActivity
public class TabWidgetActivity extends TabActivity {

	//����TabHost����
	private TabHost mTabHost;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_widget);
		
		//��ȡTabHoast����
		mTabHost=getTabHost();
		/* ΪTabHost��ӱ�ǩ */
		//�½�һ��newTabSpec(newTabSpec)
		//�������ǩ��ͼ��(setIndicator)
		//��������(setContent)
		mTabHost.addTab(mTabHost.newTabSpec("tab_test1").setIndicator("TAB1",getResources().getDrawable(R.drawable.img1)).setContent(R.id.textview1));
		mTabHost.addTab(mTabHost.newTabSpec("tab_test2").setIndicator("TAB2",getResources().getDrawable(R.drawable.img2)).setContent(R.id.textview2));
		mTabHost.addTab(mTabHost.newTabSpec("tab_test3").setIndicator("TAB3",getResources().getDrawable(R.drawable.img3)).setContent(R.id.textview3));
		
		//����TabHost�ı�����ɫ
	    mTabHost.setBackgroundColor(Color.argb(150, 22, 70, 150));
	    
	    //��ʾ��һ��tab
	    mTabHost.setCurrentTab(0);
	    
	  //��ǩ�л��¼�����setOnTabChangedListener 
	    mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
				Dialog dialog=new AlertDialog.Builder(TabWidgetActivity.this)
				.setTitle("������")
				.setMessage("�㵱ǰѡ�е���"+tabId+"��ǩ��")
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(TabWidgetActivity.this, "�㵥����ȷ����ť��", Toast.LENGTH_LONG).show();
						dialog.cancel();
					}
				})
				.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(TabWidgetActivity.this, "�㵥����ȡ����ť��", Toast.LENGTH_LONG).show();
						dialog.cancel();
					}
				})
				.create();
				dialog.show();
			}
		});
	}

}
