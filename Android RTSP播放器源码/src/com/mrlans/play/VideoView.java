package com.mrlans.play;

import java.nio.ByteBuffer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 *   ÊÓÆµ²¥·Å´°¿Ú
 */
class VideoView extends View  implements Runnable
{
	
	private Bitmap bitmap;
	private Paint p;
	private byte[] nativePixels;
	private ByteBuffer buffer;
	private int displayWidth = 480;
	private int displayHeight = 320;
	
	@SuppressWarnings("unused")
	private MediaActivity playerActivity;
	
	public VideoView(Context context)
	{
		super(context);
		p = new Paint();
	}
	
	public VideoView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		p = new Paint();
	}

	public VideoView(Context context, int DisplayWidth, int DisplayHeight)
	{
		super(context);
		this.displayWidth = DisplayWidth;
		this.displayHeight = DisplayHeight;
		p = new Paint();
	}
	
	public void setContext(MediaActivity playerActivity) 
	{
		this.playerActivity = playerActivity;
	}
	
	public int getDisplayWidth()
	{
		return displayWidth;
	}

	public void setDisplayWidth(int displayWidth)
	{
		this.displayWidth = displayWidth;
	}

	public int getDisplayHeight()
	{
		return displayHeight;
	}

	public void setDisplayHeight(int displayHeight)
	{
		this.displayHeight = displayHeight;
	}

	public void play()
	{

		 bitmap = Bitmap.createBitmap(displayWidth,  displayHeight, Bitmap.Config.ARGB_8888);
		 
	      new Thread(this).start();
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		
		super.onDraw(canvas);
		
		if(null == bitmap)
		{
			canvas.drawColor(Color.BLACK);
			 return;
		}
		
		nativePixels = VideoFrames.frames.poll();
		
    	if( nativePixels != null && nativePixels.length > 0 ) 
    	{
    		buffer = ByteBuffer.wrap(nativePixels);
        	bitmap.copyPixelsFromBuffer(buffer);
    	}

        canvas.drawBitmap(bitmap, 0, 0, p);
        
	}

	public void run()
	{
		while (!Thread.currentThread().isInterrupted()) 
		{
                try
                {
                    Thread.sleep(10);
                } catch (InterruptedException e) 
                {
                    Thread.currentThread().interrupt();
                }

                postInvalidate();
		}
	}
}

