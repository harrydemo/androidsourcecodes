package org.loon.framework.javase.game.core.graphics;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import org.loon.framework.javase.game.action.collision.GravityHandler;
import org.loon.framework.javase.game.action.sprite.ISprite;
import org.loon.framework.javase.game.action.sprite.Sprites;
import org.loon.framework.javase.game.action.sprite.Sprites.SpriteListener;
import org.loon.framework.javase.game.core.EmulatorButtons;
import org.loon.framework.javase.game.core.EmulatorListener;
import org.loon.framework.javase.game.core.LObject;
import org.loon.framework.javase.game.core.LRelease;
import org.loon.framework.javase.game.core.LSystem;
import org.loon.framework.javase.game.core.geom.Camera;
import org.loon.framework.javase.game.core.geom.RectBox;
import org.loon.framework.javase.game.core.graphics.component.LLayer;
import org.loon.framework.javase.game.core.graphics.opengl.GLColor;
import org.loon.framework.javase.game.core.graphics.opengl.GLEx;
import org.loon.framework.javase.game.core.graphics.opengl.LTexture;
import org.loon.framework.javase.game.core.graphics.opengl.LTexture.Format;
import org.loon.framework.javase.game.core.input.LProcess;
import org.loon.framework.javase.game.core.input.LInput;
import org.loon.framework.javase.game.core.input.LKey;
import org.loon.framework.javase.game.core.input.LTouch;
import org.loon.framework.javase.game.core.input.LTransition;
import org.loon.framework.javase.game.core.input.LInputFactory.Touch;
import org.loon.framework.javase.game.core.timer.LTimerContext;
import org.loon.framework.javase.game.media.SoundBox;
import org.loon.framework.javase.game.utils.log.Level;
import org.loon.framework.javase.game.utils.log.Log;
import org.loon.framework.javase.game.utils.log.LogFactory;

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
 * @version 0.1
 */
public abstract class Screen extends SoundBox implements LInput, LRelease {

	public static enum SensorDirection {
		NONE, LEFT, RIGHT, UP, DOWN;
	}

	public static interface LEvent {

		public void call();

	}

	public abstract void draw(GLEx g);

	public final static int SCREEN_NOT_REPAINT = 0;

	public final static int SCREEN_BITMAP_REPAINT = -1;

	public final static int SCREEN_CANVAS_REPAINT = -2;

	public final static int SCREEN_COLOR_REPAINT = -3;

	// 线程事件集合
	private final ArrayList<Runnable> runnables;

	// 0.3.2版新增的简易重力控制接口
	private GravityHandler gravityHandler;

	// 0.3.2版新增的照相机接口
	private Camera camera;

	private final Log log;

	private GLColor color;

	private int touchX, touchY, lastTouchX, lastTouchY, touchDX, touchDY,
			touchDirection;

	public long elapsedTime;

	private final static boolean[] touchType, keyType;

	private int touchButtonPressed = LInput.NO_BUTTON,
			touchButtonReleased = LInput.NO_BUTTON;

	private int keyButtonPressed = LInput.NO_KEY,
			keyButtonReleased = LInput.NO_KEY;

	boolean isNext;

	private int mode, frame;

	private LTexture currentScreen;

	private LProcess handler;

	private int width, height, halfWidth, halfHeight;

	private SensorDirection direction = SensorDirection.NONE;

	private LInput baseInput;

	// 精灵集合
	private Sprites sprites;

	// 桌面集合
	private Desktop desktop;

	private Point touch = new Point(0, 0);

	private boolean isLoad, isLock, isClose, isGravity, isCamera;

	static {
		keyType = new boolean[15];
		touchType = new boolean[15];
	}

	public Screen() {
		LSystem.AUTO_REPAINT = true;
		this.log = LogFactory.getInstance(this.getClass());
		this.runnables = new ArrayList<Runnable>(1);
		this.handler = LSystem.screenProcess;
		this.width = LSystem.screenRect.width;
		this.height = LSystem.screenRect.height;
		this.halfWidth = width / 2;
		this.halfHeight = height / 2;
		this.setFPS(getMaxFPS());
	}

