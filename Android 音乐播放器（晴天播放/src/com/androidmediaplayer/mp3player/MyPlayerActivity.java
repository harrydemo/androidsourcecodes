package com.androidmediaplayer.mp3player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SimpleAdapter;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.androidmediaplayer.R;
import com.androidmediaplayer.constant.AppConstant;
import com.androidmediaplayer.constant.ConstantExtendsApplication;
import com.androidmediaplayer.model.Mp3Info;
import com.androidmediaplayer.model.PlayList;
import com.androidmediaplayer.utils.InitPlayListThread;
import com.androidmediaplayer.utils.LinkedListPlayList;
import com.androidmediaplayer.utils.LrcProcessorUtils;
import com.androidmediaplayer.utils.PlayListMapUtil;
import com.androidmediaplayer.utils.PlayListUtil;

/**
 * 顺序播放，最后一首了还按NEXT咋办
 * 
 */

public class MyPlayerActivity extends Activity {

	private ImageButton modeButton_normal = null;
	private ImageButton modeButton_repeat = null;
	private ImageButton modeButton_repeat_single_ = null;
	private ImageButton modeButton_shuffle = null;
	// 避免500毫秒时间差产生 按住进度条拖拉过程中 被更新回原来的位置
	private boolean canUpdate = true;
	
	private SharedPreferences sp = null;
	
	private ImageButton startAndPauseButton = null;
	private ImageButton nextButton = null;
	private ImageButton latestBtn = null;

	private Mp3Info mp3Info = null;
	private TextView artistView = null;
	private TextView musicNameView = null;
	private TextView lrcTextView = null;
	private TextView positionView = null;
	private TextView durationView = null;
	private SeekBar seekBar = null;
	private int currentProgress = 0;
	private int duration = 0;
	private UpdatePositionThread updatePositionThread = null;
	private IntentFilter intentFilter = null;
	private ServiceMSGReceiver receiver = null;
	private UpdateLrcHandler updateLrcHandler = null;
	// private UpdateLrcThread updateLrcThread = null;
	private int point = 0;
	private ArrayList<Long> times = null;
	private int timesSize = 0;
	private ArrayList<String> lrcs = null;
	// private ArrayList<Long> timeToTime = null;
	@SuppressWarnings("rawtypes")
	private ArrayList<ArrayList> arrayList = null;
	private Looper looper = null;
	private ArrayList<Integer> playList_ids = null;
	private ConstantExtendsApplication cea = null;
	private boolean normalCompleteFlag = false;

