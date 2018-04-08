package com.androidmediaplayer.mp3player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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

public class PlayListActivity extends Activity{
	
	private Looper looper = null;
	private ConstantExtendsApplication cea = null;
	private ArrayList<PlayList> playLists = null;
	private PlayListUtil playListUtil = null;
	private ArrayList<ArrayList<HashMap<String, String>>> child = null;
	private final int DELETE_PLAYLIST = 0;
	private final int UPDATE_PLAYLIST = 1;
	private final int DELETE_FROM_PLAYLIST = 2;
	private final int ADD_TO_PLAYLIST = 3;
	
	private final int MENU_ADDPLAYLIST = 1;
	private final int MENU_EXIT = 3;
	private final int MENU_VOLUME = 2;
	
	private int groupPos;
	private int childPos;
	
	private ExpandableListView e = null;
    private boolean goFlag = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mediaplayer_playmp3list_320_480);
		
		ImageView iv = (ImageView)findViewById(R.id.playlistbg);
        iv.getBackground().setAlpha(210);
		
		View v = findViewById(R.id.play_bottom_player);
        v.setVisibility(View.INVISIBLE);
        
        setListener();
		
		looper = Looper.getMainLooper();
		cea = ((ConstantExtendsApplication) getApplicationContext());
		
		//长按需求需要的
		registerForContextMenu(e);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(cea.isRefreshPlayList()){
			updatePlayList();
		}
		if(cea.isHadInitPlayer() && goFlag){
            goFlag = false;
            View v = findViewById(R.id.play_bottom_player);
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
//			cea.setDownloadTask(null);
		}
		
	}
	
	private final int PLAYNOW_CODE = 0;
	private final int ADDTOLIST_CODE = 1;
	
	private void alreadyDialog(String title, int dialogItemMSGs, final DownloadTask downloadTask) {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle(title);
		builder.setItems(dialogItemMSGs, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case PLAYNOW_CODE:
					if(FileUtil._isFileExist(downloadTask.getMp3Info().getMp3Name()+".mp3")){
					    playNow(downloadTask.getMp3Info());
                    }else{
                        Toast.makeText(getApplicationContext(), R.string.hadnotexist, Toast.LENGTH_SHORT).show();
                    }
					break;
				case ADDTOLIST_CODE:
					if(FileUtil._isFileExist(downloadTask.getMp3Info().getMp3Name()+".mp3")){
					    Mp3Info mp3Info = downloadTask.getMp3Info();
	                    String audio_id = getAudioId(mp3Info, null);
	                    mp3Info.setAudio_id(audio_id);
	                    addToPlayListDialog(Integer.valueOf(audio_id), downloadTask, mp3Info);
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
		startActivity(intent);
	}
	
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
	
	/**
	 * 更新列表
	 */
	private synchronized void updatePlayList(){
		ProgressDialog progressDialog = ProgressDialog.show(this,
				"", getResources().getString(R.string.loading), true);
		new Thread(new UpdatePlayListThread(progressDialog)).start();
	}
	
	/**
	 * 长按后呼出的菜单
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		
		ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) menuInfo;
		groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition);
		childPos = ExpandableListView.getPackedPositionChild(info.packedPosition);
		String title = "";
		
		int type = ExpandableListView.getPackedPositionType(info.packedPosition);
		if(type == ExpandableListView.PACKED_POSITION_TYPE_GROUP && info.packedPosition != 0){
			title = ((TextView)(info.targetView.findViewById(R.id.group_name))).getText().toString();
			menu.setHeaderTitle(title);
			menu.add(0, DELETE_PLAYLIST, DELETE_PLAYLIST, R.string.delete_playlist);
			menu.add(0, UPDATE_PLAYLIST, UPDATE_PLAYLIST, R.string.update_playlist);
		} else if(type == ExpandableListView.PACKED_POSITION_TYPE_CHILD && !child.get(groupPos).get(childPos).isEmpty()){
			title = ((TextView) (info.targetView.findViewById(R.id.mp3_name)))
					.getText().toString()
					+ " - "
					+ ((TextView) (info.targetView
							.findViewById(R.id.mp3_artist))).getText()
							.toString();
			menu.setHeaderTitle(title);
			menu.add(0, DELETE_FROM_PLAYLIST, DELETE_FROM_PLAYLIST, R.string.delete_from_playlist);
			menu.add(0, ADD_TO_PLAYLIST, ADD_TO_PLAYLIST, R.string.addtoplaylist);
		}
		
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case DELETE_PLAYLIST:
			PlayListUtil playListUtil = new PlayListUtil(this);
			playListUtil.delete(playLists.get(groupPos).getPlayList_id());
			LinkedListPlayList.clear();
			updatePlayList();
			break;
		case UPDATE_PLAYLIST:
			showUpdateListNameDialog(playLists.get(groupPos).getPlayList_id(), playLists.get(groupPos).getPlayList_name());
			break;
		case DELETE_FROM_PLAYLIST:
			PlayListMapUtil playListMap = new PlayListMapUtil(this);
			playListMap.delete(Integer.valueOf(child.get(groupPos)
					.get(childPos).get("audio_id")), playLists.get(groupPos)
					.getPlayList_id());
			updatePlayList();
			break;
		case ADD_TO_PLAYLIST:
			HashMap<String, String> hashMap = child.get(groupPos).get(childPos);
			String audio_id = hashMap.get("audio_id");
			String name = hashMap.get("audio_name");
			String artist = hashMap.get("audio_artist");
			String size = hashMap.get("audio_size");
			String data = hashMap.get("audio_data");
			Mp3Info mp3Info = new Mp3Info();
			mp3Info.setAudio_id(audio_id);
			mp3Info.setMp3Name(name);
			mp3Info.setArtist(artist);
			mp3Info.setDuration(size);
			mp3Info.setPath(data);
			addToPlayListDialog(Integer.valueOf(audio_id), null, mp3Info);
		}
		return false;
	}
	
	/**
	 * 添加到播放列表
	 * @param audio_id
	 * @param downloadTask
	 */
	private void addToPlayListDialog (final Integer audio_id, DownloadTask downloadTask, final Mp3Info mp3Info){
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
						updatePlayList();
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
		AlertDialog alert = builder.create();
		alert.show();
		if(null != downloadTask && downloadTask.getState_download() == DownloadTask.STATE_DONE){
			Intent intent = new Intent(getApplicationContext(),DownloadService.class);
			intent.putExtra("taskIndex", downloadTask.getTaskIndex());
			startService(intent);
		}
	}
	
	/**
	 * 取得选择播放列表适配器
	 * @return
	 */
	private SimpleAdapter getSimpleAdapter() {
		ArrayList<HashMap<String, String>> dialog_playList = new ArrayList<HashMap<String, String>>();
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
	
	/**
	 * 更新播放列表名称
	 * @param playListId
	 * @param playListName
	 */
	private void showUpdateListNameDialog(final Integer playListId, String playListName){
		AlertDialog.Builder builder = new Builder(this);
		final EditText editText = new EditText(getApplicationContext());
		editText.setHint(playListName);
		builder.setPositiveButton(R.string.btn_confirm, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String editStr = editText.getText().toString();
				if(null != editStr && !"".equals(editStr.trim())){
					playListUtil.update(new PlayList(playListId, editStr));
					updatePlayList();
					dialog.dismiss();
				}else{
					Toast.makeText(getApplicationContext(), R.string.wrong_input, Toast.LENGTH_SHORT).show();
				}
			}
		});
		builder.setNegativeButton(R.string.btn_cancel, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.setView(editText);
		builder.create().show();
	}
	
//	@Override
//	public boolean onChildClick(ExpandableListView parent, View v,
//			int groupPosition, int childPosition, long id) {
//		Mp3Info mp3Info = new Mp3Info();
//		HashMap<String, String> temp = child.get(groupPosition).get(
//				childPosition);
//		if(!temp.isEmpty()){
//			mp3Info.setAudio_id(temp.get("audio_id"));
//			mp3Info.setArtist(temp.get("audio_artist"));
//			mp3Info.setMp3Name(temp.get("audio_name"));
//			mp3Info.setPath(temp.get("audio_data"));
//			Intent intent = new Intent(this, MyPlayerActivity.class);
//			intent.putExtra("mp3Info", mp3Info);
//			intent.putExtra("playListId", playLists.get(groupPosition).getPlayList_id());
//			//当前播放列表的指向
//			LinkedListPlayList.listPointer = childPosition;
//			startActivity(intent);
//		}
//		return super.onChildClick(parent, v, groupPosition, childPosition, id);
//	}
	
	private void setListener(){
	    e = (ExpandableListView)findViewById(R.id.playexlist);
	    e.setOnChildClickListener(new OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                    int groupPosition, int childPosition, long id) {
                Mp3Info mp3Info = new Mp3Info();
                HashMap<String, String> temp = child.get(groupPosition).get(
                        childPosition);
                if(!temp.isEmpty()){
                    mp3Info.setAudio_id(temp.get("audio_id"));
                    mp3Info.setArtist(temp.get("audio_artist"));
                    mp3Info.setMp3Name(temp.get("audio_name"));
                    mp3Info.setPath(temp.get("audio_data"));
                    Intent intent = new Intent(getApplicationContext(), MyPlayerActivity.class);
                    intent.putExtra("mp3Info", mp3Info);
                    intent.putExtra("playListId", playLists.get(groupPosition).getPlayList_id());
                    //当前播放列表的指向
                    LinkedListPlayList.listPointer = childPosition;
                    startActivity(intent);
                }
                return false;
            }
        });
	    ImageButton ib = (ImageButton)findViewById(R.id.playgotoplayer);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyPlayerActivity.class);
                intent.putExtra("mp3Info", cea.getCurrentPlayingMp3Info());
                startActivity(intent);
            }
        });
	}

	/**
	 * 更新列表UI线程
	 * @author Alex
	 *
	 */
	class UpdatePlayListThread implements Runnable{
		
		private ProgressDialog progressDialog = null;
		
		public UpdatePlayListThread(ProgressDialog progressDialog){
			this.progressDialog = progressDialog;
		}

		@Override
		public void run() {
			PlayListMapUtil playListMapUtil = new PlayListMapUtil(getApplicationContext());
			playListUtil = new PlayListUtil(getApplicationContext());
			playLists = playListUtil.findAll();
			ArrayList<Integer> mp3_ids = null;
			ContentResolver contentResolver = getContentResolver();
			Cursor cursor = null;
			
			ArrayList<HashMap<String, String>> father = new ArrayList<HashMap<String, String>>();
			child = new ArrayList<ArrayList<HashMap<String, String>>>();
			
			HashMap<String, String> fatherHashMap = null;
			
			int playListsSize = playLists.size();
			int mp3Id_size = 0;
			String listName = "";
			Integer list_id = null;
			
			ArrayList<HashMap<String, String>> children = null;
			HashMap<String, String> childData = null;
			
			for(int i = 0; i < playListsSize; i++){
				listName = playLists.get(i).getPlayList_name();
				list_id = playLists.get(i).getPlayList_id();
				fatherHashMap = new HashMap<String, String>();
				fatherHashMap.put("father", listName);
				father.add(fatherHashMap);
				
				children = new ArrayList<HashMap<String, String>>();
				
				mp3_ids = playListMapUtil.findMp3IDsByPlayListId(list_id);
				mp3Id_size = mp3_ids.size();
				
				if (mp3Id_size > 0) {
					StringBuilder selection = new StringBuilder();
					for(int j = 0; j < mp3Id_size; j++){
						selection.append(String.valueOf(mp3_ids.get(j)));
						selection.append(',');
					}
					selection.deleteCharAt(selection.length()-1);
					cursor = contentResolver.query(
							MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
							new String[] { 
									MediaStore.Audio.Media._ID,
									MediaStore.Audio.Media.DATA,
									MediaStore.Audio.Media.TITLE,
									MediaStore.Audio.Media.ARTIST,
									MediaStore.Audio.Media.DURATION}, "_id in("+ selection +")",
							null, null);
					while(cursor.moveToNext()){
						childData = new HashMap<String, String>();
						childData.put("audio_name", cursor.getString(2));
						childData.put("audio_size", StrTimeUtil.longTimeToString(cursor.getInt(4)));
						childData.put("audio_artist", cursor.getString(3));
						childData.put("audio_id", String.valueOf(cursor.getInt(0)));
						childData.put("audio_data", cursor.getString(1));
						children.add(childData);
					}
					cursor.close();
				} 
//				else{
//					childData = new HashMap<String,String>();
//					children.add(childData);
//				}
				child.add(children);
			}
			
			UpdatePlayListHandler updatePlayListHandler = new UpdatePlayListHandler(looper,father,child,progressDialog);
			updatePlayListHandler.sendEmptyMessage(0);
		}
	}
	
	/**
	 * 更新UI
	 * @author Alex
	 *
	 */
	class UpdatePlayListHandler extends Handler{
		
		private ArrayList<HashMap<String, String>> father = null;
		private ArrayList<ArrayList<HashMap<String, String>>> child = null;
		private ProgressDialog progressDialog = null;

		public UpdatePlayListHandler(Looper looper,
				ArrayList<HashMap<String, String>> father,
				ArrayList<ArrayList<HashMap<String, String>>> child,
				ProgressDialog progressDialog) {
			super(looper);
			this.father = father;
			this.child = child;
			this.progressDialog = progressDialog;
		}
		
		@Override
		public void handleMessage(Message msg) {
//			setListAdapter(getExAdapter(father, child));
			e.setAdapter(getExAdapter(father, child));
			if(null != progressDialog){
				progressDialog.cancel();
			}
			cea.setRefreshPlayList(false);
		}
		
	}
	
	private SimpleExpandableListAdapter getExAdapter(ArrayList<HashMap<String, String>> father, ArrayList<ArrayList<HashMap<String, String>>> child){
		SimpleExpandableListAdapter exAdapter = new SimpleExpandableListAdapter(
				this, father,
				R.layout.mediaplayer_expandablegroupitem_320_480,
				new String[] { "father" }, new int[] { R.id.group_name }, child,
				R.layout.mediaplayer_mp3infoitem_320_480, new String[] {
						"audio_name", "audio_size", "audio_artist" }, new int[] {
						R.id.mp3_name, R.id.mp3_size, R.id.mp3_artist });
		return exAdapter;
	}
	
	// 用户点击menu按钮之后，会调用该方法。
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuItem item = menu.add(0, MENU_ADDPLAYLIST, MENU_ADDPLAYLIST, R.string.addplaylist);
	    item.setIcon(R.drawable.icon_menu_createlist);
//		menu.add(0, MENU_VOLUME, MENU_VOLUME, R.string.volume);
	    MenuItem item2 = menu.add(0, MENU_EXIT, MENU_EXIT, R.string.exit);
	    item2.setIcon(R.drawable.icon_menu_exit);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_ADDPLAYLIST:
			createPlayListDialog();
			break;
		case MENU_VOLUME:
			//###########################################################################################
			break;
		case MENU_EXIT:
			exitDialog();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		exitDialog();
	}
	
	/**
	 * 退出窗口
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
	 * 新建播放列表 窗口
	 */
	private void createPlayListDialog(){
		AlertDialog.Builder builder = new Builder(this);
		final EditText editText = new EditText(getApplicationContext());
		editText.setText(R.string.create_new_playlist);
		builder.setPositiveButton(R.string.btn_confirm, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String listName = editText.getText().toString();
				playListUtil.save(new PlayList(listName));
				updatePlayList();
				dialog.dismiss();
			}
		});
		builder.setNegativeButton(R.string.btn_cancel, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.setView(editText);
		builder.create().show();
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
	
	@Override
	protected void onDestroy() {
		cea.setRefreshPlayList(true);
		super.onDestroy();
	}

}
