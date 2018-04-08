package org.loon.framework.android.game.core.graphics.opengl;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.core.graphics.LFont;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.graphics.device.LGraphics;
import org.loon.framework.android.game.core.graphics.opengl.LTexture.Format;
import org.loon.framework.android.game.utils.BufferUtils;

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
public final class LSTRTexture {

	public static class Exist {

		public final LTexture texture;

		public final int left;

		public final int top;

		public final String src;

		public final String dst;

		public Exist(LTexture tex2d, int left, int top, String src, String dst) {
			this.texture = tex2d;
			this.left = left;
			this.top = top;
			this.src = src;
			this.dst = dst;
		}
	}

	private static char split = '$';

	private static StringBuffer lazyKey;

	private static HashMap<String, LSTRTexture> lazyEnglish = new HashMap<String, LSTRTexture>(
			10);

	private static HashMap<String, LTexture> lazyFonts = new HashMap<String, LTexture>(
			10);

	private boolean isReplace;

	LTexture texture;

	int textureID;

	int size;

	int width;

	int height;

	int space;

	int style;

	int rows;

	int cols;

	int vertBufID;

	FloatBuffer vertData;

	int texBufID;

	Format format;

	FloatBuffer texData;

	final int vertSize = 8 * 4;

	int texSize;

	boolean isLoaded;

	String fontName;

