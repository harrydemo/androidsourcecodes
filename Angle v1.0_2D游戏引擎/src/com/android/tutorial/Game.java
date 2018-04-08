package com.android.tutorial;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.android.angle.AngleActivity;
import com.android.angle.AngleFont;
import com.android.angle.AngleObject;
import com.android.angle.AngleSprite;
import com.android.angle.AngleSpriteLayout;
import com.android.angle.AngleString;
import com.android.angle.AngleTileBank;
import com.android.angle.AngleTileMap;
import com.android.angle.AngleUI;
import com.android.angle.AngleVector;
import com.android.angle.FPSCounter;

/**
 * Cretate little game simulation
 * >Creamos una pequeña simulación de juego
 * 
 * @author Ivan Pajuelo
 * 
 */
public class Game extends AngleActivity
{
	private MyDemo mDemo;
	
	public class MyDemo extends AngleUI
	{
		private AngleTileMap tmGround;
		private static final long mShotColdDownTime = 70;
		private AngleSpriteLayout slShip;
		private AngleSpriteLayout slShot;
		private MyShip mShip;
		private long mShotColdDown = 0;
		private AngleObject ogField;
		private AngleObject ogDashboard;

		
		class MyShip extends AngleSprite
		{
			AngleVector mDestination;
			float Speed; 
			public MyShip(AngleSpriteLayout sprite)
			{
				super(sprite);
				mPosition.set(160,240);
				mDestination=new AngleVector(mPosition); 
				Speed=200;
			}
			@Override
			public void step(float secondsElapsed)
			{
				if ((int)mPosition.mX<(int)mDestination.mX)
					mPosition.mX+=Speed*secondsElapsed;
				if ((int)mPosition.mX>(int)mDestination.mX)
					mPosition.mX-=Speed*secondsElapsed;
				if ((int)mPosition.mY<(int)mDestination.mY)
					mPosition.mY+=Speed*secondsElapsed;
				if ((int)mPosition.mY>(int)mDestination.mY)
					mPosition.mY-=Speed*secondsElapsed;
				super.step(secondsElapsed);
			}
		}
		
		class MyShot extends AngleSprite
		{

			public MyShot(MyShip ship, AngleSpriteLayout layout)
			{
				super(layout);
				mPosition.set(ship.mPosition.mX, ship.mPosition.mY - 20);
			}

			@Override
			public void step(float secondsElapsed)
			{
				mPosition.mY -= 300 * secondsElapsed;
				if (mPosition.mY < -10)
					mDie=true;
				super.step(secondsElapsed);
			}
			
		};

		public MyDemo(AngleActivity activity)
		{
			super(activity);
			
			//Create and insert two main object groups. One for the game field and other for the dashboard
			//>Creamos e insertamos dos grupos de objetos principales. Uno para el campo de juego y otro para los marcadores
			ogField=addObject(new AngleObject());
			ogDashboard=addObject(new AngleObject());

			//Create a tile bank and a tile map for the ground (see constructors)
			//>Creamos un banco de tiles y un mapa para el suelo (mirar los constructores)
			AngleTileBank tbGround = new AngleTileBank(mActivity.mGLSurfaceView,R.drawable.tilemap,4,4,32,32);
			tmGround = new AngleTileMap(tbGround, 320, 480, 15, 180, false,false);
			for (int t=0;t<tmGround.mColumnsCount*tmGround.mRowsCount;t++)
				tmGround.mMap[t]=(int) (Math.random()*tbGround.mTilesCount);
			// Put the bottom of the camera at the lowest part of the map
			//>Ponemos la cámara en la parte más baja del mapa
			tmGround.mTop = tmGround.mRowsCount* tbGround.mTileHeight - tmGround.mHeight;
			ogField.addObject(tmGround);
			slShip = new AngleSpriteLayout(mActivity.mGLSurfaceView,64, 64, R.drawable.anglelogo, 0, 0, 128, 128);
			slShot = new AngleSpriteLayout(mActivity.mGLSurfaceView,16, 16, R.drawable.anglelogo, 0, 0, 128, 128);
			mShip = (MyShip)ogField.addObject(new MyShip(slShip));

			
			//The dashboard background
			//>Fondo de los marcadores
			AngleSpriteLayout slDash = new AngleSpriteLayout(mActivity.mGLSurfaceView, 320, 64, R.drawable.tilemap, 0, 32, 320, 64);
			AngleSprite mDash=(AngleSprite)ogDashboard.addObject(new AngleSprite (slDash));
			mDash.mPosition.set(160, 480-slDash.roHeight/2);
			mDash.mAlpha=0.5f;

			//Font and text
			//>Fuente y texto
			AngleFont fntCafe25 = new AngleFont(mActivity.mGLSurfaceView, 25, Typeface.createFromAsset(getAssets(),"cafe.ttf"), 222, 0, 0, 30, 200, 255, 255);
			ogDashboard.addObject(new AngleString(fntCafe25,"Hello!!!",160,440,AngleString.aCenter));
		}

		@Override
		public boolean onTouchEvent(MotionEvent event)
		{
			// Prevent event flooding
			// Max 33 events per second
			//>Prevenimos una sobrecarga de eventos de entrada
			//>33 eventos por segundo como máximo
			try
			{
				Thread.sleep(30);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			// -------------------------
			mShip.mDestination.set(event.getX(), event.getY() - 32 - slShip.roHeight / 2); // Position the ship
			long CTM = System.currentTimeMillis();
			if (CTM > mShotColdDown) // Prevent shoot in less than mShotColdDownTime milliseconds
			{
				mShotColdDown = CTM + mShotColdDownTime;
				ogField.addObject(new MyShot(mShip, slShot));
			}
			return true;
		}

		@Override
		public void step(float secondsElapsed)
		{
			tmGround.mTop -= 200 * secondsElapsed; // Move the camera
			super.step(secondsElapsed);
		}

	}

	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		mGLSurfaceView.addObject(new FPSCounter());

		FrameLayout mMainLayout=new FrameLayout(this);
		mMainLayout.addView(mGLSurfaceView);
		setContentView(mMainLayout);
		
		mDemo=new MyDemo(this);
		setUI(mDemo);
	}
}
