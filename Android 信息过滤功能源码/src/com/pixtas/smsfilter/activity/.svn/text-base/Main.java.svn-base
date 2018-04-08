package com.pixtas.smsfilter.activity;

import com.pixtas.helper.DatabaseAdapterHelper;
import com.pixtas.helper.DialogHelper;
import com.pixtas.smsfilter.Config;
import com.pixtas.smsfilter.R;
import com.pixtas.smsfilter.rest.RestAPI;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Main extends Activity implements OnClickListener{
	private static final String TAG = "Main";
	private Button switchBtn = null;
	private TextView switchTxt = null;
	
	
	private DatabaseAdapterHelper databaseAdapterHelper = null;

	private static final String START = "start";
	private static final String CLOSE = "stop";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		switchTxt = (TextView) findViewById(R.id.switch_txt);
		switchBtn = (Button) findViewById(R.id.switch_btn);
		switchBtn.setOnClickListener(this);
		if(Config.connectedNetwork(this)){
			if(Config.debug){
				Log.v(TAG,"--------connected----------");
			}
			if(!RestAPI.checkApkUpgrade()){
				Builder builder = DialogHelper.showAlertBuilder(Main.this, R.string.app_need_update, R.string.app_warning);
				builder.setCancelable(true);
	    		builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						try {
							String url = "market://details?id=com.pixtas.smsfilter";
		        			Intent intent = new Intent(Intent.ACTION_VIEW);
		        			intent.setData(Uri.parse(url));
		        			startActivity(intent);
						} catch (Exception e) {
						}finally{
							finish();
						}
					}
				});
				builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
				builder.show();
			}
		}
		initSwitchBtn();
	}
	
	private void initSwitchBtn(){
		databaseAdapterHelper = new DatabaseAdapterHelper(getApplication());
		databaseAdapterHelper.open_sms();
		Cursor cursor = databaseAdapterHelper.fetchAllData();
		if(cursor.getCount() == 0) {
			if(Config.debug){
				Log.v(TAG,"--no data in database-->");
			}
			databaseAdapterHelper.insertData(0);
			switchTxt.setText(R.string.app_start_content);
			switchBtn.setText(START);
		}else{
			cursor.moveToFirst();
			int flag = cursor.getInt(cursor.getColumnIndex("data"));
			if(Config.debug){
				Log.v(TAG,"--flag from database-->" + flag);
			}
			if(flag == 1){
				switchTxt.setText(R.string.app_stop_content);
				switchBtn.setText(CLOSE);
			}else{
				switchTxt.setText(R.string.app_start_content);
				switchBtn.setText(START);
			}
		}
		cursor.close();
		databaseAdapterHelper.close_sms();
	}
	
	@Override
	public void onClick(View v) {
		if(v == switchBtn){
			if(switchBtn.getText().equals(START)){
//				showDLG(1);
				controlReveiver(1);
				switchTxt.setText(R.string.app_stop_content);
				switchBtn.setText(CLOSE);
			}else{
//				showDLG(0);
				controlReveiver(0);
				switchTxt.setText(R.string.app_start_content);
				switchBtn.setText(START);
			}
		}
	}
	
	/*private void showDLG(final int flag){
		Builder builder = DialogHelper.showAlertBuilder(this, R.string.app_sure, R.string.app_warning);
		builder.setCancelable(true);
		builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(flag == 1){
					controlReveiver(1);
					switchBtn.setText(CLOSE);
				}else{
					controlReveiver(0);
					switchBtn.setText(START);
				}
			}
		}).setNegativeButton("no", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		}).show();
	}*/
	
	private void controlReveiver(int flag){
		databaseAdapterHelper.open_sms();
		databaseAdapterHelper.updateSmsData(1,flag);
		databaseAdapterHelper.close_sms();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		super.onCreateOptionsMenu(menu);

        menu.add(0, Menu.FIRST + 1, 1, R.string.menu_about).setIcon(R.drawable.ic_menu_about);
        menu.add(0, Menu.FIRST + 2, 2, R.string.menu_exit).setIcon(R.drawable.ic_menu_block);
        
        return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){

            case Menu.FIRST + 1:
            	this.showAboutMsg();
                break;
            case Menu.FIRST + 2:
            	finish();
                break;
            default:
                break;
        }
        
        return true;
	}
	
	private void showAboutMsg(){
		Builder builder = DialogHelper.showAlertBuilder(this, R.string.about_version, R.string.about_title);
		builder.setCancelable(true);
		builder.setPositiveButton("close", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		builder.show();
	}
	
	
	
	
	
	
	
	/*private void registerReveiver(){
		smsReceiver = new SmsReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Config.SMS_RECEIVE_ACTION);
		Main.this.registerReceiver(smsReceiver, filter);
		switchBtn.setText("close");
	}
	
	private void unregisterReveiver(){
		Main.this.unregisterReceiver(smsReceiver);
		switchBtn.setText("start");
	}*/
}
