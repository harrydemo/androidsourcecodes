package org.loon.framework.android.game.core.graphics.opengl;

import java.nio.FloatBuffer;

import org.loon.framework.android.game.action.collision.CollisionMask;
import org.loon.framework.android.game.action.sprite.Mask;
import org.loon.framework.android.game.core.LRelease;
import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.core.geom.Polygon;
import org.loon.framework.android.game.core.geom.Shape;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.graphics.LPixmap;
import org.loon.framework.android.game.utils.BufferUtils;

import android.graphics.Bitmap;
import android.opengl.GLUtils;

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
public class LTexture implements LRelease {

	public static final int TOP_LEFT = 0;

	public static final int TOP_RIGHT = 1;

	public static final int BOTTOM_RIGHT = 2;

	public static final int BOTTOM_LEFT = 3;
	
	public static boolean ALL_LINEAR = false;

	public static boolean ALL_NEAREST = false;

	public static void AUTO_LINEAR() {
		if (!LSystem.isEmulator() && !GLEx.isPixelFlinger()
				&& (LSystem.scaleWidth != 1 || LSystem.scaleHeight != 1)) {
			LTexture.ALL_LINEAR = true;
		}
	}

	public static void AUTO_NEAREST() {
		if (!LSystem.isEmulator()
				&& (LSystem.scaleWidth != 1 || LSystem.scaleHeight != 1)) {
			LTexture.ALL_NEAREST = true;
		}
	}
	
	public static enum Format {
		DEFAULT, NEAREST, LINEAR, FONT, SPEED, STATIC, BILINEAR, REPEATING, REPEATING_BILINEAR, REPEATING_BILINEAR_PREMULTIPLYALPHA;
	}

	private final int[] GENERATED_TEXTUREID = new int[1];

	private GLColor[] colors;
	
	private int hashCode = 1;

	private int subX, subY, subWidth, subHeight;

	LTextureData imageData;

	LTexture parent, child;

	boolean replace, reload, string;

	boolean isLoaded, isClose, hasAlpha;

	int width, texWidth;

	int height, texHeight;

	int textureID, bufferID;

	float xOff = 0.0f;

	float yOff = 0.0f;

	float widthRatio = 1.0f;

	float heightRatio = 1.0f;

	final int[] crops = { 0, 0, 0, 0 };

	float dataCords[];

	FloatBuffer data;

	int dataSize;

	int vertexSize;

	int texSize;

	Format format;

	String lazyName;

	boolean isStatic;

	private LTexture() {
		format = Format.DEFAULT;
		imageData = null;
		checkReplace();
	}

	public LTexture(LTexture texture) {
		if (texture == null) {
			throw new RuntimeException("texture is Null !");
		}
		this.imageData = texture.imageData;
		this.parent = texture.parent;
		this.format = texture.format;
		this.hasAlpha = texture.hasAlpha;
		this.textureID = texture.textureID;
		this.bufferID = texture.bufferID;
		this.width = texture.width;
		this.height = texture.height;
		this.setVertCords(width, height);
		this.texWidth = texture.texWidth;
		this.texHeight = texture.texHeight;
		this.xOff = texture.xOff;
		this.yOff = texture.yOff;
		this.widthRatio = texture.widthRatio;
		this.heightRatio = texture.heightRatio;
		this.replace = texture.replace;
		this.isLoaded = texture.isLoaded;
		this.isClose = texture.isClose;
		this.setTexCords(xOff, yOff, widthRatio, heightRatio);
		System.arraycopy(texture.crops, 0, crops, 0, crops.length);
	}

	public LTexture(String res) {
		this(res, Format.DEFAULT);
	}

	public LTexture(String res, Format format) {
		this(GLLoader.getTextureData(res), format);
	}

	public LTexture(LPixmap pix) {
		this(pix, Format.DEFAULT);
	}

	public LTexture(LPixmap pix, Format format) {
		this(GLLoader.getTextureData(pix), format);
	}

	public LTexture(int width, int height, Format format) {
		this(width, height, true, format);
	}

