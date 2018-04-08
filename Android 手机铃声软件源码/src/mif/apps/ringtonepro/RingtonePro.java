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
	//����������
	private MediaPlayer player = new MediaPlayer();
	/*���ݿ�*/
	MyDataBaseAdapter m_MyDatabaseAdapter;
	/*listview*/
	ListView mLib;
	
	String prePath = "/sdcard/";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        /*�������ݿ�*/
        m_MyDatabaseAdapter = new MyDataBaseAdapter(this);
        m_MyDatabaseAdapter.open();
        mLib = (ListView)findViewById(R.id.lib);
        UpdataAdapter();
        //ListView��������
        mLib.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String p = m_MyDatabaseAdapter.getPath(arg2+1);
				Log.i("ListView",p+" clicked");
				onStart(p);
				Toast.makeText( getApplicationContext (),"������������", Toast.LENGTH_SHORT ).show();
				
			}
		});
        
        mLib.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				//���ݿ�ID��1��ʼ��ListView��Item��ID��0��ʼ�����Լ�1��ʹ���ݿ���ListViewһһ��Ӧ
				final int id = arg2+1; 
				final String p = m_MyDatabaseAdapter.getPath(id);
				
				player.stop();
		    	  //�Ի���
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
        
        Toast.makeText(this, "��MENU���������", Toast.LENGTH_SHORT).show();
    }
    

    //����ʱ����Adapter
    @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		UpdataAdapter();
	}


//�ײ������˵�
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		menu.add(0, INPUT, 0, "��SD����������").setIcon(android.R.drawable.ic_menu_upload);
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
	
	/*���ݿ�list*/
	public void UpdataAdapter()
	{
		// ��ȡ���ݿ��Cursor
		Cursor cur = m_MyDatabaseAdapter.fetchAllData();

		
		
		if (cur != null && cur.getCount() >= 0)
		{
			Log.i("Database","done");
			// ListAdapter��ListView�ͺ�̨���ݵ�����
			ListAdapter adapter = new SimpleCursorAdapter(this,
			// ����List��ÿһ�е���ʾģ��
				// ��ʾÿһ�а�������������
				R.layout.liblist_item,
				// ���ݿ��Cursor����
				cur,
				// �����ݿ��TABLE_NUM��TABLE_DATA������ȡ����
				new String[] {MyDataBaseAdapter.TABLE_rNAME, MyDataBaseAdapter.TABLE_PATH },
				// ��NAME��PATH��Ӧ��Views
				new int[] { R.id.listitem_title, R.id.listitem_content });
			

			/* ��adapter��ӵ�m_ListView�� */
			mLib.setAdapter(adapter);
		}
	}

	
	
	
	
   //�����������
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

	
	//����--����
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
  //����--��ʾ��
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
  //����--������
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
    
    //���б����ݿ���ɾ������
    public void deleteitem(int id)
    {
    	m_MyDatabaseAdapter.deleteData(id);
    	Toast.makeText(this, "�����Ѵ��б����Ƴ�", Toast.LENGTH_SHORT).show();
    	m_MyDatabaseAdapter.updateID(id);
    	UpdataAdapter();
    	
    }
	 
	 
	
	//�ر�Activity����
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
	 * ���豸������Ϣ�иĶ���������Ļ����ĸı䣬ʵ����̵��ƿ�����ϵȣ�ʱ��
	 * ���������ʱ��activity�������У�ϵͳ��������������
	 * ע�⣺onConfigurationChangedֻ����Ӧ�ó�����AnroidMainifest.xml��ͨ��
	 * android:configChanges="xxxx"ָ�����������͵ĸĶ���
	 * �������������õĸ��ģ���ϵͳ��onDestroy()��ǰActivity��Ȼ������һ���µ�Activityʵ����
	 */
//	@Override
	public void onConfigurationChanged(Configuration newConfig) {	
		super.onConfigurationChanged(newConfig);
		// �����Ļ�ķ�����������
		if (this.getResources().getConfiguration().orientation 
				== Configuration.ORIENTATION_LANDSCAPE) {
			//��ǰΪ������ �ڴ˴���Ӷ���Ĵ������
		}
		else if (this.getResources().getConfiguration().orientation 
				== Configuration.ORIENTATION_PORTRAIT) {
			//��ǰΪ������ �ڴ˴���Ӷ���Ĵ������
		}
		//���ʵ����̵�״̬���Ƴ����ߺ���    
		if (newConfig.hardKeyboardHidden 
				== Configuration.HARDKEYBOARDHIDDEN_NO){ 
			//ʵ����̴����Ƴ�״̬���ڴ˴���Ӷ���Ĵ������
		} 
		else if (newConfig.hardKeyboardHidden
				== Configuration.HARDKEYBOARDHIDDEN_YES){ 
			//ʵ����̴��ں���״̬���ڴ˴���Ӷ���Ĵ������
		}
	}
}