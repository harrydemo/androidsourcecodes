package org.loon.framework.android.game.core.graphics;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import org.loon.framework.android.game.core.LRelease;
import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.core.graphics.device.LGraphics;
import org.loon.framework.android.game.core.graphics.opengl.GLColor;
import org.loon.framework.android.game.core.graphics.opengl.GLLoader;
import org.loon.framework.android.game.core.graphics.opengl.LTexture;
import org.loon.framework.android.game.core.graphics.opengl.LTextures;
import org.loon.framework.android.game.core.graphics.opengl.LTexture.Format;
import org.loon.framework.android.game.utils.BufferUtils;
import org.loon.framework.android.game.utils.GraphicsUtils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

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
public class LImage implements LRelease {

	// 自0.3.2版起新增的YUV处理类（构造同Android自带的YuvImage基本一致）
	public static class YuvImage {

		private static final int CSHIFT = 16;

		private static final int CYR = 19595;

		private static final int CYG = 38470;

		private static final int CYB = 7471;

		private static final int CUR = -11059;

		private static final int CUG = -21709;

		private static final int CUB = 32768;

		private static final int CVR = 32768;

		private static final int CVG = -27439;

		private static final int CVB = -5329;

		public static final int NV21 = 0x11;

		public static final int YUY2 = 0x14;

		public static int getBitsPerPixel(int format) {
			switch (format) {
			case NV21:
				return 12;
			case YUY2:
				return 16;
			}
			return 1;
		}

		private int format;

		private byte[] data;

		private int[] strides;

		private int width;

		private int height;

		public YuvImage(int width, int height, int format) {
			byte[] yuv = new byte[width * height * getBitsPerPixel(format)];
			set(yuv, format, width, height, null);
		}

		public YuvImage(byte[] yuv, int format, int width, int height,
				int[] strides) {
			set(yuv, format, width, height, strides);
		}

		public void set(byte[] yuv, int format, int width, int height, int[] s) {
			if (format != NV21 && format != YUY2) {
				throw new IllegalArgumentException("only support NV21 "
						+ "and YUY2 !");
			}

			if (width <= 0 || height <= 0) {
				throw new IllegalArgumentException(
						"width and height must large than 0 !");
			}

			if (yuv == null) {
				throw new IllegalArgumentException("yuv cannot be null !");
			}

			if (strides == null) {
				strides = calculateStrides(width, format);
			} else {
				strides = s;
			}
			this.data = yuv;
			this.format = format;
			this.width = width;
			this.height = height;
		}

		public int[] calculateOffsets(int left, int top) {
			int[] offsets = null;
			if (format == NV21) {
				offsets = new int[] {
						top * strides[0] + left,
						height * strides[0] + top / 2 * strides[1] + left / 2
								* 2 };
				return offsets;
			}

			if (format == YUY2) {
				offsets = new int[] { top * strides[0] + left / 2 * 4 };
				return offsets;
			}

			return offsets;
		}

		public static YuvImage toYuvImage(LImage img, int format) {
			return toYuvImage(img, format);
		}

		public static YuvImage toYuvImage(LImage img, int format, int paddings) {
			int width = img.getWidth();
			int height = img.getHeight();
			int stride = width + paddings;
			int[] pixels = new int[stride * height];
			img.getPixels(pixels, 0, stride, 0, 0, width, height);
			byte[] yuv = convertPixelsToYuvs(pixels, stride, height, format);
			int[] strides = null;
			if (format == NV21) {
				strides = new int[] { stride, stride };
			} else if (format == YUY2) {
				strides = new int[] { stride * 2 };
			}
			return new YuvImage(yuv, format, width, height, strides);
		}

		private static int[] calculateStrides(int width, int format) {
			int[] strides = null;
			if (format == NV21) {
				strides = new int[] { width, width };
				return strides;
			}

			if (format == YUY2) {
				strides = new int[] { width * 2 };
				return strides;
			}

			return strides;
		}

		private static void argb2yuv(int argb, byte[] yuv) {
			int[] rgbs = GLColor.getRGBs(argb);
			int r = rgbs[0];
			int g = rgbs[1];
			int b = rgbs[2];
			yuv[0] = (byte) ((CYR * r + CYG * g + CYB * b) >> CSHIFT);
			yuv[1] = (byte) (((CUR * r + CUG * g + CUB * b) >> CSHIFT) + 128);
			yuv[2] = (byte) (((CVR * r + CVG * g + CVB * b) >> CSHIFT) + 128);
		}

