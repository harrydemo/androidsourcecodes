package com.genius.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;


import com.genius.adapter.GridViewAdapter;
import com.genius.adapter.ListViewAdapter;
import com.genius.interfaces.IOnServiceConnectComplete;
import com.genius.interfaces.IOnSliderHandleViewClickListener;
import com.genius.musicplay.MusicData;
import com.genius.musicplay.MusicPlayMode;
import com.genius.musicplay.MusicPlayState;
import com.genius.service.ServiceManager;
import com.genius.widget.GalleryFlow;
import com.genius.widget.ImageAdapter;
import com.genius.widget.MySlidingDrawer;

import android.R.bool;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager.OnDismissListener;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LevelListDrawable;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;

public class MusicPlayActivity extends Activity implements IOnServiceConnectComplete{
    /** Called when the activity is first created. */
	private final static String TAG = "MusicPlayActivity";
	
	private final String BROCAST_NAME = "com.genius.musicplay.brocast";
	
   	private final static int REFRESH_PROGRESS_EVENT = 0x100;
   	
   	private final static int ABOUT_DIALOG_ID = 1;
	
	private Handler mHandler;
	
	private UIManager mUIManager;
	
	private ServiceManager mServiceManager;
	
	private MusicTimer mMusicTimer;
	
	private MusicPlayStateBrocast mPlayStateBrocast;

	private SDStateBrocast mSDStateBrocast;
	
	private List<MusicData> m_MusicFileList;
	
	private ListViewAdapter mListViewAdapter;
	
	private boolean mIsSdExist = false;
	private boolean mIsHaveData = false;
	
	private int mCurMusicTotalTime = 0;
	
