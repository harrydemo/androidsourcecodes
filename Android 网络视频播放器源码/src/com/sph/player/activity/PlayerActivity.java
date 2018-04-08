// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   PlayerActivity.java

package com.sph.player.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.*;
import android.util.Log;
import android.view.*;
import android.widget.*;

import com.sph.player.R;
import com.sph.player.helper.SystemUtility;
import com.sph.player.player.*;
import java.util.ArrayList;

public class PlayerActivity extends Activity
	implements com.sph.player.player.AbsMediaPlayer.OnBufferingUpdateListener, com.sph.player.player.AbsMediaPlayer.OnCompletionListener, com.sph.player.player.AbsMediaPlayer.OnErrorListener, com.sph.player.player.AbsMediaPlayer.OnInfoListener, com.sph.player.player.AbsMediaPlayer.OnPreparedListener, com.sph.player.player.AbsMediaPlayer.OnProgressUpdateListener, com.sph.player.player.AbsMediaPlayer.OnVideoSizeChangedListener, android.view.View.OnTouchListener, android.view.View.OnClickListener, android.widget.SeekBar.OnSeekBarChangeListener
{

	static final String LOGTAG = "DANMAKU-PlayerActivity";
	private static final int MEDIA_PLAYER_BUFFERING_UPDATE = 16385;
	private static final int MEDIA_PLAYER_COMPLETION = 16386;
	private static final int MEDIA_PLAYER_ERROR = 16387;
	private static final int MEDIA_PLAYER_INFO = 16388;
	private static final int MEDIA_PLAYER_PREPARED = 16389;
	private static final int MEDIA_PLAYER_PROGRESS_UPDATE = 16390;
	private static final int MEDIA_PLAYER_VIDEO_SIZE_CHANGED = 16391;
	private static final int SURFACE_16_10 = 5;
	private static final int SURFACE_16_9 = 4;
	private static final int SURFACE_4_3 = 3;
	private static final int SURFACE_FILL = 1;
	private static final int SURFACE_MAX = 6;
	private static final int SURFACE_NONE = 0;
	private static final int SURFACE_ORIG = 2;
	private ImageView bg;
	private boolean bufferPercent;
	private Context ct;
	private String finalOpenUrl;
	private int mAspectRatio;
	AudioManager mAudioManager;
	private int mAudioTrackCount;
	private int mAudioTrackIndex;
	private boolean mCanSeek;
	private Handler mEventHandler;
	private ImageButton mImageButtonNext;
	private ImageButton mImageButtonPrevious;
	private ImageButton mImageButtonSwitchAspectRatio;
	private ImageButton mImageButtonSwitchAudio;
	private ImageButton mImageButtonSwitchSubtitle;
	private ImageButton mImageButtonToggleMessage;
	private ImageButton mImageButtonTogglePlay;
	private int mLength;
	private LinearLayout mLinearLayoutControlBar;
	private AbsMediaPlayer mMediaPlayer;
	private boolean mMediaPlayerLoaded;
	private boolean mMediaPlayerStarted;
	private ArrayList mPlayListArray;
	private int mPlayListSelected;
	private ProgressBar mProgressBarPreparing;
	SeekBar mSeekBarMusic;
	private SeekBar mSeekBarProgress;
	private int mSubtitleTrackCount;
	private int mSubtitleTrackIndex;
	private SurfaceHolder mSurfaceHolderDef;
	private SurfaceHolder mSurfaceHolderVlc;
	private SurfaceView mSurfaceViewDef;
	private SurfaceView mSurfaceViewVlc;
	private TextView mTextViewLength;
	private TextView mTextViewTime;
	private int mTime;

	public PlayerActivity()
	{
		mMediaPlayer = null;
		mPlayListArray = null;
		mPlayListSelected = -1;
		finalOpenUrl = "";
		bufferPercent = false;
		mMediaPlayerLoaded = false;
		mMediaPlayerStarted = false;
		mTime = -1;
		mLength = -1;
		mCanSeek = true;
		mAspectRatio = 0;
		mAudioTrackIndex = 0;
		mAudioTrackCount = 0;
		mSubtitleTrackIndex = 0;
		mSubtitleTrackCount = 0;
		bg = null;
	}

	private static boolean isDefMediaPlayer(Object obj)
	{
		String s = obj.getClass().getName();
		String s1 =com.sph.player.player.DefMediaPlayer.class.getName();
		boolean flag;
		if (s.compareTo(s1) == 0)
			flag = true;
		else
			flag = false;
		return flag;
	}

	private static boolean isVlcMediaPlayer(Object obj)
	{
		String s = obj.getClass().getName();
		String s1 = com.sph.player.player.VlcMediaPlayer.class.getName();
		boolean flag;
		if (s.compareTo(s1) == 0)
			flag = true;
		else
			flag = false;
		return flag;
	}

	protected void changeSurfaceSize(AbsMediaPlayer absmediaplayer, SurfaceView surfaceview, int i)
	{
		int video_width = absmediaplayer.getVideoWidth();
		int video_height = absmediaplayer.getVideoHeight();
		
		if (video_width > 0 && video_height > 0){
			int width;
			int height;
			int j1 = -1;
			int k1 = -1;
			surfaceview.getHolder().setFixedSize(video_width, video_height);
			width = getWindowManager().getDefaultDisplay().getWidth();
			height = getWindowManager().getDefaultDisplay().getHeight();
			video_width = width;
			video_height = height;
			
			System.out.println("i = "+i);
			
			switch(i){
				case 1:
					j1 = 4;
					k1 = 3;
					break;
			}
			android.view.ViewGroup.LayoutParams layoutparams;
			
			System.out.println("j1 = "+j1+" k1 = "+k1);
			if (j1 > 0 && k1 > 0){
				double d2 = video_width / video_height;
				double d5 = j1 / k1;
				video_width = (video_height * j1) / k1;
				video_height = (video_width * k1) / j1;
			}
			layoutparams = surfaceview.getLayoutParams();
			layoutparams.width = video_width;
			layoutparams.height = video_height;
			surfaceview.setLayoutParams(layoutparams);
			surfaceview.invalidate();
			
		}else {
			return;
		}
	}

	protected void createMediaPlayer(boolean flag, String s, SurfaceHolder surfaceholder)
	{
		String s1 = (new StringBuilder("createMediaPlayer() ")).append(s).toString();
		int i = Log.d("DANMAKU-PlayerActivity", s1);
		resetMediaPlayer();
		AbsMediaPlayer absmediaplayer = AbsMediaPlayer.getMediaPlayer(flag);
		mMediaPlayer = absmediaplayer;
		mMediaPlayer.setOnBufferingUpdateListener(this);
		mMediaPlayer.setOnCompletionListener(this);
		mMediaPlayer.setOnErrorListener(this);
		mMediaPlayer.setOnInfoListener(this);
		mMediaPlayer.setOnPreparedListener(this);
		mMediaPlayer.setOnProgressUpdateListener(this);
		mMediaPlayer.setOnVideoSizeChangedListener(this);
		mMediaPlayer.reset();
		mMediaPlayer.setDisplay(surfaceholder);
		mMediaPlayer.setDataSource(s);
		mMediaPlayer.prepareAsync();
	}

	protected void destroyMediaPlayer(boolean flag)
	{
		boolean flag1 = isDefMediaPlayer(mMediaPlayer);
		if (flag == flag1)
		{
			mMediaPlayer.setDisplay(null);
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}

	protected void initializeControls()
	{
		//获取播放区域控件
		mSurfaceViewVlc = (SurfaceView)findViewById(R.id.player_surface_vlc);
		//取得这个SurfaceView的SurfaceHolder
		mSurfaceHolderVlc = mSurfaceViewVlc.getHolder();
		//相关属性配置
		mSurfaceHolderVlc.setType(0);
		SurfaceHolderCallback  mSurfaceHolderCallback = new SurfaceHolderCallback();
		mSurfaceHolderVlc.addCallback(mSurfaceHolderCallback);
		mSurfaceViewVlc.setOnTouchListener(this);

		
		mSurfaceViewDef = (SurfaceView)findViewById(R.id.player_surface_def);
		mSurfaceViewDef.setOnTouchListener(this);
		mSurfaceHolderDef = mSurfaceViewDef.getHolder();
		mSurfaceHolderDef.setType(3);
		SurfaceHolderCallback2 mSurfaceHolderCallback2 = new SurfaceHolderCallback2();
		mSurfaceHolderDef.addCallback(mSurfaceHolderCallback2);
		
		
		//显示时间控件
		mTextViewTime = (TextView)findViewById(R.id.player_text_position);
		
		//显示播放进度控件
		mSeekBarProgress = (SeekBar)findViewById(R.id.player_seekbar_progress);
		mSeekBarProgress.setOnSeekBarChangeListener(this);
		
		
		//显示长度的控件
		mTextViewLength = (TextView)findViewById(R.id.player_text_length);
		

		mImageButtonTogglePlay = (ImageButton)findViewById(R.id.player_button_toggle_play);
		mImageButtonTogglePlay.setOnClickListener(this);
		
		mImageButtonSwitchAspectRatio = (ImageButton)findViewById(R.id.player_button_switch_aspect_ratio);
		mImageButtonSwitchAspectRatio.setOnClickListener(this);
		
		mLinearLayoutControlBar = (LinearLayout)findViewById(R.id.player_control_bar);

		mProgressBarPreparing = (ProgressBar)findViewById(R.id.player_prepairing);
	}

	protected void initializeData()
	{
		Intent intent = getIntent();
		String s = intent.getAction();
		if (s != null && s.equals("android.intent.action.VIEW"))
		{
			mPlayListSelected = 0;
			ArrayList arraylist = new ArrayList();
			mPlayListArray = arraylist;
			ArrayList arraylist1 = mPlayListArray;
			String s1 = finalOpenUrl;
			boolean flag = arraylist1.add(s1);
		} else
		{
			int j = intent.getIntExtra("selected", 0);
			mPlayListSelected = j;
			ArrayList arraylist2 = intent.getStringArrayListExtra("playlist");
			mPlayListArray = arraylist2;
		}
		if (mPlayListArray == null || mPlayListArray.size() == 0)
		{
			int i = Log.e("DANMAKU-PlayerActivity", "initializeData(): empty");
			finish();
		}
	}

	protected void initializeEvents()
	{
		mEventHandler = new Handler(){
			public void handleMessage(Message message){
				System.out.println("handleMessage message.what = "+message.what);
				
				switch(message.what){
				
				//16385
				case MEDIA_PLAYER_BUFFERING_UPDATE:
					System.out.println("MEDIA_PLAYER_BUFFERING_UPDATE");
					System.out.println("mMediaPlayerLoaded = "+mMediaPlayerLoaded);
					if (mMediaPlayerLoaded){
						int i;
						if (message.arg1 < 100){
							i = 0;
						}
						else{
							i = 8;
						}
						System.out.println("i = "+i);
						mProgressBarPreparing.setVisibility(i);
					}
					break;
					
				//16386	
				case MEDIA_PLAYER_COMPLETION:
					System.out.println("MEDIA_PLAYER_COMPLETION");
					finish();
					break;
					
				//16387	
				case MEDIA_PLAYER_ERROR:
					System.out.println("MEDIA_PLAYER_ERROR");
					
					if (PlayerActivity.isDefMediaPlayer(message.obj)){
						PlayerActivity playeractivity = PlayerActivity.this;
						String s = (String)mPlayListArray.get(mPlayListSelected);
						playeractivity.selectMediaPlayer(s, true);
					} else{
						if (PlayerActivity.isVlcMediaPlayer(message.obj)){
							mMediaPlayerLoaded = true;
							mSurfaceViewVlc.setVisibility(8);
						}
						if (mMediaPlayerLoaded){
							mProgressBarPreparing.setVisibility(8);
						}
						startMediaPlayer();
					}
					break;
				
				//16388
				case MEDIA_PLAYER_INFO:
					System.out.println("MEDIA_PLAYER_INFO");
					if (message.arg1 == 801){
						mCanSeek = false;
					}
					break;
					
				//16389
				case MEDIA_PLAYER_PREPARED:
					System.out.println("MEDIA_PLAYER_PREPARED");
					if (PlayerActivity.isDefMediaPlayer(message.obj) || 
							PlayerActivity.isVlcMediaPlayer(message.obj)){
						mMediaPlayerLoaded = true;
					}
					if (mMediaPlayerLoaded){
						mProgressBarPreparing.setVisibility(8);
						bg.setVisibility(4);
						bg.setVisibility(8);
					}
					startMediaPlayer();
					break;
				
				//16390
				case MEDIA_PLAYER_PROGRESS_UPDATE:
					System.out.println("MEDIA_PLAYER_PROGRESS_UPDATE");
					if (mMediaPlayer != null){
						int k = message.arg2;
						if (k >= 0){
							mLength = k;
							String s1 = SystemUtility.getTimeString(mLength);
							mTextViewLength.setText(s1);
							SeekBar seekbar = mSeekBarProgress;
							seekbar.setMax(mLength);
						}
						int i1 = message.arg1;
						if (i1 >= 0){
							mTime = i1;
							String s2 = SystemUtility.getTimeString(mTime);
							mTextViewTime.setText(s2);
							mSeekBarProgress.setProgress(mTime);
						}
					}
					break;
				
				//16391
				case MEDIA_PLAYER_VIDEO_SIZE_CHANGED:
					System.out.println("MEDIA_PLAYER_VIDEO_SIZE_CHANGED");
					AbsMediaPlayer absmediaplayer = (AbsMediaPlayer)message.obj;
					SurfaceView surfaceview;
					if (PlayerActivity.isDefMediaPlayer(absmediaplayer)){
						surfaceview = mSurfaceViewDef;
					}
					else{
						surfaceview = mSurfaceViewVlc;
					}
					changeSurfaceSize(absmediaplayer, surfaceview, mAspectRatio);
					break;
				}
			}
		};
	}

	public void onBufferingUpdate(AbsMediaPlayer absmediaplayer, int i)
	{
		Message message = new Message();
		message.obj = absmediaplayer;
		message.what = 16385;
		message.arg1 = i;
		boolean flag = mEventHandler.sendMessage(message);
	}

	public void onClick(View view)
	{
		if (mMediaPlayerLoaded){
			switch (view.getId()){
			//播放按钮
			case R.id.player_button_toggle_play: 
				Boolean flag = false;
				if (mMediaPlayer != null){
					flag = mMediaPlayer.isPlaying();
				}
				String s;
				Object aobj[];
				int j;
				Integer integer;
				int k;
				if (flag){
					if (mMediaPlayer != null){
						mMediaPlayer.pause();
					}
				} else{
					if (mMediaPlayer != null)
						mMediaPlayer.start();
				}
				
				s = "btn_play_%d";
				aobj = new Object[1];
				if (flag){
					j = 1;
				}
				else{
					j = 0;
				}
				integer = Integer.valueOf(j);
				aobj[0] = integer;
				k = SystemUtility.getDrawableId(String.format(s, aobj));
				
				mImageButtonTogglePlay.setBackgroundResource(k);
				break;

			//显示模式按钮切换
			case R.id.player_button_switch_aspect_ratio: 
				mAspectRatio = (mAspectRatio + 1) % 2;
				int i1;
				Object aobj1[];
				Integer integer1;
				int j1;
				
				if (mMediaPlayer != null){
					SurfaceView surfaceview;
					if (isDefMediaPlayer(mMediaPlayer)){
						surfaceview = mSurfaceViewDef;
					}
					else{
						changeSurfaceSize(mMediaPlayer, mSurfaceViewVlc, mAspectRatio);
					}
				}
				aobj1 = new Object[1];
				integer1 = Integer.valueOf(mAspectRatio);
				aobj1[0] = integer1;
				j1 = SystemUtility.getDrawableId(String.format("btn_aspect_ratio_%d", aobj1));
				mImageButtonSwitchAspectRatio.setBackgroundResource(j1);
				break;
			}
		} else{
			return;
		}
	}

	public void onCompletion(AbsMediaPlayer absmediaplayer)
	{
		Message message = new Message();
		message.obj = absmediaplayer;
		message.what = 16386;
		boolean flag = mEventHandler.sendMessage(message);
	}

	public void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		AudioManager audiomanager = (AudioManager)getSystemService("audio");
		mAudioManager = audiomanager;
		String s = getIntent().getExtras().getString("FINAL_URL");
		finalOpenUrl = s;
		ct = this;
		if (finalOpenUrl != null && finalOpenUrl.length() > 0)
		{
			//初始化消息事件
			initializeEvents();
			//设置布局文件
			setContentView(R.layout.player);
			
			//初始控件
			initializeControls();
			//设置进度条的默认显示
			mProgressBarPreparing.setVisibility(View.VISIBLE);
			//初始化数据
			initializeData();
			
			//获取背景
			bg = (ImageView)findViewById(R.id.player_page_bg);
			System.out.println("finalOpenUrl = "+finalOpenUrl);
			
			bufferPercent = false;
			selectMediaPlayer(finalOpenUrl, false);
		} else
		{
			Toast toast = Toast.makeText(this, "url is empty", 0);
			finish();
		}
	}

	public void onDestroy()
	{
		super.onDestroy();
	}

	public boolean onError(AbsMediaPlayer absmediaplayer, int i, int j)
	{
		Message message = new Message();
		message.obj = absmediaplayer;
		message.what = 16387;
		message.arg1 = i;
		message.arg2 = j;
		boolean flag = mEventHandler.sendMessage(message);
		return true;
	}

	public boolean onInfo(AbsMediaPlayer absmediaplayer, int i, int j)
	{
		Message message = new Message();
		message.obj = absmediaplayer;
		message.what = 16388;
		message.arg1 = i;
		message.arg2 = j;
		boolean flag = mEventHandler.sendMessage(message);
		return true;
	}

	public void onPrepared(AbsMediaPlayer absmediaplayer)
	{
		Message message = new Message();
		message.obj = absmediaplayer;
		message.what = 16389;
		boolean flag = mEventHandler.sendMessage(message);
	}

	public void onProgressChanged(SeekBar seekbar, int i, boolean flag)
	{
		
	}

	public void onProgressUpdate(AbsMediaPlayer absmediaplayer, int i, int j)
	{
		Message message = new Message();
		message.obj = absmediaplayer;
		message.what = 16390;
		message.arg1 = i;
		message.arg2 = j;
		boolean flag = mEventHandler.sendMessage(message);
	}

	public void onStart()
	{
		super.onStart();
	}

	public void onStartTrackingTouch(SeekBar seekbar)
	{
	}

	public void onStop()
	{
		super.onStop();
		bufferPercent = false;
		if (mMediaPlayer != null)
			mMediaPlayer.pause();
	}

	public void onStopTrackingTouch(SeekBar seekbar)
	{
		if (mMediaPlayerLoaded) {
			switch (seekbar.getId()){
			case R.id.player_seekbar_progress: 
				if (mCanSeek && mLength > 0){
					int i = seekbar.getProgress();
					if (mMediaPlayer != null){
						mMediaPlayer.seekTo(i);
					}
				}
				break;
			}
		} else{
			
		}
	}

	public boolean onTouch(View view, MotionEvent motionevent)
	{
		boolean flag;
		if (!mMediaPlayerLoaded)
			flag = true;
		else
		if (motionevent.getAction() == 0)
		{
			if (mLinearLayoutControlBar.getVisibility() != 0)
				mLinearLayoutControlBar.setVisibility(0);
			else
				mLinearLayoutControlBar.setVisibility(8);
			flag = true;
		} else
		{
			flag = false;
		}
		return flag;
	}

	public void onVideoSizeChangedListener(AbsMediaPlayer absmediaplayer, int i, int j)
	{
		Message message = new Message();
		message.obj = absmediaplayer;
		message.what = 16391;
		message.arg1 = i;
		message.arg2 = j;
		boolean flag = mEventHandler.sendMessage(message);
	}

	protected void resetMediaPlayer()
	{
		mMediaPlayerLoaded = false;
		mTime = -1;
		mLength = -1;
		mCanSeek = true;
		mAspectRatio = 0;

		byte byte0;
		int i;
		int j;
		if (mPlayListArray.size() == 1){
			byte0 = 8;
		}
		else{
			byte0 = 0;
		}
		mImageButtonTogglePlay.setVisibility(View.VISIBLE);
		//根据名字，获取对应的资源id
		i = SystemUtility.getDrawableId("btn_play_0");
		mImageButtonTogglePlay.setBackgroundResource(i);

		
		if (mPlayListArray.size() == 1){
			byte0 = 8;
		}
		else{
			byte0 = 0;
		}
		mImageButtonSwitchAspectRatio.setVisibility(View.VISIBLE);
		j = SystemUtility.getDrawableId("btn_aspect_ratio_0");
		mImageButtonSwitchAspectRatio.setBackgroundResource(j);
		
		mLinearLayoutControlBar.setVisibility(View.GONE);
	}

	protected void selectMediaPlayer(String s, boolean flag)
	{
		if (flag){
			mSurfaceViewDef.setVisibility(View.VISIBLE);
			mSurfaceViewVlc.setVisibility(View.GONE);
		}
		else{
			mSurfaceViewDef.setVisibility(View.GONE);
			mSurfaceViewVlc.setVisibility(View.VISIBLE);
		}
	}

	protected void startMediaPlayer()
	{
		if (!mMediaPlayerStarted && mMediaPlayerLoaded && mMediaPlayer != null)
		{
			mMediaPlayer.start();
			mMediaPlayerStarted = true;
		}
	}

























	//SurfaceHolderCallback的回调类
	private class SurfaceHolderCallback
		implements android.view.SurfaceHolder.Callback{
	
		final PlayerActivity this$0;
	
		//surface状态改变函数
		public void surfaceChanged(SurfaceHolder surfaceholder, int i, int j, int k){
			System.out.println("mSurfaceViewVLC SurfaceHolderCallback");
			mMediaPlayer.setDisplay(surfaceholder);
		}
	
		//surfaceholder的创建函数
		public void surfaceCreated(SurfaceHolder surfaceholder){
			PlayerActivity playeractivity = PlayerActivity.this;
			playeractivity.createMediaPlayer(false, finalOpenUrl, mSurfaceHolderVlc);
		}
	
		//surfaceholder的销毁函数
		public void surfaceDestroyed(SurfaceHolder surfaceholder){
			destroyMediaPlayer(false);
		}
	
		//构造函数
		SurfaceHolderCallback()	{
			super();
			this$0 = PlayerActivity.this;
		}
	}
	
	//SurfaceHolderCallback的回调类
	private class SurfaceHolderCallback2
		implements android.view.SurfaceHolder.Callback{

		final PlayerActivity this$0;
	
		//surface状态改变函数
		public void surfaceChanged(SurfaceHolder surfaceholder, int i, int j, int k){
			System.out.println("mSurfaceViewDef SurfaceHolderCallback2");
			mMediaPlayer.setDisplay(surfaceholder);
		}
	
		//surfaceholder的创建函数
		public void surfaceCreated(SurfaceHolder surfaceholder){
			PlayerActivity playeractivity = PlayerActivity.this;
			playeractivity.createMediaPlayer(true, finalOpenUrl, mSurfaceHolderDef);
		}
	
		//surface状态改变函数
		public void surfaceDestroyed(SurfaceHolder surfaceholder){
			destroyMediaPlayer(true);
		}
	
		//构造函数
		SurfaceHolderCallback2(){
			super();
			this$0 = PlayerActivity.this;
		}
	}

}
