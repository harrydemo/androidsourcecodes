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
import android.util.Log;
import android.view.SurfaceHolder;

public abstract class AbsMediaPlayer
{
	public interface OnBufferingUpdateListener
	{

		public abstract void onBufferingUpdate(AbsMediaPlayer absmediaplayer, int i);
	}

	public interface OnCompletionListener
	{

		public abstract void onCompletion(AbsMediaPlayer absmediaplayer);
	}

	public interface OnErrorListener
	{

		public abstract boolean onError(AbsMediaPlayer absmediaplayer, int i, int j);
	}

	public interface OnInfoListener
	{

		public abstract boolean onInfo(AbsMediaPlayer absmediaplayer, int i, int j);
	}

	public interface OnPreparedListener
	{

		public abstract void onPrepared(AbsMediaPlayer absmediaplayer);
	}

	public interface OnProgressUpdateListener
	{

		public abstract void onProgressUpdate(AbsMediaPlayer absmediaplayer, int i, int j);
	}

	public interface OnVideoSizeChangedListener
	{

		public abstract void onVideoSizeChangedListener(AbsMediaPlayer absmediaplayer, int i, int j);
	}


	private static final String LOGTAG = "DANMAKU-AbsMediaPlayer";

	public AbsMediaPlayer()
	{
	}

	//获取默认的播放器
	protected static AbsMediaPlayer getDefMediaPlayer(){
		int i = Log.d("DANMAKU-AbsMediaPlayer", "using DefMediaPlayer");
		return DefMediaPlayer.getInstance();
	}

	//获取视频播放器
	public static AbsMediaPlayer getMediaPlayer(boolean flag){
		AbsMediaPlayer absmediaplayer;
		if (flag){
			absmediaplayer = getDefMediaPlayer();
		}
		else{
			absmediaplayer = getVlcMediaPlayer();
		}
		
		return absmediaplayer;
	}

	//获取Vlc播放器
	protected static AbsMediaPlayer getVlcMediaPlayer(){
		int i = Log.d("DANMAKU-AbsMediaPlayer", "using VlcMediaPlayer");
		return VlcMediaPlayer.getInstance();
	}

	public abstract int getAudioTrack();

	public abstract int getAudioTrackCount();

	public abstract int getCurrentPosition();

	public abstract int getDuration();

	public abstract int getSubtitleTrack();

	public abstract int getSubtitleTrackCount();

	public abstract int getVideoHeight();

	public abstract int getVideoWidth();

	public abstract boolean isLooping();

	public abstract boolean isPlaying();

	public abstract void pause();

	public abstract void prepare();

	public abstract void prepareAsync();

	public abstract void release();

	public abstract void reset();

	public abstract void seekTo(int i);

	public abstract void setAudioTrack(int i);

	public abstract void setDataSource(String s);

	public abstract void setDisplay(SurfaceHolder surfaceholder);

	public abstract void setLooping(boolean flag);

	public abstract void setOnBufferingUpdateListener(OnBufferingUpdateListener onbufferingupdatelistener);

	public abstract void setOnCompletionListener(OnCompletionListener oncompletionlistener);

	public abstract void setOnErrorListener(OnErrorListener onerrorlistener);

	public abstract void setOnInfoListener(OnInfoListener oninfolistener);

	public abstract void setOnPreparedListener(OnPreparedListener onpreparedlistener);

	public abstract void setOnProgressUpdateListener(OnProgressUpdateListener onprogressupdatelistener);

	public abstract void setOnVideoSizeChangedListener(OnVideoSizeChangedListener onvideosizechangedlistener);

	public abstract void setSubtitleTrack(int i);

	public abstract void start();

	public abstract void stop();
}
