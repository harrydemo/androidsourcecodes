package org.loon.framework.android.game.core.graphics.opengl;

import java.io.Serializable;
import java.nio.Buffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import org.loon.framework.android.game.core.graphics.LColor;
import org.loon.framework.android.game.utils.BufferUtils;

/**
 * 
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
public class GLColor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2290994222887151982L;

	private static final boolean isLittle = (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN);

	public static float[] toRGBA(int pixel) {
		int r = (pixel & 0x00FF0000) >> 16;
		int g = (pixel & 0x0000FF00) >> 8;
		int b = (pixel & 0x000000FF);
		int a = (pixel & 0xFF000000) >> 24;
		if (a < 0) {
			a += 256;
		}
		return new float[] { r / 255.0f, g / 255.0f, b / 255.0f, a / 255.0f };
	}

	public static int c(int b) {
		if (b < 0) {
			return 256 + b;
		}
		return b;
	}

	public static float[] withAlpha(float[] color, float alpha) {
		float r = color[0];
		float g = color[1];
		float b = color[2];
		float a = alpha;
		return new float[] { r, g, b, a };
	}

	public static Buffer argbToRGBABuffer(final int[] pixels) {
		if (isLittle) {
			for (int i = pixels.length - 1; i >= 0; i--) {
				final int pixel = pixels[i];
				final int red = ((pixel >> 16) & 0xFF);
				final int green = ((pixel >> 8) & 0xFF);
				final int blue = ((pixel) & 0xFF);
				final int alpha = (pixel >> 24);
				pixels[i] = alpha << 24 | blue << 16 | green << 8 | red;
			}
		} else {
			for (int i = pixels.length - 1; i >= 0; i--) {
				final int pixel = pixels[i];
				final int red = ((pixel >> 16) & 0xFF);
				final int green = ((pixel >> 8) & 0xFF);
				final int blue = ((pixel) & 0xFF);
				final int alpha = (pixel >> 24);
				pixels[i] = red << 24 | green << 16 | blue << 8 | alpha;
			}
		}
		return BufferUtils.createIntBuffer(pixels);
	}

	public static Buffer argbToRGBBuffer(final int[] pixels) {
		if (isLittle) {
			for (int i = pixels.length - 1; i >= 0; i--) {
				final int pixel = pixels[i];
				final int red = ((pixel >> 16) & 0xFF);
				final int green = ((pixel >> 8) & 0xFF);
				final int blue = ((pixel) & 0xFF);
				pixels[i] = blue << 16 | green << 8 | red;
			}
		} else {
			for (int i = pixels.length - 1; i >= 0; i--) {
				final int pixel = pixels[i];
				final int red = ((pixel >> 16) & 0xFF);
				final int green = ((pixel >> 8) & 0xFF);
				final int blue = ((pixel) & 0xFF);
				pixels[i] = red << 24 | green << 16 | blue << 8;
			}
		}
		return BufferUtils.createIntBuffer(pixels);
	}

	public static byte[] argbToRGBA(int pixel) {
		byte[] bytes = new byte[4];
		int r = (pixel >> 16) & 0xFF;
		int g = (pixel >> 8) & 0xFF;
		int b = (pixel >> 0) & 0xFF;
		int a = (pixel >> 24) & 0xFF;
		bytes[0] = (byte) r;
		bytes[1] = (byte) g;
		bytes[2] = (byte) b;
		bytes[3] = (byte) a;
		return bytes;
	}

	/**
	 * 将图片用ARGB格式转化为贴图用RGBA格式
	 * 
	 * @param pixels
	 * @return
	 */
	public static byte[] argbToRGBA(int[] pixels) {
		int size = pixels.length;
		byte[] bytes = new byte[size * 4];
		int p, r, g, b, a;
		int j = 0;
		for (int i = 0; i < size; i++) {
			p = pixels[i];
			a = (p >> 24) & 0xFF;
			r = (p >> 16) & 0xFF;
			g = (p >> 8) & 0xFF;
			b = (p >> 0) & 0xFF;
			bytes[j + 0] = (byte) r;
			bytes[j + 1] = (byte) g;
			bytes[j + 2] = (byte) b;
			bytes[j + 3] = (byte) a;
			j += 4;
		}
		return bytes;
	}

	/**
	 * 将图片用ARGB格式转化为贴图用RGB格式
	 * 
	 * @param pixels
	 * @return
	 */
	public static byte[] argbToRGB(int[] pixels) {
		int size = pixels.length;
		byte[] bytes = new byte[size * 3];
		int p, r, g, b;
		int j = 0;
		for (int i = 0; i < size; i++) {
			p = pixels[i];
			r = (p >> 16) & 0xFF;
			g = (p >> 8) & 0xFF;
			b = (p >> 0) & 0xFF;
			bytes[j + 0] = (byte) r;
			bytes[j + 1] = (byte) g;
			bytes[j + 2] = (byte) b;
			j += 3;
		}
		return bytes;
	}

	/**
	 * 获得RGB565格式数据
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @return
	 */
	public static int rgb565(float r, float g, float b) {
		return ((int) (r * 31) << 11) | ((int) (g * 63) << 5) | (int) (b * 31);
	}

	/**
	 * 获得RGBA4444格式数据
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 * @return
	 */
	public static int rgba4444(float r, float g, float b, float a) {
		return ((int) (r * 15) << 12) | ((int) (g * 15) << 8)
				| ((int) (b * 15) << 4) | (int) (a * 15);
	}

	/**
	 * 获得RGB888格式数据
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @return
	 */
	public static int rgb888(float r, float g, float b) {
		return ((int) (r * 255) << 16) | ((int) (g * 255) << 8)
				| (int) (b * 255);
	}

	/**
	 * 获得RGBA8888格式数据
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 * @return
	 */
	public static int rgba8888(float r, float g, float b, float a) {
		return ((int) (r * 255) << 24) | ((int) (g * 255) << 16)
				| ((int) (b * 255) << 8) | (int) (a * 255);
	}

	public static final GLColor white = new GLColor(1.0f, 1.0f, 1.0f, 1.0f);

	public static final GLColor yellow = new GLColor(1.0f, 1.0f, 0.0f, 1.0f);

	public static final GLColor red = new GLColor(1.0f, 0.0f, 0.0f, 1.0f);

	public static final GLColor blue = new GLColor(0.0f, 0.0f, 1.0f, 1.0f);

	public static final GLColor cornFlowerBlue = new GLColor(0.4f, 0.6f, 0.9f,
			1.0f);

	public static final GLColor green = new GLColor(0.0f, 1.0f, 0.0f, 1.0f);

	public static final GLColor black = new GLColor(0.0f, 0.0f, 0.0f, 1.0f);

	public static final GLColor gray = new GLColor(0.5f, 0.5f, 0.5f, 1.0f);

	public static final GLColor cyan = new GLColor(0.0f, 1.0f, 1.0f, 1.0f);

	public static final GLColor darkGray = new GLColor(0.3f, 0.3f, 0.3f, 1.0f);

	public static final GLColor lightGray = new GLColor(0.7f, 0.7f, 0.7f, 1.0f);

	public final static GLColor pink = new GLColor(1.0f, 0.7f, 0.7f, 1.0f);

	public final static GLColor orange = new GLColor(1.0f, 0.8f, 0.0f, 1.0f);

	public final static GLColor magenta = new GLColor(1.0f, 0.0f, 1.0f, 1.0f);

	public float r;

	public float g;

	public float b;

	public float a = 1.0f;

	public GLColor() {
		this(GLColor.white);
	}

	public GLColor(GLColor color) {
		this(color.r, color.g, color.b, color.a);
	}

	public GLColor(FloatBuffer buffer) {
		setColor(buffer.get(), buffer.get(), buffer.get(), buffer.get());
	}

	public GLColor(int r, int g, int b) {
		setColor(r, g, b);
	}

	public GLColor(int r, int g, int b, int a) {
		setColor(r, g, b, a);
	}

	public GLColor(float r, float g, float b) {
		setColor(r, g, b);
	}

	public GLColor(float r, float g, float b, float a) {
		setColor(r, g, b, a);
	}

	public GLColor(int value) {
		int r = (value & 0x00FF0000) >> 16;
		int g = (value & 0x0000FF00) >> 8;
		int b = (value & 0x000000FF);
		int a = (value & 0xFF000000) >> 24;

		if (a < 0) {
			a += 256;
		}
		if (a == 0) {
			a = 255;
		}
		setColor(r / 255.0f, g / 255.0f, b / 255.0f, a / 255.0f);
	}

	public static GLColor decode(String nm) {
		return new GLColor(Integer.decode(nm).intValue());
	}

	public int hashCode() {
		int result = (r != +0.0f ? Float.floatToIntBits(r) : 0);
		result = 31 * result + (g != +0.0f ? Float.floatToIntBits(g) : 0);
		result = 31 * result + (b != +0.0f ? Float.floatToIntBits(b) : 0);
		result = 31 * result + (a != +0.0f ? Float.floatToIntBits(a) : 0);
		return result;
	}

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		GLColor color = (GLColor) o;
		if (Float.compare(color.a, a) != 0) {
			return false;
		}
		if (Float.compare(color.b, b) != 0) {
			return false;
		}
		if (Float.compare(color.g, g) != 0) {
			return false;
		}
		if (Float.compare(color.r, r) != 0) {
			return false;
		}
		return true;
	}

	public boolean equals(float r1, float g1, float b1, float a1) {
		if (Float.compare(a1, a) != 0) {
			return false;
		}
		if (Float.compare(b1, b) != 0) {
			return false;
		}
		if (Float.compare(g1, g) != 0) {
			return false;
		}
		if (Float.compare(r1, r) != 0) {
			return false;
		}
		return true;
	}

	public GLColor darker() {
		return darker(0.5f);
	}

	public GLColor darker(float scale) {
		scale = 1 - scale;
		GLColor temp = new GLColor(r * scale, g * scale, b * scale, a);
		return temp;
	}

	public GLColor brighter() {
		return brighter(0.2f);
	}

	public void setColorValue(int r, int g, int b, int a) {
		this.r = r > 1 ? (float) r / 255.0f : r;
		this.g = g > 1 ? (float) g / 255.0f : g;
		this.b = b > 1 ? (float) b / 255.0f : b;
		this.a = a > 1 ? (float) a / 255.0f : a;
	}

	public void setColor(float r, float g, float b, float a) {
		this.r = r > 1 ? r / 255.0f : r;
		this.g = g > 1 ? g / 255.0f : g;
		this.b = b > 1 ? b / 255.0f : b;
		this.a = a > 1 ? a / 255.0f : a;
	}

	public void setColor(float r, float g, float b) {
		setColor(r, g, b, b > 1 ? 255 : 1.0f);
	}

	public void setColor(int r, int g, int b, int a) {
		this.r = (float) r / 255;
		this.g = (float) g / 255;
		this.b = (float) b / 255;
		this.a = (float) a / 255;
	}

	public void setColor(int r, int g, int b) {
		setColor(r, g, b, 255);
	}

	public void setColor(GLColor color) {
		setColor(color.r, color.g, color.b, color.a);
	}

	public float red() {
		return r;
	}

	public float green() {
		return g;
	}

	public float blue() {
		return b;
	}

	public float alpha() {
		return a;
	}

	public int getRed() {
		return (int) (r * 255);
	}

	public int getGreen() {
		return (int) (g * 255);
	}

	public int getBlue() {
		return (int) (b * 255);
	}

	public int getAlpha() {
		return (int) (a * 255);
	}

	public GLColor brighter(float scale) {
		scale += 1;
		GLColor temp = new GLColor(r * scale, g * scale, b * scale, a);
		return temp;
	}

	public GLColor multiply(GLColor c) {
		return new GLColor(r * c.r, g * c.g, b * c.b, a * c.a);
	}

	public void add(GLColor c) {
		this.r += c.r;
		this.g += c.g;
		this.b += c.b;
		this.a += c.a;
	}

	public void sub(GLColor c) {
		this.r -= c.r;
		this.g -= c.g;
		this.b -= c.b;
		this.a -= c.a;
	}

	public void mul(GLColor c) {
		this.r *= c.r;
		this.g *= c.g;
		this.b *= c.b;
		this.a *= c.a;
	}

	/**
	 * 直接复制一个Color
	 * 
	 * @param c
	 * @return
	 */
	public GLColor copy(GLColor c) {
		return new GLColor(r, g, b, a);
	}

	/**
	 * 获得像素相加的Color
	 * 
	 * @param c
	 * @return
	 */
	public GLColor addCopy(GLColor c) {
		GLColor copy = new GLColor(r, g, b, a);
		copy.r += c.r;
		copy.g += c.g;
		copy.b += c.b;
		copy.a += c.a;
		return copy;
	}

	/**
	 * 获得像素相减的Color
	 * 
	 * @param c
	 * @return
	 */
	public GLColor subCopy(GLColor c) {
		GLColor copy = new GLColor(r, g, b, a);
		copy.r -= c.r;
		copy.g -= c.g;
		copy.b -= c.b;
		copy.a -= c.a;
		return copy;
	}

	/**
	 * 获得像素相乘的Color
	 * 
	 * @param c
	 * @return
	 */
	public GLColor mulCopy(GLColor c) {
		GLColor copy = new GLColor(r, g, b, a);
		copy.r *= c.r;
		copy.g *= c.g;
		copy.b *= c.b;
		copy.a *= c.a;
		return copy;
	}

	/**
	 * 返回ARGB
	 * 
	 * @return
	 */
	public int getARGB() {
		return getARGB(getRed(), getGreen(), getBlue(), getAlpha());
	}

	/**
	 * 返回RGB
	 * 
	 * @return
	 */
	public int getRGB() {
		return getRGB(getRed(), getGreen(), getBlue());
	}

	/**
	 * 获得24位色
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @return
	 */
	public static int getRGB(int r, int g, int b) {
		return getARGB(r, g, b, 0xff);
	}

	/**
	 * 获得RGB颜色
	 * 
	 * @param pixels
	 * @return
	 */
	public static int getRGB(int pixels) {
		int r = (pixels >> 16) & 0xff;
		int g = (pixels >> 8) & 0xff;
		int b = pixels & 0xff;
		return getRGB(r, g, b);
	}

	/**
	 * 获得32位色
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @param alpha
	 * @return
	 */
	public static int getARGB(int r, int g, int b, int alpha) {
		return (alpha << 24) | (r << 16) | (g << 8) | b;
	}

	/**
	 * 获得Aplha
	 * 
	 * @param color
	 * @return
	 */
	public static int getAlpha(int color) {
		return color >>> 24;
	}

	/**
	 * 获得Red
	 * 
	 * @param color
	 * @return
	 */
	public static int getRed(int color) {
		return (color >> 16) & 0xff;
	}

	/**
	 * 获得Green
	 * 
	 * @param color
	 * @return
	 */
	public static int getGreen(int color) {
		return (color >> 8) & 0xff;
	}

	/**
	 * 获得Blud
	 * 
	 * @param color
	 * @return
	 */
	public static int getBlue(int color) {
		return color & 0xff;
	}

	/**
	 * 像素前乘
	 * 
	 * @param argbColor
	 * @return
	 */
	public static int premultiply(int argbColor) {
		int a = argbColor >>> 24;
		if (a == 0) {
			return 0;
		} else if (a == 255) {
			return argbColor;
		} else {
			int r = (argbColor >> 16) & 0xff;
			int g = (argbColor >> 8) & 0xff;
			int b = argbColor & 0xff;
			r = (a * r + 127) / 255;
			g = (a * g + 127) / 255;
			b = (a * b + 127) / 255;
			return (a << 24) | (r << 16) | (g << 8) | b;
		}
	}

	/**
	 * 像素前乘
	 * 
	 * @param rgbColor
	 * @param alpha
	 * @return
	 */
	public static int premultiply(int rgbColor, int alpha) {
		if (alpha <= 0) {
			return 0;
		} else if (alpha >= 255) {
			return 0xff000000 | rgbColor;
		} else {
			int r = (rgbColor >> 16) & 0xff;
			int g = (rgbColor >> 8) & 0xff;
			int b = rgbColor & 0xff;

			r = (alpha * r + 127) / 255;
			g = (alpha * g + 127) / 255;
			b = (alpha * b + 127) / 255;
			return (alpha << 24) | (r << 16) | (g << 8) | b;
		}
	}

	/**
	 * 消除前乘像素
	 * 
	 * @param preARGBColor
	 * @return
	 */
	public static int unpremultiply(int preARGBColor) {
		int a = preARGBColor >>> 24;
		if (a == 0) {
			return 0;
		} else if (a == 255) {
			return preARGBColor;
		} else {
			int r = (preARGBColor >> 16) & 0xff;
			int g = (preARGBColor >> 8) & 0xff;
			int b = preARGBColor & 0xff;

			r = 255 * r / a;
			g = 255 * g / a;
			b = 255 * b / a;
			return (a << 24) | (r << 16) | (g << 8) | b;
		}
	}

	/**
	 * 获得r,g,b
	 * 
	 * @param pixel
	 * @return
	 */
	public static int[] getRGBs(final int pixel) {
		int[] rgbs = new int[3];
		rgbs[0] = (pixel >> 16) & 0xff;
		rgbs[1] = (pixel >> 8) & 0xff;
		rgbs[2] = (pixel) & 0xff;
		return rgbs;
	}

	public LColor getAWTColor() {
		return new LColor(getRed(), getGreen(), getBlue(), getAlpha());
	}

}