	/**
	 * 当Screen被创建(或再次加载)时将调用此函数
	 * 
	 * @param width
	 * @param height
	 */
	public void onCreate(int width, int height) {
		this.mode = SCREEN_CANVAS_REPAINT;
		this.baseInput = this;
		this.width = width;
		this.height = height;
		this.halfWidth = width / 2;
		this.halfHeight = height / 2;
		this.touchX = touchY = lastTouchX = lastTouchY = touchDX = touchDY = 0;
		this.isLoad = isLock = isClose = isGravity = isCamera = false;
		if (sprites != null) {
			sprites.dispose();
			sprites = null;
		}
		this.sprites = new Sprites(width, height);
		if (desktop != null) {
			desktop.dispose();
			desktop = null;
		}
		this.desktop = new Desktop(baseInput, width, height);
		this.isNext = true;
	}

	/**
	 * 当执行Screen转换时将调用此函数(如果返回的LTransition不为null，则渐变效果会被执行)
	 * 
	 * @return
	 */
	public LTransition onTransition() {
		return null;
	}

	/**
	 * 设定是否开启相机
	 * 
	 * @param cam
	 */
	public void setCamera(boolean cam) {
		this.isCamera = cam;
	}

	/**
	 * 设定相机对象
	 * 
	 * @param cam
	 */
	public void setCamera(Camera cam) {
		if (this.isCamera = (cam != null)) {
			this.camera = cam;
		}
	}

	/**
	 * 设定相机跟随目标及相机大小
	 * 
	 * @param follow
	 * @param w
	 * @param h
	 */
	public void setCamera(Object follow, int w, int h) {
		Camera camera = new Camera(follow, w, h);
		setCamera(camera);
	}

	public boolean isCamera() {
		return isCamera;
	}

	public Camera getCamera() {
		return this.camera;
	}

	/**
	 * 设定重力系统是否启动
	 * 
	 * @param g
	 * @return
	 */
	public synchronized GravityHandler setGravity(boolean g) {
		if (g && gravityHandler == null) {
			gravityHandler = new GravityHandler();
		}
		this.isGravity = g;
		return gravityHandler;
	}

	/**
	 * 判断重力系统是否启动
	 * 
	 * @return
	 */
	public synchronized boolean isGravity() {
		return this.isGravity;
	}

	/**
	 * 获得当前重力器句柄
	 * 
	 * @return
	 */
	public GravityHandler getGravityHandler() {
		return setGravity(true);
	}

	/**
	 * 获得当前游戏事务运算时间是否被锁定
	 * 
	 * @return
	 */
	public boolean isLock() {
		return isLock;
	}

	/**
	 * 锁定游戏事务运算时间
	 * 
	 * @param lock
	 */
	public void setLock(boolean lock) {
		this.isLock = lock;
	}

	/**
	 * 关闭游戏
	 * 
	 * @param close
	 */
	public void setClose(boolean close) {
		this.isClose = close;
	}

	/**
	 * 判断游戏是否被关闭
	 * 
	 * @return
	 */
	public boolean isClose() {
		return isClose;
	}

	/**
	 * 设定当前帧
	 * 
	 * @param frame
	 */
	public void setFrame(int frame) {
		this.frame = frame;
	}

	/**
	 * 返回当前帧
	 * 
	 * @return
	 */
	public int getFrame() {
		return frame;
	}

	/**
	 * 移动当前帧
	 * 
	 * @return
	 */
	public synchronized boolean next() {
		this.frame++;
		return isNext;
	}

