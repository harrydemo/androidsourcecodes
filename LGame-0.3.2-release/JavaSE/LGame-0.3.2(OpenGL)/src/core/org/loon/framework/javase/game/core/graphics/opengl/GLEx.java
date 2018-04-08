package org.loon.framework.javase.game.core.graphics.opengl;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.loon.framework.javase.game.core.LSystem;
import org.loon.framework.javase.game.core.geom.Polygon;
import org.loon.framework.javase.game.core.geom.RectBox;
import org.loon.framework.javase.game.core.geom.Shape;
import org.loon.framework.javase.game.core.geom.Triangle2f;
import org.loon.framework.javase.game.core.geom.Triangle;
import org.loon.framework.javase.game.core.geom.Vector2f;
import org.loon.framework.javase.game.core.graphics.LFont;
import org.loon.framework.javase.game.core.graphics.LImage;
import org.loon.framework.javase.game.core.graphics.LPixmap;
import org.loon.framework.javase.game.core.graphics.device.LGraphics;
import org.loon.framework.javase.game.core.graphics.device.LTrans;
import org.loon.framework.javase.game.core.graphics.opengl.LTexture.Format;
import org.loon.framework.javase.game.utils.BufferUtils;
import org.loon.framework.javase.game.utils.GLUtils;
import org.loon.framework.javase.game.utils.MathUtils;
import org.loon.framework.javase.game.utils.ScreenUtils;
import org.loon.framework.javase.game.utils.StringUtils;

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
public final class GLEx implements LTrans {

	/**
	 * 复制指定区域图像(仿J2ME接口)
	 * 
	 * @param sx
	 * @param sy
	 * @param width
	 * @param height
	 * @param dx
	 * @param dy
	 * @param anchor
	 */
	public void copyArea(int sx, int sy, int width, int height, int dx, int dy,
			int anchor) {
		if (width <= 0 || height <= 0) {
			return;
		}
		boolean badAnchor = false;
		if ((anchor & 0x7f) != anchor || (anchor & BASELINE) != 0) {
			badAnchor = true;
		}
		if ((anchor & TOP) != 0) {
			if ((anchor & (VCENTER | BOTTOM)) != 0)
				badAnchor = true;
		} else if ((anchor & BOTTOM) != 0) {
			if ((anchor & VCENTER) != 0)
				badAnchor = true;
			else {
				dy -= height - 1;
			}
		} else if ((anchor & VCENTER) != 0) {
			dy -= (height - 1) >>> 1;
		} else {
			badAnchor = true;
		}
		if ((anchor & LEFT) != 0) {
			if ((anchor & (HCENTER | RIGHT)) != 0)
				badAnchor = true;
		} else if ((anchor & RIGHT) != 0) {
			if ((anchor & HCENTER) != 0)
				badAnchor = true;
			else {
				dx -= width;
			}
		} else if ((anchor & HCENTER) != 0) {
			dx -= (width - 1) >>> 1;
		} else {
			badAnchor = true;
		}
		if (badAnchor) {
			throw new IllegalArgumentException("Bad Anchor !");
		}
		copyArea(sx, sy, width, height, dx - sx, dy - sy);
	}

	/**
	 * 复制指定区域图像
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param dx
	 * @param dy
	 */
	public void copyArea(int x, int y, int width, int height, int dx, int dy) {
		copyArea(null, x, y, width, height, dx, dy);
	}

	/**
	 * 复制指定区域图像
	 * 
	 * @param texture
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param dx
	 * @param dy
	 */
	public void copyArea(LTexture texture, int x, int y, int width, int height,
			int dx, int dy) {
		if (isClose) {
			return;
		}
		if (x < 0) {
			width += x;
			x = 0;
		}
		if (y < 0) {
			height += y;
			y = 0;
		}
		if (texture != null) {
			if (x + width > texture.getWidth()) {
				width = texture.getWidth() - x;
			}
			if (y + height > texture.getHeight()) {
				height = texture.getHeight() - y;
			}
			LTexture tex2d = texture.getSubTexture(x, y, width, height);
			drawTexture(tex2d, x + dx, y + dy);
			if (GLEx.isVbo()) {
				deleteBuffer(tex2d.bufferID);
			}
			tex2d = null;
		} else {
			if (x + width > getWidth()) {
				width = getWidth() - x;
			}
			if (y + height > getHeight()) {
				height = getHeight() - y;
			}
			LTexture tex2d = ScreenUtils.toScreenCaptureTexture(x, y, width,
					height);
			drawTexture(tex2d, x + dx, y + dy);
			if (tex2d != null) {
				tex2d.destroy();
				tex2d = null;
			}
		}

	}

	/**
	 * 绘制指定纹理
	 * 
	 * @param texture
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public void draw(LTexture texture, float x, float y, float width,
			float height, boolean use) {
		if (texture == null) {
			return;
		}
		draw(texture, null, x, y, width, height, 0, 0, texture.getWidth(),
				texture.getHeight(), use);
	}

	/**
	 * 绘制指定纹理
	 * 
	 * @param texture
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param srcX
	 * @param srcY
	 * @param srcWidth
	 * @param srcHeight
	 */
	public void draw(LTexture texture, float x, float y, float width,
			float height, float srcX, float srcY, float srcWidth,
			float srcHeight, boolean use) {
		if (texture == null) {
			return;
		}
		draw(texture, null, x, y, width, height, srcX, srcY, srcWidth,
				srcHeight, use);
	}

	/**
	 * 将指定像素与纹理混合绘制
	 * 
	 * @param texture
	 * @param colors
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public void draw(LTexture texture, GLColor[] colors, float x, float y,
			float width, float height, boolean use) {
		if (texture == null) {
			return;
		}
		draw(texture, colors, x, y, width, height, 0, 0, texture.getWidth(),
				texture.getHeight(), use);
	}

	/**
	 * 将指定像素与纹理混合绘制
	 * 
	 * @param texture
	 * @param colors
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param srcX
	 * @param srcY
	 * @param srcWidth
	 * @param srcHeight
	 */
	public void draw(LTexture texture, GLColor[] colors, float x, float y,
			float width, float height, float srcX, float srcY, float srcWidth,
			float srcHeight, boolean use) {
		if (!texture.isLoaded) {
			texture.loadTexture();
		}
		glTex2DEnable();
		{
			bind(texture.textureID);
			if (use) {
				glBegin(GL.GL_TRIANGLE_STRIP, false);
			}
			float xOff = (((float) srcX / texture.width) * texture.widthRatio)
					+ texture.xOff;
			float yOff = (((float) srcY / texture.height) * texture.heightRatio)
					+ texture.yOff;
			float widthRatio = (((float) srcWidth / texture.width) * texture.widthRatio);
			float heightRatio = (((float) srcHeight / texture.height) * texture.heightRatio);
			if (colors == null) {
				glVertex3f(x, y, 0);
				glTexCoord2f(xOff, yOff);
				glVertex3f(x + width, y, 0);
				glTexCoord2f(widthRatio, yOff);
				glVertex3f(x, y + height, 0);
				glTexCoord2f(xOff, heightRatio);
				glVertex3f(x + width, y + height, 0);
				glTexCoord2f(widthRatio, heightRatio);
			} else {
				glColor4ES(colors[LTexture.TOP_LEFT]);
				glVertex3f(x, y, 0);
				glTexCoord2f(xOff, yOff);
				glColor4ES(colors[LTexture.TOP_RIGHT]);
				glVertex3f(x + width, y, 0);
				glTexCoord2f(widthRatio, yOff);
				glColor4ES(colors[LTexture.BOTTOM_LEFT]);
				glVertex3f(x, y + height, 0);
				glTexCoord2f(xOff, heightRatio);
				glColor4ES(colors[LTexture.BOTTOM_RIGHT]);
				glVertex3f(x + width, y + height, 0);
				glTexCoord2f(widthRatio, heightRatio);
			}
			if (use) {
				glEnd();
			}
		}
	}

	/**
	 * 以指定纹理为目标，复制一组像素到其上
	 * 
	 * @param texture
	 * @param pix
	 */
	public void copyPixelsToTexture(LTexture texture, LPixmap pix) {
		copyPixelsToTexture(texture, pix, false, true);
	}

	/**
	 * 以指定纹理为目标，复制一组像素到其上
	 * 
	 * @param texture
	 * @param pix
	 * @param remove
	 * @param check
	 */
	public void copyPixelsToTexture(LTexture texture, LPixmap pix,
			boolean remove, boolean check) {
		int hashCode = 0;
		if (check) {
			synchronized (LTextures.copyToTextures) {
				if (LTextures.copyToTextures.size() > LSystem.DEFAULT_MAX_CACHE_SIZE) {
					LTextures.copyToTextures.clear();
					LSystem.gc();
				}
				int[] pixels = pix.getData();
				hashCode = pixels.hashCode();
				hashCode = LSystem.unite(hashCode, texture.textureID);
				hashCode = LSystem.unite(hashCode, pix.getWidth());
				hashCode = LSystem.unite(hashCode, pix.getHeight());
				hashCode = LSystem.unite(hashCode, pixels[0]);
				hashCode = LSystem.unite(hashCode, pixels[pixels.length - 1]);
				if (remove) {
					LTextures.copyToTextures.remove(hashCode);
				} else if (LTextures.copyToTextures.contains(hashCode)) {
					return;
				}
			}
		}
		if (!texture.isLoaded) {
			texture.loadTexture();
		}
		glTex2DEnable();
		{
			bind(texture.textureID);
			gl10.glPixelStorei(GL.GL_UNPACK_ALIGNMENT, pix.hasAlpha() ? 4 : 1);
			gl10.glTexSubImage2D(GL.GL_TEXTURE_2D, 0, 0, 0, pix.getTexWidth(),
					pix.getTexHeight(),
					pix.hasAlpha() ? GL.GL_RGBA : GL.GL_RGB,
					GL.GL_UNSIGNED_BYTE, pix.getPixels());
		}
		if (check) {
			synchronized (LTextures.copyToTextures) {
				LTextures.copyToTextures.add(hashCode);
			}
		}
	}

	/**
	 * 以指定纹理为目标，复制一组图像到其上
	 * 
	 * @param texture
	 * @param pix
	 */
	public void copyImageToTexture(LTexture texture, LImage pix) {
		copyImageToTexture(texture, pix, false, true);
	}

	/**
	 * 以指定纹理为目标，复制一组图像到其上
	 * 
	 * @param texture
	 * @param pix
	 * @param x
	 * @param y
	 */
	public void copyImageToTexture(LTexture texture, LImage pix, int x, int y) {
		copyImageToTexture(texture, pix, x, y, false, true);
	}

	/**
	 * 以指定纹理为目标，复制一组图像到其上
	 * 
	 * @param texture
	 * @param pix
	 * @param remove
	 * @param check
	 */
	public void copyImageToTexture(LTexture texture, LImage pix,
			boolean remove, boolean check) {
		copyImageToTexture(texture, pix, 0, 0, remove, check);
	}

	/**
	 * 以指定纹理为目标，复制一组图像到其上
	 * 
	 * @param texture
	 * @param pix
	 * @param x
	 * @param y
	 * @param remove
	 * @param check
	 */
	public void copyImageToTexture(LTexture texture, LImage pix, int x, int y,
			boolean remove, boolean check) {
		int hashCode = 0;
		if (check) {
			synchronized (LTextures.copyToTextures) {
				if (LTextures.copyToTextures.size() > LSystem.DEFAULT_MAX_CACHE_SIZE) {
					LTextures.copyToTextures.clear();
					LSystem.gc();
				}
				hashCode = pix.hashCode();
				hashCode = LSystem.unite(hashCode, texture.textureID);
				hashCode = LSystem.unite(hashCode, x);
				hashCode = LSystem.unite(hashCode, y);
				hashCode = LSystem.unite(hashCode, pix.getWidth());
				hashCode = LSystem.unite(hashCode, pix.getHeight());
				if (remove) {
					LTextures.copyToTextures.remove(hashCode);
				} else if (LTextures.copyToTextures.contains(hashCode)) {
					return;
				}
			}
		}
		if (!texture.isLoaded) {
			texture.loadTexture();
		}
		glTex2DEnable();
		{
			bind(texture.textureID);
			gl10.glPixelStorei(GL.GL_UNPACK_ALIGNMENT, pix.hasAlpha() ? 4 : 1);
			gl10.glTexSubImage2D(GL.GL_TEXTURE_2D, 0, x, y, pix.getWidth(), pix
					.getHeight(), pix.hasAlpha() ? GL.GL_RGBA : GL.GL_RGB,
					GL.GL_UNSIGNED_BYTE, pix.getByteBuffer());
		}
		if (check) {
			synchronized (LTextures.copyToTextures) {
				LTextures.copyToTextures.add(hashCode);
			}
		}
	}

	// ----- 批处理纹理渲染开始 ------//

	public void beginBatch() {
		this.glTex2DEnable();
		this.glTex2DARRAYEnable();
	}

	public void drawBatch(LImage image, float x, float y) {
		if (image == null) {
			return;
		}
		drawBatch(image.getTexture(), x, y);
	}