	public LSTRTexture(LTexture texture, int rows, int colums) {
		this.texture = texture;
		this.rows = rows;
		this.cols = colums;
		this.format = texture.getFormat();
		this.isReplace = texture.isReplace();
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public LTexture getTexture() {
		return texture;
	}

	/**
	 * 载入指定字符纹理到画布之上
	 * 
	 * @param font
	 * @return
	 */
	public LSTRTexture load(LFont font) {

		int size = font.getHeight();
		int style = font.getStyle();
		String fontName = font.getFontName();

		if (size == this.size && style == this.style
				&& fontName.equalsIgnoreCase(this.fontName) && isLoaded) {
			return this;
		}

		this.isLoaded = true;

		if (!texture.isLoaded) {
			texture.loadTexture();
		}
		this.textureID = texture.textureID;
		this.style = style;
		this.size = width = height = size;
		this.space = width / 2;
		this.fontName = fontName;

		final float vertCords[] = { 0.0f, 0.0f, width, 0.0f, 0.0f, height,
				width, height, };
		vertData = BufferUtils.createFloatBuffer(vertCords);

		float[] fontTexCoords = new float[rows * cols * 8];

		int x = 0;
		for (float row = 0; row < rows; row++) {
			for (float col = 0; col < cols; col++) {
				float texXOff = col / cols;
				float texYOff = row / rows;
				float texWidthRatio = (col + 1) / cols;
				float texHeightRatio = (row + 1) / rows;

				fontTexCoords[x++] = texXOff;
				fontTexCoords[x++] = texYOff;

				fontTexCoords[x++] = texWidthRatio;
				fontTexCoords[x++] = texYOff;

				fontTexCoords[x++] = texXOff;
				fontTexCoords[x++] = texHeightRatio;

				fontTexCoords[x++] = texWidthRatio;
				fontTexCoords[x++] = texHeightRatio;
			}
		}

		texData = BufferUtils.createFloatBuffer(fontTexCoords);
		texSize = texData.capacity() * 4;

		if (texture != null && texture.isLoaded) {
			GLEx.deleteBuffer(texture.bufferID);
			GLEx.updateHardwareBuffDouble(this);
		}

		return this;
	}

	public Format getFormat() {
		return format;
	}

	public boolean isLoaded() {
		return isLoaded;
	}

	public boolean isReplace() {
		return isReplace;
	}

	public static void reload() {
		for (LSTRTexture tex : lazyEnglish.values()) {
			tex.isLoaded = false;
		}
	}

	/**
	 * 限制产生的字符串纹理大小
	 * 
	 * @param font
	 * @param fontWidth
	 * @param text
	 * @return
	 */
	private final static boolean limitFontWidth(LFont font, int fontWidth,
			String text) {
		if (fontWidth > 512) {
			int stringSize = text.length();
			int halfSize = stringSize / 2;
			String tmp1 = text.substring(0, halfSize);
			String tmp2 = text.substring(halfSize, stringSize);
			bindStringLazy(font, tmp1);
			bindStringLazy(font, tmp2);
			return true;
		}
		return false;
	}

	/**
	 * 绘制指定文字到画布(建议仅在混杂有非西方字符时使用)
	 * 
	 * @param gl
	 * @param text
	 * @param x
	 * @param y
	 * @param rotation
	 * @param c
	 */
	public static void drawString(final GLEx gl, final String text,
			final float x, final float y, final float rotation, final GLColor c) {
		if (gl == null) {
			return;
		}
		if (text == null) {
			return;
		}

		if (lazyFonts.size() > LSystem.DEFAULT_MAX_CACHE_SIZE * 2) {
			clearStringLazy();
		}

		final int fontWidth, fontHeight;

		LFont font = gl.getFont();
		fontWidth = font.stringWidth(text);
		fontHeight = font.getLineHeight();

		String key = makeStringLazyKey(font, text);

		LTexture texture = lazyFonts.get(key);
		if (texture != null && !texture.isClose) {
			gl.drawTexture(texture, x, y, c, rotation);
			return;
		}

		Exist exist = existStringLazy(key);
		if (exist != null) {
			texture = exist.texture;
			if (texture != null && !texture.isClose) {
				int posX = 0;
				int left = exist.left;
				String src = exist.src;
				if (left > 0 && src != null) {
					posX = font.stringWidth(src.substring(0, left));
				}
				gl.drawTexture(texture, x, y, fontWidth, fontHeight, posX, 0,
						posX + fontWidth, fontHeight, rotation, c);
				return;
			}
		}
		if (limitFontWidth(font, fontWidth, text)) {
			return;
		}

		lazyFonts.put(key, createStringTexture(font, text, fontWidth,
				fontHeight));

	}

	/**
	 * 强制绑定特定的字体与字符串与字符纹理对应关系
	 * 
	 * @param font
	 * @param text
	 */
	public static void bindStringLazy(final LFont font, final String text) {

		final int fontWidth, fontHeight;
		fontWidth = font.stringWidth(text);
		fontHeight = font.getLineHeight();

		if (limitFontWidth(font, fontWidth, text)) {
			return;
		}

		String key = makeStringLazyKey(font, text);
		LTexture oldTex2d = lazyFonts.get(key);

		if (oldTex2d != null) {
			return;
		}
		lazyFonts.put(key, createStringTexture(font, text, fontWidth,
				fontHeight));
	}

	/**
	 * 强制绑定特定的字体与字符串与字符纹理对应关系
	 * 
	 * @param font
	 * @param text
	 * @param texture
	 */
	public static void bindStringLazy(LFont font, String text, LTexture texture) {
		String key = makeStringLazyKey(font, text);
		LTexture oldTex2d = lazyFonts.get(key);
		if (oldTex2d != null) {
			return;
		}
		lazyFonts.put(key, texture);
	}

	/**
	 * 接触字符串纹理绑定
	 * 
	 * @param font
	 * @param text
	 */
	public static void unloadStringLazy(LFont font, String text) {
		String key = makeStringLazyKey(font, text);
		LTexture oldTex2d = lazyFonts.remove(key);
		if (oldTex2d != null) {
			oldTex2d.destroy();
			oldTex2d = null;
		}
	}

	/**
	 * 判定对应关系的字符纹理缓存是否存在
	 * 
	 * @param font
	 * @param text
	 * @return
	 */
	public static Exist existStringLazy(LFont font, String text) {
		String key = makeStringLazyKey(font, text);
		return existStringLazy(key);
	}

	/**
	 * 判定对应关系的字符纹理缓存是否存在，并返回缓存所在的具体位置
	 * 
	 * @param key
	 * @param font
	 * @param text
	 * @return
	 */
	private static Exist existStringLazy(String key) {
		LTexture texture = lazyFonts.get(key);
		if (texture == null) {
			Set<?> set = lazyFonts.entrySet();
			String texName, srcTmp, dstTmp;
			String srcFlag, dstFlag;
			LTexture tex2d;
			int startScrIndex, startDstIndex, srcIndex, srcLength, dstIndex, dstLength;
			for (Iterator<?> it = set.iterator(); it.hasNext();) {
				Entry<?, ?> e = (Entry<?, ?>) it.next();
				texName = (String) e.getKey();

				tex2d = (LTexture) e.getValue();
				startScrIndex = texName.lastIndexOf(split);
				startDstIndex = key.lastIndexOf(split);

				srcFlag = texName.substring(0, startScrIndex);
				dstFlag = key.substring(0, startDstIndex);

				if (!srcFlag.equalsIgnoreCase(dstFlag)) {
					continue;
				}

				srcIndex = startScrIndex + 1;
				srcLength = texName.length();
				dstIndex = startDstIndex + 1;
				dstLength = key.length();
				srcTmp = texName.substring(srcIndex, srcLength);
				dstTmp = key.substring(dstIndex, dstLength);
				int index = srcTmp.indexOf(dstTmp);
				if (index != -1) {
					if (tex2d != null && !tex2d.isClose) {
						return new Exist(tex2d, index, 0, srcTmp, dstTmp);
					}
				}
			}
		}
		if (texture == null) {
			return null;
		}
		return new Exist(texture, 0, 0, null, null);
	}

	/**
	 * 强制清空所有的非英文字符缓存
	 * 
	 */
	public static void clearStringLazy() {
		for (LTexture tex : lazyFonts.values()) {
			if (tex != null) {
				tex.destroy();
				tex = null;
			}
		}
		lazyFonts.clear();
	}

	/**
	 * 强制清空所有的英文字符缓存
	 * 
	 */
	public static void clearEnglishLazy() {
		for (LSTRTexture tex : lazyEnglish.values()) {
			if (tex.texture != null) {
				tex.texture.destroy();
				tex.texture = null;
			}
			tex.isLoaded = false;
			GLEx.deleteBuffer(tex.texBufID);
		}
		lazyEnglish.clear();
	}

	/**
	 * 生成特定字符串的缓存用ID
	 * 
	 * @param font
	 * @param text
	 * @return
	 */
	public static String makeStringLazyKey(final LFont font, final String text) {
		int hashCode = 0;
		hashCode = LSystem.unite(hashCode, font.getSize());
		hashCode = LSystem.unite(hashCode, font.getStyle());
		if (lazyKey == null) {
			lazyKey = new StringBuffer();
			lazyKey.append(font.getFontName().toLowerCase());
			lazyKey.append(hashCode);
			lazyKey.append(split);
			lazyKey.append(text);
		} else {
			lazyKey.delete(0, lazyKey.length());
			lazyKey.append(font.getFontName().toLowerCase());
			lazyKey.append(hashCode);
			lazyKey.append(split);
			lazyKey.append(text);
		}
		return lazyKey.toString();
	}

	/**
	 * 创建一张指定格式的纹理用以绘制文字
	 * 
	 * @param font
	 * @param text
	 * @param fontWidth
	 * @param fontHeight
	 * @return
	 */
	public static LTexture createStringTexture(LFont font, String text,
			int fontWidth, int fontHeight) {
		return createStringTexture(font, text, fontWidth, fontHeight,
				Format.FONT);
	}

	/**
	 * 创建一张指定格式的纹理用以绘制文字
	 * 
	 * @param gl
	 * @param key
	 * @param text
	 * @param fontWidth
	 * @param fontHeight
	 * @return
	 */
	public static LTexture createStringTexture(LFont font, String text,
			int fontWidth, int fontHeight, Format format) {
		LImage image = new LImage(fontWidth, fontHeight, Config.ARGB_8888);
		LGraphics g = image.getLGraphics();
		g.setFont(font);
		g.setAntiAlias(true);
		g.drawString(text, 0, -font.getAscent());
		g.dispose();
		LTexture texture = new LTexture(GLLoader.getTextureData(image), format);
		texture.string = true;
		if (image != null) {
			image.dispose();
			image = null;
		}
		return texture;
	}

	/**
	 * 将指定LFont分解为字符纹理(仅限常见西方字符)
	 * 
	 * @param font
	 * @return
	 */
	public static LSTRTexture createStringTexture(LFont font) {
		if (lazyEnglish.size() > 5) {
			clearEnglishLazy();
		}
		String key = makeLazyWestKey(font);
		LSTRTexture texture = lazyEnglish.get(key);
		if (texture == null) {
			LTexture tex2d = new LSTRFont(font).getTexture();
			texture = new LSTRTexture(tex2d, tex2d.getWidth() / 32, tex2d
					.getHeight() / 32);
			lazyEnglish.put(key, texture);
		}
		texture.load(font);
		return texture;
	}

	/**
	 * 强制绑定一组字体与字符纹理的对应关系
	 * 
	 * @param font
	 * @param texture
	 */
	public static void bindStringTexture(LFont font, LSTRTexture texture) {
		String key = makeLazyWestKey(font);
		LSTRTexture oldTex2d = lazyEnglish.get(key);
		lazyEnglish.put(key, texture);
		if (oldTex2d != null) {
			if (oldTex2d.texture != null) {
				oldTex2d.texture.destroy();
				oldTex2d.texture = null;
			}
			oldTex2d.isLoaded = false;
			GLEx.deleteBuffer(oldTex2d.texBufID);
		}
	}

	/**
	 * 生成一组西方字符缓存键值
	 * 
	 * @param font
	 * @return
	 */
	private static String makeLazyWestKey(LFont font) {
		if (lazyKey == null) {
			lazyKey = new StringBuffer();
			lazyKey.append(font.getFontName().toLowerCase());
			lazyKey.append(font.getStyle());
			lazyKey.append(font.getSize());
		} else {
			lazyKey.delete(0, lazyKey.length());
			lazyKey.append(font.getFontName().toLowerCase());
			lazyKey.append(font.getStyle());
			lazyKey.append(font.getSize());
		}
		return lazyKey.toString();
	}

	/**
	 * 将一张512x512的字符图片分解为字符纹理
	 * 
	 * @param path
	 * @return
	 */
	public static LSTRTexture create512x512StringTexture(String path) {
		LTexture tex2d = new LTexture(path, Format.FONT);
		tex2d.string = true;
		return new LSTRTexture(tex2d, tex2d.getWidth() / 32,
				tex2d.getHeight() / 32);
	}

	public static void dispose() {
		clearStringLazy();
		clearEnglishLazy();
	}
}