	/**
	 * 暂停当前Screen指定活动帧数
	 * 
	 * @param i
	 */
	public synchronized void waitFrame(int i) {
		for (int wait = frame + i; frame < wait;) {
			try {
				super.wait();
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * 暂停当前Screen指定时间
	 * 
	 * @param i
	 */
	public synchronized void waitTime(long i) {
		for (long time = System.currentTimeMillis() + i; System
				.currentTimeMillis() < time;)
			try {
				super.wait(time - System.currentTimeMillis());
			} catch (Exception ex) {
			}
	}

	/**
	 * 初始化时加载的数据
	 */
	public void onLoad() {

	}

	/**
	 * 初始化加载完毕
	 * 
	 */
	public void onLoaded() {

	}

	/**
	 * 改变资源加载状态
	 */
	public void setOnLoadState(boolean flag) {
		this.isLoad = flag;
	}

	/**
	 * 获得当前资源加载是否完成
	 */
	public boolean isOnLoadComplete() {
		return isLoad;
	}

	/**
	 * 取出第一个Screen并执行
	 * 
	 */
	public void runFirstScreen() {
		if (handler != null) {
			handler.runFirstScreen();
		}
	}

	/**
	 * 取出最后一个Screen并执行
	 */
	public void runLastScreen() {
		if (handler != null) {
			handler.runLastScreen();
		}
	}

	/**
	 * 运行指定位置的Screen
	 * 
	 * @param index
	 */
	public void runIndexScreen(int index) {
		if (handler != null) {
			handler.runIndexScreen(index);
		}
	}

	/**
	 * 运行自当前Screen起的上一个Screen
	 */
	public void runPreviousScreen() {
		if (handler != null) {
			handler.runPreviousScreen();
		}
	}

	/**
	 * 运行自当前Screen起的下一个Screen
	 */
	public void runNextScreen() {
		if (handler != null) {
			handler.runNextScreen();
		}
	}

	/**
	 * 向缓存中添加Screen数据，但是不立即执行
	 * 
	 * @param screen
	 */
	public void addScreen(Screen screen) {
		if (handler != null) {
			handler.addScreen(screen);
		}
	}

	/**
	 * 获得保存的Screen列表
	 * 
	 * @return
	 */
	public LinkedList<Screen> getScreens() {
		if (handler != null) {
			return handler.getScreens();
		}
		return null;
	}

	/**
	 * 获得缓存的Screen总数
	 */
	public int getScreenCount() {
		if (handler != null) {
			return handler.getScreenCount();
		}
		return 0;
	}

	/**
	 * 返回精灵监听
	 * 
	 * @return
	 */

	public SpriteListener getSprListerner() {
		if (sprites == null) {
			return null;
		}
		return sprites.getSprListerner();
	}

	/**
	 * 监听Screen中精灵
	 * 
	 * @param sprListerner
	 */

	public void setSprListerner(SpriteListener sprListerner) {
		if (sprites == null) {
			return;
		}
		sprites.setSprListerner(sprListerner);
	}

	/**
	 * 获得当前Screen类名
	 */
	public String getName() {
		return getClass().getSimpleName();
	}

	/**
	 * 设定模拟按钮监听器
	 */

	public void setEmulatorListener(EmulatorListener emulator) {
		if (LSystem.screenProcess != null) {
			LSystem.screenProcess.setEmulatorListener(emulator);
		}
	}

	/**
	 * 返回模拟按钮集合
	 * 
	 * @return
	 */

	public EmulatorButtons getEmulatorButtons() {
		if (LSystem.screenProcess != null) {
			return LSystem.screenProcess.getEmulatorButtons();
		}
		return null;
	}

	/**
	 * 设定模拟按钮组是否显示
	 * 
	 * @param visible
	 */

	public void emulatorButtonsVisible(boolean visible) {
		if (LSystem.screenProcess != null) {
			try {
				EmulatorButtons es = LSystem.screenProcess.getEmulatorButtons();
				es.setVisible(visible);
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 设定背景图像
	 * 
	 * @param screen
	 */
	public void setBackground(LTexture background) {
		if (background != null) {
			setRepaintMode(SCREEN_BITMAP_REPAINT);
			LTexture screen = null;
			if (background.getWidth() != getWidth()
					|| background.getHeight() != getHeight()) {
				screen = background.scale(getWidth(), getHeight());
			} else {
				screen = background;
			}
			LTexture tmp = currentScreen;
			currentScreen = screen;
			if (tmp != null) {
				tmp.dispose();
				tmp = null;
			}
		} else {
			setRepaintMode(SCREEN_CANVAS_REPAINT);
		}
	}

	/**
	 * 设定背景图像
	 */
	public void setBackground(String fileName) {
		this.setBackground(new LTexture(fileName, Format.STATIC));
	}

	public void setBackground(Color c) {
		setBackground(new GLColor(c.getRed(), c.getGreen(), c.getBlue(), c
				.getAlpha()));
	}

	/**
	 * 设定背景颜色
	 * 
	 * @param c
	 */
	public void setBackground(GLColor c) {
		setRepaintMode(SCREEN_COLOR_REPAINT);
		if (color == null) {
			color = new GLColor(c);
		} else {
			color.setColor(c.r, c.g, c.b, c.a);
		}
	}

	public GLColor getColor() {
		return color;
	}

	/**
	 * 返回背景图像
	 * 
	 * @return
	 */
	public LTexture getBackground() {
		return currentScreen;
	}

	/**
	 * 设定刷新率
	 * 
	 * @param fps
	 */

	public void setFPS(long fps) {
		if (handler != null) {
			handler.getScene().setFPS(fps);
		}
	}

	/**
	 * 返回当前刷新率
	 * 
	 * @return
	 */

	public long getFPS() {
		if (handler != null) {
			return handler.getScene().getCurrentFPS();
		}
		return 0;
	}

	/**
	 * 返回最大刷新率
	 * 
	 * @return
	 */

	public long getMaxFPS() {
		if (handler != null) {
			return handler.getScene().getMaxFPS();
		}
		return 0;
	}

	public Desktop getDesktop() {
		return desktop;
	}

	public Sprites getSprites() {
		return sprites;
	}

	/**
	 * 返回与指定类匹配的组件
	 */

	public ArrayList<LComponent> getComponents(Class<? extends LComponent> clazz) {
		if (desktop != null) {
			return desktop.getComponents(clazz);
		}
		return null;
	}

	/**
	 * 返回位于屏幕顶部的组件
	 * 
	 * @return
	 */

	public LComponent getTopComponent() {
		if (desktop != null) {
			return desktop.getTopComponent();
		}
		return null;
	}

	/**
	 * 返回位于屏幕底部的组件
	 * 
	 * @return
	 */

	public LComponent getBottomComponent() {
		if (desktop != null) {
			return desktop.getBottomComponent();
		}
		return null;
	}

	/**
	 * 返回位于屏幕顶部的图层
	 */

	public LLayer getTopLayer() {
		if (desktop != null) {
			return desktop.getTopLayer();
		}
		return null;
	}

	/**
	 * 返回位于屏幕底部的图层
	 */

	public LLayer getBottomLayer() {
		if (desktop != null) {
			return desktop.getBottomLayer();
		}
		return null;
	}

	/**
	 * 返回所有指定类产生的精灵
	 * 
	 */

	public ArrayList<ISprite> getSprites(Class<? extends ISprite> clazz) {
		if (sprites != null) {
			return sprites.getSprites(clazz);
		}
		return null;
	}

	/**
	 * 返回位于数据顶部的精灵
	 * 
	 */

	public ISprite getTopSprite() {
		if (sprites != null) {
			return sprites.getTopSprite();
		}
		return null;
	}

	/**
	 * 返回位于数据底部的精灵
	 * 
	 */

	public ISprite getBottomSprite() {
		if (sprites != null) {
			return sprites.getBottomSprite();
		}
		return null;
	}

	/**
	 * 添加游戏精灵
	 * 
	 * @param sprite
	 */

	public void add(ISprite sprite) {
		if (sprites != null) {
			sprites.add(sprite);
		}
	}

	/**
	 * 添加游戏组件
	 * 
	 * @param comp
	 */

	public void add(LComponent comp) {
		if (desktop != null) {
			desktop.add(comp);
		}
	}

	public void remove(ISprite sprite) {
		if (sprites != null) {
			sprites.remove(sprite);
		}
	}

	public void removeSprite(Class<? extends ISprite> clazz) {
		if (sprites != null) {
			sprites.remove(clazz);
		}
	}

	public void remove(LComponent comp) {
		if (desktop != null) {
			desktop.remove(comp);
		}
	}

	public void removeComponent(Class<? extends LComponent> clazz) {
		if (desktop != null) {
			desktop.remove(clazz);
		}
	}

	public void removeAll() {
		if (sprites != null) {
			sprites.removeAll();
		}
		if (desktop != null) {
			desktop.getContentPane().clear();
		}
	}

	/**
	 * 判断是否点中指定精灵
	 * 
	 * @param sprite
	 * @return
	 */

	public boolean onClick(ISprite sprite) {
		if (sprite == null) {
			return false;
		}
		if (sprite.isVisible()) {
			RectBox rect = sprite.getCollisionBox();
			if (rect.contains(touchX, touchY)
					|| rect.intersects(touchX, touchY)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否点中指定组件
	 * 
	 * @param component
	 * @return
	 */

	public boolean onClick(LComponent component) {
		if (component == null) {
			return false;
		}
		if (component.isVisible()) {
			RectBox rect = component.getCollisionBox();
			if (rect.contains(touchX, touchY)
					|| rect.intersects(touchX, touchY)) {
				return true;
			}
		}
		return false;
	}

	public void centerOn(final LObject object) {
		LObject.centerOn(object, getWidth(), getHeight());
	}

	public void topOn(final LObject object) {
		LObject.topOn(object, getWidth(), getHeight());
	}

	public void leftOn(final LObject object) {
		LObject.leftOn(object, getWidth(), getHeight());
	}

	public void rightOn(final LObject object) {
		LObject.rightOn(object, getWidth(), getHeight());
	}

	public void bottomOn(final LObject object) {
		LObject.bottomOn(object, getWidth(), getHeight());
	}

	/**
	 * 获得背景显示模式
	 */
	public int getRepaintMode() {
		return mode;
	}

	/**
	 * 设定背景刷新模式
	 * 
	 * @param mode
	 */
	public void setRepaintMode(int mode) {
		this.mode = mode;
	}

	/**
	 * 增减一个线程事件
	 * 
	 * @param event
	 */
	public void callEvent(final LEvent event) {
		if (event == null) {
			return;
		}
		Thread runnable = new Thread() {
			public void run() {
				event.call();
			}
		};
		callEvent(runnable);
	}

	/**
	 * 增减一个线程事件
	 * 
	 * @param runnable
	 */
	public final void callEvent(Runnable runnable) {
		synchronized (runnables) {
			runnables.add(runnable);
		}
	}

	/**
	 * 暂停指定的线程事件
	 * 
	 * @param runnable
	 */
	public final void callEventWait(Runnable runnable) {
		synchronized (runnable) {
			synchronized (runnables) {
				runnables.add(runnable);
			}
			try {
				runnable.wait();
			} catch (InterruptedException ex) {
			}
		}
	}

	/**
	 * 中断所有线程事件
	 * 
	 */
	public final void callEventInterrupt() {
		synchronized (runnables) {
			for (Iterator<Runnable> it = runnables.iterator(); it.hasNext();) {
				Object running = it.next();
				synchronized (running) {
					if (running instanceof Thread) {
						((Thread) running).setPriority(Thread.MIN_PRIORITY);
						((Thread) running).interrupt();
					}
				}
			}
		}
	}

	/**
	 * 运行线程事件
	 * 
	 */
	public final void callEvents() {
		callEvents(true);
	}

	/**
	 * 执行或中断指定的线程事件
	 * 
	 * @param execute
	 */
	public final void callEvents(boolean execute) {
		if (!execute) {
			synchronized (runnables) {
				runnables.clear();
			}
			return;
		}
		if (runnables.size() == 0) {
			return;
		}
		ArrayList<Runnable> runnableList;
		synchronized (runnables) {
			runnableList = new ArrayList<Runnable>(runnables);
			runnables.clear();
		}
		for (Iterator<Runnable> it = runnableList.iterator(); it.hasNext();) {
			Object running = it.next();
			synchronized (running) {
				try {
					if (running instanceof Thread) {
						Thread thread = (Thread) running;
						if (!thread.isAlive()) {
							thread.start();
						}

					} else {
						((Runnable) running).run();
					}
				} catch (Exception ex) {
				}
				running.notifyAll();
			}
		}
		runnableList = null;
	}

	public synchronized void createUI(GLEx g) {
		if (isClose) {
			return;
		}
		if (isCamera) {
			if (camera != null) {
				g.translate(-camera.cameraX, -camera.cameraY);
			}
		}
		draw(g);
		if (sprites != null && this.sprites.size() > 0) {
			sprites.createUI(g);
		}
		if (desktop != null && this.desktop.size() > 0) {
			desktop.createUI(g);
		}
		if (isCamera) {
			if (camera != null) {
				g.translate(camera.cameraX, camera.cameraY);
			}
		}
	}

	public void runTimer(final LTimerContext timer) {
		if (isClose) {
			return;
		}
		this.elapsedTime = timer.getTimeSinceLastUpdate();
		if (isCamera) {
			if (camera != null) {
				camera.updateCamera();
			}
		}
		if (isGravity) {
			gravityHandler.update(elapsedTime);
		}
		if (sprites != null && this.sprites.size() > 0) {
			this.sprites.update(elapsedTime);
		}
		if (desktop != null && this.desktop.size() > 0) {
			this.desktop.update(elapsedTime);
		}
		baseInput.update(elapsedTime);
		this.alter(timer);
	}

	public void update(long elapsedTime) {
		this.touchDX = touchX - lastTouchX;
		this.touchDY = touchY - lastTouchY;
		this.lastTouchX = touchX;
		this.lastTouchY = touchY;
		this.keyButtonReleased = NO_KEY;
		this.touchButtonReleased = NO_BUTTON;
	}

	public void setNext(boolean next) {
		this.isNext = next;
	}

	public abstract void alter(LTimerContext timer);

	/**
	 * 设定游戏窗体
	 */
	public void setScreen(Screen screen) {
		if (handler != null) {
			this.handler.setScreen(screen);
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	/**
	 * 刷新基础设置
	 */
	public void refresh() {
		for (int i = 0; i < touchType.length; i++) {
			touchType[i] = false;
		}
		touchDX = touchDY = 0;
		for (int i = 0; i < keyType.length; i++) {
			keyType[i] = false;
		}
	}

	/**
	 * 对外的线程暂停器
	 * 
	 * @param timeMillis
	 */
	public void pause(long timeMillis) {
		try {
			Thread.sleep(timeMillis);
		} catch (InterruptedException e) {
		}
	}

	public Point getTouch() {
		touch.setLocation(touchX, touchY);
		return touch;
	}

	public boolean isPaused() {
		return LSystem.isPaused;
	}

	public boolean isTouchClick() {
		return touchButtonPressed == Touch.TOUCH_DOWN;
	}

	public boolean isTouchClickUp() {
		return touchButtonReleased == Touch.TOUCH_UP;
	}

	public int getTouchPressed() {
		return touchButtonPressed > LInput.NO_BUTTON ? touchButtonPressed
				: LInput.NO_BUTTON;
	}

	public int getTouchReleased() {
		return touchButtonReleased > LInput.NO_BUTTON ? touchButtonReleased
				: LInput.NO_BUTTON;
	}

	public boolean isTouchPressed(int button) {
		return touchButtonPressed == button;
	}

	public boolean isTouchReleased(int button) {
		return touchButtonReleased == button;
	}

	public int getTouchX() {
		return touchX;
	}

	public int getTouchY() {
		return touchY;
	}

	public int getTouchDX() {
		return touchDX;
	}

	public int getTouchDY() {
		return touchDY;
	}

	public boolean isTouchType(int type) {
		return touchType[type];
	}

	public int getKeyPressed() {
		return keyButtonPressed > LInput.NO_KEY ? keyButtonPressed
				: LInput.NO_KEY;
	}

	public boolean isKeyPressed(int keyCode) {
		return keyButtonPressed == keyCode;
	}

	public int getKeyReleased() {
		return keyButtonReleased > LInput.NO_KEY ? keyButtonReleased
				: LInput.NO_KEY;
	}

	public boolean isKeyReleased(int keyCode) {
		return keyButtonReleased == keyCode;
	}

	public boolean isKeyType(int type) {
		return keyType[type];
	}

	public void onResume() {

	}

	public void onPause() {

	}

	public void keyPressed(LKey e) {
		if (isLock || isClose || !isLoad) {
			return;
		}
		int type = e.getType();
		int code = e.getKeyCode();
		try {
			this.onKeyDown(e);
			keyType[type] = true;
			keyButtonPressed = code;
			keyButtonReleased = LInput.NO_KEY;
		} catch (Exception ex) {
			keyButtonPressed = LInput.NO_KEY;
			keyButtonReleased = LInput.NO_KEY;
			ex.printStackTrace();
		}
	}

	/**
	 * 设置键盘按下事件
	 * 
	 * @param code
	 */
	public void setKeyDown(int button) {
		try {
			keyButtonPressed = button;
			keyButtonReleased = LInput.NO_KEY;
		} catch (Exception e) {
		}
	}

	public void keyReleased(LKey e) {
		if (isLock || isClose || !isLoad) {
			return;
		}
		int type = e.getType();
		int code = e.getKeyCode();
		try {
			this.onKeyUp(e);
			keyType[type] = false;
			keyButtonReleased = code;
			keyButtonPressed = LInput.NO_KEY;
		} catch (Exception ex) {
			keyButtonPressed = LInput.NO_KEY;
			keyButtonReleased = LInput.NO_KEY;
			ex.printStackTrace();
		}
	}

	public void setKeyUp(int button) {
		try {
			keyButtonReleased = button;
			keyButtonPressed = LInput.NO_KEY;
		} catch (Exception e) {
		}
	}

	public void keyTyped(LKey e) {
		if (isLock || isClose || !isLoad) {
			return;
		}
		onKeyTyped(e);
	}

	public void onKeyDown(LKey e) {

	}

	public void onKeyUp(LKey e) {

	}

	public void onKeyTyped(LKey e) {

	}

	public final void mousePressed(LTouch e) {
		if (isLock || isClose || !isLoad) {
			return;
		}
		int type = e.getType();
		int button = e.getButton();
		try {
			touchType[type] = true;
			touchButtonPressed = button;
			touchButtonReleased = LInput.NO_BUTTON;
			touchDown(e);
		} catch (Exception ex) {
			touchButtonPressed = LInput.NO_BUTTON;
			touchButtonReleased = LInput.NO_BUTTON;
			ex.printStackTrace();
		}
	}

	public abstract void touchDown(LTouch e);

	public void mouseReleased(LTouch e) {
		if (isLock || isClose || !isLoad) {
			return;
		}
		int type = e.getType();
		int button = e.getButton();
		try {
			touchType[type] = false;
			touchButtonReleased = button;
			touchButtonPressed = LInput.NO_BUTTON;
			touchUp(e);
		} catch (Exception ex) {
			touchButtonPressed = LInput.NO_BUTTON;
			touchButtonReleased = LInput.NO_BUTTON;
			ex.printStackTrace();
		}
	}

	public abstract void touchUp(LTouch e);

	public void mouseMoved(LTouch e) {
		if (isLock || isClose || !isLoad) {
			return;
		}
		this.touchX = e.x();
		this.touchY = e.y();
		touchMove(e);
	}

	public abstract void touchMove(LTouch e);

	public void mouseDragged(LTouch e) {
		if (isLock || isClose || !isLoad) {
			return;
		}
		this.touchX = e.x();
		this.touchY = e.y();
		touchDrag(e);
	}

	public abstract void touchDrag(LTouch e);

	public void move(double x, double y) {
		this.touchX = (int) x;
		this.touchY = (int) y;
	}

	public boolean isMoving() {
		return Touch.isDrag();
	}

	public int getHalfWidth() {
		return halfWidth;
	}

	public int getHalfHeight() {
		return halfHeight;
	}

	public int getTouchDirection() {
		return touchDirection;
	}

	public SensorDirection getSensorDirection() {
		return direction;
	}

	/**
	 * 记录info信息
	 * 
	 * @param message
	 */
	public void info(String message) {
		log.info(message);
	}

	/**
	 * 记录info信息
	 * 
	 * @param message
	 * @param tw
	 */
	public void info(String message, Throwable tw) {
		log.info(message, tw);
	}

	/**
	 * 记录debug信息
	 * 
	 * @param message
	 */
	public void debug(String message) {
		log.debug(message);
	}

	/**
	 * 记录debug信息
	 * 
	 * @param message
	 * @param tw
	 */
	public void debug(String message, Throwable tw) {
		log.debug(message, tw);
	}

	/**
	 * 记录warn信息
	 * 
	 * @param message
	 */
	public void warn(String message) {
		log.warn(message);
	}

	/**
	 * 记录warn信息
	 * 
	 * @param message
	 * @param tw
	 */
	public void warn(String message, Throwable tw) {
		log.warn(message, tw);
	}

	/**
	 * 记录error信息
	 * 
	 * @param message
	 */
	public void error(String message) {
		log.error(message);
	}

	/**
	 * 记录error信息
	 * 
	 * @param message
	 * @param tw
	 */
	public void error(String message, Throwable tw) {
		log.error(message, tw);
	}

	/**
	 * 输出日志
	 * 
	 * @param message
	 */
	public void log(String message) {
		log.log(message);
	}

	/**
	 * 输出日志
	 * 
	 * @param message
	 * @param tw
	 */
	public void log(String message, Throwable tw) {
		log.log(message, tw);
	}

	/**
	 * 设定是否在控制台显示日志
	 * 
	 * @param show
	 */
	public void logShow(boolean show) {
		log.setVisible(show);
	}

	/**
	 * 设定日志是否保存为文件
	 * 
	 * @param save
	 */
	public void logSave(boolean save) {
		log.setSave(save);
	}

	/**
	 * 日志保存地点
	 * 
	 * @param fileName
	 */
	public void logFileName(String fileName) {
		log.setFileName(fileName);
	}

	/**
	 * 设定日志等级
	 * 
	 * @param level
	 */
	public void logLevel(int level) {
		log.setLevel(level);
	}

	/**
	 * 设定日志等级
	 * 
	 * @param level
	 */
	public void logLevel(Level level) {
		log.setLevel(level);
	}

	public final void destroy() {
		synchronized (this) {
			isClose = true;
			callEvents(false);
			isNext = false;
			isGravity = false;
			isCamera = false;
			isLock = true;
			if (sprites != null) {
				sprites.dispose();
				sprites = null;
			}
			if (desktop != null) {
				desktop.dispose();
				desktop = null;
			}
			if (currentScreen != null) {
				currentScreen.destroy();
				currentScreen = null;
			}
			if (gravityHandler != null) {
				gravityHandler.dispose();
				gravityHandler = null;
			}
			camera = null;
			dispose();
		}
	}

	/**
	 * 释放函数内资源
	 * 
	 */
	public void dispose() {

	}
}
