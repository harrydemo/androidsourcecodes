package org.loon.framework.javase.game.core.graphics;

import java.awt.Color;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ImageProducer;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.HashMap;

import org.loon.framework.javase.game.core.LRelease;
import org.loon.framework.javase.game.core.LSystem;
import org.loon.framework.javase.game.core.graphics.device.LGraphics;
import org.loon.framework.javase.game.utils.GraphicsUtils;

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
 * @version 0.1.1
 */
public class LImage implements LRelease {

	private final static ArrayList<LImage> images = new ArrayList<LImage>(100);

	private HashMap<Integer, LImage> subs;

	private BufferedImage bufferedImage;

	private String fileName;

	private LGraphics g;

	private int width, height;

	private boolean isClose, isAutoDispose = true;

	public static LImage createImage(byte[] buffer) {
		return new LImage(GraphicsUtils.toolKit.createImage(buffer));
	}

	public static LImage createImage(byte[] buffer, int imageoffset,
			int imagelength) {
		return new LImage(GraphicsUtils.toolKit.createImage(buffer,
				imageoffset, imagelength));
	}

	public static LImage createImage(int width, int height) {
		return new LImage(width, height, false);
	}

	public static LImage createImage(int width, int height, boolean transparency) {
		return new LImage(width, height, transparency);
	}

	public static LImage createImage(int width, int height, int type) {
		return new LImage(width, height, type);
	}

	public static LImage createImage(String fileName) {
		return new LImage(fileName);
	}

