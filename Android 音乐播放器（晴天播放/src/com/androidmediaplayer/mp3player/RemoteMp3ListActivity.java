package com.androidmediaplayer.mp3player;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

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
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Toast;

import com.androidmediaplayer.R;
import com.androidmediaplayer.constant.AppConstant;
import com.androidmediaplayer.constant.ConstantExtendsApplication;
import com.androidmediaplayer.model.DownloadTask;
import com.androidmediaplayer.model.Mp3Info;
import com.androidmediaplayer.model.PlayList;
import com.androidmediaplayer.mp3player.service.DownloadService;
import com.androidmediaplayer.mp3player.service.ScanService;
import com.androidmediaplayer.utils.FileUtil;
import com.androidmediaplayer.utils.HttpDownloaderUtils;
import com.androidmediaplayer.utils.LinkedListPlayList;
import com.androidmediaplayer.utils.PlayListMapUtil;
import com.androidmediaplayer.utils.PlayListUtil;
import com.androidmediaplayer.utils.XML_Mp3ListContentHandler;

public class RemoteMp3ListActivity extends Activity {
	// 更新列表
	private final int M_UPDATE = 1;
	// 关于本软件
//	private final int M_ABOUT = 2;
	// 完全退出
	private final int M_EXIT = 3;
	//音量
	private final int M_VOLUME = 2; 
	
	private static int taskIndex = 0;

