package com.androidmediaplayer.mp3player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.androidmediaplayer.R;
import com.androidmediaplayer.constant.ConstantExtendsApplication;
import com.androidmediaplayer.model.DownloadTask;
import com.androidmediaplayer.model.Mp3Info;
import com.androidmediaplayer.model.PlayList;
import com.androidmediaplayer.mp3player.service.DownloadService;
import com.androidmediaplayer.mp3player.service.ScanService;
import com.androidmediaplayer.utils.FileUtil;
import com.androidmediaplayer.utils.LinkedListPlayList;
import com.androidmediaplayer.utils.PlayListMapUtil;
import com.androidmediaplayer.utils.PlayListUtil;
import com.androidmediaplayer.utils.StrTimeUtil;

public class LocalMp3ListActivity extends Activity {

	// 扫描本地音乐
	private final int MENU_SCAN = 1;
	// 退出
	private final int MENU_EXIT = 3;
	
	private final int MENU_VOLUME = 2;
	
	private ConstantExtendsApplication cea = null;
	
	private final int DELETE_FROM_PHONE = 1;
	private final int ADD_TO_PLAYLIST = 0;
	
	private int groupPos;
	private int childPos;
	
	private ArrayList<PlayList> playLists = null;
	private final int NEW_DOWNLOAD_GROUP = 0;
	private final int ALL_MUSIC_GROUP = 1;
	
	private ArrayList<Mp3Info> newDownlad_group = null;
	private ArrayList<Mp3Info> all_songs_group = null;
	private ArrayList<ArrayList<Mp3Info>> all = null;
	
	private ScanSdReceiver scanSdReceiver = null;
	private ExpandableListView e = null;
	private boolean goFlag = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mediaplayer_localmp3list_320_480);
		
		ImageView iv = (ImageView)findViewById(R.id.localbg);
        iv.getBackground().setAlpha(210);
		
		View v = findViewById(R.id.local_bottom_player);
        v.setVisibility(View.INVISIBLE);
        
        setListener();
		registerForContextMenu(e);
		cea = ((ConstantExtendsApplication) getApplicationContext());
		updateList();
		
	}
	
	@Override
    protected void onResume() {
        super.onResume();
        if(cea.isRefresh()){
            scanSdCard();
            cea.setRefresh(false);
        }
        if(cea.isRefreshOnly()){
            updateList();
            cea.setRefreshOnly(false);
        }
        if(cea.isHadInitPlayer() && goFlag){
            goFlag = false;
            View v = findViewById(R.id.local_bottom_player);
            v.setVisibility(View.VISIBLE);
        }
        if(!cea.downloadTasks.isEmpty()){
            DownloadTask downloadTask = cea.downloadTasks.pop();
            int groupPosition = downloadTask.getGroupIndex();
            int childPosition = downloadTask.getChildIndex();
            
            Set<Integer> keys = RemoteMp3ListActivity.downloadTasks.keySet();
            Iterator<Integer> iter = keys.iterator();
            
            while(iter.hasNext()){
                Integer key = iter.next();
                downloadTask = RemoteMp3ListActivity.downloadTasks.get(key);
                if (downloadTask.getGroupIndex() == groupPosition && downloadTask.getChildIndex() == childPosition) {
                    System.out.println("remote key: "+downloadTask.getTaskIndex());
                    if(downloadTask.getState_download() == DownloadTask.STATE_DONE){
                        alreadyDialog(getText(R.string.downloadSuccess).toString(), R.array.exsit_dialog_items, downloadTask);
                    }else if(downloadTask.getState_download() == DownloadTask.STATE_DOWNLOADING){
                        comeFromNoti_downloading_Dialog(downloadTask,groupPosition,childPosition);
                    }
                    break;
                }
            }
        }
    }

	// 用户点击menu按钮之后，会调用该方法。
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuItem item = menu.add(0, MENU_SCAN, MENU_SCAN, R.string.scan);
	    item.setIcon(R.drawable.icon_menu_update_file);
