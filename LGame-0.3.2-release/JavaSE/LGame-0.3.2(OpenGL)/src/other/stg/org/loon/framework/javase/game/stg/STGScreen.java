package org.loon.framework.javase.game.stg;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.loon.framework.javase.game.action.avg.command.Command;
import org.loon.framework.javase.game.action.map.Config;
import org.loon.framework.javase.game.action.sprite.effect.ScrollEffect;
import org.loon.framework.javase.game.core.LSystem;
import org.loon.framework.javase.game.core.geom.RectBox;
import org.loon.framework.javase.game.core.graphics.LFont;
import org.loon.framework.javase.game.core.graphics.LImage;
import org.loon.framework.javase.game.core.graphics.Screen;
import org.loon.framework.javase.game.core.graphics.opengl.GL;
import org.loon.framework.javase.game.core.graphics.opengl.GLColor;
import org.loon.framework.javase.game.core.graphics.opengl.GLEx;
import org.loon.framework.javase.game.core.graphics.opengl.LTexture;
import org.loon.framework.javase.game.core.graphics.opengl.LTexturePack;
import org.loon.framework.javase.game.core.graphics.opengl.LTextures;
import org.loon.framework.javase.game.core.graphics.opengl.LTexture.Format;
import org.loon.framework.javase.game.core.input.LTouch;
import org.loon.framework.javase.game.core.timer.LTimer;
import org.loon.framework.javase.game.core.timer.LTimerContext;
import org.loon.framework.javase.game.stg.STGHero.HeroTouch;
import org.loon.framework.javase.game.stg.effect.Picture;
import org.loon.framework.javase.game.utils.MathUtils;
import org.loon.framework.javase.game.utils.StringUtils;

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
 * @email：ceponline@yahoo.com.cn
 * @version 0.1
 */
public abstract class STGScreen extends Screen {

	public static final int HERO = 0;

	public static final int HERO_SHOT = 1;

	public static final int ENEMY = 2;

	public static final int ENEMY_SHOT = 3;

	public static final int NO_HIT = 4;

	public static final int ITEM = 5;

	public static final int GET_ITEM = 6;

	public static final int SUICIDE = 7;

	public static final int ALL_HIT = 8;

	final static int NULL_MODE = 0;

	final static int GRP_MODE = 1;

	final static int STR_MODE = 2;

	final static int CENTER_STR_MODE = 3;

	final static int DRW_MODE = 4;

	final static int BACK_OTHER_MODE = 0;

	final static int BACK_STAR_MODE = 1;

	final static int BACK_SCROLL_MODE = 2;

	private ShotLoop shotLoop;

	private int spriteLength;

	private int count;

	private int dot_size;

	private int[] dot;

	private boolean dirty;

	private LTimer scrollDelay;

	private ScrollEffect scroll;

	private GLColor starColor;

	private int backgroundMode;

	private Command command;

	private String cmd_pack;

	private int cmd_enemy_no = 1000;

	private LTexturePack bitmapPack;

	private int sequenceCount = 0;

	private int planeFontSize = 20;

	protected Hashtable<Integer, STGPlane> planes;

	protected STGObjects stgObjects;

	private int[] spriteList;

	private String commandName;

	private String packXmlName;

	private int threadSpeed = 30;

	private HeroTouch touch;

	private class ShotLoop extends Thread {

		boolean threadSuspended = false;

		boolean threadStoped = false;

		public void run() {
			long timer = 0L;
			for (; !this.threadStoped;) {
				try {
					timer = System.currentTimeMillis();
					mainLoop();
					timer = System.currentTimeMillis() - timer;
					if (threadSpeed - timer < 0L) {
					} else {
						Thread.sleep(threadSpeed - timer);
					}
					for (; this.threadSuspended && !this.threadStoped;) {
						Thread.yield();
					}
				} catch (Exception ex) {
				}
			}
		}
	}

