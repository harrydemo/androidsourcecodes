package org.loon.act.test;

import org.loon.act.test.Map.Coin;
import org.loon.act.test.Map.Item;
import org.loon.framework.android.game.core.EmulatorButtons;
import org.loon.framework.android.game.core.EmulatorListener;
import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.core.geom.RectBox;
import org.loon.framework.android.game.core.graphics.LFont;
import org.loon.framework.android.game.core.graphics.Screen;
import org.loon.framework.android.game.core.graphics.opengl.GLColor;
import org.loon.framework.android.game.core.graphics.opengl.GLEx;
import org.loon.framework.android.game.core.graphics.opengl.LTexture;
import org.loon.framework.android.game.core.input.LKey;
import org.loon.framework.android.game.core.input.LTouch;
import org.loon.framework.android.game.core.input.LInputFactory.Key;
import org.loon.framework.android.game.core.timer.LTimer;
import org.loon.framework.android.game.core.timer.LTimerContext;
import org.loon.framework.android.game.utils.NumberUtils;
import org.loon.framework.android.game.utils.TextureUtils;

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
public class ACTTest extends Screen {

	final class ACTKey {

		public static final int MODE_NORMAL = 0;

		public static final int MODE_PRESS_ONLY = 1;

		private static final int STATE_RELEASED = 0;

		private static final int STATE_PRESSED = 1;

		private static final int STATE_WAITING_FOR_RELEASE = 2;

		private int mode;

		private int amount;

		private int state;

		public ACTKey() {
			this(MODE_NORMAL);
		}

		public ACTKey(int mode) {
			this.mode = mode;
			this.reset();
		}

		public void reset() {
			this.state = 0;
			this.amount = 0;
		}

		public void press() {
			if (this.state != STATE_WAITING_FOR_RELEASE) {
				++this.amount;
				this.state = STATE_PRESSED;
			}

		}

		public void release() {
			this.state = 0;
		}

		public boolean isPressed() {
			if (this.amount != 0) {
				if (this.state == STATE_RELEASED) {
					this.amount = 0;
				} else if (this.mode == MODE_PRESS_ONLY) {
					this.state = STATE_WAITING_FOR_RELEASE;
					this.amount = 0;
				}
				return true;
			} else {
				return false;
			}
		}
	}

	private int s_X;

	private int s_Y;

	private String map_st;

	private LTexture blockImg;

	private LTexture iboxImg;

	private LTexture coinImg;

	private LTexture heroImg;

	private LTexture stickImg;

	private LTexture upbarImg;

	private LTexture underbarImg;

	private LTexture enemyImgE;

	private LTexture enemyImgF;

	private LTexture springImg;

	private LTexture goalImg;

	private LTexture titleStImg;

	private LTexture retryStImg;

	private LTexture gaugeImg;

	private LTexture itemImg;

	private LTexture halfImg;

	private LTexture tile1Img;

	private LTexture tileImg;

	private LTexture backImg;

	private LTexture back2img;

	private LTexture back3Img;

	private LTexture hitImg;

	private LTexture numImg;

	private LTexture poseImg;

	private int countClick;

	private boolean onClick;

	private int cNum;

	private int score;

	private int time;

	private int life;

	private int combo;

	private int combo_h;

	private int stage;

	private long nowTime;

	private long nextTime;

	private int dGauge;

	private boolean dashf;

	private long w_nowTime;

	private long w_nextTime;

	private boolean w_timef;

	private long[] ew_nowTime;

	private long[] ew_nextTime;

	private boolean[] ew_timef;

	private long d_nowTime;

	private long d_nextTime;

	private boolean d_timef;

	private long com_nowTime;

	private long com_nextTime;

	private boolean com_timef;

	private long hit_nowTime;

	private long hit_nextTime;

	private boolean enterf;

	private boolean d_se;

	private boolean posef;

	private int gameST;

	private int cursorPos;

	private int cursorCount;

	private Map map;

	private Coin coin;

	private Item item;

	private Enemy enemy;

	private Player player;

	public static final int TITLE = 11;

	public static final int GAME_S = 21;

	public static final int GAME = 31;

	public static final int GAME_OVER = 41;

	public static final int GAME_CLEAR = 51;

	public static final int SCORE = 61;

	public static final int MENU = 81;

	public static final int SHO = 91;

	public static final String PASS = "";

	public static final String PASS2 = "/";

	public static final int NORMAL_JUMP = 0;