//		menu.add(0, MENU_VOLUME, MENU_VOLUME, R.string.volume);
	    MenuItem item2 = menu.add(0, MENU_EXIT, MENU_EXIT, R.string.exit);
	    item2.setIcon(R.drawable.icon_menu_exit);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_SCAN:
			scanSdCard();
			break;
		case MENU_EXIT:
			exitDialog();
			break;
		case MENU_VOLUME:
			//########################################################################################################
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) menuInfo;
		groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition);
		childPos = ExpandableListView.getPackedPositionChild(info.packedPosition);
		String title = "";
		int type = ExpandableListView.getPackedPositionType(info.packedPosition);
		if(type == ExpandableListView.PACKED_POSITION_TYPE_CHILD){
			title = ((TextView) (info.targetView.findViewById(R.id.mp3_name)))
					.getText().toString()
					+ " - "
					+ ((TextView) (info.targetView
							.findViewById(R.id.mp3_artist))).getText()
							.toString();
			menu.setHeaderTitle(title);
			menu.add(0, ADD_TO_PLAYLIST, ADD_TO_PLAYLIST, R.string.addtoplaylist);
			menu.add(0, DELETE_FROM_PHONE, DELETE_FROM_PHONE, R.string.delete_from_phone);
		}
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		switch (itemId) {
		case ADD_TO_PLAYLIST:
			Mp3Info mp3Info = all.get(groupPos).get(childPos);
			addToPlayListDialog(Integer.valueOf(mp3Info.getAudio_id()), null, mp3Info);
			break;
		case DELETE_FROM_PHONE:
			try {
				FileUtil.deleteAnotherFile(all.get(groupPos).get(childPos).getPath());
				PlayListMapUtil plmu = new PlayListMapUtil(getApplicationContext());
				plmu.deleteByAudioId(Integer.valueOf(all.get(groupPos).get(childPos).getAudio_id()));
//				if(null != downloadTask && downloadTask.getState_download() == DownloadTask.STATE_DONE){
//					Intent intent = new Intent(getApplicationContext(),DownloadService.class);
//					intent.putExtra("taskIndex", downloadTask.getTaskIndex());
//					startService(intent);
//				}
				scanSdCard();
				cea.setRefreshPlayList(true);
			} catch (IOException e) {
				e.printStackTrace();
				Toast.makeText(this, R.string.delete_fail, Toast.LENGTH_SHORT).show();
			}
			break;
		}
		return super.onContextItemSelected(item);
	}
	
	/**
	 * 添加到播放列表
	 * @param audio_id
	 */
	private void addToPlayListDialog(final int audio_id, DownloadTask downloadTask, final Mp3Info mp3Info){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.dialog_choiceplease);
		builder.setSingleChoiceItems(getSimpleAdapter(), -1,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						PlayListMapUtil playListMapUtil = new PlayListMapUtil(getApplicationContext());
						int playListId = playLists.get(which).getPlayList_id();
						playListMapUtil.save(audio_id, playListId);
						//如果要添加到的列表 刚好 和当前播放的列表相同，则把该歌曲同时加到播放链表末尾
						if(playListId == LinkedListPlayList.playListId && !LinkedListPlayList.getPlayListMp3Infos().isEmpty()){
							LinkedListPlayList.add(mp3Info);
						}
						Toast.makeText(getApplicationContext(), R.string.msg_addtolist, Toast.LENGTH_SHORT).show();
						cea.setRefreshPlayList(true);
						dialog.dismiss();
					}
				});
		builder.setNegativeButton(R.string.btn_cancel,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		if(null != downloadTask && downloadTask.getState_download() == DownloadTask.STATE_DONE){
			Intent intent = new Intent(getApplicationContext(),DownloadService.class);
			intent.putExtra("taskIndex", downloadTask.getTaskIndex());
			startService(intent);
		}
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	/**
	 * 取得弹出选得的播放列表适配器
	 * @return
	 */
	private SimpleAdapter getSimpleAdapter() {
		ArrayList<HashMap<String, String>> dialog_playList = new ArrayList<HashMap<String, String>>();
		PlayListUtil playListUtil = new PlayListUtil(this);
		playLists = playListUtil.findAll();
		int size = playLists.size();
		HashMap<String, String> hm = null;
		for (int i = 0; i < size; i++) {
			hm = new HashMap<String, String>();
			hm.put("listName", playLists.get(i).getPlayList_name());
			hm.put("listId", String.valueOf(playLists.get(i).getPlayList_id()));
			dialog_playList.add(hm);
		}
		SimpleAdapter sAdapter = new SimpleAdapter(this, dialog_playList,
				R.layout.mediaplayer_playlistitem_320_480,
				new String[] { "listName" }, new int[] { R.id.playlist_name });
		return sAdapter;
	}

//	@Override
//	public boolean onChildClick(ExpandableListView parent, View v,
//			int groupPosition, int childPosition, long id) {
//		switch (groupPosition) {
//		case NEW_DOWNLOAD_GROUP:
//			goToPlay(newDownlad_group, childPosition);
//			break;
//		case ALL_MUSIC_GROUP:
//			goToPlay(all_songs_group, childPosition);
//			break;
//		}
//		return super.onChildClick(parent, v, groupPosition, childPosition, id);
//	}
	
	private void setListener(){
	    e = (ExpandableListView)findViewById(R.id.localexlist);
	    e.setOnChildClickListener(new OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                    int groupPosition, int childPosition, long id) {
                switch (groupPosition) {
                case NEW_DOWNLOAD_GROUP:
                    LinkedListPlayList.clear();
                    LinkedListPlayList.addAll(newDownlad_group);
                    goToPlay(newDownlad_group, childPosition);
                    break;
                case ALL_MUSIC_GROUP:
                    LinkedListPlayList.clear();
                    LinkedListPlayList.addAll(all_songs_group);
                    goToPlay(all_songs_group, childPosition);
                    break;
                }
                return false;
            }
        });
	    ImageButton ib = (ImageButton)findViewById(R.id.localgotoplayer);
	    ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyPlayerActivity.class);
                intent.putExtra("mp3Info", cea.getCurrentPlayingMp3Info());
                startActivity(intent);
            }
        });
	}
	
	private void goToPlay(ArrayList<Mp3Info> mp3Infos, int position) {
		if (null != mp3Infos && 0 != mp3Infos.size()) {
			Mp3Info mp3Info = mp3Infos.get(position);
			//有可能被其他应用软件删除了文件
			if(FileUtil._isFileExist2(mp3Info.getPath())){
			    Intent intent = new Intent();
	            intent.putExtra("mp3Info", mp3Info);
	            intent.setClass(this, MyPlayerActivity.class);
	            //点击播放后默认添加到当前播放列表
	            PlayListMapUtil playListMapUtil = new PlayListMapUtil(this);
	            playListMapUtil.save(Integer.valueOf(mp3Info.getAudio_id()), 0);
	            cea.setRefreshPlayList(true);
	            startActivity(intent);
			}else{
			    //更新媒体库和UI
			    scanSdCard();
	            cea.setRefresh(false);
			}
		}
	}
	
	/**
	 * 退出
	 */
	private void exit() {
		Intent intent = new Intent();
		intent.setClass(this, MediaPlayerService.class);
		stopService(intent);
		intent.setClass(this, DownloadService.class);
		stopService(intent);
		intent.setClass(this, ScanService.class);
		stopService(intent);
		finish(); 
	}

	/**
	 * 退出询问窗口
	 */
	private void exitDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage(R.string.choice_exit);
		builder.setTitle(R.string.dialog_tip);
		builder.setPositiveButton(R.string.btn_confirm, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				exit();
			}
		});
		builder.setNegativeButton(R.string.btn_cancel, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	/**
	 * 查询所有音乐
	 * @param selection
	 * @param selectionArgs
	 * @param sortOrder
	 * @return
	 */
	private ArrayList<Mp3Info> getMp3Infos(String selection, String[] selectionArgs,
			String sortOrder) {
		ArrayList<Mp3Info> mp3Infos = new ArrayList<Mp3Info>();
		Cursor c = this.getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				new String[] { MediaStore.Audio.Media.TITLE,
						MediaStore.Audio.Media.DATA,
						MediaStore.Audio.Media.ARTIST,
						MediaStore.Audio.Media.DURATION,
						MediaStore.Audio.Media._ID}, selection,
				selectionArgs, sortOrder);
		int length = 0;
		length = c.getCount();
		if (null != c && 0 != length) {
			c.moveToFirst();
			Mp3Info mp3info = null;
			for (int i = 0; i < length; i++) {
                mp3info = new Mp3Info();
                // new String(c.getString(0).getBytes("ISO-8859-1"),"UTF-8")
                // new String(c.getBlob(0),"UTF-8")
                // 也是不能解决乱码问题，一定要修改音乐文件属性？？
                mp3info.setMp3Name(c.getString(0));
//                try {
//                    mp3info.setMp3Name(new String(c.getBlob(0),"utf16"));
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    mp3info.setMp3Name(new String(c.getString(0).getBytes("ISO-8859-1"),"UTF-8"));
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
                mp3info.setPath(c.getString(1));
				mp3info.setArtist(c.getString(2));
				mp3info.setDuration(c.getString(3));
				mp3info.setAudio_id(String.valueOf(c.getInt(4)));
				mp3Infos.add(mp3info);
				c.moveToNext();
			}
			return mp3Infos;
		}else{
			return null;
		}
		
	}

	/**
	 * 更新UI
	 * 对此方法性能表示担忧.....
	 */
	private void updateList() {
		ArrayList<HashMap<String, String>> father = null;
		ArrayList<ArrayList<HashMap<String, String>>> child = null;
		all = new ArrayList<ArrayList<Mp3Info>>();
		if (FileUtil.isFilePathExist("mp3")) {
			
			child = new ArrayList<ArrayList<HashMap<String, String>>>();
			father = new ArrayList<HashMap<String, String>>();
			
			ArrayList<HashMap<String, String>> temp = null;
			HashMap<String, String> fatherHashMap = new HashMap<String, String>();
			fatherHashMap.put("father", getText(R.string.new_download).toString());
			// 查出在/sdcard/mp3/目录下的歌曲
			newDownlad_group = getMp3Infos("_data like '%/sdcard/mp3/%' and _display_name like '%.mp3'", null, null);
			if(null != (temp = getChildren(newDownlad_group))){
				fatherHashMap.put("count", "("+newDownlad_group.size()+")");
			}else{
				temp = new ArrayList<HashMap<String, String>>();
				fatherHashMap.put("count", "(0)");
			}
			child.add(temp);
			father.add(fatherHashMap);
			
			ArrayList<HashMap<String, String>> temp2 = null;
			HashMap<String, String> fatherHashMap2 = new HashMap<String, String>();
			fatherHashMap2.put("father", getText(R.string.allsongs).toString());
			// 查出所有歌曲
			all_songs_group = getMp3Infos("_display_name like '%.mp3'", null, null);
			if(null != (temp2 = getChildren(all_songs_group))){
				fatherHashMap2.put("count", "("+all_songs_group.size()+")");
			}else{
			    temp2 = new ArrayList<HashMap<String, String>>();
				fatherHashMap2.put("count", "(0)");
			}
			child.add(temp2);
			father.add(fatherHashMap2);
			all.add(newDownlad_group);
			all.add(all_songs_group);
			SimpleExpandableListAdapter exAdapter = new SimpleExpandableListAdapter(
					this, father,
					R.layout.mediaplayer_expandablegroupitem_320_480,
					new String[] { "father", "count" }, new int[] { R.id.group_name, R.id.count }, child,
					R.layout.mediaplayer_mp3infoitem_320_480, new String[] {
							"mp3_name", "mp3_size", "mp3_artist" }, new int[] {
							R.id.mp3_name, R.id.mp3_size, R.id.mp3_artist });
//			setListAdapter(exAdapter);
			e.setAdapter(exAdapter);
		}

	}
	
	private ArrayList<HashMap<String, String>> getChildren(ArrayList<Mp3Info> mp3Infos){
		if(null == mp3Infos || 0 == mp3Infos.size()){
			return null;
		}
		ArrayList<HashMap<String, String>> children = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> childrenData = null;
		int size = mp3Infos.size();
		Mp3Info mp3Info = null;
		for (int i = 0; i < size; i++) {
			mp3Info = mp3Infos.get(i);
			childrenData = new HashMap<String, String>();
			childrenData.put("mp3_name", mp3Info.getMp3Name());
			childrenData.put("mp3_size", StrTimeUtil.longTimeToString(Integer.valueOf(mp3Info.getDuration())));
			childrenData.put("mp3_artist", mp3Info.getArtist());
			children.add(childrenData);
		}
		return children;
	}

	//正在扫描中，不能再发送扫描广播
	private boolean scanning = false;
	
	/**
	 * 通知系统扫描多媒体文件
	 */
	private void scanSdCard() {
	    if(!scanning){
	        scanning = true;
	        IntentFilter intentfilter = new IntentFilter(Intent.ACTION_MEDIA_SCANNER_STARTED);
	        intentfilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
	        intentfilter.addDataScheme("file");
	        scanSdReceiver = new ScanSdReceiver();
	        registerReceiver(scanSdReceiver, intentfilter);
	        sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
	                Uri.parse("file://" + Environment.getExternalStorageDirectory().getAbsolutePath())));
	    }
	}

	class ScanSdReceiver extends BroadcastReceiver {
		private ProgressDialog progressDialog = null;
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Intent.ACTION_MEDIA_SCANNER_STARTED.equals(action)) {
				progressDialog = ProgressDialog.show(LocalMp3ListActivity.this,
						"", getResources().getString(R.string.loading), true);
			} else if (Intent.ACTION_MEDIA_SCANNER_FINISHED.equals(action)) {
				updateList();
				progressDialog.cancel();
				unregisterReceiver(scanSdReceiver);
				scanning = false;
			}
		}
	}

	@Override
	public void onBackPressed() {
		exitDialog();
	}
	
	private final int PLAYNOW_CODE = 0;
	private final int ADDTOLIST_CODE = 1;
	/**
	 * 文件已经存在或者下载完毕窗口
	 * @param groupPosition
	 * @param childPosition
	 */
	private void alreadyDialog(String title, int dialogItemMSGs, final DownloadTask downloadTask) {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle(title);
		builder.setItems(dialogItemMSGs, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String fileName = downloadTask.getMp3Info().getMp3Name() + ".mp3";
				switch (which) {
				case PLAYNOW_CODE:
					//避免下载完被删除但通知栏仍在的情况出错
					if(FileUtil._isFileExist(fileName)){
						playNow(downloadTask.getMp3Info());
					}else{
						Toast.makeText(getApplicationContext(), R.string.hadnotexist, Toast.LENGTH_SHORT).show();
					}
					break;
				case ADDTOLIST_CODE:
					//避免下载完被删除但通知栏仍在的情况出错
					if(FileUtil._isFileExist(fileName)){
						Mp3Info mp3Info = downloadTask.getMp3Info();
						String audioId = getAudioId(mp3Info, null);
						mp3Info.setAudio_id(audioId);
						addToPlayListDialog(Integer.valueOf(audioId), downloadTask, mp3Info);
					}else{
						Toast.makeText(getApplicationContext(), R.string.hadnotexist, Toast.LENGTH_SHORT).show();
					}
					break;
				}
			}
		});
		builder.setNegativeButton(R.string.btn_cancel, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				dialog.dismiss();
			}
		});
