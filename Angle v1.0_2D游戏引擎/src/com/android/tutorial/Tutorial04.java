package com.android.tutorial;

import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.android.angle.AngleActivity;
import com.android.angle.AngleRotatingSprite;
import com.android.angle.AngleSpriteLayout;
import com.android.angle.AngleUI;
import com.android.angle.FPSCounter;

/**
 * Cretate an user interface (AngleUI)
 * >Creación de una interface de usuario (AngleUI) 
 * 
 * @author Ivan Pajuelo
 * 
 */
public class Tutorial04 extends AngleActivity
{
	private class MyAnimatedSprite extends AngleRotatingSprite
	{
		private static final float sRotationSpeed = 20;
		private static final float sAlphaSpeed = 0.5f;
		private float mAplhaDir;

		public MyAnimatedSprite(int x, int y, AngleSpriteLayout layout)
		{
			super(x, y, layout);
			mAplhaDir=sAlphaSpeed;
		}

		@Override
		public void step(float secondsElapsed)
		{
			mRotation+=secondsElapsed*sRotationSpeed;
			mAlpha+=secondsElapsed*mAplhaDir;
			if (mAlpha>1)
			{
				mAlpha=1;
				mAplhaDir=-sAlphaSpeed;
			}
			if (mAlpha<0)
			{
				mAlpha=0;
				mAplhaDir=sAlphaSpeed;
			}
			super.step(secondsElapsed);
		}
		
	};
	
	private class MyDemo extends AngleUI
	{
		//Now our rolling sprite(s) will be in our new UI
		//>Ahora nuestro(s) sprite(s) giratorio(s) estará(n) en nuestra nueva UI 
		AngleSpriteLayout mLogoLayout;
		
		public MyDemo(AngleActivity activity)
		{
			super(activity);
			mLogoLayout = new AngleSpriteLayout(mGLSurfaceView, 128, 128, R.drawable.anglelogo);
		}

		@Override
		public boolean onTouchEvent(MotionEvent event)
		{
			if (event.getAction()==MotionEvent.ACTION_DOWN)
			{
				//Add a new MyAnimatedSprite on touch position 
				//To access mGLSurfaceView we will use mActivity
				//>Añadimos un nuevo MyAnimatedSprite en la posición donde se ha pulsado
				//>Para acceder a mGLSurfaceView usaremos mActivity
				mActivity.mGLSurfaceView.addObject(new MyAnimatedSprite ((int)event.getX(), (int)event.getY(), mLogoLayout));
				return true;
			}
			return super.onTouchEvent(event);
		}
		
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		mGLSurfaceView.addObject(new FPSCounter());

		FrameLayout mMainLayout=new FrameLayout(this);
		mMainLayout.addView(mGLSurfaceView);
		setContentView(mMainLayout);
		
		//Set current UI (create inline)
		//>Fijamos la UI activa. (creación inline)
		setUI(new MyDemo(this));
	}
}
