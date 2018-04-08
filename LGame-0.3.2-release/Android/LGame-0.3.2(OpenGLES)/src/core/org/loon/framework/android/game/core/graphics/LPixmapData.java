package org.loon.framework.android.game.core.graphics;

import org.loon.framework.android.game.core.graphics.opengl.GL;
import org.loon.framework.android.game.core.graphics.opengl.GLEx;
import org.loon.framework.android.game.core.graphics.opengl.LTexture;
import org.loon.framework.android.game.utils.CollectionUtils;
import org.loon.framework.android.game.utils.GraphicsUtils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
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
// 自0.3.2版起新增的像素处理类
public class LPixmapData {

	private int width, height;

	private boolean hasAlpha;

	private boolean isUpdate, isClose;

	private int[] pixels, finalPixels;

	private Bitmap buffer;

	private LTexture texture;

	public LPixmapData(final int width, final int height) {
		init(width, height, true);
	}

	public LPixmapData(final int width, final int height, final boolean alpha) {
		init(width, height, alpha);
	}

	private void init(int width, int height, boolean alpha) {
		this.width = width;
		this.height = height;
		this.hasAlpha = alpha;
		this.pixels = new int[width * height];
		this.buffer = Bitmap.createBitmap(width, height,
				alpha ? Config.ARGB_8888 : Config.RGB_565);
		this.buffer.getPixels(pixels, 0, width, 0, 0, width, height);
		this.finalPixels = CollectionUtils.copyOf(pixels);
	}

	private LPixmapData(final int[] pixels, final int width, final int height) {
		init(width, height, true);
		this.buffer.setPixels(pixels, 0, width, 0, 0, width, height);
		this.finalPixels = CollectionUtils.copyOf(pixels);
	}

	public LPixmapData(String resName) {
		this(GraphicsUtils.loadImage(resName));
	}

	public LPixmapData(LImage pix) {
		this(pix.getPixels(), pix.getWidth(), pix.getHeight());
	}

	public LPixmapData(LPixmap pix) {
		this(pix.getData(), pix.getWidth(), pix.getHeight());
	}

	public int get(final int x, final int y) {
		if (x >= 0 && y >= 0 && x < width && y < height) {
			return pixels[x + y * width];
		} else {
			return 0;
		}
	}

	public void put(final int x, final int y, final int color) {
		if (x >= 0 && y >= 0 && x < width && y < height) {
			pixels[x + y * width] = color;
			isUpdate = true;
		}
	}

	public void reset() {
		if (isClose) {
			return;
		}
		this.pixels = CollectionUtils.copyOf(finalPixels);
	}

	public synchronized void update() {
		if (isClose) {
			return;
		}
		if (texture == null) {
			texture = new LTexture(width, height, hasAlpha);
		}
		if (!texture.isLoaded()) {
			texture.loadTexture();
			GLEx.gl10.glBindTexture(GL.GL_TEXTURE_2D, texture.getTextureID());
			GLUtils.texSubImage2D(GL.GL_TEXTURE_2D, 0, 0, 0, buffer);
			GLEx.gl10.glBindTexture(GL.GL_TEXTURE_2D, 0);
		} else {
			if (isUpdate) {
				buffer.setPixels(pixels, 0, width, 0, 0, width, height);
				GLEx.gl10.glBindTexture(GL.GL_TEXTURE_2D, texture
						.getTextureID());
				GLUtils.texSubImage2D(GL.GL_TEXTURE_2D, 0, 0, 0, buffer);
				GLEx.gl10.glBindTexture(GL.GL_TEXTURE_2D, 0);
				isUpdate = false;
			}
		}
	}

	public final LTexture getTexture() {
		if (isClose) {
			return null;
		}
		update();
		return this.texture;
	}

	public void draw(GLEx g, float x, float y) {
		if (isClose) {
			return;
		}
		update();
		g.drawTexture(texture, x, y);
	}

	public void draw(GLEx g, float x, float y, float w, float h) {
		if (isClose) {
			return;
		}
		update();
		g.drawTexture(texture, x, y, w, h);
	}

	public void draw(GLEx g, float x1, float y1, float w1, float h1, float x2,
			float y2, float w2, float h2) {
		if (isClose) {
			return;
		}
		update();
		g.drawTexture(texture, x1, y1, w1, h1, x2, y2, w2, h2);
	}

	public boolean isClose() {
		return this.isClose;
	}

	public void dispose() {
		this.isClose = true;
		if (texture != null) {
			texture.destroy();
			texture = null;
		}
		if (buffer != null) {
			buffer.recycle();
			buffer = null;
		}
	}
}