	private IntentFilter intentFilter = null;
	private ProgressDialog progressDialog = null;
	private Looper looper = null;
	private final int DOWNLOADNOW_CODE = 0;
	private final int PLAYONLINE_CODE = 1;
	private final int PLAYNOW_CODE = 0;
	private final int ADDTOLIST_CODE = 1;
	public static HashMap<Integer, DownloadTask> downloadTasks = null;
	private ArrayList<ArrayList<Mp3Info>> all = null;
	private DownLoadMessageBroadcastReceiver downLoadMessageBroadcastReceiver = null;
	private ConstantExtendsApplication cea = null;
	private ExpandableListView e = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mediaplayer_remotemp3list_320_480);
		ImageView iv = (ImageView)findViewById(R.id.remotebg);
		iv.getBackground().setAlpha(210);
		View v = findViewById(R.id.remote_bottom_player);
        v.setVisibility(View.INVISIBLE);
        setListener();
		looper = Looper.getMainLooper();
		downLoadMessageBroadcastReceiver = new DownLoadMessageBroadcastReceiver();
		updateListView();
		downloadTasks = new HashMap<Integer, DownloadTask>();
		cea = ((ConstantExtendsApplication) getApplicationContext());
	}
	
	/**
	 * 更新列表
	 */
	private void updateListView() {
		progressDialog = ProgressDialog.show(this, "",
				getResources().getString(R.string.loading), true);
		new Thread(new UpdateExListThread()).start();
	}

	private boolean goFlag = true;
	
	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(downLoadMessageBroadcastReceiver, getIntentFilter());
		
		if(cea.isHadInitPlayer() && goFlag){
            goFlag = false;
            View v = findViewById(R.id.remote_bottom_player);
            v.setVisibility(View.VISIBLE);
        }
		
		if(!cea.downloadTasks.isEmpty()){
			DownloadTask downloadTask = cea.downloadTasks.pop();
			int groupPosition = downloadTask.getGroupIndex();
			int childPosition = downloadTask.getChildIndex();
			
			Set<Integer> keys = downloadTasks.keySet();
			Iterator<Integer> iter = keys.iterator();
			
			while(iter.hasNext()){
				Integer key = iter.next();
				downloadTask = downloadTasks.get(key);
				if (downloadTask.getGroupIndex() == groupPosition && downloadTask.getChildIndex() == childPosition) {
					System.out.println("remote key: "+downloadTask.getTaskIndex());
					if(downloadTask.getState_download() == DownloadTask.STATE_DONE){
						alreadyDialog(groupPosition, childPosition, getText(R.string.downloadSuccess).toString(), R.array.exsit_dialog_items, downloadTask);
					}else if(downloadTask.getState_download() == DownloadTask.STATE_DOWNLOADING){
						comeFromNoti_downloading_Dialog(downloadTask,groupPosition,childPosition);
					}
					break;
				}
			}
//			cea.setDownloadTask(null);
		}
	}
	
	private void comeFromNoti_downloading_Dialog(final DownloadTask downloadTask, int group, int child) {
		Mp3Info mp3Info = all.get(group).get(child);
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
	 * 广播过滤
	 * @return
	 */
	private IntentFilter getIntentFilter() {
		if (null == intentFilter) {
			intentFilter = new IntentFilter();
			intentFilter.addAction(AppConstant.Action.DOWNLOAD_MESSAGE_ACTION);
		}
		return intentFilter;
	}

	@Override
	public void onBackPressed() {
		exitDialog();
	}

	/**
	 * 接收service发送的广播
	 */
	private class DownLoadMessageBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			int downLoadMessageCode = intent.getIntExtra("downLoadMsg", 0);
			if (downLoadMessageCode == AppConstant.PlayerMsg.ADDTODOWNLOADLIST_MSG) {
				int msg = R.string.hadaddtodownlist;
				showDownLoadMsg(msg);
			}
		}
	}

	/**
	 * 显示下载信息提示
	 * @param msg
	 */
	private void showDownLoadMsg(int msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	// 用户点击menu按钮之后，会调用该方法。
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuItem item = menu.add(0, M_UPDATE, M_UPDATE, R.string.mp3list_update);
	    item.setIcon(R.drawable.icon_menu_findlrc);
//		menu.add(0, M_VOLUME, M_VOLUME, R.string.volume);
	    MenuItem item2 = menu.add(0, M_EXIT, M_EXIT, R.string.exit);
	    item2.setIcon(R.drawable.icon_menu_exit);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case M_UPDATE:
			updateListView();
			break;
		case M_VOLUME:
//			aboutDialog();
			//################################################################################################
			break;
		case M_EXIT:
			exitDialog();
			break;
		}
		return super.onOptionsItemSelected(item);
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
	 * 退出 窗口
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
	
    private void setListener() {
        e = (ExpandableListView) findViewById(R.id.remoteexlist);
        e.setOnChildClickListener(new OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                    int groupPosition, int childPosition, long id) {
                ArrayList<Mp3Info> mp3Infos = (ArrayList<Mp3Info>) all
                        .get(groupPosition);
                Mp3Info mp3Info = mp3Infos.get(childPosition);
                DownloadTask temp = null;

                if (FileUtil.isFileExist(mp3Info.getMp3Name() + ".mp3", "mp3")) {
                    Set<Integer> keys = downloadTasks.keySet();
                    Iterator<Integer> iter = keys.iterator();
                    while (iter.hasNext()) {
                        Integer key = iter.next();
                        DownloadTask downloadTask = downloadTasks.get(key);
                        if (downloadTask.getGroupIndex() == groupPosition
                                && downloadTask.getChildIndex() == childPosition) {
                            temp = downloadTask;
                            break;
                        }
                    }
                    if (null == temp
                            || temp.getState_download() == DownloadTask.STATE_DONE) {
                        String dialogTitle = mp3Info.getMp3Name() + " "
                                + getText(R.string.file_exist).toString();
                        int dialogItemMSGs = R.array.exsit_dialog_items;
                        alreadyDialog(groupPosition, childPosition,
                                dialogTitle, dialogItemMSGs, null);
                    } else if (temp.getState_download() == DownloadTask.STATE_DOWNLOADING) {
                        Toast.makeText(getApplicationContext(),
                                R.string.downloading, Toast.LENGTH_SHORT)
                                .show();
                    }

                } else {
                    String dialogTitle = mp3Info.getMp3Name();
                    int dialogItemMSGs = R.array.down_dialog_tiems;
                    downloadDialog(groupPosition, childPosition, dialogTitle,
                            dialogItemMSGs);
                }
                return false;
            }
        });
        ImageButton ib = (ImageButton)findViewById(R.id.remotegotoplayer);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyPlayerActivity.class);
                intent.putExtra("mp3Info", cea.getCurrentPlayingMp3Info());
                startActivity(intent);
            }
        });
    }