	public static final int E_DETH_JUMP = 1;

	private ACTKey enterKey;

	private ACTKey leftKey;

	private ACTKey rightKey;

	private ACTKey dashKey;

	private ACTKey jumpKey;

	private ACTKey downKey;

	private ACTKey poseKey;

	private EmulatorListener emulatorListener;

	private boolean initGame;

	public void onLoad() {

		LTexture.AUTO_LINEAR();
		this.gameST = MENU;
		this.cursorPos = 0;
		this.cursorCount = 0;
		this.stage = 0;
		this.w_nowTime = System.currentTimeMillis();
		this.backImg = new LTexture("assets/tra.png");
		this.back2img = new LTexture("assets/city.png");
		this.back3Img = new LTexture("assets/cave.png");
		this.poseImg = new LTexture("assets/pose.png");
		this.enterf = true;
		this.leftKey = new ACTKey();
		this.rightKey = new ACTKey();
		this.dashKey = new ACTKey();
		this.enterKey = new ACTKey(1);
		this.jumpKey = new ACTKey(1);
		this.downKey = new ACTKey(1);
		this.poseKey = new ACTKey(1);
	}

	public synchronized void draw(GLEx g) {
		if (!isOnLoadComplete()) {
			return;
		}
		if (gameST != MENU && !initGame) {
			this.blockImg = new LTexture("assets/block.png");
			this.iboxImg = new LTexture("assets/ibox.png");
			this.coinImg = new LTexture("assets/coin.png");
			this.heroImg = TextureUtils.filterColor("assets/hero.png",
					GLColor.black);
			this.upbarImg = new LTexture("assets/upbar.png");
			this.underbarImg = new LTexture("assets/underbar.png");
			this.enemyImgE = TextureUtils.filterColor("assets/e1.png",
					GLColor.black);
			this.enemyImgF = TextureUtils.filterColor("assets/e2.png",
					GLColor.black);
			this.springImg = new LTexture("assets/spring.png");
			this.goalImg = new LTexture("assets/goal.png");
			this.titleStImg = new LTexture("assets/title_st.png");
			this.retryStImg = new LTexture("assets/retry_st.png");
			this.gaugeImg = new LTexture("assets/bar2.png");
			this.itemImg = new LTexture("assets/honey.png");
			this.halfImg = new LTexture("assets/half2.png");
			this.tileImg = new LTexture("assets/tile.png");
			this.tile1Img = new LTexture("assets/tile1.png");
			this.stickImg = new LTexture("assets/stick.png");
			this.hitImg = new LTexture("assets/hit.png");
			this.numImg = new LTexture("assets/num.png");
			if (emulatorListener == null) {
				emulatorListener = new EmulatorListener() {

					public void onCancelClick() {
						poseKey.press();

					}

					public void onCircleClick() {

					}

					public void onDownClick() {
						downKey.press();
					}

					public void onLeftClick() {
						leftKey.press();
					}

					public void onRightClick() {
						rightKey.press();
					}

					public void onUpClick() {
						jumpKey.press();
					}

					public void onSquareClick() {
						enterKey.press();
					}

					public void onTriangleClick() {
						dashKey.press();
					}

					public void unCancelClick() {
						poseKey.release();
					}

					public void unCircleClick() {

					}

					public void unDownClick() {
						downKey.release();
					}

					public void unLeftClick() {
						leftKey.release();
					}

					public void unRightClick() {
						rightKey.release();
					}

					public void unSquareClick() {
						enterKey.release();
					}

					public void unTriangleClick() {
						dashKey.release();
					}

					public void unUpClick() {
						jumpKey.release();
					}

				};
				setEmulatorListener(emulatorListener);
				EmulatorButtons ebs = getEmulatorButtons();
				if (ebs != null) {
					ebs.getCircle().disable(true);
					ebs.getTriangle().setY(ebs.getTriangle().getY() + 120);
					ebs.getSquare().setY(ebs.getSquare().getY() + 110);
					ebs.getCancel().setY(ebs.getCancel().getY() + 90);
				}
			}
			this.initGame = true;
		} else if (gameST == MENU) {
			setRepaintMode(SCREEN_CANVAS_REPAINT);
			EmulatorButtons ebs = getEmulatorButtons();
			if (ebs != null && ebs.isVisible()) {
				ebs.setVisible(false);
			}
		} else if (gameST != MENU) {
			setRepaintMode(SCREEN_NOT_REPAINT);
			EmulatorButtons ebs = getEmulatorButtons();
			if (ebs != null && !ebs.isVisible()) {
				ebs.setVisible(true);
			}
		}
		if (timer.action(super.elapsedTime)) {

			switch (this.gameST) {

			case 21:
				this.map = new Map(this.blockImg, this.stickImg, this.iboxImg,
						this.springImg, this.goalImg, this.halfImg,
						this.tile1Img, this.tileImg, this.map_st);
				this.coin = new Coin(this.coinImg, this.map);
				this.item = new Item(this.itemImg, this.map);
				this.enemy = new Enemy(this.enemyImgE, this.enemyImgF, this.map);
				this.player = new Player(this.s_X, this.s_Y, this.heroImg,
						this.map);
				this.ew_nowTime = new long[this.map.getECount()];
				this.ew_nextTime = new long[this.map.getECount()];
				this.ew_timef = new boolean[this.map.getECount()];
				this.combo = 1;
				this.combo_h = 0;
				this.score = 0;
				this.cNum = 0;
				if (this.map_st == "map_h.dat") {
					this.life = 5;
				} else {
					this.life = 3;
				}

				this.time = 401000;
				this.nowTime = System.currentTimeMillis();
				this.nextTime = 0L;
				this.dGauge = 200;
				this.dashf = false;
				this.w_nowTime = 0L;
				this.w_nextTime = 0L;
				this.w_timef = false;
				this.d_nowTime = 0L;
				this.d_nextTime = 0L;
				this.d_timef = false;
				this.com_nowTime = 0L;
				this.com_nextTime = 0L;
				this.com_timef = false;
				this.hit_nowTime = 0L;
				this.hit_nextTime = 0L;
				this.d_se = false;

				this.posef = false;

				this.gameST = 31;
				break;
			case 31:
				if (this.poseKey.isPressed()) {
					if (this.posef) {
						this.posef = false;
						this.nowTime = System.currentTimeMillis();
						this.w_nowTime = System.currentTimeMillis();
					} else {
						this.posef = true;
					}
				}

				if (this.posef) {
					this.cursorCount = 2;
					if (this.enterKey.isPressed()) {
						switch (this.cursorPos) {
						case 0:
							ACTWavSound.getInstance().enter();
							this.gameST = 21;
							break;
						case 1:
							ACTWavSound.getInstance().enter();
							this.gameST = MENU;
						}

						this.map.setClearf();
						ACTWavSound.getInstance().enter();
						this.map = new Map(this.blockImg, this.stickImg,
								this.iboxImg, this.springImg, this.goalImg,
								this.halfImg, this.tile1Img, this.tileImg,
								this.map_st);
						this.coin = new Coin(this.coinImg, this.map);
						this.item = new Item(this.itemImg, this.map);
						this.enemy = new Enemy(this.enemyImgE, this.enemyImgF,
								this.map);
						this.player = new Player(this.s_X, this.s_Y,
								this.heroImg, this.map);
					}

					if (this.jumpKey.isPressed()) {
						ACTWavSound.getInstance().select();
						--this.cursorPos;
						if (this.cursorPos < 0) {
							this.cursorPos = this.cursorCount - 1;
						}
					} else if (this.downKey.isPressed()) {
						ACTWavSound.getInstance().select();
						++this.cursorPos;
						if (this.cursorPos > this.cursorCount - 1) {
							this.cursorPos = 0;
						}
					}
				}

				if (!this.player.getDeth() && !this.posef) {
					if (this.leftKey.isPressed()) {
						this.player.leftmove();
					} else if (this.rightKey.isPressed()) {
						this.player.rightmove();
					} else {
						this.player.stop();
					}

					if (this.jumpKey.isPressed()) {
						this.player.jump(0);
					}

					if (this.downKey.isPressed()) {
						if (this.d_timef) {
							this.d_nextTime = System.currentTimeMillis();
							if (this.d_nextTime - this.d_nowTime <= 500L) {
								this.player.onDown();
								this.d_timef = false;
							} else if (this.d_nextTime - this.d_nowTime > 500L) {
								this.d_timef = false;
							}
						} else {
							this.d_nowTime = System.currentTimeMillis();
							this.d_timef = true;
						}
					}

					this.player.dash(this.dashf);
				}

				if (!this.posef) {
					if (this.w_timef) {
						if (this.w_nextTime - this.w_nowTime > 500L) {
							this.player.update();
						}
					} else {
						this.player.update();
					}

					if (!this.player.getDeth()) {
						int e;
						for (e = 0; e < this.map.getECount(); ++e) {
							this.enemy.update(e);
						}

						for (e = 0; e < this.map.getICount(); ++e) {
							this.item.update(e);
						}

						if (this.dashKey.isPressed() && this.dGauge > 0) {
							if (!this.d_se) {
								ACTWavSound.getInstance().dash();
								this.d_se = true;
							}

							this.dGauge -= 3;
							this.dashf = true;
						} else {
							this.dashf = false;
						}

						if (!this.dashKey.isPressed() && this.dGauge < 200) {
							this.d_se = false;
							this.dGauge += 2;
						}

						for (e = this.coin.getFirstY(); e < this.coin
								.getLastY(); ++e) {
							for (int j = this.coin.getFirstX(); j < this.coin
									.getLastX(); ++j) {
								if (this.coin.collideWith(this.player, e, j)) {
									this.coin.del(e, j);
									++this.cNum;
									if (this.cNum == 100) {
										ACTWavSound.getInstance().up();
										this.cNum = 0;
										++this.life;
									} else {
										ACTWavSound.getInstance().coin();
									}
								}
							}
						}

						for (e = 0; e < this.map.getICount(); ++e) {
							if (this.map.getCount(e) == 1
									&& this.item.collideWith(this.player, e)) {
								this.score += 1000;
								ACTWavSound.getInstance().item();
								this.item.del(e);
								this.player.setItemGetf(true);
							}
						}

						for (e = 0; e < this.map.getECount(); ++e) {
							if ((!this.enemy.collideWith(this.player, e) || this.player
									.getY() >= this.enemy.getY(e))
									&& !this.ew_timef[e]) {
								if (this.enemy.collideWith(this.player, e)
										&& !this.player.getTimef()) {
									if (!this.player.getItemGetf()) {
										this.player.setDethf(true);
										this.player.dethJump();
										ACTWavSound.getInstance().deth();
										this.w_nowTime = System
												.currentTimeMillis();
										this.w_timef = true;
									} else {
										ACTWavSound.getInstance().item_d();
										this.player.setItemGetf(false);
										this.player.setNowTime(System
												.currentTimeMillis());
										this.player.setTimef(true);
									}
								}
							} else if (this.ew_timef[e]) {
								this.ew_nextTime[e] = System
										.currentTimeMillis();
								if (this.enemy.getDethf(e)
										&& this.ew_nextTime[e]
												- this.ew_nowTime[e] > 1000L) {
									this.enemy.del(e);
									this.ew_timef[e] = false;
								} else if (!this.enemy.getDethf(e)) {
									this.ew_timef[e] = false;
								}
							} else {
								if (this.com_timef) {
									this.com_nextTime = System
											.currentTimeMillis();
									if (this.com_nextTime - this.com_nowTime < 2500L) {
										++this.combo;
										this.com_nowTime = this.com_nextTime;
									} else {
										this.combo = 1;
										this.com_nowTime = this.com_nextTime;
									}
								} else {
									this.com_nowTime = System
											.currentTimeMillis();
									this.com_timef = true;
								}

								if (this.combo > this.combo_h) {
									this.combo_h = this.combo;
								}

								if (this.combo >= 20) {
									if (this.combo % 5 == 0) {
										++this.life;
										ACTWavSound.getInstance().up();
									} else {
										ACTWavSound.getInstance().e_deth();
									}
								} else {
									ACTWavSound.getInstance().e_deth();
								}

								if (this.enemy.getScore(e) * this.combo > 1000) {
									this.score += 1000;
								} else {
									this.score += this.enemy.getScore(e)
											* this.combo;
								}

								this.enemy.setCombo(e, this.combo);
								this.player.jump(1);
								this.enemy.setStf(e);
								this.ew_nowTime[e] = System.currentTimeMillis();
								this.hit_nowTime = System.currentTimeMillis();
								this.ew_timef[e] = true;
							}
						}
					}

					if (this.player.getDeth()) {
						if (this.w_timef) {
							this.w_nextTime = System.currentTimeMillis();
							if (this.w_nextTime - this.w_nowTime > 2000L) {
								this.reset();
								this.w_timef = false;
							}
						} else {
							ACTWavSound.getInstance().deth();
							this.w_nowTime = System.currentTimeMillis();
							this.w_timef = true;
						}
					}

					if (this.time / 1000 <= 0) {
						if (this.w_timef) {
							this.w_nextTime = System.currentTimeMillis();
							if (this.w_nextTime - this.w_nowTime > 2000L) {
								this.reset();
								this.w_timef = false;
							}
						} else {
							this.player.setDethf(true);
							this.player.dethJump();
							this.w_nowTime = System.currentTimeMillis();
							this.w_timef = true;
						}
					}

					if (this.map.getClear()) {
						if (this.w_timef) {
							this.w_nextTime = System.currentTimeMillis();
							if (this.w_nextTime - this.w_nowTime > 1000L) {
								this.gameST = 51;
								this.w_nowTime = System.currentTimeMillis();
							}
						} else {
							ACTWavSound.getInstance().goal();
							this.w_nowTime = System.currentTimeMillis();
							this.w_timef = true;
						}
					}
				}
				break;
			case 41:
				if (this.enterKey.isPressed()) {
					ACTWavSound.getInstance().enter();
					this.gameST = 61;
					this.cursorPos = 0;
				}
				break;
			case 51:
				if (this.enterKey.isPressed()) {
					ACTWavSound.getInstance().enter();
					this.gameST = 61;
					this.cursorPos = 0;
				}
				break;
			case 61:
				this.cursorCount = 0;
				ACTWavSound.getInstance().enter();
				this.map.setClearf();
				ACTWavSound.getInstance().enter();
				this.map = new Map(this.blockImg, this.stickImg, this.iboxImg,
						this.springImg, this.goalImg, this.halfImg,
						this.tile1Img, this.tileImg, this.map_st);
				this.coin = new Coin(this.coinImg, this.map);
				this.item = new Item(this.itemImg, this.map);
				this.enemy = new Enemy(this.enemyImgE, this.enemyImgF, this.map);
				this.player = new Player(this.s_X, this.s_Y, this.heroImg,
						this.map);
			case MENU:
				this.cursorCount = 5;
				if (this.enterKey.isPressed()) {
					switch (this.cursorPos) {
					case 1:
						ACTWavSound.getInstance().enter();
						this.map_st = "map_t.dat";
						this.stage = 0;
						this.s_X = 32;
						this.s_Y = LSystem.screenRect.width;
						this.gameST = 21;
						break;
					case 2:
						ACTWavSound.getInstance().enter();
						this.map_st = "map_e.dat";
						this.stage = 1;
						this.s_X = 64;
						this.s_Y = 64;
						this.gameST = 21;
						break;
					case 3:
						ACTWavSound.getInstance().enter();
						this.map_st = "map.dat";
						this.stage = 2;
						this.s_X = 64;
						this.s_Y = 1536;
						this.gameST = 21;
						break;
					case 4:
						ACTWavSound.getInstance().enter();
						this.map_st = "map_h.dat";
						this.stage = 3;
						this.s_X = 64;
						this.s_Y = 64;
						this.gameST = 21;
					}
				}

				if (this.jumpKey.isPressed()) {
					ACTWavSound.getInstance().select();
					--this.cursorPos;
					if (this.cursorPos < 0) {
						this.cursorPos = this.cursorCount - 1;
					}
				} else if (this.downKey.isPressed()) {
					ACTWavSound.getInstance().select();
					++this.cursorPos;
					if (this.cursorPos > this.cursorCount - 1) {
						this.cursorPos = 0;
					}
				}
				break;
			}
		}

		switch (this.gameST) {

		case 31:
		case 41:
			switch (stage) {
			case 0:
				g.drawTexture(this.backImg, 0, 0);
				break;
			case 1:
				g.drawTexture(this.back2img, 0, 0);
				break;
			case 2:
				g.drawTexture(this.back2img, 0, 0);
				break;
			case 3:
				g.drawTexture(this.back3Img, 0, 0);
				break;
			}
			int offsetX = 320 - this.player.getX();
			offsetX = Math.min(offsetX, 0);
			offsetX = Math.max(offsetX, LSystem.screenRect.width
					- this.map.getWidth());
			int offsetY = 224 - this.player.getY();
			offsetY = Math.min(offsetY, 0);
			offsetY = Math.max(offsetY, LSystem.screenRect.height
					- this.map.getHeight() - 32);
			this.coin.draw(super.elapsedTime, g, offsetX, offsetY);
			this.item.draw(g, offsetX, offsetY);
			if (this.player.getDeth()) {
				this.map.draw(g, offsetX, offsetY);
				this.enemy.draw(super.elapsedTime, g, offsetX, offsetY);
				if (this.gameST == 31) {
					this.player.draw(super.elapsedTime, g, offsetX, offsetY);
				}
			} else {
				if (this.gameST == 31
						&& (!this.player.getTimef() || (this.player
								.getNextTimef() - this.player.getNowTime()) % 10L >= 2L)) {
					this.player.draw(super.elapsedTime, g, offsetX, offsetY);
				}

				this.map.draw(g, offsetX, offsetY);
				this.enemy.draw(super.elapsedTime, g, offsetX, offsetY);
				this.enemy.drawScore(g, offsetX, offsetY);
			}

			if (getFPS() >= 15) {
				g.drawTexture(this.upbarImg, 0, 0);
				g.setColor(GLColor.white);
				g.setFont(LFont.getDefaultFont());
				g.drawWestString("" + this.life, 16, 24);
				g.drawWestString(NumberUtils.addZeros(this.score, 8), 105, 24);

				if (this.cNum < 10) {
					g.drawString("0" + this.cNum, 289, 24);
				} else if (this.cNum < 100) {
					g.drawString("" + this.cNum, 289, 24);
				}

				if (this.time / 1000 > 0) {
					if (this.gameST == 31 && !this.posef) {
						this.nextTime = System.currentTimeMillis();
						this.time = (int) ((long) this.time - (this.nextTime - this.nowTime));
						this.nowTime = this.nextTime;
					}
					if (this.time / 1000 < 100) {
						g.setColor(GLColor.red);
					}
					if (this.time / 1000 < 10) {
						g.drawString("00" + this.time / 1000, 400, 24);
					} else if (this.time / 1000 < 100) {
						g.drawString("0" + this.time / 1000, 400, 24);
					} else {
						g.drawString("" + this.time / 1000, 400, 24);
					}
				} else {
					g.setColor(GLColor.red);
					g.drawString("000", 400, 24);
				}
				g.setFont(LFont.getFont(15));
				this.hit_nextTime = System.currentTimeMillis();
				if (this.hit_nextTime - this.hit_nowTime < 2500L) {
					if (this.combo < 10) {
						g.drawTexture(this.numImg, 370, 40, 32, 32,
								this.combo * 32, 0, this.combo * 32 + 32, 32);
						g.drawTexture(this.hitImg, 400, 44);
					} else if (this.combo >= 10) {
						g.drawTexture(this.numImg, 370, 40, 32, 32,
								this.combo % 10 * 32, 0,
								this.combo % 10 * 32 + 32, 32);
						g.drawTexture(this.numImg, 347, 40, 32, 32,
								this.combo / 10 * 32, 0,
								this.combo / 10 * 32 + 32, 32);
						g.drawTexture(this.hitImg, 400, 44);
					}
				}

				int top = getHeight() - 32;

				g.setColor(GLColor.darkGray);
				g.fillRect(0, top, LSystem.screenRect.width, 32);
				g.resetColor();
				if (this.dGauge < 75) {
					g.drawTexture(this.gaugeImg, 57, top, this.dGauge, 385, 0,
							32, this.dGauge, 64);
				} else if (this.dashf) {
					g.drawTexture(this.gaugeImg, 57, top, this.dGauge, 32, 0,
							64, this.dGauge, 96);
				} else {
					g.drawTexture(this.gaugeImg, 57, top, this.dGauge, 32, 0,
							0, this.dGauge, 32);
				}

				g.drawTexture(this.underbarImg, 0, getHeight()
						- underbarImg.getHeight());
				if (this.player.getItemGetf()) {
					g.drawTexture(this.itemImg, getWidth() - 33,
							getHeight() - 24, 16, 16, 0, 0, 32, 32);
				}

				g.setFont(LFont.getDefaultFont());
				g.setColor(GLColor.white);
				g.drawWestString("STAGE " + this.stage, 300, getHeight() - 8);
				if (this.posef) {
					g.drawTexture(this.poseImg, 0, 0);
					g.drawString("[X] CANCEL", 170, 160);
					if (this.cursorPos == 0) {
						g.drawTexture(this.retryStImg, 205, 220, 96, 32, 0, 32,
								96, 64);
					} else {
						g.drawTexture(this.retryStImg, 205, 220, 96, 32, 0, 0,
								96, 32);
					}
					if (this.cursorPos == 1) {
						g.drawTexture(this.titleStImg, 202, 270, 96, 32, 0, 32,
								96, 64);
					} else {
						g.drawTexture(this.titleStImg, 202, 270, 96, 32, 0, 0,
								96, 32);
					}
				}

				if (this.gameST == 41) {
					this.w_nextTime = System.currentTimeMillis();
					if (this.w_nextTime - this.w_nowTime > 600L) {
						if (this.enterf) {
							this.enterf = false;
							this.w_nowTime = this.w_nextTime;
						} else {
							this.enterf = true;
							this.w_nowTime = this.w_nextTime;
						}
					}
				}
			}
			break;
		case 51:
			this.w_nextTime = System.currentTimeMillis();
			if (this.w_nextTime - this.w_nowTime > 600L) {
				if (this.enterf) {
					this.enterf = false;
					this.w_nowTime = this.w_nextTime;
				} else {
					this.enterf = true;
					this.w_nowTime = this.w_nextTime;
				}
			}
			break;

		case MENU:
			drawMenu(g);
			break;
		}

	}

