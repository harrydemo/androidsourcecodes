package org.loon.framework.javase.game.core.graphics.component;

import java.awt.Color;

import org.loon.framework.javase.game.core.graphics.LComponent;
import org.loon.framework.javase.game.core.graphics.LContainer;
import org.loon.framework.javase.game.core.graphics.LFont;
import org.loon.framework.javase.game.core.graphics.LGradation;
import org.loon.framework.javase.game.core.graphics.LImage;
import org.loon.framework.javase.game.core.graphics.device.LGraphics;
import org.loon.framework.javase.game.core.graphics.opengl.GLColor;
import org.loon.framework.javase.game.core.graphics.opengl.GLEx;
import org.loon.framework.javase.game.core.graphics.opengl.LTexture;
import org.loon.framework.javase.game.core.input.LInputFactory.Key;

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
public class LProgress extends LContainer {

	private boolean showMessage;

	private GLColor fillColor;

	private GLColor fontColor;

	private String message;

	private LFont font;

	private LTexture progressImage, progressBackground;

	private float size;

	private float current;

	public LProgress(int x, int y, int width, int height) {
		this(100, x, y, width, height);
	}

	public LProgress(int size, int x, int y, int width, int height) {
		super(x, y, width, height);
		this.setMessage(null);
		this.font = LFont.getDefaultFont();
		this.fontColor = GLColor.white;
		this.showMessage = false;
		this.customRendering = true;
		this.elastic = true;
		this.visible = true;
		this.size = size;
		this.current = 0;
		this.progressBackgound(GLColor.black);
		this.progressBlue();
		this.setLocked(true);
		this.setLayer(100);
	}

	public void add() {
		add(1);
	}

	public void add(float count) {
		current += count;
		if (current > size) {
			current = size;
		}
	}

	public void finish() {
		this.add(size);
	}

	public float getPercentage() {
		return current / size;
	}

	public void setTotal(float size) {
		this.size = size;
	}

	/**
	 * 处理点击事件（请重载实现）
	 * 
	 */
	public void doClick() {

	}

	protected void processTouchClicked() {
		this.doClick();
	}

	protected void processKeyPressed() {
		if (this.isSelected() && this.input.getKeyPressed() == Key.ENTER) {
			this.doClick();
		}
	}

	protected void processTouchDragged() {
		if (!locked) {
			if (getContainer() != null) {
				getContainer().sendToFront(this);
			}
			this.move(this.input.getTouchDX(), this.input.getTouchDY());
		}
	}

	protected void createCustomUI(GLEx g, int x, int y, int w, int h) {
		if (!visible) {
			return;
		}
		GLColor oldColor = g.getColor();
		LFont oldFont = g.getFont();
		g.setFont(font);
		g.drawTexture(progressBackground, x, y);
		int size = Math.round(getPercentage() * w);
		g.drawTexture(progressImage, x, y, size, h, 0, 0, size, h);
		g.setColor(fontColor);
		String mes = null;
		if (message == null & showMessage) {
			mes = Integer.toString((int) (getPercentage() * 100)) + " %";
		} else if (showMessage) {
			mes = message;
		} else {
			mes = "";
		}
		if (this.showMessage) {
			g.drawStyleString(mes, x + (w - font.stringWidth(mes)) / 2 + 2, y
					+ ((h + font.getAscent()) / 2) - 2, fontColor,
					GLColor.black);
		}
		g.setFont(oldFont);
		g.setColor(oldColor);
	}

	public void createUI(GLEx g, int x, int y, LComponent component,
			LTexture[] buttonImage) {

	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public void progressBackgound(GLColor c) {
		if (progressBackground != null) {
			progressBackground.destroy();
			progressBackground = null;
		}
		this.progressBackground = createBackground(c.getAWTColor(), getWidth(),
				getHeight());
	}

	public void progressColor(GLColor c1, GLColor c2, boolean flag) {
		if (progressImage != null) {
			progressImage.destroy();
			progressImage = null;
		}
		this.progressImage = createProgressImage(c1.getAWTColor(), c2
				.getAWTColor(), flag, getWidth(), getHeight());
	}

	private LTexture createBackground(Color c, int w, int h) {
		LImage image = new LImage(w, h, false);
		LGraphics g = image.getLGraphics();
		g.setColor(c);
		g.fillRect(0, 0, w, h);
		g.setColor(Color.black);
		g.drawLine(1, h - 3, 1, 1);
		g.drawLine(1, 1, w - 3, 1);
		g.setColor(Color.black);
		g.drawLine(0, h - 1, w - 1, h - 1);
		g.drawLine(w - 1, h - 1, w - 1, 0);
		g.setColor(Color.black);
		g.drawRect(0, 0, w - 2, h - 2);
		g.dispose();
		g = null;
		LTexture texture = image.getTexture();
		if (image != null) {
			image.dispose();
			image = null;
		}
		return texture;
	}

	private LTexture createProgressImage(Color c1, Color c2, boolean flag,
			int w, int h) {
		LImage image = new LImage(w, h, true);
		LGraphics g = image.getLGraphics();
		LGradation gradation = LGradation
				.getInstance(c1, c2, w - 4, h - 4, 255);
		if (flag) {
			gradation.drawWidth(g, 2, 2);
		} else {
			gradation.drawHeight(g, 2, 2);
		}
		g.setColor(Color.black);
		g.drawLine(1, h - 3, 1, 1);
		g.drawLine(1, 1, w - 3, 1);
		g.setColor(Color.black);
		g.drawLine(0, h - 1, w - 1, h - 1);
		g.drawLine(w - 1, h - 1, w - 1, 0);
		g.setColor(Color.black);
		g.drawRect(0, 0, w - 2, h - 2);
		g.dispose();
		g = null;
		LTexture texture = image.getTexture();
		if (image != null) {
			image.dispose();
			image = null;
		}
		return texture;
	}

	public void progressRed() {
		progressColor(this.fillColor = GLColor.red, new GLColor(75, 75, 75),
				false);
	}

	public void progressYellow() {
		progressColor(this.fillColor = GLColor.yellow, new GLColor(75, 75, 75),
				false);
	}

	public void progressBlue() {
		progressColor(this.fillColor = GLColor.blue, new GLColor(75, 75, 75),
				false);
	}

	public void percent(int c) {
		this.current = c;
	}

	public GLColor getFillColor() {
		return fillColor;
	}

	public LFont getFont() {
		return font;
	}

	public void setFont(LFont font) {
		this.font = font;
	}

	public boolean isProgressTitle() {
		return showMessage;
	}

	public void setProgressTitle(boolean showMessage) {
		this.showMessage = showMessage;
	}

	public GLColor getFontColor() {
		return fontColor;
	}

	public void setFontColor(GLColor fontColor) {
		this.fontColor = fontColor;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		if (message != null) {
			this.showMessage = true;
		}
		this.message = message;
	}

	public String getUIName() {
		return "Progress";
	}

	public void dispose() {
		super.dispose();
		if (progressImage != null) {
			progressImage.destroy();
			progressImage = null;
		}
		if (progressBackground != null) {
			progressBackground.destroy();
			progressBackground = null;
		}
	}

}