	/**
	 * 这是一个STG模块使用的访问限制器，用于制约用户仅可使用必要的图像功能。
	 * 
	 * PS:STG模块会将所有游戏图打包入LTexturePack当中，作为一张独立图片 使用(默认清空实际加载的图像)，所以需要特别的图像处理方式。
	 */
	public static class DrawableVisit {

		private STGScreen screen;

		private Format format = Format.DEFAULT;

		DrawableVisit(STGScreen screen) {
			this.screen = screen;
		}

		public Format getFormat() {
			return format;
		}

		public void setFormat(Format format) {
			this.format = format;
		}

		public int getWidth() {
			return screen.getWidth();
		}

		public int getHeight() {
			return screen.getHeight();
		}

		public int add(String res) {
			return screen.addBitmap(res);
		}

		public int add(LImage img) {
			return screen.addBitmap(img);
		}

		public boolean setPlaneScale(int index, float scale) {
			return screen.setPlaneScale(index, scale);
		}

		public void setPlaneSize(int index, int w, int h) {
			screen.setPlaneSize(index, w, h);
		}

		public void setPlaneGraphics(int index, int imgId, int x, int y) {
			setPlane(index, imgId, x, y, true);
		}

		public void setPlane(int index, int imgId, int x, int y, boolean v) {
			screen.setPlaneBitmap(index, 0, imgId);
			screen.setPlaneView(index, v);
			screen.setPlanePos(index, x, y);
		}

		public boolean setPlaneBitmap(int index, int animeNo, int imgId) {
			return screen.setPlaneBitmap(index, animeNo, imgId);
		}

		public int getPlanePosX(int index) {
			return screen.getPlanePosX(index);
		}

		public int getPlanePosY(int index) {
			return screen.getPlanePosY(index);
		}

		public boolean setPlaneAnimeDelay(int index, long delay) {
			return screen.setPlaneAnimeDelay(index, delay);
		}

		public boolean setPlaneAnime(int index, boolean anime) {
			return screen.setPlaneAnime(index, anime);
		}

		public boolean setPlaneString(int index, String mes) {
			return screen.setPlaneString(index, mes);
		}

		public boolean setPlaneCenterString(int index, String mes) {
			return screen.setPlaneCenterString(index, mes);
		}

		public boolean setPlaneFont(int index, String font, int style, int size) {
			return screen.setPlaneFont(index, font, style, size);
		}

		public boolean setPlaneColor(int index, int r, int g, int b) {
			return screen.setPlaneColor(index, r, g, b);
		}

		public boolean setPlaneView(int index, boolean v) {
			return screen.setPlaneView(index, v);
		}

		public boolean setPlaneDraw(int index, Picture draw) {
			return screen.setPlaneDraw(index, draw);
		}

		public boolean setPlaneMov(int index, int x, int y) {
			return screen.setPlaneMov(index, x, y);
		}

		public boolean setLocation(int index, int x, int y) {
			return screen.setLocation(index, x, y);
		}

		public boolean setPlanePos(int index, int x, int y) {
			return screen.setPlanePos(index, x, y);
		}

		public boolean deletePlane(int index) {
			return screen.deletePlane(index);
		}

		public boolean deletePlaneAll() {
			return screen.deletePlaneAll();
		}

	}

	int getPlanePosX(int index) {
		STGPlane plane = planes.get(index);
		return plane == null ? 0 : plane.posX;
	}

	int getPlanePosY(int index) {
		STGPlane plane = planes.get(index);
		return plane == null ? 0 : plane.posY;
	}

	boolean setPlaneAnimeDelay(int index, long delay) {
		STGPlane plane = planes.get(index);
		if (plane == null) {
			return false;
		} else if (plane.planeMode != GRP_MODE) {
			return false;
		} else {
			if (plane.delay != null) {
				plane.delay.setDelay(delay);
			} else {
				plane.delay = new LTimer(delay);
			}
			return true;
		}
	}

	boolean setPlaneScale(int index, float scale) {
		STGPlane plane = planes.get(index);
		if (plane == null) {
			return false;
		} else {
			plane.scaleX = scale;
			plane.scaleY = scale;
			return true;
		}
	}