//		if(null != downloadTask && downloadTask.getState_download() == DownloadTask.STATE_DONE){
//			Intent intent = new Intent(getApplicationContext(),DownloadService.class);
//			intent.putExtra("taskIndex", downloadTask.getTaskIndex());
//			startService(intent);
//		}
		builder.create().show();
	}
	
	private void playNow(Mp3Info mp3Info){
		String path = FileUtil.getFilePath(mp3Info.getMp3Name() + ".mp3");
		mp3Info.setAudio_id(getAudioId(mp3Info, path));
		mp3Info.setPath(path);
		Intent intent = new Intent(this,MyPlayerActivity.class);
		intent.putExtra("mp3Info", mp3Info);
		//点击播放后默认添加到最近播放列表
		PlayListMapUtil playListMapUtil = new PlayListMapUtil(this);
		playListMapUtil.save(Integer.valueOf(mp3Info.getAudio_id()), 0);
		cea.setRefreshPlayList(true);
		startActivity(intent);
	}
	
	/**
	 * 如果路径为空则按歌名查找
	 * 
	 * @param mp3Info
	 * @param path
	 * @return
	 */
	private String getAudioId(Mp3Info mp3Info, String path) {
		String id = mp3Info.getAudio_id();
		String pathStr = path;
		if (id.contains(".")) {
			if (null == pathStr) {
				pathStr = FileUtil.getFilePath(mp3Info.getMp3Name() + ".mp3");
			}
			Cursor c = getContentResolver().query(
					MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
					new String[] { MediaStore.Audio.AudioColumns._ID },
					"_data=?", new String[] { pathStr }, null);
			c.moveToFirst();
			return String.valueOf(c.getInt(0));
		}
		return id;
	}
	
	private void comeFromNoti_downloading_Dialog(final DownloadTask downloadTask, int group, int child) {
		Mp3Info mp3Info = downloadTask.getMp3Info();
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle(mp3Info.getMp3Name()+" - "+mp3Info.getArtist());
		String[] temps = new String[]{getText(R.string.stop_download).toString()};
		builder.setItems(temps, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(getApplicationContext(),DownloadService.class);
				intent.putExtra("taskIndex", downloadTask.getTaskIndex());
				startService(intent);
			}
		});
		builder.setNegativeButton(R.string.btn_cancel, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
	
	@Override
	protected void onDestroy() {
		//被回收后，设置需要刷新
		cea.setRefresh(true);
		super.onDestroy();
		finish();
	}

}