//	@Override
//	public boolean onChildClick(ExpandableListView parent, View v,
//			int groupPosition, int childPosition, long id) {
//		ArrayList<Mp3Info> mp3Infos = (ArrayList<Mp3Info>) all
//				.get(groupPosition);
//		Mp3Info mp3Info = mp3Infos.get(childPosition);
//		DownloadTask temp = null;
//		
//		if (FileUtil.isFileExist(mp3Info.getMp3Name() + ".mp3", "mp3")) {
//			Set<Integer> keys = downloadTasks.keySet();
//			Iterator<Integer> iter = keys.iterator();
//			while(iter.hasNext()){
//				Integer key = iter.next();
//				DownloadTask downloadTask = downloadTasks.get(key);
//				if (downloadTask.getGroupIndex() == groupPosition
//						&& downloadTask.getChildIndex() == childPosition) {
//					temp = downloadTask;
//					break;
//				}
//			}
//			if (null == temp || temp.getState_download() == DownloadTask.STATE_DONE) {
//				String dialogTitle = mp3Info.getMp3Name() + " " + getText(R.string.file_exist).toString();
//				int dialogItemMSGs = R.array.exsit_dialog_items;
//				alreadyDialog(groupPosition, childPosition, dialogTitle, dialogItemMSGs, null);
//			} else if (temp.getState_download() == DownloadTask.STATE_DOWNLOADING) {
//				Toast.makeText(this, R.string.downloading, Toast.LENGTH_SHORT)
//						.show();
//			}
//			
//		} else {
//			String dialogTitle = mp3Info.getMp3Name();
//			int dialogItemMSGs = R.array.down_dialog_tiems;
//			downloadDialog(groupPosition, childPosition, dialogTitle, dialogItemMSGs);
//		}
//		return super.onChildClick(parent, v, groupPosition, childPosition, id);
//	}

	/**
	 * 下载窗口
	 * @param groupPosition
	 * @param childPosition
	 */
	private void downloadDialog(final int groupPosition, final int childPosition, String title, int dialogItemMSGs) {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle(title);
		builder.setItems(dialogItemMSGs, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DOWNLOADNOW_CODE:
					downloadNow(groupPosition, childPosition);
					break;
				case PLAYONLINE_CODE:
					playOnline(groupPosition, childPosition);
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
		builder.create().show();
	}
	
	/**
	 * 在线播放
	 * @param groupPosition
	 * @param childPosition
	 */
	private void playOnline(int groupPosition, int childPosition){
		Mp3Info mp3Info = all.get(groupPosition).get(childPosition);
		String audio_id = mp3Info.getAudio_id();
		String path = AppConstant.URL.BASE_URL + audio_id;
		mp3Info.setPath(path);
//		//为避免与本地音乐ID冲突，所以在线播放音乐ID为负
//		if(mp3Info.getAudio_id().contains(".")){
//			mp3Info.setAudio_id("-"+audio_id.substring(0, audio_id.indexOf('.')));
//		}
		Intent intent = new Intent(this,MyPlayerActivity.class);
		intent.putExtra("mp3Info", mp3Info);
		startActivity(intent);
	}

	/**
	 * 文件已经存在或者下载完毕窗口
	 * @param groupPosition
	 * @param childPosition
	 */
	private void alreadyDialog(int groupPosition, int childPosition,String title, int dialogItemMSGs, final DownloadTask downloadTask) {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle(title);
		final Mp3Info mp3Info = all.get(groupPosition).get(childPosition);
		builder.setItems(dialogItemMSGs, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case PLAYNOW_CODE:
					if(FileUtil._isFileExist(mp3Info.getMp3Name()+".mp3")){
					    playNow(mp3Info);
                    }else{
                        Toast.makeText(getApplicationContext(), R.string.hadnotexist, Toast.LENGTH_SHORT).show();
                    }
					break;
				case ADDTOLIST_CODE:
					if(FileUtil._isFileExist(mp3Info.getMp3Name()+".mp3")){
                        addToPlayListDialog(mp3Info);
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

	/**
	 * 文件已经存在，立即播放
	 * 
	 * @param whichGroup
	 * @param pos
	 */
	private void playNow(Mp3Info mp3Info) {
//		Mp3Info mp3Info = all.get(whichGroup).get(pos);
		String path = FileUtil.getFilePath(mp3Info.getMp3Name() + ".mp3");
		mp3Info.setAudio_id(getAudioId(mp3Info, path));
		mp3Info.setPath(path);
		
		//点击播放后默认添加到当前播放列表
		PlayListMapUtil playListMapUtil = new PlayListMapUtil(this);
		playListMapUtil.save(Integer.valueOf(mp3Info.getAudio_id()), 0);
		cea.setRefreshPlayList(true);
		
		Intent intent = new Intent(this, MyPlayerActivity.class);
		intent.putExtra("mp3Info", mp3Info);
		startActivity(intent);
	}

	/**
	 * 立即下载
	 * @param whichGroup
	 * @param pos
	 */
	private void downloadNow(int whichGroup, int pos) {
		Mp3Info mp3Info = all.get(whichGroup).get(pos);
		Intent intent = new Intent();
		intent.putExtra("mp3Info", mp3Info);
		intent.setClass(RemoteMp3ListActivity.this, DownloadService.class);
		DownloadTask downloadTask = new DownloadTask();
		downloadTask.setState_download(DownloadTask.STATE_DOWNLOADING);
		downloadTask.setTaskIndex(taskIndex);
		downloadTask.setGroupIndex(whichGroup);
		downloadTask.setChildIndex(pos);
		downloadTask.setMp3Info(mp3Info);
		downloadTasks.put(taskIndex, downloadTask);
		intent.putExtra("downloadTask", downloadTask);
		startService(intent);
		taskIndex++;
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

	/**
	 * 添加到播放列表
	 * @param whichGroup
	 * @param pos
	 */
	private void addToPlayListDialog(final Mp3Info mp3Info) {
		PlayListUtil playListUtil = new PlayListUtil(this);
		final ArrayList<PlayList> playLists = playListUtil.findAll();
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.dialog_choiceplease);
		builder.setSingleChoiceItems(getSimpleAdapter(playLists), -1,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String audio_id = getAudioId(mp3Info, null);
						PlayListMapUtil playListMapUtil = new PlayListMapUtil(
								getApplicationContext());
						int playListId = playLists.get(which).getPlayList_id();
						playListMapUtil.save(Integer.valueOf(audio_id), playListId);
						//如果要添加到的列表 刚好 和当前播放的列表相同，则把该歌曲同时加到播放链表末尾
						if(playListId == LinkedListPlayList.playListId && !LinkedListPlayList.getPlayListMp3Infos().isEmpty()){
							mp3Info.setAudio_id(audio_id);
							LinkedListPlayList.add(mp3Info);
						}
						Toast.makeText(getApplicationContext(),
								R.string.msg_addtolist, Toast.LENGTH_SHORT)
								.show();
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
		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * 取得播放列表适配器
	 * @return
	 */
	private SimpleAdapter getSimpleAdapter(ArrayList<PlayList> playLists) {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		int size = playLists.size();
		HashMap<String, String> hm = null;
		for (int i = 0; i < size; i++) {
			hm = new HashMap<String, String>();
			hm.put("listName", playLists.get(i).getPlayList_name());
			list.add(hm);
		}
		SimpleAdapter sAdapter = new SimpleAdapter(this, list,
				R.layout.mediaplayer_playlistitem_320_480,
				new String[] { "listName" }, new int[] { R.id.playlist_name });
		return sAdapter;
	}

	/**
	 * 取得折叠控件适配器
	 * @return
	 */
	private SimpleExpandableListAdapter getAdapter(ArrayList<HashMap<String, String>> father, ArrayList<ArrayList<HashMap<String, String>>> child) {
		SimpleExpandableListAdapter exAdapter = null;
		exAdapter = new SimpleExpandableListAdapter(this, father,
				R.layout.mediaplayer_expandablegroupitem_320_480,
				new String[] { "father" }, new int[] { R.id.group_name },
				child, R.layout.mediaplayer_mp3infoitem_320_480, new String[] {
						"mp3_name", "mp3_size", "mp3_artist" }, new int[] {
						R.id.mp3_name, R.id.mp3_size, R.id.mp3_artist });
		return exAdapter;
	}
	
	/**
	 * 更新控件UI线程
	 * @author Alex
	 *
	 */
	class UpdateExListThread implements Runnable {
		
		@Override
		public void run() {
			ArrayList<HashMap<String, String>> father = null;
			ArrayList<ArrayList<HashMap<String, String>>> child = null;
			String newtop50Xml = downloadXML(AppConstant.URL.BASE_URL
					+ "newtop50.xml");
			String clicktop50Xml = downloadXML(AppConstant.URL.BASE_URL
					+ "clicktop50.xml");
			if (null != newtop50Xml && !"".equals(newtop50Xml.trim())
					&& null != clicktop50Xml
					&& !"".equals(clicktop50Xml.trim())) {
				
				ArrayList<Mp3Info> newtop50 = null;
				ArrayList<Mp3Info> clicktop50 = null;
				
				newtop50 = parse(newtop50Xml);
				clicktop50 = parse(clicktop50Xml);
				all = new ArrayList<ArrayList<Mp3Info>>();
				all.add(newtop50);
				all.add(clicktop50);
				
				father = new ArrayList<HashMap<String, String>>();
				HashMap<String, String> hashMap = new HashMap<String, String>();
				hashMap.put("father", getResources().getString(R.string.newtop50));
				father.add(hashMap);
				hashMap = new HashMap<String, String>();
				hashMap.put("father", getResources().getString(R.string.clicktop50));
				father.add(hashMap);

				child = new ArrayList<ArrayList<HashMap<String, String>>>();
				ArrayList<HashMap<String, String>> children = getChildren(newtop50);
				child.add(children);
				children = getChildren(clicktop50);
				child.add(children);
			}
			UpdateHandler updateHandler = new UpdateHandler(looper, father, child);
			updateHandler.sendEmptyMessage(0);
		}

		private ArrayList<HashMap<String, String>> getChildren(
				ArrayList<Mp3Info> mp3Infos) {
			ArrayList<HashMap<String, String>> children = new ArrayList<HashMap<String, String>>();
			Mp3Info mp3Info = null;
			HashMap<String, String> childHashMap = null;
			int size = mp3Infos.size();
			for (int i = 0; i < size; i++) {
				mp3Info = mp3Infos.get(i);
				childHashMap = new HashMap<String, String>();
				childHashMap.put("mp3_name", mp3Info.getMp3Name());
				childHashMap.put("mp3_size", mp3Info.getMp3Size());
				childHashMap.put("mp3_artist", mp3Info.getArtist());
				children.add(childHashMap);
			}
			return children;
		}

		/**
		 * 解析XML
		 * @param xmlStr
		 * @return
		 */
		private ArrayList<Mp3Info> parse(String xmlStr) {
			ArrayList<Mp3Info> mp3Infos = null;
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			mp3Infos = new ArrayList<Mp3Info>();
			try {
				XMLReader xmlReader = saxParserFactory.newSAXParser()
						.getXMLReader();
				XML_Mp3ListContentHandler mp3ListContentHandler = new XML_Mp3ListContentHandler(
						mp3Infos);
				xmlReader.setContentHandler(mp3ListContentHandler);
				xmlReader.parse(new InputSource(new StringReader(xmlStr)));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mp3Infos;
		}

	}

	/**
	 * 更新UI  Handler
	 * @author Alex
	 *
	 */
	private class UpdateHandler extends Handler{
		private ArrayList<HashMap<String, String>> father = null;
		private ArrayList<ArrayList<HashMap<String, String>>> child = null;
		
		public UpdateHandler(Looper looper, ArrayList<HashMap<String, String>> father, ArrayList<ArrayList<HashMap<String, String>>> child){
			super(looper);
			this.father = father;
			this.child = child;
		}
		@Override
		public void handleMessage(Message msg) {
			if(null != father && null != child){
//				setListAdapter(getAdapter(father, child));
				e.setAdapter(getAdapter(father, child));
			}else{
				Toast toast = Toast.makeText(RemoteMp3ListActivity.this,
						R.string.requestTimeOut, Toast.LENGTH_SHORT);
				toast.show();
			}
			progressDialog.cancel();
		}
	}

	/**
	 * 下载XML文件内容
	 * @param urlStr
	 * @return
	 */
	private String downloadXML(String urlStr) {
		String result = HttpDownloaderUtils.downloadStringFromInternetFile(urlStr);
		return result;
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(downLoadMessageBroadcastReceiver);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		finish();
	}

}