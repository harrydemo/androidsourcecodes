package org.loon.framework.javase.game.action.sprite;

import org.loon.framework.javase.game.core.LObject;
import org.loon.framework.javase.game.core.geom.RectBox;
import org.loon.framework.javase.game.core.graphics.opengl.GLEx;
import org.loon.framework.javase.game.core.graphics.opengl.LTexture;

/**
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
 * @email：ceponline@yahoo.com.cn
 * @version 0.1
 */
public class Picture extends LObject implements ISprite {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1982153514439690901L;

	private boolean visible;

	private float alpha=1f;

	private int width, height;

	private LTexture image;

	public Picture(String fileName) {
		this(fileName, 0, 0);
	}

	public Picture(int x, int y) {
		this((LTexture) null, x, y);
	}

	public Picture(String fileName, int x, int y) {
		this(new LTexture(fileName), x, y);
	}

	public Picture(LTexture image) {
		this(image, 0, 0);
	}

	public Picture(LTexture image, int x, int y) {
		if (image != null) {
			this.setImage(image);
			this.width = image.getWidth();
			this.height = image.getHeight();
		}
		this.setLocation(x, y);
		this.visible = true;
	}

	public void createUI(GLEx g) {
		if (visible) {
			if (alpha > 0 && alpha < 1) {
				g.setAlpha(alpha);
			}
			g.drawTexture(image, x(), y());
			if (alpha > 0 && alpha < 1) {
				g.setAlpha(1.0f);
			}
		}
	}

	public boolean equals(Picture p) {
		if (this.width == p.width && this.height == p.height) {
			if (image.hashCode() == p.image.hashCode()) {
				return true;
			}
		}
		return false;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public void update(long timer) {
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void dispose() {
		if (image != null) {
			image.dispose();
			image = null;
		}
	}

	public void setImage(String fileName) {
		this.image = new LTexture(fileName);
		this.width = image.getWidth();
		this.height = image.getHeight();
	}

	public void setImage(LTexture image) {
		this.image = image;
		this.width = image.getWidth();
		this.height = image.getHeight();
	}

	public RectBox getCollisionBox() {
		return getRect(x(), y(), width, height);
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public LTexture getBitmap() {
		return image;
	}

}
