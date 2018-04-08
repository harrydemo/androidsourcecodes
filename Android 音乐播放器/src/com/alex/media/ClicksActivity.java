package com.alex.media;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;

public class ClicksActivity extends Activity {
	private DBHelper dbHelper = null;
	private ListView listview;
	private int[] _ids;
	private String[]_titles;
	Cursor cursor = null;
	int[] music_id;
	//关于音量的变量
	private AudioManager mAudioManager = null;
	private int maxVolume;//最大音量
	private int currentVolume;//当前音量
	@Override
	protected void onStop() {
		super.onStop();
		if (dbHelper!=null){
			dbHelper.close();
			dbHelper = null;
		}
		if (cursor!=null){
			cursor.close();
			cursor = null;
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		dbHelper = new DBHelper(this, "music.db", null, 2);
		cursor = dbHelper.queryByClicks();
		cursor.moveToFirst();
		int num;
		if (cursor!=null){
			num = cursor.getCount();
		} else{
			return;
		}
		String idString ="";
		if (num>=10){
			for(int i=0;i<10;i++){
				music_id = new int[10];
				music_id[i]=cursor.getInt(cursor.getColumnIndex("music_id"));
				if (i<9){
					idString = idString+music_id[i]+",";
				} else{
					idString = idString+music_id[i];
				}
				cursor.moveToNext();
			} 
		}else if(num>0){
			for(int i=0;i<num;i++){
				music_id = new int[num];
				music_id[i]=cursor.getInt(cursor.getColumnIndex("music_id"));
				if (i<num-1){
					idString = idString+music_id[i]+",";
				} else{
					idString = idString+music_id[i];
				}
				cursor.moveToNext();
			}
		}
		if (cursor!=null){
			cursor.close();
			cursor=null;
		}
		if (dbHelper!=null){
			dbHelper.close();
			dbHelper = null;
		}
		listview = new ListView(this);
		Cursor c = this.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        			new String[]{MediaStore.Audio.Media.TITLE,
					MediaStore.Audio.Media.DURATION,
					MediaStore.Audio.Media.ARTIST,
					MediaStore.Audio.Media._ID,
					MediaStore.Audio.Media.DISPLAY_NAME} , MediaStore.Audio.Media._ID + " in ("+ idString + ")", null, null);
		
		  c.moveToFirst();
	        _ids = new int[c.getCount()];
	        _titles = new String[c.getCount()];
	        for(int i=0;i<c.getCount();i++){
	        	_ids[i] = c.getInt(3);
	        	_titles[i] = c.getString(0);
	        	c.moveToNext();
	        }
	        listview.setAdapter(new MusicListAdapter(this, c));
	        listview.setOnItemClickListener(new ListItemClickListener());
	        
	        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE); 
			maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//获得最大音量  
			currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);//获得当前音量 
			LinearLayout list = new LinearLayout(this);
			list.setBackgroundResource(R.drawable.listbg);
	        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
	        LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
	        list.addView(listview,params);
	        setContentView(list);
	    }
	   
	    class ListItemClickListener implements OnItemClickListener{

	    	@Override
	    	public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
	    		// TODO Auto-generated method stub
	    		Intent intent = new Intent(ClicksActivity.this,MusicActivity.class);
	    		intent.putExtra("_ids", _ids);
	    		intent.putExtra("_titles", _titles);
	    		intent.putExtra("position", position);
	    		
	    		startActivity(intent);
	    		finish();
	    	}
	    	
	    }
	    public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == event.KEYCODE_BACK) {
				Intent intent = new Intent();
				intent.setClass(this, MainActivity.class);
				startActivity(intent);
				finish();
			}
			return true;
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


