package org.loon.framework.javase.game.core.graphics;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import org.loon.framework.javase.game.core.LSystem;

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
public class LFont {

	private final static LFont defaultFont = LFont.getFont("Dialog", 0, 20);

	public static final int STYLE_PLAIN = 0;

	public static final int STYLE_BOLD = 1;

	public static final int STYLE_ITALIC = 2;

	public static final int STYLE_UNDERLINED = 4;

	public static final int SIZE_SMALL = 8;

	public static final int SIZE_MEDIUM = 0;

	public static final int SIZE_LARGE = 16;

	public static final int FACE_MONOSPACE = 32;

	public static final int FACE_PROPORTIONAL = 64;

	public static final int FACE_SYSTEM = 0;

	public static final int FONT_STATIC_TEXT = 0;

	public static final int FONT_INPUT_TEXT = 1;

	private final static Graphics2D g2d = (Graphics2D) new BufferedImage(1, 1,
			BufferedImage.TYPE_INT_ARGB).getGraphics();

	private String name;

	private int style;

	private int size;

	private boolean antialiasing;

	private boolean initialized;

	private FontMetrics fontMetrics;

	public static LFont getFont(int size) {
		return LFont.getFont(LSystem.FONT, 0, size);
	}

	public static LFont getFont(String familyName, int size) {
		return getFont(familyName, 0, size);
	}
	
	public LFont(int face, int style, int size) {
		this.style = style;
		this.size = size;
		switch (face) {
		case FACE_SYSTEM:
			this.name = "system";
			break;
		case FACE_PROPORTIONAL:
			this.name = "proportional";
			break;
		case FACE_MONOSPACE:
			this.name = "monospace";
			break;
		default:
			throw new IllegalArgumentException();
		}
	}

	public LFont(Font font) {
		this.name = font.getFontName();
		this.style = font.getStyle();
		this.size = font.getSize();
		this.antialiasing = true;
		this.initialized = false;
	}

	public LFont(String name, int style, int size, boolean antialiasing) {
		this.name = name;
		this.style = style;
		this.size = size;
		this.antialiasing = antialiasing;
		this.initialized = false;
	}

	public LFont(String name, int style, int size) {
		this(name, style, size, false);
	}

	public void setAntialiasing(boolean antialiasing) {
		if (this.antialiasing != antialiasing) {
			this.antialiasing = antialiasing;
			initialized = false;
		}
	}

	public String getFontName() {
		return this.name;
	}

	public int charWidth(char ch) {
		checkInitialized();
		return fontMetrics.charWidth(ch);
	}

	public int charsWidth(char[] ch, int offset, int length) {
		checkInitialized();
		return fontMetrics.charsWidth(ch, offset, length);
	}

	public int getBaselinePosition() {
		checkInitialized();
		return fontMetrics.getAscent();
	}

	public int getLineHeight() {
		return getAscent() + getDescent();
	}

	public int getHeight() {
		checkInitialized();
		return fontMetrics.getHeight();
	}

	public int getDescent() {
		checkInitialized();
		return fontMetrics.getDescent();
	}

	public int stringWidth(String str) {
		checkInitialized();
		return fontMetrics.stringWidth(str);
	}

	public int subStringWidth(String str, int offset, int count) {
		checkInitialized();
		return fontMetrics.stringWidth(str.substring(offset, offset + count));
	}

	public static LFont getDefaultFont() {
		return defaultFont;
	}

	public static LFont getFont(int face, int style, int size) {
		return new LFont(face, style, size);
	}

	public static LFont getFont(String name, int style, int size) {
		return new LFont(name, style, size);
	}

	public Font getFont() {
		checkInitialized();
		return fontMetrics.getFont();
	}

	public int getSize() {
		return size;
	}

	public int getStyle() {
		return style;
	}

	public boolean isBold() {
		return (style & STYLE_BOLD) != 0;
	}

	public boolean isUnderlined() {
		return (style & STYLE_UNDERLINED) != 0;
	}

	public boolean isItalic() {
		return (style & STYLE_ITALIC) != 0;
	}

	public boolean isPlain() {
		return style == 0;
	}

	public int getAscent() {
		checkInitialized();
		return fontMetrics.getAscent();
	}

	private synchronized void checkInitialized() {
		if (!initialized) {
			if (antialiasing) {
				g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
						RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			} else {
				g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
						RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
			}
			fontMetrics = g2d.getFontMetrics(new Font(name, style, size));
			initialized = true;
		}
	}

}
