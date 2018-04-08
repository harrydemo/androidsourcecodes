package org.loon.framework.android.game.core.graphics.opengl;

import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.core.graphics.LColor;
import org.loon.framework.android.game.core.graphics.LFont;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.graphics.device.LGraphics;
import org.loon.framework.android.game.core.graphics.opengl.LTexture.Format;

import android.graphics.Bitmap.Config;

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
public class LSTRFont {

	/**
	 * 获得指定字符串的LImage图像
	 * 
	 * @param fontName
	 * @param style
	 * @param size
	 * @param color
	 * @param text
	 * @return
	 */
	public static LImage createFontImage(String fontName, int style, int size,
			GLColor color, String text) {
		return createFontImage(LFont.getFont(fontName, style, size), color,
				text);
	}

	/**
	 * 获得指定字符串的LImage图像
	 * 
	 * @param font
	 * @param color
	 * @param text
	 * @return
	 */
	public static LImage createFontImage(LFont font, GLColor color, String text) {
		LImage image = new LImage(font.stringWidth(text), font.getHeight(), Config.ARGB_8888);
		LGraphics g = image.getLGraphics();
		g.setColor(color.getAWTColor());
		g.setFont(font);
		g.setAntiAlias(true);
		g.drawString(text, 0, -font.getAscent());
		g.dispose();
		return image;
	}

	private LFont font;

	private int defFontWidth = 512;

	private int defFontHeight = 512;

	private int totalCharSet = 256;

	private int defSize = 32;

	private LTexture texture;

	/**
	 * LSTRFont目前仅用于生成指定字体的英文字母表，虽然理论上也可以生成或自带中文字母表，但会很大，暂时不予考虑使用。
	 * 
	 * （以AngelFont生成的含fnt与tga文件的标准宋体字母图表(仅常见字),压缩后大约6MB左右|||……）
	 * 
	 * @param font
	 */
	public LSTRFont(LFont font) {

		if (font.getSize() > 30) {
			throw new RuntimeException(
					"Sorry ,font size > 30 are not supported !");
		}

		this.font = font;

		LImage tempImage = new LImage(defFontWidth, defFontHeight,
				Config.ARGB_8888);

		LGraphics g = tempImage.getLGraphics();

		g.setFont(LFont.getFont(font.getFontName(), font.getStyle(), font
				.getSize() + 5));
		g.setColor(LColor.white);
		g.setAntiAlias(true);

		int fontHeight = font.getHeight();
		int rows = 0, cols = font.getSize();

		for (int i = 0; i < totalCharSet; i++) {
			char chars = (char) i;
			String string = String.valueOf((char) i);
			int fontWidth = font.charWidth(chars);
			if (fontWidth < 1) {
				fontWidth = 1;
			}
			int posX = rows + (defSize - fontWidth) / 2;
			int posY = cols + (defSize - fontHeight) / 2;
			g.drawString(string, posX, posY);
			rows += defSize;
			if (rows >= defFontWidth) {
				rows = 0;
				cols += defSize;
			}
		}
		g.dispose();
		texture = new LTexture(GLLoader.getTextureData(tempImage), Format.FONT);
		texture.string = true;
		if (tempImage != null) {
			tempImage.dispose();
			tempImage = null;
		}
		LSystem.gc();
	}

	public LTexture getTexture() {
		return texture;
	}

	public LFont getFont() {
		return font;
	}

	public int getDefFontHeight() {
		return defFontHeight;
	}

	public int getDefFontWidth() {
		return defFontWidth;
	}

	public int getDefSize() {
		return defSize;
	}

	public int getTotalCharSet() {
		return totalCharSet;
	}

}
