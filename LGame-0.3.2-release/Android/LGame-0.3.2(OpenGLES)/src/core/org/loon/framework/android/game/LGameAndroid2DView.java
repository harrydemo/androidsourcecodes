package org.loon.framework.android.game;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.opengles.GL10;

import org.loon.framework.android.game.LGameAndroid2DActivity.LMode;
import org.loon.framework.android.game.action.ActionControl;
import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.core.geom.RectBox;
import org.loon.framework.android.game.core.graphics.LFont;
import org.loon.framework.android.game.core.graphics.Screen;
import org.loon.framework.android.game.core.graphics.opengl.GL;
import org.loon.framework.android.game.core.graphics.opengl.GLColor;
import org.loon.framework.android.game.core.graphics.opengl.GLEx;
import org.loon.framework.android.game.core.graphics.opengl.LTexture;
import org.loon.framework.android.game.core.graphics.opengl.LTextures;
import org.loon.framework.android.game.core.graphics.opengl.LTexture.Format;
import org.loon.framework.android.game.core.input.LProcess;
import org.loon.framework.android.game.core.timer.LTimerContext;
import org.loon.framework.android.game.core.timer.SystemTimer;
import org.loon.framework.android.game.utils.GraphicsUtils;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView.Renderer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * 
 * Copyright 2008 - 2011
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * @project loonframework
 * @author chenpeng
 * @email：ceponline@yahoo.com.cn
 * @version 0.1.1
 */
public final class LGameAndroid2DView implements Renderer {

	// 默认不开启VBO加速（选择开启的话有可能减低兼容性）
	private GLMode glMode = GLMode.Default;

	public static enum GLMode {

		Default, VBO;

		private String text;

		private GLMode() {
			text = "GLMode : " + name();
		}

		public String toString() {
			return text;
		};
	}

	private long lastTimeMicros, currTimeMicros, goalTimeMicros,
			elapsedTimeMicros, remainderMicros, elapsedTime, frameCount,
			frames;

	private long maxFrames, curFPS;

	private final Object synch = new Object();

	private final LTimerContext timerContext = new LTimerContext();

	private LGameLogo logoFlag;

	private SystemTimer timer;

	private boolean clear = true;

	private final LFont fpsFont = LFont.getFont(20);

	private boolean isFPS, isMemory;

	private boolean onRunning, onPause, onDestroy, onResume;

	private GLEx gl;

	private int maxWidth, maxHeight;

	private Context context;

	private Window window;

	private WindowManager windowManager;

	private SurfaceView surfaceView;

	private boolean supportVBO;

	private int width, height;

	private LProcess process;

	abstract static class GLType {

		static android.view.SurfaceHolder updateSurfaceHolder(
				android.view.SurfaceHolder holder,
				android.view.SurfaceHolder.Callback call) {
			if (holder != null) {
				holder.addCallback(call);
				int mode = 0;
				try {
					mode = 1;
					holder.setType(android.view.SurfaceHolder.SURFACE_TYPE_GPU);
				} catch (Exception e) {
					try {
						mode = 2;
						holder
								.setType(android.view.SurfaceHolder.SURFACE_TYPE_HARDWARE);

					} catch (Exception ex) {
						holder
								.setType(android.view.SurfaceHolder.SURFACE_TYPE_NORMAL);
					}
				}
				switch (mode) {
				case 1:
					Log.i("Android2DView", "GPU surface");
					break;
				case 2:
					Log.i("Android2DView", "Hardware surface");
					break;
				default:
					Log
							.i("Android2DView",
									"No hardware acceleration available");
				}
				holder.setFormat(PixelFormat.RGB_565);
			}
			return holder;
		}

	}

	/**
	 * 构建LGameAndroid2DView
	 * 
	 * @param activity
	 * @param mode
	 * @param fullScreen
	 * @param landscape
	 */
	public LGameAndroid2DView(LGameAndroid2DActivity activity, LMode mode,
			boolean landscape, boolean fullScreen) {
		this.setFPS(LSystem.DEFAULT_MAX_FPS);
		this.initScreen(activity, mode, landscape, fullScreen);
		this.surfaceView = createGLSurfaceView(activity);
		this.process = LSystem.screenProcess;
	}

	/**
	 * 初始化窗体设定
	 * 
	 * @param activity
	 * @param mode
	 * @param fullScreen
	 * @param landscape
	 */
	private void initScreen(LGameAndroid2DActivity activity, LMode mode,
			boolean fullScreen, boolean landscape) {
		LSystem.screenActivity = activity;
		this.context = activity.getApplicationContext();
		this.window = activity.getWindow();
		this.windowManager = activity.getWindowManager();
		this.setFullScreen(fullScreen);
		this.setLandscape(landscape, mode);
	}