	private LTexture howWinImg;

	private LTexture howPlayImg;

	private LTexture traiPlayImg;

	private LTexture traiWinImg;

	private LTexture stage1Img;

	private LTexture stage2Img;

	private LTexture stage3Img;

	private LTexture stage1WinImg;

	private LTexture stage2WinImg;

	private LTexture stage3WinImg;

	private RectBox howButton;

	private RectBox traiButton;

	private RectBox s1Button;

	private RectBox s2Button;

	private RectBox s3Button;

	private boolean initMenu;

	private int menuStateX;

	private int menuStateY;

	final void drawMenu(GLEx g) {
		if (!initMenu) {
			int menuLeft = 5;
			int menuTop = 15;
			int width = 150;
			int height = 40;
			this.menuStateX = menuLeft + width;
			this.menuStateY = 5;
			this.howButton = new RectBox(menuLeft, menuTop, width, height);
			menuTop += height;
			this.traiButton = new RectBox(menuLeft, menuTop, width, height);
			menuTop += height;
			this.s1Button = new RectBox(menuLeft, menuTop, width, height);
			menuTop += height;
			this.s2Button = new RectBox(menuLeft, menuTop, width, height);
			menuTop += height;
			this.s3Button = new RectBox(menuLeft, menuTop, width, height);
			menuTop += height;
			this.howWinImg = new LTexture("assets/how_win.png");
			this.howPlayImg = new LTexture("assets/how.png");
			this.traiPlayImg = new LTexture("assets/trai.png");
			this.traiWinImg = new LTexture("assets/trai_win.png");
			this.stage1Img = new LTexture("assets/stage1.png");
			this.stage2Img = new LTexture("assets/stage2.png");
			this.stage3Img = new LTexture("assets/stage3.png");
			this.stage1WinImg = new LTexture("assets/stage1_win.png");
			this.stage2WinImg = new LTexture("assets/stage2_win.png");
			this.stage3WinImg = new LTexture("assets/stage3_win.png");
			this.initMenu = true;
		}

		if (this.cursorPos == 0) {
			g.drawTexture(this.howWinImg, menuStateX, menuStateY);
			g.drawTexture(this.howPlayImg, howButton.x, howButton.y,
					howButton.width, howButton.height, 0, howButton.height,
					howButton.width, howButton.height * 2);
		} else {
			g.drawTexture(this.howPlayImg, howButton.x, howButton.y,
					howButton.width, howButton.height, 0, 0, howButton.width,
					howButton.height);
		}

		if (this.cursorPos == 1) {
			g.drawTexture(this.traiWinImg, menuStateX, menuStateY);
			g.drawTexture(this.traiPlayImg, traiButton.x, traiButton.y,
					traiButton.width, traiButton.height, 0, traiButton.height,
					traiButton.width, traiButton.height * 2);
		} else {
			g.drawTexture(this.traiPlayImg, traiButton.x, traiButton.y,
					traiButton.width, traiButton.height, 0, 0,
					traiButton.width, traiButton.height);
		}

		if (this.cursorPos == 2) {
			g.drawTexture(this.stage1WinImg, menuStateX, menuStateY);
			g.drawTexture(this.stage1Img, s1Button.x, s1Button.y,
					s1Button.width, s1Button.height, 0, s1Button.height,
					s1Button.width, s1Button.height * 2);
		} else {
			g.drawTexture(this.stage1Img, s1Button.x, s1Button.y,
					s1Button.width, s1Button.height, 0, 0, s1Button.width,
					s1Button.height);
		}

		if (this.cursorPos == 3) {
			g.drawTexture(this.stage2WinImg, menuStateX, menuStateY);
			g.drawTexture(this.stage2Img, s2Button.x, s2Button.y,
					s2Button.width, s2Button.height, 0, s2Button.height,
					s2Button.width, s2Button.height * 2);
		} else {
			g.drawTexture(this.stage2Img, s2Button.x, s2Button.y,
					s2Button.width, s2Button.height, 0, 0, s2Button.width,
					s2Button.height);
		}

		if (this.cursorPos == 4) {
			g.drawTexture(this.stage3WinImg, menuStateX, menuStateY);
			g.drawTexture(this.stage3Img, s3Button.x, s3Button.y,
					s3Button.width, s3Button.height, 0, s3Button.height,
					s3Button.width, s3Button.height * 2);
		} else {
			g.drawTexture(this.stage3Img, s3Button.x, s3Button.y,
					s3Button.width, s3Button.height, 0, 0, s3Button.width,
					s3Button.height);
		}

	}

