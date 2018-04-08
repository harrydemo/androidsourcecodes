package com.mrlans.play;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Rtsp Android  Player
 * @author mrlans
 *
 */
public class MediaActivity extends Activity
{
	public final static String TAG ="MediaActivity";
	
	RtspPalyThread rtspPalyThread;
	
	public boolean isPlaying = false;
	
	 public int VideoHeight = 288;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_media);
		
		final DisplayMetrics dm = new DisplayMetrics();  
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		
	    int swale = 0;
	    if(swale == 0)
	    {
	    	VideoHeight = (dm.widthPixels *3) /4;
	    }
	    else
	    {
	    	VideoHeight = (dm.widthPixels *9) /16;
	    }
		
	    VideoView view = (VideoView) findViewById(R.id.video);
	    view.setDisplayWidth(dm.widthPixels);
	    view.setDisplayHeight(VideoHeight);
	    view.setContext(this);
	    view.setBackgroundColor(Color.BLACK);
		view.play();
		
		//String rtsp = "rtsp://10.4.0.95/video";   
		//String rtsp = "rtsp://10.4.2.230/media/video1";
		final EditText rtspText = (EditText) findViewById(R.id.rtsptxt);
		
		final EditText rtspPort = (EditText) findViewById(R.id.rtspport);
		
		 //初始化参数
		String rtsp = rtspText.getText().toString();
		int port = Integer.parseInt(rtspPort.getText().toString());
	    init(rtsp, port, 0, dm.widthPixels, VideoHeight);
		
		Button playButton = (Button) findViewById(R.id.playb);
		playButton.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) 
			{
				rtspPalyThread = new RtspPalyThread(MediaActivity.this); 
				if(!isPlaying)
				{
					 //初始化参数
					String rtsp = rtspText.getText().toString();
					
					int port = Integer.parseInt(rtspPort.getText().toString());
					
				    init(rtsp, port, 0, dm.widthPixels, VideoHeight);
					
					//播放线程
				    rtspPalyThread.start();
					isPlaying = true;
					Toast.makeText(MediaActivity.this, "视频播放开始...", Toast.LENGTH_LONG).show();
				}
				else
				{
					Toast.makeText(MediaActivity.this, "视频已经在播放...", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		Button stopButton = (Button) findViewById(R.id.stopb);
		stopButton.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) 
			{
				if(isPlaying)
				{
					VideoFrames.frames.clear();
					stop();
					isPlaying = false;
					if(rtspPalyThread.isAlive())
					{
						rtspPalyThread.interrupt();
						rtspPalyThread = null;
					}
					Toast.makeText(MediaActivity.this, "视频停止...", Toast.LENGTH_LONG).show();
				}
				else
				{
					Toast.makeText(MediaActivity.this, "视频未播放...", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		
		Log.d(TAG,"MediaActivity Start Success!.....................................");
	}
	
	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		stop();
	}

	@Override
	public void onDetachedFromWindow()
	{
		// TODO Auto-generated method stub
		super.onDetachedFromWindow();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.activity_media, menu);
		return true;
	}
	
	public native int init(String rtspUrl, int mediaType, int scale, int width, int height);

	public native void resize(int width, int height);
	
	public native void play();
	
	public native void stop();
	
	static
	{
		System.loadLibrary("liveplay");
		Log.d(TAG, "Sytem load library liveplay  Success!");
	}
}

class RtspPalyThread extends Thread
{
	private MediaActivity mediaActivity;
	
	public RtspPalyThread(MediaActivity mediaActivity)
	{
		this.mediaActivity = mediaActivity;
	}
	
	@Override
	public void run()
	{
		mediaActivity.play();
	}
}