	/**
	 * 判断当前游戏屏幕是否需要拉伸
	 * 
	 * @return
	 */
	public boolean isScale() {
		return LSystem.scaleWidth != 1 || LSystem.scaleHeight != 1;
	}

	/**
	 * 强制设定横屏竖屏
	 * 
	 * @param landscape
	 * @param mode
	 */
	protected void setLandscape(boolean landscape, LMode mode) {
		try {
			if (landscape) {
				LSystem.screenActivity
						.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			} else {
				LSystem.screenActivity
						.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}

			RectBox d = getScreenDimension();

			LSystem.SCREEN_LANDSCAPE = landscape;

			this.maxWidth = (int) d.getWidth();
			this.maxHeight = (int) d.getHeight();

			if (landscape && (d.getWidth() > d.getHeight())) {
				maxWidth = (int) d.getWidth();
				maxHeight = (int) d.getHeight();
			} else if (landscape && (d.getWidth() < d.getHeight())) {
				maxHeight = (int) d.getWidth();
				maxWidth = (int) d.getHeight();
			} else if (!landscape && (d.getWidth() < d.getHeight())) {
				maxWidth = (int) d.getWidth();
				maxHeight = (int) d.getHeight();
			} else if (!landscape && (d.getWidth() > d.getHeight())) {
				maxHeight = (int) d.getWidth();
				maxWidth = (int) d.getHeight();
			}

			if (mode != LMode.Max) {
				if (landscape) {
					this.width = LSystem.MAX_SCREEN_WIDTH;
					this.height = LSystem.MAX_SCREEN_HEIGHT;
				} else {
					this.width = LSystem.MAX_SCREEN_HEIGHT;
					this.height = LSystem.MAX_SCREEN_WIDTH;
				}
			} else {
				if (landscape) {
					this.width = maxWidth >= LSystem.MAX_SCREEN_WIDTH ? LSystem.MAX_SCREEN_WIDTH
							: maxWidth;
					this.height = maxHeight >= LSystem.MAX_SCREEN_HEIGHT ? LSystem.MAX_SCREEN_HEIGHT
							: maxHeight;
				} else {
					this.width = maxWidth >= LSystem.MAX_SCREEN_HEIGHT ? LSystem.MAX_SCREEN_HEIGHT
							: maxWidth;
					this.height = maxHeight >= LSystem.MAX_SCREEN_WIDTH ? LSystem.MAX_SCREEN_WIDTH
							: maxHeight;
				}
			}

			if (mode == LMode.Fill) {

				LSystem.scaleWidth = ((float) maxWidth) / width;
				LSystem.scaleHeight = ((float) maxHeight) / height;

			} else if (mode == LMode.FitFill) {

				RectBox res = GraphicsUtils.fitLimitSize(width, height,
						maxWidth, maxHeight);
				maxWidth = res.width;
				maxHeight = res.height;
				LSystem.scaleWidth = ((float) maxWidth) / width;
				LSystem.scaleHeight = ((float) maxHeight) / height;

			} else if (mode == LMode.Ratio) {

				maxWidth = View.MeasureSpec.getSize(maxWidth);
				maxHeight = View.MeasureSpec.getSize(maxHeight);

				float userAspect = (float) width / (float) height;
				float realAspect = (float) maxWidth / (float) maxHeight;

				if (realAspect < userAspect) {
					maxHeight = Math.round(maxWidth / userAspect);
				} else {
					maxWidth = Math.round(maxHeight * userAspect);
				}

				LSystem.scaleWidth = ((float) maxWidth) / width;
				LSystem.scaleHeight = ((float) maxHeight) / height;

			} else if (mode == LMode.MaxRatio) {

				maxWidth = View.MeasureSpec.getSize(maxWidth);
				maxHeight = View.MeasureSpec.getSize(maxHeight);

				float userAspect = (float) width / (float) height;
				float realAspect = (float) maxWidth / (float) maxHeight;

				if ((realAspect < 1 && userAspect > 1)
						|| (realAspect > 1 && userAspect < 1)) {
					userAspect = (float) height / (float) width;
				}

				if (realAspect < userAspect) {
					maxHeight = Math.round(maxWidth / userAspect);
				} else {
					maxWidth = Math.round(maxHeight * userAspect);
				}

				LSystem.scaleWidth = ((float) maxWidth) / width;
				LSystem.scaleHeight = ((float) maxHeight) / height;

			} else {

				LSystem.scaleWidth = 1;
				LSystem.scaleHeight = 1;

			}

			LSystem.screenRect = new RectBox(0, 0, width, height);

			StringBuffer sbr = new StringBuffer();
			sbr.append("Mode:").append(mode);
			sbr.append("\nWidth:").append(width).append(",Height:" + height);
			sbr.append("\nMaxWidth:").append(maxWidth).append(
					",MaxHeight:" + maxHeight);
			sbr.append("\nScale:").append(isScale());
			Log.i("Android2DSize", sbr.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public boolean isPortrait() {
		final Display display = LSystem.screenActivity.getWindowManager()
				.getDefaultDisplay();
		return display.getWidth() < display.getHeight();
	}

	public boolean isLandscape() {
		final Display display = LSystem.screenActivity.getWindowManager()
				.getDefaultDisplay();
		return display.getWidth() > display.getHeight();
	}

	public boolean isSquare() {
		final Display display = LSystem.screenActivity.getWindowManager()
				.getDefaultDisplay();
		return display.getWidth() == display.getHeight();
	}

	public RectBox getScreenDimension() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		return new RectBox((int) dm.xdpi, (int) dm.ydpi, (int) dm.widthPixels,
				(int) dm.heightPixels);

	}

	public int getMaxWidth() {
		return maxWidth;
	}

	public int getMaxHeight() {
		return maxHeight;
	}

	public Context getContext() {
		return context;
	}

	public Window getWindow() {
		return window;
	}

	public WindowManager getWindowManager() {
		return windowManager;
	}

	private SurfaceView createGLSurfaceView(LGameAndroid2DActivity activity) {
		android.opengl.GLSurfaceView.EGLConfigChooser configChooser = getEglConfigChooser();
		if (LSystem.isOverrunOS16()) {
			LGameGLNew view = new LGameGLNew(activity);
			if (configChooser != null) {
				view.setEGLConfigChooser(configChooser);
			}
			this.surfaceView = view;
		} else {
			LGameGLOld view = new LGameGLOld(activity);
			if (configChooser != null) {
				view.setEGLConfigChooser(configChooser);
			}
			this.surfaceView = view;
		}
		if (surfaceView instanceof LGameGLOld) {
			((LGameGLOld) surfaceView).setRenderer(this);
		} else if (surfaceView instanceof LGameGLNew) {
			((LGameGLNew) surfaceView).setRenderer(this);
		} else {
			throw new RuntimeException("Create Android2DView Error!");
		}
		surfaceView.setClickable(false);
		surfaceView.setFocusable(true);
		surfaceView.setFocusableInTouchMode(true);
		surfaceView.setKeepScreenOn(true);
		surfaceView.setLongClickable(false);
		surfaceView.destroyDrawingCache();
		surfaceView.setDrawingCacheBackgroundColor(0);
		surfaceView.setDrawingCacheEnabled(false);
		if (LSystem.isHTC()) {
			surfaceView.setWillNotCacheDrawing(false);
			surfaceView.setWillNotDraw(false);
		} else {
			surfaceView.setWillNotCacheDrawing(true);
			surfaceView.setWillNotDraw(true);
		}
		surfaceView.requestFocus();
		surfaceView.requestFocusFromTouch();
		LSystem.screenProcess = new LProcess(this, surfaceView, width, height);
		return surfaceView;
	}

	private android.opengl.GLSurfaceView.EGLConfigChooser getEglConfigChooser() {
		if (LSystem.isSamsung7500()) {
			return new android.opengl.GLSurfaceView.EGLConfigChooser() {
				public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
					int[] attributes = new int[] { EGL10.EGL_DEPTH_SIZE, 16,
							EGL10.EGL_NONE };
					EGLConfig[] configs = new EGLConfig[1];
					int[] result = new int[1];
					egl
							.eglChooseConfig(display, attributes, configs, 1,
									result);
					return configs[0];
				}
			};
		} else {
			return null;
		}
	}

	final void resume() {
		synchronized (synch) {
			LSystem.isRunning = true;
			LSystem.isResume = true;
			timer = LSystem.getSystemTimer();
			LTextures.reload();
		}
	}

	final void pause() {
		synchronized (synch) {
			if (!LSystem.isRunning) {
				return;
			}
			LSystem.isRunning = false;
			LSystem.isPaused = true;
			while (LSystem.isPaused) {
				try {
					synch.wait();
				} catch (InterruptedException ignored) {
				}
			}
		}
	}

	final void destroy() {
		synchronized (synch) {
			LSystem.isRunning = false;
			LSystem.isDestroy = true;
			if (LSystem.screenProcess != null) {
				LSystem.screenProcess.getAssetsSound().stopSoundAll();
				LSystem.screenProcess.getPlaySound().stopSoundAll();
				LSystem.screenProcess.onDestroy();
				ActionControl.getInstance().stopAll();
				LSystem.destroy();
				LSystem.gc();
			}
			while (LSystem.isDestroy) {
				try {
					synch.wait();
				} catch (InterruptedException ex) {
				}
			}
		}
	}

	public void onDrawFrame(javax.microedition.khronos.opengles.GL10 gl10) {

		this.onRunning = false;
		this.onPause = false;
		this.onDestroy = false;
		this.onResume = false;

		synchronized (synch) {
			onRunning = LSystem.isRunning;
			onPause = LSystem.isPaused;
			onDestroy = LSystem.isDestroy;
			onResume = LSystem.isResume;

			if (LSystem.isResume) {
				LSystem.isResume = false;
			}

			if (LSystem.isPaused) {
				LSystem.isPaused = false;
				synch.notifyAll();
			}

			if (LSystem.isDestroy) {
				LSystem.isDestroy = false;
				synch.notifyAll();
			}
		}

		if (onResume) {
			Log.i("Android2DView", "onResume");
			LSystem.gc(1000, 1);
			timer = LSystem.getSystemTimer();
			lastTimeMicros = timer.getTimeMicros();
			elapsedTime = 0;
			remainderMicros = 0;
			process.onResume();
		}

		if (onRunning) {

			if (LSystem.isLogo) {
				synchronized (synch) {
					if (logoFlag == null) {
						LSystem.isLogo = false;
						return;
					}
					logoFlag.draw(gl);
					if (logoFlag.finish) {
						gl.setAlpha(1.0f);
						gl.setBlendMode(GL.MODE_NORMAL);
						gl.drawClear();
						LSystem.isLogo = false;
						logoFlag = null;
						return;
					}
				}
				return;
			}

			if (!process.next()) {
				return;
			}

			process.calls();

			goalTimeMicros = lastTimeMicros + 1000000L / maxFrames;
			currTimeMicros = timer.sleepTimeMicros(goalTimeMicros);
			elapsedTimeMicros = currTimeMicros - lastTimeMicros
					+ remainderMicros;
			elapsedTime = Math.max(0, (int) (elapsedTimeMicros / 1000));
			remainderMicros = elapsedTimeMicros - elapsedTime * 1000;
			lastTimeMicros = currTimeMicros;
			timerContext.millisSleepTime = remainderMicros;
			timerContext.timeSinceLastUpdate = elapsedTime;

			process.runTimer(timerContext);

			if (LSystem.AUTO_REPAINT) {
				int repaintMode = process.getRepaintMode();
				switch (repaintMode) {
				case Screen.SCREEN_BITMAP_REPAINT:
					gl.drawTexture(process.getBackground(), 0, 0);
					break;
				case Screen.SCREEN_COLOR_REPAINT:
					GLColor c = process.getColor();
					if (c != null) {
						gl.drawClear(c);
					}
					break;
				case Screen.SCREEN_CANVAS_REPAINT:
					gl.reset(clear);
					break;
				case Screen.SCREEN_NOT_REPAINT:
					break;
				default:
					gl.drawTexture(process.getBackground(), repaintMode / 2
							- LSystem.random.nextInt(repaintMode), repaintMode
							/ 2 - LSystem.random.nextInt(repaintMode));
					break;
				}
				gl.resetFont();

				process.draw(gl);

				if (isFPS) {
					tickFrames();
					gl.setFont(fpsFont);
					gl.setColor(GLColor.white);
					gl.drawWestString("FPS:" + curFPS, 5, 25);
				}
				if (isMemory) {
					Runtime runtime = Runtime.getRuntime();
					long totalMemory = runtime.totalMemory();
					long currentMemory = totalMemory - runtime.freeMemory();
					String memory = ((float) ((currentMemory * 10) >> 20) / 10)
							+ " of "
							+ ((float) ((totalMemory * 10) >> 20) / 10) + " MB";
					gl.setFont(fpsFont);
					gl.setColor(GLColor.white);
					gl.drawWestString("MEMORY:" + memory, 5, 45);
				}

				process.drawEmulator(gl);

			}

		}

		if (onPause) {
			Log.i("Android2DView", "onPause");
			pause(500);
			process.onPause();
		}

		if (onDestroy) {
			if (process != null) {
				process.end();
			}
			process.onDestroy();
		}

	}

	private final void pause(long sleep) {
		try {
			Thread.sleep(sleep);
		} catch (InterruptedException ex) {
		}
	}

	private void tickFrames() {
		long time = System.currentTimeMillis();
		if (time - frameCount > 1000L) {
			curFPS = Math.min(maxFrames, frames);
			frames = 0;
			frameCount = time;
		}
		frames++;
	}

	public void onSurfaceChanged(GL10 gl10, int width, int height) {
		if (gl != null) {
			gl.setViewPort(0, 0, width, height);
			this.width = width;
			this.height = height;
			this.maxWidth = width;
			this.maxHeight = height;
			LSystem.scaleWidth = ((float) maxWidth) / LSystem.screenRect.width;
			LSystem.scaleHeight = ((float) maxHeight)
					/ LSystem.screenRect.height;
			if (LSystem.isCreated == false) {
				if (process != null) {
					process.begin();
				}
				LSystem.isCreated = true;
				synchronized (this) {
					LSystem.isRunning = true;
				}
			}
		} else if (gl == null || !gl.equals(gl10, width, height)) {
			Log.i("Android2DView", "onSurfaceChanged");
			this.gl = new GLEx(gl10, width, height);
			this.width = width;
			this.height = height;
			this.maxWidth = width;
			this.maxHeight = height;
			LSystem.scaleWidth = ((float) maxWidth) / LSystem.screenRect.width;
			LSystem.scaleHeight = ((float) maxHeight)
					/ LSystem.screenRect.height;
			gl.update();
			gl.setViewPort(0, 0, width, height);
			if (LSystem.isCreated == false) {
				if (process != null) {
					process.begin();
				}
				LSystem.isCreated = true;
				synchronized (this) {
					LSystem.isRunning = true;
				}
			}
		}
	}

	public void onSurfaceCreated(GL10 gl10, EGLConfig config) {
		if (gl == null || !gl.equals(gl10, width, height)) {
			Log.i("Android2DView", "onSurfaceCreated");
			this.gl = new GLEx(gl10, LSystem.screenRect.width,
					LSystem.screenRect.height);
			this.supportVBO = GLEx.checkVBO();
			if (glMode == GLMode.VBO) {
				if (supportVBO) {
					GLEx.setVbo(true);
				} else {
					GLEx.setVbo(false);
					setGLMode(GLMode.Default);
				}
			} else {
				GLEx.setVbo(false);
				setGLMode(GLMode.Default);
			}
			gl.update();
			gl.setViewPort(0, 0, LSystem.screenRect.width,
					LSystem.screenRect.height);
			if (process != null) {
				process.begin();
			}
		}
	}

	public void setFullScreen(boolean fullScreen) {
		if (fullScreen) {
			window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			window
					.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
			window.requestFeature(android.view.Window.FEATURE_NO_TITLE);
		} else {
			window.setFlags(
					WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		}
		try {
			window.setBackgroundDrawable(null);
		} catch (Exception e) {
		}
	}

	public void setGLMode(GLMode mode) {
		this.glMode = mode;
	}

	public boolean isSupportVBO() {
		return supportVBO;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public LTexture getLogo() {
		return logoFlag.logo;
	}

	public void setLogo(LTexture img) {
		if (logoFlag == null) {
			this.logoFlag = new LGameLogo(new LTexture(img));
		}
	}

	public void setLogo(String path) {
		setLogo(LTextures.loadTexture(path, Format.BILINEAR).get());
	}

	public void setShowLogo(boolean showLogo) {
		LSystem.isLogo = showLogo;
		if (logoFlag == null) {
			setLogo(LSystem.FRAMEWORK_IMG_NAME + "logo.png");
		}
	}

	public void setShowFPS(boolean showFps) {
		this.isFPS = showFps;
	}

	public void setShowMemory(boolean showMemory) {
		this.isMemory = showMemory;
	}

	public void setClearFrame(boolean clearFrame) {
		this.clear = clearFrame;
	}

	public void setFPS(long frames) {
		this.maxFrames = frames;
	}

	public long getMaxFPS() {
		return this.maxFrames;
	}

	public long getCurrentFPS() {
		return this.curFPS;
	}

	public float getScalex() {
		return LSystem.scaleWidth;
	}

	public float getScaley() {
		return LSystem.scaleHeight;
	}

	public int getScreenWidth() {
		return getScreenDimension().width;
	}

	public int getScreenHeight() {
		return getScreenDimension().height;
	}

	public boolean isRunning() {
		return LSystem.isRunning;
	}

	public void setRunning(boolean isRunning) {
		LSystem.isRunning = isRunning;
	}

	public boolean isPause() {
		return LSystem.isPaused;
	}

	public void setPause(boolean isPause) {
		LSystem.isPaused = isPause;
	}

	public View getView() {
		return surfaceView;
	}

}
