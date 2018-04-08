package org.loon.framework.javase.game.action.sprite.effect;

import org.loon.framework.javase.game.core.LSystem;
import org.loon.framework.javase.game.core.graphics.opengl.GLEx;
import org.loon.framework.javase.game.core.graphics.opengl.LTexture;

/**
 * Copyright 2008 - 2009
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
public class RainKernel implements IKernel {

	private boolean exist;

	private LTexture rain;

	private double offsetX, offsetY, x, y, width, height, rainWidth,
			rainHeight;

	public RainKernel(int n, int w, int h) {
		rain = new LTexture((LSystem.FRAMEWORK_IMG_NAME + "rain_" + n + ".png")
				.intern());
		rainWidth = rain.getWidth();
		rainHeight = rain.getHeight();
		width = w;
		height = h;
		offsetX = 0;
		offsetY = (5 - n) * 30 + 75 + Math.random() * 15;
	}

	public void make() {
		exist = true;
		x = Math.random() * width;
		y = -rainHeight;
	}

	public void move() {
		if (!exist) {
			if (Math.random() < 0.002)
				make();
		} else {
			x += offsetX;
			y += offsetY;
			if (y >= height) {
				x = Math.random() * width;
				y = -rainHeight * Math.random();
			}
		}
	}

	public void draw(GLEx g) {
		if (exist) {
			g.drawTexture(rain, (int) x, (int) y);
		}
	}

	public LTexture getRain() {
		return rain;
	}

	public double getRainHeight() {
		return rainHeight;
	}

	public double getRainWidth() {
		return rainWidth;
	}

}
