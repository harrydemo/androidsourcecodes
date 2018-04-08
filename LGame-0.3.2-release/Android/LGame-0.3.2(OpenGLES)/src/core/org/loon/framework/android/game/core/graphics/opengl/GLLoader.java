package org.loon.framework.android.game.core.graphics.opengl;

import java.util.HashMap;

import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.graphics.LPixmap;
import org.loon.framework.android.game.core.graphics.device.LGraphics;
import org.loon.framework.android.game.utils.CollectionUtils;

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
public final class GLLoader extends LTextureData {

	private final static HashMap<String, LTextureData> lazyLoader = new HashMap<String, LTextureData>(
			10);

	public static LTextureData getTextureData(LImage img) {
		if (img == null) {
			throw new RuntimeException("Source image is null !");
		}
		try {
			return new GLLoader(img);
		} catch (Exception e) {
			LTextures.destroyAll();
			LImage.disposeAll();
			LSystem.gc();
		}
		return new GLLoader(img);
	}

	public static LTextureData getTextureData(LPixmap pix) {
		if (pix == null) {
			throw new RuntimeException("Source image is null !");
		}
		try {
			return new GLLoader(pix);
		} catch (Exception e) {
			LTextures.destroyAll();
			LImage.disposeAll();
			LSystem.gc();
		}
		return new GLLoader(pix);
	}

	public static LTextureData getTextureData(String fileName) {
		if (fileName == null) {
			throw new RuntimeException("Path is null !");
		}
		String key = fileName.trim().toLowerCase();
		LTextureData data = lazyLoader.get(key);
		if (data == null || data.source == null) {
			try {
				lazyLoader.put(key, data = new GLLoader(fileName));
			} catch (Exception e) {
				LTextures.destroyAll();
				LImage.disposeAll();
				LSystem.gc();
				lazyLoader.put(key, data = new GLLoader(fileName));
			}
		}
		return data;
	}

	private GLLoader(LTextureData data, boolean newCopy) {
		this.width = data.width;
		this.height = data.height;
		this.texWidth = data.texWidth;
		this.texHeight = data.texHeight;
		this.hasAlpha = data.hasAlpha;
		this.source = newCopy ? CollectionUtils.copyOf(data.source)
				: data.source;
		this.fileName = data.fileName;
	}

	private GLLoader(LImage image) {
		create(image);
	}

	private GLLoader(LPixmap pix) {
		create(pix);
	}

	private GLLoader(String fileName) {
		create(new LImage(fileName));
		this.fileName = fileName;
	}

	/**
	 * 将LImage转化为LTextureData
	 * 
	 * @param image
	 * @return
	 */
	private void create(LImage image) {
		if (image == null) {
			return;
		}
		int srcWidth = image.getWidth();
		int srcHeight = image.getHeight();

		this.hasAlpha = image.hasAlpha();

		if (GLEx.isPowerOfTwo(srcWidth) && GLEx.isPowerOfTwo(srcHeight)) {
			this.width = srcWidth;
			this.height = srcHeight;
			this.texHeight = srcHeight;
			this.texWidth = srcWidth;
			this.source = image.getPixels();
			if (image.isAutoDispose()) {
				image.dispose();
				image = null;
			}
			return;
		}

		int texWidth = GLEx.toPowerOfTwo(srcWidth);
		int texHeight = GLEx.toPowerOfTwo(srcHeight);

		this.width = srcWidth;
		this.height = srcHeight;
		this.texHeight = texHeight;
		this.texWidth = texWidth;

		LImage texImage = new LImage(texWidth, texHeight, hasAlpha);

		LGraphics g = texImage.getLGraphics();

		g.drawImage(image, 0, 0);

		if (this.height < texHeight - 1) {
			copyArea(texImage, g, 0, 0, width, 1, 0, texHeight - 1);
			copyArea(texImage, g, 0, height - 1, width, 1, 0, 1);
		}
		if (this.width < texWidth - 1) {
			copyArea(texImage, g, 0, 0, 1, height, texWidth - 1, 0);
			copyArea(texImage, g, width - 1, 0, 1, height, 1, 0);
		}

		this.source = texImage.getPixels();

		if (texImage != null && texImage.isAutoDispose()) {
			texImage.dispose();
			texImage = null;
		}
		if (image != null && image.isAutoDispose()) {
			image.dispose();
			image = null;
		}
	}

	/**
	 * 将LPixmap转化为LTextureData
	 * 
	 * @param image
	 */
	private void create(LPixmap image) {
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.texWidth = image.getTexWidth();
		this.texHeight = image.getTexHeight();
		this.hasAlpha = image.hasAlpha();
		this.source = image.getData();
		this.pixmap = image;
	}

	/**
	 * 复制指定的LImage图像区域
	 * 
	 * @param image
	 * @param g
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param dx
	 * @param dy
	 */
	public static void copyArea(LImage image, LGraphics g, int x, int y,
			int width, int height, int dx, int dy) {
		LImage tmp = image.getSubImage(x, y, width, height);
		g.drawImage(tmp, x + dx, y + dy);
		tmp.dispose();
		tmp = null;
	}

	/**
	 * 复制当前LTextureData
	 * 
	 */
	public LTextureData copy() {
		return new GLLoader(this, true);
	}

}
