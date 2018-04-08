package org.loon.framework.javase.game.action.sprite.effect;

import org.loon.framework.javase.game.action.sprite.ISprite;
import org.loon.framework.javase.game.core.LObject;
import org.loon.framework.javase.game.core.LSystem;
import org.loon.framework.javase.game.core.geom.RectBox;
import org.loon.framework.javase.game.core.graphics.opengl.GLColor;
import org.loon.framework.javase.game.core.graphics.opengl.GLEx;
import org.loon.framework.javase.game.core.graphics.opengl.LTexture;

/**
 * Copyright 2008 - 2010
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
public class FadeEffect extends LObject implements ISprite {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GLColor color;

	public float time;

	public float currentFrame;

	public int type;

	public boolean stop;

	private float opacity;

	private int offsetX, offsetY;

	private int width;

	private int height;
	
	private boolean visible;

	public static FadeEffect getInstance(int type, GLColor c) {
		return new FadeEffect(c, 120, type, (int)LSystem.screenRect.getWidth(),
				(int)LSystem.screenRect.getHeight());

	}

	public FadeEffect(GLColor c, int delay, int type, int w, int h) {
		this.visible = true;
		this.type = type;
		this.setDelay(delay);
		this.setColor(c);
		this.width = w;
		this.height = h;
	}

	public float getDelay() {
		return time;
	}

	public void setDelay(int delay) {
		this.time = delay;
		if (type == TYPE_FADE_IN) {
			this.currentFrame = this.time;
		} else {
			this.currentFrame = 0;
		}
	}

	public GLColor getColor() {
		return color;
	}

	public void setColor(GLColor color) {
		this.color = color;
	}

	public float getCurrentFrame() {
		return currentFrame;
	}

	public void setCurrentFrame(float currentFrame) {
		this.currentFrame = currentFrame;
	}

	public boolean isStop() {
		return stop;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setVisible(boolean visible) {
		this.opacity = visible ? 255 : 0;
		this.visible = visible;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setOpacity(float opacity) {
		this.opacity = opacity;
	}

	public float getOpacity() {
		return opacity;
	}

	public void createUI(GLEx g) {
		if (!visible) {
			return;
		}
		if (stop) {
			return;
		}
		float op = (currentFrame / time);
		setOpacity(op);
		if (opacity > 0) {
			GLColor old = g.getColor();
			g.setColor(color.r, color.g, color.b, opacity);
			g.fillRect(offsetX + this.x(), offsetY + this.y(), width, height);
			g.setColor(old);
			return;
		}
	}

	public void update(long timer) {
		if (type == TYPE_FADE_IN) {
			currentFrame--;
			if (currentFrame == 0) {
				setOpacity(0);
				stop = true;
			}
		} else {
			currentFrame++;
			if (currentFrame == time) {
				setOpacity(0);
				stop = true;
			}
		}
	}

	public float getAlpha() {
		return 0;
	}

	public RectBox getCollisionBox() {
		return getRect(x(), y(), getWidth(), getHeight());
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public int getOffsetX() {
		return offsetX;
	}

	public void setOffsetX(int offsetX) {
		this.offsetX = offsetX;
	}

	public int getOffsetY() {
		return offsetY;
	}

	public void setOffsetY(int offsetY) {
		this.offsetY = offsetY;
	}

	public LTexture getBitmap() {
		return null;
	}

	public void dispose() {

	}

}
