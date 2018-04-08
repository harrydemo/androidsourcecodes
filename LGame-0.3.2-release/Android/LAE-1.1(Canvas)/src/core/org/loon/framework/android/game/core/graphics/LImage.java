package org.loon.framework.android.game.core.graphics;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.loon.framework.android.game.core.LRelease;
import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.core.graphics.device.LGraphics;
import org.loon.framework.android.game.utils.GraphicsUtils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

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
 * @email ceponline@yahoo.com.cn
 * @version 0.1.0
 */
public class LImage implements LRelease {

	private final static ArrayList<LImage> images = new ArrayList<LImage>(100);

	private HashMap<Integer, LImage> subs;

	private Bitmap bitmap;

	private String fileName;

	private LGraphics g;

	private int width, height;

	private boolean isClose, isAutoDispose = true;

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

	public LImage getMirrorImage() {
		if (bitmap != null) {
			synchronized (bitmap) {
				LImage image = LImage.createImage(width, height, bitmap
						.getConfig());
				LGraphics g = image.getLGraphics();
				g.drawMirrorBitmap(bitmap, 0, 0, true);
				g.dispose();
				return image;
			}
		}
		return this;
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