	private IMediaPlayerService mediaPlayerService = null;
	private ServiceConnection connection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mediaPlayerService = IMediaPlayerService.Stub.asInterface(service);
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mediaPlayerService = null;
		}
	};

	// 更新UI(只运行一次)
	private Handler updateUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			onLoad();
			setPrepareUI();
			updatePositionThread = new UpdatePositionThread();
			updatePositionHandler.postDelayed(updatePositionThread, 500);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mediaplayer_player_320_480);
		
		cea = (ConstantExtendsApplication)getApplicationContext();

		startAndPauseButton = (ImageButton) findViewById(R.id.playBtn);
		startAndPauseButton.setOnClickListener(new StartButtonListener());

		nextButton = (ImageButton) findViewById(R.id.nextBtn);
		latestBtn = (ImageButton) findViewById(R.id.latestBtn);
		lrcTextView = (TextView) findViewById(R.id.lrcText);
		artistView = (TextView) findViewById(R.id.artistName);

		musicNameView = (TextView) findViewById(R.id.songName);
		seekBar = (SeekBar) findViewById(R.id.seekBar);
		positionView = (TextView) findViewById(R.id.playTime);
		durationView = (TextView) findViewById(R.id.duration);

		setSeekBarListener();
		receiver = new ServiceMSGReceiver();
		registerReceiver(receiver, getIntentFilter());
		looper = Looper.getMainLooper();
		BindService();

		mp3Info = (Mp3Info) getIntent().getSerializableExtra("mp3Info");
		LinkedListPlayList.playListId = getIntent().getIntExtra("playListId", -1);
		new Thread(new InitPlayListThread(this)).start();
		
		// 进度条和歌词的更新
		new Thread(new PrepareThread()).start();
		setModeButtonListener();
		setNLButtonListener();
	}

	@Override
	protected void onResume() {
		super.onResume();
		cea.setPlayerActivityExist(true);
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

	private class PrepareThread implements Runnable {
		@Override
		public void run() {
			// updateLrcThread = new UpdateLrcThread();
			updateLrcHandler = new UpdateLrcHandler(looper);
			prepareLrc();
			while (true) {
				if (null != mediaPlayerService) {
					break;
				}
			}
			updateUIHandler.sendEmptyMessage(0);
		}
	}

	// 绑定AIDL服务
	private void BindService() {
		Intent intent = new Intent(IMediaPlayerService.class.getName());
		bindService(intent, connection, BIND_AUTO_CREATE);
	}

	// 点击歌曲时候
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void onLoad() {
		canUpdate = true;
		List list = new ArrayList();
		list.add(mp3Info);
		try {
			mediaPlayerService.setFileInfo(list);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	// 播放和暂停按钮
	private class StartButtonListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			try {
				if (mediaPlayerService.isPlaying()) {
					canUpdate = false;
					mediaPlayerService.pause();
					updatePositionHandler.removeCallbacks(updatePositionThread);
					// updateLrcHandler.removeCallbacks(updateLrcThread);
					startAndPauseButton.setImageResource(R.drawable.play_selector);
				} else {
					canUpdate = true;
					mediaPlayerService.start();
					updatePositionHandler
							.postDelayed(updatePositionThread, 500);
					// updateLrcHandler.post(updateLrcThread);
					startAndPauseButton.setImageResource(R.drawable.pause_selector);
					if (null != arrayList) {
						lrcTextView.setText(lrcs.get(point));
					}
					//顺序播放模式，列表播放完毕，按下播放键后重头开始播放
					if(sp.getInt("playMode", -1) == AppConstant.PlayMode.MODE_NOAMAL && normalCompleteFlag == true){
						MyPlayerActivity.this.mp3Info = LinkedListPlayList.getFirst();
						prepareLrc();
						onLoad();
						setPrepareUI();
						updatePositionHandler.postDelayed(updatePositionThread, 500);
						startAndPauseButton.setImageResource(R.drawable.pause_selector);
					}
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void setNLButtonListener(){
		nextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(LinkedListPlayList.size()>1){
					try {
						LinkedListPlayList.listPointer++;
						MyPlayerActivity.this.mp3Info = LinkedListPlayList.get(LinkedListPlayList.listPointer);
					} catch (IndexOutOfBoundsException e) {
						e.printStackTrace();
						LinkedListPlayList.listPointer = 0;
						MyPlayerActivity.this.mp3Info = LinkedListPlayList.getFirst();
					}
					prepareLrc();
					onLoad();
					setPrepareUI();
					updatePositionHandler.postDelayed(updatePositionThread, 500);
					startAndPauseButton.setImageResource(R.drawable.pause_selector);
				}
			}
		});
		latestBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(LinkedListPlayList.size()>1){
					try {
						LinkedListPlayList.listPointer--;
						MyPlayerActivity.this.mp3Info = LinkedListPlayList.get(LinkedListPlayList.listPointer);
					} catch (Exception e) {
						e.printStackTrace();
						LinkedListPlayList.listPointer = LinkedListPlayList.size() - 1;
						MyPlayerActivity.this.mp3Info = LinkedListPlayList.getLast();
					}
					prepareLrc();
					onLoad();
					setPrepareUI();
					updatePositionHandler.postDelayed(updatePositionThread, 500);
					startAndPauseButton.setImageResource(R.drawable.pause_selector);
				}
			}
		});
	}

	// 启动播放画面前设置UI
	private void setPrepareUI() {
		musicNameView.setText(mp3Info.getMp3Name().trim());
		try {
			artistView.setText(mp3Info.getArtist().trim());
			// duration = Integer.valueOf(mp3Info.getDuration());
			duration = mediaPlayerService.getDuration();
			seekBar.setMax(duration);
			currentProgress = mediaPlayerService.getCurrentPosition();
			// 设置歌词指向
			point = getCurrentPoint(currentProgress);
			if (null != arrayList) {
				lrcTextView.setText(lrcs.get(point));
			} else {
				lrcTextView.setText(R.string.lrc_load_fail);
			}
			// 设置进度
			seekBar.setProgress(currentProgress);
			positionView.setText(longTimeToString(currentProgress));
			durationView.setText(longTimeToString(duration));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	// 过滤器
	private IntentFilter getIntentFilter() {
		if (intentFilter == null) {
			intentFilter = new IntentFilter();
			intentFilter
					.addAction(AppConstant.Action.MEDIAPLAYERSERVICE_MESSAGE_ACTION);
		}
		return intentFilter;
	}

	
	
	// 广播接受
	private class ServiceMSGReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			int MSG_CODE = intent.getIntExtra("MEDIAPLAYERMSG", -1);
			// 播放完毕
			if (MSG_CODE == AppConstant.PlayerMsg.COMPLETE) {
				System.out.println("播放完毕");
				canUpdate = false;
				currentProgress = 0;
				point = 0;
				seekBar.setProgress(0);
				updatePositionHandler.removeCallbacks(updatePositionThread);
				startAndPauseButton.setImageResource(R.drawable.play_selector);
				positionView.setText("00:00");
				
				int mode = sp.getInt("playMode", -1);
				switch (mode) {
				case AppConstant.PlayMode.MODE_LIST_REPEAT:
					canUpdate = true;
//					int index1 = 0;
//					for(Mp3Info mp3Info : LinkedListPlayList.getPlayListMp3Infos()){
//						if(mp3Info.getAudio_id().equals(MyPlayerActivity.this.mp3Info.getAudio_id())){
							try {
								LinkedListPlayList.listPointer++;
								MyPlayerActivity.this.mp3Info = LinkedListPlayList.get(LinkedListPlayList.listPointer);
								prepareLrc();
								onLoad();
								setPrepareUI();
								updatePositionHandler.postDelayed(updatePositionThread, 500);
								startAndPauseButton.setImageResource(R.drawable.pause_selector);
							} catch (IndexOutOfBoundsException e) {
								e.printStackTrace();
								System.out.println("列表播放完毕");
								LinkedListPlayList.listPointer = 0;
								MyPlayerActivity.this.mp3Info = LinkedListPlayList.getFirst();
								prepareLrc();
								onLoad();
								setPrepareUI();
								updatePositionHandler.postDelayed(updatePositionThread, 500);
								startAndPauseButton.setImageResource(R.drawable.pause_selector);
							}
//							break;
//						}
//						index1++;
//					}
					break;
				case AppConstant.PlayMode.MODE_NOAMAL:
					canUpdate = true;
//					int index2 = 0;
//					for(Mp3Info mp3Info : LinkedListPlayList.getPlayListMp3Infos()){
//						if(mp3Info.getAudio_id().equals(MyPlayerActivity.this.mp3Info.getAudio_id())){
							try {
								LinkedListPlayList.listPointer++;
								MyPlayerActivity.this.mp3Info = LinkedListPlayList.get(LinkedListPlayList.listPointer);
								prepareLrc();
								onLoad();
								setPrepareUI();
								updatePositionHandler.postDelayed(updatePositionThread, 500);
								startAndPauseButton.setImageResource(R.drawable.pause_selector);
							} catch (IndexOutOfBoundsException e) {
								e.printStackTrace();
								System.out.println("列表播放完毕");
								LinkedListPlayList.listPointer = 0;
								normalCompleteFlag = true;
							}
//							break;
//						}
//						index2++;
//					}
					break;
				case AppConstant.PlayMode.MODE_SHUFFLE:
					canUpdate = true;
					if(LinkedListPlayList.size()>1){
						int _index3 = LinkedListPlayList.listPointer;
						int index3 = shuffle(0,  LinkedListPlayList.size());
						while(index3 == _index3){
							index3 = shuffle(0,  LinkedListPlayList.size());
						}
						MyPlayerActivity.this.mp3Info = LinkedListPlayList.get(index3);
						LinkedListPlayList.listPointer = index3;
						prepareLrc();
						onLoad();
						setPrepareUI();
					}else{ //播放列表只有一首歌，随机的情况
						LinkedListPlayList.listPointer = 0;
						try {
							mediaPlayerService.start();
						} catch (RemoteException e) {
							e.printStackTrace();
						}
						if (null != arrayList) {
							lrcTextView.setText(lrcs.get(point));
						}
					}
					updatePositionHandler.postDelayed(updatePositionThread, 500);
					startAndPauseButton.setImageResource(R.drawable.pause_selector);
					break;
				case AppConstant.PlayMode.MODE_SINGLE_REPEAT:
					canUpdate = true;
					try {
						mediaPlayerService.start();
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					updatePositionHandler.postDelayed(updatePositionThread, 500);
					startAndPauseButton.setImageResource(R.drawable.pause_selector);
					if (null != arrayList) {
						lrcTextView.setText(lrcs.get(point));
					}
					break;
				}
			} else if(MSG_CODE == AppConstant.PlayerMsg.MEDIAPLAYERERROR){
				
			}
			
		}
	}
	
	private int shuffle(int x, int y){
		return (int)(x + Math.random()*(y - x));
	}

	// 设置seekbar监听
	private void setSeekBarListener() {
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				canUpdate = true;
				try {
					mediaPlayerService.start();
					mediaPlayerService.seekTo(currentProgress);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				updatePositionHandler.postDelayed(updatePositionThread, 500);
				startAndPauseButton.setImageResource(R.drawable.pause_selector);
				// 歌词
				point = getCurrentPoint(currentProgress);
				if (null != arrayList) {
					lrcTextView.setText(lrcs.get(point));
				}
				// updateLrcHandler.post(updateLrcThread);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				canUpdate = false;
				updatePositionHandler.removeCallbacks(updatePositionThread);
				try {
					mediaPlayerService.pause();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				startAndPauseButton.setImageResource(R.drawable.play_selector);
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (fromUser) {
					currentProgress = progress;
					positionView.setText(longTimeToString(progress));
				} else {
					// 继续更新进度条
					updatePositionHandler
							.postDelayed(updatePositionThread, 500);
					// 更新歌词
					if (null != arrayList && timesSize > point + 1) {
						if (progress >= times.get(point + 1)) {
							updateLrcHandler.sendEmptyMessage(0);
							point++;
						}
					}
				}
			}
		});
	}

	/**
	 * 根据歌词文件的名字，来读取歌词文件当中的信息
	 * 
	 * @param lrcName
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private void prepareLrc() {
		String lrcPath = "";
		lrcPath = Environment.getExternalStorageDirectory().getAbsoluteFile()
				+ File.separator + "mp3/" + mp3Info.getMp3Name() + ".lrc";
		System.out.println(lrcPath);
		try {
			arrayList = null;
			arrayList = LrcProcessorUtils.process(lrcPath);
			times = arrayList.get(0);
			lrcs = arrayList.get(1);
			timesSize = times.size();
			// timeToTime = arrayList.get(2);
			// updateLrcHandler.postDelayed(updateLrcThread,100);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("lrc file does not exist!!");
		}
	}

	// 取得当前指向
	// 数据量大时可以采取折半查找，但是歌词一般不多
	private int getCurrentPoint(int progress) {
		if (null != arrayList) {
			int currentPosition = 0;
			try {
				currentPosition = mediaPlayerService.getCurrentPosition();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			for (int i = 0; i < timesSize - 1; i++) {
				if (times.get(i) <= currentPosition
						&& currentPosition < times.get(i + 1)) {
					return i;
				}
			}
			if (currentPosition < times.get(0)) {
				return 0;
			} else {
				return timesSize - 1;
			}
		} else {
			return 0;
		}
	}

	// 更新歌词handler
	class UpdateLrcHandler extends Handler {
		public UpdateLrcHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			if (null != arrayList) {
				lrcTextView.setText(lrcs.get(point));
			}
		}
	}

	// 持续更新UI
	private Handler updatePositionHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (canUpdate) {
				positionView.setText(longTimeToString(currentProgress));
				seekBar.setProgress(currentProgress);
			}
		}
	};

	// 更新进度条线程
	class UpdatePositionThread implements Runnable {
		@Override
		public void run() {
			try {
				currentProgress = mediaPlayerService.getCurrentPosition();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			updatePositionHandler.sendEmptyMessage(0);
		}
	}

	private final int MENU_ADDTOPLAYLIST = 1;
	private final int MENU_VOLUME = 2;

	// 用户点击menu按钮之后，会调用该方法。
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuItem item = menu.add(0, MENU_ADDTOPLAYLIST, MENU_ADDTOPLAYLIST,
				R.string.addtoplaylist);
	    item.setIcon(R.drawable.icon_menu_addto);
//		menu.add(0, MENU_VOLUME, MENU_VOLUME, R.string.volume);
		// menu.add(0, EXIT, 2, R.string.exit);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_ADDTOPLAYLIST:
			addToPlayListDialog();
			break;
		case MENU_VOLUME:

			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 添加到播放列表
	 */
	private void addToPlayListDialog() {
		final ConstantExtendsApplication cea = ((ConstantExtendsApplication) getApplicationContext());
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.dialog_choiceplease);
		builder.setSingleChoiceItems(getSimpleAdapter(), -1,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String audio_id = mp3Info.getAudio_id();
						PlayListMapUtil playListMapUtil = new PlayListMapUtil(
								getApplicationContext());
						playListMapUtil.save(Integer.valueOf(audio_id),
								playList_ids.get(which));
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

	private SimpleAdapter getSimpleAdapter() {
		playList_ids = new ArrayList<Integer>();
		ArrayList<PlayList> playListArrayList = null;
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		PlayListUtil playListUtil = new PlayListUtil(this);
		playListArrayList = playListUtil.findAll();
		int size = playListArrayList.size();
		HashMap<String, String> hm = null;
		for (int i = 0; i < size; i++) {
			hm = new HashMap<String, String>();
			hm.put("listName", playListArrayList.get(i).getPlayList_name());
			list.add(hm);
			playList_ids.add(playListArrayList.get(i).getPlayList_id());
		}
		SimpleAdapter sAdapter = new SimpleAdapter(this, list,
				R.layout.mediaplayer_playlistitem_320_480,
				new String[] { "listName" }, new int[] { R.id.playlist_name });
		return sAdapter;
	}
	
	/**
	 * 设置点击后的按钮背景透明度
	 * @param b1
	 * @param b2
	 * @param b3
	 * @param b4
	 * @param sd
	 */
	private void setModeButton(int b1, int b2, int b3, int b4, SlidingDrawer sd) {
		modeButton_normal.setImageResource(b1);
		modeButton_repeat.setImageResource(b2);
		modeButton_repeat_single_.setImageResource(b3);
		modeButton_shuffle.setImageResource(b4);
		sd.getContent().invalidate();
	}
	
	/**
	 * 初始化播放模式按钮
	 * @param sp
	 */
	private void initModeButton(SharedPreferences sp){
		modeButton_normal = (ImageButton) findViewById(R.id.mode_normal);
		modeButton_repeat = (ImageButton) findViewById(R.id.mode_repeat);
		modeButton_repeat_single_ = (ImageButton) findViewById(R.id.mode_repeat_single);
		modeButton_shuffle = (ImageButton) findViewById(R.id.mode_shuffle);
		
		int playMode = sp.getInt("playMode", -1);
		switch (playMode) {
		case AppConstant.PlayMode.MODE_LIST_REPEAT:
		    modeButton_repeat.setImageResource(R.drawable.icon_playmode_repeat);
			break;
		case AppConstant.PlayMode.MODE_NOAMAL:
		    modeButton_normal.setImageResource(R.drawable.icon_playmode_normal);
			break;
		case AppConstant.PlayMode.MODE_SHUFFLE:
		    modeButton_shuffle.setImageResource(R.drawable.icon_playmode_shuffle);
			break;
		case AppConstant.PlayMode.MODE_SINGLE_REPEAT:
		    modeButton_repeat_single_.setImageResource(R.drawable.icon_playmode_repeat_single);
			break;
		}
		
	}
	
	/**
	 * 设置记录播放模式
	 * @param editor
	 * @param playMode
	 */
	private void setPlayMode(Editor editor, int playMode){
		editor.putInt("playMode", playMode);
		editor.commit();
	}
	
	/**
	 * 提示播放模式
	 * @param toast
	 * @param resId
	 */
	private void setToast(Toast toast, int resId){
		toast.cancel();
		toast.setText(resId);
		toast.show();
	}
	
	/**
	 * 设置播放模式按钮监听
	 */
	private void setModeButtonListener(){
		final SlidingDrawer sd = (SlidingDrawer) findViewById(R.id.slidingdrawer);
		sp = getSharedPreferences("playMode", MODE_PRIVATE);
		final Editor editor = sp.edit();
		final Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		initModeButton(sp);
		
		modeButton_normal.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                setModeButton(R.drawable.icon_playmode_normal,
                        R.drawable.icon_playmode_repeat_1,
                        R.drawable.icon_playmode_repeat_single_1,
                        R.drawable.icon_playmode_shuffle_1, sd);
                setToast(toast, R.string.mode_list_play);
                setPlayMode(editor, AppConstant.PlayMode.MODE_NOAMAL);
			}
		});
		modeButton_repeat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			    setModeButton(R.drawable.icon_playmode_normal_1,
                        R.drawable.icon_playmode_repeat,
                        R.drawable.icon_playmode_repeat_single_1,
                        R.drawable.icon_playmode_shuffle_1, sd);
				setToast(toast, R.string.mode_list_repeat);
				setPlayMode(editor, AppConstant.PlayMode.MODE_LIST_REPEAT);
			}
		});
		modeButton_repeat_single_.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			    setModeButton(R.drawable.icon_playmode_normal_1,
                        R.drawable.icon_playmode_repeat_1,
                        R.drawable.icon_playmode_repeat_single,
                        R.drawable.icon_playmode_shuffle_1, sd);
				setToast(toast, R.string.mode_single_repeat);
				setPlayMode(editor, AppConstant.PlayMode.MODE_SINGLE_REPEAT);
			}
		});
		modeButton_shuffle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			    setModeButton(R.drawable.icon_playmode_normal_1,
                        R.drawable.icon_playmode_repeat_1,
                        R.drawable.icon_playmode_repeat_single_1,
                        R.drawable.icon_playmode_shuffle, sd);
				setToast(toast, R.string.mode_random_play);
				setPlayMode(editor, AppConstant.PlayMode.MODE_SHUFFLE);
			}
		});
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		cea.setPlayerActivityExist(false);
	}

	@Override
	protected void onStop() {
		updatePositionHandler.removeCallbacks(updatePositionThread);
		unregisterReceiver(receiver);
		unbindService(connection);
		canUpdate = false;
		finish();
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		updatePositionHandler.removeCallbacks(updatePositionThread);
//		unregisterReceiver(receiver);
//		unbindService(connection);
//		canUpdate = false;
	}

	/**
	 * 毫秒转化成字符串时间 00:00
	 * 
	 * @param i
	 * @return
	 */
	private synchronized String longTimeToString(int i) {
		StringBuilder sb = new StringBuilder();
		i = i / 1000;
		int m = i / 60;
		int s = i % 60;
		if (i >= 60) {
			if (m < 10) {
				sb.append("0");
				sb.append(String.valueOf(m));
			} else {
				sb.append(String.valueOf(m));
			}
			sb.append(":");
			if (s > 9) {
				sb.append(String.valueOf(s));
			} else {
				sb.append("0");
				sb.append(String.valueOf(s));
			}
		} else {
			sb.append("00:");
			if (s > 9) {
				sb.append(String.valueOf(s));
			} else {
				sb.append("0");
				sb.append(String.valueOf(s));
			}
		}
		return sb.toString();
	}

}
