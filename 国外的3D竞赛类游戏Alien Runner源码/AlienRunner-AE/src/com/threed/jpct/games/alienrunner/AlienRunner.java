package com.threed.jpct.games.alienrunner;

import java.lang.reflect.Field;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.threed.jpct.Camera;
import com.threed.jpct.Config;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Interact2D;
import com.threed.jpct.Logger;
import com.threed.jpct.RGBColor;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;
import com.threed.jpct.games.alienrunner.glfont.GLFont;
import com.threed.jpct.games.alienrunner.glfont.Rectangle;
import com.threed.jpct.util.MemoryHelper;

public class AlienRunner extends Activity {

	private static Object syncy = new Object();

	private static AlienRunner master = null;
	private final static int[] SCORE_POSITIONS = new int[] { 0, 20, 33, 51, 68, 88, 105, 123, 143, 160, 180, 196, 205 }; // 0123456789s.
	private final static long serialVersionUID = 1L;

	private GLSurfaceView mGLView;
	private MyRenderer renderer = null;
	private PowerManager.WakeLock wl = null;

	private boolean loaded = false;
	private boolean bit32 = false;
	private boolean vbo = true;

	private boolean[] movement = new boolean[2];

	private float x = 0;
	private float y = 0;
	private float lastX = 0;

	private boolean initializing = false;
	private long lastEvent = 0;
	private long lastDownEvent = 0;
	private long stoppedTime = 0;
	private boolean startTouch = false;
	private boolean leftDown = false;
	private boolean rightDown = false;

	private FrameBuffer buffer = null;
	private World world = null;

	private Texture font = null;

	private boolean showLoadingScreen = false;
	private boolean firstIter = true;

	private Ticker ticker = null;

	private Player player = null;

	private EndlessTrack track = null;
	private RGBColor fadeColor = new RGBColor(192, 135, 46);
	private RGBColor ambientColor = new RGBColor(70, 70, 70);
	private ObjectPool objPool = null;
	private Level level = null;

	private ParticleManager partMan = null;

	private Texture[] sky = null;

	private boolean left = false;
	private boolean right = false;
	private int arrowX = 0;

	private Texture loadingText = null;
	private Texture loadingColors = null;
	private Texture medalTexture = null;
	private Texture finishedTexture = null;
	private Texture startTexture = null;
	private Texture arrowTexture = null;
	private Texture speakerTexture = null;

	private long lastSoundChange = 0;

	private SimpleVector tmp = new SimpleVector();

	private int fps = 0;
	private int lfps = 0;
	private long lastTime = 0;
	private long sleepDiv = 0;

	private int width = 0;
	private int height = 0;
	private int almostCenter = 0;
	private long selectionTime = 0;

	private ScoreManager scoreMan = ScoreManager.getInstance();
	private Texture scoreFont = null;

	private boolean readyToLoad = false;

	private GLFont parFont = null;
	private Rectangle parSize = null;

	private EventRegistrator regis = EventRegistrator.getInstance();

	private float skyOffset = 0;
	private float skyDirection = 1;

	private Recorder recorder = new Recorder();
	private GhostPlayer ghost = new GhostPlayer();
	private Ghost ghostObj = null;

	private Rotator rotator = null;

	private float textScale = 0.1f;
	private boolean blockRender = false;
	private boolean showSelection = true;
	private int selected = -1;

	private Handler myHandler = new Handler();
	private Toast toast = null;
	private long guiTime = 0;
	private boolean ok = false;
	private boolean menuShowing = false;
	private boolean jinglePlayed = false;

	// private MediaPlayer mp = null;

	protected void onCreate(Bundle savedInstanceState) {
		Logger.log("Activity created!");

		if (master != null) {
			copy(master);
		}

		FontProvider.inject(getAssets());
		setConfig();

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN);

		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
		mGLView = (GLSurfaceView) this.findViewById(R.id.surface_view);// new
		// GLSurfaceView(getApplication());