	boolean setPlaneAnime(int index, boolean anime) {
		STGPlane plane = planes.get(index);
		if (plane == null) {
			return false;
		} else if (plane.planeMode != GRP_MODE) {
			return false;
		} else {
			if (plane.animation = anime) {
				if (plane.animeList == null
						|| (plane.animeList.length != plane.images.size())) {
					plane.animeList = new int[plane.images.size()];
				}
				Enumeration enumeration = plane.images.keys();
				for (int j = 0; enumeration.hasMoreElements(); ++j) {
					plane.animeList[j] = ((Integer) enumeration.nextElement());
				}
				for (int i = 0; i < plane.animeList.length - 1; ++i) {
					for (int j = i + 1; j < plane.animeList.length; ++j) {
						if (plane.animeList[i] > plane.animeList[j]) {
							int animeNo = plane.animeList[i];
							plane.animeList[i] = plane.animeList[j];
							plane.animeList[j] = animeNo;
						}
					}
				}
			} else {
				plane.animeList = null;
			}
			return true;
		}
	}

	boolean setPlaneString(int index, String mes) {
		STGPlane plane = planes.get(index);
		if (plane == null) {
			plane = new STGPlane();
			this.planes.put(index, plane);
		}
		plane.font = LFont.getFont(LSystem.FONT, 0, planeFontSize);
		plane.color = new GLColor(0, 0, 0);
		plane.str = mes;
		plane.planeMode = STR_MODE;
		plane.view = true;
		plane.images.clear();
		plane.animation = false;
		plane.animeNo = 0;
		plane.draw = null;
		return true;
	}

	boolean setPlaneCenterString(int index, String mes) {
		STGPlane plane = planes.get(index);
		if (plane == null) {
			plane = new STGPlane();
			this.planes.put(index, plane);
		}
		plane.font = LFont.getFont(LSystem.FONT, 0, planeFontSize);
		plane.color = new GLColor(0, 0, 0);
		plane.str = mes;
		plane.planeMode = CENTER_STR_MODE;
		plane.view = true;
		plane.images.clear();
		plane.animation = false;
		plane.animeNo = 0;
		plane.draw = null;
		return true;
	}

	boolean setPlaneFont(int index, String font, int style, int size) {
		STGPlane plane = planes.get(index);
		if (plane == null) {
			return false;
		} else if ((plane.planeMode & STR_MODE) == 0) {
			return false;
		} else {
			if (font == null) {
				font = LSystem.FONT;
			}
			if (style < 0) {
				style = 0;
			}
			if (size < 0) {
				size = planeFontSize;
			}
			plane.font = LFont.getFont(font, style, size);
			return true;
		}
	}

	boolean setPlaneColor(int index, int r, int g, int b) {
		STGPlane plane = planes.get(index);
		if (plane == null) {
			return false;
		} else if ((plane.planeMode & STR_MODE) == 0) {
			return false;
		} else {
			plane.color = new GLColor(r, g, b);
			return true;
		}
	}

	boolean setPlaneView(int index, boolean v) {
		STGPlane plane = planes.get(index);
		if (plane == null) {
			return false;
		} else if ((plane.planeMode & STR_MODE) == 0) {
			return false;
		} else {
			plane.view = v;
			return true;
		}
	}

	boolean setPlaneDraw(int index, Picture draw) {
		STGPlane plane = planes.get(index);
		if (plane == null) {
			plane = new STGPlane();
			this.planes.put(index, plane);
		}
		plane.font = null;
		plane.color = null;
		plane.str = null;
		plane.planeMode = DRW_MODE;
		plane.view = true;
		plane.images.clear();
		plane.animation = false;
		plane.animeNo = 0;
		plane.draw = draw;
		return true;
	}

	boolean setPlaneMov(int index, int x, int y) {
		STGPlane plane = planes.get(index);
		if (plane == null) {
			return false;
		} else {
			plane.posX += x;
			plane.posY += y;
			return true;
		}
	}

