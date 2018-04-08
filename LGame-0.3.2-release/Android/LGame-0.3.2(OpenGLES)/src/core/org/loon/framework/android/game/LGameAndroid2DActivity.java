package org.loon.framework.android.game;

import java.util.LinkedList;
import java.util.List;

import org.loon.framework.android.game.core.EmulatorListener;
import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.core.graphics.Screen;
import org.loon.framework.android.game.core.graphics.opengl.LTexture;
import org.loon.framework.android.game.core.input.LInput.ClickListener;
import org.loon.framework.android.game.core.input.LInput.SelectListener;
import org.loon.framework.android.game.core.input.LInput.TextListener;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

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
 * @email ceponline@yahoo.com.cn
 * @version 0.1.3
 */
public abstract class LGameAndroid2DActivity extends Activity implements
		android.hardware.SensorEventListener {

	public static enum LMode {

		Defalut, Max, Fill, FitFill, Ratio, MaxRatio

	}

	public static enum Location {

		LEFT, RIGHT, TOP, BOTTOM, BOTTOM_LEFT, BOTTOM_RIGHT, CENTER, ALIGN_BASELINE, ALIGN_LEFT, ALIGN_TOP, ALIGN_RIGHT, ALIGN_BOTTOM, ALIGN_PARENT_LEFT, ALIGN_PARENT_TOP, ALIGN_PARENT_RIGHT, ALIGN_PARENT_BOTTOM, CENTER_IN_PARENT, CENTER_HORIZONTAL, CENTER_VERTICAL;

	}

	private android.hardware.SensorManager sensorManager;

	private android.hardware.Sensor sensorAccelerometer;

	private boolean setupSensors, keyboardOpen, isLandscape, isDestroy;

	private int orientation;

	private boolean firstResume;

	private LGameAndroid2DView gameView;

	private FrameLayout frameLayout;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		LSystem.gc();
		Log.i("Android2DActivity", "LGame 2D Engine Start");
		LSystem.screenActivity = this;
		this.frameLayout = new FrameLayout(LGameAndroid2DActivity.this);
		this.isDestroy = true;
		this.onMain();
	}

	public void initialization(final boolean landscape) {
		initialization(landscape, LMode.Ratio);
	}

	public void initialization(final boolean landscape, final LMode mode) {
		initialization(landscape, true, mode);
	}

	/**
	 * 以指定倾斜方式显示游戏画面
	 * 
	 * @param width
	 * @param height
	 * @param landscape
	 */
	public void initialization(final int width, final int height,
			final boolean landscape) {
		initialization(width, height, landscape, LMode.Ratio);
	}

	/**
	 * 以指定倾斜方式显示游戏画面
	 * 
	 * @param width
	 * @param height
	 * @param landscape
	 * @param mode
	 */
	public void initialization(final int width, final int height,
			final boolean landscape, final LMode mode) {
		maxScreen(width, height);
		initialization(landscape, mode);
	}

	public void initialization(final boolean landscape,
			final boolean fullScreen, final LMode mode) {
		if (landscape == false) {
			if (LSystem.MAX_SCREEN_HEIGHT > LSystem.MAX_SCREEN_WIDTH) {
				int tmp_height = LSystem.MAX_SCREEN_HEIGHT;
				LSystem.MAX_SCREEN_HEIGHT = LSystem.MAX_SCREEN_WIDTH;
				LSystem.MAX_SCREEN_WIDTH = tmp_height;
			}
		}
		this.gameView = new LGameAndroid2DView(LGameAndroid2DActivity.this,
				mode, fullScreen, landscape);

		if (mode == LMode.Defalut) {
			// 添加游戏View，显示为指定大小，并居中
			this.addView(gameView.getView(), gameView.getWidth(), gameView
					.getHeight(), Location.CENTER);
		} else if (mode == LMode.Ratio) {
			// 添加游戏View，显示为屏幕许可范围，并居中
			this.addView(gameView.getView(), gameView.getMaxWidth(), gameView
					.getMaxHeight(), Location.CENTER);
		} else if (mode == LMode.MaxRatio) {
			// 添加游戏View，显示为屏幕许可的最大范围(可能比单纯的Ratio失真)，并居中
			this.addView(gameView.getView(), gameView.getMaxWidth(), gameView
					.getMaxHeight(), Location.CENTER);
		} else if (mode == LMode.Max) {
			// 添加游戏View，显示为最大范围值，并居中
			this.addView(gameView.getView(), gameView.getMaxWidth(), gameView
					.getMaxHeight(), Location.CENTER);
		} else if (mode == LMode.Fill) {
			// 添加游戏View，显示为全屏，并居中
			this.addView(gameView.getView(),
					android.view.ViewGroup.LayoutParams.FILL_PARENT,
					android.view.ViewGroup.LayoutParams.FILL_PARENT,
					Location.CENTER);
		} else if (mode == LMode.FitFill) {
			// 添加游戏View，显示为按比例缩放情况下的最大值，并居中
			this.addView(gameView.getView(), gameView.getMaxWidth(), gameView
					.getMaxHeight(), Location.CENTER);
		}

		if (setupSensors) {
			this.initSensors();
		}

	}

	public void setupGravity() {
		this.setupSensors = true;
	}

	private void initSensors() {
		try {
			android.hardware.SensorManager sensorService = (android.hardware.SensorManager) getSystemService(Context.SENSOR_SERVICE);
			this.sensorManager = sensorService;
			if (sensorService == null) {
				return;
			}

			List<android.hardware.Sensor> sensors = sensorManager
					.getSensorList(android.hardware.Sensor.TYPE_ACCELEROMETER);
			if (sensors.size() > 0) {
				sensorAccelerometer = sensors.get(0);
			}

			boolean accelSupported = sensorManager.registerListener(this,
					sensorAccelerometer,
					android.hardware.SensorManager.SENSOR_DELAY_GAME);

			if (!accelSupported) {
				sensorManager.unregisterListener(this, sensorAccelerometer);
			}

		} catch (Exception ex) {
		}
	}

	private void stopSensors() {
		try {
			if (sensorManager != null) {
				this.sensorManager.unregisterListener(this);
				this.sensorManager = null;
			}
		} catch (Exception ex) {

		}
	}

	public abstract void onMain();

	public void showAndroidTextInput(final TextListener listener,
			final String title, final String message) {
		if (listener == null) {
			return;
		}
		android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(
				LGameAndroid2DActivity.this);
		builder.setTitle(title);
		final android.widget.EditText input = new android.widget.EditText(
				LGameAndroid2DActivity.this);
		input.setText(message);
		input.setSingleLine();
		builder.setView(input);
		builder.setPositiveButton("Ok",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(android.content.DialogInterface dialog,
							int whichButton) {
						listener.input(input.getText().toString());
					}
				});
		builder
				.setOnCancelListener(new android.content.DialogInterface.OnCancelListener() {
					public void onCancel(android.content.DialogInterface dialog) {
						listener.cancled();
					}
				});
		builder.show();
	}

	public void showAndroidAlert(final ClickListener listener,
			final String title, final String message) {
		if (listener == null) {
			return;
		}

		final android.app.AlertDialog alert = new android.app.AlertDialog.Builder(
				LGameAndroid2DActivity.this).create();
		alert.setTitle(title);
		alert.setMessage(message);
		alert.setButton("OK",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(android.content.DialogInterface dialog,
							int whichButton) {
						listener.clicked();
					}
				});
		alert.show();
	}

	public void showAndroidOpenHTML(final ClickListener listener,
			final String title, final String url) {
		final LGameWeb web = new LGameWeb(LGameAndroid2DActivity.this, url);
		android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(
				LGameAndroid2DActivity.this);
		builder.setCancelable(true);
		builder.setTitle(title);
		builder.setView(web);
		builder.setPositiveButton("Ok",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(android.content.DialogInterface dialog,
							int whichButton) {
						listener.clicked();
					}
				}).setNegativeButton("Cancel",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(android.content.DialogInterface dialog,
							int whichButton) {
						listener.cancled();
					}
				});
		builder.show();
	}

	public void showAndroidSelect(final SelectListener listener,
			final String title, final String text[]) {
		if (listener == null) {
			return;
		}

		final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(
				LGameAndroid2DActivity.this);
		builder.setTitle(title);
		builder.setItems(text,
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(android.content.DialogInterface dialog,
							int item) {
						listener.item(item);
					}
				});
		builder
				.setOnCancelListener(new android.content.DialogInterface.OnCancelListener() {
					public void onCancel(android.content.DialogInterface dialog) {
						listener.cancled();
					}
				});
		android.app.AlertDialog alert = builder.create();
		alert.show();

	}

	public View inflate(final int layoutID) {
		final android.view.LayoutInflater inflater = android.view.LayoutInflater
				.from(this);
		return inflater.inflate(layoutID, null);
	}

	public void addView(final View view, Location location) {
		if (view == null) {
			return;
		}
		addView(view, android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT, location);
	}

	public void addView(final View view, int w, int h, Location location) {
		if (view == null) {
			return;
		}
		android.widget.RelativeLayout viewLayout = new android.widget.RelativeLayout(
				LGameAndroid2DActivity.this);
		android.widget.RelativeLayout.LayoutParams relativeParams = LSystem
				.createRelativeLayout(location, w, h);
		viewLayout.addView(view, relativeParams);
		addView(viewLayout);
	}

	public void addView(final View view) {
		if (view == null) {
			return;
		}
		frameLayout.addView(view, createLayoutParams());
	}

	public void removeView(final View view) {
		if (view == null) {
			return;
		}
		view.setVisibility(View.GONE);
		frameLayout.removeView(view);
	}

	public int setAD(String ad) {
		int result = 0;
		try {
			Class<LGameAndroid2DActivity> clazz = LGameAndroid2DActivity.class;
			java.lang.reflect.Field[] field = clazz.getDeclaredFields();
			if (field != null) {
				result = field.length;
			}
		} catch (Exception e) {
		}
		return result + ad.length();
	}

	public void maxScreen(int w, int h) {
		LSystem.MAX_SCREEN_WIDTH = w;
		LSystem.MAX_SCREEN_HEIGHT = h;
	}

	public void showScreen() {
		setContentView(frameLayout);
		try {
			getWindow().setBackgroundDrawable(null);
		} catch (Exception e) {

		}
	}

	public FrameLayout getFrameLayout() {
		return frameLayout;
	}

	public android.content.pm.PackageInfo getPackageInfo() {
		try {
			String packName = getPackageName();
			return getPackageManager().getPackageInfo(packName, 0);
		} catch (Exception ex) {

		}
		return null;
	}

	public String getVersionName() {
		android.content.pm.PackageInfo info = getPackageInfo();
		if (info != null) {
			return info.versionName;
		}
		return null;
	}

	public int getVersionCode() {
		android.content.pm.PackageInfo info = getPackageInfo();
		if (info != null) {
			return info.versionCode;
		}
		return -1;
	}

	public void onConfigurationChanged(android.content.res.Configuration config) {
		super.onConfigurationChanged(config);
		orientation = config.orientation;
		keyboardOpen = config.keyboardHidden == android.content.res.Configuration.KEYBOARDHIDDEN_NO;
		isLandscape = config.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE;
	}

	protected FrameLayout.LayoutParams createLayoutParams() {
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.FILL_PARENT);
		layoutParams.gravity = Gravity.CENTER;
		return layoutParams;
	}

	/**
	 * 设定常规图像加载方法的扩大值
	 * 
	 * @param sampleSize
	 */
	public void setSizeImage(int sampleSize) {
		LSystem.setPoorImage(sampleSize);
	}

	/**
	 * 取出第一个Screen并执行
	 * 
	 */
	public void runFirstScreen() {
		if (LSystem.screenProcess != null) {
			LSystem.screenProcess.runFirstScreen();
		}
	}

	/**
	 * 取出最后一个Screen并执行
	 */
	public void runLastScreen() {
		if (LSystem.screenProcess != null) {
			LSystem.screenProcess.runLastScreen();
		}
	}

	/**
	 * 运行指定位置的Screen
	 * 
	 * @param index
	 */
	public void runIndexScreen(int index) {
		if (LSystem.screenProcess != null) {
			LSystem.screenProcess.runIndexScreen(index);
		}
	}

	/**
	 * 运行自当前Screen起的上一个Screen
	 */
	public void runPreviousScreen() {
		if (LSystem.screenProcess != null) {
			LSystem.screenProcess.runPreviousScreen();
		}
	}

	/**
	 * 运行自当前Screen起的下一个Screen
	 */
	public void runNextScreen() {
		if (LSystem.screenProcess != null) {
			LSystem.screenProcess.runNextScreen();
		}
	}

	/**
	 * 向缓存中添加Screen数据，但是不立即执行
	 * 
	 * @param screen
	 */
	public void addScreen(Screen screen) {
		if (LSystem.screenProcess != null) {
			LSystem.screenProcess.addScreen(screen);
		}
	}

	/**
	 * 切换当前窗体为指定Screen
	 * 
	 * @param screen
	 */
	public void setScreen(Screen screen) {
		if (LSystem.screenProcess != null) {
			LSystem.screenProcess.setScreen(screen);
		}
	}

	/**
	 * 获得保存的Screen列表
	 * 
	 * @return
	 */
	public LinkedList<Screen> getScreens() {
		if (LSystem.screenProcess != null) {
			return LSystem.screenProcess.getScreens();
		}
		return null;
	}

	/**
	 * 获得缓存的Screen总数
	 */
	public int getScreenCount() {
		if (LSystem.screenProcess != null) {
			return LSystem.screenProcess.getScreenCount();
		}
		return 0;
	}

	public void setEmulatorListener(EmulatorListener emulator) {
		if (LSystem.screenProcess != null) {
			LSystem.screenProcess.setEmulatorListener(emulator);
		}
	}

	public void setShowFPS(boolean flag) {
		if (gameView != null) {
			this.gameView.setShowFPS(flag);
		}
	}

	public void setShowMemory(boolean flag) {
		if (gameView != null) {
			this.gameView.setShowMemory(flag);
		}
	}

	public void setFPS(long frames) {
		if (gameView != null) {
			this.gameView.setFPS(frames);
		}
	}

	public void setShowLogo(boolean showLogo) {
		if (gameView != null) {
			gameView.setShowLogo(showLogo);
		}
	}

	public void setLogo(LTexture img) {
		if (gameView != null) {
			gameView.setLogo(img);
		}
	}

	public LGameAndroid2DView gameView() {
		return gameView;
	}

	/**
	 * 键盘是否已显示
	 * 
	 * @return
	 */
	public boolean isKeyboardOpen() {
		return keyboardOpen;
	}

	/**
	 * 是否使用了横屏
	 * 
	 * @return
	 */
	public boolean isLandscape() {
		return isLandscape;
	}

	/**
	 * 当前窗体方向
	 * 
	 * @return
	 */
	public int getOrientation() {
		return orientation;
	}

	/**
	 * 退出当前应用
	 */
	public void close() {
		finish();
	}

	public boolean isDestroy() {
		return isDestroy;
	}

	/**
	 * 设定是否在Activity注销时强制关闭整个程序
	 * 
	 * @param isDestroy
	 */
	public void setDestroy(boolean isDestroy) {
		this.isDestroy = isDestroy;
		if (isDestroy == false) {
			LSystem.isBackLocked = true;
		}
	}

	public boolean isBackLocked() {
		return LSystem.isBackLocked;
	}

	/**
	 * 设定锁死BACK事件不处理
	 * 
	 * @param isBackLocked
	 */
	public void setBackLocked(boolean isBackLocked) {
		LSystem.isBackLocked = isBackLocked;
	}

	protected void onStop() {
		try {
			if (gameView != null) {
				gameView.setPause(true);
			}
			super.onStop();
		} catch (Exception e) {

		}
	}

	protected void onPause() {
		if (gameView == null) {
			return;
		}
		gameView.pause();
		if (isFinishing()) {
			gameView.destroy();
		}

		if (gameView != null && gameView.getView() != null) {
			if (gameView.getView() instanceof LGameGLOld) {
				((LGameGLOld) gameView.getView()).onPause();
			}
			if (gameView.getView() instanceof LGameGLNew) {
				((LGameGLNew) gameView.getView()).onPause();
			}
		}
		super.onPause();
		if (setupSensors) {
			// 停止重力感应
			stopSensors();
		}
		onGamePaused();
	}

	protected void onResume() {
		if (gameView == null) {
			return;
		}
		if (gameView != null && gameView.getView() != null) {
			if (gameView.getView() instanceof LGameGLOld) {
				((LGameGLOld) gameView.getView()).onResume();
			}
			if (gameView.getView() instanceof LGameGLNew) {
				((LGameGLNew) gameView.getView()).onResume();
			}
		}
		if (!firstResume) {
			gameView.resume();
		} else {
			firstResume = false;
		}
		super.onResume();
		if (setupSensors) {
			// 恢复重力感应
			initSensors();
		}
		onGameResumed();
	}

	public abstract void onGameResumed();

	public abstract void onGamePaused();

	protected void onDestroy() {
		try {
			if (gameView != null) {
				gameView.setRunning(false);
				Thread.sleep(16);
			}
			super.onDestroy();
			// 当此项为True时，强制关闭整个程序
			if (isDestroy) {
				Log.i("Android2DActivity", "LGame 2D Engine Shutdown");
				android.os.Process.killProcess(android.os.Process.myPid());
			}
		} catch (Exception e) {

		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		if (LSystem.screenProcess != null) {
			if (LSystem.screenProcess.onCreateOptionsMenu(menu)) {
				return true;
			}
		}
		return result;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		boolean result = super.onOptionsItemSelected(item);
		if (LSystem.screenProcess != null) {
			if (LSystem.screenProcess.onOptionsItemSelected(item)) {
				return true;
			}
		}
		return result;
	}

	public void onOptionsMenuClosed(Menu menu) {
		super.onOptionsMenuClosed(menu);
		if (LSystem.screenProcess != null) {
			LSystem.screenProcess.onOptionsMenuClosed(menu);
		}
	}

	public void onAccuracyChanged(android.hardware.Sensor sensor, int accuracy) {
		if (LSystem.screenProcess != null) {
			LSystem.screenProcess.onAccuracyChanged(sensor, accuracy);
		}
	}

	public void onSensorChanged(android.hardware.SensorEvent event) {
		if (LSystem.screenProcess != null) {
			LSystem.screenProcess.onSensorChanged(event);
		}
	}

}
