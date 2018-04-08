package com.alex.media;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import com.alex.media.RecentlyActivity.ListItemClickListener;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
public class TestMain extends TabActivity implements TabHost.TabContentFactory{

	
	@Override
	protected void onStart() {
		IntentFilter filter = new IntentFilter();
        filter.addAction(MUSIC_LIST);
		registerReceiver(changeItem, filter);
		super.onStart();
	}
	private static final String MUSIC_LIST = "com.alex.list";
	private ListView listview;
	private int[] _ids;
	private String[] _titles;
	private String[] _artists;
	private String[] albums;
	private String[] artists;
	private String[] _path;									//�����ļ���·��
	private int pos;										//���ڲ������ֵ�λ��
	private int num;										//ѡ��ĸ�����λ��
	private  MusicListAdapter adapter;
	private ScanSdReceiver scanSdReceiver = null;
	private AlertDialog ad = null;
	private AlertDialog.Builder  builder = null;
	private Cursor c;
	private String tag;
	/*�����Ĳ˵���*/
	private static final int PLAY_ITEM = Menu.FIRST;
	private static final int DELETE_ITEM = Menu.FIRST+1;
	//���������ı���
	private AudioManager mAudioManager = null;
	private int maxVolume;//�������
	private int currentVolume;//��ǰ����
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		TabHost th = getTabHost();
		th.addTab(th.newTabSpec("list").setIndicator(
				"�����б�",getResources().getDrawable(R.drawable.item))
				.setContent(this));
		th.addTab(th.newTabSpec("artists").setIndicator(
				"������",getResources().getDrawable(R.drawable.artist))
				.setContent(this));
		th.addTab(th.newTabSpec("albums").setIndicator(
				"ר��",getResources().getDrawable(R.drawable.album))
				.setContent(this));
		th.addTab(th.newTabSpec("recent").setIndicator(
				"�������",getResources().getDrawable(R.drawable.album))
				.setContent(this));
        //�����Ƿ��������ڲ���
        Intent intent = new Intent();
        intent.setAction("com.alex.media.MUSIC_SERVICE");
        intent.putExtra("list", 1);
        startService(intent);
        

	}
	

	@Override
	public View createTabContent(String tag) {
		this.tag = tag;
		if (tag.equals("list")){
			listview = new ListView(this);
			setListData();
		    listview.setOnItemClickListener(new ListItemClickListener());
		    listview.setOnCreateContextMenuListener(new ContextMenuListener());
		} else if (tag.equals("artists")){
			c.moveToFirst();
			int num = c.getCount();
			HashSet set = new HashSet();
			for (int i = 0; i < num; i++){
				String szArtist = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST));
				if (szArtist.equals("<unknown>")){
					set.add("δ֪������");
				}else{
					set.add(szArtist);
				}
				c.moveToNext();
			}
			num = set.size();
			Iterator it = set.iterator();
			artists = new String[num];
			int i = 0;
			while(it.hasNext()){
				artists[i] = it.next().toString();
				i++;
			}
			/*����ÿ������ӵ�еĸ�����*/
			int counts[] = new int[num];
			//int n = 0;
			
			for(int j = 0; j<num; j++){
				c.moveToFirst();
				//String name = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST));
				for(int k = 0; k < c.getCount(); k++){
					String szArtist = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST)); 
					if (szArtist.equals("<unknown>"))
					{
						szArtist = "δ֪������";
					}
					if (artists[j].equals(szArtist)){
						counts[j]++;
						//n++;
					}
					c.moveToNext();
				}
			}
			

			listview = new ListView(this);
			listview.setAdapter(new ArtistListAdapter(this, artists,counts));
			listview.setOnItemClickListener(new ArtistsItemClickListener());
		} else if (tag.equals("albums")){
			Cursor c = this.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
					new String[]{MediaStore.Audio.Media.TITLE,
					MediaStore.Audio.Media.DURATION,
					MediaStore.Audio.Media.ARTIST,
					MediaStore.Audio.Media.ALBUM,
					MediaStore.Audio.Media._ID,
					MediaStore.Audio.Media.DISPLAY_NAME}, 
					null, 
					null,
					MediaStore.Audio.Media.ALBUM);
			c.moveToFirst();
			int num = c.getCount();
			HashSet set = new HashSet();
			for (int i = 0; i < num; i++){
				String szAlbum = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM));
				set.add(szAlbum);
				c.moveToNext();
			}
			num = set.size();
			Iterator it = set.iterator();
			albums = new String[num];
			int i = 0;
			while(it.hasNext()){
				albums[i] = it.next().toString();
				i++;
			}
			String album="";
			for (int j=0;j<num; j++){
				if (j<num-1){
					album = album + "'" + albums[j] + "',"; 
				} else{
					album = album + "'" + albums[j] + "'";
				}
			}
			
			Cursor c1 = this.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
					new String[]{
					MediaStore.Audio.Media.TITLE,
					MediaStore.Audio.Media.DURATION,
					MediaStore.Audio.Media.ARTIST,
					MediaStore.Audio.Media.ALBUM,
					MediaStore.Audio.Media._ID,
					MediaStore.Audio.Media.DISPLAY_NAME,
					}, 
					null,
					null,
					MediaStore.Audio.Media.ALBUM);
			c1.moveToFirst();
			HashMap<String, String> map = new HashMap<String, String>();
			int num1 = c1.getCount();
			for (int j=0;j<num1;j++){
				map.put(c1.getString(3), c1.getString(2));
				c1.moveToNext();
			}
			listview = new ListView(this);
			listview.setAdapter(new AlbumListAdapter(this, albums,map));
			listview.setOnItemClickListener(new AlbumsItemClickListener());
		}else if (tag.equals("recent")){
			DBHelper dbHelper = new DBHelper(this, "music.db", null, 2);
			Cursor cursor = dbHelper.queryRecently();
			cursor.moveToFirst();
			int num = 0;
			int[] music_id;
			if (cursor!=null){
				num = cursor.getCount();
			} else{
				return null;
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
			listview.setCacheColorHint(00000000);
			Cursor c = this.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
	        			new String[]{MediaStore.Audio.Media.TITLE,
						MediaStore.Audio.Media.DURATION,
						MediaStore.Audio.Media.ARTIST,
						MediaStore.Audio.Media._ID,
						MediaStore.Audio.Media.DISPLAY_NAME,
						MediaStore.Audio.Media.ALBUM_ID,} , MediaStore.Audio.Media._ID + " in ("+ idString + ")", null,null);
			
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
		}
		listview.setCacheColorHint(00000000);
        
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE); 
		maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//����������  
		currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);//��õ�ǰ���� 
		LinearLayout list = new LinearLayout(this);
		list.setBackgroundResource(R.drawable.listbg);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
        LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        list.removeAllViews();
        list.addView(listview,params);
		return list;
	}
	
	/*��������*/
	private void playMusic(int position){
		Intent intent = new Intent(TestMain.this,MusicActivity.class);
		intent.putExtra("_ids", _ids);
		intent.putExtra("_titles", _titles);
		intent.putExtra("_artists", _artists);
		intent.putExtra("position", position);
		startActivity(intent);
		finish();
	}
	
	/*���б���ɾ��ѡ�е�����*/
	private void deleteMusic(int position){
		this.getContentResolver().delete(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, 
				MediaStore.Audio.Media._ID + "=" + _ids[position], 
				null);
	}
	
	/*��sdcard��ɾ��ѡ�е�����*/
	private void deleteMusicFile(int position){
		File file = new File(_path[position]);
		System.out.println(_path[position]);
		file.delete();
	}
	
	class ListItemClickListener implements OnItemClickListener{

    	@Override
    	public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
    		// TODO Auto-generated method stub
    		playMusic(position);
    	}
    	
    }
	class ArtistsItemClickListener implements OnItemClickListener{

    	@Override
    	public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
    		Intent intent = new Intent();
    		intent.setClass(TestMain.this, ArtistActivity.class);
    		intent.putExtra("artist", artists[position]);
    		startActivity(intent);
    	}
    }
	
	class AlbumsItemClickListener implements OnItemClickListener{

    	@Override
    	public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
    		Intent intent = new Intent();
    		intent.setClass(TestMain.this, AlbumActivity.class);
    		intent.putExtra("albums", albums[position]);
    		startActivity(intent);
    	}
    }
	
	
	
    
   
    
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == event.KEYCODE_BACK) {
//			if (scanSdReceiver!=null)
//      				unregisterReceiver(scanSdReceiver);
//			Intent intent = new Intent();
//			intent.setClass(this, MainActivity.class);
//			startActivity(intent);
//			finish();
//		}
//		return true;
//    }
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
    private BroadcastReceiver changeItem = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(MUSIC_LIST)){
				pos = intent.getExtras().getInt("position");
				adapter.setItemIcon(pos);//�ı��б���ͼ��
				adapter.notifyDataSetChanged();//֪ͨUI����
				System.out.println("List Update...");
			}
			
		}
	};
	
	private void setListData(){
		c = this.getContentResolver()
		.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				new String[]{
				MediaStore.Audio.Media.TITLE,
				MediaStore.Audio.Media.DURATION,
				MediaStore.Audio.Media.ARTIST,
				MediaStore.Audio.Media._ID,
				MediaStore.Audio.Media.DISPLAY_NAME,
				MediaStore.Audio.Media.DATA,
				MediaStore.Audio.Media.ALBUM_ID},
				null, null, null);
	    if (c==null || c.getCount()==0){
	    	builder = new AlertDialog.Builder(this);
			builder.setMessage("�洢�б�Ϊ��...").setPositiveButton("ȷ��", null);
			ad = builder.create();
			ad.show();
			return;
	    }
	    c.moveToFirst();
	    _ids = new int[c.getCount()];
	    _titles = new String[c.getCount()];
	    _artists = new String[c.getCount()];
	    _path = new String[c.getCount()];
	    for(int i=0;i<c.getCount();i++){
	    	_ids[i] = c.getInt(3);
	    	_titles[i] = c.getString(0);
	    	_artists[i] = c.getString(2);
	    	_path[i] = c.getString(5).substring(4);
	    	c.moveToNext();
	    }
	    adapter = new MusicListAdapter(this, c);
	    listview.setAdapter(adapter);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		unregisterReceiver(changeItem);
	}
	
	/*���������Ĳ˵�������*/
	class ContextMenuListener implements OnCreateContextMenuListener{
		@Override
		public void onCreateContextMenu(ContextMenu menu, View view,
				ContextMenuInfo info) {
			if (tag.equals("list")){
				menu.setHeaderTitle("����");
				menu.add(0, PLAY_ITEM, 0, "����");
				menu.add(0, DELETE_ITEM, 0, "ɾ��");
				final AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) info;
				num = menuInfo.position;
			}
		}
	}
	
	/*�����Ĳ˵���ĳһ����ʱ�ص��÷���*/
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case PLAY_ITEM:									//��ʼ����
			playMusic(num);
			break;

		case DELETE_ITEM:								//ɾ��һ�׸���
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("���Ҫɾ�����׸�����")
			.setPositiveButton("��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					deleteMusic(num);					//���б���ɾ������
					deleteMusicFile(num);				//��sdcard��ɾ������
					setListData();						//���»���б���ҩ��ʾ������
					adapter.notifyDataSetChanged();		//�����б�UI
				}
			})
			.setNegativeButton("��", null);
			AlertDialog ad = builder.create();
			ad.show();
			break;
		}
		return true;
	}
	
	
}
	