	boolean setPlaneScale(int index, float sx, float sy) {
		STGPlane plane = planes.get(index);
		if (plane == null) {
			return false;
		} else {
			plane.scaleX = sx;
			plane.scaleY = sy;
			return true;
		}
	}

	boolean setLocation(int index, int x, int y) {
		return setPlanePos(index, x, y);
	}

	boolean setPlanePos(int index, int x, int y) {
		STGPlane plane = planes.get(index);
		if (plane == null) {
			return false;
		} else {
			plane.posX = x;
			plane.posY = y;
			return true;
		}
	}

	boolean setPosX(int index, int x) {
		STGPlane plane = planes.get(index);
		if (plane == null) {
			return false;
		} else {
			plane.posX = x;
			return true;
		}
	}

	boolean setPosY(int index, int y) {
		STGPlane plane = planes.get(index);
		if (plane == null) {
			return false;
		} else {
			plane.posY = y;
			return true;
		}
	}

	void setPlaneSize(int index, int w, int h) {
		STGPlane plane = planes.get(index);
		if (plane == null) {
			plane = new STGPlane();
			this.planes.put(index, plane);
		}
		if (plane.rect == null) {
			plane.rect = new RectBox(0, 0, w, h);
		} else {
			plane.rect.setBounds(0, 0, w, h);
		}
	}

