package org.loon.framework.android.game.action.sprite.effect;

import org.loon.framework.android.game.action.sprite.ISprite;
import org.loon.framework.android.game.core.LObject;
import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.core.geom.RectBox;
import org.loon.framework.android.game.core.graphics.opengl.GL;
import org.loon.framework.android.game.core.graphics.opengl.GLColor;
import org.loon.framework.android.game.core.graphics.opengl.GLEx;
import org.loon.framework.android.game.core.graphics.opengl.LTexture;
import org.loon.framework.android.game.core.timer.LTimer;
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
//自0.3.2版新增的游戏特效，用以模拟昼夜交替效果(默认触发事件较慢，可调整setDelay函数改变速度)
public class DayNightEffect extends LObject implements ISprite {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int DAY_DAWN = 0;

	public static final int DAY_NEXT = 4;

	public static final int EVENING = 16;

	public static final int NIGHT = 20;

	public static final int ENDOFDAY = 29;

	private int dayTime = 12;

	private int nextDayTime = 1000;

	private int counterDayTime = 0;

	private int dawnCounter = 0;

	private int dawnSpan = 0;

	private LTexture whiteSquare;

	private float[][][] roughLightValues;

	private float[][][] smoothLightValues;

	private int mapWidth, mapHeight, mapTileSize;

	private LTimer timer;

	private boolean visible;

	public DayNightEffect(float x, float y, int size) {
		this.setLocation(x, y);
		this.mapTileSize = size;
		this.mapWidth = LSystem.screenRect.width / size;
		this.mapHeight = LSystem.screenRect.height / size;
		this.roughLightValues = new float[mapWidth + 1][mapHeight + 1][4];
		this.smoothLightValues = new float[mapWidth + 1][mapHeight + 1][4];
		this.timer = new LTimer(240);
		this.visible = true;
	}

	public void setDelay(long d) {
		timer.setDelay(d);
	}

	public long getDelay() {
		return timer.getDelay();
	}

	public void update(long elapsedTime) {
		if (timer.action(elapsedTime)) {
			counterDayTime += elapsedTime;
			if (counterDayTime >= nextDayTime) {
				counterDayTime -= nextDayTime;
				dayTime++;
				if (dayTime > ENDOFDAY)
					dayTime = 0;
			}

			boolean dawn = false;
			if (dayTime >= DAY_DAWN && dayTime < DAY_NEXT) {
				dawn = fillLight(elapsedTime, -1, DAY_NEXT - DAY_DAWN);
				if (!dawn) {
					return;
				}
			} else if (dayTime >= EVENING && dayTime < NIGHT) {
				dawn = fillLight(elapsedTime, 1, NIGHT - EVENING);
			}
			for (int y = 0; y < mapHeight + 1; y++) {
				for (int x = 0; x < mapWidth + 1; x++) {
					if (!dawn) {
						for (int component = 0; component < 4; component++) {
							roughLightValues[x][y][component] = 0;
						}
					}
					for (int component = 0; component < 4; component++) {
						if (roughLightValues[x][y][component] > 1) {
							roughLightValues[x][y][component] = 1;
						}
					}
				}
			}
			float smooth = 1 / 9f;
			for (int x = 0; x < mapWidth + 1; x++) {
				for (int y = 0; y < mapHeight + 1; y++) {
					for (int i = 0; i < 3; i++) {
						smoothLightValues[x][y][i] = 0;
						for (int a = -1; a < 2; a++) {
							for (int b = -1; b < 2; b++) {
								if ((x + a) >= 0 && (y + b) >= 0
										&& (x + a) < mapWidth + 1
										&& (y + b) < mapHeight + 1) {
									smoothLightValues[x][y][i] += (roughLightValues[x
											+ a][y + b][i] * smooth);
								}
							}
						}
					}
					smoothLightValues[x][y][3] = roughLightValues[x][y][3];
				}
			}

		}
	}

	private boolean fillLight(long delta, int mul, int range) {
		if (this.dawnSpan == 0) {
			this.dawnSpan = range * 1000;
			if (mul == 1) {
				this.dawnCounter = 0;
			} else {
				this.dawnCounter = this.dawnSpan;
			}
		}
		this.dawnCounter += (delta * mul);
		float greyVal = 0.0f;
		if (this.dawnCounter <= 0) {
			this.dawnSpan = 0;
			this.dawnCounter = 0;
			return false;
		} else if (this.dawnCounter >= this.dawnSpan) {
			this.dawnSpan = 0;
			this.dawnCounter = 0;
			return false;
		} else {
			greyVal = ((float) this.dawnCounter) / ((float) this.dawnSpan);
		}
		for (int y = 0; y < mapHeight + 1; y++) {
			for (int x = 0; x < mapWidth + 1; x++) {
				for (int component = 0; component < 3; component++) {
					roughLightValues[x][y][component] = 0;
				}
				roughLightValues[x][y][3] = greyVal;
			}
		}
		return true;
	}

	public int getDayTime() {
		return dayTime;
	}

	public void setDayTime(int newDayTime) {
		dayTime = newDayTime;
	}

	public boolean isDay() {
		return dayTime >= DAY_DAWN && dayTime < NIGHT;
	}

	public boolean isNight() {
		return dayTime >= NIGHT || dayTime < DAY_DAWN;
	}

	public int getHeight() {
		return LSystem.screenRect.height;
	}

	public int getWidth() {
		return LSystem.screenRect.width;
	}

	public void createUI(GLEx g) {
		if (!visible) {
			return;
		}
		if (dayTime >= EVENING || dayTime < DAY_NEXT) {
			if (whiteSquare == null) {
				whiteSquare = TextureUtils.createTexture(mapTileSize,
						mapTileSize, GLColor.white);
			}
			g.glBegin(GL.GL_TRIANGLE_STRIP, false);
			for (int y = 0; y < mapHeight; y++) {
				for (int x = 0; x < mapWidth; x++) {
					whiteSquare.setColor(LTexture.TOP_LEFT,
							smoothLightValues[x][y][0],
							smoothLightValues[x][y][1],
							smoothLightValues[x][y][2],
							smoothLightValues[x][y][3]);
					whiteSquare.setColor(LTexture.TOP_RIGHT,
							smoothLightValues[x + 1][y][0],
							smoothLightValues[x + 1][y][1],
							smoothLightValues[x + 1][y][2],
							smoothLightValues[x + 1][y][3]);
					whiteSquare.setColor(LTexture.BOTTOM_RIGHT,
							smoothLightValues[x + 1][y + 1][0],
							smoothLightValues[x + 1][y + 1][1],
							smoothLightValues[x + 1][y + 1][2],
							smoothLightValues[x + 1][y + 1][3]);
					whiteSquare.setColor(LTexture.BOTTOM_LEFT,
							smoothLightValues[x][y + 1][0],
							smoothLightValues[x][y + 1][1],
							smoothLightValues[x][y + 1][2],
							smoothLightValues[x][y + 1][3]);
					whiteSquare.draw(x * mapTileSize, y * mapTileSize,
							mapTileSize, mapTileSize);
				}
			}
			g.glEnd();
		}
	}

	public float getAlpha() {
		return 1;
	}

	public LTexture getBitmap() {
		return null;
	}

	public RectBox getCollisionBox() {
		return null;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean v) {
		this.visible = v;
	}

	public void dispose() {
		if (whiteSquare != null) {
			whiteSquare.destroy();
			whiteSquare = null;
		}
	}


}