		private static byte[] convertPixelsToYuvs(int[] argb, int width,
				int height, int format) {
			byte[] yuv = new byte[width * height
					* YuvImage.getBitsPerPixel(format)];
			if (format == YuvImage.NV21) {
				int vuStart = width * height;
				byte[] yuvColor = new byte[3];
				for (int row = 0; row < height; ++row) {
					for (int col = 0; col < width; ++col) {
						int idx = row * width + col;
						argb2yuv(argb[idx], yuvColor);
						yuv[idx] = yuvColor[0];
						if ((row & 1) == 0 && (col & 1) == 0) {
							int offset = row / 2 * width + col / 2 * 2;
							yuv[vuStart + offset] = yuvColor[2];
							yuv[vuStart + offset + 1] = yuvColor[1];
						}
					}
				}
			} else if (format == YuvImage.YUY2) {
				byte[] yuvColor0 = new byte[3];
				byte[] yuvColor1 = new byte[3];
				for (int row = 0; row < height; ++row) {
					for (int col = 0; col < width; col += 2) {
						int idx = row * width + col;
						argb2yuv(argb[idx], yuvColor0);
						argb2yuv(argb[idx + 1], yuvColor1);
						int offset = idx / 2 * 4;
						yuv[offset] = yuvColor0[0];
						yuv[offset + 1] = yuvColor0[1];
						yuv[offset + 2] = yuvColor1[0];
						yuv[offset + 3] = yuvColor0[2];
					}
				}
			}
			return yuv;
		}

		public ByteBuffer getByteBuffer() {
			return BufferUtils.createByteBuffer(data);
		}

		public byte[] getYuvData() {
			return data;
		}

		public int getYuvFormat() {
			return format;
		}

		public int[] getStrides() {
			return strides;
		}

		public int getWidth() {
			return width;
		}

		public int getHeight() {
			return height;
		}

	}

	private final static ArrayList<LImage> images = new ArrayList<LImage>(100);

	private HashMap<Integer, LImage> subs;

	private Bitmap bitmap;

	private String fileName;

	private LGraphics g;

	private int width, height;

	private boolean isClose, isUpdate, isAutoDispose = true;

	private LTexture texture;

	private Format format = Format.DEFAULT;

	public static LImage createImage(InputStream in, boolean transparency) {
		return GraphicsUtils.loadImage(in, transparency);
	}

	public static LImage createImage(byte[] buffer) {
		return GraphicsUtils.loadImage(buffer, true);
	}

	public static LImage createImage(byte[] buffer, boolean transparency) {
		return GraphicsUtils.loadImage(buffer, transparency);
	}

	public static LImage createImage(int width, int height, boolean transparency) {
		return new LImage(width, height, transparency);
	}

	public static LImage createImage(int width, int height) {
		return new LImage(width, height, false);
	}

	public static LImage createImage(int width, int height, Config config) {
		return new LImage(width, height, config);
	}

	public static LImage createImage(byte[] imageData, int imageOffset,
			int imageLength, boolean transparency) {
		return GraphicsUtils.loadImage(imageData, imageOffset, imageLength,
				transparency);
	}

	public static LImage createImage(byte[] imageData, int imageOffset,
			int imageLength) {
		return GraphicsUtils.loadImage(imageData, imageOffset, imageLength,
				false);
	}

	public static LImage createImage(String fileName) {
		return GraphicsUtils.loadImage(fileName);
	}

	/**
	 * 以指定像素集合生成LImage文件
	 * 
	 * @param rgb
	 * @param width
	 * @param height
	 * @param processAlpha
	 * @return
	 */
	public static final LImage createRGBImage(int[] rgb, int width, int height,
			boolean processAlpha) {
		Bitmap bitmap = null;
		try {
			Bitmap.Config config;
			if (processAlpha) {
				config = Bitmap.Config.ARGB_8888;
			} else {
				config = Bitmap.Config.RGB_565;
			}
			bitmap = Bitmap.createBitmap(rgb, width, height, config);
		} catch (Exception e) {
			LSystem.gc();
			Bitmap.Config config;
			if (processAlpha) {
				config = Bitmap.Config.ARGB_8888;
			} else {
				config = Bitmap.Config.RGB_565;
			}
			bitmap = Bitmap.createBitmap(rgb, width, height, config);
		}
		return new LImage(bitmap);
	}

