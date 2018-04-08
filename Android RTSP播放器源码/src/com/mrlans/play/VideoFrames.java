package com.mrlans.play;

import java.util.concurrent.ConcurrentLinkedQueue;

import android.util.Log;

public class VideoFrames
{
	public static ConcurrentLinkedQueue<byte[] >  frames = new ConcurrentLinkedQueue<byte[] >(); 
	
	public static void callback(byte[] buffer)
	{
		frames.add(buffer);
		Log.d("VideoFrames_callback", "VideoFrames_callback Success! size = "+ buffer.length);
	}
}
