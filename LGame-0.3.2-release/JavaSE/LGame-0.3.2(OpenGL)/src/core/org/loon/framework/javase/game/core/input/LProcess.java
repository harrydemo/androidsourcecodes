package org.loon.framework.javase.game.core.input;

import java.util.LinkedList;

import org.loon.framework.javase.game.GameScene;
import org.loon.framework.javase.game.core.EmulatorButtons;
import org.loon.framework.javase.game.core.EmulatorListener;
import org.loon.framework.javase.game.core.LSystem;
import org.loon.framework.javase.game.core.graphics.LImage;
import org.loon.framework.javase.game.core.graphics.Screen;
import org.loon.framework.javase.game.core.graphics.opengl.GLColor;
import org.loon.framework.javase.game.core.graphics.opengl.GLEx;
import org.loon.framework.javase.game.core.graphics.opengl.LTexture;
import org.loon.framework.javase.game.core.graphics.opengl.LTextures;
import org.loon.framework.javase.game.core.timer.LTimerContext;
import org.loon.framework.javase.game.utils.ScreenUtils;

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
public class LProcess {

	private GameScene scene;

	EmulatorListener emulatorListener;

	EmulatorButtons emulatorButtons;

	private LinkedList<Screen> screens;

	private Screen currentControl, tmp;

	private boolean isInstance;

	private int id, width, height;

	private LInputFactory currentInput;

	private LTransition transition;

	private boolean waitTransition;

	private boolean running;

	public LProcess(GameScene scene, int width, int height) {
		this.width = width;
		this.height = height;
		this.scene = scene;
		this.screens = new LinkedList<Screen>();
		this.currentInput = new LInputFactory(this);
	}

	public void begin() {
		if (!running) {
			if (tmp != null) {
				setScreen(tmp);
			}
			running = true;
		}
	}

	public void end() {
		if (running) {
			running = false;
		}
	}

	public void calls() {
		if (isInstance) {
			currentControl.callEvents(true);
		}
	}

	public boolean next() {
		if (isInstance) {
			if (currentControl.next()) {
				return true;
			}
		}
		return false;
	}

	public void runTimer(LTimerContext context) {
		if (isInstance) {
			if (waitTransition) {
				if (transition != null) {
					switch (transition.code) {
					default:
						if (!currentControl.isOnLoadComplete()) {
							transition.update(context.timeSinceLastUpdate);
						}
						break;
					case 1:
						if (!transition.completed()) {
							transition.update(context.timeSinceLastUpdate);
						} else {
							endTransition();
						}
						break;
					}
				}
			} else {
				currentControl.runTimer(context);
				currentInput.runTimer(context);
				return;
			}
		}
	}

	public void draw(GLEx g) {
		if (isInstance) {
			if (waitTransition) {
				if (transition != null) {
					if (transition.isDisplayGameUI) {
						currentControl.createUI(g);
					}
					switch (transition.code) {
					default:
						if (!currentControl.isOnLoadComplete()) {
							transition.draw(g);
						}
						break;
					case 1:
						if (!transition.completed()) {
							transition.draw(g);
						}
						break;
					}
				}
			} else {
				currentControl.createUI(g);
				return;
			}
		}
	}

	public void drawEmulator(GLEx gl) {
		if (emulatorButtons != null) {
			emulatorButtons.draw(gl);
		}
	}

	public GLColor getColor() {
		if (isInstance) {
			return currentControl.getColor();
		}
		return null;
	}

	public LTexture getBackground() {
		if (isInstance) {
			return currentControl.getBackground();
		}
		return null;
	}

	public int getRepaintMode() {
		if (isInstance) {
			return currentControl.getRepaintMode();
		}
		return Screen.SCREEN_CANVAS_REPAINT;
	}

	/**
	 * 设定模拟按钮监听器
	 * 
	 * @param emulatorListener
	 */
	public void setEmulatorListener(EmulatorListener emulator) {
		this.emulatorListener = emulator;
		if (emulatorListener != null) {
			if (emulatorButtons == null) {
				emulatorButtons = new EmulatorButtons(emulatorListener,
						LSystem.screenRect.width, LSystem.screenRect.height);
			} else {
				emulatorButtons.setEmulatorListener(emulator);
			}
		} else {
			emulatorButtons = null;
		}
	}

	/**
	 * 获得模拟器监听
	 * 
	 * @return
	 */
	public EmulatorListener getEmulatorListener() {
		return emulatorListener;
	}

	/**
	 * 获得模拟器按钮
	 * 
	 * @return
	 */
	public EmulatorButtons getEmulatorButtons() {
		return emulatorButtons;
	}

	public void setID(int id) {
		this.id = id;
	}

	public int getID() {
		return id;
	}

	public final void setTransition(LTransition t) {
		this.transition = t;
	}

	public final LTransition getTransition() {
		return this.transition;
	}

	private final void startTransition() {
		if (transition != null) {
			waitTransition = true;
			if (isInstance) {
				currentControl.setLock(true);
			}
		}
	}

	private final void endTransition() {
		if (transition != null) {
			switch (transition.code) {
			default:
				waitTransition = false;
				transition.dispose();
				break;
			case 1:
				if (transition.completed()) {
					waitTransition = false;
					transition.dispose();
				}
				break;
			}
			if (isInstance) {
				currentControl.setLock(false);
			}
		} else {
			waitTransition = false;
		}
	}