	/**
	 * 生成旋转为指定角度的图像
	 * 
	 * @param image
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param transform
	 * @return
	 */
	public static LImage createImage(LImage image, int x, int y, int width,
			int height, int transform) {
		int[] buf = new int[width * height];
		image.getPixels(buf, 0, width, x, y, width, height);
		int th;
		int tw;
		if ((transform & 4) != 0) {
			th = width;
			tw = height;
		} else {
			th = height;
			tw = width;
		}
		if (transform != 0) {
			int[] trans = new int[buf.length];
			int sp = 0;
			for (int sy = 0; sy < height; sy++) {
				int tx;
				int ty;
				int td;

				switch (transform) {
				case LGraphics.TRANS_ROT90:
					tx = tw - sy - 1;
					ty = 0;
					td = tw;
					break;
				case LGraphics.TRANS_ROT180:
					tx = tw - 1;
					ty = th - sy - 1;
					td = -1;
					break;
				case LGraphics.TRANS_ROT270:
					tx = sy;
					ty = th - 1;
					td = -tw;
					break;
				case LGraphics.TRANS_MIRROR:
					tx = tw - 1;
					ty = sy;
					td = -1;
					break;
				case LGraphics.TRANS_MIRROR_ROT90:
					tx = tw - sy - 1;
					ty = th - 1;
					td = -tw;
					break;
				case LGraphics.TRANS_MIRROR_ROT180:
					tx = 0;
					ty = th - sy - 1;
					td = 1;
					break;
				case LGraphics.TRANS_MIRROR_ROT270:
					tx = sy;
					ty = 0;
					td = tw;
					break;
				default:
					throw new RuntimeException("illegal transformation: "
							+ transform);
				}

				int tp = ty * tw + tx;
				for (int sx = 0; sx < width; sx++) {
					trans[tp] = buf[sp++];
					tp += td;
				}
			}
			buf = trans;
		}

		return createRGBImage(buf, tw, th, true);
	}

	/**
	 * 创建指定数量的LImage
	 * 
	 * @param count
	 * @param w
	 * @param h
	 * @param transparency
	 * @return
	 */
	public static LImage[] createImage(int count, int w, int h,
			boolean transparency) {
		LImage[] image = new LImage[count];
		for (int i = 0; i < image.length; i++) {
			image[i] = new LImage(w, h, transparency);
		}
		return image;
	}

	/**
	 * 创建指定数量的LImage
	 * 
	 * @param count
	 * @param w
	 * @param h
	 * @param config
	 * @return
	 */
	public static LImage[] createImage(int count, int w, int h, Config config) {
		LImage[] image = new LImage[count];
		for (int i = 0; i < image.length; i++) {
			image[i] = new LImage(w, h, config);
		}
		return image;
	}

	public LImage(String fileName) {
		String res;
		if (fileName.startsWith("/")) {
			res = fileName.substring(1);
		} else {
			res = fileName;
		}
		this.fileName = fileName;
		Bitmap bitmap = GraphicsUtils.loadBitmap(res);
		setBitmap(bitmap);
		if (!images.contains(this)) {
			images.add(this);
		}
	}

	public LImage(int width, int height) {
		this(width, height, false);
	}

	/**
	 * 构建一个LImage(true:ARGB8888或false:RGB565)
	 * 
	 * @param width
	 * @param height
	 * @param transparency
	 */
	public LImage(int width, int height, boolean transparency) {
		try {
			LSystem.gc(100, 1);
			this.width = width;
			this.height = height;
			if (transparency) {
				this.bitmap = Bitmap.createBitmap(width, height,
						Bitmap.Config.ARGB_8888);
			} else {
				this.bitmap = Bitmap.createBitmap(width, height,
						Bitmap.Config.RGB_565);
			}
		} catch (Exception e) {
			try {
				GraphicsUtils.destroy();
				LTextures.destroyAll();
				LSystem.gc();
				this.width = width;
				this.height = height;
				if (transparency) {
					this.bitmap = Bitmap.createBitmap(width, height,
							Bitmap.Config.ARGB_8888);
				} else {
					this.bitmap = Bitmap.createBitmap(width, height,
							Bitmap.Config.RGB_565);
				}
			} catch (Exception ex) {
				LSystem.gc();
			}
		}
		if (!images.contains(this)) {
			images.add(this);
		}
	}