	boolean setPlaneBitmap(int index, int animeNo, int imgId) {
		try {
			STGPlane plane = planes.get(index);
			if (plane == null) {
				plane = new STGPlane();
				this.planes.put(index, plane);
			}
			plane.animeNo = animeNo;
			plane.rect = bitmapPack.getImageRect(imgId);
			plane.images.put(plane.animeNo, imgId);
			plane.planeMode = GRP_MODE;
			plane.view = true;
			plane.str = null;
			plane.font = null;
			plane.color = null;
			plane.draw = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	int addBitmap(String res) {
		try {
			return bitmapPack.putImage(res);
		} catch (Exception e) {
		}
		return -1;
	}

	int addBitmap(LImage img) {
		try {
			return bitmapPack.putImage(img);
		} catch (Exception e) {
		}
		return -1;
	}

	public void deleteIndex(int id) {
		stgObjects.delObj(id);
		dirty = true;
	}

	public boolean deletePlane(int index) {
		STGPlane plane = planes.remove(index);
		dirty = true;
		return plane != null;
	}

	public boolean deletePlaneAll() {
		planes.clear();
		dirty = true;
		return planes.size() == 0;
	}

	public boolean clearScore() {
		return stgObjects.clearObjects();
	}

	public int getScore() {
		return stgObjects.score;
	}

	public int getHeroHP() {
		return getHero().getHP();
	}

	public int getHeroMP() {
		return getHero().getMP();
	}

	public void addBombHero(String packageName) {
		stgObjects.addBombHero(packageName);
		dirty = true;
	}

	public void addBombHero(String packageName, int x, int y) {
		stgObjects.addBombHero(packageName, x, y);
		dirty = true;
	}

	public void addClass(Class<?> clazz, int x, int y, int tpno) {
		addClass(clazz.getName(), x, y, tpno);
	}

	public void addScreenPackageClass(String className, int x, int y, int tpno) {
		addClass(getScreenPackName() + className, x, y, tpno);
	}

	public void addClass(String className, int x, int y, int tpno) {
		stgObjects.addClass(className, x, y, tpno);
		dirty = true;
	}

	public void addHeroClass(Class<?> clazz, int x, int y) {
		addHeroClass(clazz.getName(), x, y);
	}

	public void addHeroScreenPackageClass(String className, int x, int y) {
		addHeroClass(getScreenPackName() + className, x, y);
	}

	public void addHeroClass(String className, int x, int y) {
		stgObjects.addHero(className, x, y, stgObjects.heroPlnNo);
		STGHero hero = stgObjects.getHero();
		if (hero != null) {
			touch = new HeroTouch(hero, getWidth(), getHeight(), true);
		}
		dirty = true;
	}

	public int getHeroNo() {
		return stgObjects.heroPlnNo;
	}

	public ShotLoop getShotLoop() {
		return shotLoop;
	}

	public void pause() {
		if (shotLoop != null) {
			shotLoop.threadSuspended = true;
		}
	}

	public void resume() {
		if (shotLoop != null) {
			shotLoop.threadSuspended = false;
		}
	}

	public void startShotLoop() {
		stopShotLoop();
		shotLoop = new ShotLoop();
		shotLoop.setPriority(Thread.NORM_PRIORITY);
		shotLoop.threadSuspended = false;
		shotLoop.start();
	}

	public void stopShotLoop() {
		if (shotLoop != null) {
			shotLoop.threadStoped = true;
			try {
				shotLoop.interrupt();
				shotLoop = null;
			} catch (Exception ex) {
			}
		}
	}

	public STGScreen(String path, String pack) {
		this.commandName = path;
		this.packXmlName = pack;
	}

	public STGScreen(String path) {
		this(path, null);
	}

	public void onCreate(int width, int height) {
		super.onCreate(width, height);
		if (planes == null) {
			planes = new Hashtable<Integer, STGPlane>(100);
		} else {
			planes.clear();
		}
		if (stgObjects != null) {
			stgObjects.dispose();
			stgObjects = null;
		}
		this.stgObjects = new STGObjects(this, 100);
		if (width > height) {
			this.scrollDelay = new LTimer(50);
		} else {
			this.scrollDelay = new LTimer(40);
		}
		this.dot_size = height / 10;
		this.count = 0;
		if (dot != null) {
			dot = null;
		}
		this.dot = new int[dot_size];
		for (int i = 0; i < dot_size; ++i) {
			this.dot[i] = (int) (MathUtils.random() * width);
		}
	}

	public final void onLoad() {
		if (bitmapPack != null) {
			bitmapPack.dispose();
			bitmapPack = null;
		}
		if (packXmlName == null) {
			bitmapPack = new LTexturePack();
		} else {
			bitmapPack = new LTexturePack(packXmlName);
		}
		if (commandName != null) {
			this.openCommand(commandName, true);
		}
		final DrawableVisit visit = new DrawableVisit(this);
		this.loadDrawable(visit);
		this.bitmapPack.packed(visit.format);
		this.onLoading();
	}

	public abstract void onLoading();

	/**
	 * 返回当前游戏主角
	 * 
	 * @return
	 */
	public STGHero getHero() {
		return stgObjects.getHero();
	}

	/**
	 * 返回当前Screen所在包
	 * 
	 * @return
	 */
	public String getScreenPackName() {
		if (cmd_pack == null) {
			Class<?> clazz = this.getClass();
			cmd_pack = clazz.getPackage().getName();
		}
		return cmd_pack;
	}

	/**
	 * STG游戏主循环
	 * 
	 */
	private void mainLoop() {
		if (!nextCommand()) {
			onCommandAchieve();
			if (stgObjects.isAliveHero() && stgObjects.size() == 1) {
				onEnemyClear();
			}
		} else if (!stgObjects.isAliveHero()) {
			onHeroDeath();
		}
		this.stgObjects.running();
		this.stgObjects.hitCheckHero();
		this.stgObjects.hitCheckHeroShot();
		this.onGameLoop();
		if (dirty) {
			updateSprite();
		}
	}

	/**
	 * 加载游戏脚本
	 * 
	 * @param in
	 * @return
	 */
	public boolean openCommand(InputStream in) {
		return openCommand(in, false);
	}

	/**
	 * 加载游戏脚本
	 * 
	 * @param resName
	 * @return
	 */
	public boolean openCommand(String resName) {
		return openCommand(resName, false);
	}

	/**
	 * 加载游戏脚本
	 * 
	 * @param resName
	 * @param thread
	 * @return
	 */
	public boolean openCommand(String resName, boolean thread) {
		this.sequenceCount = 0;
		this.cmd_pack = getScreenPackName();
		Command.resetCache();
		if (command == null) {
			command = new Command(resName);
		} else {
			command.formatCommand(resName);
		}
		if (thread) {
			this.startShotLoop();
		}
		return command != null;
	}

	/**
	 * 加载游戏脚本
	 * 
	 * @param in
	 * @param thread
	 * @return
	 */
	public boolean openCommand(InputStream in, boolean thread) {
		this.sequenceCount = 0;
		this.cmd_pack = getScreenPackName();
		Command.resetCache();
		if (command == null) {
			command = new Command(in);
		} else {
			command.formatCommand(in);
		}
		if (thread) {
			this.startShotLoop();
		}
		return command != null;
	}

	/**
	 * 读取游戏脚本
	 * 
	 * @return
	 */
	boolean nextCommand() {
		if (this.sequenceCount < 0) {
			Enumeration e = this.stgObjects.elements();
			while (e.hasMoreElements()) {
				STGObject shot = (STGObject) e.nextElement();
				if (shot.attribute == ENEMY) {
					return true;
				}
			}
			this.sequenceCount = 0;
		}
		if (this.sequenceCount > 0) {
			--this.sequenceCount;
			return true;
		} else {
			try {
				if (command.next()) {
					String cmd = command.doExecute();
					if (cmd.length() == 0) {
						return true;
					} else {
						if (onCommandAction(cmd)) {
							return true;
						}
						List<?> commands = Command.splitToList(cmd, " ");
						if (commands.size() > 0) {
							String cmdName = (String) commands.get(0);
							if (cmdName.equalsIgnoreCase("sleep")) {
								this.sequenceCount = Integer
										.parseInt((String) commands.get(1));
							} else if (cmdName.equalsIgnoreCase("wait")) {
								this.sequenceCount = -1;
							} else if (cmdName.equalsIgnoreCase("enemy")) {
								String enemy = (String) commands.get(1);
								int x = Integer.parseInt((String) commands
										.get(2));
								int y = Integer.parseInt((String) commands
										.get(3));
								if (StringUtils.charCount(enemy, '.') > 0) {
									addClass(enemy, x, y, cmd_enemy_no);
								} else {
									addClass(cmd_pack + "." + enemy, x, y,
											cmd_enemy_no);
								}
							} else if (cmdName.equalsIgnoreCase("package")) {
								this.cmd_pack = (String) commands.get(1);
							} else if (cmdName.equalsIgnoreCase("leader")) {
								String hero = (String) commands.get(1);
								int x = Integer.parseInt((String) commands
										.get(2));
								int y = Integer.parseInt((String) commands
										.get(3));
								if (StringUtils.charCount(hero, '.') > 0) {
									addHeroClass(hero, x, y);
								} else {
									addHeroClass(cmd_pack + "." + hero, x, y);
								}
							}
							return true;
						}
					}
				}
				return false;
			} catch (Exception ex) {
				return false;
			}
		}
	}

	/**
	 * 默认的Screen主线程中（非ShotLoop线程）事件
	 */
	public final void alter(LTimerContext context) {
		long elapsedTime = context.getTimeSinceLastUpdate();
		if (touch != null) {
			touch.update(elapsedTime);
		}
		if (scrollDelay.action(elapsedTime)) {
			switch (backgroundMode) {
			case BACK_STAR_MODE:
				this.dot[this.count] = (int) (MathUtils.random() * this
						.getWidth());
				++this.count;
				this.count %= dot_size;
				break;
			case BACK_SCROLL_MODE:
				if (scroll != null) {
					scroll.update(context.getTimeSinceLastUpdate());
				}
				break;
			}
		}
		update(elapsedTime);
	}

	public abstract void update(long elapsedTime);

	/**
	 * 非使用默认的游戏背景效果时，将调用此函数
	 * 
	 * @param g
	 */
	protected void drawOtherBackground(GLEx g) {

	}

	/**
	 * 游戏背景绘制
	 * 
	 * @param g
	 */
	protected void background(GLEx g) {
		switch (backgroundMode) {
		case BACK_STAR_MODE:
			if (starColor != null) {
				g.setColor(starColor);
			}
			g.glBegin(GL.GL_LINES);
			for (int j = 0; j < dot_size; this.count = (this.count + 1)
					% dot_size) {
				int index = this.dot[this.count] % 3;
				g.glVertex2f(dot[count] - index, getHeight() - j * 10);
				g.glVertex2f(dot[count] + index, getHeight() - j * 10);
				g.glVertex2f(dot[count], getHeight() - j * 10 - index);
				g.glVertex2f(dot[count], getHeight() - j * 10 + index);
				++j;
			}
			g.glEnd();
			if (starColor != null) {
				g.resetColor();
			}
			break;
		case BACK_SCROLL_MODE:
			if (scroll != null) {
				scroll.createUI(g);
			}
			break;
		case BACK_OTHER_MODE:
			drawOtherBackground(g);
			break;
		default:
			break;
		}
	}

	/**
	 * 游戏前景绘制
	 * 
	 * @param g
	 */
	protected void foreground(GLEx g) {

	}

	private void updateSprite() {
		synchronized (planes) {
			final int size = planes.size();
			if (spriteList == null) {
				this.spriteList = new int[size];
			} else if (spriteLength != size) {
				this.spriteList = null;
				this.spriteList = new int[size];
			}
			spriteLength = size;
			Enumeration<Integer> keys = this.planes.keys();
			for (int i = 0; keys.hasMoreElements(); ++i) {
				this.spriteList[i] = keys.nextElement();
			}
			for (int i = 0; i < spriteLength - 1; ++i) {
				for (int j = i + 1; j < spriteLength; ++j) {
					if (this.spriteList[i] > this.spriteList[j]) {
						int index = this.spriteList[i];
						this.spriteList[i] = this.spriteList[j];
						this.spriteList[j] = index;
					}
				}
			}
			dirty = false;
		}
	}

	public final synchronized void draw(GLEx g) {
		background(g);
		if (isOnLoadComplete()) {
			if (spriteLength == 0) {
				return;
			}
			bitmapPack.glBegin();
			for (int j = 0; j < spriteLength; ++j) {
				final int id = spriteList[j];
				STGPlane plane = planes.get(id);
				if (plane == null) {
					continue;
				}
				if (onDrawPlane(g, id)) {
					continue;
				}
				if (plane.view) {
					if (plane.planeMode == GRP_MODE) {
						if (plane.animation) {
							if (plane.delay.action(elapsedTime)) {
								int index;
								for (index = 0; plane.animeList[index] != plane.animeNo; ++index) {
									;
								}
								index = (index + 1) % plane.animeList.length;
								plane.animeNo = plane.animeList[index];
							}
						}
						if (plane.scaleX == 1 && plane.scaleY == 1) {
							bitmapPack.draw(plane.images.get(plane.animeNo),
									plane.posX, plane.posY);
						} else {
							bitmapPack.draw(plane.images.get(plane.animeNo),
									plane.posX, plane.posY, plane.rect.width
											* plane.scaleX, plane.rect.height
											* plane.scaleY);
						}
					} else if (plane.planeMode == STR_MODE) {
						g.setFont(plane.font);
						g.setColor(plane.color);
						g.drawString(plane.str, plane.posX, plane.posY
								+ plane.font.getSize());
					} else if (plane.planeMode == CENTER_STR_MODE) {
						g.setFont(plane.font);
						g.setColor(plane.color);
						g.drawString(plane.str, plane.posX
								- plane.font.stringWidth(plane.str) / 2,
								plane.posY + plane.font.getSize());
					} else if (plane.planeMode == DRW_MODE) {
						plane.draw.paint(g, plane);
					}
				}
			}
			bitmapPack.glEnd();
		}
		foreground(g);
	}

	/**
	 * 单独绘制某一指定图像画面(当返回True时，不会自动绘制该精灵)
	 * 
	 * @param g
	 * @param id
	 * @return
	 */
	public abstract boolean onDrawPlane(GLEx g, int id);

	/**
	 * 统一的游戏图片资源管理接口
	 * 
	 * @param drawable
	 */
	public abstract void loadDrawable(final DrawableVisit drawable);

	/**
	 * 当游戏线程每次循环时都将触发此函数
	 * 
	 */
	public abstract void onGameLoop();

	/**
	 * 当游戏主角死亡时将触发此函数
	 * 
	 */
	public abstract void onHeroDeath();

	/**
	 * 当敌人被全部清空时，将触发此函数
	 * 
	 */
	public abstract void onEnemyClear();

	/**
	 * 当所有脚本读取完毕时，将触发此函数
	 * 
	 */
	public abstract void onCommandAchieve();

	/**
	 * 当每次读取脚本时，将触发此函数(如果返回True，则中断默认脚本规则)
	 * 
	 * @param cmd
	 * @return
	 */
	public abstract boolean onCommandAction(String cmd);

	public final void touchDown(final LTouch e) {
		Runnable runnable = new Runnable() {
			public void run() {
				if (touch != null) {
					touch.onTouch(e);
				}
			}
		};
		callEvent(runnable);
		onDown(e);
	}

	public abstract void onDown(final LTouch e);

	public final void touchDrag(LTouch e) {
		onDrag(e);
	}

	public abstract void onDrag(final LTouch e);

	public final void touchMove(LTouch e) {
		onMove(e);
	}

	public abstract void onMove(final LTouch e);

	public final void touchUp(LTouch e) {
		onUp(e);
	}

	public abstract void onUp(final LTouch e);

	public void setScrollModeBackground(String resName) {
		setScrollModeBackground(Config.UP, resName);
	}

	public void setScrollModeBackground(int c, String resName) {
		setScrollModeBackground(c, LTextures.loadTexture(resName).get());
	}

	public void setScrollModeBackground(int c, LTexture t) {
		if (scroll != null) {
			scroll.dispose();
			scroll = null;
		}
		scroll = new ScrollEffect(c, t);
		scroll.setDelay(0);
		this.backgroundMode = BACK_SCROLL_MODE;
	}

	public void setStarModeBackground(GLColor c) {
		if (c != null && GLColor.white.equals(c)) {
			starColor = null;
		} else {
			starColor = c;
		}
		this.backgroundMode = BACK_STAR_MODE;
	}

	public LTexturePack getBitmapPack() {
		return bitmapPack;
	}

	public int getCmdEnemyNo() {
		return cmd_enemy_no;
	}

	public void setCmdEnemyNo(int no) {
		this.cmd_enemy_no = no;
	}

	public String getCmdPack() {
		return cmd_pack;
	}

	public void setCmdPack(String pack) {
		this.cmd_pack = pack;
	}

	public Command getCommand() {
		return command;
	}

	public String getCommandName() {
		return commandName;
	}

	public LTimer getScrollDelay() {
		return scrollDelay;
	}

	public String getPackXmlName() {
		return packXmlName;
	}

	public int getPlaneFontSize() {
		return planeFontSize;
	}

	public void setPlaneFontSize(int planeFontSize) {
		this.planeFontSize = planeFontSize;
	}

	public int getSequenceCount() {
		return sequenceCount;
	}

	public void setSequenceCount(int sequenceCount) {
		this.sequenceCount = sequenceCount;
	}

	public int[] getSpriteList() {
		return spriteList;
	}

	public int getThreadSpeed() {
		return threadSpeed;
	}

	public void setThreadSpeed(int threadSpeed) {
		this.threadSpeed = threadSpeed;
	}

	public HeroTouch getHeroTouch() {
		return touch;
	}

	public void dispose() {
		if (bitmapPack != null) {
			bitmapPack.dispose();
			bitmapPack = null;
		}
		stopShotLoop();
		if (stgObjects != null) {
			stgObjects.dispose();
			stgObjects = null;
		}
	}

}
