// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   VlcMediaPlayer.java

package com.sph.player.player;

import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

// Referenced classes of package com.sph.player.player:
//			AbsMediaPlayer

public class VlcMediaPlayer extends AbsMediaPlayer
{
	class VlcEvent
	{

		public static final int MediaDurationChanged = 2;
		public static final int MediaFreed = 4;
		public static final int MediaMetaChanged = 0;
		public static final int MediaParsedChanged = 3;
		public static final int MediaPlayerBackward = 264;
		public static final int MediaPlayerBuffering = 259;
		public static final int MediaPlayerEncounteredError = 266;
		public static final int MediaPlayerEndReached = 265;
		public static final int MediaPlayerForward = 263;
		public static final int MediaPlayerLengthChanged = 273;
		public static final int MediaPlayerMediaChanged = 256;
		public static final int MediaPlayerNothingSpecial = 257;
		public static final int MediaPlayerOpening = 258;
		public static final int MediaPlayerPausableChanged = 270;
		public static final int MediaPlayerPaused = 261;
		public static final int MediaPlayerPlaying = 260;
		public static final int MediaPlayerPositionChanged = 268;
		public static final int MediaPlayerSeekableChanged = 269;
		public static final int MediaPlayerSnapshotTaken = 272;
		public static final int MediaPlayerStopped = 262;
		public static final int MediaPlayerTimeChanged = 267;
		public static final int MediaPlayerTitleChanged = 271;
		public static final int MediaStateChanged = 5;
		public static final int MediaSubItemAdded = 1;
		public boolean booleanValue;
		public int eventType;
		public float floatValue;
		public int intValue;
		public long longValue;
		public String stringValue;
		final VlcMediaPlayer this$0;

		private VlcEvent()
		{
			super();
			this$0 = VlcMediaPlayer.this;
			eventType = -1;
			booleanValue = false;
			intValue = -1;
			longValue = 65535L;
			floatValue = -1F;
			stringValue = null;
		}
	}


	private static final String LOGTAG = "DANMAKU-VlcMediaPlayer";
	protected AbsMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener;
	protected AbsMediaPlayer.OnCompletionListener mOnCompletionListener;
	protected AbsMediaPlayer.OnErrorListener mOnErrorListener;
	protected AbsMediaPlayer.OnInfoListener mOnInfoListener;
	protected AbsMediaPlayer.OnPreparedListener mOnPreparedListener;
	protected AbsMediaPlayer.OnProgressUpdateListener mOnProgressUpdateListener;
	protected AbsMediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListener;
	private int mTime;

	protected VlcMediaPlayer()
	{
		mOnBufferingUpdateListener = null;
		mOnCompletionListener = null;
		mOnErrorListener = null;
		mOnInfoListener = null;
		mOnPreparedListener = null;
		mOnProgressUpdateListener = null;
		mOnVideoSizeChangedListener = null;
		mTime = -1;
		nativeCreate();
	}

	public static VlcMediaPlayer getInstance()
	{
		return new VlcMediaPlayer();
	}

	private void onVlcEvent(VlcEvent vlcevent)
	{
		Object aobj[] = new Object[1];
		Integer integer = Integer.valueOf(vlcevent.eventType);
		aobj[0] = integer;
		String s = String.format("received vlc event %d", aobj);
		int i = Log.d("DANMAKU-VlcMediaPlayer", s);
		
		System.out.println("vlcevent.eventType = "+vlcevent.eventType);
		
		switch(vlcevent.eventType){
			case VlcEvent.MediaParsedChanged:
				boolean flag;
				if (!vlcevent.booleanValue){
					if (mOnErrorListener != null){
						flag = mOnErrorListener.onError(this, 1, 0);
					}
				} else{
					if (mOnPreparedListener != null){
						mOnPreparedListener.onPrepared(this);
					}
					if (mOnVideoSizeChangedListener != null){
						int j = getVideoWidth();
						int k = getVideoHeight();
						mOnVideoSizeChangedListener.onVideoSizeChangedListener(this, j, k);
					}
				}
				break;
				
			case VlcEvent.MediaPlayerBuffering:
				if (mOnBufferingUpdateListener != null){
					int l = (int)vlcevent.floatValue;
					mOnBufferingUpdateListener.onBufferingUpdate(this, l);
				}
				break;
				
			case VlcEvent.MediaPlayerEndReached:
				if (mOnCompletionListener != null){
					mOnCompletionListener.onCompletion(this);
				}
				break;
				
			case VlcEvent.MediaPlayerEncounteredError:
				boolean flag1;
				if (mOnErrorListener != null){
					flag1 = mOnErrorListener.onError(this, 1, 0);
				}
				break;
				
			case VlcEvent.MediaPlayerTimeChanged:
				if (mOnProgressUpdateListener != null){
					AbsMediaPlayer.OnProgressUpdateListener onprogressupdatelistener = mOnProgressUpdateListener;
					int i1 = (int)vlcevent.longValue;
					onprogressupdatelistener.onProgressUpdate(this, i1, -1);
				}
				if (mTime < 0 && mOnVideoSizeChangedListener != null){
					int j1 = getVideoWidth();
					int k1 = getVideoHeight();
					mOnVideoSizeChangedListener.onVideoSizeChangedListener(this, j1, k1);
				}
				mTime = (int)vlcevent.longValue;
				break;
			
			case VlcEvent.MediaPlayerSeekableChanged:
				boolean flag2;
				if (!vlcevent.booleanValue && mOnInfoListener != null){
					flag2 = mOnInfoListener.onInfo(this, 801, 0);
				}
				break;
				
			case VlcEvent.MediaPlayerLengthChanged:
				if (mOnProgressUpdateListener != null){
					AbsMediaPlayer.OnProgressUpdateListener onprogressupdatelistener1 = mOnProgressUpdateListener;
					int i2 = (int)vlcevent.longValue;
					onprogressupdatelistener1.onProgressUpdate(this, -1, i2);
				}
				break;
		}
	}