	public LImage(int width, int height, Config config) {
		this.width = width;
		this.height = height;
		this.bitmap = Bitmap.createBitmap(width, height, config);
		if (!images.contains(this)) {
			images.add(this);
		}
	}

	public LImage(LImage img) {
		this(img.getBitmap());
	}

	public LImage(Bitmap bitmap) {
		setBitmap(bitmap);
		if (!images.contains(this)) {
			images.add(this);
		}
	}

	public void setBitmap(Bitmap bitmap) {
		this.width = bitmap.getWidth();
		this.height = bitmap.getHeight();
		this.bitmap = bitmap;
	}

	public Config getConfig() {
		Config config = bitmap.getConfig();
		if (config == null) {
			return Config.ARGB_8888;
		}
		return config;
	}

	public LImage clone() {
		return new LImage(bitmap);
	}

	public boolean hasAlpha() {
		if (bitmap == null) {
			return false;
		}
		if (bitmap.getConfig() == Config.RGB_565) {
			return false;
		}
		return bitmap.hasAlpha();
	}

	public LGraphics getLGraphics() {
		if (g == null || g.isClose()) {
			g = new LGraphics(bitmap);
			isUpdate = true;
		}
		return g;
	}

	public LGraphics create() {
		return new LGraphics(bitmap);
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public int getWidth() {
		return bitmap.getWidth();
	}

	public int getHeight() {
		return bitmap.getHeight();
	}

	public int[] getPixels() {
		int pixels[] = new int[width * height];
		bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
		return pixels;
	}

	public int[] getPixels(int pixels[]) {
		bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
		return pixels;
	}

	public int[] getPixels(int x, int y, int w, int h) {
		int[] pixels = new int[w * h];
		bitmap.getPixels(pixels, 0, w, x, y, w, h);
		return pixels;
	}

	public int[] getPixels(int offset, int stride, int x, int y, int w, int h) {
		int pixels[] = new int[w * h];
		bitmap.getPixels(pixels, offset, stride, x, y, w, h);
		return pixels;
	}

	public int[] getPixels(int pixels[], int offset, int stride, int x, int y,
			int width, int height) {
		bitmap.getPixels(pixels, offset, stride, x, y, width, height);
		return pixels;
	}

	public void setPixels(int[] pixels, int w, int h) {
		bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
	}

	public void setPixels(int[] pixels, int offset, int stride, int x, int y,
			int width, int height) {
		bitmap.setPixels(pixels, offset, stride, x, y, width, height);
	}

	public int[] setPixels(int[] pixels, int x, int y, int w, int h) {
		bitmap.setPixels(pixels, 0, w, x, y, w, h);
		return pixels;
	}

	public int getPixel(int x, int y) {
		return bitmap.getPixel(x, y);
	}

	public int[] getRGB(int pixels[], int offset, int stride, int x, int y,
			int width, int height) {
		bitmap.getPixels(pixels, offset, stride, x, y, width, height);
		return pixels;
	}

	public int getRGB(int x, int y) {
		return bitmap.getPixel(x, y);
	}

	public void setPixel(LColor c, int x, int y) {
		bitmap.setPixel(x, y, c.getRGB());
	}

	public void setPixel(int rgb, int x, int y) {
		bitmap.setPixel(x, y, rgb);
	}

	public void setRGB(int rgb, int x, int y) {
		bitmap.setPixel(x, y, rgb);
	}


	/**
	 * 变更图像Config设置
	 * 
	 * @param config
	 */
	public void convertConfig(android.graphics.Bitmap.Config config) {
		if (!bitmap.getConfig().equals(config)) {
			boolean flag = bitmap.isMutable();
			Bitmap tmp = bitmap.copy(config, flag);
			bitmap = tmp;
			if (g != null && !g.isClose()) {
				g.dispose();
				g = new LGraphics(bitmap);
			}
		}
	}

	/**
	 * 截小图(有缓存)
	 * 
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @return
	 */
	public LImage getCacheSubImage(int x, int y, int w, int h) {
		return getCacheSubImage(x, y, w, h, bitmap.getConfig());
	}

	/**
	 * 截小图(有缓存)
	 * 
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @param transparency
	 * @return
	 */
	public LImage getCacheSubImage(int x, int y, int w, int h, Config config) {
		if (subs == null) {
			subs = new HashMap<Integer, LImage>(10);
		}
		int hashCode = 1;
		hashCode = LSystem.unite(hashCode, x);
		hashCode = LSystem.unite(hashCode, y);
		hashCode = LSystem.unite(hashCode, w);
		hashCode = LSystem.unite(hashCode, h);
		LImage img = (LImage) subs.get(hashCode);
		if (img == null) {
			subs.put(hashCode, img = GraphicsUtils.drawClipImage(this, w, h, x,
					y, config));
		}
		return img;
	}

	/**
	 * 截小图
	 * 
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @param transparency
	 * @return
	 */
	public LImage getSubImage(int x, int y, int w, int h, Config config) {
		return GraphicsUtils.drawClipImage(this, w, h, x, y, config);
	}

	/**
	 * 截小图
	 * 
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @return
	 */
	public LImage getSubImage(int x, int y, int w, int h) {
		return GraphicsUtils
				.drawClipImage(this, w, h, x, y, bitmap.getConfig());
	}

	/**
	 * 扩充图像为指定大小
	 * 
	 * @param w
	 * @param h
	 * @return
	 */
	public LImage scaledInstance(int w, int h) {
		int width = getWidth();
		int height = getHeight();
		if (width == w && height == h) {
			return this;
		}
		Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);
		return new LImage(resizedBitmap);
	}

