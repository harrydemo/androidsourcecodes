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
	//Toast����
	private void display(String msg){
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
    //���ı�����Ի���
	private void dialog(String msg){       
	        AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setTitle(msg);
	        builder.show();
	}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //��spinner��Ч��
        String[] universities= {"�廪","����","�˴�","����","����","��Ӱ"};
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
        //����/Ů��ѡЧ��
        rg = (RadioGroup)findViewById(R.id.radioGroup);
        rb1 = (RadioButton)findViewById(R.id.radioButton1);
        rb2 = (RadioButton)findViewById(R.id.radioButton2);
        rg.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if(rb1.getId()==checkedId)
					display("�Ա���");
				else if(rb2.getId()==checkedId)
					display("�Ա�Ů");
			}
        	
        });
        //����ť��ת
        button = (Button)findViewById(R.id.button1);
        button.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				et1 = (EditText)findViewById(R.id.editText);
		        et2 = (EditText)findViewById(R.id.editText1);
		        if((et1.getText().toString()).equals("����������")||((et1.getText().toString()).equals("")))
		        	dialog("����Ϊ��������������������");
		        else if((et2.getText().toString()).equals("������绰")||((et2.getText().toString()).equals("")))
		        	dialog("��ϵ�绰Ϊ��������������绰���룡");
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
				//��һ��notification
				//���ȼ���notification manager����ȷ��notification�������ı���������ʱ��		
				NotificationManager notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				int icon = R.drawable.ic_launcher;
				CharSequence flashText = "¼������";
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
//						.setContentTitle("¼����ϵ��ʽ")
//						.setContentText("�������ͨ��ϵ�˵绰")
//						.setContentIntent(pi)
//						.build();				
//				notifyManager.notify(1, notify);
				}	      
			}        	
        });
    }
    @Override
    //���˵���
    public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add("�˳�");
    	menu.add("��������");
        return true;
    }
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getTitle().equals("�˳�"))
			finish();
		else if(item.getTitle().equals("��������"));
			dialog("SamG�����ҳ�Ʒ");
		return super.onMenuItemSelected(featureId, item);
		
	}
    
    
}
