package org.loon.framework.javase.game.core.graphics;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.ImageProducer;
import java.awt.image.WritableRaster;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import org.loon.framework.javase.game.core.LRelease;
import org.loon.framework.javase.game.core.LSystem;
import org.loon.framework.javase.game.core.graphics.device.LGraphics;
import org.loon.framework.javase.game.core.graphics.opengl.GLColor;
import org.loon.framework.javase.game.core.graphics.opengl.GLLoader;
import org.loon.framework.javase.game.core.graphics.opengl.LTexture;
import org.loon.framework.javase.game.core.graphics.opengl.LTextures;
import org.loon.framework.javase.game.core.graphics.opengl.LTexture.Format;
import org.loon.framework.javase.game.utils.BufferUtils;
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

	private BufferedImage bufferedImage;

	private String fileName;

	private LGraphics g;

	private int width, height;

	private boolean isClose, isUpdate, isAutoDispose = true;

	private LTexture texture;

	private Format format = Format.DEFAULT;

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
				LTextures.destroyAll();
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

	public Buffer getByteBuffer() {
		return getByteBuffer(this);
	}

	public static Buffer getByteBuffer(LImage image) {
		boolean useByte = (image.getRaster().getTransferType() == DataBuffer.TYPE_BYTE);
		if (useByte) {
			return BufferUtils.createByteBuffer((byte[]) image.getRaster()
					.getDataElements(0, 0, image.getWidth(), image.getHeight(),
							null));
		}
		return image.hasAlpha() ? GLColor.argbToRGBABuffer(image.getPixels())
				: GLColor.argbToRGBBuffer(image.getPixels());
	}

	public LGraphics getLGraphics() {
		if (g == null || g.isClose()) {
			g = new LGraphics(bufferedImage);
			isUpdate = true;
		}
		return g;
	}

	public LGraphics create() {
		return new LGraphics(bufferedImage);
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
		if (bufferedImage != null) {
			bufferedImage.flush();
			bufferedImage = null;
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
		for (LImage img : images) {
			if (img != null) {
				img.dispose(false);
				img = null;
			}
		}
		images.clear();
	}

}
