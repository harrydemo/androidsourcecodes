package org.loon.framework.android.game.core.graphics;

import org.loon.framework.android.game.core.graphics.opengl.GLColor;

/**
 * 
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
 * @email ceponline@yahoo.com.cn
 * @version 0.1.0
 */
public class LColor {

	public final static int transparent = 0xff000000;
	
	public final static LColor white = new LColor(255, 255, 255);

	public final static LColor lightGray = new LColor(192, 192, 192);

	public final static LColor gray = new LColor(128, 128, 128);

	public final static LColor darkGray = new LColor(64, 64, 64);

	public final static LColor black = new LColor(0, 0, 0);

	public final static LColor red = new LColor(255, 0, 0);

	public final static LColor pink = new LColor(255, 175, 175);

	public final static LColor orange = new LColor(255, 200, 0);

	public final static LColor yellow = new LColor(255, 255, 0);

	public final static LColor green = new LColor(0, 255, 0);

	public final static LColor magenta = new LColor(255, 0, 255);

	public final static LColor cyan = new LColor(0, 255, 255);

	public final static LColor blue = new LColor(0, 0, 255);

	private static final double FACTOR = 0.7;

	private int r, g, b, alpha;

	private int[] rgba;

	public LColor(int r, int g, int b, int alpha) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.alpha = alpha;
		this.rgba = new int[] { r, g, b, alpha };
	}

	public LColor(LColor c) {
		this(c.r, c.g, c.b, c.alpha);
	}

	public LColor(int r, int g, int b) {
		this(r, g, b, 0xff);
	}

	public LColor(float r, float g, float b) {
		this((int) (r * 255 + 0.5), (int) (g * 255 + 0.5),
				(int) (b * 255 + 0.5));

	}

	public LColor(float r, float g, float b, float a) {
		this((int) (r * 255 + 0.5), (int) (g * 255 + 0.5),
				(int) (b * 255 + 0.5), (int) (a * 255 + 0.5));

	}

	public LColor(int rgba, boolean hasalpha) {
		if (hasalpha) {
			r = GLColor.getRed(rgba);
			g = GLColor.getGreen(rgba);
			b = GLColor.getBlue(rgba);
			alpha = GLColor.getAlpha(rgba);
		} else {
			r = GLColor.getRed(rgba);
			g = GLColor.getGreen(rgba);
			b = GLColor.getBlue(rgba);
		}
	}

	public LColor(int color) {
		this.r = GLColor.getRed(color);
		this.g = GLColor.getGreen(color);
		this.b = GLColor.getBlue(color);
		this.alpha = GLColor.getAlpha(color);
	}

	public LColor(int[] colors) {
		this.r = colors[0];
		this.g = colors[1];
		this.b = colors[2];
		this.alpha = colors[3];
	}

	public int[] getRGBs() {
		return rgba;
	}

	public void setAlphaValue(int alpha) {
		this.alpha = alpha;
	}

	public void setAlpha(float alpha) {
		setAlphaValue((int) (255 * alpha));
	}

	public LColor brighter() {
		int r = getRed();
		int g = getGreen();
		int b = getBlue();

		int i = (int) (1.0 / (1.0 - FACTOR));
		if (r == 0 && g == 0 && b == 0) {
			return new LColor(i, i, i);
		}
		if (r > 0 && r < i) {
			r = i;
		}
		if (g > 0 && g < i) {
			g = i;
		}
		if (b > 0 && b < i) {
			b = i;
		}
		return new LColor(Math.min((int) (r / FACTOR), 255), Math.min(
				(int) (g / FACTOR), 255), Math.min((int) (b / FACTOR), 255));
	}

	public LColor darker() {
		return new LColor(Math.max((int) (getRed() * FACTOR), 0), Math.max(
				(int) (getGreen() * FACTOR), 0), Math.max(
				(int) (getBlue() * FACTOR), 0));
	}

	public int getARGB() {
		return GLColor.getARGB(r, g, b, alpha);
	}

	public int getRGB() {
		return GLColor.getRGB(r, g, b);
	}

	public boolean equals(final LColor c) {
		return (c.r == r) && (c.g == g) && (c.b == b) && (c.alpha == alpha);
	}

	public int getRed() {
		return r;
	}

	public int getGreen() {
		return g;
	}

	public int getBlue() {
		return b;
	}

	public int getAlpha() {
		return alpha;
	}

}