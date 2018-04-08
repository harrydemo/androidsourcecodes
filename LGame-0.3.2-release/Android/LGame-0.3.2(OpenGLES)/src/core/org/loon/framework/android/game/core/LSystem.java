package org.loon.framework.android.game.core;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;

import org.loon.framework.android.game.LGameAndroid2DActivity;
import org.loon.framework.android.game.LGameAndroid2DActivity.Location;
import org.loon.framework.android.game.core.geom.RectBox;
import org.loon.framework.android.game.core.graphics.Screen;
import org.loon.framework.android.game.core.input.LProcess;
import org.loon.framework.android.game.core.resource.Resources;
import org.loon.framework.android.game.core.resource.ZIPResource;
import org.loon.framework.android.game.core.timer.SystemTimer;
import org.loon.framework.android.game.utils.GraphicsUtils;
import org.loon.framework.android.game.utils.StringUtils;

import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

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
 * @version 0.1.1
 */
public final class LSystem {

	/***
	 * 获得指定名称资源的数据流
	 * 
	 * @param resName
	 * @return
	 */
	public final static InputStream getResourceAsStream(String resName) {
		try {
			return Resources.getResourceAsStream(resName);
		} catch (Exception e) {
			try {
				return Resources.class.getClassLoader().getResourceAsStream(
						resName);
			} catch (Exception ex) {
				try {
					return getActivity().getAssets().open(resName);
				} catch (IOException e1) {
					try {
						return getActivity().openFileInput(resName);
					} catch (IOException e2) {
						try {
							return new FileInputStream(new File(resName));
						} catch (FileNotFoundException e3) {
							return null;
						}
					}
				}
			}
		}
	}

	/**
	 * 执行一个位于Screen线程中的Runnable
	 * 
	 * @param runnable
	 */
	public final static void callScreenRunnable(Runnable runnable) {
		LProcess process = LSystem.screenProcess;
		if (process != null) {
			Screen screen = process.getScreen();
			if (screen != null) {
				synchronized (screen) {
					screen.callEvent(runnable);
				}
			}
		}
	}

	// 框架名
	final static public String FRAMEWORK = "LGame";

	// 包内默认的图片路径
	final static public String FRAMEWORK_IMG_NAME = "assets/loon_";

	// 框架版本信息
	final static public String VERSION = "0.3.2";

	// 默认的最大窗体宽（横屏）
	public static int MAX_SCREEN_WIDTH = 480;

	// 默认的最大窗体高（横屏）
	public static int MAX_SCREEN_HEIGHT = 320;

	// 图像缩放值
	public static int IMAGE_SIZE = 0;

	// 秒
	final static public long SECOND = 1000;

	// 分
	final static public long MINUTE = SECOND * 60;

	// 小时
	final static public long HOUR = MINUTE * 60;

	// 天
	final static public long DAY = HOUR * 24;

	// 周
	final static public long WEEK = DAY * 7;

	// 理论上一年
	final static public long YEAR = DAY * 365;

	// 行分隔符
	final static public String LS = System.getProperty("line.separator", "\n");

	// 文件分割符
	final static public String FS = System.getProperty("file.separator", "\\");

	// 随机数
	final static public Random random = new Random();

	// 屏幕是否横屏
	public static boolean SCREEN_LANDSCAPE;

	// 最大缓存数量
	final static public int DEFAULT_MAX_CACHE_SIZE = 30;

	final static public String encoding = "UTF-8";

	final static public String FONT_NAME = "Monospaced";

	final static public int DEFAULT_MAX_FPS = 60;

	public static RectBox screenRect;

	public static LGameAndroid2DActivity screenActivity;

	public static LProcess screenProcess;

	public static boolean isStringTexture, isBackLocked = true;

	public static float scaleWidth = 1, scaleHeight = 1;

	public static boolean isCreated, isLogo, isRunning, isResume, isDestroy,
			isPaused, AUTO_REPAINT;

	private static String temp_file;

	private static int TMP_MAJOR = 0;

	private static boolean LOWER_VER, EMULATOR;

