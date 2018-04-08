package mif.apps.ringtonepro;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class RingtonePro extends Activity {
	private static final int INPUT = 0 ;
	//试听播放器
	private MediaPlayer player = new MediaPlayer();
	/*数据库*/
	MyDataBaseAdapter m_MyDatabaseAdapter;
	/*listview*/
	ListView mLib;
	
	String prePath = "/sdcard/";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        /*创建数据库*/
        m_MyDatabaseAdapter = new MyDataBaseAdapter(this);
        m_MyDatabaseAdapter.open();
        mLib = (ListView)findViewById(R.id.lib);
        UpdataAdapter();
        //ListView监听设置
        mLib.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String p = m_MyDatabaseAdapter.getPath(arg2+1);
				Log.i("ListView",p+" clicked");
				onStart(p);
				Toast.makeText( getApplicationContext (),"长按进行设置", Toast.LENGTH_SHORT ).show();
				
			}
		});
        
        mLib.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				//数据库ID从1开始，ListView的Item的ID从0开始，所以加1，使数据库与ListView一一对应
				final int id = arg2+1; 
				final String p = m_MyDatabaseAdapter.getPath(id);
				
				player.stop();
		    	  //对话框
		    	  final CharSequence [] items = { getString(R.string.setRingtone) , getString(R.string.setNotification) , getString(R.string.setAlarm) , getString(R.string.delete) , getString(R.string.neverMind) }; 
		    	  
		    	  Dialog dialog = new AlertDialog.Builder(RingtonePro.this)
		    	   .setItems(items , new DialogInterface . OnClickListener () { 
		    		    public void onClick ( DialogInterface dialog , int item ) { 
		    		    	switch(item){
		    		    	    case 0:setMyRingtone(p);
		    		    	         break;
		    		    	    case 1:setMyNotification(p);
	   		    	                 break;
		    		    	    case 2:setMyAlarm(p);
	   		    	                 break;
		    		    	    case 3:deleteitem(id);
		    		    	    	 break;
		    		    	    case 4:
		    		    	    	 Toast.makeText( getApplicationContext (),R.string.neverMind, Toast.LENGTH_SHORT ).show();
	   		    	                 break;
		    		    	}
		    		    } 
		    		}).create();
		    	  dialog.show();
		    	  return false;
			}
		});
        
        Toast.makeText(this, "按MENU键添加铃声", Toast.LENGTH_SHORT).show();
    }
    

    //返回时更新Adapter
    @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		UpdataAdapter();
	}