		mGLView.setEGLConfigChooser(new GLSurfaceView.EGLConfigChooser() {
			public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
				int[] attributes = new int[] { EGL10.EGL_DEPTH_SIZE, 16, EGL10.EGL_NONE };
				EGLConfig[] configs = new EGLConfig[1];
				int[] result = new int[1];
				egl.eglChooseConfig(display, attributes, configs, 1, result);
				return configs[0];
			}
		});

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		if (renderer != null) {
			renderer.stop();
		}

		renderer = new MyRenderer();
		mGLView.setRenderer(renderer);

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "wtf?");
		wl.acquire();

		// mp = MediaPlayer.create(AlienRunner.this, R.raw.music);
	}

	private void copy(AlienRunner src) {
		try {
			Logger.log("Copying data from master Activity!");
			Field[] fs = src.getClass().getDeclaredFields();
			for (Field f : fs) {
				f.setAccessible(true);
				f.set(this, f.get(src));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void setConfig() {
		Config.maxPolysVisible = 500;
		Config.farPlane = 1500;
		Config.nearPlane = 1;

		Texture.defaultTo4bpp(!bit32);
		Texture.defaultToMipmapping(GameConfig.mipmaps);

		Config.useVBO = vbo;
	}

	@Override
	protected void onDestroy() {
		Logger.log("Activity destroyed!");
		super.onDestroy();
		if (wl != null && wl.isHeld()) {
			wl.release();
		}
		if (loaded) {
			renderer.stop();
		}
	}

	@Override
	protected void onPause() {
		Logger.log("Activity paused!");
		if (buffer != null) {
			buffer.freeMemory();
		}
		super.onPause();
		mGLView.onPause();
		Ticker.setPaused(true);
		if (wl != null && wl.isHeld()) {
			wl.release();
		}
		if (master != null) {
			blockRender = true;
		}
	}

	@Override
	protected void onResume() {
		Logger.log("Activity resumed!");
		super.onResume();
		mGLView.onResume();
		Ticker.setPaused(false);
		if (master != null) {
			blockRender = true;
		}
		if (wl != null && !wl.isHeld()) {
			wl.acquire();
		}
	}

	protected void onStop() {
		Logger.log("Activity stopped!");
		if (buffer != null) {
			buffer.freeMemory();
		}
		super.onStop();
		if (wl != null && wl.isHeld()) {
			wl.release();
		}
		if (loaded) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
			Ticker.setPaused(true);
		}
		if (master != null) {
			blockRender = true;
		}
	}

	/*
	 * public boolean onKeyDown(int keyCode, KeyEvent msg) {
	 * 
	 * if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) { if (mp.isPlaying()) {
	 * mp.pause(); } else { mp.start(); mp.setLooping(true); } return true; }
	 * 
	 * return super.onKeyDown(keyCode, msg); }
	 */

	protected boolean isFullscreenOpaque() {
		return true;
	}

	private void showOptionsMenu() {
		Ticker.setPaused(true);
		menuShowing = true;

		View v2 = this.findViewById(R.id.mylayout);

		if (v2.getVisibility() == View.VISIBLE) {
			return;
		}

		mGLView.setVisibility(View.INVISIBLE);
		v2.setVisibility(View.VISIBLE);

		((CheckBox) this.findViewById(R.id.particles)).setChecked(GameConfig.particles);
		((CheckBox) this.findViewById(R.id.glows)).setChecked(GameConfig.glow);
		((CheckBox) this.findViewById(R.id.backdrop)).setChecked(GameConfig.backDrop);
		((CheckBox) this.findViewById(R.id.limiter)).setChecked(GameConfig.throttling);
		((CheckBox) this.findViewById(R.id.arrow)).setChecked(GameConfig.showArrow);
		((CheckBox) this.findViewById(R.id.decorations)).setChecked(GameConfig.showDecorations);
		((CheckBox) this.findViewById(R.id.camera)).setChecked(GameConfig.staticCamera);
		((CheckBox) this.findViewById(R.id.fps)).setChecked(GameConfig.showFps);

		Button button = (Button) this.findViewById(R.id.Button01);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				GameConfig.particles = ((CheckBox) AlienRunner.this.findViewById(R.id.particles)).isChecked();
				GameConfig.glow = ((CheckBox) AlienRunner.this.findViewById(R.id.glows)).isChecked();
				GameConfig.backDrop = ((CheckBox) AlienRunner.this.findViewById(R.id.backdrop)).isChecked();
				GameConfig.throttling = ((CheckBox) AlienRunner.this.findViewById(R.id.limiter)).isChecked();
				GameConfig.showArrow = ((CheckBox) AlienRunner.this.findViewById(R.id.arrow)).isChecked();
				GameConfig.showDecorations = ((CheckBox) AlienRunner.this.findViewById(R.id.decorations)).isChecked();
				GameConfig.staticCamera = ((CheckBox) AlienRunner.this.findViewById(R.id.camera)).isChecked();
				GameConfig.showFps = ((CheckBox) AlienRunner.this.findViewById(R.id.fps)).isChecked();

				GameConfig.saveConfig(getBaseContext());
				forceCloseOptionsMenu();
			}
		});
	}

	private void forceCloseOptionsMenu() {
		View v2 = this.findViewById(R.id.mylayout);
		v2.setVisibility(View.INVISIBLE);
		MemoryHelper.compact();
		mGLView.setVisibility(View.VISIBLE);
		Ticker.setPaused(false);
		menuShowing = false;
	}

	public boolean onPrepareOptionsMenu(Menu menu) {
		showOptionsMenu();
		return true;
	}

	public boolean onKeyUp(int keyCode, KeyEvent msg) {

		if (menuShowing) {
			return super.onKeyUp(keyCode, msg);
		}

		lastEvent = Ticker.getRawTime();

		if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
			ok = false;
			if (showSelection || player.isStopped() || player.hasFinished()) {
				if (checkStartTouch()) {
					return true;
				}
			}
		}

		if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			rightDown = false;
			return true;
		}

		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			leftDown = false;
			return true;
		}

		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			if (checkStartTouch()) {
				return true;
			}
		}

		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			if (GameConfig.pauseAllowed) {
				Ticker.setPaused(!Ticker.isPaused());
			}
			return true;
		}

		return super.onKeyUp(keyCode, msg);
	}

	private boolean checkStartTouch() {

		if (!loaded) {
			return true;
		}

		if (showSelection && rotator != null) {
			if (rotator.getLevel() != -1) {
				selected = rotator.getLevel();
				showSelection = false;
				Ticker.setPaused(false);
				SoundManager.getInstance().play(SoundManager.CLICK);
			}
			return true;
		}
		if (stoppedTime != 0) {
			if (lastEvent - stoppedTime > 1200) {
				startTouch = true;
				return true;
			}
		}
		return false;
	}

	public boolean onKeyDown(int keyCode, KeyEvent msg) {

		if (!loaded) {
			return true;
		}

		if (menuShowing) {
			if ((keyCode == KeyEvent.KEYCODE_BACK)) {
				forceCloseOptionsMenu();
				return true;
			}
			return super.onKeyDown(keyCode, msg);
		}

		if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
			ok = true;
		}

		if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			rightDown = true;
			if (showSelection) {
				rotator.scroll(false);
			}
			return true;
		}

		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			leftDown = true;
			if (showSelection) {
				rotator.scroll(true);
			}
			return true;
		}

		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (!showSelection) {
				// player.setStopped(true);
				Ticker.setPaused(true);
				guiTime = Ticker.getRawTime();
				showSelection();
				return true;
			} else {
				showSelection = false;
				Ticker.setPaused(false);
				return true;
			}
		}

		return super.onKeyDown(keyCode, msg);
	}

	private void showSelection() {
		showSelection = true;
		selectionTime = Ticker.getRawTime();
		lastDownEvent = 0;
	}

	public boolean onTouchEvent(MotionEvent event) {

		y = 0;

		if (!loaded) {
			return true;
		}

		if (menuShowing) {
			return super.onTouchEvent(event);
		}

		boolean evaluated = false;
		lastEvent = Ticker.getRawTime();
		int action = event.getAction();

		if (showSelection) {
			if (lastEvent - selectionTime < 500 && selectionTime != 0) {
				return true;
			}

			if (action == MotionEvent.ACTION_MOVE) {
				lastDownEvent = 0;
				float px = event.getX();
				float dx = Math.abs(x - px);
				if (dx > 40 && x != -1) {
					if (px > x) {
						rotator.scroll(true);
					}
					if (px < x) {
						rotator.scroll(false);
					}
					x = px;
					return true;
				}

			}
			if (action == MotionEvent.ACTION_DOWN) {
				lastDownEvent = lastEvent;
				x = event.getX();
				return true;
			}
		}

		if (!showSelection || (action == MotionEvent.ACTION_UP || (lastEvent - lastDownEvent < 2500 && Math.abs(x - event.getX()) <= 40))) {
			if (checkStartTouch()) {
				return true;
			}
		}

		if (!showSelection) {
			if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
				x = (int) event.getX();
				y = (int) event.getY();
				evaluated = true;
			}
		}

		// Silence the touch events a little...
		try {
			Thread.sleep(GameConfig.eventSleepTime);
		} catch (InterruptedException e) {
		}

		return evaluated;
	}

	class MyRenderer implements GLSurfaceView.Renderer {

		private boolean init = false;
		private boolean stop = false;

		public MyRenderer() {
		}

		public void stop() {
			stop = true;
			init = false;
		}

		public void onSurfaceChanged(GL10 gl, int w, int h) {
			synchronized (syncy) {

				Logger.log("Initializing surface with width: " + w + "/height: " + h);
				if (buffer != null) {
					buffer.dispose();
				}
				buffer = new FrameBuffer(gl, w, h);

				if (!init && master == null && !initializing) {
					initializing = true;
					loadingText = new Texture(getResources().openRawResource(R.raw.c64_bb));
					loadingColors = new Texture(getResources().openRawResource(R.raw.c64_colors));
					showLoadingScreen = true;

					loadingText.setMipmap(false);
					loadingColors.setMipmap(false);
					loadingColors.setFiltering(false);

					new Thread() {
						public void run() {
							// Hack to avoid heap increases during runtime.
							int[] memoryAlloc = new int[1024 * 512 * 3]; // grow
							// heap
							// ~6mb
							memoryAlloc[0] = 1; // use it
							memoryAlloc = null; // null it

							loadData();

							if (master == null) {
								Logger.log("Saving master Activity!");
								master = AlienRunner.this;
							}

							showLoadingScreen = false;
							init = true;
							loaded = true;
							initializing = false;
						}
					}.start();
				} else {
					while (initializing) {
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
						}
					}

					init = true;
					Ticker.setPaused(true);
					MemoryHelper.compact();
					world.setFogging(World.FOGGING_ENABLED);
					world.setFogParameters(450, 600, fadeColor.getRed(), fadeColor.getGreen(), fadeColor.getBlue());
					world.setAmbientLight(ambientColor.getRed(), ambientColor.getGreen(), ambientColor.getBlue());
					Ticker.setPaused(false);
					blockRender = false;
				}
			}
		}

		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			Logger.log("Surface created!");
		}

		private void loadData() {

			while (!readyToLoad) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
				}
			}

			GameConfig.loadConfig(getBaseContext());

			Resources res = getResources();
			SoundManager.inject(res, getBaseContext());
			TextureManager texMan = TextureManager.getInstance();

			world = new World();
			player = new Player(res);
			world.addObject(player);

			width = buffer.getWidth();
			height = buffer.getHeight();

			float mul = (float) height / GameConfig.defaultHeight;
			almostCenter = (int) (128f * mul);

			partMan = new ParticleManager(world);
			objPool = new ObjectPool(world, res);

			track = new EndlessTrack(res);
			track.add(world);

			level = new Level(res.openRawResource(R.raw.level1));

			medalTexture = new Texture(res.openRawResource(R.raw.medal));
			arrowTexture = new Texture(res.openRawResource(R.raw.arrow));
			speakerTexture = new Texture(res.openRawResource(R.raw.speakers), true);

			texMan.addTexture("rubble", new Texture(res.openRawResource(R.raw.rubble)));
			texMan.addTexture("particle", new Texture(res.openRawResource(R.raw.star)));
			texMan.addTexture("collected", new Texture(res.openRawResource(R.raw.collected), true));
			texMan.addTexture("drop", new Texture(res.openRawResource(R.raw.drop), true));
			texMan.addTexture("finished", finishedTexture = new Texture(res.openRawResource(R.raw.finished), true));
			texMan.addTexture("start", startTexture = new Texture(res.openRawResource(R.raw.start), true));

			scoreFont = new Texture(res.openRawResource(R.raw.score), true);

			Camera cam = world.getCamera();
			cam.rotateCameraX(0.175f);
			cam.setFOV(2);
			setCamera();

			world.setFogging(World.FOGGING_ENABLED);
			fadeColor = level.getFog();
			ambientColor = level.getAmbient();
			world.setFogParameters(450, 600, fadeColor.getRed(), fadeColor.getGreen(), fadeColor.getBlue());
			world.setAmbientLight(ambientColor.getRed(), ambientColor.getGreen(), ambientColor.getBlue());

			// Set initial position.
			tmp = player.getTranslation(tmp);
			SimpleVector td = Interact2D.project3D2D(world.getCamera(), buffer, tmp);
			x = td.x;

			font = new Texture(res.openRawResource(R.raw.numbers));

			sky = new Texture[3];

			sky[0] = new Texture(res.openRawResource(R.raw.sky), false);
			sky[0].setMipmap(false);
			sky[1] = new Texture(res.openRawResource(R.raw.sky2), false);
			sky[1].setMipmap(false);
			sky[2] = new Texture(res.openRawResource(R.raw.sky3), false);
			sky[2].setMipmap(false);

			scoreFont.setMipmap(false);
			font.setMipmap(false);
			medalTexture.setMipmap(false);
			arrowTexture.setMipmap(false);

			ghostObj = new Ghost(res);
			world.addObject(ghostObj);
			loadGhost();

			rotator = new Rotator(res);

			world.compileAllObjects(buffer);

			// TextureManager.getInstance().compress();

			MemoryHelper.compact();
		}

		private void checkInput(long ticks) {

			float widthMul = (float) buffer.getWidth() / (float) GameConfig.defaultWidth;

			if (leftDown) {
				x -= ticks * 7 * widthMul;
				if (x < 5) {
					x = 5;
				}
			}
			if (rightDown) {
				x += ticks * 7 * widthMul;
				if (x > width - 5) {
					x = width - 5;
				}
			}

			tmp = player.getTranslation(tmp);
			SimpleVector td = Interact2D.project3D2D(world.getCamera(), buffer, tmp, tmp);
			left = false;
			right = false;
			if (td != null && Math.abs(x - lastX) > 1 * widthMul) {
				if (x > td.x) {
					right = true;
				}

				if (x < td.x) {
					left = true;
				}
			}
			if (!right && !left && td != null) {
				x = td.x;
				lastX = x;
			}
		}

		private void setCamera() {
			Camera cam = world.getCamera();
			SimpleVector p = player.getTranslation(tmp);
			if (GameConfig.staticCamera) {
				p.y = -50;
				p.x = 0;
				p.z -= 100;
			} else {
				p.y -= 40;
				p.x = 0;
				p.z -= 100;
			}
			cam.setPosition(p);
		}

		private void reset() {
			reset(false);
		}

		private void reset(boolean all) {
			if (player.isStopped()) {
				if (player.hasFinished() || all) {
					level.reset();
					track.reset();
					objPool.reset();
					regis.reset();
					partMan.reset();

					ticker = null;
					fps = 0;
					lfps = 0;
				}
				arrowX = buffer.getWidth() / 2;
				x = arrowX;
				player.reset();
				loadGhost();
				scoreMan.reset();
				recorder.rewind();
				textScale = 0.1f;
				lastX = 0;
			}
		}

		private void loadGhost() {
			ghost.load(getBaseContext(), level);
			if (ghost.isLoaded()) {
				ghostObj.init(ghost);
				ghostObj.setVisibility(true);
			} else {
				ghostObj.setVisibility(false);
			}
		}

		private void loadSelectedLevel() {
			if (selected == -1) {
				return;
			}

			final int sel = selected;
			myHandler.post(new Runnable() {
				public void run() {
					toast = Toast.makeText(getBaseContext(), "Loading track " + sel, Toast.LENGTH_LONG);
					toast.show();
				}
			});

			if (selected != level.getNumber()) {
				switch (selected) {
				case 1:
					selected = -1;
					level = new Level(getResources().openRawResource(R.raw.level1));
					break;
				case 2:
					selected = -1;
					level = new Level(getResources().openRawResource(R.raw.level2));
					break;
				case 3:
					selected = -1;
					level = new Level(getResources().openRawResource(R.raw.level3));
					break;
				default:
					throw new RuntimeException("Unknown level: " + selected);
				}
			} else {
				selected = -1;
			}

			fadeColor = level.getFog();
			ambientColor = level.getAmbient();
			world.setFogging(World.FOGGING_ENABLED);
			world.setFogParameters(450, 600, fadeColor.getRed(), fadeColor.getGreen(), fadeColor.getBlue());
			world.setAmbientLight(ambientColor.getRed(), ambientColor.getGreen(), ambientColor.getBlue());

			track.setTexture(level.getTexture());

			player.setStopped(true);
			player.setFinished(false);
			stoppedTime = Ticker.getRawTime();
			reset(true);

			myHandler.post(new Runnable() {
				public void run() {
					toast.cancel();
				}
			});

			showSelection = false;
		}

		private void updateGameState() {
			if (selected != -1) {
				loadSelectedLevel();
			}

			if (startTouch) {
				if (player.hasFinished()) {
					reset();
					lastEvent = Ticker.getRawTime();
					stoppedTime = lastEvent;
					showSelection();
				} else {
					player.reset();
					player.setStopped(false);
					stoppedTime = 0;
				}
				startTouch = false;
				textScale = 0.1f;
			}

			if (player.hasFinished() && player.isStopped() && stoppedTime == 0) {
				stoppedTime = Ticker.getRawTime();
				EventRegistrator.getInstance().process(buffer, medalTexture);
				if (ghost.getScore() < ScoreManager.getInstance().getScore()) {
					recorder.writeData(getBaseContext(), level, player);
				}
			}

			int ticks = 0;
			long ft = 0;
			boolean firstLoop = false;
			if (ticker == null) {
				ticker = new Ticker(30);
				lastTime = Ticker.getRawTime();
				ft = Ticker.getRawTime();
				ticks = 1;
				left = false;
				right = false;
				firstLoop = true;
				lastEvent = Ticker.getRawTime();
			} else {
				ft = Ticker.getRawTime();
				ticks = ticker.getTicks();
				if (ticks > 5) {
					// Limit the number of ticks, let the game slow down
					// instead!
					ticks = 5;
				}
			}

			if (ticks > 0) {
				skyOffset += GameConfig.skySpeed * ticks * skyDirection;

				if (skyOffset > 128 && skyDirection == 1) {
					skyDirection = -1;
					skyOffset = 128;
				} else {
					if (skyOffset < 0 && skyDirection == -1) {
						skyDirection = 1;
						skyOffset = 0;
					}
				}

				checkInput(ticks);

				movement[0] = left;
				movement[1] = right;
				boolean fallen = player.move(ticks, buffer, world, partMan, objPool, recorder, level, movement, x, ok);

				if ((left && !movement[0]) || (right && !movement[1])) {
					lastX = x;
				}

				if (fallen) {
					lastEvent = 0;
				}

				renderGhost(ticks);

				objPool.process(player, partMan, level, track);

				partMan.move(ticks);
				track.process(player);

				setCamera();

				tmp = player.getTranslation(tmp);
				SimpleVector td = Interact2D.project3D2D(world.getCamera(), buffer, tmp, tmp);

				if (fallen) {
					// Stop movement
					if (td != null) {
						x = td.x;
						left = false;
						right = false;
					}
				} else {
					if ((left && td.x < x) || (right && td.x > x)) {
						x = td.x;
					}
				}

				if (arrowX == 0) {
					arrowX = (int) x - 16;
				}

				if (x - 16 != arrowX) {
					float d = arrowX - (x - 16);
					d *= ticks;
					d /= 7;
					arrowX -= d;
				}
			}

			buffer.clear(fadeColor);
			world.renderScene(buffer);

			if (GameConfig.backDrop) {
				// buffer.blit(sky, 0, 0, 0, 0, 256, 128, width, almostCenter,
				// -1, false, null);
				float mul = buffer.getWidth() / GameConfig.defaultWidth;
				float frac = skyOffset - (int) skyOffset;
				int add = (int) (((float) width / 128f) * frac);
				buffer.blit(sky[level.getSky()], (int) skyOffset, 0, -add, 0, 128, 128, width + (int) (3 * mul), almostCenter, -1, false, null);
			}

			world.draw(buffer);

			if (!showSelection) {
				blitScore();
				blitTime();
				blitPar();
				checkAndBlitSpeaker();
			}

			if (GameConfig.showFps) {
				blitNumber(lfps, 5, 5);
			}

			if (player.isStopped()) {
				if (!showSelection) {
					if (player.hasFinished()) {
						blitInfoText(finishedTexture, ticks);
					} else {
						blitInfoText(startTexture, ticks);
					}
				}
			} else {
				if (GameConfig.showArrow) {
					buffer.blit(arrowTexture, 0, 0, arrowX, (int) (height / 5f), 64, 64, height / 10, height / 10, 15, false, null);
				}
			}

			if (!showSelection) {
				regis.process(buffer, medalTexture);
			}

			if (showSelection) {
				long ticky = (Ticker.getRawTime() - guiTime) / 15;
				drawLevelSelection(ticky);
				if (ticky != 0) {
					guiTime = Ticker.getRawTime();
				}
				lastEvent = 0;
			}

			buffer.display();

			if (firstLoop) {
				System.runFinalization();
				System.gc();
				ticker.getTicks(); // skip ticks
			}

			ft = Ticker.getRawTime() - ft;

			long sleepTime = (GameConfig.sleepValue - ft) - sleepDiv;
			if (sleepTime < 0) {
				// Sanity check, but this shouldn't happen
				sleepTime = 0;
			}
			if (GameConfig.throttling) {
				long st = Ticker.getRawTime();
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					// Whoever decided that sleep has to throw a checked
					// exception should be executed...
				}
				long et = Ticker.getRawTime();
				sleepDiv = (et - st) - sleepTime;
			} else {
				Thread.yield();
			}

			fps++;

			long time = Ticker.getRawTime();

			if (time - lastTime >= 1000) {
				lastTime = Ticker.getRawTime();
				lfps = fps;
				fps = 0;
			}

			if (lastEvent != 0 && time - lastEvent >= 15000 && stoppedTime == 0) {
				regis.register(EventRegistrator.RELAXED);
			}
		}

		public void onDrawFrame(GL10 gl) {
			// world.removeAllObjects();
			if (blockRender) {
				return;
			}

			if (init) {
				if (!stop) {
					if (firstIter) {
						// Logger.log("Changing thread priority...");
						// Thread.currentThread().setPriority(Thread.MAX_PRIORITY
						// - 1);
						firstIter = false;
						stoppedTime = 1;
					}
					updateGameState();
				} else {
					try {
						buffer.clear();
						buffer.display();
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			if (showLoadingScreen) {
				buffer.clear();

				int end = buffer.getHeight();
				int dif = end / 16;
				int yPos = 0;
				int lastColor = -1;

				while (yPos < end) {
					int s = yPos;
					yPos += dif + (int) (-(end / 32) + Randomizer.random() * (end / 16));
					int color = 0;
					do {
						color = (int) (Randomizer.random() * 15 + 0.5f);
					} while (color == lastColor);
					lastColor = color;
					color *= 8;
					buffer.blit(loadingColors, color, 0, 0, s, 8, 8, buffer.getWidth() + 2, yPos - s, -1, false, null);
				}

				buffer.blit(loadingText, 0, 0, 0, 0, 256, 256, buffer.getWidth(), buffer.getHeight(), 1000, false, null);
				buffer.display();
				readyToLoad = true;
				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
				}
			} else {
				if (!jinglePlayed) {
					jinglePlayed = true;
					SoundManager.getInstance().play(SoundManager.JINGLE);
				}
			}
		}

		private void blitInfoText(Texture texture, long ticks) {

			if (textScale >= 0.5f && textScale != 1) {
				SoundManager.getInstance().play(SoundManager.ZOOM, 1500);
			}

			float w = width * textScale;
			float h = height * textScale;
			float wAdd = width / 2f - w / 2f;
			float hAdd = height / 2f - h / 2f;

			buffer.blit(texture, 0, 0, (int) wAdd, (int) hAdd, 256, 128, (int) w, (int) h, (int) (30 * textScale), false, null);
			textScale += ticks * 0.03f;
			if (textScale > 1) {
				textScale = 1;
			}
		}

		private void renderGhost(long ticks) {
			if (player.isStopped() || !player.isOnTrack()) {
				return;
			}
			ghostObj.update(ghost, ticks);
		}

		private void drawLevelSelection(long ticks) {
			rotator.paint(ticks, buffer);
		}

		private void blitNumber(int number, int x, int y) {
			if (font != null) {

				int max = 10;
				for (int i = max; i <= 10000; i = i * 10) {
					if (number / i == 0) {
						max = i / 10;
						break;
					}
				}

				for (int i = max; i > 0; i = i / 10) {
					int num = number / i;
					buffer.blit(font, num * 5, 0, x, y, 5, 9, FrameBuffer.TRANSPARENT_BLITTING);
					number -= num * i;
					x += 5;
				}
			}
		}

		private void blitScore() {
			if (scoreFont != null) {

				float mul = buffer.getWidth() / GameConfig.defaultWidth;
				int number = scoreMan.getScore();
				int snum = number;

				int max = 10;
				for (int i = max; i <= 1000000; i = i * 10) {
					if (number / i == 0) {
						max = i / 10;
						break;
					}
				}

				int width = 0;

				for (int i = max; i > 0; i = i / 10) {
					int num = number / i;
					width += (SCORE_POSITIONS[num + 1] - SCORE_POSITIONS[num]);
					number -= num * i;
				}
				width *= mul;

				int x = (int) (buffer.getWidth() - width - 10 * mul);
				int y = (int) (5 * mul);

				number = snum;

				for (int i = max; i > 0; i = i / 10) {
					int num = number / i;
					int w = SCORE_POSITIONS[num + 1] - SCORE_POSITIONS[num];
					buffer.blit(scoreFont, SCORE_POSITIONS[num], 0, x, y, w, 32, (int) (w * mul), (int) (32 * mul), 20, false, null);
					number -= num * i;
					x += w * mul;
				}
			}
		}

		private void blitPar() {
			if (parFont == null) {
				parFont = FontProvider.getFont(12, buffer, false);
				parSize = parFont.getStringBounds(level.getParText(), parSize);
			}

			int h = buffer.getHeight();
			parFont.blitString(buffer, level.getParText(), 5, h - 5, 50, null);
			if (ghost.isLoaded()) {
				parFont.blitString(buffer, ghost.getScoreText(), 5, h - 5 - parSize.height, 50, null);
			}
		}

		private void checkAndBlitSpeaker() {

			if (EventRegistrator.getInstance().isShowing()) {
				return;
			}

			float mul = buffer.getWidth() / GameConfig.defaultWidth;
			float xp = 5f * mul;
			float yp = 5f * mul;

			if (GameConfig.showFps) {
				xp = 32f * mul;
			}

			float size = 20 * mul;
			int srcX = 0;
			if (GameConfig.soundMul == 0.25f) {
				srcX = 33;
			}
			if (GameConfig.soundMul == 0f) {
				srcX = 66;
			}

			long time = Ticker.getRawTime();

			if (time - lastSoundChange > 1000) {
				if (x >= xp && x <= xp + size && y >= yp && y <= yp + size) {
					lastSoundChange = time;
					if (GameConfig.soundMul == 1) {
						GameConfig.soundMul = 0.25f;
					} else {
						if (GameConfig.soundMul == 0.25f) {
							GameConfig.soundMul = 0f;
						} else {
							if (GameConfig.soundMul == 0) {
								GameConfig.soundMul = 1f;
							}
						}
					}
				}
			}

			buffer.blit(speakerTexture, srcX, 0, (int) xp, (int) yp, 32, 32, (int) size, (int) size, 20, false, null);

		}

		private void blitTime() {
			if (scoreFont != null) {
				float mul = buffer.getWidth() / GameConfig.defaultWidth;
				int number = player.getTime();

				RGBColor col = RGBColor.GREEN;
				if (number > level.getLongPar() / 10) {
					col = RGBColor.RED;
				}

				if (number < 10) {
					return;
				}

				int snum = number;

				int max = 10;
				for (int i = max; i <= 1000000; i = i * 10) {
					if (number / i == 0) {
						max = i / 10;
						break;
					}
				}

				int width = (int) (SCORE_POSITIONS[12] - SCORE_POSITIONS[11] + SCORE_POSITIONS[12] - SCORE_POSITIONS[10]);

				for (int i = max; i > 0; i = i / 10) {
					int num = number / i;
					width += (SCORE_POSITIONS[num + 1] - SCORE_POSITIONS[num]);
					number -= num * i;
				}
				width *= mul;
				number = snum;
				int w = 0;

				if (number < 100) {
					w = (int) (SCORE_POSITIONS[1] - SCORE_POSITIONS[0]);
					width += w * mul;
				}

				int x = (int) (buffer.getWidth() - width - 10 * mul);
				int y = (int) (35 * mul);

				if (number < 100) {
					buffer.blit(scoreFont, SCORE_POSITIONS[0], 0, x, y, w, 32, (int) (w * mul), (int) (32 * mul), 20, false, col);
					x += w * mul;
				}

				for (int i = max; i > 0; i = i / 10) {
					int num = number / i;
					if (i == 10) {
						w = SCORE_POSITIONS[12] - SCORE_POSITIONS[11];
						buffer.blit(scoreFont, SCORE_POSITIONS[11], 0, x, y, w, 32, (int) (w * mul), (int) (32 * mul), 20, false, col);
						x += w * mul;
					}
					w = SCORE_POSITIONS[num + 1] - SCORE_POSITIONS[num];
					buffer.blit(scoreFont, SCORE_POSITIONS[num], 0, x, y, w, 32, (int) (w * mul), (int) (32 * mul), 20, false, col);
					number -= num * i;
					x += w * mul;
				}
				w = SCORE_POSITIONS[12] - SCORE_POSITIONS[10];
				buffer.blit(scoreFont, SCORE_POSITIONS[10], 0, x, y, w, 32, (int) (w * mul), (int) (32 * mul), 20, false, col);
			}
		}

	}
}