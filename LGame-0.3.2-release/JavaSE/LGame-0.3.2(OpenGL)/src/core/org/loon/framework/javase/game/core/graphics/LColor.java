package org.loon.framework.javase.game.core.graphics;

import java.awt.Color;

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
public class LColor extends Color {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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

	public final static int transparent = 0xff000000;

	public LColor(Color c) {
		super(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
	}

	public LColor(int r, int g, int b) {
		super(r, g, b, 255);
	}

	public LColor(int r, int g, int b, int a) {
		super(r, g, b, a);
	}

	public LColor(int rgb) {
		super(rgb);
	}

	public LColor(int rgba, boolean alpha) {
		super(rgba, alpha);
	}

	public LColor(float r, float g, float b) {
		super(r, g, b);
	}

	public LColor(float r, float g, float b, float a) {
		super(r, g, b, a);
	}

	public Color getAWTColor() {
		return new Color(getRed(), getGreen(), getBlue(), getAlpha());
	}

}
