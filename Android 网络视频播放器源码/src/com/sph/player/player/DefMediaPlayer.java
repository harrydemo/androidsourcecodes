package com.sph.player.player;
/*
 * Copyright (C) 2011 Androd源码工作室
 * 
 * Android实战教程--网络视频类播发器
 * 
 * taobao : http://androidsource.taobao.com
 * mail : androidSource@139.com
 * QQ:    androidSource@139.com
 * 
 */
import android.media.MediaPlayer;
import android.util.Log;
import android.view.SurfaceHolder;
import java.util.Timer;
import java.util.TimerTask;

/*
 * Def播放器类
 * */
public class DefMediaPlayer extends AbsMediaPlayer
	implements android.media.MediaPlayer.OnBufferingUpdateListener, 
		android.media.MediaPlayer.OnCompletionListener, 
		android.media.MediaPlayer.OnErrorListener, 
		android.media.MediaPlayer.OnInfoListener, 
		android.media.MediaPlayer.OnPreparedListener, 
		android.media.MediaPlayer.OnSeekCompleteListener, 
		android.media.MediaPlayer.OnVideoSizeChangedListener{

	private static final String LOGTAG = "DANMAKU-DefMediaPlayer";
	protected static DefMediaPlayer sInstance = null;
	private MediaPlayer mMediaPlayer;
	private AbsMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener;
	private AbsMediaPlayer.OnCompletionListener mOnCompletionListener;
	private AbsMediaPlayer.OnErrorListener mOnErrorListener;
	private AbsMediaPlayer.OnInfoListener mOnInfoListener;
	private AbsMediaPlayer.OnPreparedListener mOnPreparedListener;
	private AbsMediaPlayer.OnProgressUpdateListener mOnProgressUpdateListener;
	private AbsMediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListener;
	private Timer mTimer;
	private TimerTask mTimerTask;

	//def播放器的构造函数
	protected DefMediaPlayer(){
		//创建播放事件线程
		mTimerTask = new PlayerTimerTask();
		mMediaPlayer = null;
		mTimer = null;
		mOnBufferingUpdateListener = null;
		mOnCompletionListener = null;
		mOnErrorListener = null;
		mOnInfoListener = null;
		mOnPreparedListener = null;
		mOnProgressUpdateListener = null;
		mOnVideoSizeChangedListener = null;
		
		//创建媒体播放类
		mMediaPlayer = new MediaPlayer();
		mMediaPlayer.setOnBufferingUpdateListener(this);
		mMediaPlayer.setOnCompletionListener(this);
		mMediaPlayer.setOnErrorListener(this);
		mMediaPlayer.setOnInfoListener(this);
		mMediaPlayer.setOnPreparedListener(this);
		mMediaPlayer.setOnSeekCompleteListener(this);
		mMediaPlayer.setOnVideoSizeChangedListener(this);
	}

	//获取Def播放器对象
	public static DefMediaPlayer getInstance(){
		if (sInstance == null){
			sInstance = new DefMediaPlayer();
		}
		return sInstance;
	}

	public int getAudioTrack(){
		return -1;
	}

	public int getAudioTrackCount(){
		return -1;
	}

	//获取当前部分进度
	public int getCurrentPosition(){
		int curPosition = -1;
		
		try {
			curPosition = mMediaPlayer.getCurrentPosition();
		} catch (Exception e) {
			Log.e("DANMAKU-DefMediaPlayer", "getCurrentPosition()");
			e.printStackTrace();
			// TODO: handle exception
		}
		
		return curPosition;
	}

	
	public int getDuration(){
		int duration = -1;

		try {
			duration = mMediaPlayer.getDuration();
		} catch (Exception e) {
			Log.e("DANMAKU-DefMediaPlayer", "getDuration()");
			e.printStackTrace();
			// TODO: handle exception
		}
		
		return duration;
	}

	public int getSubtitleTrack(){
		return -1;
	}

	public int getSubtitleTrackCount(){
		return -1;
	}

	//获取视频的高度
	public int getVideoHeight(){
		int video_height = -1;
		
		try {
			video_height = mMediaPlayer.getVideoHeight();
		} catch (Exception e) {
			Log.e("DANMAKU-DefMediaPlayer", "getVideoHeight()");
			e.printStackTrace();
			// TODO: handle exception
		}
		
		return video_height;
	}

	//获取视频宽度
	public int getVideoWidth(){
		int video_width = -1;
		try {
			video_width = mMediaPlayer.getVideoWidth();
		} catch (Exception e) {
			Log.e("DANMAKU-DefMediaPlayer", "getVideoWidth()");
			e.printStackTrace();
			// TODO: handle exception
		}
		return video_width;
	}

	public boolean isLooping(){
		
		boolean flag = false;
		try {
			flag = mMediaPlayer.isLooping();
		} catch (Exception e) {
			Log.e("DANMAKU-DefMediaPlayer", "isLooping()");
			e.printStackTrace();
			// TODO: handle exception
		}

		return flag;
	}

	//判断是否还在运行
	public boolean isPlaying(){
		boolean flag = false;
		
		try {
			flag = mMediaPlayer.isPlaying();
		} catch (Exception e) {
			Log.e("DANMAKU-DefMediaPlayer", "isPlaying()");
			e.printStackTrace();
			// TODO: handle exception
		}
		
		return flag;
	}

	//buffer更新
	public void onBufferingUpdate(MediaPlayer mediaplayer, int i){
		if (mOnBufferingUpdateListener != null){
			mOnBufferingUpdateListener.onBufferingUpdate(this, i);
		}
	}

	public void onCompletion(MediaPlayer mediaplayer){
		if (mOnCompletionListener != null){
			mOnCompletionListener.onCompletion(this);
		}
	}

	public boolean onError(MediaPlayer mediaplayer, int i, int j){
		boolean flag;
		if (mOnErrorListener != null){
			flag = mOnErrorListener.onError(this, i, j);
		}
		else{
			flag = false;
		}
		return flag;
	}

	public boolean onInfo(MediaPlayer mediaplayer, int i, int j){
		boolean flag;
		if (mOnInfoListener != null){
			flag = mOnInfoListener.onInfo(this, i, j);
		}
		else {
			flag = true;
		}
		return flag;
	}

	public void onPrepared(MediaPlayer mediaplayer){
		if (mOnPreparedListener != null){
			mOnPreparedListener.onPrepared(this);
		}
	}

	public void onSeekComplete(MediaPlayer mediaplayer){
		
	}

	public void onVideoSizeChanged(MediaPlayer mediaplayer, int i, int j){
		if (mOnVideoSizeChangedListener != null){
			mOnVideoSizeChangedListener.onVideoSizeChangedListener(this, i, j);
		}
	}

	//暂停函数
	public void pause(){
		try {
			mMediaPlayer.pause();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("DANMAKU-DefMediaPlayer", "pause()");
			// TODO: handle exception
		}
	}

	//准备函数
	public void prepare(){
		try {
			mMediaPlayer.prepare();
		} catch (Exception e) {
			Log.e("DANMAKU-DefMediaPlayer", "prepare()");
			e.printStackTrace();
			// TODO: handle exception
		}
	}

	public void prepareAsync(){
		try {
			mMediaPlayer.prepareAsync();
		} catch (Exception e) {
			Log.e("DANMAKU-DefMediaPlayer", "prepareAsync()");
			e.printStackTrace();
			// TODO: handle exception
		}
	}

	public void release(){
		try{
			mMediaPlayer.release();
		}
		catch (Exception exception){
			Log.e("DANMAKU-DefMediaPlayer", "release()");
		}
		sInstance = null;
		
	}

	//复位
	public void reset(){
		try {
			stop();
			mMediaPlayer.reset();
		} catch (Exception e) {
			Log.e("DANMAKU-DefMediaPlayer", "reset()");
			// TODO: handle exception
		}
	}

	public void seekTo(int i){	
		try {
			mMediaPlayer.seekTo(i);
		} catch (Exception e) {
			Log.e("DANMAKU-DefMediaPlayer", "seekTo()");
			// TODO: handle exception
		}
	}

	public void setAudioTrack(int i){
		
	}

	public void setDataSource(String s){
		try {
			if (s.startsWith("file://")) {
				s = s.substring(7);
			}
			mMediaPlayer.setDataSource(s);
			mMediaPlayer.setAudioStreamType(3);
		} catch (Exception e) {
			Log.e("DANMAKU-DefMediaPlayer", "setDataSource()");
			e.printStackTrace();
			// TODO: handle exception
		}
	}

	public void setDisplay(SurfaceHolder surfaceholder){
		try {
			mMediaPlayer.setDisplay(surfaceholder);
		} catch (Exception e) {
			Log.e("DANMAKU-DefMediaPlayer", "setDisplay()");
			e.printStackTrace();
			// TODO: handle exception
		}
	}

	public void setLooping(boolean flag){
		try {
			mMediaPlayer.setLooping(flag);
		} catch (Exception e) {
			Log.e("DANMAKU-DefMediaPlayer", "setLooping()");
			e.printStackTrace();
			// TODO: handle exception
		}
	}

	public void setOnBufferingUpdateListener(AbsMediaPlayer.OnBufferingUpdateListener onbufferingupdatelistener){
		mOnBufferingUpdateListener = onbufferingupdatelistener;
	}

	public void setOnCompletionListener(AbsMediaPlayer.OnCompletionListener oncompletionlistener){
		mOnCompletionListener = oncompletionlistener;
	}

	public void setOnErrorListener(AbsMediaPlayer.OnErrorListener onerrorlistener){
		mOnErrorListener = onerrorlistener;
	}

	public void setOnInfoListener(AbsMediaPlayer.OnInfoListener oninfolistener){
		mOnInfoListener = oninfolistener;
	}

	public void setOnPreparedListener(AbsMediaPlayer.OnPreparedListener onpreparedlistener){
		mOnPreparedListener = onpreparedlistener;
	}

	public void setOnProgressUpdateListener(AbsMediaPlayer.OnProgressUpdateListener onprogressupdatelistener){
		mOnProgressUpdateListener = onprogressupdatelistener;
	}

	public void setOnVideoSizeChangedListener(AbsMediaPlayer.OnVideoSizeChangedListener onvideosizechangedlistener){
		mOnVideoSizeChangedListener = onvideosizechangedlistener;
	}

	public void setSubtitleTrack(int i){
		
	}

	public void start(){
		int j;
		try		{
			mMediaPlayer.start();
			int i;
			if (mTimer != null){
				i = mTimer.purge();
			} else{
				Timer timer1 = new Timer();
				mTimer = timer1;
			}
			mTimer.schedule(mTimerTask, 0L, 250L);
		}
		catch (Exception exception){
			j = Log.e("DANMAKU-DefMediaPlayer", "start()");
		}
	}

	public void stop(){
		try {
			if (mTimer != null) {
				int i = mTimer.purge();
				mTimer = null;
			}
			mMediaPlayer.stop();
		} catch (Exception e) {
			Log.e("DANMAKU-DefMediaPlayer", "stop()");
			e.printStackTrace();
			// TODO: handle exception
		}
	}




	//时间线程类--用于保存当前播放进度，用于恢复播放
	private class PlayerTimerTask extends TimerTask{

		final DefMediaPlayer this$0;

		public void run(){
			if (mMediaPlayer != null && mOnProgressUpdateListener != null 
					&& mMediaPlayer.isPlaying()){
				
				int curPosition = mMediaPlayer.getCurrentPosition();
				int curDuration = mMediaPlayer.getDuration();
				
				DefMediaPlayer defmediaplayer = DefMediaPlayer.this;
				//更新进度图
				mOnProgressUpdateListener.onProgressUpdate(defmediaplayer, curPosition, curDuration);
			}
		}

		//构造函数
		PlayerTimerTask(){
			super();
			this$0 = DefMediaPlayer.this;
		}
	}

}
