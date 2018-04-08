package org.loon.framework.javase.game.core.graphics;

import java.nio.ByteBuffer;

import org.loon.framework.javase.game.core.LRelease;
import org.loon.framework.javase.game.core.graphics.opengl.GL;
import org.loon.framework.javase.game.core.graphics.opengl.GLEx;
import org.loon.framework.javase.game.core.graphics.opengl.LTexture;
import org.loon.framework.javase.game.utils.BufferUtils;

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
public class LPixmapData implements LRelease {

	private int width, height;

	private boolean hasAlpha;

	private boolean isUpdate, isClose;

	private ByteBuffer buffer;

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
		this.buffer = BufferUtils.createByteBuffer(width * height * 4);
	}

	private LPixmapData(final int[] pixels, final int width, final int height) {
		init(width, height, true);
		final int n = pixels.length;
		int i = 0;
		while (i < n) {
			final int color = pixels[i];
			final byte r = (byte) ((color & 0x00FF0000) >> 16);
			final byte g = (byte) ((color & 0x0000FF00) >> 8);
			final byte b = (byte) ((color & 0x000000FF));
			final byte a = (byte) ((color & 0xFF000000) >> 24);
			buffer.put(r).put(g).put(b).put(a);
			i++;
		}
		buffer.flip();
	}

	public LPixmapData(LImage pix) {
		this(pix.getPixels(), pix.getWidth(), pix.getHeight());
	}

	public LPixmapData(LPixmap pix) {
		this(pix.getData(), pix.getWidth(), pix.getHeight());
	}

	public int get(final int x, final int y) {
		if (x >= 0 && y >= 0 && x < width && y < height) {
			final int index = y * width * 4 + x * 4;
			final byte r = buffer.get(index);
			final byte g = buffer.get(index + 1);
			final byte b = buffer.get(index + 2);
			final byte a = buffer.get(index + 3);
			return r << 24 | g << 16 | b << 8 | a;
		} else {
			return 0;
		}
	}

	public void put(final int x, final int y, final int color) {
		if (x >= 0 && y >= 0 && x < width && y < height) {
			final int index = y * width * 4 + x * 4;
			final byte r = (byte) ((color & 0x00FF0000) >> 16);
			final byte g = (byte) ((color & 0x0000FF00) >> 8);
			final byte b = (byte) ((color & 0x000000FF));
			final byte a = (byte) ((color & 0xFF000000) >> 24);
			buffer.put(index, r);
			buffer.put(index + 1, g);
			buffer.put(index + 2, b);
			buffer.put(index + 3, a);
			isUpdate = true;
		}
	}

	public void reset() {
		if (isClose) {
			return;
		}
		this.init(width, height, hasAlpha);
		this.isUpdate = true;
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
			GLEx.gl10.glTexSubImage2D(GL.GL_TEXTURE_2D, 0, 0, 0, width, height,
					GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, buffer);
			GLEx.gl10.glBindTexture(GL.GL_TEXTURE_2D, 0);
		} else {
			if (isUpdate) {
				GLEx.gl10.glBindTexture(GL.GL_TEXTURE_2D, texture
						.getTextureID());
				GLEx.gl10.glTexSubImage2D(GL.GL_TEXTURE_2D, 0, 0, 0, width,
						height, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, buffer);
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
	}

	//最早考虑过用vertex和color做像素填充，以下代码在PC中也表现正常,
	//然而一旦处理的像素稍多，Android上就会出现悲剧性的1-3FPS(比如
	//分解较大的角色图像),所以修改为目前的创建统一纹理，然而在上面贴像素。
	//实际上在PC中该方式处理速度较慢，可惜为配合Android只能如此(怒了改JNI，不要纯Java～)。
	/**
	
	private boolean isUpdate = true;

	private float[] vertices;

	private float[] colors;

	private FloatBuffer vertexBuffer;

	private FloatBuffer colorBuffer;

	private int index, offset;

	private int moveX, moveY;

	private int numVertices = 0;

	private int width, height;

	private boolean hasAlpha, isClose;

	public LPixmapData(int num) {
		this(num, true);
	}

	public LPixmapData(int num, boolean alpha) {
		this.initialize(num, 0, 0, alpha);
	}

	public LPixmapData(int w, int h) {
		this(w, h, true);
	}

	public LPixmapData(int w, int h, boolean alpha) {
		this.initialize(w * h, w, h, alpha);
	}

	public LPixmapData(String res) {
		this(res, 0, 0);
	}

	public LPixmapData(String res, int x, int y) {
		LImage temp = LImage.createImage(res);
		this.moveX = x;
		this.moveY = y;
		this.width = temp.getWidth();
		this.height = temp.getHeight();
		this.initialize(width * height, width, height, temp.hasAlpha());
		int[] pixels = temp.getPixels();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				put(i, j, pixels[i + j * width]);
			}
		}
		if (temp != null) {
			temp.dispose();
			temp = null;
		}
	}

	public LPixmapData(LImage pix, int x, int y) {
		this.moveX = x;
		this.moveY = y;
		this.width = pix.getWidth();
		this.height = pix.getHeight();
		this.initialize(width * height, width, height, pix.hasAlpha());
		int[] pixels = pix.getPixels();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				put(i, j, pixels[i + j * width]);
			}
		}
	}

	public LPixmapData(LPixmap pix, int x, int y) {
		this.moveX = x;
		this.moveY = y;
		this.width = pix.getWidth();
		this.height = pix.getHeight();
		this.initialize(width * height, width, height, pix.hasAlpha());
		int[] pixels = pix.getData();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				put(i, j, pixels[i + j * width]);
			}
		}
	}

	private void initialize(int num, int w, int h, boolean alpha) {
		this.vertices = new float[num * 2];
		this.colors = new float[num * 4];
		this.numVertices = num;
		this.vertexBuffer = BufferUtils.createFloatBuffer(numVertices * 2);
		this.colorBuffer = BufferUtils.createFloatBuffer(numVertices * 4);
		this.hasAlpha = alpha;
		if (w <= 0) {
			this.width = LSystem.screenRect.width;
		} else {
			this.width = w;
		}
		if (h <= 0) {
			this.height = LSystem.screenRect.height;
		} else {
			this.height = h;
		}
	}

	public synchronized void put(float x, float y, float r, float g, float b,
			float a, boolean check) {
		if (isClose) {
			return;
		}
		if (check) {
			if (a <= 0 || (r == 0 && g == 0 && b == 0 && a == 0)) {
				return;
			}
			if ((x < 0 || y < 0) || (x > width || y > height)) {
				return;
			}
			if ((moveX + x < 0 || moveY + y < 0)
					|| (moveX + x > LSystem.screenRect.width || moveY + y > LSystem.screenRect.height)) {
				return;
			}
		}
		if (hasAlpha) {
			offset = index * 4;
			colors[offset] = r;
			colors[offset + 1] = g;
			colors[offset + 2] = b;
			colors[offset + 3] = a;
		} else {
			offset = index * 4;
			colors[offset] = r;
			colors[offset + 1] = g;
			colors[offset + 2] = b;
			colors[offset + 3] = 1;
		}
		offset = index * 2;
		vertices[offset] = moveX + x;
		vertices[offset + 1] = moveY + y;
		index++;
		isUpdate = true;
	}

	public void put(float x, float y, GLColor color, boolean check) {
		put(x, y, color.r, color.g, color.b, color.a, check);
	}

	public void put(float x, float y, int pixel, boolean check) {
		if (pixel == 0xffffff) {
			return;
		}
		float[] rgbs = GLColor.toRGBA(pixel);
		put(x, y, rgbs[0], rgbs[1], rgbs[2], rgbs[3], check);
	}

	public void put(float x, float y, GLColor color) {
		put(x, y, color, true);
	}

	public void put(float x, float y, int pixel) {
		put(x, y, pixel, true);
	}

	public void setLocation(int x, int y) {
		this.moveX = x;
		this.moveY = y;
		for (int i = 0; i < vertices.length; i++) {
			if (i % 2 == 0) {
				vertices[i] = vertices[i] + x;
				vertices[i + 1] = vertices[i + 1] + y;
			}
		}
		this.isUpdate = true;
	}

	public int getX() {
		return moveX;
	}

	public int getY() {
		return moveY;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	private final void vertexArraySet() {
		GLEx.gl10.glVertexPointer(2, GL10.GL_FLOAT, 0, vertexBuffer);
		GLEx.gl10.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
	}

	private final void vertexArrayOn() {
		GLUtils.enableVertexArray(GLEx.gl10);
		GLUtils.enableTexColorArray(GLEx.gl10);
	}

	private final void vertexArrayUn() {
		GLUtils.disableVertexArray(GLEx.gl10);
		GLUtils.disableTexColorArray(GLEx.gl10);
		GLEx.gl10.glColor4f(1, 1, 1, 1);
	}

	public void update() {
		if (isClose) {
			return;
		}
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		colorBuffer.put(colors);
		colorBuffer.position(0);
		numVertices = index;
		index = 0;
		isUpdate = false;
	}

	public boolean isUpdate() {
		return isUpdate;
	}

	public final void draw() {
		draw(GL.GL_POINTS);
	}

	public final void draw(int type) {
		if (isClose) {
			return;
		}
		try {
			vertexArrayOn();
			if (isUpdate) {
				update();
			}
			vertexArraySet();
			GLEx.gl10.glDrawArrays(type, 0, numVertices);
		} catch (Exception e) {
		} finally {
			vertexArrayUn();
		}
	}

	public int getMaximumVertices() {
		return vertices.length / 3;
	}

	public boolean isClose() {
		return this.isClose;
	}

	public void reset() {
		isUpdate = true;
		index = 0;
	}

	public void dispose() {
		this.isClose = true;
		this.vertices = null;
		this.vertexBuffer = null;
		this.colors = null;
		this.colorBuffer = null;
	}
	 */

}