	private int mCurPlayMode = MusicPlayMode.MPM_LIST_LOOP_PLAY;
	

	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    	
        long time1 = System.currentTimeMillis();
        init();
    	long time2 = System.currentTimeMillis();
    	Log.i(TAG, "initView	cost = " + (time2 - time1));
    }
    
    
    
    @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		mMusicTimer.stopTimer();
		unregisterReceiver(mPlayStateBrocast);
		unregisterReceiver(mSDStateBrocast);
		mServiceManager.disconnectService();
		
	
	}

    public void onResume()
    {
    	super.onResume();
    	Log.i(TAG, "onResume");
    }
    
    public void onStart()
    {
    	super.onStart();
    	Log.i(TAG, "onStart");
    }
    
   
    

	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		
		switch(id)
		{
		case ABOUT_DIALOG_ID:
		{
    		Dialog aboutDialog = new AlertDialog.Builder(MusicPlayActivity.this)
            .setIcon(R.drawable.about_dialog_icon)
            .setTitle(R.string.about_title_name)
            .setMessage(R.string.about_content)
            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    /* User clicked OK so do some stuff */
                }
            }).create();
    		
    		return aboutDialog;
		}
		default:
			break;
		}
		
		return null;
	}



	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
		mUIManager.Back();
	}



	public void init()
    {
		mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				
				switch(msg.what)
				{
					case REFRESH_PROGRESS_EVENT:
						mUIManager.setPlayInfo(mServiceManager.getCurPosition(), mCurMusicTotalTime, null);
						break;
					default:
						break;
				}		
			}
			
		};
		
		mUIManager = new UIManager();
		
		mServiceManager = new ServiceManager(this);
		mServiceManager.setOnServiceConnectComplete(this);
		mServiceManager.connectService();
	
	
		mMusicTimer = new MusicTimer(mHandler, REFRESH_PROGRESS_EVENT);

		m_MusicFileList = new ArrayList<MusicData>();
		mListViewAdapter = new ListViewAdapter(this, m_MusicFileList);
		mUIManager.mListView.setAdapter(mListViewAdapter);
	
		
		mPlayStateBrocast = new MusicPlayStateBrocast();
		IntentFilter intentFilter1 = new IntentFilter(BROCAST_NAME);
		registerReceiver(mPlayStateBrocast, intentFilter1);
		
		mSDStateBrocast = new SDStateBrocast();
		IntentFilter intentFilter2 = new IntentFilter();
		intentFilter2.addAction(Intent.ACTION_MEDIA_MOUNTED);
        intentFilter2.addAction(Intent.ACTION_MEDIA_UNMOUNTED);    
        intentFilter2.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED); 
        intentFilter2.addAction(Intent.ACTION_MEDIA_EJECT);    
        intentFilter2.addDataScheme("file"); 
        registerReceiver(mSDStateBrocast, intentFilter2);
    }
	
    private List<MusicData> getMusicFileList()
    {
    	List<MusicData> list = new ArrayList<MusicData>();
    
    	
    	String[] projection = new String[]{MediaStore.Audio.Media._ID, 
    									MediaStore.Audio.Media.TITLE, 
    									MediaStore.Audio.Media.DURATION,
    									MediaStore.Audio.Media.DATA,
    									MediaStore.Audio.Media.ARTIST};   
    	
    	long time1 = System.currentTimeMillis();
    	Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI , projection, null, null, null);
    	if (cursor != null)
    	{
    		cursor.moveToFirst();

		    int colNameIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int colTimeIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int colPathIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int colArtistIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            
         
            int fileNum = cursor.getCount();  
            for(int counter = 0; counter < fileNum; counter++){        
                
                MusicData data = new MusicData();
                data.mMusicName = cursor.getString(colNameIndex);
                data.mMusicTime = cursor.getInt(colTimeIndex);
                data.mMusicPath = cursor.getString(colPathIndex);
                data.mMusicAritst = cursor.getString(colArtistIndex);
                
                list.add(data);
                cursor.moveToNext();
            }
            
            cursor.close();
    	}
    	long time2 = System.currentTimeMillis();
    	
    	Log.i(TAG, "seach filelist cost = " + (time2 - time1));
    	return list;
    }

    public void showNoData()
    {
    	Toast.makeText(this, "No Music Data...", Toast.LENGTH_SHORT).show();
    }
	   
	public void rePlay()
	{
		if (mIsHaveData == false)
		{
			showNoData();
		}else{
			mServiceManager.rePlay();
		}
		
		
	}
	   
	public void play(int position)
	{
		if (mIsHaveData == false)
		{
			showNoData();
		}else{
			mServiceManager.play(position);
		}

	}
	   
	public void pause()
	{
		mServiceManager.pause();
	}
	   
	public void stop()
	{
		mServiceManager.stop();
	}
	   
	   
	public void playPre()
	{
		if (mIsHaveData == false)
		{
			showNoData();
		}else{
			mServiceManager.playPre();
		}
		
	}
	   
	public void playNext()
	{
		if (mIsHaveData == false)
		{
			showNoData();
		}else{
			mServiceManager.playNext();
		}
	
	}

	   
	public void seekTo(int rate)
	{
		mServiceManager.seekTo(rate);
	}
	   
	public void exit()
	{
		mServiceManager.exit();
		finish();
	}
	
	public void modeChange()
	{
		mCurPlayMode++;
		if (mCurPlayMode > MusicPlayMode.MPM_RANDOM_PLAY)
		{
			mCurPlayMode = MusicPlayMode.MPM_SINGLE_LOOP_PLAY;
		}
		
		mServiceManager.setPlayMode(mCurPlayMode);
		mUIManager.setPlayMode(mCurPlayMode, true);
	}
    
	public void onMenuItemClick(int pos)
	{
		switch (pos) {
		case 0:						// 设置		
			Toast.makeText(this, "该功能尚未开放,请下版本再试", Toast.LENGTH_SHORT).show();
			break;
		case 1:						// 关于
			showDialog(ABOUT_DIALOG_ID);
			break;
		case 2:						// 云同步
			Toast.makeText(this, "该服务尚未开通,请购买后使用", Toast.LENGTH_SHORT).show();
			break;
		case 3:						// 退出
			exit();
			break;
		default:
			break;
		}
	}
	
	@Override
	public void OnServiceConnectComplete() {
		// TODO Auto-generated method stub
		
		Log.i(TAG, "OnServiceConnectComplete.......");
		
     //   long time3 = System.currentTimeMillis();
        
		String state = Environment.getExternalStorageState().toString();
		if (state.equals(Environment.MEDIA_MOUNTED))
		{
			mIsSdExist = true;
		}else{
			Toast.makeText(this, "SD卡未安装，建议安装SD卡", Toast.LENGTH_SHORT).show();
			return ;
		}
	
		int playState = mServiceManager.getPlayState();
		switch(playState)
		{
			case MusicPlayState.MPS_NOFILE:
				m_MusicFileList = getMusicFileList();
			
				mServiceManager.refreshMusicList(m_MusicFileList);
				break;
			case MusicPlayState.MPS_INVALID:
			case MusicPlayState.MPS_PREPARE:
			case MusicPlayState.MPS_PLAYING:
			case MusicPlayState.MPS_PAUSE:
				long time1 = System.currentTimeMillis();
				m_MusicFileList = mServiceManager.getFileList();
				long time2 = System.currentTimeMillis();
				Log.i(TAG, "mServiceManager.getFileList()	cost = " + (time2 - time1));
				mServiceManager.sendPlayStateBrocast();
				break;
			default:
				break;
		}
		
		if (m_MusicFileList.size() > 0)
		{	
			mIsHaveData = true;
		}
		
		mListViewAdapter.refreshAdapter(m_MusicFileList);

		mUIManager.setPlayMode(mServiceManager.getPlayMode(), false);
		
	// 	long time4 = System.currentTimeMillis();
    //	Log.e(TAG, "OnServiceConnectComplete	cost = " + (time4 - time3));
	}

    
	class  SDStateBrocast extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			 String action = intent.getAction(); 
			 
			if (action.equals(Intent.ACTION_MEDIA_MOUNTED))
			  {
			//	  Log.i(TAG, "===================>Intent.ACTION_MEDIA_MOUNTED");		
				  mIsSdExist = true;
			  }else if (action.equals(Intent.ACTION_MEDIA_UNMOUNTED))
			  {
			//	  Log.i(TAG, "===================>Intent.ACTION_MEDIA_UNMOUNTED");
				  mIsSdExist = false;
				 
			  }else if (Intent.ACTION_MEDIA_SCANNER_FINISHED.equals(action))
			  {
				//  Log.i(TAG, "===================>Intent.ACTION_MEDIA_SCANNER_FINISHED	mIsSdExist = " + mIsSdExist);
				  if (mIsSdExist)
				  {
					  m_MusicFileList = getMusicFileList();
					  mServiceManager.refreshMusicList(m_MusicFileList);
					  if (m_MusicFileList.size() > 0)
					  {	
						mIsHaveData = true;
					  }
					  mListViewAdapter.refreshAdapter(m_MusicFileList);
				  }
				  
			  }else if (Intent.ACTION_MEDIA_EJECT.equals(action))
			  {
				 // Log.i(TAG, "===================>Intent.ACTION_MEDIA_EJECT");
				//  mServiceManager.reset();
				  m_MusicFileList.clear();
				  mListViewAdapter.refreshAdapter(m_MusicFileList);
				  mIsHaveData = false;
				  mUIManager.emptyPlayInfo();
			  }
			
		}
		
	}
	
	
	class  MusicPlayStateBrocast extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

			  String action = intent.getAction(); 
			  if (action.equals(BROCAST_NAME))
			  {
				  TransPlayStateEvent(intent);
			  }
			
		}
		
		
		public void TransPlayStateEvent(Intent intent)
		{
			MusicData data = new MusicData();
			int playState = intent.getIntExtra(MusicPlayState.PLAY_STATE_NAME, -1);
			Bundle bundle = intent.getBundleExtra(MusicData.KEY_MUSIC_DATA);
			if (bundle != null)
			{
				data = bundle.getParcelable(MusicData.KEY_MUSIC_DATA);
			}
			int playIndex = intent.getIntExtra(MusicPlayState.PLAY_MUSIC_INDEX, -1);

			switch (playState) {
			case MusicPlayState.MPS_INVALID:
				mMusicTimer.stopTimer();
				Toast.makeText(MusicPlayActivity.this, "当前音乐文件无效", Toast.LENGTH_SHORT).show();
				mUIManager.setPlayInfo(0, data.mMusicTime, data.mMusicName);
				mUIManager.showPlay(true);
				break;
			case MusicPlayState.MPS_PREPARE:
				mMusicTimer.stopTimer();
				mCurMusicTotalTime = data.mMusicTime;
				if (mCurMusicTotalTime == 0)
				{
					mCurMusicTotalTime = mServiceManager.getDuration();
				}
				mUIManager.setPlayInfo(0, data.mMusicTime, data.mMusicName);
				mUIManager.showPlay(true);
				break;
			case MusicPlayState.MPS_PLAYING:
				mMusicTimer.startTimer();
				if (mCurMusicTotalTime == 0)
				{
					mCurMusicTotalTime = mServiceManager.getDuration();
				}
				mUIManager.setPlayInfo(mServiceManager.getCurPosition(), data.mMusicTime, data.mMusicName);
				mUIManager.showPlay(false);
				break;
			case MusicPlayState.MPS_PAUSE:
				mMusicTimer.stopTimer();
				if (mCurMusicTotalTime == 0)
				{
					mCurMusicTotalTime = mServiceManager.getDuration();
				}	
				mUIManager.setPlayInfo(mServiceManager.getCurPosition(), data.mMusicTime, data.mMusicName);
				mUIManager.showPlay(true);
				break;
			default:
				break;
			}
			
			mUIManager.setSongNumInfo(playIndex, m_MusicFileList.size());
			
			mListViewAdapter.setPlayState(playIndex, playState);
		}
		
		
	}
	
	

	public boolean onMenuOpened(int featureId, Menu menu) {		// 截获菜单事件
		// TODO Auto-generated method stub

		mUIManager.showMenuWindow();
		
		return false;		// 返回为true 则显示系统menu
		
	}


	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub

		menu.add("menu");// 必须创建一项
		return super.onCreateOptionsMenu(menu);
		
	}  
	
    
    class UIManager implements OnItemClickListener{
    	
    	private final static String TAG = "UIManager";
    	
    	private SliderDrawerManager mSliderDrawerManager;
    	
    	private PopMenuManager 		mPopMenuManager;
    	
    	public ListView				mListView;

    	private View        		mMusicListView;
    	  
  	
    	private int mModeDrawableIDS[] = {	R.drawable.mode_single_loop,
				R.drawable.mode_order,
				R.drawable.mode_list_loop,
				R.drawable.mode_random
    	};
    	
    	private String modeToasts[] = {"单曲循环",
    									"顺序播放",
    									"列表循环",
    									"随机播放"
    	};
    	
    	public UIManager()
    	{ 		
    		initView();
    	}
    	
    	private void initView()
    	{
  
        	mListView = (ListView) findViewById(R.id.listView);
        	mListView.setOnItemClickListener(this);
        	mMusicListView = findViewById(R.id.ListLayout);
        	
    		mSliderDrawerManager = new SliderDrawerManager();
    		mPopMenuManager = new PopMenuManager();
    	}
    	
    	public void setPlayInfo(int curTime, int totalTime, String musicName)
    	{
    		curTime /= 1000;
    		totalTime /= 1000;
    		int curminute = curTime/60;
    		int cursecond = curTime%60;
    		
    		String curTimeString = String.format("%02d:%02d", curminute,cursecond);
    		
    		int totalminute = totalTime/60;
    		int totalsecond = totalTime%60;
    		String totalTimeString = String.format("%02d:%02d", totalminute,totalsecond);
    		
    		int rate = 0;
    		if (totalTime != 0)
    		{
    			rate = (int) ((float)curTime / totalTime * 100);       		       
    		}
    		
    		mSliderDrawerManager.mPlayProgress.setProgress(rate);
    		
    		mSliderDrawerManager.mcurtimeTextView.setText(curTimeString);
    		mSliderDrawerManager.mtotaltimeTextView.setText(totalTimeString);
    		if (musicName != null)
    		{
    			mSliderDrawerManager.mPlaySongTextView.setText(musicName);	
    		}
    	
    	}
    	
    	
    	public void emptyPlayInfo()
    	{
    		mSliderDrawerManager.mPlayProgress.setProgress(0);	
    		mSliderDrawerManager.mcurtimeTextView.setText("00:00");
    		mSliderDrawerManager.mtotaltimeTextView.setText("00:00");
    		mSliderDrawerManager.mPlaySongTextView.setText(R.string.default_title_name);	
    		
    	}
    	
    	public void setSongNumInfo(int curPlayIndex, int totalSongNum)
    	{
    		String str = String.valueOf(curPlayIndex + 1) + "/" + String.valueOf(totalSongNum);
    		
    		mSliderDrawerManager.mSongNumTextView.setText(str);
    	}
    	
    	public void showPlay(boolean flag)
    	{
    		if (flag)
    		{
    			mSliderDrawerManager.mBtnPlay.setVisibility(View.VISIBLE);
    			mSliderDrawerManager.mBtnPause.setVisibility(View.GONE);
    			mSliderDrawerManager.mBtnHandlePlay.setVisibility(View.VISIBLE);
    			mSliderDrawerManager.mBtnHandlePause.setVisibility(View.INVISIBLE);
        		
    		}else{
    			mSliderDrawerManager.mBtnPlay.setVisibility(View.GONE);
    			mSliderDrawerManager.mBtnPause.setVisibility(View.VISIBLE);
    			mSliderDrawerManager.mBtnHandlePlay.setVisibility(View.INVISIBLE);
    			mSliderDrawerManager.mBtnHandlePause.setVisibility(View.VISIBLE);
    		}
    		
    	}
    	
    	public void ShowHandlePanel(boolean flag)
    	{
    		if (flag)
    		{
    			mSliderDrawerManager.mHandlePane.setVisibility(View.VISIBLE);
    		}else{
    			mSliderDrawerManager.mHandlePane.setVisibility(View.INVISIBLE);
    		}
    		
    	}
    	
    	public void setPlayMode(int mode, Boolean bShowToast)
    	{
    		if (mode >= MusicPlayMode.MPM_SINGLE_LOOP_PLAY && mode <= MusicPlayMode.MPM_RANDOM_PLAY)
    		{
    			mSliderDrawerManager.mBtnModeSet.setImageResource(mModeDrawableIDS[mode]);
        		
        		if (bShowToast)
        		{
        			Toast.makeText(MusicPlayActivity.this, modeToasts[mode], Toast.LENGTH_SHORT).show();	
        		}
        		
    		}

    	}

    	public void Back()
    	{
    		if (mSliderDrawerManager.mSlidingDrawer.isOpened())
    		{
    			mSliderDrawerManager.mSlidingDrawer.close();
    		}else{
    			finish();
    		}
    	}
    	
    	public void showMenuWindow()
    	{
    		mPopMenuManager.showMenuWindow();
    	}

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
				long arg3) {
			// TODO Auto-generated method stub
			
			play(pos);		
			
		}
		
		

		
		
		
		class SliderDrawerManager  implements OnClickListener, OnSeekBarChangeListener, 
		OnDrawerOpenListener, OnDrawerCloseListener, IOnSliderHandleViewClickListener
		{
			public MySlidingDrawer mSlidingDrawer;
			

			public ImageButton   mBtnHandle;
			public ImageButton   mBtnHandlePlay;
			public ImageButton   mBtnHandlePause;
			public TextView 	  mSongNumTextView;
			public View 		  mHandlePane;
			public TextView mPlaySongTextView;
		
	    	
			
			public ImageButton mBtnModeSet;
			public ImageButton mBtnVolumnSet;
			public SeekBar mPlayProgress;
			public TextView mcurtimeTextView;
			public TextView mtotaltimeTextView;
	    	
	    	
			public ImageButton mBtnPlay;
			public ImageButton mBtnPause;
			public ImageButton mBtnStop;	    		    	
			public ImageButton mBtnPlayNext;
			public ImageButton mBtnPlayPre;
			public ImageButton mBtnMenu;    	  
	    	
	    	private boolean mPlayAuto = true;
	    	
	    	
	    	private GalleryFlow mGalleryFlow1;
	    	private GalleryFlow mGalleryFlow2;
	    	
	    	
			public SliderDrawerManager()
			{
				initView();
			}
			
			private void initView()
			{
				mBtnPlay = (ImageButton) findViewById(R.id.buttonPlay);
	        	mBtnPause = (ImageButton) findViewById(R.id.buttonPause);
	        	mBtnStop = (ImageButton) findViewById(R.id.buttonStop);
	        	mBtnPlayPre = (ImageButton) findViewById(R.id.buttonPlayPre);      	
	        	mBtnPlayNext = (ImageButton) findViewById(R.id.buttonPlayNext);      	
	            mBtnMenu = (ImageButton) findViewById(R.id.buttonMenu);
	            mBtnModeSet = (ImageButton) findViewById(R.id.buttonMode);
	            mBtnVolumnSet = (ImageButton) findViewById(R.id.buttonVolumn);
	        	mBtnHandle = (ImageButton) findViewById(R.id.handler_icon);
	        	mBtnHandlePlay= (ImageButton) findViewById(R.id.handler_play);
	        	mBtnHandlePause = (ImageButton) findViewById(R.id.handler_pause);
	            mBtnPlay.setOnClickListener(this);
	            mBtnPause.setOnClickListener(this);
	            mBtnStop.setOnClickListener(this);
	            mBtnPlayPre.setOnClickListener(this);
	            mBtnPlayNext.setOnClickListener(this);
	            mBtnStop.setOnClickListener(this);
	            mBtnMenu.setOnClickListener(this);	        	
	        	mBtnModeSet.setOnClickListener(this);
	        	mBtnVolumnSet.setOnClickListener(this);
	            
	            
	            mPlaySongTextView = (TextView) findViewById(R.id.textPlaySong);        	
	        	mcurtimeTextView = (TextView) findViewById(R.id.textViewCurTime);
	        	mtotaltimeTextView = (TextView) findViewById(R.id.textViewTotalTime);
	        	mSongNumTextView = (TextView) findViewById(R.id.textSongNum);
	        	
	        		    
	        	
	        	mPlayProgress = (SeekBar) findViewById(R.id.seekBar);
	        	mPlayProgress.setOnSeekBarChangeListener(this);
	        	
	       
	        	mSlidingDrawer = (MySlidingDrawer) findViewById(R.id.slidingDrawer);
	        	mSlidingDrawer.setOnDrawerOpenListener(this);
	        	mSlidingDrawer.setOnDrawerCloseListener(this);
	        	mSlidingDrawer.setHandleId(R.id.handler_icon);
	        	mSlidingDrawer.setTouchableIds(new int[]{R.id.handler_play, R.id.handler_pause});
	        	mSlidingDrawer.setOnSliderHandleViewClickListener(this);
	        	
     	
	        
	        	mHandlePane = findViewById(R.id.handle_panel);
	        	
	        	
	            Integer[] images1 = { R.drawable.ablumlayout_bg }; 
	            ImageAdapter adapter1 = new ImageAdapter(MusicPlayActivity.this, images1);
	            adapter1.createReflectedImages();
	            mGalleryFlow1 = (GalleryFlow) findViewById(R.id.imageGallery1);
	            mGalleryFlow1.setAdapter(adapter1);
	            
	            Integer[] images2 = { R.drawable.ablum_deflaut }; 
	            ImageAdapter adapter2 = new ImageAdapter(MusicPlayActivity.this, images2);
	            adapter2.createReflectedImages(23);
	            mGalleryFlow2 = (GalleryFlow) findViewById(R.id.imageGallery2);
	            mGalleryFlow2.setAdapter(adapter2);
			}

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch(v.getId())
				{
				case R.id.buttonPlay:
					rePlay();
					break;
				case R.id.buttonPause:
					pause();
					break;
				case R.id.buttonStop:
					stop();
					break;
				case R.id.buttonPlayPre:
					playPre();
					break;
				case R.id.buttonPlayNext:
					playNext();
					break;
				case R.id.buttonMenu:
					showMenuWindow();
					break;
				case R.id.buttonMode:
					modeChange();
					break;
				case R.id.buttonVolumn:
					Toast.makeText(MusicPlayActivity.this, "别摁了,这个按钮摆设用的", Toast.LENGTH_SHORT).show();					;
					break;
				default:
					break;
				}
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				if (mPlayAuto == false)
				{
					mServiceManager.seekTo(progress);
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				mPlayAuto = false;
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				mPlayAuto = true;
			}
		

			@Override
			public void onDrawerOpened() {
				// TODO Auto-generated method stub
				mMusicListView.setVisibility(View.INVISIBLE);
				mBtnHandle.setImageResource(R.drawable.handle_down);
				ShowHandlePanel(false);
			}

			@Override
			public void onDrawerClosed() {
				// TODO Auto-generated method stub
				mMusicListView.setVisibility(View.VISIBLE);
				mBtnHandle.setImageResource(R.drawable.handle_up);
				ShowHandlePanel(true);
			}

			@Override
			public void onViewClick(View view) {
				// TODO Auto-generated method stub
				switch(view.getId())
				{
				case R.id.handler_play:
					rePlay();
					break;
				case R.id.handler_pause:
					pause();
					break;
				default:
					break;
				}
			}
		
		}
		
		
		
		
		
		
		class PopMenuManager implements android.widget.PopupWindow.OnDismissListener{
			
			MenuItemData    		mMenuItemData;
			private GridView 		mMenuGrid;			// 弹出菜单GRIDVIEW
			private View 			mMenuView;			// 弹出菜单视图
			private GridViewAdapter	mGridViewAdapter;	// 弹出菜单适配器
			private PopupWindow		mPopupWindow;		// 弹出菜单WINDOW
			
			private View 			mPopBackgroundView;
			
			public PopMenuManager()
	    	{ 		
	    		initView();
	    	}
	    	
	    	private void initView()
	    	{
	    		mPopBackgroundView = findViewById(R.id.VirtualLayout);
	    		
	        	String []menu_name_array1 = getResources().getStringArray(R.array.menu_item_name_array);	
	        	LevelListDrawable levelListDrawable1 = (LevelListDrawable) getResources().getDrawable(R.drawable.menu_image_list);
	        	mMenuItemData = new MenuItemData(levelListDrawable1, menu_name_array1, menu_name_array1.length);
	        	
	        	LayoutInflater inflater = getLayoutInflater();
	        	mMenuView = inflater.inflate(R.layout.menu, null);
	    		mMenuGrid = (GridView)mMenuView.findViewById(R.id.menuGridView);
	    		

	    		mGridViewAdapter = new GridViewAdapter(MusicPlayActivity.this, mMenuItemData);
	    		mMenuGrid.setAdapter(mGridViewAdapter);

	    		
	        	mMenuGrid.setOnItemClickListener(new OnItemClickListener() {

	    			@Override
	    			public void onItemClick(AdapterView<?> parent, View view,
	    					int position, long id) {
	    				// TODO Auto-generated method stub		
	    				onMenuItemClick(position);
	    			}

	        		
	    		});        		
	       		
	       		mMenuGrid.setOnKeyListener(new OnKeyListener() {
					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event) {
						// TODO Auto-generated method stub
						switch(keyCode)
	    				{
	    					case KeyEvent.KEYCODE_MENU:
	    						showMenuWindow();
	    						break;
	    				}
	    				
	    				return false;
					}
	    		}); 
	       		
	       		
	    		mPopupWindow = new PopupWindow(mMenuView, LayoutParams.FILL_PARENT,  LayoutParams.WRAP_CONTENT);
	    		mPopupWindow.setFocusable(true);		// 如果没有获得焦点menu菜单中的控件事件无法响应
	    		
	    		//	以下两行加上去后就可以使用BACK键关闭POPWINDOW
	    		ColorDrawable dw = new ColorDrawable(0x00);
	    		mPopupWindow.setBackgroundDrawable(dw);
	    		
	    	    mPopupWindow.setAnimationStyle(android.R.style.Animation_Toast);  
	    	    mPopupWindow.setOnDismissListener(this);
	    	}
	    	
	    	  
	        public void showMenuWindow()
	        {
	        	if (mPopupWindow.isShowing())
	        	{
	        		mPopupWindow.dismiss();
	        		mPopBackgroundView.setVisibility(View.INVISIBLE);
	        	}else{
	        		mPopupWindow.showAsDropDown(findViewById(R.id.mainLayout), 0, -125);
	        		mPopBackgroundView.setVisibility(View.VISIBLE);
	        	}

	        }

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				mPopBackgroundView.setVisibility(View.INVISIBLE);
			}
	    	
		}
  	
    }

 
}