	public void onKeyDown(LKey e) {

		int key = e.getKeyCode();
		if (key == Key.ENTER) {
			this.enterKey.press();
		}

		if (key == Key.DPAD_LEFT) {
			this.leftKey.press();
		}

		if (key == Key.DPAD_RIGHT) {
			this.rightKey.press();
		}

		if (key == Key.SPACE || key == Key.UP) {
			this.jumpKey.press();
		}

		if (key == Key.DPAD_DOWN) {
			this.downKey.press();
		}

		if (key == Key.S) {
			this.dashKey.press();
		}

		if (key == Key.X) {
			this.poseKey.press();
		}
	}

	public void onKeyUp(LKey e) {

		int key = e.getKeyCode();
		if (key == Key.ENTER) {
			this.enterKey.release();
		}

		if (key == Key.DPAD_LEFT) {
			this.leftKey.release();
		}

		if (key == Key.DPAD_RIGHT) {
			this.rightKey.release();
		}

		if (key == Key.SPACE || key == Key.UP) {
			this.jumpKey.release();
		}

		if (key == Key.DPAD_DOWN) {
			this.downKey.release();
		}

		if (key == Key.S) {
			this.dashKey.release();
		}

		if (key == Key.X) {
			this.poseKey.release();
		}
	}

	private LTimer timer = new LTimer(60);

