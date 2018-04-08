package com.android.tutorial;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.android.angle.AngleActivity;
import com.android.angle.AngleRotatingSprite;
import com.android.angle.AngleSpriteLayout;

/**
 * Override an Angle class and create our first animated object
 * >Sobrecargamos una clase Angle para crear nuestro primer objeto animado
 * 
 * @author Ivan Pajuelo
 * 
 */
public class Tutorial02 extends AngleActivity
{
	private class MyAnimatedSprite extends AngleRotatingSprite
	{
		public MyAnimatedSprite(int x, int y, AngleSpriteLayout layout)
		{
			super(x, y, layout);
		}

		//Override step function to implement animations and object logic
		//>Sobrecargar la función step para implementar animaciones y la lógica del objeto
		@Override
		public void step(float secondsElapsed)
		{
			mRotation+=secondsElapsed*10;//10º per second
			super.step(secondsElapsed);
		}
		
	};
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		//Now we will insert our sprite with only one line of code
		//Use MyAnimatedSprite so we can make it roll
		//>Esta vez insertaremos nuestro sprite con una sola linea de código
		//>Usaremos MyAnimatedSprite para hacerlo girar
		mGLSurfaceView.addObject(new MyAnimatedSprite (160, 200, new AngleSpriteLayout(mGLSurfaceView, R.drawable.anglelogo)));

		//Use a FrameLayout as main view instead of using mGLSurfaceView directly
		//>Usaremos un FrameLayout como vista activa en lugar de usar mGLSurfaceView directamente
		FrameLayout mMainLayout=new FrameLayout(this);
		mMainLayout.addView(mGLSurfaceView);
		setContentView(mMainLayout);
	}
}