	public LTexture(int width, int height, boolean hasAlpha, Format format) {
		this(new LPixmap(width, height, hasAlpha), format);
	}

	public LTexture(int width, int height, boolean hasAlpha) {
		this(new LPixmap(width, height, hasAlpha), Format.DEFAULT);
	}

	public LTexture(LTextureData data) {
		this(data, Format.DEFAULT);
	}

	public LTexture(LTextureData d, Format format) {
		this.format = format;
		this.imageData = d;
		this.texWidth = d.texWidth;
		this.texHeight = d.texHeight;
		this.width = d.width;
		this.height = d.height;
		this.checkReplace();
	}

	public final String getFileName() {
		if (imageData != null) {
			return imageData.fileName;
		}
		return null;
	}

	public final void checkReplace() {
		this.replace = Format.BILINEAR == format || Format.BILINEAR == format
				|| Format.REPEATING_BILINEAR == format;
		this.isStatic = format == Format.SPEED || format == Format.STATIC;
	}

	public synchronized final void loadTexture() {
		if (parent != null) {
			parent.loadTexture();
			return;
		}
		if (imageData == null || isLoaded || GLEx.gl == null) {
			return;
		}
		if (imageData.source == null && imageData.pixmap != null) {
			imageData = GLLoader.getTextureData(imageData.pixmap);
		}
		if (imageData.source == null && imageData.fileName != null) {
			imageData = GLLoader.getTextureData(imageData.fileName);
		}
		if (imageData.source == null) {
			return;
		}
		isLoaded = true;
		loadTextureBuffer();
		setFormat(format);
		LTextures.loadTexture(this);
	}

	private synchronized void loadTextureBuffer() {
		if (!reload) {
			this.textureID = createTextureID();
			this.reload = false;
		}
		bind();

		hasAlpha = imageData.hasAlpha;

		setWidth(imageData.width);
		setHeight(imageData.height);
		setTextureWidth(imageData.texWidth);
		setTextureHeight(imageData.texHeight);

		Bitmap bind = null;
		if (hasAlpha) {
			bind = Bitmap.createBitmap(imageData.texWidth, imageData.texHeight,
					Bitmap.Config.ARGB_8888);
		} else {
			bind = Bitmap.createBitmap(imageData.texWidth, imageData.texHeight,
					Bitmap.Config.RGB_565);
		}
		bind.setPixels(imageData.source, 0, imageData.texWidth, 0, 0,
				imageData.texWidth, imageData.texHeight);

		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bind, 0);