//底部弹出菜单
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		menu.add(0, INPUT, 0, "从SD卡导入铃声").setIcon(android.R.drawable.ic_menu_upload);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item)
	{
		super.onOptionsItemSelected(item);
		switch (item.getItemId())
		{
			case INPUT:
				goInputFileActivity();
				break;
		}
		return false;
	}
	public void goInputFileActivity(){
		Intent intent = new Intent();
		intent.setClass(RingtonePro.this, InputFile.class);
		startActivity(intent);
	}
	
	/*数据库list*/
	public void UpdataAdapter()
	{
		// 获取数据库的Cursor
		Cursor cur = m_MyDatabaseAdapter.fetchAllData();

		
		
		if (cur != null && cur.getCount() >= 0)
		{
			Log.i("Database","done");
			// ListAdapter是ListView和后台数据的桥梁
			ListAdapter adapter = new SimpleCursorAdapter(this,
			// 定义List中每一行的显示模板
				// 表示每一行包含两个数据项
				R.layout.liblist_item,
				// 数据库的Cursor对象
				cur,
				// 从数据库的TABLE_NUM和TABLE_DATA两列中取数据
				new String[] {MyDataBaseAdapter.TABLE_rNAME, MyDataBaseAdapter.TABLE_PATH },
				// 与NAME和PATH对应的Views
				new int[] { R.id.listitem_title, R.id.listitem_content });
			

			/* 将adapter添加到m_ListView中 */
			mLib.setAdapter(adapter);
		}
	}

	
	
	
	
   //点击播放试听
	public void onStart(String p)
    {
			/*Uri path= Uri.parse(p);
    		 player.stop();
    	     player = MediaPlayer.create(this, path);
		     player.start();*/
		if(prePath.equals(p)&&player.isPlaying())
		{
			player.stop();
		}
		else
		{
			Uri path= Uri.parse(p);
			prePath = p;
   		 	player.stop();
   		 	player = MediaPlayer.create(this, path);
		    player.start();
		}
    	
    }

	
	//设置--铃声
    public void setMyRingtone(final String p)  
    {  
      player.stop();

      File sdfile = new File(p);
      Log.i("File",p);
      ContentValues values = new ContentValues();  
      values.put(MediaStore.MediaColumns.DATA, sdfile.getAbsolutePath());  
      values.put(MediaStore.MediaColumns.TITLE, sdfile.getName());
      values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/*");   
      values.put(MediaStore.Audio.Media.IS_RINGTONE, true);  
      values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);  
      values.put(MediaStore.Audio.Media.IS_ALARM, false);  
      values.put(MediaStore.Audio.Media.IS_MUSIC, false);  
      
      Uri uri = MediaStore.Audio.Media.getContentUriForPath(sdfile.getAbsolutePath());
      Uri newUri = this.getContentResolver().insert(uri, values);
      
 
      RingtoneManager.setActualDefaultRingtoneUri(this, RingtoneManager.TYPE_RINGTONE, newUri); 
      Toast.makeText( getApplicationContext (),R.string.setRingtoneSucceed, Toast.LENGTH_SHORT ).show();
      System.out.println("setMyRingtone()-------------");
    }  
  //设置--提示音
    public void setMyNotification(final String p)  
    { 
      player.stop();

      File sdfile = new File(p);    
      ContentValues values = new ContentValues();  
      values.put(MediaStore.MediaColumns.DATA, sdfile.getAbsolutePath());  
      values.put(MediaStore.MediaColumns.TITLE, sdfile.getName());
      values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/*");   
      values.put(MediaStore.Audio.Media.IS_RINGTONE, false);  
      values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);  
      values.put(MediaStore.Audio.Media.IS_ALARM, false);  
      values.put(MediaStore.Audio.Media.IS_MUSIC, false);  
      
      Uri uri = MediaStore.Audio.Media.getContentUriForPath(sdfile.getAbsolutePath());
      Uri newUri = this.getContentResolver().insert(uri, values);
      
 
      RingtoneManager.setActualDefaultRingtoneUri(this, RingtoneManager.TYPE_NOTIFICATION, newUri); 
      Toast.makeText( getApplicationContext (),R.string.setNotificationSucceed, Toast.LENGTH_SHORT ).show();
      System.out.println("setMyNOTIFICATION-------------");
    } 
  //设置--闹铃音
    public void setMyAlarm(final String p)  
    {  
      player.stop();

      File sdfile = new File(p);    
      ContentValues values = new ContentValues();  
      values.put(MediaStore.MediaColumns.DATA, sdfile.getAbsolutePath());  
      values.put(MediaStore.MediaColumns.TITLE, sdfile.getName());
      values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/*");   
      values.put(MediaStore.Audio.Media.IS_RINGTONE, false);  
      values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);  
      values.put(MediaStore.Audio.Media.IS_ALARM, true);  
      values.put(MediaStore.Audio.Media.IS_MUSIC, false);  
      
      Uri uri = MediaStore.Audio.Media.getContentUriForPath(sdfile.getAbsolutePath());
      Uri newUri = this.getContentResolver().insert(uri, values);
      
 
      RingtoneManager.setActualDefaultRingtoneUri(this, RingtoneManager.TYPE_ALARM, newUri); 
      Toast.makeText( getApplicationContext (),R.string.setAlarmSucceed, Toast.LENGTH_SHORT ).show();
      System.out.println("setMyNOTIFICATION-------------");
    } 
    
    //从列表数据库中删除铃声
    public void deleteitem(int id)
    {
    	m_MyDatabaseAdapter.deleteData(id);
    	Toast.makeText(this, "铃声已从列表中移除", Toast.LENGTH_SHORT).show();
    	m_MyDatabaseAdapter.updateID(id);
    	UpdataAdapter();
    	
    }
	 
	 
	
	//关闭Activity方法
	public void onDestroy() { 
		super.onDestroy();
        try{
        	this.finish();
        } 
        catch(Exception e){} 
    }
	/**
	 * onConfigurationChanged
	 * the package:android.content.res.Configuration.
	 * @param newConfig, The new device configuration.
	 * 当设备配置信息有改动（比如屏幕方向的改变，实体键盘的推开或合上等）时，
	 * 并且如果此时有activity正在运行，系统会调用这个函数。
	 * 注意：onConfigurationChanged只会监测应用程序在AnroidMainifest.xml中通过
	 * android:configChanges="xxxx"指定的配置类型的改动；
	 * 而对于其他配置的更改，则系统会onDestroy()当前Activity，然后重启一个新的Activity实例。
	 */
//	@Override
	public void onConfigurationChanged(Configuration newConfig) {	
		super.onConfigurationChanged(newConfig);
		// 检测屏幕的方向：纵向或横向
		if (this.getResources().getConfiguration().orientation 
				== Configuration.ORIENTATION_LANDSCAPE) {
			//当前为横屏， 在此处添加额外的处理代码
		}
		else if (this.getResources().getConfiguration().orientation 
				== Configuration.ORIENTATION_PORTRAIT) {
			//当前为竖屏， 在此处添加额外的处理代码
		}
		//检测实体键盘的状态：推出或者合上    
		if (newConfig.hardKeyboardHidden 
				== Configuration.HARDKEYBOARDHIDDEN_NO){ 
			//实体键盘处于推出状态，在此处添加额外的处理代码
		} 
		else if (newConfig.hardKeyboardHidden
				== Configuration.HARDKEYBOARDHIDDEN_YES){ 
			//实体键盘处于合上状态，在此处添加额外的处理代码
		}
	}
}