	private static Handler OS_HANDLER;

	final private static String BULID_BRAND, BULID_MODEL, BULIDM_PRODUCT,
			BULIDM_RELEASE, BULIDM_SDK, BULIDM_DEVICE;

	final private static int OS_11 = 0, OS_15 = 1, OS_16 = 2, OS_20 = 3,
			OS_21 = 4, OS_22 = 5, OS_23 = 6, OS_30 = 7, OS_31 = 8, OS_35 = 9,
			OS_40 = 10, OS_45 = 11, OS_50 = 12, OS_60 = 13;

	private static HashMap<String, Object> settings = new HashMap<String, Object>(
			5);

	/**
	 * 设定一组键值对到缓存当中
	 * 
	 * @param key
	 * @param value
	 */
	public static void set(String key, Object value) {
		if (key == null || "".equals(key)) {
			return;
		}
		settings.put(key, value);
	}

	/**
	 * 获得指定键所对应的数值
	 * 
	 * @param key
	 * @return
	 */
	public static Object get(String key) {
		if (key == null || "".equals(key)) {
			return null;
		}
		return settings.get(key);
	}

	static {
		BULID_BRAND = Build.BRAND.toLowerCase();
		BULID_MODEL = Build.MODEL.toLowerCase();
		BULIDM_PRODUCT = Build.PRODUCT.toLowerCase();
		BULIDM_RELEASE = Build.VERSION.RELEASE;
		BULIDM_SDK = Build.VERSION.SDK;
		BULIDM_DEVICE = Build.DEVICE;
		if (BULIDM_RELEASE.indexOf("1.1") != -1) {
			TMP_MAJOR = OS_11;
		} else if (BULIDM_RELEASE.indexOf("1.5") != -1) {
			TMP_MAJOR = OS_15;
		} else if (BULIDM_RELEASE.indexOf("1.6") != -1) {
			TMP_MAJOR = OS_16;
		} else if (BULIDM_RELEASE.indexOf("2.0") != -1) {
			TMP_MAJOR = OS_20;
		} else if (BULIDM_RELEASE.indexOf("2.1") != -1) {
			TMP_MAJOR = OS_21;
		} else if (BULIDM_RELEASE.indexOf("2.2") != -1) {
			TMP_MAJOR = OS_22;
		} else if (BULIDM_RELEASE.indexOf("2.3") != -1) {
			TMP_MAJOR = OS_23;
		} else if (BULIDM_RELEASE.indexOf("3.0") != -1) {
			TMP_MAJOR = OS_30;
		} else if (BULIDM_RELEASE.indexOf("3.1") != -1) {
			TMP_MAJOR = OS_31;
		} else if (BULIDM_RELEASE.indexOf("3.5") != -1) {
			TMP_MAJOR = OS_35;
		} else if (BULIDM_RELEASE.indexOf("4.0") != -1) {
			TMP_MAJOR = OS_40;
		} else if (BULIDM_RELEASE.indexOf("4.5") != -1) {
			TMP_MAJOR = OS_45;
		} else if (BULIDM_RELEASE.indexOf("5.0") != -1) {
			TMP_MAJOR = OS_50;
		} else if (BULIDM_RELEASE.indexOf("6.0") != -1) {
			TMP_MAJOR = OS_60;
		} else {
			TMP_MAJOR = OS_15;
		}
		EMULATOR = BULID_BRAND.indexOf("generic") != -1
				&& BULID_MODEL.indexOf("sdk") != -1;
		LOWER_VER = (TMP_MAJOR < OS_16 && !EMULATOR);

	}

	/**
	 * 设定常规图像加载方法（非全部）的默认劣化值
	 * 
	 * @param sampleSize
	 */
	public static void setPoorImage(int sampleSize) {
		if (sampleSize > 0) {
			LSystem.IMAGE_SIZE = sampleSize;
		} else {
			LSystem.IMAGE_SIZE = 0;
		}
	}

	/**
	 * 判定是否使用了“奇异”的三星机型
	 * 
	 * @return
	 */
	public static boolean isSamsung7500() {
		return BULIDM_DEVICE.equalsIgnoreCase("GT-I7500");
	}

