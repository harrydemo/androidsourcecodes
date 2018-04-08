package com.crackedcarrot;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.crackedcarrot.UI.UIHandler;
import com.crackedcarrot.fileloader.Level;
import com.crackedcarrot.fileloader.Map;
import com.crackedcarrot.fileloader.MapLoader;
import com.crackedcarrot.fileloader.TowerLoader;
import com.crackedcarrot.fileloader.WaveLoader;
import com.crackedcarrot.menu.R;
import com.crackedcarrot.multiplayer.MultiplayerGameLoop;
import com.crackedcarrot.multiplayer.MultiplayerService;
import com.crackedcarrot.textures.TextureLibraryLoader;

public class GameInit extends Activity {

	public GameLoop gameLoop;
	public MultiplayerGameLoop gLoop = null;
	public SurfaceView mGLSurfaceView;

	private GameLoopGUI gameLoopGui;
	private Thread gameLoopThread;
	public UIHandler hudHandler;
	private MapLoader mapLoader;
	private SoundManager soundManager;

	// GameFinished wants this to determine highscores.
	public int mapChoice;

	public static boolean survivalGame = false;

	// /////////////// Multiplayer ////////////////////////////
	private static MultiplayerService mMultiplayerService;
	public static boolean multiplayergame = false;

	/** Makes the Bluetooth socket available from GameInit */
	public static void setMultiplayer(MultiplayerService service) {
		mMultiplayerService = service;
		multiplayergame = true;
	}

	/*
	 * DONT CHANGE THESE @Override FUNCTIONS UNLESS YOU KNOW WHAT YOU'RE DOING.
	 * 
	 * If you want to change how the GUI works in the gameloop you probably want
	 * to edit GameLoopGUI.java instead?
	 */

	@Override
	protected Dialog onCreateDialog(int id) {
		return gameLoopGui.onCreateDialog(id);
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		gameLoopGui.onPrepareDialog(id, dialog);
	}

	/*
	 * This will have to live in GameInit.java for now - havent figured out how
	 * to move it correctly yet, there might be problems with the key-input to
	 * the application/gameLoop... /Fredrik
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// Log.d("GAMEINIT", "onKeyDown KEYCODE_BACK");
			showDialog(gameLoopGui.DIALOG_QUIT_ID);
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {
			// Log.d("GAMEINIT", "onKeyDown KEYCODE_MENU");
			if (!multiplayergame)
				GameLoop.pause();
			showDialog(gameLoopGui.DIALOG_PAUSE_ID);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Log.d("GAMEINIT", "onCreate");

		/**
		 * Ensures that the activity is displayed only in the portrait
		 * orientation
		 */
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		/** Prevent the screen from sleeping while the game is active */
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		/** Use the xml layout file. The GLSufaceView is declared in this */
		setContentView(R.layout.gameinit);

		/**
		 * Create objects of GLSurfaceView, NativeRender and the two objects
		 * that are used for define the pixel resolution of current display;
		 * DisplayMetrics & Scaler
		 */
		mGLSurfaceView = (SurfaceView) findViewById(R.id.surface_view);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		Scaler scaler = new Scaler(dm.widthPixels, dm.heightPixels);

		hudHandler = new UIHandler(scaler);
		hudHandler.start();

		mGLSurfaceView.setScreenHeight(dm.heightPixels);

		// We need this to communicate with our GUI.
		if (multiplayergame) {
			gameLoopGui = new GameLoopGUI(this, hudHandler, true);
		} else {
			gameLoopGui = new GameLoopGUI(this, hudHandler, false);
		}

		// Fetch information from previous intent. The information will contain
		// the
		// map and difficulty decided by the player.
		Bundle extras = getIntent().getExtras();
		mapChoice = 0;
		int difficulty = 0;
		int gameMode = 0;

		if (extras != null) {
			// Log.d("GAMEINIT", "Extras != null, fetching intents...");
			mapChoice = extras.getInt("com.crackedcarrot.menu.map");
			difficulty = extras.getInt("com.crackedcarrot.menu.difficulty");
			gameMode = extras.getInt("com.crackedcarrot.menu.wave");
		}

