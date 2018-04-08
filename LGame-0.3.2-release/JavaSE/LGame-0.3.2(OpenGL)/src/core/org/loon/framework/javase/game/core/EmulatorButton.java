package org.loon.framework.javase.game.core;

import org.loon.framework.javase.game.core.geom.RectBox;
import org.loon.framework.javase.game.core.graphics.opengl.GLColor;
import org.loon.framework.javase.game.core.graphics.opengl.GLEx;
import org.loon.framework.javase.game.core.graphics.opengl.LTexture;
import org.loon.framework.javase.game.core.graphics.opengl.LTextures;

/**
 * Copyright 2008 - 2010
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
public class EmulatorButton {

	private final GLColor color = new GLColor(GLColor.gray.r, GLColor.gray.g,
			GLColor.gray.b, 0.5f);

	private boolean disabled;

	private boolean click, onClick;

	private RectBox bounds;

	private LTexture bitmap, bitmap1;

	private int id;

	public EmulatorButton(String fileName, int w, int h, int x, int y) {
		this(LTextures.loadTexture(fileName).get(), w, h, x, y, true);
	}

	public EmulatorButton(LTexture img, int w, int h, int x, int y) {
		this(img, w, h, x, y, true);
	}

	public EmulatorButton(String fileName, int x, int y) {
		this(LTextures.loadTexture(fileName).get(), 0, 0, x, y, false);
	}

	public EmulatorButton(LTexture img, int x, int y) {
		this(img, 0, 0, x, y, false);
	}

	public EmulatorButton(LTexture img, int w, int h, int x, int y, boolean flag) {
		this(img, w, h, x, y, flag, img.getWidth(), img.getHeight());
	}

	public EmulatorButton(LTexture img, int w, int h, int x, int y,
			boolean flag, int sizew, int sizeh) {
		img.loadTexture();
		if (flag) {
			this.bitmap = img.getSubTexture(x, y, w, h);
		} else {
			this.bitmap = img;
		}
		if (img.getWidth() != sizew || img.getHeight() != sizeh) {
			LTexture tmp = bitmap;
			this.bitmap = bitmap.scale(sizew, sizeh);
			if (tmp != null) {
				tmp.dispose();
				tmp = null;
			}
		}
		this.bounds = new RectBox(0, 0, bitmap.getWidth(), bitmap.getHeight());
	}

	public boolean isClick() {
		return click;
	}

	public RectBox getBounds() {
		return bounds;
	}

	public void hit(int nid, int x, int y) {
		onClick = bounds.contains(x, y);
		if (nid == id) {
			click = false;
		}
		if (!disabled && !click) {
			setPointerId(nid);
			click = onClick;
		}
	}

	public void hit(int x, int y) {
		onClick = bounds.contains(x, y);
		if (!disabled && !click) {
			click = onClick;
		}
	}

	public void unhit(int nid) {
		if (id == nid) {
			click = false;
			onClick = false;
		}
	}

	public void unhit() {
		click = false;
		onClick = false;
	}

	public void setX(int x) {
		this.bounds.setX(x);
	}

	public void setY(int y) {
		this.bounds.setY(y);
	}

	public int getX() {
		return (int)bounds.getX();
	}

	public int getY() {
		return (int)bounds.getY();
	}

	public void setLocation(int x, int y) {
		this.bounds.setX(x);
		this.bounds.setY(y);
	}

	public void setPointerId(int id) {
		this.id = id;
	}

	public int getPointerId() {
		return this.id;
	}

	public boolean isEnabled() {
		return disabled;
	}

	public void disable(boolean flag) {
		this.disabled = flag;
	}

	public int getHeight() {
		return bounds.height;
	}

	public int getWidth() {
		return bounds.width;
	}

	public void setSize(int w, int h) {
		this.bounds.setWidth(w);
		this.bounds.setHeight(h);
	}

	public void setBounds(int x, int y, int w, int h) {
		this.bounds.setBounds(x, y, w, h);
	}

	public synchronized void setClickImage(LTexture i) {
		setClickImage(null, i);
	}

	public synchronized void setClickImage(LTexture on, LTexture un) {
		if (un == null) {
			return;
		}
		if (bitmap != null) {
			bitmap.dispose();
			bitmap = null;
		}
		if (bitmap1 != null) {
			bitmap1.dispose();
			bitmap1 = null;
		}
		this.bitmap = un == null ? on : un;
		this.bitmap1 = on == null ? un : on;
		this.setSize(un.getWidth(), un.getHeight());
	}

	public synchronized void setOnClickImage(LTexture img) {
		if (bitmap1 != null) {
			bitmap1.dispose();
			bitmap1 = null;
		}
		this.bitmap1 = img;
	}

	public synchronized void setUnClickImage(LTexture img) {
		if (bitmap != null) {
			bitmap.dispose();
			bitmap = null;
		}
		this.bitmap = img;
	}

	public void draw(GLEx g) {
		if (!disabled) {
			if (click && onClick) {
				if (bitmap1 != null) {
					g.drawBatch(bitmap1, bounds.x, bounds.y);
				} else {
					g.drawBatch(bitmap, bounds.x, bounds.y, color);
				}
			} else {
				g.drawBatch(bitmap, bounds.x, bounds.y);
			}
		}
	}

}