	/**
	 * 设定LayoutParams为全屏模式
	 * 
	 * @return
	 */
	public static LayoutParams createFillLayoutParams() {
		return new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
	}

	/**
	 * 生成一个对应指定位置的RelativeLayout
	 * 
	 * @param location
	 * @return
	 */
	public static RelativeLayout.LayoutParams createRelativeLayout(
			Location location) {
		return createRelativeLayout(location, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
	}

	/**
	 * 生成一个对应指定位置的RelativeLayout
	 * 
	 * @param location
	 * @return
	 */
	public static RelativeLayout.LayoutParams createRelativeLayout(
			Location location, int w, int h) {
		RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
				w, h);
		if (location == Location.LEFT) {
			relativeParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
					RelativeLayout.TRUE);
			relativeParams.addRule(RelativeLayout.ALIGN_PARENT_TOP,
					RelativeLayout.TRUE);
		} else if (location == Location.RIGHT) {
			relativeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,
					RelativeLayout.TRUE);
			relativeParams.addRule(RelativeLayout.ALIGN_PARENT_TOP,
					RelativeLayout.TRUE);
		} else if (location == Location.TOP) {
			relativeParams.addRule(RelativeLayout.ALIGN_PARENT_TOP,
					RelativeLayout.TRUE);
		} else if (location == Location.BOTTOM) {
			relativeParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
					RelativeLayout.TRUE);
		} else if (location == Location.BOTTOM_LEFT) {
			relativeParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
					RelativeLayout.TRUE);
			relativeParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
					RelativeLayout.TRUE);
		} else if (location == Location.BOTTOM_RIGHT) {
			relativeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,
					RelativeLayout.TRUE);
			relativeParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
					RelativeLayout.TRUE);
		} else if (location == Location.CENTER) {
			relativeParams.addRule(RelativeLayout.CENTER_VERTICAL,
					RelativeLayout.TRUE);
			relativeParams.addRule(RelativeLayout.CENTER_IN_PARENT,
					RelativeLayout.TRUE);
		} else if (location == Location.ALIGN_BASELINE) {
			relativeParams.addRule(RelativeLayout.ALIGN_BASELINE,
					RelativeLayout.TRUE);
		} else if (location == Location.ALIGN_LEFT) {
			relativeParams.addRule(RelativeLayout.ALIGN_LEFT,
					RelativeLayout.TRUE);
		} else if (location == Location.ALIGN_TOP) {
			relativeParams.addRule(RelativeLayout.ALIGN_TOP,
					RelativeLayout.TRUE);
		} else if (location == Location.ALIGN_RIGHT) {
			relativeParams.addRule(RelativeLayout.ALIGN_RIGHT,
					RelativeLayout.TRUE);
		} else if (location == Location.ALIGN_BOTTOM) {
			relativeParams.addRule(RelativeLayout.ALIGN_BOTTOM,
					RelativeLayout.TRUE);
		} else if (location == Location.ALIGN_PARENT_LEFT) {
			relativeParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
					RelativeLayout.TRUE);
		} else if (location == Location.ALIGN_PARENT_TOP) {
			relativeParams.addRule(RelativeLayout.ALIGN_PARENT_TOP,
					RelativeLayout.TRUE);
		} else if (location == Location.ALIGN_PARENT_RIGHT) {
			relativeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,
					RelativeLayout.TRUE);
		} else if (location == Location.ALIGN_PARENT_BOTTOM) {
			relativeParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
					RelativeLayout.TRUE);
		} else if (location == Location.CENTER_IN_PARENT) {
			relativeParams.addRule(RelativeLayout.CENTER_IN_PARENT,
					RelativeLayout.TRUE);
		} else if (location == Location.CENTER_HORIZONTAL) {
			relativeParams.addRule(RelativeLayout.CENTER_HORIZONTAL,
					RelativeLayout.TRUE);
		} else if (location == Location.CENTER_VERTICAL) {
			relativeParams.addRule(RelativeLayout.CENTER_VERTICAL,
					RelativeLayout.TRUE);
		}

		return relativeParams;
	}

	/**
	 * 跳转到指定的Activity
	 * 
	 * @param from
	 */
	public static void action(Activity from, Class<?> clazz) {

		screenActivity.setDestroy(false);

		android.content.Intent intent = new android.content.Intent(from, clazz);
		from.startActivity(intent);
	}

	/**
	 * 跳转到指定的Activity,并将其设定为最初的Activity
	 * 
	 * @param from
	 * @param clazz
	 */
	public static void go(Activity from, Class<?> clazz) {
		if (LSystem.getActivity() instanceof LGameAndroid2DActivity) {
			((LGameAndroid2DActivity) LSystem.getActivity()).setDestroy(false);
		}
		android.content.Intent intent = new android.content.Intent(from, clazz);
		intent.setFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP);
		from.startActivity(intent);
	}

	public static Handler getOSHandler() {
		if (OS_HANDLER == null) {
			OS_HANDLER = new Handler();
		}
		return OS_HANDLER;
	}

	public static void threadUi(final Runnable runnable) {
		Thread thread = new Thread() {
			public void run() {
				for (; screenProcess == null || isLogo;) {
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
					}
				}
				Screen screen = null;
				for (; (screen = screenProcess.getScreen()) == null;) {
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
					}
				}
				for (; !screen.isOnLoadComplete();) {
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
					}
				}
				try {
					LSystem.stopRepaint();
					LSystem.getActivity().runOnUiThread(runnable);
				} catch (Exception ex) {
					post(runnable);
				} finally {
					LSystem.startRepaint();
				}
			}
		};
		thread.setPriority(Thread.NORM_PRIORITY - 2);
		thread.start();
	}

	public static void post(final Runnable runnable) {
		getOSHandler().post(runnable);
	}

	public static void runOnUiThread(final Runnable runnable) {
		LSystem.getActivity().runOnUiThread(runnable);
	}

	public static void dispose(final Runnable runnable) {
		getOSHandler().removeCallbacks(runnable);
		getOSHandler().removeMessages(0);
	}

	public static void stopRepaint() {
		LSystem.AUTO_REPAINT = false;
		LSystem.isPaused = true;
	}

	public static void startRepaint() {
		LSystem.AUTO_REPAINT = true;
		LSystem.isPaused = false;
	}

	public static long getJavaHeap() {
		return Runtime.getRuntime().totalMemory()
				- Runtime.getRuntime().freeMemory();
	}

	/**
	 * 判定当前Android系统版本是否高于指定的版本
	 * 
	 * @param ver
	 * @return
	 */
	public static boolean isAndroidVersionHigher(final int ver) {
		return Integer.parseInt(BULIDM_SDK) >= ver;
	}

	public static boolean isLowerVer() {
		return LOWER_VER;
	}

	public static boolean isOverrunOS11() {
		return TMP_MAJOR > OS_11;
	}

	public static boolean isOverrunOS15() {
		return TMP_MAJOR > OS_15;
	}

	public static boolean isOverrunOS16() {
		return TMP_MAJOR > OS_16;
	}

	public static boolean isOverrunOS21() {
		return TMP_MAJOR > OS_21;
	}

	public static boolean isOverrunOS23() {
		return TMP_MAJOR > OS_23;
	}

	public static boolean isOverrunOS30() {
		return TMP_MAJOR > OS_30;
	}

	public static boolean isOverrunOS40() {
		return TMP_MAJOR > OS_40;
	}

	public static boolean isOverrunOS50() {
		return TMP_MAJOR > OS_50;
	}

	public static boolean isOverrunOS60() {
		return TMP_MAJOR > OS_60;
	}

	public static String getModel() {
		return BULID_MODEL;
	}

	public static String getProductName() {
		return BULIDM_PRODUCT;
	}

	public static int getMajorOSVersion() {
		return TMP_MAJOR;
	}

	public static String getOSVersion() {
		return BULIDM_RELEASE;
	}

	public static String getSDKVersion() {
		return BULIDM_SDK;
	}

	public static boolean isPaused() {
		return isPaused;
	}

	public static String getBRANDName() {
		return BULID_BRAND;
	}

	public static boolean isEmulator() {
		return EMULATOR;
	}

	public static boolean isHTC() {
		return BULID_BRAND.indexOf("htc") != -1;
	}

	public static boolean isSamsungGalaxy() {
		final boolean isSamsung = BULID_BRAND.indexOf("samsung") != -1;
		final boolean isGalaxy = BULID_MODEL.indexOf("galaxy") != -1;
		return isSamsung && isGalaxy;
	}

	public static boolean isDroidOrMilestone() {
		final boolean isMotorola = BULID_BRAND.indexOf("moto") != -1;
		final boolean isDroid = BULID_MODEL.indexOf("droid") != -1;
		final boolean isMilestone = BULID_MODEL.indexOf("milestone") != -1;
		return isMotorola && (isDroid || isMilestone);
	}

	/**
	 * 清空框架临时资源
	 */
	public static void destroy() {
		GraphicsUtils.destroy();
		ZIPResource.destroy();
		Resources.destroy();
		LSystem.gc();
	}

	/**
	 * 退出当前应用
	 * 
	 */
	public static void exit() {
		if (screenProcess == null) {
			return;
		}
		synchronized (screenProcess) {
			try {
				if (screenProcess != null) {
					screenProcess.setRunning(false);
					if (screenActivity != null) {
						if (screenActivity.isDestroy()) {
							screenActivity.finish();
						}
					}
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 设定刷新率
	 * 
	 * @param fps
	 */
	public static void setFPS(int fps) {
		if (screenProcess != null) {
			screenProcess.setFPS(fps);
		}
	}

	/**
	 * 返回当前刷新率
	 */
	public static int getFPS() {
		if (screenProcess != null) {
			return screenProcess.getFPS();
		}
		return 0;
	}

	/**
	 * 返回最大刷新率
	 */
	public static int getMaxFPS() {
		if (screenProcess != null) {
			return screenProcess.getMaxFPS();
		}
		return 0;
	}

	/**
	 * 返回当前的Activity
	 * 
	 * @return
	 */
	public static LGameAndroid2DActivity getActivity() {
		return screenActivity;
	}

	public static int getRandom(int i, int j) {
		if (j < i) {
			int tmp = j;
			i = tmp;
			j = i;
		}
		return i + random.nextInt((j - i) + 1);
	}

	/**
	 * 返回一个随机数
	 * 
	 * @param i
	 * @param j
	 * @return
	 */
	public static int getRandomBetWeen(int i, int j) {
		if (j < i) {
			int tmp = j;
			i = tmp;
			j = i;
		}
		return LSystem.random.nextInt(j + 1 - i) + i;
	}

	public static SystemTimer getSystemTimer() {
		return new SystemTimer();
	}

	public static String getVersionName() {
		if (screenActivity != null) {
			return screenActivity.getVersionName();
		}
		return null;
	}

	public static File getCacheFile() {
		if (screenActivity != null) {
			return screenActivity.getCacheDir();
		}
		return null;
	}

	public static File getCacheFile(String fileName) {
		final String file = LSystem.getCacheFileName();
		fileName = StringUtils.replaceIgnoreCase(fileName, "\\", "/");
		if (file != null) {
			if (fileName.startsWith("/") || fileName.startsWith("\\")) {
				fileName = fileName.substring(1, fileName.length());
			}
			if (file.endsWith("/") || file.endsWith("\\")) {
				return new File(LSystem.getCacheFileName() + fileName);
			} else {
				return new File(LSystem.getCacheFileName() + LSystem.FS
						+ fileName);
			}
		} else {
			return new File(fileName);
		}
	}

	public static String getCacheFileName() {
		if (temp_file == null) {
			if (screenActivity != null) {
				temp_file = screenActivity.getCacheDir().getAbsolutePath();
			}
		}
		return temp_file;
	}

	/**
	 * 强制进行系统资源回收
	 */
	public static void gcFinalization() {
		System.gc();
		System.runFinalization();
		System.gc();
	}

	/**
	 * 申请回收系统资源
	 * 
	 */
	final public static void gc() {
		System.gc();
	}

	/**
	 * 以指定范围内的指定概率执行gc
	 * 
	 * @param size
	 * @param rand
	 */
	final public static void gc(final int size, final long rand) {
		if (rand > size) {
			throw new RuntimeException(
					("GC random probability " + rand + " > " + size).intern());
		}
		if (LSystem.random.nextInt(size) <= rand) {
			LSystem.gc();
		}
	}

	/**
	 * 以指定概率使用gc回收系统资源
	 * 
	 * @param rand
	 */
	final public static void gc(final long rand) {
		gc(100, rand);
	}

	/**
	 * 读取指定文件到InputStream
	 * 
	 * @param buffer
	 * @return
	 */
	public static final InputStream read(byte[] buffer) {
		return new BufferedInputStream(new ByteArrayInputStream(buffer));
	}

	/**
	 * 读取指定文件到InputStream
	 * 
	 * @param file
	 * @return
	 */
	public static final InputStream read(File file) {
		try {
			return new BufferedInputStream(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	/**
	 * 读取指定文件到InputStream
	 * 
	 * @param fileName
	 * @return
	 */
	public static final InputStream read(String fileName) {
		return read(new File(fileName));
	}

	/**
	 * 加载Properties文件
	 * 
	 * @param file
	 */
	final public static void loadPropertiesFileToSystem(final File file) {
		Properties properties = System.getProperties();
		InputStream in = LSystem.read(file);
		loadProperties(properties, in, file.getName());
	}

	/**
	 * 加载Properties文件
	 * 
	 * @param file
	 * @return
	 */
	final public static Properties loadPropertiesFromFile(final File file) {
		if (file == null) {
			return null;
		}
		Properties properties = new Properties();
		try {
			InputStream in = LSystem.read(file);
			loadProperties(properties, in, file.getName());
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}

	/**
	 * 加载Properties文件
	 * 
	 * @param properties
	 * @param inputStream
	 * @param fileName
	 */
	private static void loadProperties(Properties properties,
			InputStream inputStream, String fileName) {
		try {
			properties.load(inputStream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				throw new RuntimeException(
						("error closing input stream from file " + fileName
								+ ", ignoring , " + e.getMessage()).intern());
			}
		}
	}

	/**
	 * 写入整型数据到OutputStream
	 * 
	 * @param out
	 * @param number
	 */
	public final static void writeInt(final OutputStream out, final int number) {
		byte[] bytes = new byte[4];
		try {
			for (int i = 0; i < 4; i++) {
				bytes[i] = (byte) ((number >> (i * 8)) & 0xff);
			}
			out.write(bytes);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 从InputStream中获得整型数据
	 * 
	 * @param in
	 * @return
	 */
	final static public int readInt(final InputStream in) {
		int data = -1;
		try {
			data = (in.read() & 0xff);
			data |= ((in.read() & 0xff) << 8);
			data |= ((in.read() & 0xff) << 16);
			data |= ((in.read() & 0xff) << 24);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		return data;
	}

	/**
	 * 合并hashCode和指定类型的数值生成新的Code值(以下同)
	 * 
	 * @param hashCode
	 * @param value
	 * @return
	 */
	public static int unite(int hashCode, boolean value) {
		int v = value ? 1231 : 1237;
		return unite(hashCode, v);
	}

	public static int unite(int hashCode, long value) {
		int v = (int) (value ^ (value >>> 32));
		return unite(hashCode, v);
	}

	public static int unite(int hashCode, float value) {
		int v = Float.floatToIntBits(value);
		return unite(hashCode, v);
	}

	public static int unite(int hashCode, double value) {
		long v = Double.doubleToLongBits(value);
		return unite(hashCode, v);
	}

	public static int unite(int hashCode, Object value) {
		return unite(hashCode, value.hashCode());
	}

	public static int unite(int hashCode, int value) {
		return 31 * hashCode + value;
	}
}
