/**
 * 
 */
package com.appspot.hexagon;

import java.util.List;

import com.appspot.hexagon.dbo.LocationAnchor;
import com.appspot.hexagon.dbo.PlayerObject;
import com.appspot.hexagon.dbo.PlayerObject.HorizontalDirection;
import com.appspot.hexagon.dbo.PlayerObject.VerticalDirection;
import com.appspot.hexagon.util.FrameRateCounter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Path.Direction;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

/**
 * @author Avatar Ng
 * 
 */
public class HexagonGameView extends SurfaceView implements SurfaceHolder.Callback, GestureDetector.OnGestureListener{	
	private TextView mStatusText = null;
	private HexagonGameThread thread = null;
	private GestureDetector gameGestureDetector = null;

	public HexagonGameView(Context context,  AttributeSet attrs) {
		super(context, attrs);
		gameGestureDetector = new GestureDetector(this);

		// register our interest in hearing about changes to our surface
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		// create thread only; it's started in surfaceCreated()
		thread = new HexagonGameThread(holder, context, new Handler() {
			@Override
			public void handleMessage(Message m) {
				mStatusText.setVisibility(m.getData().getInt("viz"));
				mStatusText.setText(m.getData().getString("text"));
			}
		});
		setFocusable(true); // make sure we get key events
		// Set the background
		//this.setBackgroundColor(background_color);
	}
	
	public HexagonGameThread getThread() {
        return thread;
    }
	
	// capture user input
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
		return thread.doKeyDown(keyCode, event);
	}
	
	// capture user touch input
	@Override
    public boolean onKeyUp(int keyCode, KeyEvent event){
		return thread.doKeyUp(keyCode, event);
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
		thread.setSurfaceSize(width, height);
	}
	
	public void surfaceCreated(SurfaceHolder holder) {
        // start the thread here so that we don't busy-wait in run()
        // waiting for the surface to be created
        thread.setRunning(true);
        thread.start();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // we have to tell thread to shut down & wait for it to finish, or else
        // it might touch the Surface after we return and explode
        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }
	
	@Override
    public boolean onTouchEvent(MotionEvent event){
        gameGestureDetector.onTouchEvent(event);
                
        return true;
    }
	
	// capture user touch gesture input
	public boolean onDown(MotionEvent e) {
		return thread.onDown(e);
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return thread.onFling(e1, e2, velocityX, velocityY);
	}

	public void onLongPress(MotionEvent e) {
		thread.onLongPress(e);		
	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return thread.onScroll(e1, e2, distanceX, distanceY);
	}

	public void onShowPress(MotionEvent e) {
		thread.onShowPress(e);
	}

	public boolean onSingleTapUp(MotionEvent e) {
		return thread.onSingleTapUp(e);
	}
}
