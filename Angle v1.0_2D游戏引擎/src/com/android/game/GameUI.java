package com.android.game;

import android.graphics.Typeface;
import android.view.MotionEvent;

import com.android.angle.AngleActivity;
import com.android.angle.AngleFont;
import com.android.angle.AngleObject;
import com.android.angle.AngleSound;
import com.android.angle.AngleSprite;
import com.android.angle.AngleSpriteLayout;
import com.android.angle.AngleString;
import com.android.angle.AngleUI;
import com.android.angle.AngleVector;

/** 
* @author Ivan Pajuelo
*/
public class GameUI extends AngleUI
{
	private static final float sSightSpeed = 5;
	public Field mField;
	private Sight mSight;
	private AngleObject ogDashboard;
	private AngleSprite sprTrackPad;
	private AngleVector mAim;
	private AngleVector mTrackPadPos;
	private AngleVector mTrackPadDelta;
	private AngleString strScore;
	private AngleString strLifes;
	public AngleSpriteLayout slGround;
	public AngleSpriteLayout slSmiley;
	public AngleSpriteLayout slSight;
	private AngleSound sndMachineGun;
	public int mScore;
	private int mLifes;
	private boolean mInverted;
	
	//STEP 15
	//This is the entry point
	//Here we initialize all the variables that need when this UI is just activated
	//>PASO 15:
	//>Este será el punto de entrada
	//>Aquí inicializaremos todas la variables que necesitemos cuando se acabe de activar esta UI
	@Override
	public void onActivate()
	{
		mLifes=3;
		mScore=0;
		strLifes.set(""+mLifes);
		strScore.set(""+mScore);
		super.onActivate();
	}

	public GameUI(AngleActivity activity)
	{
		super(activity);
		
		//STEP 16:
		//We created all the SpriteLayouts that we will need 
		//>PASO 16:
		//>Creamos todos los SpriteLayouts que vayamos a necesitar
		slGround=new AngleSpriteLayout(mActivity.mGLSurfaceView,com.android.tutorial.R.drawable.fondo);
		slSmiley=new AngleSpriteLayout(mActivity.mGLSurfaceView,32,45,com.android.tutorial.R.drawable.salto,0,0,32,45,8,8); 
		slSight=new AngleSpriteLayout(mActivity.mGLSurfaceView,32,32,com.android.tutorial.R.drawable.mira,0,0,32,32,2,2);

		//STEP 17:
		//Create and load a sound effect		
		//>PASO 17:
		//>Creamos y cargamos un efecto de sonido
		sndMachineGun=new AngleSound(mActivity,com.android.tutorial.R.raw.machinegun);
		
		//STEP 18:
		//Create a font for the dashboard strings		
		//>PASO 18:
		//>Creamos una fuente para los marcadores
		AngleFont fntBazaronite=new AngleFont(mActivity.mGLSurfaceView, 18, Typeface.createFromAsset(mActivity.getAssets(),"bazaronite.ttf"), 222, 0, 2, 255, 100, 255, 255);

		//STEP 19:
		//Create the game objects
		//Here we use the quick insertion form
		//>PASO 19:
		//>Creación de los objetos de juego
		//>Aquí usamos la forma de inserción rápida

		//For the battlefield,
		//>Para el campo de batalla,
		mField=(Field)addObject(new Field(this));
		//The sight
		//>El punto de mira
		mSight=(Sight)addObject(new Sight(this));
		//And a group for dashboard and other floating things
		//>y un grupo para marcadores y demás cosas flotantes
		ogDashboard=addObject(new AngleObject());
		sprTrackPad=(AngleSprite) ogDashboard.addObject(new AngleSprite(160,380,new AngleSpriteLayout(mActivity.mGLSurfaceView,320,200,com.android.tutorial.R.drawable.panel)));

		//STEP 20:
		//Add the 2 dashboard strings
		//>PASO 20:
		//>Insertamos los 2 Strings de los marcadores
		strScore = (AngleString) ogDashboard.addObject(new AngleString(fntBazaronite, "0", 310, 30, AngleString.aRight));
		strLifes = (AngleString) ogDashboard.addObject(new AngleString(fntBazaronite, "0", 60, 30, AngleString.aRight));
		
		//STEP 21:
		//Create some vectors to store bidimensional values
		//>PASO 21:
		//>Creamos unos vectores para guardar varios valores bidimensionales
		mAim=new AngleVector();
		mTrackPadPos=new AngleVector();
		mTrackPadDelta=new AngleVector();
		
		//Esta variable indica si el control está invertido
		mInverted=false;
	}

