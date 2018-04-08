package com.alex.media;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity{
	private ListView listview=null;
	private Intent intent = null;
	private static final int ITEM1 = Menu.FIRST;
	private static final int ITEM2 = Menu.FIRST+1;
	private ScanSdReceiver scanSdReceiver = null;
	private AudioManager mAudioManager = null;
	private int maxVolume;//最大音量
	private int currentVolume;//当前音量
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		String[] strs = {"全部音乐","最近播放列表","最经常播放列表"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1,strs);
		listview = new ListView(this);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				switch (position) {
				case 0:
			        intent = new Intent();
					intent.setClass(MainActivity.this, TestMain.class);
					startActivity(intent);
					finish();
					break;

				case 1:
					intent = new Intent();
					intent.setClass(MainActivity.this, RecentlyActivity.class);
					startActivity(intent);
					finish();
					break;
				case 2:
					intent = new Intent();
					intent.setClass(MainActivity.this, ClicksActivity.class);
					startActivity(intent);
					finish();
					break;
				}
				
			}
		});
	    mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE); 
		maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//获得最大音量  
		currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);//获得当前音量 
		this.setContentView(listview);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, ITEM1, 0, "刷新库");
		menu.add(0, ITEM2, 0, "退出");
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()){
			case ITEM1:
				scanSdCard();
				break;
			case ITEM2:
				if (scanSdReceiver!=null)
      				unregisterReceiver(scanSdReceiver);
				this.finish();
				Intent intent = new Intent();
				intent.setAction("com.alex.media.MUSIC_SERVICE");
				stopService(intent);
				break;
			default:
				break;
		}
		return true;
	}
	
	 private void scanSdCard(){
	    	IntentFilter intentfilter = new IntentFilter( Intent.ACTION_MEDIA_SCANNER_STARTED);
	    	intentfilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
	    	intentfilter.addDataScheme("file");
	    	scanSdReceiver = new ScanSdReceiver();
	    	registerReceiver(scanSdReceiver, intentfilter);
	    	sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
	    			Uri.parse("file://" + Environment.getExternalStorageDirectory().getAbsolutePath())));
	    }
	 public boolean dispatchKeyEvent(KeyEvent event) { 
			int action = event.getAction(); 
			int keyCode = event.getKeyCode(); 
			switch (keyCode) { 
				case KeyEvent.KEYCODE_VOLUME_UP: 
				if (action == KeyEvent.ACTION_UP) {
					if (currentVolume<maxVolume){
						currentVolume = currentVolume + 1;
						mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
					} else {
						mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
					}
				} 
				return false; 
				case KeyEvent.KEYCODE_VOLUME_DOWN: 
				if (action == KeyEvent.ACTION_UP) { 
					if (currentVolume>0){
						currentVolume = currentVolume - 1;
						mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume , 0);
					} else {
						mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
					}
				} 
				return false; 
				default: 
				return super.dispatchKeyEvent(event); 
			} 
		}
	 
}