	public int getAudioTrack()
	{
		return 0;
	}

	public int getAudioTrackCount()
	{
		return 0;
	}

	public int getCurrentPosition()
	{
		return nativeGetCurrentPosition();
	}

	public int getDuration()
	{
		return nativeGetDuration();
	}

	public int getSubtitleTrack()
	{
		return 0;
	}

	public int getSubtitleTrackCount()
	{
		return 0;
	}

	public int getVideoHeight()
	{
		return nativeGetVideoHeight();
	}

	public int getVideoWidth()
	{
		return nativeGetVideoWidth();
	}

	public boolean isLooping()
	{
		return nativeIsLooping();
	}

	public boolean isPlaying()
	{
		return nativeIsPlaying();
	}

	protected native void nativeAttachSurface(Surface surface);

	protected native void nativeCreate();

	protected native void nativeDetachSurface();

	protected native int nativeGetCurrentPosition();

	protected native int nativeGetDuration();

	protected native int nativeGetVideoHeight();

	protected native int nativeGetVideoWidth();

	protected native boolean nativeIsLooping();

	protected native boolean nativeIsPlaying();

	protected native void nativePause();

	protected native void nativePrepare();

	protected native void nativePrepareAsync();

	protected native void nativeRelease();

	protected native void nativeSeekTo(int i);

	protected native void nativeSetDataSource(String s);

	protected native void nativeSetLooping(boolean flag);

	protected native void nativeStart();

	protected native void nativeStop();

	public void pause()
	{
		nativePause();
	}

	public void prepare()
	{
		nativePrepare();
	}

	public void prepareAsync()
	{
		nativePrepareAsync();
	}

	public void release()
	{
		nativeRelease();
	}

	public void reset()
	{
	}

	public void seekTo(int i)
	{
		nativeSeekTo(i);
	}

	public void setAudioTrack(int i)
	{
	}

	public void setDataSource(String s)
	{
		nativeSetDataSource(s);
	}

	public void setDisplay(SurfaceHolder surfaceholder)
	{
		if (surfaceholder != null)
		{
			surfaceholder.setFormat(4);
			Surface surface = surfaceholder.getSurface();
			nativeAttachSurface(surface);
		} else
		{
			nativeDetachSurface();
		}
	}

	public void setLooping(boolean flag)
	{
		nativeSetLooping(flag);
	}

	public void setOnBufferingUpdateListener(AbsMediaPlayer.OnBufferingUpdateListener onbufferingupdatelistener)
	{
		mOnBufferingUpdateListener = onbufferingupdatelistener;
	}

	public void setOnCompletionListener(AbsMediaPlayer.OnCompletionListener oncompletionlistener)
	{
		mOnCompletionListener = oncompletionlistener;
	}

	public void setOnErrorListener(AbsMediaPlayer.OnErrorListener onerrorlistener)
	{
		mOnErrorListener = onerrorlistener;
	}

	public void setOnInfoListener(AbsMediaPlayer.OnInfoListener oninfolistener)
	{
		mOnInfoListener = oninfolistener;
	}

	public void setOnPreparedListener(AbsMediaPlayer.OnPreparedListener onpreparedlistener)
	{
		mOnPreparedListener = onpreparedlistener;
	}

	public void setOnProgressUpdateListener(AbsMediaPlayer.OnProgressUpdateListener onprogressupdatelistener)
	{
		mOnProgressUpdateListener = onprogressupdatelistener;
	}

	public void setOnVideoSizeChangedListener(AbsMediaPlayer.OnVideoSizeChangedListener onvideosizechangedlistener)
	{
		mOnVideoSizeChangedListener = onvideosizechangedlistener;
	}

	public void setSubtitleTrack(int i)
	{
	}

	public void start()
	{
		nativeStart();
	}

	public void stop()
	{
		nativeStop();
	}

	static 
	{
		System.loadLibrary("vlccore");
	}
}
