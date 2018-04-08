package com.samg_checkinapp;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends Activity {
	private Button button = null;
	private Spinner spinner = null;
	private EditText et1 = null;
	private EditText et2 = null;
	private RadioGroup rg = null;
	private RadioButton rb1 = null;
	private RadioButton rb2 = null;
	//Toast数据
	private void display(String msg){
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
    //做文本输入对话框
	private void dialog(String msg){       
	        AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setTitle(msg);
	        builder.show();
	}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //做spinner的效果
        String[] universities= {"清华","北大","人大","北航","北邮","北影"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, universities);
        spinner = (Spinner)findViewById(R.id.spinner1);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener(){

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				display(((TextView)arg1).getText().toString());
			}
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub				
			}    	
        });
        //做男/女单选效果
        rg = (RadioGroup)findViewById(R.id.radioGroup);
        rb1 = (RadioButton)findViewById(R.id.radioButton1);
        rb2 = (RadioButton)findViewById(R.id.radioButton2);
        rg.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if(rb1.getId()==checkedId)
					display("性别：男");
				else if(rb2.getId()==checkedId)
					display("性别：女");
			}
        	
        });
        //做按钮跳转
        button = (Button)findViewById(R.id.button1);
        button.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				et1 = (EditText)findViewById(R.id.editText);
		        et2 = (EditText)findViewById(R.id.editText1);
		        if((et1.getText().toString()).equals("请输入姓名")||((et1.getText().toString()).equals("")))
		        	dialog("姓名为必填项，请务必输入姓名！");
		        else if((et2.getText().toString()).equals("请输入电话")||((et2.getText().toString()).equals("")))
		        	dialog("联系电话为必填项，请务必输入电话号码！");
		        else {
		        DatabaseHelper dbh = new DatabaseHelper(MainActivity.this,"SamG_Checkin");
		    	SQLiteDatabase sd = dbh.getWritableDatabase();
		    	ContentValues values = new ContentValues();
		    	values.put("name", et1.getText().toString());
		    	values.put("number", et2.getText().toString());
		    	sd.insert("CheckinTable", null, values);
		    	sd.close();
		        Intent intent = new Intent();
		        //intent.putExtra("name", et1.getText().toString());
		        //intent.putExtra("number", et2.getText().toString());
				intent.setClass(MainActivity.this, ListActivity.class);
				startActivity(intent);
				//加一个notification
				//首先加入notification manager，再确定notification的闪出文本，和闪出时间		
				NotificationManager notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				int icon = R.drawable.ic_launcher;
				CharSequence flashText = "录入提醒";
				long time = System.currentTimeMillis();
				PendingIntent pi = PendingIntent.getActivity(MainActivity.this,110,new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+et2.getText().toString())), 0);
				
				Notification notify = new Notification();
				notify.icon = icon;
				notify.tickerText=flashText;
				notify.when = time;
				notify.contentIntent=pi;

				
//				Notification notify = new Notification
//						.Builder(MainActivity.this)
//						.setSmallIcon(icon)
//						.setTicker(flashText)
//						.setWhen(time)
//						.setContentTitle("录入联系方式")
//						.setContentText("点击将拨通联系人电话")
//						.setContentIntent(pi)
//						.build();				
//				notifyManager.notify(1, notify);
				}	      
			}        	
        });
    }
    @Override
    //做菜单栏
    public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add("退出");
    	menu.add("关于我们");
        return true;
    }
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getTitle().equals("退出"))
			finish();
		else if(item.getTitle().equals("关于我们"));
			dialog("SamG工作室出品");
		return super.onMenuItemSelected(featureId, item);
		
	}
    
    
}
