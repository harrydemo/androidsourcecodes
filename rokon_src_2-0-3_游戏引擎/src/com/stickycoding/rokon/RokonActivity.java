package com.stickycoding.rokon;

import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Path.FillType;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.stickycoding.rokon.device.Graphics;
import com.stickycoding.rokon.device.OS;
import com.stickycoding.rokon.vbo.ArrayVBO;
import com.stickycoding.rokon.vbo.VBO;

/**
 * RokonActivity.java
 * The base Activity for the graphics engine to work from
 * 
 * @author Richard
 */
public class RokonActivity extends Activity {
	
	protected static boolean engineCreated;
	protected static Scene currentScene = null;
	protected static boolean forceLandscape, forcePortrait, forceFullscreen;
	protected static RokonSurfaceView surfaceView;
	protected static boolean engineLoaded = false;
	protected static float gameWidth, gameHeight;
	protected static String graphicsPath = "";
	
	protected static int toastType;
	protected static String toastMessage;
	
	protected static Toast lastToast;

	
	/**
	 * Removes everyting from the memory, and resets statics.
	 * This is automatically called at onDestroy() when isFinishing() is TRUE
	 * You shouldn't need to call this yourself
	 */
	public void dispose() {
		Debug.print("dispose()");
		engineCreated = false;
		currentScene = null;
		forceLandscape = false;
		forcePortrait = false;
		forceFullscreen = false;
		surfaceView = null;
		engineLoaded = false;
		gameWidth = 0;
		gameHeight = 0;
		graphicsPath = "";
		Rokon.currentActivity = null;
		System.gc();
	}

	/**
	 * Flags debugMode as true, Debug.print will output to LogCat
	 */
	public static void debugMode() {
		Debug.debugMode = true;
	}
	
	/**
	 * Flags debugMode as false (default state), Debug.print will not output to LogCat
	 */
	public static void normalMode() {
		Debug.debugMode = false;
	}
	
	public static float getGameWidth() {
		return gameWidth;
	}
	
