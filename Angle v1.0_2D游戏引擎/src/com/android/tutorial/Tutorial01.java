package com.android.tutorial;

import android.os.Bundle;

import com.android.angle.AngleActivity;
import com.android.angle.AngleSprite;
import com.android.angle.AngleSpriteLayout;

/**
 * This tutorial demonstrates how to create an ANGLE engine and add a sprite to render.
 * In this example we use the main GL view directly
 * >Este tutorial demuestra como crear una instancia del motor usando una AngleActivity
 * >y como añadir un AngleSprite para que se pinte.
 * 
 * @author Ivan Pajuelo
 * 
 */
public class Tutorial01 extends AngleActivity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//Create an AngleSpriteLayout that contains how the sprite will be rendered
		//(See the other constructors to see more options of creation)
		//>Creamos un AngleSpriteLayout el cual especificará como pintar el sprite
		//>(Consultar los otros constructores para ver mas opciones de creación)
		AngleSpriteLayout mLogoLayout = new AngleSpriteLayout(mGLSurfaceView, R.drawable.anglelogo);
		//Create a sprite that use this AngleSpriteLayout
		//>Creamos un sprite que use este AngleSpriteLayout
		AngleSprite mLogo = new AngleSprite (mLogoLayout);
		//Set screen position
		//>Fijamos la posición en pantalla
		mLogo.mPosition.set(160, 200); 
		//Add sprite to main view
		//>Añadimos el sprite a la View principal
		mGLSurfaceView.addObject(mLogo);
		//Set mGLSurfaceView as active view
		//>Establecemos mGLSurfaceView como vista activa 
		setContentView(mGLSurfaceView);
	}

}