		// Are we resuming an old saved game?
		SharedPreferences resume = getSharedPreferences("resume", 0);
		int resumes = 0;

		if (gameMode == 0) {
			if (mapChoice == 0) {
				// Increase the resumes-counter, keep people from cheating.
				SharedPreferences.Editor editor = resume.edit();
				editor.putInt("resumes", resume.getInt("resumes", 0) + 1);
				editor.commit();
				resumes = resume.getInt("resumes", 0);
				difficulty = -1; // load saved health/money-values as well.
			} else if (multiplayergame == false) {
				// We are not resuming anything, clear the old flag(s) and
				// prepare for a new save. Saves the chosen map directly.
				SharedPreferences.Editor editor = resume.edit();
				editor.putInt("map", mapChoice);
				editor.putInt("resumes", 0);
				editor.commit();
			}
		}
		// Create the map requested by the player

		// resume needs to load the correct map as well.
		if (resumes > 0)
			mapChoice = resume.getInt("map", 0);

		mapLoader = new MapLoader(this, scaler);
		Map gameMap = null;
		if (mapChoice == 1) {
			gameMap = mapLoader.readLevel("level1");
		} else if (mapChoice == 2) {
			gameMap = mapLoader.readLevel("level2");
		} else if (mapChoice == 3) {
			gameMap = mapLoader.readLevel("level3");
		} else if (mapChoice == 4) {
			gameMap = mapLoader.readLevel("level4");
		} else if (mapChoice == 5) {
			gameMap = mapLoader.readLevel("level5");
		} else {
			gameMap = mapLoader.readLevel("level6");
		}

		NativeRender nativeRenderer = new NativeRender(this, mGLSurfaceView,
				TextureLibraryLoader.loadTextures(gameMap.getTextureFile(),
						this), hudHandler.getOverlayObjectsToRender());
		mGLSurfaceView.setRenderer(nativeRenderer);

		// We will init soundmanager here insteed
		soundManager = new SoundManager(getBaseContext());
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

		// Define player specific variables depending on difficulty.
		Player p;
		if (difficulty == 0) {
			p = new Player(difficulty, 60, 50, 10);
		} else if (difficulty == 1) {
			p = new Player(difficulty, 50, 50, 10);
		} else if (difficulty == 2) {
			p = new Player(difficulty, 40, 50, 10);
		} else { // resume.
			p = new Player(resume.getInt("difficulty", 0), resume.getInt(
					"health", 0), resume.getInt("money", 0), 10);
		}

		// Load the creature waves and apply the correct difficulty
		WaveLoader waveLoad = new WaveLoader(this, scaler);
		Level[] waveList;

		if (gameMode == 1) {
			// Normal multiplayer game
			waveList = waveLoad.readWave("wave_multiplayer_game", 20,
					difficulty);
		} else if (gameMode == 2) {
			// Survival multiplayer game
			waveList = waveLoad.readWave(difficulty);
			survivalGame = true;
		} else if (gameMode == 3) {
			// Survival game. No multiplayer
			waveList = waveLoad.readWave(difficulty);
			survivalGame = true;
		} else {
			// Normal game

			int maxNbrWaves = 0;

			if (mapChoice == 1)
				maxNbrWaves = 30;
			else if (mapChoice == 2)
				maxNbrWaves = 35;
			else if (mapChoice == 3)
				maxNbrWaves = 40;
			else if (mapChoice == 4)
				maxNbrWaves = 45;
			else if (mapChoice == 5)
				maxNbrWaves = 50;
			else
				maxNbrWaves = 55;

			waveList = waveLoad.readWave("wave_normal_game", maxNbrWaves,
					difficulty);

		}

		// Load all available towers and the shots related to the tower
		TowerLoader towerLoad = new TowerLoader(this, scaler, soundManager);
		Tower[] tTypes = towerLoad.readTowers("towers");