	public static float getGameHeight() {
		return gameHeight;
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(currentScene != null) {
			if(currentScene.onKeyDown(keyCode, event)) {
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onTrackballEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTrackballEvent(MotionEvent event) {
		if(currentScene != null) {
			return currentScene.onTrackballEvent(event);
		}
		return super.onTrackballEvent(event);
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onKeyUp(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if(currentScene != null) {
			if(currentScene.onKeyUp(keyCode, event)) {
				return true;
			}
		}
		return super.onKeyUp(keyCode, event);
	}
	
	/**
	 * Called when RokonActivity is created
	 */
	public void onCreate() {};
	
	/**
	 * Called when the loading is completed, and OpenGL is ready to draw 
	 */
	public void onLoadComplete() { };
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedState) {
		super.onCreate(savedState);		
		Debug.print("onCreate()");
		try {
			MotionEventWrapper5.checkAvailable();
			Rokon.motionEvent5 = new MotionEventWrapper5();
		} catch (VerifyError e) { }
		
		try {
			MotionEventWrapper8.checkAvailable();
			Rokon.motionEvent8 = new MotionEventWrapper8();
		} catch (VerifyError e) { }		
		if(engineCreated) {
			Debug.print("onCreate() when already started, creating new GLSurfaceView");
			//surfaceView = new RokonSurfaceView(this);
			//setContentView(surfaceView);
			return;
		}
		Debug.print("Engine Activity created");
		onCreate();
		if(!engineCreated) {
			Debug.error("The engine was not created");
			Debug.print("#################### FINISH ME HERE");
			finish();
			return;
		}
	}	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	public void onDestroy() {
		Debug.print("onDestroy()");
		if(isFinishing()) {
			dispose();
		}
		super.onDestroy();
	}
	
	private void createStatics() {
		Graphics.determine(this);
		Rokon.blendFunction = new BlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		Rokon.triangleStripBoxBuffer = new BufferObject(new float[] { 0, 0, 1, 0, 0, 1, 1, 1 });
		Rokon.lineLoopBoxBuffer = new BufferObject(new float[] { 0, 0, 1, 0, 1, 1, 0, 1 });
		
		Rokon.arrayVBO = new ArrayVBO(Rokon.triangleStripBoxBuffer, VBO.STATIC);		
		Rokon.boxArrayVBO = new ArrayVBO(Rokon.lineLoopBoxBuffer, VBO.STATIC);
		
		Rokon.rectangle = new Polygon(new float[] { 0, 0, 1, 0, 1, 1, 0, 1 });
		Rokon.circle = new Polygon(new float[] { 0, 0, 1, 0, 1, 1, 0, 1 });
		
		OS.determineAPI();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	public void onPause() {
		Debug.print("onPause()");
		if(currentScene != null) {
			currentScene.onPause();
		}
		//RokonMusic.onPause();
		surfaceView.onPause();
		super.onPause();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	public void onResume() {
		Debug.print("onResume()");
		Rokon.currentActivity = this;
		surfaceView.onResume();
		if(currentScene != null) {
			currentScene.onResume();
		}
		//RokonMusic.onResume();
		super.onResume();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(currentScene == null)
			return false;
		currentScene.handleTouch(event);
		return false;
	}
	
	/**
	 * Sets a default location for the graphics.
	 * All Texture objects created after this will have this prefix on their absolute path
	 * If the path you give is invalid, an error will not be raised until you try creating a Texture
	 * 
	 * @param path valid Sting path, with trailing slash (eg, "textures/")
	 */
	public void setGraphicsPath(String path) {
		graphicsPath = path;
	}
	
	/**
	 * @return the current default location for textures
	 */
	public String getGraphicsPath() {
		return graphicsPath;
	}
	
	/**
	 * Sets the game width of the OpenGL surface
	 * This must be called before createEngine
	 * 
	 * @param width
	 */
	public void setGameWidth(float width) {
		gameWidth = width;
		if(engineLoaded) {
			
		}
	}
	
	/**
	 * Sets the game height of the OpenGL surface
	 * This must be called before createEngine
	 * 
	 * @param height
	 */
	public void setGameHeight(float height) {
		gameHeight = height;
	}

	/**
	 * Sets the game width and height of the OpenGL surface
	 * The width will altered depending on screen resolution
	 * 
	 * @param width
	 * @param height
	 * 
	 * @return final game width
	 */
	public float setGameSize(float width, float height) {
		Graphics.determine(this);
		float aspect = width / height;
		gameWidth = width;
		gameHeight = height;
		if(Graphics.getAspectRatio() < aspect) {
			Debug.print("Thinner than expected");
		} else if(Graphics.getAspectRatio() > aspect) {
			Debug.print("Wider than expected");
		}
		gameWidth = Graphics.getAspectRatio() * gameHeight;
		return gameWidth;
	}
	
	/**
	 * Forces the game width and height of the OpenGL surface
	 * This must be called before createEngine
	 * 
	 * @param width
	 * @param height
	 */
	public void forceGameSize(float width, float height) {
		gameWidth = width;
		gameHeight = height;
	}
	
	/**
	 * Sets the currently active Scene to be rendered
	 * 
	 * @param scene a valid Scene
	 */
	public void setScene(Scene scene) {
		if(scene == null) {
			Debug.warning("RokonActivity.setScene", "Tried setting to a NULL Scene");
			currentScene = null;
			return;
		}
		if(currentScene != null) {
			currentScene.onEndScene();
		}
		currentScene = scene;
		scene.onSetScene();
		scene.onReady();
	}
	
	/**
	 * Fetches the currently active Scene object
	 * 
	 * @return NULL of no Scene is set
	 */
	public static Scene getScene() {
		return currentScene;
	}
	
	/**
	 * Forces the engine to stick to a portrait screen, must be set before createEngine() 
	 * This should be backed up by the correct android:screenOrientation parameter in AndroidManifest.xml
	 */
	public void forcePortrait() {
		if(engineCreated) {
			Debug.warning("RokonActivity.forceFullscreen", "This function may only be called before createEngine()");
			return;
		}
		forcePortrait = true;
		forceLandscape = false;
	}
	
	/**
	 * Forces the engine to stick to a landscape screen, must be set before createEngine()
	 * This should be backed up by the correct android:screenOrientation parameter in AndroidManifest.xml
	 */
	public void forceLandscape() {
		if(engineCreated) {
			Debug.warning("RokonActivity.forceFullscreen", "This function may only be called before createEngine()");
			return;
		}
		forcePortrait = false;
		forceLandscape = true;
	}

	/**
	 * @return TRUE if the engine is being forced into portrait mode
	 */
	public boolean isForcePortrait() {
		return forcePortrait;
	}
	
	/**
	 * @return TRUE if the engine is being forced into landscape mode
	 */
	public boolean isForceLandscape() {
		return forceLandscape;
	}
	
	/**
	 * Forces the Activity to be shown fullscreen
	 *  ie, no titlebar
	 */
	public void forceFullscreen() {
		if(engineCreated) {
			Debug.warning("RokonActivity.forceFullscreen", "This function may only be called before createEngine()");
			return;
		}
		forceFullscreen = true;
	}
	
	/**
	 * Prepares the Activity for rendering
	 * Note that some functions may only be called before createEngine
	 */
	public void createEngine() {
		if(engineCreated) {
			Debug.warning("RokonActivity.createEngine", "Attempted to call createEngine for a second time");
			return;
		}
		createStatics();
		initEngine(false);
	}
	
	/**
	 * Prepares the Activity for rendering
	 * Note that some functions may only be called before createEngine
	 * 
	 * @param createRelativeLayout TRUE to create a RelativeLayout above the surface for allowing Views ontop of the OGL surface
	 */
	public void createEngine(boolean createRelativeLayout) {
		if(engineCreated) {
			Debug.warning("RokonActivity.createEngine", "Attempted to call createEngine for a second time");
			return;
		}
		createStatics();
		initEngine(createRelativeLayout);
	}
	
	protected static RelativeLayout rokonInterface;
	protected static RelativeLayout rokonContainer;
	
	public RelativeLayout getInterface() {
		return rokonInterface;
	}
	
	private void initEngine(boolean createRelativeLayout) {
		if(forceFullscreen) {
	        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
	        getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
		}
		if(forceLandscape) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		if(forcePortrait) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		
		if(!createRelativeLayout) {
			surfaceView = new RokonSurfaceView(this);
			setContentView(surfaceView);
			engineCreated = true;
		} else {
			surfaceView = new RokonSurfaceView(this);
			
			rokonInterface = new RelativeLayout(this);
			rokonInterface.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
			
			rokonContainer = new RelativeLayout(this);
			rokonContainer.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT));
			
			rokonContainer.addView(surfaceView);
			rokonContainer.addView(rokonInterface);
			setContentView(rokonContainer);
			engineCreated = true;
		}
	}

	/**
	 * Returns the current draw priority
	 * 
	 * @return 0 by default
	 */
	public static int getDrawPriority() {
		return DrawPriority.drawPriority;
	}
	
	/**
	 * Sets the draw priority to be used
	 * If not set, or invalid parameters given, defaults to 0
	 * 
	 * @param drawPriority
	 */
	public static void setDrawPriority(int drawPriority) {
		DrawPriority.drawPriority = drawPriority;
	}	
	
	protected static Handler toastHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(lastToast != null) {
				lastToast.cancel();
			}
			lastToast = Toast.makeText(Rokon.getActivity(), toastMessage, toastType);
			lastToast.show();
		}
	};
}
