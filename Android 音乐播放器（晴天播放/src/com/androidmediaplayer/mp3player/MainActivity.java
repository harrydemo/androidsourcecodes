package com.androidmediaplayer.mp3player;

import android.app.ActivityManager;
import android.app.TabActivity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

import com.androidmediaplayer.R;
import com.androidmediaplayer.constant.AppConstant;
import com.androidmediaplayer.constant.ConstantExtendsApplication;
import com.androidmediaplayer.model.DownloadTask;

public class MainActivity extends TabActivity{
	
	private ConstantExtendsApplication cea = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mediaplayer_main_320_480);
		cea = ((ConstantExtendsApplication) getApplicationContext());
		
		TabHost tabHost = getTabHost();
		Resources res = getResources();
		
		Intent remoteIntent = new Intent();
		remoteIntent.setClass(this, RemoteMp3ListActivity.class);
		TabHost.TabSpec remoteSpec = tabHost.newTabSpec("Remote");
		remoteSpec.setIndicator(res.getString(R.string.remote),res.getDrawable(android.R.drawable.stat_sys_download_done));
		remoteSpec.setContent(remoteIntent);
		tabHost.addTab(remoteSpec);
		
		Intent localIntent = new Intent(); 
		localIntent.setClass(this, LocalMp3ListActivity.class);
		TabHost.TabSpec localSpec = tabHost.newTabSpec("Local");
		localSpec.setIndicator(res.getString(R.string.local), res.getDrawable(android.R.drawable.stat_sys_upload_done));
		localSpec.setContent(localIntent);
		tabHost.addTab(localSpec);
		
		Intent playListIntent = new Intent();
		playListIntent.setClass(this, PlayListActivity.class);
		TabHost.TabSpec playSpec = tabHost.newTabSpec("PlayList");
		playSpec.setIndicator(res.getString(R.string.playlist), res.getDrawable(android.R.drawable.ic_media_play));
		playSpec.setContent(playListIntent);
		tabHost.addTab(playSpec);
		
		// 启动播放器服务
		startService(new Intent(this, MediaPlayerService.class));
		
		SharedPreferences sp = getSharedPreferences("boot",MODE_PRIVATE);
		if(sp.getBoolean("first", true)){
			new Thread(new Addshorcut()).start();
		}
	}

	class Addshorcut implements Runnable{

		@Override
		public void run() {
			addShortcut();
		}
		
		/**
		 * 添加桌面快捷方式
		 */
		private void addShortcut() {
			SharedPreferences sp = getSharedPreferences("boot", MODE_PRIVATE);
			Intent shortcut = new Intent(
					"com.android.launcher.action.INSTALL_SHORTCUT");
			shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,
					getString(R.string.app_name));
			shortcut.putExtra("duplicate", false);
			Intent intent2 = new Intent(Intent.ACTION_MAIN);
			intent2.addCategory(Intent.CATEGORY_LAUNCHER);
			intent2.setComponent(new ComponentName(MainActivity.this
					.getPackageName(), "."
					+ MainActivity.this.getLocalClassName()));
			shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent2);
			ShortcutIconResource iconRes = Intent.ShortcutIconResource
					.fromContext(MainActivity.this, R.drawable.appwidget_played);
			shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
			sendBroadcast(shortcut);
			Editor editor = sp.edit();
			editor.putBoolean("first", false);
			editor.commit();
			// 播放模式默认单曲循环
			SharedPreferences playModeSP = getSharedPreferences("playMode",
					MODE_PRIVATE);
			Editor playModeEditor = playModeSP.edit();
			playModeEditor.putInt("playMode",
					AppConstant.PlayMode.MODE_SINGLE_REPEAT);
			playModeEditor.commit();
		}
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		System.out.println("onResume");
//		Intent intent = null;
//		intent = getIntent();
//		System.out.println(intent);
//		if(null != intent){
//            if(null != intent.getSerializableExtra("downloadTask")){
//                System.out.println(intent);
//                DownloadTask downloadTask = (DownloadTask) intent.getSerializableExtra("downloadTask");
//                System.out.println(downloadTask);
//                cea.downloadTasks.add(downloadTask);
//            }
//        }
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		System.out.println("onNewIntent");
		if(null != intent){
			if(null != intent.getSerializableExtra("downloadTask")){
				DownloadTask downloadTask = (DownloadTask) intent.getSerializableExtra("downloadTask");
				System.out.println(downloadTask);
				cea.downloadTasks.add(downloadTask);
				System.out.println("栈的长度"+cea.downloadTasks.size());
				System.out.println("栈顶"+cea.downloadTasks.peek());
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		finish();
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		// API Level至少为8才能使用
		am.killBackgroundProcesses("com.androidmediaplayer"); 
	}
	
}