		if (multiplayergame) {
			// Log.d("GAMEINIT", "Create multiplayerGameLoop");

			gLoop = new MultiplayerGameLoop(nativeRenderer, gameMap, waveList,
					tTypes, p, gameLoopGui, soundManager, mMultiplayerService,
					survivalGame);
			gameLoop = gLoop;

			mMultiplayerService.setLoopAndGUI(gLoop, gameLoopGui);

		} else {
			// Sending data to GAMELOOP
			// Log.d("GAMEINIT", "Create ordinary GameLoop");
			gameLoop = new GameLoop(nativeRenderer, gameMap, waveList, tTypes,
					p, gameLoopGui, soundManager, survivalGame);
		}

		// Resuming old game? Prepare GameLoop for this...
		if (resumes > 0) {
			gameLoop.resume(resume.getInt("level", 0),
					resume.getString("towers", null));
		}

		gameLoopThread = new Thread(gameLoop);

		mGLSurfaceView.setSimulationRuntime(gameLoop);
		mGLSurfaceView.setHUDHandler(hudHandler);

		// Uncomment this to start cpu profileing (IT KICKS ROYAL ASS!)
		// You also need to uncomment the stopMethodTraceing() further down.
		// Debug.startMethodTracing();

		// Start GameLoop
		gameLoopThread.start();
	}

	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	protected void onDestroy() {
		super.onDestroy();
		// Log.d("GAMEINIT", "onDestroy");
	}

	protected void onPause() {
		super.onPause();
		// Log.d("GAMEINIT", "onPause");
	}

	protected void onRestart() {
		super.onRestart();
		// Log.d("GAMEINIT", "onRestart");
	}

	protected void onResume() {
		super.onResume();
		// Log.d("GAMEINIT", "onResume");
	}

	protected void onStart() {
		super.onStart();
		// Log.d("GAMEINIT", "onStart");
	}

	protected void onStop() {
		super.onStop();
		// Fix for user pressing Home during the game.
		if (!multiplayergame)
			gameLoopGui.quitDialogPressed = true;

		gameLoop.stopGameLoop();
		gameLoop.soundManager.release();
		if (multiplayergame) {
			GameInit.mMultiplayerService.endBluetooth();
			GameInit.mMultiplayerService = null;
			// Log.d("GAMEINIT", "End Bluetooth");
		}
		// Log.d("GAMEINIT", "onStop");

		// You also need to stop the trace when you are done!
		// Debug.stopMethodTracing();
	}

	public void saveGame(int i) {

		// This uses Android's own internal storage-system to save all
		// currently relevent information to restore the game to the
		// beginning of the next wave of creatures.
		// This is probably (read: only meant to work with) best called
		// in between levels when the NextLevel-dialog is shown.

		// Never save multiplayer-status.
		if (multiplayergame == true || survivalGame == true)
			return;

		if (i == 1) {
			// Log.d("GAMEINIT", "Saving game status...");
			// Save everything.
			SharedPreferences resume = getSharedPreferences("resume", 0);
			SharedPreferences.Editor editor = resume.edit();
			// editor.putInt("map",... <- this is saved above, at the if
			// (mapChoice == 0) check.
			editor.putInt("difficulty", gameLoop.getPlayerData()
					.getDifficulty());
			editor.putInt("health", gameLoop.getPlayerData().getHealth());
			editor.putInt("level", gameLoop.getLevelNumber());
			editor.putInt("money", gameLoop.getPlayerData().getMoney());
			editor.putInt("resumes", resume.getInt("resumes", 0));
			editor.putString("towers", gameLoop.resumeGetTowers());
			editor.commit();
			// Log.d("GAMEINIT","resumes: " + resume.getInt("resumes", 0));
		} else {
			// Log.d("GAMEINIT", "Erasing saved game status.");
			// Dont allow resume. Clears the main resume flag!
			SharedPreferences resume = getSharedPreferences("resume", 0);
			SharedPreferences.Editor editor = resume.edit();
			editor.putInt("resumes", -1);
			editor.commit();
			// Log.d("GAMEINIT","resumes: " + -1);
		}
	}
}