		if (bind != null) {
			bind.recycle();
			bind = null;
		}
		if (GLEx.isVbo()) {
			GLEx.updateHardwareBuff(this);
		}
	}

	public void draw(Bitmap bit, int x, int y) {
		if (GLEx.gl != null) {
			loadTexture();
			bind();
			int level = 0;
			int height = bit.getHeight();
			int width = bit.getWidth();

			while (height >= 1 || width >= 1 && level < 4) {
				GLUtils.texSubImage2D(GL10.GL_TEXTURE_2D, level, x, y, bit);
				if (height == 1 || width == 1) {
					break;
				}

				level++;
				if (height > 1) {
					height /= 2;
				}
				if (width > 1) {
					width /= 2;
				}
				Bitmap bitmap2 = Bitmap.createScaledBitmap(bit, width, height,
						true);
				bit.recycle();
				bit = bitmap2;
			}
			if (GLEx.isVbo()) {
				GLEx.updateHardwareBuff(this);
			}
		}
	}

	public void draw(LPixmap pixmap, int x, int y) {
		if (imageData != null && imageData.pixmap != null) {
			if (imageData.source != null) {
				imageData.source = null;
			}
			if (imageData.fileName != null) {
				imageData.fileName = null;
			}
			imageData.pixmap.drawPixmap(pixmap, x, y);
			reload();
			return;
		}
		if (GLEx.gl != null) {
			loadTexture();
			bind();
			Bitmap bind;
			if (pixmap.hasAlpha()) {
				bind = Bitmap.createBitmap(pixmap.getTexWidth(), pixmap
						.getTexHeight(), Bitmap.Config.ARGB_8888);
			} else {
				bind = Bitmap.createBitmap(pixmap.getTexWidth(), pixmap
						.getTexHeight(), Bitmap.Config.RGB_565);
			}
			bind.setPixels(pixmap.getData(), 0, pixmap.getTexWidth(), 0, 0,
					pixmap.getTexWidth(), pixmap.getTexHeight());

			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bind, 0);

			if (bind != null) {
				bind.recycle();
				bind = null;
			}
			if (GLEx.isVbo()) {
				GLEx.updateHardwareBuff(this);
			}
		}
	}

	public void setStringFlag(boolean s) {
		this.string = s;
	}

	public boolean isStringFlag() {
		return this.string;
	}

	public void reload() {
		this.isLoaded = false;
		this.reload = true;
		this.hashCode = 1;
	}

	private synchronized int createTextureID() {
		if (textureID > 0) {
			GLEx.deleteTexture(this.textureID);
			this.textureID = -1;
			if (GLEx.isVbo() && bufferID > 0) {
				try {
					GLEx.deleteBuffer(this.bufferID);
					this.bufferID = -1;
				} catch (Exception e) {
				}
			}
		}
		GLEx.gl10.glGenTextures(1, GENERATED_TEXTUREID, 0);
		return GENERATED_TEXTUREID[0];
	}

	public boolean isReplace() {
		return replace;
	}

	public void setFormat(Format format) {

		int minFilter = GL10.GL_NEAREST;
		int maxFilter = GL10.GL_NEAREST;
		int wrapS = GL10.GL_CLAMP_TO_EDGE;
		int wrapT = GL10.GL_CLAMP_TO_EDGE;
		int texEnv = GL10.GL_MODULATE;

		if (imageData != null) {
			if (format == Format.DEFAULT && imageData.hasAlpha) {
				format = Format.SPEED;
			} else if (format == Format.DEFAULT && !imageData.hasAlpha) {
				format = Format.STATIC;
				this.format = format;
			}
		}

		switch (format) {
		case DEFAULT:
		case NEAREST:
			break;
		case FONT:
		case LINEAR:
			minFilter = GL10.GL_LINEAR;
			maxFilter = GL10.GL_LINEAR;
			wrapS = GL10.GL_CLAMP_TO_EDGE;
			wrapT = GL10.GL_CLAMP_TO_EDGE;
			texEnv = GL10.GL_MODULATE;
			break;
		case STATIC:
		case SPEED:
			minFilter = GL10.GL_NEAREST;
			maxFilter = GL10.GL_NEAREST;
			wrapS = GL10.GL_REPEAT;
			wrapT = GL10.GL_REPEAT;
			texEnv = GL10.GL_REPLACE;
			break;
		case BILINEAR:
			minFilter = GL10.GL_LINEAR;
			maxFilter = GL10.GL_LINEAR;
			wrapS = GL10.GL_CLAMP_TO_EDGE;
			wrapT = GL10.GL_CLAMP_TO_EDGE;
			texEnv = GL10.GL_REPLACE;
			break;
		case REPEATING:
			minFilter = GL10.GL_NEAREST;
			maxFilter = GL10.GL_NEAREST;
			wrapS = GL10.GL_REPEAT;
			wrapT = GL10.GL_REPEAT;
			texEnv = GL10.GL_REPLACE;
			break;
		case REPEATING_BILINEAR:
			minFilter = GL10.GL_LINEAR;
			maxFilter = GL10.GL_LINEAR;
			wrapS = GL10.GL_REPEAT;
			wrapT = GL10.GL_REPEAT;
			texEnv = GL10.GL_REPLACE;
			break;
		case REPEATING_BILINEAR_PREMULTIPLYALPHA:
			minFilter = GL10.GL_LINEAR;
			maxFilter = GL10.GL_LINEAR;
			wrapS = GL10.GL_REPEAT;
			wrapT = GL10.GL_REPEAT;
			texEnv = GL10.GL_MODULATE;
			break;
		}

		if (!string && format != Format.FONT) {
			if (ALL_LINEAR && !ALL_NEAREST) {
				minFilter = GL10.GL_LINEAR;
				maxFilter = GL10.GL_LINEAR;
			} else if (ALL_NEAREST && !ALL_LINEAR) {
				minFilter = GL10.GL_NEAREST;
				maxFilter = GL10.GL_NEAREST;
			} else if (ALL_NEAREST && ALL_LINEAR) {
				minFilter = GL10.GL_NEAREST;
				maxFilter = GL10.GL_LINEAR;
			}
		}

		GL10 gl10 = GLEx.gl10;
		if (gl10 == null) {
			return;
		}
		gl10.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER,
				minFilter);
		gl10.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER,
				maxFilter);
		gl10.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, wrapS);
		gl10.glTexParameterf(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, wrapT);
		gl10.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, texEnv);
	}

	public int getTextureID() {
		return textureID;
	}

	public void setVertCords(int width, int height) {
		if (dataCords == null) {

			dataCords = new float[] { 0.0f, 0.0f, width, 0.0f, 0.0f, height,
					width, height, xOff, yOff, widthRatio, yOff, xOff,
					heightRatio, widthRatio, heightRatio };
			data = BufferUtils.createFloatBuffer(dataCords);

			dataSize = data.capacity() * 4;

			vertexSize = 8 * 4;

			texSize = 8 * 4;
		}
		dataCords[0] = 0;
		dataCords[1] = 0;
		dataCords[2] = width;
		dataCords[3] = 0;
		dataCords[4] = 0;
		dataCords[5] = height;
		dataCords[6] = width;
		dataCords[7] = height;

		this.width = width;
		this.height = height;

		BufferUtils.replaceFloats(data, dataCords);

	}

	public void setTexCords(float texXOff, float texYOff, float texWidthRatio,
			float texHeightRatio) {
		if (dataCords == null) {

			dataCords = new float[] { 0.0f, 0.0f, imageData.width, 0.0f, 0.0f,
					imageData.height, imageData.width, imageData.height, xOff,
					yOff, widthRatio, yOff, xOff, heightRatio, widthRatio,
					heightRatio };
			data = BufferUtils.createFloatBuffer(dataCords);

			dataSize = data.capacity() * 4;

			vertexSize = 8 * 4;

			texSize = 8 * 4;
		}
		dataCords[8] = texXOff;
		dataCords[9] = texYOff;
		dataCords[10] = texWidthRatio;
		dataCords[11] = texYOff;
		dataCords[12] = texXOff;
		dataCords[13] = texHeightRatio;
		dataCords[14] = texWidthRatio;
		dataCords[15] = texHeightRatio;

		this.xOff = texXOff;
		this.yOff = texYOff;
		this.widthRatio = texWidthRatio;
		this.heightRatio = texHeightRatio;
		BufferUtils.replaceFloats(data, dataCords);
	}

	public void setWidth(int width) {
		this.width = width;
		setVertCords(width, height);
	}

	public void setHeight(int height) {
		this.height = height;
		setVertCords(width, height);
	}

	public int getWidth() {
		if (width == 0 && imageData != null) {
			return imageData.getWidth();
		}
		return width;
	}

	public int getHeight() {
		if (height == 0 && imageData != null) {
			return imageData.getHeight();
		}
		return height;
	}

	public void setTextureWidth(int textureWidth) {
		setTextureSize(textureWidth, texHeight);
	}

	public void setTextureHeight(int textureHeight) {
		setTextureSize(texWidth, textureHeight);
	}

	public float getTextureWidth() {
		return texWidth;
	}

	public float getTextureHeight() {
		return texHeight;
	}

	public void setTextureSize(int textureWidth, int textureHeight) {
		this.texWidth = textureWidth;
		this.texHeight = textureHeight;
		setTexCordRatio();
	}

	private void setTexCordRatio() {
		widthRatio = (float) width / (texWidth < 1 ? width : texWidth);
		heightRatio = (float) height / (texHeight < 1 ? height : texHeight);
		setTexCords(xOff, yOff, widthRatio, heightRatio);
	}

	public LTexture getSubTexture(final int x, final int y, final int width,
			final int height) {
		this.loadTexture();
		LTexture sub = new LTexture();
		sub.parent = LTexture.this;
		sub.textureID = textureID;
		sub.imageData = imageData;
		sub.hasAlpha = hasAlpha;
		sub.replace = replace;
		sub.isStatic = isStatic;
		sub.format = format;
		sub.width = width;
		sub.height = height;
		sub.texWidth = texWidth;
		sub.texHeight = texHeight;
		sub.setVertCords(width, height);
		sub.xOff = (((float) x / this.width) * widthRatio) + xOff;
		sub.yOff = (((float) y / this.height) * heightRatio) + yOff;
		sub.widthRatio = (((float) width / LTexture.this.width) * widthRatio)
				+ sub.xOff;
		sub.heightRatio = (((float) height / LTexture.this.height) * heightRatio)
				+ sub.yOff;
		sub.setTexCords(sub.xOff, sub.yOff, sub.widthRatio, sub.heightRatio);
		crop(sub, x, y, width, height);
		if (GLEx.isVbo()) {
			sub.bufferID = GLEx.createBufferID();
			GLEx.bufferDataARR(sub.bufferID, sub.data, GL11.GL_STATIC_DRAW);
		}
		this.child = sub;
		return sub;
	}

	public LTexture scale(float scale) {
		int nW = (int) (width * scale);
		int nH = (int) (height * scale);
		return copy(nW, nH, false, false);
	}

	public LTexture scale(int width, int height) {
		return copy(width, height, false, false);
	}

	public LTexture copy() {
		return copy(width, height, false, false);
	}

	public LTexture flip(boolean flipHorizontal, boolean flipVertial) {
		return copy(width, height, flipHorizontal, flipVertial);
	}

	private LTexture copy(int width, int height, boolean flipHorizontal,
			boolean flipVertial) {
		this.loadTexture();
		LTexture copy = new LTexture();
		copy.parent = this;
		copy.textureID = textureID;
		copy.replace = replace;
		copy.isStatic = isStatic;
		copy.format = format;
		copy.hasAlpha = hasAlpha;
		copy.setVertCords(width, height);
		copy.texWidth = texWidth;
		copy.texHeight = texHeight;
		copy.setTexCords(xOff, yOff, widthRatio, heightRatio);
		if (flipHorizontal) {
			swap(8, 10, copy.dataCords);
			swap(12, 14, copy.dataCords);
		}
		if (flipVertial) {
			swap(9, 13, copy.dataCords);
			swap(11, 15, copy.dataCords);
		}
		copy.xOff = dataCords[8];
		copy.yOff = dataCords[9];
		copy.widthRatio = dataCords[14];
		copy.heightRatio = dataCords[15];
		BufferUtils.replaceFloats(copy.data, copy.dataCords);
		System.arraycopy(crops, 0, copy.crops, 0, crops.length);
		if (GLEx.isVbo()) {
			copy.bufferID = GLEx.createBufferID();
			GLEx.bufferDataARR(copy.bufferID, copy.data, GL11.GL_STATIC_DRAW);
		}
		this.child = copy;
		return copy;
	}

	private void crop(LTexture texture, int x, int y, int width, int height) {
		texture.crops[0] = x;
		texture.crops[1] = height + y;
		texture.crops[2] = width;
		texture.crops[3] = -height;
		texture.subX = x;
		texture.subY = y;
		texture.subWidth = width;
		texture.subHeight = height;
	}

	private void swap(int idx1, int idx2, float[] texCords) {
		float tmp = texCords[idx1];
		texCords[idx1] = texCords[idx2];
		texCords[idx2] = tmp;
	}

	public LTexture getParent() {
		return parent;
	}

	public synchronized void bind() {
		GLEx.gl10.glPixelStorei(GL.GL_UNPACK_ALIGNMENT, 1);
		GLEx.gl10.glBindTexture(GL.GL_TEXTURE_2D, textureID);
	}

	public synchronized void bind(int unit) {
		GLEx.gl10.glActiveTexture(GL10.GL_TEXTURE0 + unit);
		GLEx.gl10.glPixelStorei(GL.GL_UNPACK_ALIGNMENT, 1);
		GLEx.gl10.glBindTexture(GL10.GL_TEXTURE_2D, textureID);
	}

	public int hashCode() {
		if (hashCode == 1 && imageData.source != null) {
			int[] buffer = imageData.source;
			int limit = buffer.length;
			for (int j = 0; j < limit; j++) {
				hashCode = LSystem.unite(hashCode, buffer[j]);
			}
			if (dataCords != null) {
				for (int i = 0; i < dataCords.length; i++) {
					hashCode = LSystem.unite(hashCode, dataCords[i]);
				}
			}
		}
		return hashCode;
	}

	public boolean isLoaded() {
		return isLoaded;
	}

	public void setLoaded(boolean isLoaded) {
		this.isLoaded = isLoaded;
	}

	public LPixmap newPixmap() {
		if (imageData != null) {
			if (imageData.pixmap != null) {
				return imageData.pixmap;
			} else {
				return new LPixmap(imageData);
			}
		}
		return null;
	}

	public LTextureData getImageData() {
		return imageData;
	}

	public Format getFormat() {
		return format;
	}

	public void dispose() {
		dispose(true);
	}

	void dispose(boolean remove) {
		if (child != null && !child.isClose) {
			return;
		}
		if (remove) {
			LTextures.removeTexture(this);
		}
		GLEx.deleteTexture(textureID);
		GLEx.deleteBuffer(bufferID);
		isLoaded = false;
		isClose = true;
	}

	public boolean isRecycled() {
		return this.isClose;
	}

	private Shape shapeCache;

	private Mask maskCache;

	public Shape getShape() {
		if (shapeCache != null) {
			return shapeCache;
		}
		LImage shapeImage = getImage();
		if (shapeImage != null) {
			Polygon polygon = CollisionMask.makePolygon(shapeImage);
			if (shapeImage != null) {
				shapeImage.dispose();
				shapeImage = null;
			}
			return (shapeCache = polygon);
		}
		throw new RuntimeException("Create texture for shape fail !");
	}

	public Mask getMask() {
		if (maskCache != null) {
			return maskCache;
		}
		LImage maskImage = getImage();
		if (maskImage != null) {
			Mask mask = CollisionMask.createMask(maskImage);
			if (maskImage != null) {
				maskImage.dispose();
				maskImage = null;
			}
			return (maskCache = mask);
		}
		throw new RuntimeException("Create texture for shape fail !");
	}

	public final LImage getImage() {
		LImage image = null;
		if (imageData != null) {
			if (imageData.source != null) {
				int[] data = imageData.source;
				if (data != null) {
					image = createPixelImage(data, imageData.texWidth,
							imageData.texHeight, imageData.width,
							imageData.height, imageData.hasAlpha);
				}
			} else if (imageData.pixmap != null) {
				int[] data = imageData.pixmap.getData();
				if (data != null) {
					image = createPixelImage(data, imageData.pixmap
							.getTexWidth(), imageData.pixmap.getTexHeight(),
							imageData.pixmap.getWidth(), imageData.pixmap
									.getHeight(), imageData.pixmap.hasAlpha());
				}
			} else if (imageData.fileName != null) {
				image = LImage.createImage(imageData.fileName);
			}
		}
		if (subWidth != 0 && subHeight != 0) {
			if (image != null) {
				LImage tmp = image.getSubImage(subX, subY, subWidth, subHeight);
				if (tmp != image) {
					if (image != null) {
						image.dispose();
						image = null;
					}
					return tmp;
				} else {
					return image;
				}
			}
		}
		return image;
	}

	private final static LImage createPixelImage(int[] pixels, int texWidth,
			int texHeight, int width, int height, boolean alpha) {
		LImage image = new LImage(texWidth, texHeight, alpha);
		image.setPixels(pixels, texWidth, texHeight);
		if (texWidth != width || texHeight != height) {
			LImage temp = image.getSubImage(0, 0, width, height);
			if (temp != image) {
				if (image != null) {
					image.dispose();
					image = null;
				}
				image = temp;
			}
		}
		return image;
	}

	public void setImageColor(float r, float g, float b, float a) {
		setColor(TOP_LEFT, r, g, b, a);
		setColor(TOP_RIGHT, r, g, b, a);
		setColor(BOTTOM_LEFT, r, g, b, a);
		setColor(BOTTOM_RIGHT, r, g, b, a);
	}

	public void setImageColor(float r, float g, float b) {
		setColor(TOP_LEFT, r, g, b);
		setColor(TOP_RIGHT, r, g, b);
		setColor(BOTTOM_LEFT, r, g, b);
		setColor(BOTTOM_RIGHT, r, g, b);
	}

	public void setImageColor(GLColor c) {
		if (c == null) {
			return;
		}
		setImageColor(c.r, c.g, c.b, c.a);
	}

	public void setColor(int corner, float r, float g, float b, float a) {
		if (colors == null) {
			colors = new GLColor[] { new GLColor(1, 1, 1, 1f),
					new GLColor(1, 1, 1, 1f), new GLColor(1, 1, 1, 1f),
					new GLColor(1, 1, 1, 1f) };
		}

		colors[corner].r = r;
		colors[corner].g = g;
		colors[corner].b = b;
		colors[corner].a = a;
	}

	public void setColor(int corner, float r, float g, float b) {
		if (colors == null) {
			colors = new GLColor[] { new GLColor(1, 1, 1, 1f),
					new GLColor(1, 1, 1, 1f), new GLColor(1, 1, 1, 1f),
					new GLColor(1, 1, 1, 1f) };
		}

		colors[corner].r = r;
		colors[corner].g = g;
		colors[corner].b = b;
	}
	public void glBegin() {
		if (GLEx.self != null) {
			GLEx.self.glBegin(GL.GL_TRIANGLE_STRIP, false);
		}
	}

	public void glEnd() {
		if (GLEx.self != null) {
			GLEx.self.glEnd();
			setImageColor(GLColor.white);
		}
	}

	public void draw(float x, float y) {
		draw(x, y, width, height);
	}

	public void draw(float x, float y, float width, float height) {
		if (GLEx.self != null) {
			GLEx.self.draw(this, colors, x, y, width, height, false);
		}
	}

	public void draw(float x, float y, GLColor c) {
		if (GLEx.self != null) {
			setImageColor(c);
			GLEx.self.draw(this, colors, x, y, width, height, false);
			setImageColor(GLColor.white);
		}
	}

	public void draw(float x, float y, float width, float height, GLColor c) {
		if (GLEx.self != null) {
			setImageColor(c);
			GLEx.self.draw(this, colors, x, y, width, height, false);
			setImageColor(GLColor.white);
		}
	}

	public void draw(float x, float y, float width, float height, float x1,
			float y1, float x2, float y2) {
		if (GLEx.self != null) {
			GLEx.self.draw(this, colors, x, y, width, height, x1, y1, x2, y2,
					false);
		}
	}
	public void freeCache() {
		if (imageData != null) {
			if (imageData.pixmap != null) {
				imageData.pixmap.clear();
				imageData.pixmap = null;
			}
			if (imageData.source != null) {
				imageData.source = null;
			}
		}
		if (shapeCache != null) {
			shapeCache = null;
		}
		if (maskCache != null) {
			maskCache.dispose();
			maskCache = null;
		}
	}

	public void destroy() {
		destroy(true);
	}

	public void destroy(boolean remove) {
		dispose(remove);
		freeCache();
		if (imageData != null) {
			imageData = null;
		}
	}

}