	//Step 22:
	//Create a function for the game logic.
	//This function will be called every time you paint the screen and get as parameter
	//the number of seconds that have elapsed since the last time you called it.
	//Calling super.step, the step function of all children is called
	//In this case, the field of battle, the sight and dashboard
	//In this step by step, I will not comment on the game logic. Only the parts of
	//the engine itself.
	//>Paso 22:
	//>Creamos una función para la lógica del juego.
	//>Esta función será llamada cada vez que se pinte la pantalla y recibe como parámetro
	//>la cantidad de segundos que han pasado desde la última vez que se la llamó.
	//>Al llamar a super.step, hacemos que se llame a la función step de todos los hijos que 
	//>tenga este objeto por debajo. En este caso, el campo de batalla, la mirilla y los marcadores
	//>En este paso a paso, no voy a comentar la lógica del juego. Sólo las partes que correspondan 
	//>al motor en si.
	@Override
	public void step(float secondsElapsed)
	{
		super.step(secondsElapsed);
		mSight.moveTo(mTrackPadPos);
		mTrackPadDelta.set(mTrackPadPos);
		mTrackPadDelta.mul(sSightSpeed*secondsElapsed);
		mAim.add(mTrackPadDelta);
		if (mAim.mX>1)
			mAim.mX=1;
		if (mAim.mX<-1)
			mAim.mX=-1;
		if (mAim.mY>1)
			mAim.mY=1;
		if (mAim.mY<-1)
			mAim.mY=-1;
		mField.moveTo(mAim);
	}

	//Step 23:
	//Overload the onTouchEvent event to handle when the 
	//player touchs the screen.
	//Also we can overload any other events such as onKeyDown or onTrackballEvent to
	//respond to different input methods.
	//>Paso 23:
	//>Sobrecargamos el evento onTouchEvent para responder a las pulsaciones que haga
	//>el jugador en la pantalla.
	//>También podemos sobrecargar otros eventos como onKeyDown y onTrackballEvent para 
	//>responder a distintos métodos de entrada.
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		//Prevent event flooding
		//>Impedimos que el sistema sea sobrecargado por eventos de touch
		//>Máximo 60 por segundo
		try
		{
			Thread.sleep(16);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		// -------------------------
		float eX=event.getX();
		float eY=event.getY();
		if (event.getAction()==MotionEvent.ACTION_DOWN)
		{
			if (eX>sprTrackPad.mPosition.mX-sprTrackPad.roLayout.roWidth/2)
				if (eY>sprTrackPad.mPosition.mY-sprTrackPad.roLayout.roHeight/2)
					if (eX<sprTrackPad.mPosition.mX+sprTrackPad.roLayout.roWidth/2)
						if (eY<sprTrackPad.mPosition.mY+sprTrackPad.roLayout.roHeight/2)
						{
							//STEP 24:
							//Play a sound effect in an infinite loop with volume at 1005(1)
							//>PASO 24:
							//>Aquí vemos como reproducir un sonido con volumen al 100%(1) y en un bucle infinito
							sndMachineGun.play(1,true);
							mSight.fire(true);
						}
		}
		else if (event.getAction()==MotionEvent.ACTION_UP)
		{
			//STEP 25:
			//Stop the sound playing
			//>PASO 25:
			//>Detenemos la reproducción del sonido
			sndMachineGun.stop();
			mSight.fire(false);
			mTrackPadPos.set(0,0);
		}
		else if (event.getAction()==MotionEvent.ACTION_MOVE)
		{
			if (eX>sprTrackPad.mPosition.mX-sprTrackPad.roLayout.roWidth/2)
				if (eY>sprTrackPad.mPosition.mY-sprTrackPad.roLayout.roHeight/2)
					if (eX<sprTrackPad.mPosition.mX+sprTrackPad.roLayout.roWidth/2)
						if (eY<sprTrackPad.mPosition.mY+sprTrackPad.roLayout.roHeight/2)
						{
							if (mInverted)
								mTrackPadPos.set(-(eX-sprTrackPad.mPosition.mX)/(sprTrackPad.roLayout.roWidth/2),-(eY-sprTrackPad.mPosition.mY)/(sprTrackPad.roLayout.roHeight/2));
							else
								mTrackPadPos.set((eX-sprTrackPad.mPosition.mX)/(sprTrackPad.roLayout.roWidth/2),(eY-sprTrackPad.mPosition.mY)/(sprTrackPad.roLayout.roHeight/2));
						}
		}
		return true;
	}

	//STEP 26:
	//This callback updates the number of remaining lives, also see how to return to the menu
	//setting its UI	
	//>PASO 26:
	//>En este callback para actualizar la cantidad de vidas restantes, vemos como volver al menú
	//>estableciendo su UI
	public void updateLifes(int lifes)
	{
		mLifes+=lifes;
		if (mLifes<0)
			((StepByStepGame) mActivity).setUI(((StepByStepGame) mActivity).mTheMenu);
		strLifes.set(""+mLifes);
	}

	public void updateScore(int score)
	{
		mScore+=score;
		strScore.set(""+mScore);
	}

	//STEP 27:
	//Overload the onDeactivate event to empty the smileys' list and stop the sound
	//if it is playing
	//>PASO 27:
	//>Sobrecargamos el evento onDeactivate para vaciar la lista de smileys y parar el sonido
	//>si está sonando
	@Override
	public void onDeactivate()
	{
		sndMachineGun.stop();
		for (int s=1;s<mField.count();s++)
			mField.childAt(s).mDie=true;
		super.onDeactivate();
	}

	///Continues in FIeld)
	//(continua en Field)
}
