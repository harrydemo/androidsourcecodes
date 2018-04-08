package org.loon.framework.javase.game.core;

import java.awt.AWTException;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;

import org.loon.framework.javase.game.core.geom.RectBox;
import org.loon.framework.javase.game.core.graphics.Screen;
import org.loon.framework.javase.game.core.input.LProcess;
import org.loon.framework.javase.game.core.resource.Resources;
import org.loon.framework.javase.game.core.resource.ZIPResource;
import org.loon.framework.javase.game.core.timer.SystemTimer;
import org.loon.framework.javase.game.utils.GraphicsUtils;

/**
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
 * @email��ceponline ceponline@yahoo.com.cn
 * @version 0.1.2
 */
public final class LSystem {

	final static private ClassLoader classLoader;

	/**
	 * 获得指定名称资源的数据流
	 * 
	 * @param resName
	 * @return
	 */
	public final static InputStream getResourceAsStream(String resName) {
		try {
			return classLoader.getResourceAsStream(resName);
		} catch (Exception e) {
			try {
				return Resources.class.getClassLoader().getResourceAsStream(
						resName);
			} catch (Exception ex) {
				try {
					return new FileInputStream(new File(resName));
				} catch (FileNotFoundException e1) {
					return null;
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

	// 随机数
	final static public Random random = new Random();

	// 默认编码格式
	final static public String encoding = "UTF-8";

	// 行分隔符
	final static public String LS = System.getProperty("line.separator", "\n");

	// 文件分割符
	final static public String FS = System.getProperty("file.separator", "\\");

	// 默认的窗体大小
	final static public int MAX_WIDTH = 480, MAX_HEIGHT = 320;

	final private static Runtime systemRuntime = Runtime.getRuntime();

	final static private boolean osIsLinux;

	final static private boolean osIsUnix;

	final static private boolean osIsMacOs;

	final static private boolean osIsWindows;

	final static private boolean osIsWindowsXP;

	final static private boolean osIsWindows2003;

	final static private boolean osBit64;

	final static public String OS_NAME;

	final static public int JAVA_13 = 0;

	final static public int JAVA_14 = 1;

	final static public int JAVA_15 = 2;

	final static public int JAVA_16 = 3;

	final static public int JAVA_17 = 4;

	final static public int JAVA_18 = 5;

	final static public int JAVA_19 = 6;

	final static private String TMP_DIR = "java.io.tmpdir";

	public static RectBox screenRect;

	public static LProcess screenProcess;

	public static float scaleWidth = 1, scaleHeight = 1;

	public static int FONT_TYPE = 15;

	public static int FONT_SIZE = 1;

	public static String FONT = "黑体";

	public static String LOG_FILE = "log.txt";

	public static Robot RO_BOT;

	public static boolean DEFAULT_ROTATE_CACHE = true;

	public static int DEFAULT_MAX_CACHE_SIZE = 30;

	public static int DEFAULT_MAX_FPS = 200;

	public static boolean AUTO_REPAINT = true;

	public static boolean isApplet, isPaused, isLogo;

	public static boolean isStringTexture = false;

	private static Dimension screenSize;

	private static String javaVersion;

	final static private float majorJavaVersion = getMajorJavaVersion(System
			.getProperty("java.specification.version"));

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

	/**
	 * 获得计时器
	 * 
	 * @return
	 */
	public static SystemTimer getSystemTimer() {
		return new SystemTimer();
	}

	private static int tmpmajor = 0;

	final static private float DEFAULT_JAVA_VERSION = 1.4F;

	// 尝试gc释放的循环次数
	final static private int GC_FREE_NUM = 10;

	// 每次gc请求数量
	final static private int GC_LOOP_CHECK = 20;

	static {
		classLoader = Thread.currentThread().getContextClassLoader();
		OS_NAME = System.getProperty("os.name").toLowerCase();
		osIsLinux = OS_NAME.indexOf("linux") != -1;
		osIsUnix = OS_NAME.indexOf("nix") != -1 || OS_NAME.indexOf("nux") != 1;
		osIsMacOs = OS_NAME.indexOf("mac") != -1;
		osIsWindows = OS_NAME.indexOf("windows") != -1;
		osIsWindowsXP = OS_NAME.startsWith("Windows")
				&& (OS_NAME.compareTo("5.1") >= 0);
		osIsWindows2003 = "windows 2003".equals(OS_NAME);
		osBit64 = System.getProperty("os.arch").equals("amd64");
		javaVersion = System.getProperty("java.version");
		if (javaVersion.indexOf("1.4.") != -1) {
			tmpmajor = JAVA_14;
		} else if (javaVersion.indexOf("1.5.") != -1) {
			tmpmajor = JAVA_15;
		} else if (javaVersion.indexOf("1.6.") != -1) {
			tmpmajor = JAVA_16;
		} else if (javaVersion.indexOf("1.7.") != -1) {
			tmpmajor = JAVA_17;
		} else if (javaVersion.indexOf("1.8.") != -1) {
			tmpmajor = JAVA_18;
		} else if (javaVersion.indexOf("1.9.") != -1) {
			tmpmajor = JAVA_19;
		} else {
			tmpmajor = JAVA_13;
		}
		try {
			RO_BOT = new Robot();
		} catch (AWTException e) {
		}
	}

	public static void exit() {
		System.exit(0);
	}

	public static void stopRepaint() {
		LSystem.AUTO_REPAINT = false;
		LSystem.isPaused = true;
	}

	public static void startRepaint() {
		LSystem.AUTO_REPAINT = true;
		LSystem.isPaused = false;
	}

	/**
	 * 清空系统缓存资源
	 * 
	 */
	public static void destroy() {
		ZIPResource.destroy();
		GraphicsUtils.destroyImages();
		Resources.destroy();
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
		try {
			long maxRestoreLoops = 299;
			long pauseTime = 10;
			long UsedMemoryNow = nowMemory();
			long UsedMemoryPrev = Long.MAX_VALUE;
			for (int i = 0; i < maxRestoreLoops; i++) {
				systemRuntime.runFinalization();
				systemRuntime.gc();
				try {
					Thread.sleep(pauseTime);
				} catch (InterruptedException e) {
				}
				if (UsedMemoryPrev > UsedMemoryNow) {
					UsedMemoryPrev = UsedMemoryNow;
					UsedMemoryNow = nowMemory();
				} else {
					break;
				}
			}
		} catch (Throwable e) {
			System.gc();
		}
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
	 * 创建指定类，指定长度的数组对象，如果创建时内存不足，则尝试gc释放后重新创建。
	 * 
	 * @param clazz
	 * @param size
	 * @return
	 */
	public static Object makeGCArray(Class clazz, int[] size) {
		for (int i = 0; i < GC_FREE_NUM; i++) {
			try {
				return Array.newInstance(clazz, size);
			} catch (OutOfMemoryError e) {
				systemRuntime.runFinalization();
				long free_memory = systemRuntime.freeMemory();
				long was_free;
				int count = 0;
				do {
					count++;
					was_free = free_memory;
					systemRuntime.gc();
					free_memory = systemRuntime.freeMemory();
				} while ((free_memory > was_free) && (count <= GC_LOOP_CHECK));
			}
		}
		return Array.newInstance(clazz, size);
	}

	/**
	 * 创建指定类，指定长度的数组对象，如果创建时内存不足，则尝试gc释放后重新创建。
	 * 
	 * @param clazz
	 * @param size
	 * @return
	 */
	public static Object makeGCArray(Class clazz, int size) {
		return makeGCArray(clazz, new int[] { size });
	}

	/**
	 * 创建一个会尝试在初始化时gc调用的byte[]
	 * 
	 * @param size
	 * @return
	 */
	public static byte[] makeGCByteArray(int size) {
		return (byte[]) makeGCArray(byte.class, size);
	}

	/**
	 * 创建一个会尝试在初始化时gc调用的int[]
	 * 
	 * @param size
	 * @return
	 */
	public static int[] makeGCIntArray(int size) {
		return (int[]) makeGCArray(int.class, size);
	}

	/**
	 * 创建一个会尝试在初始化时gc调用的boolean[]
	 * 
	 * @param size
	 * @return
	 */
	public static boolean[] makeGCBooleanArray(int size) {
		return (boolean[]) makeGCArray(boolean.class, size);
	}

	/**
	 * 获得当前系统临时目录所在路径
	 * 
	 * @return
	 */
	public static String getTempPath() {
		return System.getProperty(TMP_DIR);
	}

	/**
	 * 打开当前系统浏览器
	 * 
	 * @param url
	 * @return
	 */
	public static boolean openBrowser(String url) {
		try {
			if (LSystem.isWindows()) {
				File iexplore = new File(
						"C:\\Program Files\\Internet Explorer\\iexplore.exe");
				if (iexplore.exists()) {
					systemRuntime.exec(iexplore.getAbsolutePath() + " \"" + url
							+ "\"");
				} else {
					systemRuntime.exec("rundll32 url.dll,FileProtocolHandler "
							+ url);
				}
			} else if (LSystem.isMacOS()) {
				systemRuntime.exec("open " + url);
			} else if (LSystem.isUnix()) {
				String[] browsers = { "epiphany", "firefox", "mozilla",
						"konqueror", "netscape", "opera", "links", "lynx" };
				StringBuffer cmd = new StringBuffer();
				for (int i = 0; i < browsers.length; i++) {
					cmd.append((i == 0 ? "" : " || ") + browsers[i] + " \""
							+ url + "\" ");
				}
				systemRuntime.exec(new String[] { "sh", "-c", cmd.toString() });
			} else {
				return false;
			}
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	final private static long nowMemory() {
		return systemRuntime.totalMemory() - systemRuntime.freeMemory();
	}

	/**
	 * 居中指定容器
	 * 
	 * @param childWindow
	 */
	public static void centerOn(Container childWindow) {
		Dimension screen = LSystem.getScreenSize();
		Dimension window = childWindow.getSize();
		if (window.width == 0) {
			return;
		}
		int left = screen.width / 2 - window.width / 2;
		int top = screen.height / 2 - window.height / 2;
		childWindow.setLocation(left, top);
	}

	/**
	 * 取得当前屏幕大小
	 * 
	 * @return
	 */
	public static Dimension getScreenSize() {
		if (screenSize == null) {
			if (isWindows()) {
				screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				return screenSize;
			}
			if (GraphicsEnvironment.isHeadless()) {
				screenSize = new Dimension(0, 0);
			} else {
				GraphicsEnvironment ge = GraphicsEnvironment
						.getLocalGraphicsEnvironment();
				GraphicsDevice[] gd = ge.getScreenDevices();
				GraphicsConfiguration[] gc = gd[0].getConfigurations();
				Rectangle bounds = gc[0].getBounds();
				if (bounds.x == 0 && bounds.y == 0) {
					screenSize = new Dimension(bounds.width, bounds.height);
				} else {
					screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				}
			}
		}
		return screenSize;
	}

	public static boolean isOverrunJdk14() {
		return tmpmajor > JAVA_14;
	}

	public static boolean isOverrunJdk15() {
		return tmpmajor > JAVA_15;
	}

	public static boolean isOverrunJdk16() {
		return tmpmajor > JAVA_16;
	}

	public static boolean isOverrunJdk17() {
		return tmpmajor > JAVA_17;
	}

	public static boolean isOverrunJdk18() {
		return tmpmajor > JAVA_18;
	}

	public static boolean isOverrunJdk19() {
		return tmpmajor > JAVA_19;
	}

	public static boolean isLinux() {
		return osIsLinux;
	}

	public static boolean isMacOS() {
		return osIsMacOs;
	}

	public static boolean isUnix() {
		return osIsUnix;
	}

	public static boolean isWindows() {
		return osIsWindows;
	}

	public static boolean isWindowsXP() {
		return osIsWindowsXP;
	}

	public static boolean isWindows2003() {
		return osIsWindows2003;
	}

	public static boolean isBit64() {
		return osBit64;
	}

	public static boolean isUsingWindowsVisualStyles() {
		if (!isWindows()) {
			return false;
		}

		boolean xpthemeActive = Boolean.TRUE.equals(Toolkit.getDefaultToolkit()
				.getDesktopProperty("win.xpstyle.themeActive"));
		if (!xpthemeActive) {
			return false;
		} else {
			try {
				return System.getProperty("swing.noxp") == null;
			} catch (RuntimeException e) {
				return true;
			}
		}
	}

	final static private float getMajorJavaVersion(String javaVersion) {
		try {
			return Float.parseFloat(javaVersion.substring(0, 3));
		} catch (NumberFormatException e) {
			return DEFAULT_JAVA_VERSION;
		}
	}

	public static boolean isJDK13() {
		return majorJavaVersion == DEFAULT_JAVA_VERSION;
	}

	public static boolean isJDK14() {
		return majorJavaVersion == 1.4f;
	}

	public static boolean isJDK15() {
		return majorJavaVersion == 1.5f;
	}

	public static boolean isJDK16() {
		return majorJavaVersion == 1.6f;
	}

	public static boolean isJDK17() {
		return majorJavaVersion == 1.7f;
	}

	public static boolean isSun() {
		return System.getProperty("java.vm.vendor").indexOf("Sun") != -1
				|| System.getProperty("java.vm.vendor").indexOf("Oracle") != -1;
	}

	public static boolean isApple() {
		return System.getProperty("java.vm.vendor").indexOf("Apple") != -1;
	}

	public static boolean isHPUX() {
		return System.getProperty("java.vm.vendor").indexOf(
				"Hewlett-Packard Company") != -1;
	}

	public static boolean isIBM() {
		return System.getProperty("java.vm.vendor").indexOf("IBM") != -1;
	}

	public static boolean isBlackdown() {
		return System.getProperty("java.vm.vendor").indexOf("Blackdown") != -1;
	}

	public static boolean isBEAWithUnsafeSupport() {
		if (System.getProperty("java.vm.vendor").indexOf("BEA") != -1) {
			String vmVersion = System.getProperty("java.vm.version");
			if (vmVersion.startsWith("R")) {
				return true;
			}
			String vmInfo = System.getProperty("java.vm.info");
			if (vmInfo != null) {
				return (vmInfo.startsWith("R25.1") || vmInfo
						.startsWith("R25.2"));
			}
		}

		return false;
	}

	public static String getJavaVersion() {
		return javaVersion;
	}

	public static int getMajorJavaVersion() {
		return tmpmajor;
	}

	/**
	 * 返回一个Random对象
	 * 
	 * @return
	 */
	public static Random getRandomObject() {
		return random;
	}

	/**
	 * 返回一个随机数
	 * 
	 * @param i
	 * @param j
	 * @return
	 */
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
		} catch (IOException ex) {
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