	public static LImage createRGBImage(int[] rgb, int width, int height,
			boolean processAlpha) {
		if (rgb == null) {
			throw new NullPointerException();
		}
		if (width <= 0 || height <= 0) {
			throw new IllegalArgumentException();
		}
		BufferedImage img = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);
		if (!processAlpha) {
			int l = rgb.length;
			int[] rgbAux = new int[l];
			for (int i = 0; i < l; i++) {
				rgbAux[i] = rgb[i] | 0xff000000;
			}
			rgb = rgbAux;
		}
		img.setRGB(0, 0, width, height, rgb, 0, width);
		return new LImage(img);
	}

	public static LImage[] createImage(int count, int w, int h,
			boolean transparency) {
		LImage[] image = new LImage[count];
		for (int i = 0; i < image.length; i++) {
			image[i] = new LImage(w, h, transparency);
		}
		return image;
	}

	public static LImage[] createImage(int count, int w, int h, int type) {
		LImage[] image = new LImage[count];
		for (int i = 0; i < image.length; i++) {
			image[i] = new LImage(w, h, type);
		}
		return image;
	}

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
					throw new RuntimeException("Illegal transformation: "
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

	public LImage(int width, int height) {
		this(width, height, true);
	}

	public LImage(int width, int height, boolean transparency) {
		try {
			LSystem.gc(50, 1);
			this.width = width;
			this.height = height;
			this.bufferedImage = GraphicsUtils.createImage(width, height,
					transparency);
		} catch (Exception e) {
			try {
				LSystem.gc();
				this.width = width;
				this.height = height;
				this.bufferedImage = GraphicsUtils.createImage(width, height,
						transparency);
			} catch (Exception ex) {
				LSystem.gc();
			}
		}
		if (!images.contains(this)) {
			images.add(this);
		}
	}

	public LImage(int width, int height, int type) {
		this.width = width;
		this.height = height;
		this.bufferedImage = GraphicsUtils.createImage(width, height, type);
		if (!images.contains(this)) {
			images.add(this);
		}
	}

	public LImage(String fileName) {
		String res;
		if (fileName.startsWith("/")) {
			res = fileName.substring(1);
		} else {
			res = fileName;
		}
		this.fileName = fileName;
		BufferedImage img = GraphicsUtils.loadBufferedImage(res);
		setImage(img);
		if (!images.contains(this)) {
			images.add(this);
		}
	}

	public LImage(BufferedImage img) {
		this.setImage(img);
		if (!images.contains(this)) {
			images.add(this);
		}
	}

	public LImage(Image img) {
		GraphicsUtils.waitImage(img);
		this.setImage(img);
		if (!images.contains(this)) {
			images.add(this);
		}
	}

	public void setImage(LImage img) {
		this.width = img.getWidth();
		this.height = img.getHeight();
		this.bufferedImage = img.bufferedImage;
		this.isAutoDispose = img.isAutoDispose;
	}

	public void setImage(BufferedImage img) {
		this.width = img.getWidth();
		this.height = img.getHeight();
		this.bufferedImage = img;
	}

	public void setImage(Image img) {
		this.width = img.getWidth(null);
		this.height = img.getHeight(null);
		this.bufferedImage = GraphicsUtils.getBufferImage(img);
	}

	public Object clone() {
		return new LImage(bufferedImage);
	}

	public ImageProducer getSource() {
		return bufferedImage.getSource();
	}

	public boolean hasAlpha() {
		return bufferedImage.getColorModel().hasAlpha();
	}

	public LGraphics getLGraphics() {
		if (g == null || g.isClose()) {
			g = new LGraphics(bufferedImage);
		}
		return g;
	}

	public LGraphics create() {
		return new LGraphics(bufferedImage);
	}

	public LImage getMirrorImage() {
		if (bufferedImage != null) {
			synchronized (bufferedImage) {
				LImage image = null;
				if (bufferedImage.getTransparency() == Transparency.TRANSLUCENT) {
					image = LImage.createImage(width, height, true);
				} else {
					image = LImage.createImage(width, height, false);
				}
				LGraphics g = image.getLGraphics();
				g.drawMirrorImage(bufferedImage, 0, 0);
				g.dispose();
				return image;
			}
		}
		return this;
	}

	public BufferedImage getBufferedImage() {
		return bufferedImage;
	}

	public int getWidth() {
		return bufferedImage.getWidth();
	}

	public int getHeight() {
		return bufferedImage.getHeight();
	}

	public Color getColorAt(int x, int y) {
		return new Color(this.getRGBAt(x, y), true);
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
			return this.bufferedImage.getRGB(x, y);
		}
	}

	public WritableRaster getRaster() {
		return bufferedImage.getRaster();
	}

	public int[] getPixels() {
		int pixels[] = new int[width * height];
		bufferedImage.getRGB(0, 0, width, height, pixels, 0, width);
		return pixels;
	}

	public int[] getPixels(int pixels[]) {
		bufferedImage.getRGB(0, 0, width, height, pixels, 0, width);
		return pixels;
	}

	public int[] getPixels(int x, int y, int w, int h) {
		int[] pixels = new int[w * h];
		bufferedImage.getRGB(x, y, w, h, pixels, 0, w);
		return pixels;
	}

	public int[] getPixels(int offset, int stride, int x, int y, int width,
			int height) {
		int pixels[] = new int[width * height];
		bufferedImage.getRGB(x, y, width, height, pixels, offset, stride);
		return pixels;
	}

	public int[] getPixels(int pixels[], int offset, int stride, int x, int y,
			int width, int height) {
		bufferedImage.getRGB(x, y, width, height, pixels, offset, stride);
		return pixels;
	}

	public void setPixels(int[] pixels, int width, int height) {
		bufferedImage.setRGB(0, 0, width, height, pixels, 0, width);
	}

	public void setPixels(int[] pixels, int offset, int stride, int x, int y,
			int width, int height) {
		bufferedImage.setRGB(x, y, width, height, pixels, offset, stride);
	}

	public int[] setPixels(int[] pixels, int x, int y, int w, int h) {
		bufferedImage.setRGB(x, y, w, h, pixels, 0, w);
		return pixels;
	}

	public void setPixel(Color c, int x, int y) {
		bufferedImage.setRGB(x, y, c.getRGB());
	}

	public void setPixel(int rgb, int x, int y) {
		bufferedImage.setRGB(x, y, rgb);
	}

	public int getPixel(int x, int y) {
		return bufferedImage.getRGB(x, y);
	}

	public int getRGB(int x, int y) {
		return bufferedImage.getRGB(x, y);
	}

	public void setRGB(int rgb, int x, int y) {
		bufferedImage.setRGB(x, y, rgb);
	}

	public LImage getCacheSubImage(int x, int y, int w, int h) {
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
			subs.put(hashCode, img = new LImage(bufferedImage.getSubimage(x, y,
					w, h)));
		}
		return img;
	}

	public LImage getSubImage(int x, int y, int w, int h) {
		return new LImage(bufferedImage.getSubimage(x, y, w, h));
	}

	public LImage scaledInstance(int w, int h) {
		int width = getWidth();
		int height = getHeight();
		if (width == w && height == h) {
			return this;
		}
		return new LImage(GraphicsUtils.getResize(bufferedImage, w, h));
	}

	public void getRGB(int pixels[], int offset, int stride, int x, int y,
			int width, int height) {
		getPixels(pixels, offset, stride, x, y, width, height);
	}

	public int hashCode() {
		return GraphicsUtils.hashImage(bufferedImage);
	}

	public boolean isClose() {
		return isClose || bufferedImage == null;
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
		if (bufferedImage != null) {
			bufferedImage.flush();
			bufferedImage = null;
		}
		if (remove) {
			images.remove(this);
		}
	}

	public static void disposeAll() {
		for (LImage img : images) {
			if (img != null) {
				img.dispose(false);
				img = null;
			}
		}
		images.clear();
	}
}