	public void drawBatch(LTexture texture, float x, float y) {
		if (texture == null || isClose) {
			return;
		}
		if (!texture.isLoaded) {
			texture.loadTexture();
		}
		bind(texture.textureID);
		{
			if (x != 0 || y != 0) {
				gl10.glTranslatef(x, y, 0);
			}
			if (vboOn) {
				gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, texture.bufferID);
				gl11.glVertexPointer(2, GL11.GL_FLOAT, 0, 0);
				gl11.glTexCoordPointer(2, GL11.GL_FLOAT, 0, texture.texSize);
				gl11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
				gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
			} else {
				texture.data.position(0);
				GLUtils.vertexPointer(gl10, 2, texture.data);
				texture.data.position(8);
				gl10.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texture.data);
				gl10.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			}
			if (x != 0 || y != 0) {
				gl10.glTranslatef(-x, -y, 0);
			}
		}
	}

	public void drawBatch(LTexture texture, float x, float y, GLColor c) {
		if (texture == null || isClose) {
			return;
		}
		if (!texture.isLoaded) {
			texture.loadTexture();
		}
		bind(texture.textureID);
		{
			boolean flag = !color.equals(c);
			if (x != 0 || y != 0) {
				gl10.glTranslatef(x, y, 0);
			}
			if (flag) {
				gl10.glColor4f(c.r, c.g, c.b, c.a);
			}
			if (vboOn) {
				gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, texture.bufferID);
				gl11.glVertexPointer(2, GL11.GL_FLOAT, 0, 0);
				gl11.glTexCoordPointer(2, GL11.GL_FLOAT, 0, texture.texSize);
				gl11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
				gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
			} else {
				texture.data.position(0);
				GLUtils.vertexPointer(gl10, 2, texture.data);
				texture.data.position(8);
				gl10.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texture.data);
				gl10.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			}
			if (flag) {
				gl10.glColor4f(color.r, color.g, color.b, color.a);
			}
			if (x != 0 || y != 0) {
				gl10.glTranslatef(-x, -y, 0);
			}
		}
	}

	public void drawBatch(LImage image, float x, float y, float width,
			float height, float rotation, GLColor color) {
		if (image == null) {
			return;
		}
		drawBatch(image.getTexture(), x, y, width, height, rotation, color);
	}

	public void drawBatch(LTexture texture, float x, float y, float width,
			float height, float rotation, GLColor c) {
		if (texture == null || isClose) {
			return;
		}
		if (!texture.isLoaded) {
			texture.loadTexture();
		}
		bind(texture.textureID);
		{
			boolean flag = !color.equals(c);
			gl10.glPushMatrix();
			gl10.glTranslatef(x, y, 0);
			if (rotation != 0) {
				float centerX = width / 2;
				float centerY = height / 2;
				gl10.glTranslatef(centerX, centerY, 0.0f);
				gl10.glRotatef(rotation, 0.0f, 0.0f, 1.0f);
				gl10.glTranslatef(-centerX, -centerY, 0.0f);
			}
			if (width != texture.width || height != texture.height) {
				float sx = width / texture.width;
				float sy = height / texture.height;
				try {
					gl10.glScalef(sx, sy, 1);
				} catch (Exception e) {
					gl10.glScalef(sx, sy, 0);
				}
			}
			if (flag) {
				gl10.glColor4f(c.r, c.g, c.b, c.a);
			}
			if (vboOn) {
				gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, texture.bufferID);
				gl11.glVertexPointer(2, GL11.GL_FLOAT, 0, 0);
				gl11.glTexCoordPointer(2, GL11.GL_FLOAT, 0, texture.texSize);
				gl11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
				gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
			} else {
				texture.data.position(0);
				GLUtils.vertexPointer(gl10, 2, texture.data);
				texture.data.position(8);
				gl10.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texture.data);
				gl10.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			}
			if (flag) {
				gl10.glColor4f(color.r, color.g, color.b, color.a);
			}
			gl10.glPopMatrix();

		}
	}

	public void drawBatch(LImage image, float x, float y, float width,
			float height, float srcX, float srcY, float srcWidth,
			float srcHeight) {
		if (image == null) {
			return;
		}
		drawBatch(image.getTexture(), x, y, width, height, srcX, srcY,
				srcWidth, srcHeight);
	}

	public void drawBatch(LTexture texture, float x, float y, float width,
			float height, float srcX, float srcY, float srcWidth,
			float srcHeight) {
		if (texture == null || isClose) {
			return;
		}
		if (!texture.isLoaded) {
			texture.loadTexture();
		}

		boolean save = width != texture.width || height != texture.height;
		bind(texture.textureID);
		{
			if (x != 0 || y != 0) {
				gl10.glTranslatef(x, y, 0);
			}
			if (save) {
				gl10.glPushMatrix();
				float sx = width / texture.width;
				float sy = height / texture.height;
				try {
					gl10.glScalef(sx, sy, 1);
				} catch (Exception e) {
					gl10.glScalef(sx, sy, 0);
				}
			}
			if (GLEx.vboOn) {
				gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, texture.bufferID);
				gl11.glVertexPointer(2, GL11.GL_FLOAT, 0, 0);
				if (srcX != 0 || srcY != 0 || srcWidth != texture.width
						|| srcHeight != texture.height) {
					gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, glDataBufferID);
					putRectangle(texture, srcX, srcY, srcWidth, srcHeight);
					rectData.position(8);
					gl11.glBufferSubData(GL11.GL_ARRAY_BUFFER,
							texture.vertexSize, texture.texSize, rectData);
					gl11
							.glTexCoordPointer(2, GL11.GL_FLOAT, 0,
									texture.texSize);
				} else {
					gl11
							.glTexCoordPointer(2, GL10.GL_FLOAT, 0,
									texture.texSize);
				}
				gl11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
				gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
			} else {
				texture.data.position(0);
				GLUtils.vertexPointer(gl10, 2, texture.data);
				if (srcX != 0 || srcY != 0 || srcWidth != texture.width
						|| srcHeight != texture.height) {
					putRectangle(texture, srcX, srcY, srcWidth, srcHeight);
					rectData.position(8);
					gl10.glTexCoordPointer(2, GL10.GL_FLOAT, 0, rectData);
				} else {
					texture.data.position(8);
					gl10.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texture.data);
				}
				gl10.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			}

		}
		if (save) {
			gl10.glPopMatrix();
		}
		if (x != 0 || y != 0) {
			gl10.glTranslatef(-x, -y, 0);
		}

	}

	public void endBatch() {
		this.glTex2DARRAYDisable();
		this.glTex2DDisable();
	}

	// ----- 批处理纹理渲染结束 ------//

	public static class Clip {

		public int x;

		public int y;

		public int width;

		public int height;

		public Clip(Clip clip) {
			this(clip.x, clip.y, clip.width, clip.height);
		}

		public Clip(int x, int y, int w, int h) {
			this.setBounds(x, y, w, h);
		}

		public void setBounds(int x, int y, int w, int h) {
			this.x = x;
			this.y = y;
			this.width = w;
			this.height = h;
		}

		public int getBottom() {
			return height;
		}

		public int getLeft() {
			return x;
		}

		public int getRight() {
			return width;
		}

		public int getTop() {
			return y;
		}

	}

	public static enum Direction {
		TRANS_NONE, TRANS_MIRROR, TRANS_FILP, TRANS_MF;
	}

	public static GLEx self;

	public static GLBase gl;

	public static GL10 gl10;

	public static GL11 gl11;

	private static GLU glu;

	private int currentBlendMode;

	private int prevBlendMode;

	private float lastAlpha = 1.0F, lineWidth, sx = 1, sy = 1;

	private boolean isClose, isTex2DEnabled, isARRAYEnable, isAntialias,
			isScissorTest, isPushed;

	private GLColor boundColor;

	private final Clip clip;

	private int mode, drawingType = 0;

	private float translateX, translateY;

	private final RectBox viewPort;

	private static boolean isAlpha, isMask;

	private boolean preTex2d, preLight, preSmoot, preCull, preTex2dMode;

	private boolean isUpdateColor, isUpdateTexture;

	private static boolean vboOn, vboSupported;

	public static int lazyTextureID;

	boolean onAlpha, onReplace;

	static int verMajor, verMinor;

	private GLColor color = new GLColor(GLColor.white);

	private static final float[] rectDataCords = new float[16];

	private static final FloatBuffer rectData = BufferUtils
			.createFloatBuffer(rectDataCords.length);

	private static int glDataBufferID;

	private ByteBuffer readBuffer = BufferUtils.createByteBuffer(4);

	private ShortBuffer shortBuffer = BufferUtils.createShortBuffer(64);

	private static final int DEFAULT_MAX_VERTICES = 16384;

	private FloatBuffer floatVertices = BufferUtils
			.createFloatBuffer(DEFAULT_MAX_VERTICES);

	private FloatBuffer texCoordBuf;

	private FloatBuffer vertexBuf;

	private FloatBuffer colorBuf;

	private LFont font = LFont.getDefaultFont();

	private boolean onTexEnvf, onSaveFlag;

	public GLEx(int width, int height) {
		String version = org.lwjgl.opengl.GL11.glGetString(GL11.GL_VERSION);
		verMajor = Integer.parseInt("" + version.charAt(0));
		verMinor = Integer.parseInt("" + version.charAt(2));
		if (verMajor == 1 && verMinor < 5) {
			GLEx.gl10 = new LWjglGL10();
			setVbo(false);
			setVBOSupported(false);
		} else {
			GLEx.gl11 = new LWjglGL11();
			GLEx.gl10 = GLEx.gl11;
		}
		GLEx.gl = gl10;
		GLEx.glu = new LWjglGLU();
		GLEx.self = this;
		this.viewPort = new RectBox(0, 0, width, height);
		this.clip = new Clip(0, 0, viewPort.width, viewPort.height);
		this.isTex2DEnabled = false;
		this.isClose = false;
	}

	public int getWidth() {
		return (int) viewPort.getWidth();
	}

	public int getHeight() {
		return (int) viewPort.getHeight();
	}

	public final void enableSmooth() {
		if (isClose) {
			return;
		}
		if (!preSmoot) {
			gl.glEnable(GL10.GL_POINT_SMOOTH);
			gl.glEnable(GL10.GL_LINE_SMOOTH);
			preSmoot = true;
		}
	}

	public final void disableSmooth() {
		if (isClose) {
			return;
		}
		if (preSmoot) {
			gl.glDisable(GL10.GL_POINT_SMOOTH);
			gl.glDisable(GL10.GL_LINE_SMOOTH);
			preSmoot = false;
		}
	}

	public final void enableLighting() {
		if (isClose) {
			return;
		}
		if (!preLight) {
			gl10.glEnable(GL10.GL_LIGHTING);
			preLight = true;
		}
	}

	public final void disableLighting() {
		if (isClose) {
			return;
		}
		if (preLight) {
			gl10.glDisable(GL10.GL_LIGHTING);
			preLight = false;
		}
	}

	/**
	 * 变更画布基础设置
	 * 
	 */
	public final void update() {
		if (isClose) {
			return;
		}
		// 刷新原始设置
		GLUtils.reset(gl10);
		// 清空背景为黑色
		GLUtils.setClearColor(gl10, GLColor.black);
		// 设定插值模式为FASTEST(最快,质量有损)
		GLUtils.setHintFastest(gl10);
		// 着色模式设为FLAT
		GLUtils.setShadeModelFlat(gl10);
		// 禁用光照效果
		GLUtils.disableLightning(gl10);
		// 禁用色彩抖动
		GLUtils.disableDither(gl10);
		// 禁用深度测试
		GLUtils.disableDepthTest(gl10);
		// 禁用多重采样
		GLUtils.disableMultisample(gl10);
		// 禁用双面剪切
		GLUtils.disableCulling(gl10);
		// 禁用顶点数据
		GLUtils.disableVertexArray(gl10);
		// 禁用纹理坐标
		GLUtils.disableTexCoordArray(gl10);
		// 禁用纹理色彩
		GLUtils.disableTexColorArray(gl10);
		// 禁用纹理贴图
		GLUtils.disableTextures(gl10);
		// 设定画布渲染模式为默认
		this.setBlendMode(GL.MODE_NORMAL);
		this.prevBlendMode = currentBlendMode;
		// 支持VBO则启用VBO加速
		if (GLEx.vboOn) {
			try {
				glDataBufferID = createBufferID();
				bufferDataARR(glDataBufferID, rectData, GL11.GL_DYNAMIC_DRAW);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 设为2D界面模式(转为2D屏幕坐标系)
		set2DStateOn();
	}

	private boolean useBegin;

	/**
	 * 模拟标准OpenGL的glBegin(实际为重新初始化顶点集合)
	 * 
	 * @param mode
	 */
	public final void glBegin(int mode) {
		glBegin(mode, true);
	}

	/**
	 * 模拟标准OpenGL的glBegin(实际为重新初始化顶点集合)
	 * 
	 * @param mode
	 * @param n
	 */
	public final void glBegin(int mode, boolean d) {
		if (isClose) {
			return;
		}
		if (d) {
			this.glTex2DDisable();
		}
		this.mode = mode;
		if (vertexBuf == null) {
			vertexBuf = BufferUtils.createFloatBuffer(DEFAULT_MAX_VERTICES * 2);
		} else {
			vertexBuf.rewind();
			vertexBuf.limit(vertexBuf.capacity());
		}
		if (texCoordBuf == null) {
			texCoordBuf = BufferUtils
					.createFloatBuffer(DEFAULT_MAX_VERTICES * 3);
		} else {
			texCoordBuf.rewind();
			texCoordBuf.limit(texCoordBuf.capacity());
		}
		if (colorBuf == null) {
			colorBuf = BufferUtils.createFloatBuffer(DEFAULT_MAX_VERTICES * 4);
		} else {
			colorBuf.rewind();
			colorBuf.limit(colorBuf.capacity());
		}
		this.useBegin = true;
	}

	public FloatBuffer getCacheESVertexBuffer() {
		return BufferUtils.copyFloatBuffer(vertexBuf);
	}

	public FloatBuffer getCacheESCoordBuffer() {
		return BufferUtils.copyFloatBuffer(texCoordBuf);
	}

	public FloatBuffer getCacheESColorBuffer() {
		return BufferUtils.copyFloatBuffer(colorBuf);
	}

	/**
	 * 在模拟标准OpenGL的环境中传入指定像素点
	 * 
	 * @param x
	 * @param y
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 */
	public void putPixel4ES(float x, float y, float r, float g, float b, float a) {
		if (isClose || !useBegin) {
			return;
		}
		if (a <= 0 || (r == 0 && g == 0 && b == 0 && a == 0)) {
			return;
		}
		if ((x < 0 || y < 0) || (x > viewPort.width || y > viewPort.height)) {
			return;
		}
		this.glVertex2f(x, y);
		this.glColor4ES(r, g, b, a);
	}

	/**
	 * 在模拟标准OpenGL的环境中传入指定像素点
	 * 
	 * @param x
	 * @param y
	 * @param c
	 */
	public void putPixel4ES(float x, float y, GLColor c) {
		putPixel4ES(x, y, c.r, c.g, c.b, c.a);
	}

	/**
	 * 在模拟标准OpenGL的环境中传入指定像素点
	 * 
	 * @param x
	 * @param y
	 * @param r
	 * @param g
	 * @param b
	 */
	public void putPixel3ES(float x, float y, float r, float g, float b) {
		putPixel4ES(x, y, r, g, b, 1);
	}

	/**
	 * 设置纹理坐标
	 * 
	 * @param fcol
	 * @param frow
	 */
	public final void glTexCoord2f(float fcol, float frow) {
		if (isClose || !useBegin) {
			return;
		}
		texCoordBuf.put(fcol);
		texCoordBuf.put(frow);
	}

	/**
	 * 添加二维纹理
	 * 
	 * @param x
	 * @param y
	 */
	public final void glVertex2f(float x, float y) {
		if (isClose || !useBegin) {
			return;
		}
		glVertex3f(x, y, 0);
	}

	/**
	 * 添加三维纹理
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public final void glVertex3f(float x, float y, float z) {
		if (isClose || !useBegin) {
			return;
		}
		vertexBuf.put(x);
		vertexBuf.put(y);
		vertexBuf.put(z);
	}

	/**
	 * 定义色彩
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 */
	public void glColor4ES(float r, float g, float b, float a) {
		if (isClose || !useBegin) {
			return;
		}
		colorBuf.put(r);
		colorBuf.put(g);
		colorBuf.put(b);
		colorBuf.put(a);
	}

	/**
	 * 定义色彩
	 * 
	 * @param c
	 */
	public void glColor4ES(GLColor c) {
		if (isClose) {
			return;
		}
		glColor4ES(c.r, c.g, c.b, c.a);
	}

	/**
	 * 定义色彩
	 * 
	 * @param r
	 * @param g
	 * @param b
	 */
	public void glColor3ES(float r, float g, float b) {
		glColor4ES(r, g, b, 1);
	}

	/**
	 * 定义色彩
	 * 
	 * @param r
	 * @param g
	 * @param b
	 */
	public final void glColor3ub(byte r, byte g, byte b) {
		if (isClose) {
			return;
		}
		gl10.glColor4f((r & 255) / 255.0f, (g & 255) / 255.0f,
				(b & 255) / 255.0f, 1);
	}

	/**
	 * 定义色彩
	 * 
	 * @param r
	 * @param g
	 * @param b
	 */
	public final void glColor3f(float r, float g, float b) {
		if (isClose) {
			return;
		}
		gl10.glColor4f(r, g, b, 1);
	}

	/**
	 * 设定顶点数组的字节缓冲与类型
	 * 
	 * @param size
	 * @param stride
	 * @param vertexArray
	 */
	public final void glVertexPointer(int size, int stride,
			FloatBuffer vertexArray) {
		if (isClose) {
			return;
		}
		gl10.glVertexPointer(size, GL10.GL_FLOAT, stride, vertexArray);
	}

	/**
	 * 设定顶点数组的字节缓冲与类型
	 * 
	 * @param size
	 * @param type
	 * @param stride
	 * @param vertexArray
	 */
	public final void glVertexPointer(int size, int type, int stride,
			FloatBuffer vertexArray) {
		if (isClose) {
			return;
		}
		gl10.glVertexPointer(size, type, stride, vertexArray);
	}

	/**
	 * 设定顶点数组的字节缓冲与类型
	 * 
	 * @param size
	 * @param type
	 * @param stride
	 * @param pointer
	 */
	public final void glVertexPointer(int size, int type, int stride,
			Buffer pointer) {
		if (isClose) {
			return;
		}
		gl10.glVertexPointer(size, type, stride, pointer);
	}

	/**
	 * 该函数可以设置或禁用指定的顶点数组
	 * 
	 * @param mode
	 * @param byteStride
	 * @param buf
	 */
	public final void glInterleavedArrays(int mode, int byteStride,
			FloatBuffer buf) {
		if (isClose) {
			return;
		}
		if (byteStride == 0) {
			byteStride = 5 * 4;
		}
		if (mode != GL.GL_T2F_V3F) {
			throw new RuntimeException("Unsupported interleaved array mode!");
		}
		int pos = buf.position();
		gl10.glTexCoordPointer(2, GL10.GL_FLOAT, byteStride, buf);
		buf.position(pos + 2);
		gl10.glVertexPointer(3, GL10.GL_FLOAT, byteStride, buf);
		buf.position(pos);
		gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl10.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	}

	/**
	 * 获取当前画布的矩阵数据
	 * 
	 * @param buffer
	 */
	public final void glGetFloat(FloatBuffer buffer) {
		glGetFloat(GL11.GL_MODELVIEW_MATRIX, buffer);
	}

	/**
	 * 以指定方式获取当前画布的矩阵数据
	 * 
	 * @param name
	 * @param buffer
	 */
	public final void glGetFloat(int name, FloatBuffer buffer) {
		if (isClose) {
			return;
		}
		switch (name) {
		case GL11.GL_MODELVIEW_MATRIX:
			mode = GL11.GL_MODELVIEW_MATRIX_FLOAT_AS_INT_BITS_OES;
			break;
		default:
			throw new RuntimeException("Unsupported: " + name);
		}
		IntBuffer intBuffer = BufferUtils.createIntBuffer(16);
		gl.glGetIntegerv(name, intBuffer);
		int p = buffer.position();
		for (int i = 0; i < 16; i++) {
			buffer.put(Float.intBitsToFloat(intBuffer.get(i)));
		}
		buffer.position(p);
	}

	public void glTexEnvf(int target, int pname, float param) {
		gl10.glTexEnvf(target, pname, param);
	}

	public void glPointSizePointerOES(int type, int stride, Buffer pointer) {
		gl11.glPointSizePointerOES(type, stride, pointer);
	}

	/**
	 * 设定颜色组
	 * 
	 * @param size
	 * @param b
	 * @param stride
	 * @param colorAsByteBuffer
	 */
	public final void glColorPointer(int size, int type, int stride,
			ByteBuffer colorAsByteBuffer) {
		if (isClose) {
			return;
		}
		gl10.glColorPointer(size, type, stride, colorAsByteBuffer);
	}

	/**
	 * 设定颜色组
	 * 
	 * @param size
	 * @param type
	 * @param stride
	 * @param pointer
	 */
	public final void glColorPointer(int size, int type, int stride,
			Buffer pointer) {
		if (isClose) {
			return;
		}
		gl10.glColorPointer(size, type, stride, pointer);
	}

	/**
	 * 设定颜色组
	 * 
	 * @param size
	 * @param stride
	 * @param colorArrayBuf
	 */
	public final void glColorPointer(int size, int stride,
			FloatBuffer colorArrayBuf) {
		if (isClose) {
			return;
		}
		gl10.glColorPointer(size, GL10.GL_FLOAT, stride, colorArrayBuf);
	}

	/**
	 * 设定颜色组
	 * 
	 * @param size
	 * @param type
	 * @param stride
	 * @param colorArrayBuf
	 */
	public final void glColorPointer(int size, int type, int stride,
			FloatBuffer colorArrayBuf) {
		if (isClose) {
			return;
		}
		gl10.glColorPointer(size, type, stride, colorArrayBuf);
	}

	/**
	 * 以指定模式通过指定数据进行图元渲染
	 * 
	 * @param mode
	 * @param srcIndexBuf
	 */
	public final void glDrawElements(int mode, IntBuffer srcIndexBuf) {
		if (isClose) {
			return;
		}
		int count = srcIndexBuf.remaining();
		if (count > srcIndexBuf.capacity()) {
			shortBuffer = BufferUtils.createShortBuffer(count);
		}
		for (int i = 0; i < count; i++) {
			shortBuffer.put(i, (short) srcIndexBuf.get());
		}
		gl10.glDrawElements(mode, count, GL10.GL_SHORT, shortBuffer);
	}

	/**
	 * 以指定模式通过指定数据进行图元渲染
	 * 
	 * @param mode
	 * @param count
	 * @param type
	 * @param indices
	 */
	public final void glDrawElements(int mode, int count, int type,
			Buffer indices) {
		if (isClose) {
			return;
		}
		gl10.glDrawElements(mode, count, type, indices);
	}

	/**
	 * 绘制线段(模式应为GL.GL_LINES)
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public void glLine(float x1, float y1, float x2, float y2) {
		$drawLine1(x1, y1, x2, y2, false);
	}

	/**
	 * 绘制多边形(模式应为GL.GL_LINE_LOOP)
	 * 
	 * @param xPoints
	 * @param yPoints
	 * @param nPoints
	 */
	public void glDrawPoly(float[] xPoints, float[] yPoints, int nPoints) {
		$drawPolygon1(xPoints, yPoints, nPoints, false);
	}

	/**
	 * 填充多边形(模式应为GL.GL_LINE_LOOP)
	 * 
	 * @param xPoints
	 * @param yPoints
	 * @param nPoints
	 */
	public void glFillPoly(float[] xPoints, float[] yPoints, int nPoints) {
		$fillPolygon1(xPoints, yPoints, nPoints, false);
	}

	/**
	 * 检查是否开启了glbegin函数
	 * 
	 * @return
	 */
	public boolean useGLBegin() {
		return useBegin;
	}

	/**
	 * 模拟标准OpenGL的glEnd(实际为提交顶点坐标给OpenGL)
	 * 
	 */
	public final void glEnd() {
		if (isClose || !useBegin) {
			return;
		}
		int count = vertexBuf.position() / 3;
		if (count < 1) {
			useBegin = false;
			return;
		}
		vertexBuf.flip();
		gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl10.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuf);
		if (colorBuf.position() > 0) {
			colorBuf.flip();
			if (!isUpdateColor) {
				gl10.glEnableClientState(GL10.GL_COLOR_ARRAY);
				isUpdateColor = true;
			}
			gl10.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuf);
		}
		if (texCoordBuf.position() > 0) {
			texCoordBuf.flip();
			if (!isUpdateTexture) {
				gl10.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
				isUpdateTexture = true;
			}
			gl10.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordBuf);
		}
		switch (mode) {
		case GL.GL_QUADS:
			for (int i = 0; i < count; i += 4) {
				gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, i, 4);
			}
			break;
		case GL.GL_TRIANGLE_STRIP:
			for (int i = 0; i < count; i += 4) {
				gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, i, 4);
			}
			break;
		default:
			gl.glDrawArrays(mode, 0, count);
		}
		if (isUpdateTexture) {
			gl10.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			isUpdateTexture = false;
		}
		if (isUpdateColor) {
			gl10.glDisableClientState(GL10.GL_COLOR_ARRAY);
			if (color != null) {
				gl10.glColor4f(color.r, color.g, color.b, color.a);
			}
			isUpdateColor = false;
		}
		gl10.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		useBegin = false;
	}

	/**
	 * 开启2D纹理设置(禁用此前的纹理操作)
	 * 
	 */
	public final void glTex2DDisable() {
		if (isClose) {
			return;
		}
		if (isTex2DEnabled) {
			gl.glDisable(GL.GL_TEXTURE_2D);
			isTex2DEnabled = false;
		}
	}

	/**
	 * 关闭2D纹理设置(允许新的纹理操作)
	 * 
	 */
	public final void glTex2DEnable() {
		if (isClose) {
			return;
		}
		if (!isTex2DEnabled) {
			gl.glEnable(GL.GL_TEXTURE_2D);
			isTex2DEnabled = true;
		}
	}

	/**
	 * 允许顶点数组操作
	 * 
	 */
	public final void glTex2DARRAYEnable() {
		if (isClose) {
			return;
		}
		if (!isARRAYEnable) {
			gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			gl10.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			isARRAYEnable = true;
		}
	}

	/**
	 * 禁用定点数组操作
	 * 
	 */
	public final void glTex2DARRAYDisable() {
		if (isClose) {
			return;
		}
		if (isARRAYEnable) {
			gl10.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			gl10.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			isARRAYEnable = false;
		}
	}

	/**
	 * 设定gluLookAt
	 * 
	 * @param gl
	 * @param eyeX
	 * @param eyeY
	 * @param eyeZ
	 * @param centerX
	 * @param centerY
	 * @param centerZ
	 * @param upX
	 * @param upY
	 * @param upZ
	 */
	public void gluLookAt(GL10 gl, float eyeX, float eyeY, float eyeZ,
			float centerX, float centerY, float centerZ, float upX, float upY,
			float upZ) {
		if (isClose) {
			return;
		}
		glu.gluLookAt(gl, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX,
				upY, upZ);
	}

	/**
	 * 设定gluOrtho2D
	 * 
	 * @param gl
	 * @param left
	 * @param right
	 * @param bottom
	 * @param top
	 */
	public void gluOrtho2D(GL10 gl, float left, float right, float bottom,
			float top) {
		if (isClose) {
			return;
		}
		glu.gluOrtho2D(gl, left, right, bottom, top);
	}

	/**
	 * 设定gluPerspective
	 * 
	 * @param gl
	 * @param fovy
	 * @param aspect
	 * @param zNear
	 * @param zFar
	 */
	public void gluPerspective(GL10 gl, float fovy, float aspect, float zNear,
			float zFar) {
		if (isClose) {
			return;
		}
		glu.gluPerspective(gl, fovy, aspect, zNear, zFar);
	}

	/**
	 * 设定gluProject
	 * 
	 * @param objX
	 * @param objY
	 * @param objZ
	 * @param model
	 * @param modelOffset
	 * @param project
	 * @param projectOffset
	 * @param view
	 * @param viewOffset
	 * @param win
	 * @param winOffset
	 * @return
	 */
	public boolean gluProject(float objX, float objY, float objZ,
			float[] model, int modelOffset, float[] project, int projectOffset,
			int[] view, int viewOffset, float[] win, int winOffset) {
		if (isClose) {
			return false;
		}
		return glu.gluProject(objX, objY, objZ, model, modelOffset, project,
				projectOffset, view, viewOffset, win, winOffset);
	}

	/**
	 * 设定gluUnProject
	 * 
	 * @param winX
	 * @param winY
	 * @param winZ
	 * @param model
	 * @param modelOffset
	 * @param project
	 * @param projectOffset
	 * @param view
	 * @param viewOffset
	 * @param obj
	 * @param objOffset
	 * @return
	 */
	public boolean gluUnProject(float winX, float winY, float winZ,
			float[] model, int modelOffset, float[] project, int projectOffset,
			int[] view, int viewOffset, float[] obj, int objOffset) {
		if (isClose) {
			return false;
		}
		return glu.gluUnProject(winX, winY, winZ, model, modelOffset, project,
				projectOffset, view, viewOffset, obj, objOffset);
	}

	/**
	 * 设定当前使用的色彩混合模式
	 * 
	 * @param mode
	 */
	public final void setBlendMode(int mode) {
		if (isClose) {
			return;
		}
		if (currentBlendMode == mode) {
			return;
		}
		this.currentBlendMode = mode;
		if (currentBlendMode == GL.MODE_NORMAL) {
			GLUtils.enableBlend(gl10);
			gl10.glColorMask(true, true, true, true);
			gl10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			return;
		} else if (currentBlendMode == GL.MODE_ALPHA_MAP) {
			GLUtils.disableBlend(gl10);
			gl10.glColorMask(false, false, false, true);
			return;
		} else if (currentBlendMode == GL.MODE_ALPHA_BLEND) {
			GLUtils.enableBlend(gl10);
			gl10.glColorMask(true, true, true, false);
			gl10.glBlendFunc(GL10.GL_DST_ALPHA, GL10.GL_ONE_MINUS_DST_ALPHA);
			return;
		} else if (currentBlendMode == GL.MODE_COLOR_MULTIPLY) {
			GLUtils.enableBlend(gl10);
			gl10.glColorMask(true, true, true, true);
			gl10.glBlendFunc(GL10.GL_ONE_MINUS_SRC_COLOR, GL10.GL_SRC_COLOR);
			return;
		} else if (currentBlendMode == GL.MODE_ADD) {
			GLUtils.enableBlend(gl10);
			gl10.glColorMask(true, true, true, true);
			gl10.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE);
			return;
		} else if (currentBlendMode == GL.MODE_SPEED) {
			GLUtils.enableBlend(gl10);
			gl10.glColorMask(true, true, true, false);
			gl10.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
			return;
		} else if (currentBlendMode == GL.MODE_SCREEN) {
			GLUtils.enableBlend(gl10);
			gl10.glColorMask(true, true, true, true);
			gl10.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_COLOR);
			return;
		} else if (currentBlendMode == GL.MODE_ALPHA_ONE) {
			GLUtils.enableBlend(gl10);
			gl10.glColorMask(true, true, true, true);
			gl10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
			return;
		} else if (currentBlendMode == GL.MODE_ALPHA) {
			GLUtils.enableBlend(gl10);
			gl10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			return;
		} else if (currentBlendMode == GL.MODE_NONE) {
			GLUtils.disableBlend(gl10);
			gl10.glColorMask(true, true, true, false);
			return;
		}
	}

	/**
	 * 判断当前系统是否支持VBO
	 * 
	 * @return
	 */
	public final static boolean checkVBO() {
		if (isVboSupported()) {
			return true;
		}
		String string = org.lwjgl.opengl.GL11.glGetString(GL10.GL_EXTENSIONS);
		if (string.contains("vertex_buffer_object")) {
			GLEx.setVBOSupported(true);
			return true;
		}
		GLEx.setVBOSupported(false);
		return false;
	}

	/**
	 * 判定是否使用VBO
	 * 
	 * @return
	 */
	public final static boolean isVbo() {
		return GLEx.vboOn;
	}

	/**
	 * 设定是否使用VBO
	 * 
	 * @param vboOn
	 */
	public final static void setVbo(boolean vbo) {
		GLEx.vboOn = vbo;
	}

	/**
	 * 判定是否支持VBO
	 * 
	 * @return
	 */
	public static boolean isVboSupported() {
		return vboSupported;
	}

	/**
	 * 设定是否支持VBO
	 * 
	 * @param vboSupported
	 */
	public final static void setVBOSupported(boolean vboSupported) {
		GLEx.vboSupported = vboSupported;
	}

	/**
	 * 保存当前的矩阵设置
	 * 
	 */
	public final void glPushMatrix() {
		if (isClose) {
			return;
		}
		gl10.glPushMatrix();
	}

	/**
	 * 还原上次保存的矩阵设置
	 * 
	 */
	public final void glPopMatrix() {
		if (isClose) {
			return;
		}
		gl10.glPopMatrix();
	}

	/**
	 * 清除当前帧色彩
	 * 
	 * @param clear
	 */
	public void reset(boolean clear) {
		if (isClose) {
			return;
		}
		bind(0);
		if (isTex2DEnabled) {
			gl.glDisable(GL.GL_TEXTURE_2D);
			isTex2DEnabled = false;
		}
		if (clear) {
			gl10.glClearColor(0, 0, 0, 1);
			gl10.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT
					| GL10.GL_STENCIL_BUFFER_BIT);
		}
	}

	/**
	 * 清空屏幕
	 * 
	 */
	public final void drawClear() {
		if (isClose) {
			return;
		}
		drawClear(GLColor.black);
	}

	/**
	 * 以指定色彩清空屏幕
	 * 
	 * @param color
	 */
	public final void drawClear(GLColor color) {
		if (isClose) {
			return;
		}
		gl10.glClearColor(color.r, color.g, color.b, color.a);
		gl10.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT
				| GL10.GL_STENCIL_BUFFER_BIT);
	}

	/**
	 * 以指定色彩模式渲染画布
	 * 
	 * @param blendMode
	 */
	public final void beginBlend(int blendMode) {
		if (currentBlendMode == blendMode) {
			return;
		}
		this.prevBlendMode = currentBlendMode;
		this.setBlendMode(blendMode);
	}

	/**
	 * 回复色彩模式改变前的设置
	 * 
	 */
	public final void endBlend() {
		this.setBlendMode(prevBlendMode);
	}

	/**
	 * 设定色彩透明度
	 * 
	 * @param alpha
	 */
	public void setAlphaValue(int alpha) {
		if (isClose) {
			return;
		}
		setAlpha((float) alpha / 255);
	}

	public void test() {
		lastAlpha = 1 < 0 ? 0 : 1;
	}

	public boolean isAlpha() {
		return onAlpha;
	}

	/**
	 * 设定色彩透明度
	 * 
	 * @param alpha
	 */
	public void setAlpha(float alpha) {
		if (alpha == lastAlpha) {
			return;
		}
		lastAlpha = alpha < 0 ? 0 : alpha > 1 ? 1 : alpha;
		if (lastAlpha >= 0.95f) {
			if (onReplace) {
				glTexEnvfReplaceColor(1, 1, 1, 1);
			} else {
				glTexEnvfModulateColor(1, 1, 1, 1);
			}
			onAlpha = false;
		} else {
			glTexEnvfModulateColor(1, 1, 1, lastAlpha);
			onAlpha = true;
		}
	}

	/**
	 * 返回当前的色彩透明度
	 * 
	 * @return
	 */
	public float getAlpha() {
		return color.a;
	}

	/**
	 * 设定画布颜色
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 */
	public void setColorValue(int r, int g, int b, int a) {
		float red = (float) r / 255.0f;
		float green = (float) g / 255.0f;
		float blue = (float) b / 255.0f;
		float alpha = (float) a / 255.0f;
		setColor(red, green, blue, alpha);
	}

	/**
	 * 释放颜色设定
	 * 
	 */
	public final void resetColor() {
		if (isClose) {
			return;
		}
		if (!color.equals(GLColor.white)) {
			color.setColor(1f, 1f, 1f, 1f);
			gl10.glColor4f(1f, 1f, 1f, 1f);
		}
	}

	/**
	 * 设定画布颜色
	 * 
	 * @param color
	 */
	public final void setColorRGB(GLColor c) {
		if (isClose) {
			return;
		}
		if (!c.equals(color)) {
			updateColor(c.r, c.g, c.b, lastAlpha);
			color.setColor(c.r, c.g, c.b, lastAlpha);
			gl10.glColor4f(color.r, color.g, color.b, color.a);
		}
	}

	/**
	 * 设定画布颜色
	 * 
	 * @param color
	 */
	public final void setColorARGB(GLColor c) {
		if (isClose) {
			return;
		}
		if (!c.equals(color)) {
			float alpha = lastAlpha == 1 ? c.a : lastAlpha;
			updateColor(c.r, c.g, c.b, alpha);
			color.setColor(c.r, c.g, c.b, alpha);
			gl10.glColor4f(color.r, color.g, color.b, color.a);
		}
	}

	/**
	 * 设定画布颜色
	 * 
	 * @param pixel
	 */
	public final void setColor(int pixel) {
		int[] rgbs = GLColor.getRGBs(pixel);
		setColorValue(rgbs[0], rgbs[1], rgbs[2], (int) (lastAlpha * 255));
	}

	/**
	 * 设定画布颜色
	 * 
	 * @param c
	 */
	public final void setColor(GLColor c) {
		setColorARGB(c);
	}

	/**
	 * 设定画布颜色
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @param a
	 */
	public final void setColor(final float r, final float g, final float b,
			final float a) {
		if (isClose) {
			return;
		}
		updateColor(r, g, b, a);
		color.setColor(r, g, b, a);
		gl10.glColor4f(color.r, color.g, color.b, color.a);
	}

	/**
	 * 设定画布颜色
	 * 
	 * @param r
	 * @param g
	 * @param b
	 */
	public final void setColor(final float r, final float g, final float b) {
		setColor(r, g, b, lastAlpha);
	}

	/**
	 * 获得当前画布颜色
	 * 
	 * @return
	 */
	public final GLColor getColor() {
		return new GLColor(color);
	}

	public final int getColorRGB() {
		return color.getRGB();
	}

	public final int getColorARGB() {
		return color.getARGB();
	}

	private void updateColor(float r, float g, float b, float a) {
		if (!onReplace && !onTexEnvf && lastAlpha == 1
				&& !color.equals(r, g, b, a)) {
			gl10.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE,
					GL.GL_MODULATE);
		}
	}

	/**
	 * 是否采用清晰的绘图模式
	 * 
	 * @param flag
	 */
	public void setAntiAlias(boolean flag) {
		if (isClose) {
			return;
		}
		if (flag) {
			gl10.glEnable(GL.GL_LINE_SMOOTH);
		} else {
			gl10.glDisable(GL.GL_LINE_SMOOTH);
		}
		this.isAntialias = flag;
	}

	public boolean isAntialias() {
		return isAntialias;
	}

	/**
	 * 返回指定位置像素
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public GLColor getPixel(int x, int y) {
		if (isClose) {
			return color;
		}
		GLEx.gl10.glReadPixels(x,
				(int) (LSystem.screenRect.height * LSystem.scaleHeight) - y
						- viewPort.height, viewPort.width, viewPort.height,
				GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, readBuffer);
		return new GLColor(GLColor.c(readBuffer.get(0)), GLColor.c(readBuffer
				.get(1)), GLColor.c(readBuffer.get(2)), GLColor.c(readBuffer
				.get(3)));
	}

	/**
	 * 将指定区域像素封装为ByteBuffer后返回
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param target
	 */
	public void getArea(int x, int y, int width, int height, ByteBuffer target) {
		if (isClose) {
			return;
		}
		if (target.capacity() < width * height * 4) {
			throw new IllegalArgumentException(
					"Byte buffer provided to get area is not big enough");
		}
		gl10.glReadPixels(x,
				(int) (LSystem.screenRect.height * LSystem.scaleHeight) - y
						- height, width, height, GL10.GL_RGBA,
				GL10.GL_UNSIGNED_BYTE, target);

	}

	/**
	 * 绘制五角星
	 * 
	 * @param color
	 * @param x
	 * @param y
	 * @param r
	 */
	public void drawSixStart(GLColor color, float x, float y, float r) {
		if (isClose) {
			return;
		}
		setColor(color);
		drawTriangle(color, x, y, r);
		drawRTriangle(color, x, y, r);
	}

	/**
	 * 绘制正三角
	 * 
	 * @param color
	 * @param x
	 * @param y
	 * @param r
	 */
	public void drawTriangle(GLColor color, float x, float y, float r) {
		if (isClose) {
			return;
		}
		float x1 = x;
		float y1 = y - r;
		float x2 = x - (r * MathUtils.cos(MathUtils.PI / 6));
		float y2 = y + (r * MathUtils.sin(MathUtils.PI / 6));
		float x3 = x + (r * MathUtils.cos(MathUtils.PI / 6));
		float y3 = y + (r * MathUtils.sin(MathUtils.PI / 6));
		float[] xpos = new float[3];
		xpos[0] = x1;
		xpos[1] = x2;
		xpos[2] = x3;
		float[] ypos = new float[3];
		ypos[0] = y1;
		ypos[1] = y2;
		ypos[2] = y3;
		setColor(color);
		fillPolygon(xpos, ypos, 3);
	}

	/**
	 * 绘制倒三角
	 * 
	 * @param color
	 * @param x
	 * @param y
	 * @param r
	 */
	public void drawRTriangle(GLColor color, float x, float y, float r) {
		if (isClose) {
			return;
		}
		float x1 = x;
		float y1 = y + r;
		float x2 = x - (r * MathUtils.cos(MathUtils.PI / 6.0f));
		float y2 = y - (r * MathUtils.sin(MathUtils.PI / 6.0f));
		float x3 = x + (r * MathUtils.cos(MathUtils.PI / 6.0f));
		float y3 = y - (r * MathUtils.sin(MathUtils.PI / 6.0f));
		float[] xpos = new float[3];
		xpos[0] = x1;
		xpos[1] = x2;
		xpos[2] = x3;
		float[] ypos = new float[3];
		ypos[0] = y1;
		ypos[1] = y2;
		ypos[2] = y3;
		setColor(color);
		fillPolygon(xpos, ypos, 3);
	}

	/**
	 * 绘制三角形
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param x3
	 * @param y3
	 */
	public void drawTriangle(final float x1, final float y1, final float x2,
			final float y2, final float x3, final float y3) {
		if (isClose) {
			return;
		}
		glBegin(GL.GL_LINE_LOOP);
		glVertex2f(x1, y1);
		glVertex2f(x2, y2);
		glVertex2f(x3, y3);
		glEnd();
	}

	/**
	 * 填充三角形
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param x3
	 * @param y3
	 */
	public void fillTriangle(final float x1, final float y1, final float x2,
			final float y2, final float x3, final float y3) {
		if (isClose) {
			return;
		}
		glBegin(GL.GL_TRIANGLES);
		glVertex2f(x1, y1);
		glVertex2f(x2, y2);
		glVertex2f(x3, y3);
		glEnd();
	}

	/**
	 * 绘制并填充一组三角
	 * 
	 * @param ts
	 */
	public void fillTriangle(Triangle2f[] ts) {
		fillTriangle(ts, 0, 0);
	}

	/**
	 * 绘制并填充一组三角
	 * 
	 * @param ts
	 * @param x
	 * @param y
	 */
	public void fillTriangle(Triangle2f[] ts, int x, int y) {
		if (isClose) {
			return;
		}
		if (ts == null) {
			return;
		}
		int size = ts.length;
		for (int i = 0; i < size; i++) {
			fillTriangle(ts[i], x, y);
		}
	}

	/**
	 * 绘制并填充一组三角
	 * 
	 * @param t
	 */
	public void fillTriangle(Triangle2f t) {
		fillTriangle(t, 0, 0);
	}

	/**
	 * 绘制并填充一组三角
	 * 
	 * @param t
	 * @param x
	 * @param y
	 */
	public void fillTriangle(Triangle2f t, float x, float y) {
		if (isClose) {
			return;
		}
		if (t == null) {
			return;
		}
		float[] xpos = new float[3];
		float[] ypos = new float[3];
		xpos[0] = x + t.xpoints[0];
		xpos[1] = x + t.xpoints[1];
		xpos[2] = x + t.xpoints[2];
		ypos[0] = y + t.ypoints[0];
		ypos[1] = y + t.ypoints[1];
		ypos[2] = y + t.ypoints[2];
		fillPolygon(xpos, ypos, 3);
	}

	/**
	 * 绘制一组三角
	 * 
	 * @param ts
	 */
	public void drawTriangle(Triangle2f[] ts) {
		drawTriangle(ts, 0, 0);
	}

	/**
	 * 绘制一组三角
	 * 
	 * @param ts
	 * @param x
	 * @param y
	 */
	public void drawTriangle(Triangle2f[] ts, int x, int y) {
		if (isClose) {
			return;
		}
		if (ts == null) {
			return;
		}
		int size = ts.length;
		for (int i = 0; i < size; i++) {
			drawTriangle(ts[i], x, y);
		}
	}

	/**
	 * 绘制三角
	 * 
	 * @param t
	 */
	public void drawTriangle(Triangle2f t) {
		drawTriangle(t, 0, 0);
	}

	/**
	 * 绘制三角
	 * 
	 * @param t
	 * @param x
	 * @param y
	 */
	public void drawTriangle(Triangle2f t, float x, float y) {
		if (isClose) {
			return;
		}
		if (t == null) {
			return;
		}
		float[] xpos = new float[3];
		float[] ypos = new float[3];
		xpos[0] = x + t.xpoints[0];
		xpos[1] = x + t.xpoints[1];
		xpos[2] = x + t.xpoints[2];
		ypos[0] = y + t.ypoints[0];
		ypos[1] = y + t.ypoints[1];
		ypos[2] = y + t.ypoints[2];
		drawPolygon(xpos, ypos, 3);
	}

	/**
	 * 绘制椭圆
	 * 
	 * @param centerX
	 * @param centerY
	 * @param r
	 * @param a
	 */
	public void drawOval(float x1, float y1, float width, float height) {
		this.drawArc(x1, y1, width, height, 32, 0, 360);
	}

	/**
	 * 填充椭圆
	 * 
	 * @param centerX
	 * @param centerY
	 * @param r
	 * @param a
	 */
	public void fillOval(float x1, float y1, float width, float height) {
		this.fillArc(x1, y1, width, height, 32, 0, 360);
	}

	/**
	 * 画线
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public void drawLine(float x1, float y1, float x2, float y2) {
		if (isClose) {
			return;
		}
		try {
			switch (drawingType) {
			case 0:
				$drawLine0(x1, y1, x2, y2);
				break;
			case 1:
				$drawLine1(x1, y1, x2, y2, true);
				break;
			}
		} catch (Exception e) {
			switch (drawingType) {
			case 0:
				$drawLine1(x1, y1, x2, y2, true);
				break;
			case 1:
				$drawLine0(x1, y1, x2, y2);
				break;
			}
		}
	}

	/**
	 * drawLine函数的解决模式一
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	private void $drawLine0(float x1, float y1, float x2, float y2) {
		if (x1 > x2) {
			x1++;
		} else {
			x2++;
		}
		if (y1 > y2) {
			y1++;
		} else {
			y2++;
		}

		floatVertices.rewind();
		floatVertices.limit(4 * 2 * 2);
		floatVertices.put(x1);
		floatVertices.put(y1);
		floatVertices.put(x2);
		floatVertices.put(y2);
		floatVertices.position(0);
		floatVertices.flip();

		glTex2DDisable();
		{
			gl10.glVertexPointer(2, GL10.GL_FLOAT, 0, floatVertices);
			gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			gl10.glDrawArrays(GL10.GL_LINES, 0, 2);
			gl10.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		}
	}

	/**
	 * drawLine函数的解决模式二
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	private void $drawLine1(float x1, float y1, float x2, float y2, boolean use) {
		if (x1 > x2) {
			x1++;
		} else {
			x2++;
		}
		if (y1 > y2) {
			y1++;
		} else {
			y2++;
		}

		if (use) {
			glBegin(GL.GL_LINES);
		}
		{
			glVertex2f(x1, y1);
			glVertex2f(x2, y2);
		}
		if (use) {
			glEnd();
		}
	}

	/**
	 * 绘制色彩点
	 * 
	 * @param x
	 * @param y
	 */
	public void drawPoint(float x, float y) {
		if (isClose) {
			return;
		}
		floatVertices.rewind();
		floatVertices.limit(4 * 2 * 1);

		floatVertices.put(x);
		floatVertices.put(y);
		floatVertices.position(0);
		floatVertices.flip();
		glTex2DDisable();
		{
			gl10.glVertexPointer(2, GL10.GL_FLOAT, 0, floatVertices);
			gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			gl10.glDrawArrays(GL10.GL_POINTS, 0, 1);
			gl10.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		}
	}

	/**
	 * 绘制一组色彩点
	 * 
	 * @param x
	 * @param y
	 * @param size
	 */
	public void drawPoints(float x[], float y[], int size) {
		if (isClose) {
			return;
		}
		floatVertices.rewind();
		floatVertices.limit(4 * 2 * size);
		for (int i = 0; i < size; i++) {
			floatVertices.put(x[i]);
			floatVertices.put(y[i]);
		}
		floatVertices.position(0);
		floatVertices.flip();
		glTex2DDisable();
		{
			gl10.glVertexPointer(2, GL10.GL_FLOAT, 0, floatVertices);
			gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			gl10.glDrawArrays(GL10.GL_POINTS, 0, size);
			gl10.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		}
	}

	/**
	 * 绘制指定图形
	 * 
	 * @param shape
	 */
	public final void draw(Shape shape) {
		if (isClose) {
			return;
		}
		float[] points = shape.getPoints();
		switch (drawingType) {
		case 0:
			int size = points.length / 2;
			floatVertices.rewind();
			floatVertices.limit(4 * 2 * size);
			for (int i = 0; i < points.length; i += 2) {
				floatVertices.put(points[i]);
				floatVertices.put(points[i + 1]);
			}
			if (shape.closed()) {
				floatVertices.put(points[0]);
				floatVertices.put(points[1]);
			}
			floatVertices.position(0);
			floatVertices.flip();
			glTex2DDisable();
			{
				gl10.glVertexPointer(2, GL10.GL_FLOAT, 0, floatVertices);
				gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
				gl10.glDrawArrays(GL10.GL_LINE_LOOP, 0, size);
				gl10.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			}
			return;
		case 1:
			glBegin(GL.GL_LINE_STRIP);
			for (int i = 0; i < points.length; i += 2) {
				glVertex2f(points[i], points[i + 1]);
			}
			if (shape.closed()) {
				glVertex2f(points[0], points[1]);
			}
			glEnd();
			return;
		}
	}

	/**
	 * 填充指定图形
	 * 
	 * @param shape
	 */
	public final void fill(Shape shape) {
		Triangle tris = shape.getTriangles();
		if (isClose) {
			return;
		}
		float[] points = shape.getPoints();
		switch (drawingType) {
		case 0:
			int size = points.length / 2;
			floatVertices.rewind();
			floatVertices.limit(4 * 3 * size);
			for (int i = 0; i < tris.getTriangleCount(); i++) {
				for (int p = 0; p < 3; p++) {
					float[] pt = tris.getTrianglePoint(i, p);
					floatVertices.put(pt[0]);
					floatVertices.put(pt[1]);
					floatVertices.put(0);
				}
			}
			int count = floatVertices.position() / 3;
			floatVertices.position(0);
			floatVertices.flip();
			glTex2DDisable();
			{
				gl10.glVertexPointer(3, GL10.GL_FLOAT, 0, floatVertices);
				gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
				gl10.glDrawArrays(GL10.GL_TRIANGLES, 0, count);
				gl10.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			}
			return;
		case 1:
			glBegin(GL.GL_TRIANGLES);
			for (int i = 0; i < tris.getTriangleCount(); i++) {
				for (int p = 0; p < 3; p++) {
					float[] pt = tris.getTrianglePoint(i, p);
					glVertex2f(pt[0], pt[1]);
				}
			}
			glEnd();
			return;
		}
	}

	/**
	 * 绘制多边形到指定位置
	 * 
	 * @param p
	 * @param x
	 * @param y
	 */
	public void draw(final Shape p, final float x, final float y) {
		if (isClose) {
			return;
		}
		gl10.glPushMatrix();
		gl10.glTranslatef(x, y, 0.0f);
		draw(p);
		gl10.glPopMatrix();
	}

	/**
	 * 绘制多边形为指定旋转方向
	 * 
	 * @param p
	 * @param rotation
	 */
	public void draw(final Shape p, final float rotation) {
		if (isClose) {
			return;
		}
		gl10.glPushMatrix();
		gl10.glRotatef(-rotation, 0.0f, 0.0f, 1.0f);
		draw(p);
		gl10.glPopMatrix();
	}

	/**
	 * 填充多边形到指定位置
	 * 
	 * @param p
	 * @param x
	 * @param y
	 */
	public void fill(final Shape p, final float x, final float y) {
		if (isClose) {
			return;
		}
		gl10.glPushMatrix();
		gl10.glTranslatef(x, y, 0.0f);
		fill(p);
		gl10.glPopMatrix();
	}

	/**
	 * 填充多边形为指定旋转方向
	 * 
	 * @param p
	 * @param rotation
	 */
	public void fill(final Shape p, final float rotation) {
		if (isClose) {
			return;
		}
		gl10.glPushMatrix();
		gl10.glRotatef(-rotation, 0.0f, 0.0f, 1.0f);
		fill(p);
		gl10.glPopMatrix();
	}

	/**
	 * 填充多边形
	 * 
	 * @param p
	 */
	public void fillPolygon(Polygon p) {
		fill(p);
	}

	/**
	 * 填充多边形
	 * 
	 * @param xPoints
	 * @param yPoints
	 * @param nPoints
	 */
	public void fillPolygon(float xPoints[], float yPoints[], int nPoints) {
		if (isClose) {
			return;
		}
		try {
			switch (drawingType) {
			case 0:
				$fillPolygon0(xPoints, yPoints, nPoints);
				break;
			case 1:
				$fillPolygon1(xPoints, yPoints, nPoints, true);
				break;
			}
		} catch (Exception e) {
			switch (drawingType) {
			case 0:
				$fillPolygon1(xPoints, yPoints, nPoints, true);
				break;
			case 1:
				$fillPolygon0(xPoints, yPoints, nPoints);
				break;
			}
		}
	}

	/**
	 * fillPolygon解决模式一
	 * 
	 * @param xPoints
	 * @param yPoints
	 * @param nPoints
	 */
	private void $fillPolygon0(float xPoints[], float yPoints[], int nPoints) {
		floatVertices.rewind();
		floatVertices.limit(4 * 2 * nPoints);

		for (int i = 0; i < nPoints; i++) {
			floatVertices.put(xPoints[i]);
			floatVertices.put(yPoints[i]);
		}
		floatVertices.position(0);
		floatVertices.flip();
		glTex2DDisable();
		{
			gl10.glVertexPointer(2, GL10.GL_FLOAT, 0, floatVertices);
			gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			gl10.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, nPoints);
			gl10.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		}
	}

	/**
	 * fillPolygon解决模式二
	 * 
	 * @param xPoints
	 * @param yPoints
	 * @param nPoints
	 */
	private final void $fillPolygon1(float xPoints[], float yPoints[],
			int nPoints, boolean use) {
		if (use) {
			glBegin(GL.GL_POLYGON);
		}
		{
			for (int i = 0; i < nPoints; i++) {
				glVertex2f(xPoints[i], yPoints[i]);
			}
		}
		if (use) {
			glEnd();
		}
	}

	/**
	 * 绘制多边形轮廓
	 * 
	 * @param p
	 */
	public void drawPolygon(Polygon p) {
		draw(p);
	}

	/**
	 * 绘制多边形轮廓
	 * 
	 * @param xPoints
	 * @param yPoints
	 * @param nPoints
	 */
	public void drawPolygon(float[] xPoints, float[] yPoints, int nPoints) {
		if (isClose) {
			return;
		}
		try {
			switch (drawingType) {
			case 0:
				$drawPolygon0(xPoints, yPoints, nPoints);
				break;
			case 1:
				$drawPolygon1(xPoints, yPoints, nPoints, true);
				break;
			}
		} catch (Exception e) {
			switch (drawingType) {
			case 0:
				$drawPolygon1(xPoints, yPoints, nPoints, true);
				break;
			case 1:
				$drawPolygon0(xPoints, yPoints, nPoints);
				break;
			}
		}
	}

	/**
	 * drawPolygon解决方案一
	 * 
	 * @param xPoints
	 * @param yPoints
	 * @param nPoints
	 */
	private void $drawPolygon0(float[] xPoints, float[] yPoints, int nPoints) {
		floatVertices.rewind();
		floatVertices.limit(4 * 2 * nPoints);

		for (int i = 0; i < nPoints; i++) {
			floatVertices.put(xPoints[i]);
			floatVertices.put(yPoints[i]);
		}

		floatVertices.position(0);
		floatVertices.flip();
		glTex2DDisable();
		{
			gl10.glVertexPointer(2, GL10.GL_FLOAT, 0, floatVertices);
			gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			gl10.glDrawArrays(GL10.GL_LINE_LOOP, 0, nPoints);
			gl10.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		}
	}

	/**
	 * drawPolygon解决方案二
	 * 
	 * @param xPoints
	 * @param yPoints
	 * @param nPoints
	 */
	private void $drawPolygon1(float[] xPoints, float[] yPoints, int nPoints,
			boolean use) {
		if (use) {
			glBegin(GL.GL_LINE_LOOP);
		}
		for (int i = 0; i < nPoints; i++) {
			glVertex2f(xPoints[i], yPoints[i]);
		}
		if (use) {
			glEnd();
		}
	}

	/**
	 * 绘制一个矩形
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public final void drawRect(final float x1, final float y1, final float x2,
			final float y2) {
		setRect(x1, y1, x2, y2, false);
	}

	/**
	 * 填充一个矩形
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public final void fillRect(final float x1, final float y1, final float x2,
			final float y2) {
		setRect(x1, y1, x2, y2, true);
	}

	/**
	 * 设置矩形图案
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param fill
	 */
	public final void setRect(float x, float y, float width, float height,
			boolean fill) {
		if (isClose) {
			return;
		}
		float[] xs = new float[4];
		float[] ys = new float[4];

		xs[0] = x;
		xs[1] = x + width;
		xs[2] = x + width;
		xs[3] = x;

		ys[0] = y;
		ys[1] = y;
		ys[2] = y + height;
		ys[3] = y + height;

		if (fill) {
			fillPolygon(xs, ys, 4);
		} else {
			drawPolygon(xs, ys, 4);
		}
	}

	/**
	 * 绘制指定大小的弧度
	 * 
	 * @param rect
	 * @param segments
	 * @param start
	 * @param end
	 */
	public final void drawArc(RectBox rect, int segments, float start, float end) {
		drawArc(rect.x, rect.y, rect.width, rect.height, segments, start, end);
	}

	/**
	 * 绘制指定大小的弧度
	 * 
	 * @param x1
	 * @param y1
	 * @param width
	 * @param height
	 * @param segments
	 * @param start
	 * @param end
	 */
	public final void drawArc(float x1, float y1, float width, float height,
			int segments, float start, float end) {
		if (isClose) {
			return;
		}
		while (end < start) {
			end += 360;
		}
		float cx = x1 + (width / 2.0f);
		float cy = y1 + (height / 2.0f);
		glBegin(GL.GL_LINE_STRIP);
		int step = 360 / segments;
		for (int a = (int) start; a < (int) (end + step); a += step) {
			float ang = a;
			if (ang > end) {
				ang = end;
			}
			float x = (cx + (MathUtils.cos(MathUtils.toRadians(ang)) * width / 2.0f));
			float y = (cy + (MathUtils.sin(MathUtils.toRadians(ang)) * height / 2.0f));
			glVertex2f(x, y);
		}
		glEnd();
	}

	/**
	 * 填充指定大小的弧度
	 * 
	 * @param x1
	 * @param y1
	 * @param width
	 * @param height
	 * @param start
	 * @param end
	 */
	public final void fillArc(float x1, float y1, float width, float height,
			float start, float end) {
		fillArc(x1, y1, width, height, 40, start, end);
	}

	/**
	 * 填充指定大小的弧度
	 * 
	 * @param x1
	 * @param y1
	 * @param width
	 * @param height
	 * @param segments
	 * @param start
	 * @param end
	 */
	public final void fillArc(float x1, float y1, float width, float height,
			int segments, float start, float end) {
		if (isClose) {
			return;
		}
		while (end < start) {
			end += 360;
		}
		float cx = x1 + (width / 2.0f);
		float cy = y1 + (height / 2.0f);

		glBegin(GL.GL_TRIANGLE_FAN);
		int step = 360 / segments;

		glVertex2f(cx, cy);

		for (int a = (int) start; a < (int) (end + step); a += step) {
			float ang = a;
			if (ang > end) {
				ang = end;
			}

			float x = (float) (cx + (MathUtils.cos(MathUtils.toRadians(ang))
					* width / 2.0f));
			float y = (float) (cy + (MathUtils.sin(MathUtils.toRadians(ang))
					* height / 2.0f));

			glVertex2f(x, y);
		}
		glEnd();
		if (isAntialias) {
			glBegin(GL.GL_TRIANGLE_FAN);
			glVertex2f(cx, cy);
			if (end != 360) {
				end -= 10;
			}
			for (int j = (int) start; j < (int) (end + step); j += step) {
				float ang = j;
				if (ang > end) {
					ang = end;
				}

				float x = (float) (cx + (MathUtils.cos(MathUtils
						.toRadians(ang + 10))
						* width / 2.0f));
				float y = (float) (cy + (MathUtils.sin(MathUtils
						.toRadians(ang + 10))
						* height / 2.0f));

				glVertex2f(x, y);
			}
			glEnd();
		}
	}

	/**
	 * 绘制圆形边框
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param radius
	 */
	public final void drawRoundRect(float x, float y, float width,
			float height, int radius) {
		drawRoundRect(x, y, width, height, radius, 40);
	}

	/**
	 * 绘制圆形边框
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param radius
	 * @param segs
	 */
	public final void drawRoundRect(float x, float y, float width,
			float height, int radius, int segs) {
		if (isClose) {
			return;
		}
		if (radius < 0) {
			throw new IllegalArgumentException("radius > 0");
		}
		if (radius == 0) {
			drawRect(x, y, width, height);
			return;
		}
		int mr = (int) MathUtils.min(width, height) / 2;
		if (radius > mr) {
			radius = mr;
		}
		drawLine(x + radius, y, x + width - radius, y);
		drawLine(x, y + radius, x, y + height - radius);
		drawLine(x + width, y + radius, x + width, y + height - radius);
		drawLine(x + radius, y + height, x + width - radius, y + height);
		float d = radius * 2;
		drawArc(x + width - d, y + height - d, d, d, segs, 0, 90);
		drawArc(x, y + height - d, d, d, segs, 90, 180);
		drawArc(x + width - d, y, d, d, segs, 270, 360);
		drawArc(x, y, d, d, segs, 180, 270);
	}

	/**
	 * 填充圆形边框
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param cornerRadius
	 */
	public final void fillRoundRect(float x, float y, float width,
			float height, int cornerRadius) {
		fillRoundRect(x, y, width, height, cornerRadius, 40);
	}

	/**
	 * 填充圆形边框
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param radius
	 * @param segs
	 */
	public final void fillRoundRect(float x, float y, float width,
			float height, int radius, int segs) {
		if (isClose) {
			return;
		}
		if (radius < 0) {
			throw new IllegalArgumentException("radius > 0");
		}
		if (radius == 0) {
			fillRect(x, y, width, height);
			return;
		}
		int mr = (int) MathUtils.min(width, height) / 2;
		if (radius > mr) {
			radius = mr;
		}
		float d = radius * 2;
		fillRect(x + radius, y, width - d, radius);
		fillRect(x, y + radius, radius, height - d);
		fillRect(x + width - radius, y + radius, radius, height - d);
		fillRect(x + radius, y + height - radius, width - d, radius);
		fillRect(x + radius, y + radius, width - d, height - d);
		fillArc(x + width - d, y + height - d, d, d, segs, 0, 90);
		fillArc(x, y + height - d, d, d, segs, 90, 180);
		fillArc(x + width - d, y, d, d, segs, 270, 360);
		fillArc(x, y, d, d, segs, 180, 270);
	}

	/**
	 * 填充指定点
	 * 
	 * @param x
	 * @param y
	 */
	public void fillPixel(final float x, final float y) {
		if (isClose) {
			return;
		}
		try {
			glBegin(GL.GL_POINTS);
			glVertex2f(x, y);
			glEnd();
		} catch (Exception e) {
			drawPoint(x, y);
		}
	}

	public final void setLineWidth(float width) {
		if (isClose) {
			return;
		}
		this.lineWidth = width;
		gl10.glLineWidth(width);
		gl10.glPointSize(width);
	}

	public void resetLineWidth() {
		if (isClose) {
			return;
		}
		gl10.glLineWidth(1.0f);
		gl10.glPointSize(1.0f);
		this.lineWidth = 1.0f;
	}

	public final float getLineWidth() {
		return lineWidth;
	}

	public final void glLoadIdentity() {
		if (isClose) {
			return;
		}
		gl.glLoadIdentity();
	}

	public final void glReadPixels(int x, int y, int width, int height,
			int format, int type, ByteBuffer pixels) {
		if (isClose) {
			return;
		}
		gl.glReadPixels(x, y, width, height, format, type, pixels);
	}

	public final void glTexParameteri(int target, int param, int value) {
		if (isClose) {
			return;
		}
		gl.glTexParameterf(target, param, value);
	}

	public final void glDepthFunc(int func) {
		if (isClose) {
			return;
		}
		gl.glDepthFunc(func);
	}

	public final void glDepthMask(boolean mask) {
		if (isClose) {
			return;
		}
		gl.glDepthMask(mask);
	}

	public final void glGenTextures(IntBuffer ids) {
		if (isClose) {
			return;
		}
		gl.glGenTextures(ids.remaining(), ids);
	}

	public final int glGetError() {
		if (isClose) {
			return -1;
		}
		return gl10.glGetError();
	}

	public final void glTexSubImage2D(int glTexture2d, int i, int pageX,
			int pageY, int width, int height, int glBgra, int glUnsignedByte,
			ByteBuffer byteBuffer) {
		if (isClose) {
			return;
		}
		gl10.glTexSubImage2D(glTexture2d, i, pageX, pageY, width, height,
				glBgra, glUnsignedByte, byteBuffer);
	}

	public void glTexImage2D(int target, int i, int dstPixelFormat, int width,
			int height, int j, int srcPixelFormat, int glUnsignedByte,
			ByteBuffer byteBuffer) {
		if (isClose) {
			return;
		}
		gl10.glTexImage2D(target, i, dstPixelFormat, width, height, j,
				srcPixelFormat, glUnsignedByte, byteBuffer);
	}

	public void glScalef(float x, float y, float z) {
		if (isClose) {
			return;
		}
		gl10.glScalef(x, y, z);
	}

	public void glPixelStorei(int pname, int param) {
		if (isClose) {
			return;
		}
		gl10.glPixelStorei(pname, param);
	}

	public void glGenTextures(int count, int[] textures, int offset) {
		if (isClose) {
			return;
		}
		gl10.glGenTextures(count, textures, offset);
	}

	public void glBindTexture(int target, int texture) {
		if (isClose) {
			return;
		}
		gl10.glBindTexture(target, texture);
	}

	public void glDeleteTextures(int count, int[] textures, int offset) {
		if (isClose) {
			return;
		}
		gl10.glDeleteTextures(count, textures, offset);
	}

	public void glDisableClientState(int capability) {
		if (isClose) {
			return;
		}
		gl10.glDisableClientState(capability);
	}

	public void glDrawArrays(int mode, int first, int count) {
		if (isClose) {
			return;
		}
		gl10.glDrawArrays(mode, first, count);
	}

	public void glEnableClientState(int capability) {
		if (isClose) {
			return;
		}
		gl10.glEnableClientState(capability);
	}

	public void glTexParameterf(int target, int paramName, float paramValue) {
		if (isClose) {
			return;
		}
		gl.glTexParameterf(target, paramName, paramValue);
	}

	public void glColorMask(boolean r, boolean g, boolean b, boolean a) {
		if (isClose) {
			return;
		}
		gl10.glColorMask(r, g, b, a);
	}

	public void glColorPointerx(int size, int stride, IntBuffer pointer) {
		if (isClose) {
			return;
		}
		gl10.glColorPointer(size, GL.GL_FIXED, stride, pointer);
	}

	public void glVertexPointerx(int size, int stride, IntBuffer pointer) {
		if (isClose) {
			return;
		}
		gl10.glVertexPointer(size, GL.GL_FIXED, stride, pointer);
	}

	public void glTexCoordPointerx(int size, int stride, IntBuffer pointer) {
		if (isClose) {
			return;
		}
		gl10.glTexCoordPointer(size, GL.GL_FIXED, stride, pointer);
	}

	public void glTexCoordPointer(int size, int stride, Buffer pointer) {
		if (isClose) {
			return;
		}
		gl10.glTexCoordPointer(size, GL.GL_FIXED, stride, pointer);
	}

	public void glTexCoordPointer(int size, int type, int stride, Buffer pointer) {
		if (isClose) {
			return;
		}
		gl10.glTexCoordPointer(size, type, stride, pointer);
	}

	public void glDisable(int capability) {
		if (isClose) {
			return;
		}
		gl10.glDisable(capability);
	}

	public void glEnable(int capability) {
		if (isClose) {
			return;
		}
		gl10.glEnable(capability);
	}

	public void glBlendFunc(int sfactor, int dfactor) {
		if (isClose) {
			return;
		}
		gl10.glBlendFunc(sfactor, dfactor);
	}

	public void glClearColor(float red, float green, float blue, float alpha) {
		if (isClose) {
			return;
		}
		gl10.glClearColor(red, green, blue, alpha);
	}

	public void glClearDepth(float depth) {
		if (isClose) {
			return;
		}
		gl10.glClearDepthf(depth);
	}

	public void glHint(int target, int mode) {
		if (isClose) {
			return;
		}
		gl10.glHint(target, mode);
	}

	public void glMatrixMode(int mode) {
		if (isClose) {
			return;
		}
		gl10.glMatrixMode(mode);
	}

	public void glOrtho(float left, float right, float bottom, float top,
			float zNear, float zFar) {
		if (isClose) {
			return;
		}
		gl10.glOrthof(left, right, bottom, top, zNear, zFar);
	}

	public void glScissor(int x, int y, int width, int height) {
		if (isClose) {
			return;
		}
		gl10.glScissor(x, y, width, height);
	}

	public void glShadeModel(int mode) {
		if (isClose) {
			return;
		}
		gl10.glShadeModel(mode);
	}

	public void glViewport(int x, int y, int width, int height) {
		if (isClose) {
			return;
		}
		gl.glViewport(x, y, width, height);
	}

	public void glClear(int mask) {
		if (isClose) {
			return;
		}
		gl.glClear(mask);
	}

	public void glPointSize(int size) {
		if (isClose) {
			return;
		}
		gl10.glPointSize(size);
	}

	public void glColor4f(float r, float g, float b, float a) {
		if (isClose) {
			return;
		}
		gl10.glColor4f(r, g, b, a);
	}

	public void glRotate(float angle, float x, float y, float z) {
		if (isClose) {
			return;
		}
		gl10.glRotatef(angle, x, y, z);
	}

	public static void updateHardwareBuff(LTexture texture) {
		if (!vboOn) {
			return;
		}
		IntBuffer buff = genBuffers(1);
		texture.bufferID = buff.get(0);
		gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, texture.bufferID);
		gl11.glBufferData(GL11.GL_ARRAY_BUFFER, texture.dataSize, texture.data,
				GL11.GL_STATIC_DRAW);
		gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
	}

	public static void updateHardwareBuffDouble(LSTRTexture font) {
		if (!vboOn) {
			return;
		}
		IntBuffer buff = genBuffers(2);
		font.vertBufID = buff.get(0);
		gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, font.vertBufID);
		gl11.glBufferData(GL11.GL_ARRAY_BUFFER, font.vertSize, font.vertData,
				GL11.GL_STATIC_DRAW);
		font.texBufID = buff.get(1);
		gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, font.texBufID);
		gl11.glBufferData(GL11.GL_ARRAY_BUFFER, font.texSize, font.texData,
				GL11.GL_STATIC_DRAW);
		gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
	}

	public static int createTextureID() {
		int[] textures = new int[1];
		gl10.glGenTextures(1, textures, 0);
		return textures[0];
	}

	public static IntBuffer genTextures(int tcount) {
		IntBuffer buffer = BufferUtils.createIntBuffer(tcount);
		gl10.glGenTextures(tcount, buffer);
		return buffer;
	}

	public static int createBufferID() {
		if (!vboOn) {
			return -1;
		}
		IntBuffer buffer = BufferUtils.createIntBuffer(1);
		gl11.glGenBuffers(1, buffer);
		return buffer.get(0);
	}

	public static IntBuffer genBuffers(int bcount) {
		if (!vboOn) {
			return IntBuffer.wrap(new int[] { -1 });
		}
		IntBuffer buffer = BufferUtils.createIntBuffer(bcount);
		gl11.glGenBuffers(bcount, buffer);
		return buffer;
	}

	public static void bufferDataARR(int bufferID, FloatBuffer data, int usage) {
		gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, bufferID);
		gl11.glBufferData(GL11.GL_ARRAY_BUFFER, data.remaining(), data, usage);
		gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
		data.position(0);
	}

	public static void bufferSubDataARR(int bufferID, int offset,
			FloatBuffer data) {
		gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, bufferID);
		gl11.glBufferSubData(GL11.GL_ARRAY_BUFFER, offset, data.remaining(),
				data);
		gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
		data.position(0);
	}

	public static void deleteTexture(int textureID) {
		IntBuffer buffer = BufferUtils.createIntBuffer(1);
		buffer.put(textureID);
		buffer.flip();
		try {
			gl11.glDeleteTextures(1, buffer);
		} catch (Exception ex) {
		}
	}

	public static void deleteBuffer(int bufferID) {
		if (!vboOn) {
			return;
		}
		IntBuffer buffer = BufferUtils.createIntBuffer(1);
		buffer.put(bufferID);
		buffer.flip();
		try {
			gl11.glDeleteBuffers(1, buffer);
		} catch (Exception ex) {
		}
	}

	public static void glEnableBlend() {
		if (!isAlpha && !isMask) {
			gl.glEnable(GL10.GL_BLEND);
			gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			isAlpha = true;
		}
	}

	public static void glDisableBlend() {
		if (isAlpha && !isMask) {
			gl.glDisable(GL10.GL_BLEND);
			isAlpha = false;
		}
	}

	public static void glEnableMask(boolean flag) {
		if (flag) {
			gl.glEnable(GL10.GL_BLEND);
			gl.glBlendFunc(GL10.GL_DST_COLOR, GL10.GL_ZERO);
			isAlpha = true;
			isMask = true;
		} else if (isAlpha) {
			gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE);
			isAlpha = true;
			isMask = true;
		}
	}

	public static void glDisableMask() {
		if (isMask) {
			gl.glDisable(GL10.GL_BLEND);
			isAlpha = false;
			isMask = false;
		}
	}

	/**
	 * 验证是否为2的N次幂
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isPowerOfTwo(int value) {
		return value != 0 && (value & value - 1) == 0;
	}

	/**
	 * 转换数值为2的N次幂
	 * 
	 * @param value
	 * @return
	 */
	public static int toPowerOfTwo(int value) {
		if (value == 0) {
			return 1;
		}
		if ((value & value - 1) == 0) {
			return value;
		}
		value |= value >> 1;
		value |= value >> 2;
		value |= value >> 4;
		value |= value >> 8;
		value |= value >> 16;
		return value + 1;
	}

	/**
	 * 清空剪切后的显示区域
	 * 
	 */
	public void clearClip() {
		if (isClose) {
			return;
		}
		try {
			if (isScissorTest) {
				gl10.glDisable(GL10.GL_SCISSOR_TEST);
				isScissorTest = false;
			}
			clip.setBounds(0, 0, (int) viewPort.width, (int) viewPort.height);
			gl.glScissor(0, 0, (int) (viewPort.width * LSystem.scaleWidth),
					(int) (viewPort.height * LSystem.scaleHeight));
		} catch (Exception e) {
		}
	}

	/**
	 * 设定指定剪切区域显示图像
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public final void setClip(int x, int y, int width, int height) {
		if (isClose) {
			return;
		}
		if (!isScissorTest) {
			gl10.glEnable(GL10.GL_SCISSOR_TEST);
			isScissorTest = true;
		}
		clip.setBounds(x, y, width, height);
		gl10
				.glScissor(
						(int) (x * LSystem.scaleWidth),
						(int) ((LSystem.screenRect.height - y - height) * LSystem.scaleHeight),
						(int) (width * LSystem.scaleWidth),
						(int) (height * LSystem.scaleHeight));
	}

	/**
	 * 设定指定剪切区域显示图像
	 * 
	 * @param c
	 */
	public final void setClip(Clip c) {
		if (isClose) {
			return;
		}
		if (c == null) {
			clearClip();
			return;
		}
		setClip(c.x, c.y, c.width, c.height);
	}

	/**
	 * 返回当前的剪切区域
	 * 
	 * @return
	 */
	public Clip getClip() {
		return new Clip(clip);
	}

	public int getClipHeight() {
		return clip.height;
	}

	public int getClipWidth() {
		return clip.width;
	}

	public int getClipX() {
		return clip.x;
	}

	public int getClipY() {
		return clip.y;
	}

	public final void setViewPort(int x, int y, int width, int height) {
		if (isClose) {
			return;
		}
		gl10.glViewport(x, y, width, height);
		gl10.glLoadIdentity();
	}

	public final void setViewPort(RectBox port) {
		setViewPort((int) port.x, (int) port.y, (int) port.width,
				(int) port.height);
	}

	public final RectBox getViewPort() {
		return viewPort;
	}

	public void save() {
		if (isClose) {
			return;
		}
		if (!isPushed) {
			gl10.glPushMatrix();
			isPushed = true;
		}
	}

	public void restore() {
		if (isClose) {
			return;
		}
		this.lastAlpha = 1;
		this.sx = 1;
		this.sy = 1;
		if (isPushed) {
			gl10.glPopMatrix();
			isPushed = false;
		}
		resetFont();
	}

	public void scale(float sx, float sy) {
		if (isClose) {
			return;
		}
		save();
		this.sx = this.sx * sx;
		this.sy = this.sy * sy;
		try {
			gl10.glScalef(sx, sy, 1);
		} catch (Exception e) {
			gl10.glScalef(sx, sy, 0);
		}
	}

	public void rotate(float rx, float ry, float angle) {
		if (isClose) {
			return;
		}
		save();
		translate(rx, ry);
		gl10.glRotatef(angle, 0, 0, 1);
		translate(-rx, -ry);
	}

	public final void rotate(float angle) {
		float centerX = viewPort.width / 2;
		float centerY = viewPort.height / 2;
		rotate(angle, centerX, centerY);
	}

	public void translate(float x, float y) {
		if (isClose) {
			return;
		}
		save();
		translateX = x;
		translateY = y;
		gl10.glTranslatef(x, y, 0);
		clip.x -= x;
		clip.width -= x;
		clip.y -= y;
		clip.height -= y;
	}

	public void glTranslatef(float x, float y, float z) {
		if (isClose) {
			return;
		}
		gl10.glTranslatef(x, y, z);
	}

	public void glRotatef(float angle, float x, float y, float z) {
		if (isClose) {
			return;
		}
		gl10.glRotatef(angle, x, y, z);
	}

	/**
	 * 设定背景颜色
	 * 
	 * @param color
	 */
	public final void setBackground(GLColor color) {
		if (isClose) {
			return;
		}
		gl10.glClearColor(color.r, color.g, color.b, color.a);
	}

	/**
	 * 设定深度测试模式是否开启
	 * 
	 * @param flag
	 */
	public void setDepth(boolean flag) {
		if (flag) {
			gl10.glEnable(GL10.GL_DEPTH_TEST);
			gl10.glDepthFunc(GL10.GL_LEQUAL);
			gl10.glDepthRangef(0.0F, 1.0F);
			gl10.glClearDepthf(1.0F);
		} else {
			gl10.glDisable(GL10.GL_DEPTH_TEST);
		}
	}

	/**
	 * 设定双面剪切模式是否开启
	 * 
	 * @param flag
	 */
	public void setCullFace(boolean flag) {
		if (flag) {
			gl10.glFrontFace(GL10.GL_CCW);
			gl10.glEnable(GL10.GL_CULL_FACE);
			gl10.glCullFace(GL10.GL_BACK);
		} else {
			gl10.glDisable(GL10.GL_CULL_FACE);
		}
	}

	/**
	 * 渲染图片到指定位置并修正为指定色彩
	 * 
	 * @param image
	 * @param x
	 * @param y
	 * @param color
	 */
	public final void drawImage(LImage image, float x, float y, GLColor color) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), x, y, color);
	}

	/**
	 * 渲染图片到指定位置并修正为指定角度
	 * 
	 * @param image
	 * @param x
	 * @param y
	 * @param rotation
	 */
	public final void drawImage(LImage image, float x, float y, float rotation) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), x, y, rotation);
	}

	/**
	 * 渲染图片为指定状态
	 * 
	 * @param image
	 * @param x
	 * @param y
	 * @param dir
	 */
	public final void drawImage(LImage image, float x, float y, Direction dir) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), x, y, dir);
	}

	/**
	 * 渲染图片为指定状态
	 * 
	 * @param image
	 * @param x
	 * @param y
	 * @param color
	 * @param rotation
	 */
	public final void drawImage(LImage image, float x, float y, GLColor color,
			float rotation) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), x, y, color, rotation);
	}

	/**
	 * 渲染图片到指定位置
	 * 
	 * @param image
	 * @param x
	 * @param y
	 */
	public final void drawImage(LImage image, float x, float y) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), x, y);
	}

	/**
	 * 渲染图片为指定状态
	 * 
	 * @param image
	 * @param x
	 * @param y
	 * @param color
	 * @param dir
	 */
	public final void drawImage(LImage image, float x, float y, GLColor color,
			Direction dir) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), x, y, color, dir);
	}

	/**
	 * 渲染图片为指定状态
	 * 
	 * @param image
	 * @param x
	 * @param y
	 * @param color
	 * @param rotation
	 * @param origin
	 * @param dir
	 */
	public final void drawImage(LImage image, float x, float y, GLColor color,
			float rotation, Vector2f origin, Direction dir) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), x, y, color, rotation, origin, dir);
	}

	/**
	 * 渲染图片为指定状态
	 * 
	 * @param image
	 * @param x
	 * @param y
	 * @param color
	 * @param rotation
	 * @param origin
	 * @param scale
	 * @param dir
	 */
	public final void drawImage(LImage image, float x, float y, GLColor color,
			float rotation, Vector2f origin, float scale, Direction dir) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), x, y, color, rotation, origin, scale,
				dir);
	}

	/**
	 * 渲染图片为指定状态
	 * 
	 * @param image
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public final void drawImage(LImage image, float x, float y, float width,
			float height) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), x, y, width, height);
	}

	/**
	 * 渲染图片为指定状态
	 * 
	 * @param image
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param color
	 */
	public final void drawImage(LImage image, float x, float y, float width,
			float height, GLColor color) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), x, y, width, height, color);
	}

	/**
	 * 渲染图片为指定状态
	 * 
	 * @param image
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param rotation
	 */
	public final void drawImage(LImage image, float x, float y, float width,
			float height, float rotation) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), x, y, width, height, rotation);
	}

	/**
	 * 渲染图片为指定状态
	 * 
	 * @param texture
	 * @param x
	 * @param y
	 * @param rotation
	 * @param d
	 */
	public final void drawImage(LTexture texture, float x, float y,
			float rotation, Direction d) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, x, y, rotation, d);
	}

	/**
	 * 渲染图片为指定状态
	 * 
	 * @param image
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param dir
	 */
	public final void drawImage(LImage image, float x, float y, float width,
			float height, Direction dir) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), x, y, width, height, dir);
	}

	/**
	 * 渲染图片为指定状态
	 * 
	 * @param image
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param color
	 * @param rotation
	 */
	public final void drawImage(LImage image, float x, float y, float width,
			float height, GLColor color, float rotation) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), x, y, width, height, color, rotation);
	}

	/**
	 * 渲染图片为指定状态
	 * 
	 * @param image
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param color
	 * @param dir
	 */
	public final void drawImage(LImage image, float x, float y, float width,
			float height, GLColor color, Direction dir) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), x, y, width, height, color, dir);
	}

	/**
	 * 渲染图片为指定状态
	 * 
	 * @param image
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param color
	 * @param rotation
	 * @param origin
	 * @param dir
	 */
	public final void drawImage(LImage image, float x, float y, float width,
			float height, GLColor color, float rotation, Vector2f origin,
			Direction dir) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), x, y, width, height, color, rotation,
				origin, dir);
	}

	/**
	 * 渲染图片为指定状态
	 * 
	 * @param image
	 * @param dx1
	 * @param dy1
	 * @param dx2
	 * @param dy2
	 * @param sx1
	 * @param sy1
	 * @param sx2
	 * @param sy2
	 */
	public final void drawImage(LImage image, float dx1, float dy1, float dx2,
			float dy2, float sx1, float sy1, float sx2, float sy2) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2);
	}

	/**
	 * 按照Java中近似作用函数切分图片
	 * 
	 * @param image
	 * @param dx1
	 * @param dy1
	 * @param dx2
	 * @param dy2
	 * @param sx1
	 * @param sy1
	 * @param sx2
	 * @param sy2
	 */
	public final void drawJavaImage(LImage image, float dx1, float dy1,
			float dx2, float dy2, float sx1, float sy1, float sx2, float sy2) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawJavaTexture(image.getTexture(), dx1, dy1, dx2, dy2, sx1, sy1, sx2,
				sy2);
	}

	/**
	 * 渲染图片为指定状态
	 * 
	 * @param image
	 * @param dx1
	 * @param dy1
	 * @param dx2
	 * @param dy2
	 * @param sx1
	 * @param sy1
	 * @param sx2
	 * @param sy2
	 * @param color
	 */
	public final void drawImage(LImage image, float dx1, float dy1, float dx2,
			float dy2, float sx1, float sy1, float sx2, float sy2, GLColor color) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2,
				color);
	}

	/**
	 * 渲染图片为指定状态
	 * 
	 * @param image
	 * @param dx1
	 * @param dy1
	 * @param dx2
	 * @param dy2
	 * @param sx1
	 * @param sy1
	 * @param sx2
	 * @param sy2
	 * @param rotation
	 * @param color
	 */
	public final void drawImage(LImage image, float dx1, float dy1, float dx2,
			float dy2, float sx1, float sy1, float sx2, float sy2,
			float rotation, GLColor color) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2,
				rotation, color);
	}

	/**
	 * 渲染图片为指定状态
	 * 
	 * @param image
	 * @param destRect
	 */
	public final void drawImage(LImage image, RectBox destRect) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), destRect);
	}

	/**
	 * 渲染图片为指定状态
	 * 
	 * @param image
	 * @param destRect
	 * @param color
	 */
	public final void drawImage(LImage image, RectBox destRect, GLColor color) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), destRect, color);
	}

	/**
	 * 渲染图片为指定状态
	 * 
	 * @param image
	 * @param destRect
	 * @param rotation
	 */
	public final void drawImage(LImage image, RectBox destRect, float rotation) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), destRect, rotation);
	}

	/**
	 * 渲染图片为指定状态
	 * 
	 * @param image
	 * @param destRect
	 * @param dir
	 */
	public final void drawImage(LImage image, RectBox destRect, Direction dir) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), destRect, dir);
	}

	/**
	 * 渲染图片为指定状态
	 * 
	 * @param image
	 * @param destRect
	 * @param color
	 * @param rotation
	 */
	public final void drawImage(LImage image, RectBox destRect, GLColor color,
			float rotation) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), destRect, color, rotation);
	}

	/**
	 * 渲染图片为指定状态
	 * 
	 * @param image
	 * @param destRect
	 * @param color
	 * @param dir
	 */
	public final void drawImage(LImage image, RectBox destRect, GLColor color,
			Direction dir) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), destRect, color, dir);
	}

	/**
	 * 渲染图片为指定状态
	 * 
	 * @param image
	 * @param destRect
	 * @param color
	 * @param rotation
	 * @param origin
	 * @param dir
	 */
	public final void drawImage(LImage image, RectBox destRect, GLColor color,
			float rotation, Vector2f origin, Direction dir) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), destRect, color, rotation, origin, dir);
	}

	/**
	 * 渲染图片为指定状态
	 * 
	 * @param image
	 * @param destRect
	 * @param srcRect
	 */
	public final void drawImage(LImage image, RectBox destRect, RectBox srcRect) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), destRect, srcRect);
	}

	/**
	 * 渲染图片为指定状态
	 * 
	 * @param image
	 * @param destRect
	 * @param srcRect
	 * @param color
	 */
	public final void drawImage(LImage image, RectBox destRect,
			RectBox srcRect, GLColor color) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), destRect, srcRect, color);
	}

	/**
	 * 渲染图片为指定状态
	 * 
	 * @param image
	 * @param destRect
	 * @param srcRect
	 * @param rotation
	 */
	public final void drawImage(LImage image, RectBox destRect,
			RectBox srcRect, float rotation) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), destRect, srcRect, rotation);
	}

	/**
	 * 渲染图片为指定状态
	 * 
	 * @param image
	 * @param destRect
	 * @param srcRect
	 * @param dir
	 */
	public final void drawImage(LImage image, RectBox destRect,
			RectBox srcRect, Direction dir) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), destRect, srcRect, dir);
	}

	/**
	 * 渲染图片为指定状态
	 * 
	 * @param image
	 * @param destRect
	 * @param srcRect
	 * @param color
	 * @param rotation
	 */
	public final void drawImage(LImage image, RectBox destRect,
			RectBox srcRect, GLColor color, float rotation) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), destRect, srcRect, color, rotation);
	}

	/**
	 * 渲染图片为指定状态
	 * 
	 * @param image
	 * @param destRect
	 * @param srcRect
	 * @param color
	 * @param dir
	 */
	public final void drawImage(LImage image, RectBox destRect,
			RectBox srcRect, GLColor color, Direction dir) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), destRect, srcRect, color, dir);
	}

	/**
	 * 渲染图片为指定状态
	 * 
	 * @param image
	 * @param destRect
	 * @param srcRect
	 * @param color
	 * @param rotation
	 * @param origin
	 * @param dir
	 */
	public final void drawImage(LImage image, RectBox destRect,
			RectBox srcRect, GLColor color, float rotation, Vector2f origin,
			Direction dir) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), destRect, srcRect, color, rotation,
				origin, dir);
	}

	/**
	 * 渲染图片为指定状态
	 * 
	 * @param image
	 * @param position
	 */
	public final void drawImage(LImage image, Vector2f position) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), position);
	}

	/**
	 * 渲染图片为指定状态
	 * 
	 * @param image
	 * @param position
	 * @param color
	 */
	public final void drawImage(LImage image, Vector2f position, GLColor color) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), position, color);
	}

	/**
	 * 渲染图片为指定状态
	 * 
	 * @param image
	 * @param position
	 * @param rotation
	 */
	public final void drawImage(LImage image, Vector2f position, float rotation) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), position, rotation);
	}

	/**
	 * 渲染图片为指定状态
	 * 
	 * @param image
	 * @param position
	 * @param dir
	 */
	public final void drawImage(LImage image, Vector2f position, Direction dir) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), position, dir);
	}

	/**
	 * 渲染图片为指定状态
	 * 
	 * @param image
	 * @param position
	 * @param color
	 * @param rotation
	 */
	public final void drawImage(LImage image, Vector2f position, GLColor color,
			float rotation) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), position, color, rotation);
	}

	/**
	 * 渲染图片为指定状态
	 * 
	 * @param image
	 * @param position
	 * @param color
	 * @param dir
	 */
	public final void drawImage(LImage image, Vector2f position, GLColor color,
			Direction dir) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), position, color, dir);
	}

	/**
	 * 渲染图片为指定状态
	 * 
	 * @param image
	 * @param position
	 * @param color
	 * @param rotation
	 * @param origin
	 * @param dir
	 */
	public final void drawImage(LImage image, Vector2f position, GLColor color,
			float rotation, Vector2f origin, Direction dir) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), position, color, rotation, origin, dir);
	}

	/**
	 * 渲染图片为指定状态
	 * 
	 * @param image
	 * @param position
	 * @param color
	 * @param rotation
	 * @param origin
	 * @param scale
	 * @param dir
	 */
	public final void drawImage(LImage image, Vector2f position, GLColor color,
			float rotation, Vector2f origin, float scale, Direction dir) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), position, color, rotation, origin,
				scale, dir);
	}

	/**
	 * 渲染图片为指定状态
	 * 
	 * @param image
	 * @param position
	 * @param srcRect
	 */
	public final void drawImage(LImage image, Vector2f position, RectBox srcRect) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), position, srcRect);
	}

	/**
	 * 渲染图片为指定状态
	 * 
	 * @param image
	 * @param position
	 * @param srcRect
	 * @param color
	 */
	public final void drawImage(LImage image, Vector2f position,
			RectBox srcRect, GLColor color) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), position, srcRect, color);
	}

	/**
	 * 渲染图片为指定状态
	 * 
	 * @param image
	 * @param position
	 * @param srcRect
	 * @param rotation
	 */
	public final void drawImage(LImage image, Vector2f position,
			RectBox srcRect, float rotation) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), position, srcRect, rotation);
	}

	/**
	 * 渲染图片为指定状态
	 * 
	 * @param image
	 * @param position
	 * @param srcRect
	 * @param dir
	 */
	public final void drawImage(LImage image, Vector2f position,
			RectBox srcRect, Direction dir) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), position, srcRect, dir);
	}

	/**
	 * 渲染图片为指定状态
	 * 
	 * @param image
	 * @param position
	 * @param srcRect
	 * @param color
	 * @param rotation
	 */
	public final void drawImage(LImage image, Vector2f position,
			RectBox srcRect, GLColor color, float rotation) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), position, srcRect, color, rotation);
	}

	/**
	 * 渲染图片为指定状态
	 * 
	 * @param image
	 * @param position
	 * @param srcRect
	 * @param color
	 * @param dir
	 */
	public final void drawImage(LImage image, Vector2f position,
			RectBox srcRect, GLColor color, Direction dir) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), position, srcRect, color, dir);
	}

	/**
	 * 渲染图片为指定状态
	 * 
	 * @param image
	 * @param position
	 * @param srcRect
	 * @param color
	 * @param rotation
	 * @param origin
	 * @param dir
	 */
	public final void drawImage(LImage image, Vector2f position,
			RectBox srcRect, GLColor color, float rotation, Vector2f origin,
			Direction dir) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), position, srcRect, color, rotation,
				origin, dir);
	}

	/**
	 * 渲染图片为指定状态
	 * 
	 * @param image
	 * @param position
	 * @param srcRect
	 * @param color
	 * @param rotation
	 * @param origin
	 * @param scale
	 * @param dir
	 */
	public final void drawImage(LImage image, Vector2f position,
			RectBox srcRect, GLColor color, float rotation, Vector2f origin,
			float scale, Direction dir) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawTexture(image.getTexture(), position, srcRect, color, rotation,
				origin, scale, dir);
	}

	/**
	 * 渲染纹理到指定位置
	 * 
	 * @param texture
	 * @param x
	 * @param y
	 */
	public final void drawTexture(LTexture texture, float x, float y) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, x, y, texture.width, texture.height, 0, 0,
				texture.width, texture.height, color, 0, null, null);
	}

	/**
	 * 渲染纹理到指定位置
	 * 
	 * @param texture
	 * @param x
	 * @param y
	 * @param rotation
	 * @param d
	 */
	public final void drawTexture(LTexture texture, float x, float y,
			float rotation, Direction d) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, x, y, texture.width, texture.height, 0, 0,
				texture.width, texture.height, color, rotation, null, d);
	}

	/**
	 * 渲染纹理到指定位置并修正为指定色彩
	 * 
	 * @param texture
	 * @param x
	 * @param y
	 * @param color
	 */
	public final void drawTexture(LTexture texture, float x, float y,
			GLColor color) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, x, y, texture.width, texture.height, 0, 0,
				texture.width, texture.height, color, 0, null, null);
	}

	/**
	 * 渲染纹理到指定位置并修正为指定角度
	 * 
	 * @param texture
	 * @param x
	 * @param y
	 * @param rotation
	 */
	public final void drawTexture(LTexture texture, float x, float y,
			float rotation) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, x, y, texture.width, texture.height, 0, 0,
				texture.width, texture.height, color, rotation, null, null);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param x
	 * @param y
	 * @param dir
	 */
	public final void drawTexture(LTexture texture, float x, float y,
			Direction dir) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, x, y, texture.width, texture.height, 0, 0,
				texture.width, texture.height, color, 0, null, dir);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param x
	 * @param y
	 * @param color
	 * @param rotation
	 */
	public final void drawTexture(LTexture texture, float x, float y,
			GLColor color, float rotation) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, x, y, texture.width, texture.height, 0, 0,
				texture.width, texture.height, color, rotation, null, null);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param x
	 * @param y
	 * @param color
	 * @param dir
	 */
	public final void drawTexture(LTexture texture, float x, float y,
			GLColor color, Direction dir) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, x, y, texture.width, texture.height, 0, 0,
				texture.width, texture.height, color, 0, null, dir);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param x
	 * @param y
	 * @param color
	 * @param rotation
	 * @param origin
	 * @param dir
	 */
	public final void drawTexture(LTexture texture, float x, float y,
			GLColor color, float rotation, Vector2f origin, Direction dir) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, x, y, texture.width, texture.height, 0, 0,
				texture.width, texture.height, color, rotation, origin, dir);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param x
	 * @param y
	 * @param color
	 * @param rotation
	 * @param origin
	 * @param scale
	 * @param dir
	 */
	public final void drawTexture(LTexture texture, float x, float y,
			GLColor color, float rotation, Vector2f origin, float scale,
			Direction dir) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, x, y, texture.width * scale, texture.height
				* scale, 0, 0, texture.width, texture.height, color, rotation,
				origin, dir);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public final void drawTexture(LTexture texture, float x, float y,
			float width, float height) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, x, y, width, height, 0, 0, texture.width,
				texture.height, color, 0, null, null);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param color
	 */
	public final void drawTexture(LTexture texture, float x, float y,
			float width, float height, GLColor color) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, x, y, width, height, 0, 0, texture.width,
				texture.height, color, 0, null, null);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param rotation
	 */
	public final void drawTexture(LTexture texture, float x, float y,
			float width, float height, float rotation) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, x, y, width, height, 0, 0, texture.width,
				texture.height, color, rotation, null, null);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param dir
	 */
	public final void drawTexture(LTexture texture, float x, float y,
			float width, float height, Direction dir) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, x, y, width, height, 0, 0, texture.width,
				texture.height, color, 0, null, dir);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param color
	 * @param rotation
	 */
	public final void drawTexture(LTexture texture, float x, float y,
			float width, float height, GLColor color, float rotation) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, x, y, width, height, 0, 0, texture.width,
				texture.height, color, 0, null, null);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param color
	 * @param dir
	 */
	public final void drawTexture(LTexture texture, float x, float y,
			float width, float height, GLColor color, Direction dir) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, x, y, width, height, 0, 0, texture.width,
				texture.height, color, 0, null, dir);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param color
	 * @param rotation
	 * @param origin
	 * @param dir
	 */
	public final void drawTexture(LTexture texture, float x, float y,
			float width, float height, GLColor color, float rotation,
			Vector2f origin, Direction dir) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, x, y, width, height, 0, 0, texture.width,
				texture.height, color, rotation, origin, dir);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param dx1
	 * @param dy1
	 * @param dx2
	 * @param dy2
	 * @param sx1
	 * @param sy1
	 * @param sx2
	 * @param sy2
	 */
	public final void drawTexture(LTexture texture, float dx1, float dy1,
			float dx2, float dy2, float sx1, float sy1, float sx2, float sy2) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, color, 0,
				null, null);
	}

	/**
	 * 按照Java中近似作用函数切分纹理
	 * 
	 * @param texture
	 * @param dx1
	 * @param dy1
	 * @param dx2
	 * @param dy2
	 * @param sx1
	 * @param sy1
	 * @param sx2
	 * @param sy2
	 */
	public final void drawJavaTexture(LTexture texture, float dx1, float dy1,
			float dx2, float dy2, float sx1, float sy1, float sx2, float sy2) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, dx1, dy1, dx2 - dx1, dy2 - dy1, sx1, sy1, sx2,
				sy2, color, 0, null, null);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param dx1
	 * @param dy1
	 * @param dx2
	 * @param dy2
	 * @param sx1
	 * @param sy1
	 * @param sx2
	 * @param sy2
	 * @param color
	 */
	public final void drawTexture(LTexture texture, float dx1, float dy1,
			float dx2, float dy2, float sx1, float sy1, float sx2, float sy2,
			GLColor color) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, color, 0,
				null, null);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param dx1
	 * @param dy1
	 * @param dx2
	 * @param dy2
	 * @param sx1
	 * @param sy1
	 * @param sx2
	 * @param sy2
	 * @param rotation
	 * @param color
	 */
	public final void drawTexture(LTexture texture, float dx1, float dy1,
			float dx2, float dy2, float sx1, float sy1, float sx2, float sy2,
			float rotation, GLColor color) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, color,
				rotation, null, null);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param image
	 * @param x_src
	 * @param y_src
	 * @param width
	 * @param height
	 * @param transform
	 * @param x_dst
	 * @param y_dst
	 * @param anchor
	 */
	public void drawRegion(LImage image, int x_src, int y_src, int width,
			int height, int transform, int x_dst, int y_dst, int anchor) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawRegion(image.getTexture(), x_src, y_src, width, height, transform,
				x_dst, y_dst, anchor, color);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param image
	 * @param x_src
	 * @param y_src
	 * @param width
	 * @param height
	 * @param transform
	 * @param x_dst
	 * @param y_dst
	 * @param anchor
	 * @param c
	 */
	public void drawRegion(LImage image, int x_src, int y_src, int width,
			int height, int transform, int x_dst, int y_dst, int anchor,
			GLColor c) {
		if (isClose || image == null || image.isClose()) {
			return;
		}
		drawRegion(image.getTexture(), x_src, y_src, width, height, transform,
				x_dst, y_dst, anchor, c);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param x_src
	 * @param y_src
	 * @param width
	 * @param height
	 * @param transform
	 * @param x_dst
	 * @param y_dst
	 * @param anchor
	 */
	public void drawRegion(LTexture texture, int x_src, int y_src, int width,
			int height, int transform, int x_dst, int y_dst, int anchor) {
		drawRegion(texture, x_src, y_src, width, height, transform, x_dst,
				y_dst, anchor, color);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param x_src
	 * @param y_src
	 * @param width
	 * @param height
	 * @param transform
	 * @param x_dst
	 * @param y_dst
	 * @param anchor
	 */
	public void drawJavaRegion(LTexture texture, int x_src, int y_src,
			int width, int height, int transform, int x_dst, int y_dst,
			int anchor) {
		drawRegion(texture, x_src, y_src, width - x_src, height - y_src,
				transform, x_dst, y_dst, anchor, color);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param x_src
	 * @param y_src
	 * @param width
	 * @param height
	 * @param transform
	 * @param x_dst
	 * @param y_dst
	 * @param anchor
	 */
	public void drawJavaRegion(LImage texture, int x_src, int y_src, int width,
			int height, int transform, int x_dst, int y_dst, int anchor) {
		drawRegion(texture, x_src, y_src, width - x_src, height - y_src,
				transform, x_dst, y_dst, anchor, color);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param x_src
	 * @param y_src
	 * @param width
	 * @param height
	 * @param transform
	 * @param x_dst
	 * @param y_dst
	 * @param anchor
	 * @param c
	 */
	public void drawRegion(LTexture texture, int x_src, int y_src, int width,
			int height, int transform, int x_dst, int y_dst, int anchor,
			GLColor c) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		if (x_src + width > texture.getWidth()
				|| y_src + height > texture.getHeight() || width < 0
				|| height < 0 || x_src < 0 || y_src < 0) {
			throw new IllegalArgumentException("Area out of texture");
		}
		int dW = width, dH = height;

		float rotate = 0;
		Direction dir = Direction.TRANS_NONE;

		switch (transform) {
		case TRANS_NONE: {
			break;
		}
		case TRANS_ROT90: {
			rotate = 90;
			dW = height;
			dH = width;
			break;
		}
		case TRANS_ROT180: {
			rotate = 180;
			break;
		}
		case TRANS_ROT270: {
			rotate = 270;
			dW = height;
			dH = width;
			break;
		}
		case TRANS_MIRROR: {
			dir = Direction.TRANS_MIRROR;
			break;
		}
		case TRANS_MIRROR_ROT90: {
			dir = Direction.TRANS_MIRROR;
			rotate = -90;
			dW = height;
			dH = width;
			break;
		}
		case TRANS_MIRROR_ROT180: {
			dir = Direction.TRANS_MIRROR;
			rotate = -180;
			break;
		}
		case TRANS_MIRROR_ROT270: {
			dir = Direction.TRANS_MIRROR;
			rotate = -270;
			dW = height;
			dH = width;
			break;
		}
		default:
			throw new IllegalArgumentException("Bad transform");
		}

		boolean badAnchor = false;

		if (anchor == 0) {
			anchor = LGraphics.TOP | LGraphics.LEFT;
		}

		if ((anchor & 0x7f) != anchor || (anchor & LGraphics.BASELINE) != 0) {
			badAnchor = true;
		}

		if ((anchor & LGraphics.TOP) != 0) {
			if ((anchor & (LGraphics.VCENTER | LGraphics.BOTTOM)) != 0) {
				badAnchor = true;
			}
		} else if ((anchor & LGraphics.BOTTOM) != 0) {
			if ((anchor & LGraphics.VCENTER) != 0) {
				badAnchor = true;
			} else {
				y_dst -= dH - 1;
			}
		} else if ((anchor & LGraphics.VCENTER) != 0) {
			y_dst -= (dH - 1) >>> 1;
		} else {
			badAnchor = true;
		}

		if ((anchor & LGraphics.LEFT) != 0) {
			if ((anchor & (LGraphics.HCENTER | LGraphics.RIGHT)) != 0) {
				badAnchor = true;
			}
		} else if ((anchor & LGraphics.RIGHT) != 0) {
			if ((anchor & LGraphics.HCENTER) != 0) {
				badAnchor = true;
			} else {
				x_dst -= dW - 1;
			}
		} else if ((anchor & LGraphics.HCENTER) != 0) {
			x_dst -= (dW - 1) >>> 1;
		} else {
			badAnchor = true;
		}
		if (badAnchor) {
			throw new IllegalArgumentException("Bad Anchor");
		}

		drawTexture(texture, x_dst, y_dst, width, height, x_src, y_src, x_src
				+ width, y_src + height, c, rotate, null, dir);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param destRect
	 */
	public final void drawTexture(LTexture texture, RectBox destRect) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, destRect.x, destRect.y, destRect.width,
				destRect.height, 0, 0, texture.width, texture.height, color, 0,
				null, null);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param destRect
	 * @param color
	 */
	public final void drawTexture(LTexture texture, RectBox destRect,
			GLColor color) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, destRect.x, destRect.y, destRect.width,
				destRect.height, 0, 0, texture.width, texture.height, color, 0,
				null, null);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param destRect
	 * @param rotation
	 */
	public final void drawTexture(LTexture texture, RectBox destRect,
			float rotation) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, destRect.x, destRect.y, destRect.width,
				destRect.height, 0, 0, texture.width, texture.height, color,
				rotation, null, null);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param destRect
	 * @param dir
	 */
	public final void drawTexture(LTexture texture, RectBox destRect,
			Direction dir) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, destRect.x, destRect.y, destRect.width,
				destRect.height, 0, 0, texture.width, texture.height, color, 0,
				null, dir);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param destRect
	 * @param color
	 * @param rotation
	 */
	public final void drawTexture(LTexture texture, RectBox destRect,
			GLColor color, float rotation) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, destRect.x, destRect.y, destRect.width,
				destRect.height, 0, 0, texture.width, texture.height, color,
				rotation, null, null);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param destRect
	 * @param color
	 * @param dir
	 */
	public final void drawTexture(LTexture texture, RectBox destRect,
			GLColor color, Direction dir) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, destRect.x, destRect.y, destRect.width,
				destRect.height, 0, 0, texture.width, texture.height, color, 0,
				null, dir);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param destRect
	 * @param color
	 * @param rotation
	 * @param origin
	 * @param dir
	 */
	public final void drawTexture(LTexture texture, RectBox destRect,
			GLColor color, float rotation, Vector2f origin, Direction dir) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, destRect.x, destRect.y, destRect.width,
				destRect.height, 0, 0, texture.width, texture.height, color,
				rotation, origin, dir);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param destRect
	 * @param srcRect
	 */
	public final void drawTexture(LTexture texture, RectBox destRect,
			RectBox srcRect) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, destRect.x, destRect.y, destRect.width,
				destRect.height, srcRect.x, srcRect.y, srcRect.width,
				srcRect.height, color, 0, null, null);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param destRect
	 * @param srcRect
	 * @param color
	 */
	public final void drawTexture(LTexture texture, RectBox destRect,
			RectBox srcRect, GLColor color) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, destRect.x, destRect.y, destRect.width,
				destRect.height, srcRect.x, srcRect.y, srcRect.width,
				srcRect.height, color, 0, null, null);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param destRect
	 * @param srcRect
	 * @param rotation
	 */
	public final void drawTexture(LTexture texture, RectBox destRect,
			RectBox srcRect, float rotation) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, destRect.x, destRect.y, destRect.width,
				destRect.height, srcRect.x, srcRect.y, srcRect.width,
				srcRect.height, color, rotation, null, null);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param destRect
	 * @param srcRect
	 * @param dir
	 */
	public final void drawTexture(LTexture texture, RectBox destRect,
			RectBox srcRect, Direction dir) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, destRect.x, destRect.y, destRect.width,
				destRect.height, srcRect.x, srcRect.y, srcRect.width,
				srcRect.height, color, 0, null, dir);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param destRect
	 * @param srcRect
	 * @param color
	 * @param rotation
	 */
	public final void drawTexture(LTexture texture, RectBox destRect,
			RectBox srcRect, GLColor color, float rotation) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, destRect.x, destRect.y, destRect.width,
				destRect.height, srcRect.x, srcRect.y, srcRect.width,
				srcRect.height, color, rotation, null, null);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param destRect
	 * @param srcRect
	 * @param color
	 * @param dir
	 */
	public final void drawTexture(LTexture texture, RectBox destRect,
			RectBox srcRect, GLColor color, Direction dir) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, destRect.x, destRect.y, destRect.width,
				destRect.height, srcRect.x, srcRect.y, srcRect.width,
				srcRect.height, color, 0, null, dir);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param destRect
	 * @param srcRect
	 * @param color
	 * @param rotation
	 * @param origin
	 * @param dir
	 */
	public final void drawTexture(LTexture texture, RectBox destRect,
			RectBox srcRect, GLColor color, float rotation, Vector2f origin,
			Direction dir) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, destRect.x, destRect.y, destRect.width,
				destRect.height, srcRect.x, srcRect.y, srcRect.width,
				srcRect.height, color, rotation, origin, dir);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param position
	 */
	public final void drawTexture(LTexture texture, Vector2f position) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, position.x, position.y, texture.width,
				texture.height, 0, 0, texture.width, texture.height, color, 0,
				null, null);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param position
	 * @param color
	 */
	public final void drawTexture(LTexture texture, Vector2f position,
			GLColor color) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, position.x, position.y, texture.width,
				texture.height, 0, 0, texture.width, texture.height, color, 0,
				null, null);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param position
	 * @param rotation
	 */
	public final void drawTexture(LTexture texture, Vector2f position,
			float rotation) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, position.x, position.y, texture.width,
				texture.height, 0, 0, texture.width, texture.height, color,
				rotation, null, null);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param position
	 * @param dir
	 */
	public final void drawTexture(LTexture texture, Vector2f position,
			Direction dir) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, position.x, position.y, texture.width,
				texture.height, 0, 0, texture.width, texture.height, color, 0,
				null, dir);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param position
	 * @param color
	 * @param rotation
	 */
	public final void drawTexture(LTexture texture, Vector2f position,
			GLColor color, float rotation) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, position.x, position.y, texture.width,
				texture.height, 0, 0, texture.width, texture.height, color,
				rotation, null, null);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param position
	 * @param color
	 * @param dir
	 */
	public final void drawTexture(LTexture texture, Vector2f position,
			GLColor color, Direction dir) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, position.x, position.y, texture.width,
				texture.height, 0, 0, texture.width, texture.height, color, 0,
				null, dir);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param position
	 * @param color
	 * @param rotation
	 * @param origin
	 * @param dir
	 */
	public final void drawTexture(LTexture texture, Vector2f position,
			GLColor color, float rotation, Vector2f origin, Direction dir) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, position.x, position.y, texture.width,
				texture.height, 0, 0, texture.width, texture.height, color,
				rotation, origin, dir);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param position
	 * @param color
	 * @param rotation
	 * @param origin
	 * @param scale
	 * @param dir
	 */
	public final void drawTexture(LTexture texture, Vector2f position,
			GLColor color, float rotation, Vector2f origin, float scale,
			Direction dir) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, position.x, position.y, texture.width * scale,
				texture.height * scale, 0, 0, texture.width, texture.height,
				color, rotation, origin, dir);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param position
	 * @param srcRect
	 */
	public final void drawTexture(LTexture texture, Vector2f position,
			RectBox srcRect) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, position.x, position.y, texture.width,
				texture.height, srcRect.x, srcRect.y, srcRect.width,
				srcRect.height, color, 0, null, null);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param position
	 * @param srcRect
	 * @param color
	 */
	public final void drawTexture(LTexture texture, Vector2f position,
			RectBox srcRect, GLColor color) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, position.x, position.y, texture.width,
				texture.height, srcRect.x, srcRect.y, srcRect.width,
				srcRect.height, color, 0, null, null);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param position
	 * @param srcRect
	 * @param rotation
	 */
	public final void drawTexture(LTexture texture, Vector2f position,
			RectBox srcRect, float rotation) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, position.x, position.y, texture.width,
				texture.height, srcRect.x, srcRect.y, srcRect.width,
				srcRect.height, color, rotation, null, null);
	}

	/**
	 * 
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param position
	 * @param srcRect
	 * @param dir
	 */
	public final void drawTexture(LTexture texture, Vector2f position,
			RectBox srcRect, Direction dir) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, position.x, position.y, texture.width,
				texture.height, srcRect.x, srcRect.y, srcRect.width,
				srcRect.height, color, 0, null, dir);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param position
	 * @param srcRect
	 * @param color
	 * @param rotation
	 */
	public final void drawTexture(LTexture texture, Vector2f position,
			RectBox srcRect, GLColor color, float rotation) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, position.x, position.y, texture.width,
				texture.height, srcRect.x, srcRect.y, srcRect.width,
				srcRect.height, color, rotation, null, null);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param position
	 * @param srcRect
	 * @param color
	 * @param dir
	 */
	public final void drawTexture(LTexture texture, Vector2f position,
			RectBox srcRect, GLColor color, Direction dir) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, position.x, position.y, texture.width,
				texture.height, srcRect.x, srcRect.y, srcRect.width,
				srcRect.height, color, 0, null, dir);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param position
	 * @param srcRect
	 * @param color
	 * @param rotation
	 * @param origin
	 * @param dir
	 */
	public final void drawTexture(LTexture texture, Vector2f position,
			RectBox srcRect, GLColor color, float rotation, Vector2f origin,
			Direction dir) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, position.x, position.y, texture.width,
				texture.height, srcRect.x, srcRect.y, srcRect.width,
				srcRect.height, color, rotation, origin, dir);
	}

	/**
	 * 渲染纹理为指定状态
	 * 
	 * @param texture
	 * @param position
	 * @param srcRect
	 * @param color
	 * @param rotation
	 * @param origin
	 * @param scale
	 * @param dir
	 */
	public final void drawTexture(LTexture texture, Vector2f position,
			RectBox srcRect, GLColor color, float rotation, Vector2f origin,
			float scale, Direction dir) {
		if (isClose || texture == null || texture.isClose) {
			return;
		}
		drawTexture(texture, position.x, position.y, texture.width * scale,
				texture.height * scale, srcRect.x, srcRect.y, srcRect.width,
				srcRect.height, color, rotation, origin, dir);
	}

	/**
	 * 渲染纹理为指定设置
	 * 
	 * @param texture
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param srcX
	 * @param srcY
	 * @param srcWidth
	 * @param srcHeight
	 * @param color
	 * @param rotation
	 * @param origin
	 * @param dir
	 */
	private final void drawTexture(LTexture texture, float x, float y,
			float width, float height, float srcX, float srcY, float srcWidth,
			float srcHeight, GLColor c, float rotation, Vector2f origin,
			Direction dir) {

		if (!texture.isLoaded) {
			texture.loadTexture();
		}

		if (checkAlpha(c)) {
			return;
		}

		glTex2DEnable();
		{

			bind(texture.textureID);

			if (!texture.isStatic) {
				if (c != null && boundColor != c) {
					if (!onTexEnvf && !onReplace && texture.replace) {
						glTexEnvfReplaceColor(c.r, c.g, c.b, c.a);
						onTexEnvf = true;
					}
					gl10.glColor4f(c.r, c.g, c.b, c.a);
					boundColor = c;
				}
			}

			if (onSaveFlag = checkSave(texture, x, y, width, height, rotation,
					dir)) {
				gl10.glPushMatrix();
			}
			{
				if (x != 0 || y != 0) {
					gl10.glTranslatef(x, y, 0);
				}
				if (rotation != 0) {
					float centerX = width / 2;
					float centerY = height / 2;
					if (origin != null) {
						centerX = origin.x;
						centerY = origin.y;
					}
					gl10.glTranslatef(centerX, centerY, 0.0f);
					gl10.glRotatef(rotation, 0.0f, 0.0f, 1.0f);
					gl10.glTranslatef(-centerX, -centerY, 0.0f);
				}
				if (width != texture.width || height != texture.height) {
					float sx = width / texture.width;
					float sy = height / texture.height;
					try {
						gl10.glScalef(sx, sy, 1);
					} catch (Exception e) {
						gl10.glScalef(sx, sy, 0);
					}
				}
				if (dir != null || dir != Direction.TRANS_NONE) {
					float sx = 1, tranX = 0;
					float sy = 1, tranY = 0;
					if (dir == Direction.TRANS_MIRROR) {
						sx = -1;
						tranX = width;
					} else if (dir == Direction.TRANS_FILP) {
						sy = -1;
						tranY = height;
					} else if (dir == Direction.TRANS_MF) {
						sx = sy = -1;
						tranX = width;
						tranY = height;
					}
					gl10.glTranslatef(tranX, tranY, 0);
					try {
						gl10.glScalef(sx, sy, 1);
					} catch (Exception e) {
						gl10.glScalef(sx, sy, 0);
					}
				}

				if (texture.string) {
					beginBlend(GL.MODE_SPEED);
				} else if (texture.format == Format.STATIC) {
					beginBlend(GL.MODE_NONE);
				} else if (texture.format != Format.DEFAULT) {
					boolean noAlpha = !onReplace && lastAlpha > 0.95f
							&& c != null && c.a > 0.95f;
					if ((texture.hasAlpha && noAlpha) || texture.string) {
						switch (texture.format) {
						case SPEED:
						case FONT:
							beginBlend(GL.MODE_SPEED);
							break;
						}
					} else if (!texture.hasAlpha && noAlpha
							&& (texture.width >= 32 && texture.height >= 32)) {
						beginBlend(GL.MODE_NONE);
					}
				}

				glTex2DARRAYEnable();
				{

					if (GLEx.vboOn) {
						gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER,
								texture.bufferID);
						gl11.glVertexPointer(2, GL10.GL_FLOAT, 0, 0);
						if (srcX != 0 || srcY != 0 || srcWidth != texture.width
								|| srcHeight != texture.height) {
							gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER,
									glDataBufferID);

							putRectangle(texture, srcX, srcY, srcWidth,
									srcHeight);

							rectData.position(8);
							gl11.glBufferSubData(GL11.GL_ARRAY_BUFFER,
									texture.vertexSize, texture.texSize,
									rectData);
							gl11.glTexCoordPointer(2, GL11.GL_FLOAT, 0,
									texture.texSize);
						} else {
							gl11.glTexCoordPointer(2, GL11.GL_FLOAT, 0,
									texture.texSize);
						}
						gl11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
						gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
					} else {
						texture.data.position(0);
						GLUtils.vertexPointer(gl10, 2, texture.data);
						if (srcX != 0 || srcY != 0 || srcWidth != texture.width
								|| srcHeight != texture.height) {
							putRectangle(texture, srcX, srcY, srcWidth,
									srcHeight);
							rectData.position(8);
							gl10.glTexCoordPointer(2, GL10.GL_FLOAT, 0,
									rectData);
						} else {
							texture.data.position(8);
							gl10.glTexCoordPointer(2, GL10.GL_FLOAT, 0,
									texture.data);
						}
						gl10.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
					}

				}
				glTex2DARRAYDisable();

				if (prevBlendMode != currentBlendMode) {
					endBlend();
				}

			}
			if (onSaveFlag) {
				gl10.glPopMatrix();
			}

			if (!texture.isStatic) {
				if (onTexEnvf && !onReplace && texture.isReplace()) {
					glTexEnvfModulateColor(1, 1, 1, 1);
					onTexEnvf = false;
				}
				if (c != null && !c.equals(color)) {
					gl10.glColor4f(color.r, color.g, color.b, color.a);
				}
			}

		}
	}

	/**
	 * 旋转纹理
	 * 
	 * @param centerX
	 * @param centerY
	 * @param rotation
	 */
	public final void setRotation(float centerX, float centerY, float rotation) {
		if (isClose) {
			return;
		}
		gl10.glTranslatef(centerX, centerY, 0.0f);
		gl10.glRotatef(rotation, 0.0f, 0.0f, 1.0f);
		gl10.glTranslatef(-centerX, -centerY, 0.0f);
	}

	/**
	 * 将指定纹理文件作为矩形区域注入画布
	 * 
	 * @param texture
	 * @param srcX
	 * @param srcY
	 * @param srcWidth
	 * @param srcHeight
	 */
	private final static void putRectangle(LTexture texture, float srcX,
			float srcY, float srcWidth, float srcHeight) {
		float xOff = (((float) srcX / texture.width) * texture.widthRatio)
				+ texture.xOff;
		float yOff = (((float) srcY / texture.height) * texture.heightRatio)
				+ texture.yOff;
		float widthRatio = (((float) srcWidth / texture.width) * texture.widthRatio);
		float heightRatio = (((float) srcHeight / texture.height) * texture.heightRatio);
		rectDataCords[8] = xOff;
		rectDataCords[9] = yOff;
		rectDataCords[10] = widthRatio;
		rectDataCords[11] = yOff;
		rectDataCords[12] = xOff;
		rectDataCords[13] = heightRatio;
		rectDataCords[14] = widthRatio;
		rectDataCords[15] = heightRatio;
		rectData.position(8);
		rectData.put(rectDataCords, 8, 8);
	}

	/**
	 * 清空画布为指定色彩
	 * 
	 * @param r
	 * @param g
	 * @param b
	 */
	public void clear(float r, float g, float b) {
		GLUtils.setClearColor(gl10, r, g, b, 1f);
	}

	/**
	 * 调整矩阵布局
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param scaleX
	 * @param scaleY
	 * @param rotation
	 */
	public void setMatrix(float x, float y, float z, float scaleX,
			float scaleY, float rotation) {
		if (isClose) {
			return;
		}
		save();
		glTex2DDisable();
		{
			gl10.glTranslatef(x, y, z);
			gl10.glScalef(scaleX, scaleY, 1f);
			gl10.glRotatef(rotation, 0, 0, 1);
		}
	}

	/**
	 * 输出字符串
	 * 
	 * @param string
	 * @param position
	 */
	public final void drawString(String string, Vector2f position) {
		drawString(string, position.x, position.y, color);
	}

	/**
	 * 输出字符串
	 * 
	 * @param string
	 * @param position
	 * @param color
	 */
	public final void drawString(String string, Vector2f position, GLColor color) {
		drawString(string, position.x, position.y, color);
	}

	/**
	 * 输出字符串
	 * 
	 * @param string
	 * @param x
	 * @param y
	 */
	public final void drawString(String string, float x, float y) {
		drawString(string, x, y, color);
	}

	/**
	 * 输出字符串
	 * 
	 * @param string
	 * @param x
	 * @param y
	 * @param color
	 */
	public final void drawString(String string, float x, float y, GLColor color) {
		if (isClose) {
			return;
		}
		drawString(string, x, y, 0, color);
	}

	/**
	 * 输出字符串
	 * 
	 * @param string
	 * @param x
	 * @param y
	 * @param rotation
	 */
	public final void drawString(String string, float x, float y, float rotation) {
		if (isClose) {
			return;
		}
		drawString(string, x, y, rotation, color);
	}

	/**
	 * 输出字符串
	 * 
	 * @param string
	 * @param x
	 * @param y
	 * @param rotation
	 * @param c
	 */
	public void drawString(String string, float x, float y, float rotation,
			GLColor c) {
		drawString(string, x, y, rotation, c, true);
	}

	/**
	 * 输出字符串
	 * 
	 * @param string
	 * @param x
	 * @param y
	 * @param rotation
	 * @param c
	 * @param check
	 */
	private void drawString(String string, float x, float y, float rotation,
			GLColor c, boolean check) {
		drawString(string, x, y, rotation, c, check, false);
	}

	/**
	 * 输出字符串
	 * 
	 * @param string
	 * @param x
	 * @param y
	 * @param rotation
	 * @param c
	 * @param check
	 */
	private void drawString(String string, float x, float y, float rotation,
			GLColor c, boolean check, boolean notEnglish) {
		if (isClose || c == null || checkAlpha(c)) {
			return;
		}
		if (string == null || string.length() == 0) {
			return;
		}

		y = y - font.getAscent();

		if (notEnglish || LSystem.isStringTexture) {
			LSTRTexture.drawString(this, string, x, y, rotation, c);
			return;
		}

		if (check) {
			if (!StringUtils.isWestern(string)) {
				LSTRTexture.drawString(this, string, x, y, rotation, c);
				return;
			}
		}

		glTex2DEnable();
		{
			LSTRTexture english = LSTRTexture.createStringTexture(font);

			char[] chars = string.toCharArray();

			int size = chars.length;

			bind(english.textureID);

			if (boundColor != c) {
				if (!onTexEnvf && !onReplace && english.isReplace()) {
					glTexEnvfReplaceColor(c.r, c.g, c.b, c.a);
					onTexEnvf = true;
				}
				gl10.glColor4f(c.r, c.g, c.b, c.a);
				boundColor = c;
			}

			if (onSaveFlag = x != 0 || y != 0 || rotation != 0) {
				gl10.glPushMatrix();
			}
			{
				if (x != 0 || y != 0) {
					gl10.glTranslatef(x, y, 0);
				}
				if (rotation != 0) {
					float centerX = english.getWidth() / 2;
					float centerY = english.getHeight() / 2;
					gl10.glTranslatef(centerX, centerY, 0.0f);
					gl10.glRotatef(rotation, 0.0f, 0.0f, 1.0f);
					gl10.glTranslatef(-centerX, -centerY, 0.0f);
				}
				glTex2DARRAYEnable();
				{
					if (vboOn) {
						gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER,
								english.vertBufID);
						gl11.glVertexPointer(2, GL10.GL_FLOAT, 0, 0);
						gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER,
								english.texBufID);
						for (int i = 0; i < size; i++) {
							int tranX = 0;
							if (chars[i] == '\n') {
								gl11.glTranslatef(tranX, english.height, 0);
								continue;
							}
							gl11.glTexCoordPointer(2, GL10.GL_FLOAT, 8,
									chars[i] * 32);
							gl11.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
							gl11.glTranslatef(english.space, 0, 0);
						}
						gl11.glBindBuffer(GL11.GL_ARRAY_BUFFER, 0);
					} else {
						GLUtils.vertexPointer(gl10, 2, english.vertData);
						for (int i = 0; i < size; i++) {
							int tranX = 0;
							if (chars[i] == '\n') {
								tranX = -(english.space * i);
								gl10.glTranslatef(tranX, english.height, 0);
								continue;
							}
							english.texData.position(chars[i] * 8);
							gl10.glTexCoordPointer(2, GL10.GL_FLOAT, 0,
									english.texData);
							gl10.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
							gl10.glTranslatef(english.space, 0, 0);
						}
					}
				}
				glTex2DARRAYDisable();
			}
			if (onSaveFlag) {
				gl10.glPopMatrix();
			}
			if (onTexEnvf && !onReplace && english.isReplace()) {
				glTexEnvfModulateColor(1, 1, 1, 1);
				onTexEnvf = false;
				if (c != null && !c.equals(color)) {
					gl10.glColor4f(color.r, color.g, color.b, color.a);
				}
			}
		}
	}

	/**
	 * 仅输出东方字符
	 * 
	 * @param string
	 * @param x
	 * @param y
	 */
	public void drawEastString(String string, float x, float y) {
		drawString(string, x, y, 0, color, false, true);
	}

	/**
	 * 仅输出东方字符
	 * 
	 * @param string
	 * @param x
	 * @param y
	 * @param rotation
	 * @param c
	 */
	public void drawEastString(String string, float x, float y, float rotation,
			GLColor c) {
		drawString(string, x, y, rotation, c, false, true);
	}

	/**
	 * 仅输出西方字符串
	 * 
	 * @param string
	 * @param x
	 * @param y
	 */
	public void drawWestString(String string, float x, float y) {
		drawString(string, x, y, 0, color, false);
	}

	/**
	 * 仅输出西方字符串
	 * 
	 * @param chars
	 * @param x
	 * @param y
	 */
	public void drawWestString(char chars, float x, float y) {
		drawString(String.valueOf(chars), x, y, 0, color, false);
	}

	/**
	 * 仅输出西方字符串
	 * 
	 * @param chars
	 * @param x
	 * @param y
	 * @param c
	 */
	public void drawWestString(String string, float x, float y, GLColor c) {
		drawString(string, x, y, 0, c, false);
	}

	/**
	 * 仅输出西方字符串
	 * 
	 * @param chars
	 * @param x
	 * @param y
	 * @param rotation
	 */
	public void drawWestString(char chars, float x, float y, float rotation) {
		drawString(String.valueOf(chars), x, y, rotation, color, false);
	}

	/**
	 * 仅输出西方字符串
	 * 
	 * @param chars
	 * @param x
	 * @param y
	 * @param rotation
	 * @param c
	 */
	public void drawWestString(char chars, float x, float y, float rotation,
			GLColor c) {
		drawString(String.valueOf(chars), x, y, rotation, c, false);
	}

	/**
	 * 输出字符
	 * 
	 * @param chars
	 * @param x
	 * @param y
	 */
	public void drawChar(char chars, float x, float y) {
		drawChar(chars, x, y, 0);
	}

	/**
	 * 输出字符
	 * 
	 * @param chars
	 * @param x
	 * @param y
	 * @param rotation
	 */
	public void drawChar(char chars, float x, float y, float rotation) {
		drawChar(chars, x, y, rotation, color);
	}

	/**
	 * 输出字符
	 * 
	 * @param chars
	 * @param x
	 * @param y
	 * @param rotation
	 * @param c
	 */
	public void drawChar(char chars, float x, float y, float rotation, GLColor c) {
		drawString(String.valueOf(chars), x, y, rotation, c);
	}

	/**
	 * 输出字符
	 * 
	 * @param message
	 * @param offset
	 * @param length
	 * @param x
	 * @param y
	 */
	public void drawBytes(byte[] message, int offset, int length, int x, int y) {
		if (isClose) {
			return;
		}
		drawString(new String(message, offset, length), x, y);
	}

	/**
	 * 输出字符
	 * 
	 * @param message
	 * @param offset
	 * @param length
	 * @param x
	 * @param y
	 */
	public void drawChars(char[] message, int offset, int length, int x, int y) {
		if (isClose) {
			return;
		}
		drawString(new String(message, offset, length), x, y);
	}

	/**
	 * 输出字符
	 * 
	 * @param message
	 * @param x
	 * @param y
	 * @param anchor
	 */
	public void drawString(String message, int x, int y, int anchor) {
		if (isClose) {
			return;
		}
		int newx = x;
		int newy = y;
		if (anchor == 0) {
			anchor = LGraphics.TOP | LGraphics.LEFT;
		}
		if ((anchor & LGraphics.TOP) != 0) {
			newy -= font.getAscent();
		} else if ((anchor & LGraphics.BOTTOM) != 0) {
			newy -= font.getAscent();
		}
		if ((anchor & LGraphics.HCENTER) != 0) {
			newx -= font.stringWidth(message) / 2;
		} else if ((anchor & LGraphics.RIGHT) != 0) {
			newx -= font.stringWidth(message);
		}
		drawString(message, newx, newy);
	}

	public void drawStyleString(String message, int x, int y, int color,
			int color1) {
		if (isClose) {
			return;
		}
		setColor(color);
		drawEastString(message, x + 1, y);
		drawEastString(message, x - 1, y);
		drawEastString(message, x, y + 1);
		drawEastString(message, x, y - 1);
		setColor(color1);
		drawEastString(message, x, y);
	}

	public void drawStyleString(String message, int x, int y, GLColor c1,
			GLColor c2) {
		if (isClose) {
			return;
		}
		setColorRGB(c1);
		drawEastString(message, x + 1, y);
		drawEastString(message, x - 1, y);
		drawEastString(message, x, y + 1);
		drawEastString(message, x, y - 1);
		setColorRGB(c2);
		drawEastString(message, x, y);
	}

	/**
	 * 绑定指定纹理ID
	 * 
	 * @param id
	 */
	public void bind(int id) {
		if (lazyTextureID != id) {
			gl10.glBindTexture(GL10.GL_TEXTURE_2D, id);
			lazyTextureID = id;
		}
	}

	public final int getBlendMode() {
		return this.currentBlendMode;
	}

	final void glTexEnvfModulateColor(float r, float g, float b, float a) {
		glTexEnvfModulateColor(r, g, b, a, true);
	}

	final void glTexEnvfModulateColor(float r, float g, float b, float a,
			boolean update) {
		try {
			gl10.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE,
					GL.GL_MODULATE);
			if (update) {
				setColor(r, g, b, a);
			}
			onReplace = false;
		} catch (Exception e) {
		}
	}

	final void glTexEnvfReplaceColor(float r, float g, float b, float a) {
		glTexEnvfReplaceColor(r, g, b, a, true);
	}

	final void glTexEnvfReplaceColor(float r, float g, float b, float a,
			boolean update) {
		try {
			gl10.glTexEnvf(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE,
					GL.GL_REPLACE);
			if (update) {
				setColor(r, g, b, a);
			}
			onReplace = true;
		} catch (Exception e) {
		}
	}

	final void glUpdateModulate(float r, float g, float b, float a) {
		glTexEnvfModulateColor(r, g, b, a, true);
	}

	final void glUpdateReplace(float r, float g, float b, float a) {
		glTexEnvfReplaceColor(r, g, b, a, true);
	}

	public void resetFont() {
		this.font = LFont.getDefaultFont();
		this.resetColor();
	}

	public void setFont(LFont font) {
		this.font = font;
	}

	public LFont getFont() {
		return this.font;
	}

	public final void set2DStateOn() {
		if (!preTex2dMode) {
			try {
				gl10.glDisable(GL10.GL_DEPTH_TEST);
			} catch (Exception e) {
			}
			try {
				gl10.glMatrixMode(GL10.GL_PROJECTION);
			} catch (Exception e) {
			}
			try {
				gl10.glPushMatrix();
			} catch (Exception e) {
			}
			try {
				gl10.glLoadIdentity();
			} catch (Exception e) {
			}
			try {
				gl10.glOrthof(0, viewPort.width, viewPort.height, 0, 1f, -1f);
			} catch (Exception e) {
			}
			try {
				gl10.glMatrixMode(GL10.GL_MODELVIEW);
			} catch (Exception e) {
			}
			try {
				gl10.glPushMatrix();
			} catch (Exception e) {
			}
			try {
				gl10.glLoadIdentity();
			} catch (Exception e) {
			}
			preTex2dMode = true;
		}
	}

	public final void set2DStateOff() {
		if (preTex2dMode) {
			try {
				gl10.glMatrixMode(GL.GL_PROJECTION);
			} catch (Exception e) {
			}
			try {
				gl10.glPopMatrix();
			} catch (Exception e) {
			}
			try {
				gl10.glMatrixMode(GL.GL_MODELVIEW);
			} catch (Exception e) {
			}
			try {
				gl10.glPopMatrix();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				gl10.glEnable(GL.GL_DEPTH_TEST);
			} catch (Exception e) {
				e.printStackTrace();
			}
			preTex2dMode = false;
		}
	}

	public void begin2DTex() {
		if (vboOn) {
			gl.glLoadIdentity();
			if (!gl11.glIsEnabled(GL.GL_TEXTURE_2D)) {
				gl11.glEnable(GL.GL_TEXTURE_2D);
				preTex2d = false;
			}
			if (gl11.glIsEnabled(GL.GL_LIGHTING)) {
				gl11.glDisable(GL.GL_LIGHTING);
				preLight = true;
			}
			if (gl11.glIsEnabled(GL.GL_CULL_FACE)) {
				gl11.glDisable(GL.GL_CULL_FACE);
				preCull = true;
			}
			set2DStateOn();
		}
	}

	public void end2DTex() {
		if (preTex2d) {
			gl11.glEnable(GL.GL_TEXTURE_2D);
		} else {
			gl11.glDisable(GL.GL_TEXTURE_2D);
		}
		if (preLight) {
			gl11.glEnable(GL.GL_LIGHTING);
		} else {
			gl11.glDisable(GL.GL_LIGHTING);
		}
		if (preCull) {
			gl11.glEnable(GL.GL_CULL_FACE);
		} else {
			gl11.glDisable(GL.GL_CULL_FACE);
		}
	}

	public void saveMatrices() {
		if (isClose) {
			return;
		}
		gl10.glMatrixMode(GL.GL_PROJECTION);
		gl10.glPushMatrix();
		gl10.glMatrixMode(GL.GL_MODELVIEW);
		gl10.glPushMatrix();
	}

	public void restoreMatrices() {
		if (isClose) {
			return;
		}
		gl10.glMatrixMode(GL.GL_PROJECTION);
		gl10.glPopMatrix();
		gl10.glMatrixMode(GL.GL_MODELVIEW);
		gl10.glPopMatrix();
	}

	public void setDefenableLighting() {
		if (isClose) {
			return;
		}
		float light_ambient[] = { 0.5f, 0.5f, 0.5f, 1.0f };
		float light_diffuse[] = { 1.0f, 1.0f, 1.0f, 1.0f };
		float light_specular[] = { 1.0f, 1.0f, 1.0f, 1.0f };
		float[] light0 = { -1.0f, -2.0f, 2.0f, 0.0f };
		float[] light1 = { 1.0f, 2.0f, -2.0f, 0.0f };
		enableLighting();
		gl10.glEnable(GL.GL_LIGHT0);
		gl10.glEnable(GL.GL_LIGHT1);
		gl10.glLightfv(GL.GL_LIGHT0, GL.GL_AMBIENT, light_ambient, 0);
		gl10.glLightfv(GL.GL_LIGHT0, GL.GL_DIFFUSE, light_diffuse, 0);
		gl10.glLightfv(GL.GL_LIGHT0, GL.GL_SPECULAR, light_specular, 0);
		gl10.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, light0, 0);
		gl10.glLightfv(GL.GL_LIGHT1, GL.GL_AMBIENT, light_ambient, 0);
		gl10.glLightfv(GL.GL_LIGHT1, GL.GL_DIFFUSE, light_diffuse, 0);
		gl10.glLightfv(GL.GL_LIGHT1, GL.GL_SPECULAR, light_specular, 0);
		gl10.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, light1, 0);
		gl10.glLightModelfv(GL.GL_LIGHT_MODEL_AMBIENT, light_ambient, 0);
	}

	private final boolean checkAlpha(GLColor c) {
		return lastAlpha < 0.1 || (c != null && c.a < 0.1);
	}

	private final boolean checkSave(LTexture texture, float x, float y,
			float width, float height, float rotation, Direction dir) {
		return x != 0 || y != 0 || width != texture.width
				|| height != texture.height || dir != Direction.TRANS_NONE;
	}

	public final int getDrawingType() {
		return drawingType;
	}

	public final void setDrawingType(int drawingType) {
		this.drawingType = drawingType;
	}

	public final float getTranslateX() {
		return translateX;
	}

	public final float getTranslateY() {
		return translateY;
	}

	public boolean isReplace() {
		return onReplace;
	}

	public final boolean isClose() {
		return isClose;
	}

	public final void dispose() {
		isClose = true;
		LSTRTexture.dispose();
	}

}