	public void reset() {
		--this.life;
		if (this.score > 100000) {
			this.score = (int) ((double) this.score - (double) this.score * 0.3D);
		} else {
			this.score = (int) ((double) this.score - (double) this.score * 0.2D);
		}

		if (this.score < 0) {
			this.score = 0;
		}

		if (this.life < 0) {
			++this.life;
			this.gameST = 41;
			this.w_nowTime = System.currentTimeMillis();
		} else {
			this.time = 401000;
			this.player.setDethf(false);
			this.dGauge = 200;
			this.player.deth();
			this.player.setItemGetf(false);
			this.enemy = new Enemy(this.enemyImgE, this.enemyImgF, this.map);
			this.item = new Item(this.itemImg, this.map);
			this.map.countReset();
		}

	}

	public void alter(LTimerContext timer) {

	}

	public void touchDown(final LTouch e) {
		onClick = false;

		switch (gameST) {
		case MENU:
			if (howButton != null) {
				if (howButton.contains(e.x(), e.y())) {
					cursorPos = 0;
				}
			}
			if (traiButton != null) {
				if (traiButton.contains(e.x(), e.y())) {
					onClick = true;
					cursorPos = 1;
				}
			}
			if (s1Button != null) {
				if (s1Button.contains(e.x(), e.y())) {
					onClick = true;
					cursorPos = 2;
				}
			}
			if (s2Button != null) {
				if (s2Button.contains(e.x(), e.y())) {
					onClick = true;
					cursorPos = 3;
				}
			}
			if (s3Button != null) {
				if (s3Button.contains(e.x(), e.y())) {
					onClick = true;
					cursorPos = 4;
				}
			}
			break;
		}

		if (onClick) {
			if (countClick > 0) {
				enterKey.press();
				countClick = 0;
			}
			countClick++;
		}

	}

	public void touchMove(LTouch e) {

	}

	public void touchUp(LTouch e) {
		if (enterKey.isPressed()) {
			enterKey.release();
		}
	}

}
