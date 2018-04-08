package org.loon.framework.javase.game.action.sprite;

import org.loon.framework.javase.game.core.LObject;
import org.loon.framework.javase.game.core.geom.RectBox;
import org.loon.framework.javase.game.core.graphics.opengl.GLColor;
import org.loon.framework.javase.game.core.graphics.opengl.GLEx;
import org.loon.framework.javase.game.core.graphics.opengl.LTexture;
import org.loon.framework.javase.game.core.timer.LTimer;

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
public class Blood extends LObject implements ISprite {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	class Drop {
		public float x, y, xspeed, yspeed;
	}

	private float xSpeed, ySpeed, alpha = 1;

	private LTimer timer;

	private int step, limit;

	private Drop[] drops;

	private boolean visible;

	private GLColor color;

	public Blood(int x, int y) {
		this(GLColor.red, x, y);
	}

	public Blood(GLColor c, int x, int y) {
		this.setLocation(x, y);
		this.color = c;
		this.timer = new LTimer(20);
		this.drops = new Drop[20];
		this.limit = 50;
		for (int i = 0; i < drops.length; ++i) {
			setBoolds(i, x, y, 6.f * ((float) Math.random() - 0.5f), -2.0f
					* (float) Math.random());
		}
		this.xSpeed = 0F;
		this.ySpeed = 0.5F;
		this.step = 0;
		this.visible = true;
	}

	public void setBoolds(int index, float x, float y, float xs, float ys) {
		if (index > drops.length - 1) {
			return;
		}
		drops[index] = new Drop();
		drops[index].x = x;
		drops[index].y = y;
		drops[index].xspeed = xs;
		drops[index].yspeed = ys;
	}

	public void update(long elapsedTime) {
		if (timer.action(elapsedTime)) {
			for (int i = 0; i < drops.length; ++i) {
				drops[i].xspeed += xSpeed;
				drops[i].yspeed += ySpeed;
				drops[i].x -= drops[i].xspeed;
				drops[i].y += drops[i].yspeed;
			}
			step++;
			if (step > limit) {
				this.visible = false;
			}
		}
	}

	public void setDelay(long delay) {
		timer.setDelay(delay);
	}

	public long getDelay() {
		return timer.getDelay();
	}

	public void createUI(GLEx g) {
		if (!visible) {
			return;
		}
		if (alpha > 0 && alpha < 1) {
			g.setAlpha(alpha);
		}
		g.setColor(color);
		for (int i = 0; i < drops.length; ++i) {
			g.fillOval((int) drops[i].x, (int) drops[i].y, 2, 2);
		}
		g.resetColor();
		if (alpha > 0 && alpha < 1) {
			g.setAlpha(1);
		}
	}

	public GLColor getColor() {
		return color;
	}

	public void setColor(GLColor color) {
		this.color = color;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float a) {
		this.alpha = a;
	}

	public LTexture getBitmap() {
		return null;
	}

	public RectBox getCollisionBox() {
		return null;
	}

	public float getXSpeed() {
		return xSpeed;
	}

	public void setXSpeed(float speed) {
		this.xSpeed = speed;
	}

	public float getYSpeed() {
		return ySpeed;
	}

	public void setYSpeed(float speed) {
		this.ySpeed = speed;
	}

	public int getHeight() {
		return 0;
	}

	public int getWidth() {
		return 0;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void dispose() {

	}

}
