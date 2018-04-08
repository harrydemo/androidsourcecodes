package com.android.game;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.android.angle.AngleActivity;
import com.android.angle.FPSCounter;

//STEP 1:
//The first thing we must do is create the Activity of our game overloading AngleActivity.
//This class will do a lot of things for us.
//>PASO 1:
//>Lo 1º que debemos hacer es crear la Activity de nuestro juego derivándola de AngleActivity.
//>Con esto conseguiremos que el motor haga por si solo muchas de las cosas que necesitaremos.
/** 
* @author Ivan Pajuelo
*/
public class StepByStepGame extends AngleActivity
{
	//STEP 2:
	//Instead of use directly the Activity, we create two user interfaces overloading AngleUI.
	//One for the game and another for the menu. With AngleUI we can switch between one and another instantaneously
	//>PASO 2:
	//>En lugar de trabajar directamente sobre la Activity, crearemos dos interfaces de usuario 
	//>derivándolas de AngleUI. Una para el juego y otra para el menú
	//>De esta forma veremos como cambiar instantáneamente de una a otra.
	GameUI mTheGame;
	MenuUI mTheMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		//This line is optional. It adds an FPSCounter object to main View to se the performance in frames per second using LogCat
		//>Esta linea es opcional. Añade un objeto FPSCounter directamente a la View principal
		//>Así podremos ver el rendimiento de nuestro juego a través de LogCat
		mGLSurfaceView.addObject(new FPSCounter());

		//STEP 3:
		//Don't forget to instantiate the objects you will use
		//>PASO 3:
		//>Importante. No olvidemos instanciar los objetos (esto debería ser obvio)
		mTheGame=new GameUI(this);
		mTheMenu=new MenuUI(this);

		//STEP 4:
		//Set the active user interface. The menu in this case.
		//>PASO 4:
		//>Establecemos la interface de usuario activa. En este caso el menú.
		setUI(mTheMenu);
		
		//STEP 5:
		//All the engine run over a main View that is created by  AngleActivity (mGLSurfaceView)
		//Even so, instead of use this view directly, we will insert it into a FrameLayout.
		//This will be useful if we want to add standard API Views.
		//>PASO 5:
		//>Todo el motor corre sobre una View principal que crea AngleActivity (mGLSurfaceView)
		//>Aun así, en lugar de usar esta View directamente, la insertaremos dentro de un 
		//>FrameLayout por si queremos añadir Views de las API.
		FrameLayout mainLayout=new FrameLayout(this);
		mainLayout.addView(mGLSurfaceView);
		setContentView(mainLayout);
		
		//(Continues in MenuUI)
		//>(continua en MenuUI)
	}

}