	public LColor getColorAt(int x, int y) {
		return new LColor(this.getRGBAt(x, y), true);
	}

	public int getRGBAt(int x, int y) {
		if (x >= this.getWidth()) {
			throw new IndexOutOfBoundsException("X is out of bounds: " + x
					+ "," + this.getWidth());
		} else if (y >= this.getHeight()) {
			throw new IndexOutOfBoundsException("Y is out of bounds: " + y
					+ "," + this.getHeight());
		} else if (x < 0) {
			throw new IndexOutOfBoundsException("X is out of bounds: " + x);
		} else if (y < 0) {
			throw new IndexOutOfBoundsException("Y is out of bounds: " + y);
		} else {
			return bitmap.getPixel(x, y);
		}
	}

	/**
	 * 返回LImage的hash序列
	 * 
	 */
	public int hashCode() {
		return GraphicsUtils.hashBitmap(bitmap);
	}

	public Format getFormat() {
		return format;
	}

	public void setFormat(Format format) {
		this.format = format;
		this.isUpdate = true;
	}

	public LTexture getTexture() {
		if (texture == null || isUpdate) {
			setAutoDispose(false);
			LTexture tmp = texture;
			texture = new LTexture(GLLoader.getTextureData(this), format);
			if (tmp != null) {
				tmp.dispose();
				tmp = null;
			}
			isUpdate = false;
		}
		return texture;
	}

	/**
	 * 判定当前LImage是否已被关闭
	 * 
	 * @return
	 */
	public boolean isClose() {
		return isClose || bitmap == null
				|| (bitmap != null ? bitmap.isRecycled() : false);
	}

	public boolean isAutoDispose() {
		return isAutoDispose && !isClose();
	}

	public void setAutoDispose(boolean dispose) {
		this.isAutoDispose = dispose;
	}

	public LPixmapData newPixmap() {
		return new LPixmapData(this);
	}

	public String getPath() {
		return fileName;
	}

	public void dispose() {
		dispose(true);
	}

	private void dispose(boolean remove) {
		isClose = true;
		subs = null;
		if (bitmap != null) {
			bitmap.recycle();
			bitmap = null;
		}
		if (texture != null && isAutoDispose) {
			texture.dispose();
			texture = null;
		}
		if (remove) {
			images.remove(this);
		}
	}

	public static void disposeAll() {
		if (images.size() > 0) {
			for (LImage img : images) {
				if (img != null) {
					img.dispose(false);
					img = null;
				}
			}
			images.clear();
		}
	}
}