	public synchronized Screen getScreen() {
		return currentControl;
	}

	public void runFirstScreen() {
		int size = screens.size();
		if (size > 0) {
			Object o = screens.getFirst();
			if (o != currentControl) {
				setScreen((Screen) o, false);
			}
		}
	}

	public void runLastScreen() {
		int size = screens.size();
		if (size > 0) {
			Object o = screens.getLast();
			if (o != currentControl) {
				setScreen((Screen) o, false);
			}
		}
	}

	public void runPreviousScreen() {
		int size = screens.size();
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				if (currentControl == screens.get(i)) {
					if (i - 1 > -1) {
						setScreen((Screen) screens.get(i - 1), false);
						return;
					}
				}
			}
		}
	}

	public void runNextScreen() {
		int size = screens.size();
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				if (currentControl == screens.get(i)) {
					if (i + 1 < size) {
						setScreen((Screen) screens.get(i + 1), false);
						return;
					}
				}
			}
		}
	}

	public void runIndexScreen(int index) {
		int size = screens.size();
		if (size > 0 && index > -1 && index < size) {
			Object o = screens.get(index);
			if (currentControl != o) {
				setScreen((Screen) screens.get(index), false);
			}
		}
	}

	public void addScreen(final Screen screen) {
		if (screen == null) {
			throw new RuntimeException("Cannot create a [IScreen] instance !");
		}
		screens.add(screen);
	}

	public LinkedList<Screen> getScreens() {
		return screens;
	}

	public int getScreenCount() {
		return screens.size();
	}

	public void setScreen(final Screen screen) {
		if (GLEx.gl == null || LSystem.isLogo) {
			tmp = screen;
		} else {
			setScreen(screen, true);
		}
	}

	private void setScreen(final Screen screen, boolean put) {
		synchronized (this) {
			if (screen == null) {
				this.isInstance = false;
				throw new RuntimeException(
						"Cannot create a [Screen] instance !");
			}
			if (!LSystem.isLogo) {
				if (currentControl != null) {
					setTransition(screen.onTransition());
				} else {
					LTransition transition = screen.onTransition();
					if (transition == null) {
						switch (LSystem.getRandomBetWeen(0, 3)) {
						case 0:
							transition = LTransition.newFadeIn();
							break;
						case 1:
							transition = LTransition.newArc();
							break;
						case 2:
							transition = LTransition
									.newSplitRandom(GLColor.black);
							break;
						case 3:
							transition = LTransition
									.newCrossRandom(GLColor.black);
							break;
						}
					}
					setTransition(transition);
				}
			}
			screen.setOnLoadState(false);
			if (currentControl == null) {
				currentControl = screen;
			} else {
				synchronized (currentControl) {
					currentControl.destroy();
					currentControl = screen;
				}
			}
			this.isInstance = true;
			if (screen instanceof EmulatorListener) {
				setEmulatorListener((EmulatorListener) screen);
			} else {
				setEmulatorListener(null);
			}
			startTransition();
			screen
					.onCreate(LSystem.screenRect.width,
							LSystem.screenRect.height);
			if (!running || GLEx.gl == null) {
				screen.setClose(false);
				screen.onLoad();
				screen.setOnLoadState(true);
				screen.onLoaded();
				endTransition();
			} else {
				Thread load = null;
				try {
					load = new Thread() {
						public void run() {
							for (; LSystem.isLogo;) {
								try {
									Thread.sleep(60);
								} catch (InterruptedException e) {
								}
							}
							screen.setClose(false);
							screen.onLoad();
							screen.setOnLoadState(true);
							screen.onLoaded();
							endTransition();
						}
					};
					load.setPriority(Thread.NORM_PRIORITY);
					load.start();
				} catch (Exception ex) {
					throw new RuntimeException(currentControl.getName()
							+ " onLoad:" + ex.getMessage());
				} finally {
					load = null;
				}
			}
			if (put) {
				screens.add(screen);
			}
			Thread.yield();
		}
	}

	public void keyDown(LKey e) {
		if (isInstance) {
			currentControl.keyPressed(e);
		}
	}

	public void keyUp(LKey e) {
		if (isInstance) {
			currentControl.keyReleased(e);
		}
	}

	public void keyTyped(LKey e) {
		if (isInstance) {
			currentControl.keyTyped(e);
		}
	}

	public void mousePressed(LTouch e) {
		if (isInstance) {
			currentControl.mousePressed(e);
		}
	}

	public void mouseReleased(LTouch e) {
		if (isInstance) {
			currentControl.mouseReleased(e);
		}
	}

	public void mouseMoved(LTouch e) {
		if (isInstance) {
			currentControl.mouseMoved(e);
		}
	}

	public void mouseDragged(LTouch e) {
		if (isInstance) {
			currentControl.mouseDragged(e);
		}
	}

	public GameScene getScene() {
		return scene;
	}

	public void setScene(GameScene scene) {
		this.scene = scene;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public void onDestroy() {
		endTransition();
		if (isInstance) {
			isInstance = false;
			if (currentControl != null) {
				currentControl.destroy();
				currentControl = null;
			}
			LTextures.disposeAll();
			LImage.disposeAll();
			ScreenUtils.disposeAll();
		}
	}

	public LInputFactory getInput() {
		return currentInput;
	}

}
