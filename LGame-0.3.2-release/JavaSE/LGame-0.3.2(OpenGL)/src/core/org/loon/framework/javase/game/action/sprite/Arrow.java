package org.loon.framework.javase.game.action.sprite;

import org.loon.framework.javase.game.core.LObject;
import org.loon.framework.javase.game.core.geom.Polygon;
import org.loon.framework.javase.game.core.geom.RectBox;
import org.loon.framework.javase.game.core.graphics.opengl.GLColor;
import org.loon.framework.javase.game.core.graphics.opengl.GLEx;
import org.loon.framework.javase.game.core.graphics.opengl.LTexture;

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
// 0.3.2版起新增类，用以绘制一个方向箭头
public class Arrow extends LObject implements ISprite {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean visible;

	private Polygon arrow;

	private GLColor color;

	private float alpha = 1f;

	private int triangleA, triangleB, triangleC;

	public Arrow(int x1, int y1, int x2, int y2) {
		setTriangle(5, 10, 15);
		this.visible = true;
		this.color = GLColor.red;
		setArrow(x1, y1, x2, y2);
	}

	public void createUI(GLEx g) {
		if (!visible) {
			return;
		}
		if (arrow != null) {
			if (alpha > 0 && alpha < 1) {
				g.setAlpha(alpha);
			}
			g.setColor(color);
			g.fill(arrow);
			g.setColor(GLColor.black);
			g.draw(arrow);
			g.resetColor();
			if (alpha > 0 && alpha < 1) {
				g.setAlpha(1);
			}
		}
	}

	public void setTriangle(int a, int b, int c) {
		this.triangleA = a;
		this.triangleB = b;
		this.triangleC = c;
	}

	public void setArrow(int x1, int y1, int x2, int y2) {
		float theta = (float) Math.atan((y1 - y2) / (x1 - x2));
		if (x1 - x2 >= 0) {
			theta += 3.141592653589793D;
		}

		float s = (float) Math.sin(theta);
		float c = (float) Math.cos(theta);
		float sw1 = (s * triangleA);
		float sw2 = (s * triangleB);
		float sw3 = (s * triangleC);
		float cw1 = (c * triangleA);
		float cw2 = (c * triangleB);
		float cw3 = (c * triangleC);
		float[] exes = { x1 - sw1, x2 - cw3 - sw1, x2 - cw3 - sw2, x2,
				x2 - cw3 + sw2, x2 - cw3 + sw1, x1 + sw1, x1 - sw1 };
		float[] whys = { y1 + cw1, y2 - sw3 + cw1, y2 - sw3 + cw2, y2,
				y2 - sw3 - cw2, y2 - sw3 - cw1, y1 - cw1, y1 + cw1 };
		this.arrow = new Polygon(exes, whys, 8);
	}

	public int getWidth() {
		if (arrow != null) {
			return (int) arrow.getWidth();
		}
		return 0;
	}

	public int getHeight() {
		if (arrow != null) {
			return (int) arrow.getHeight();
		}
		return 0;
	}

	public void setAlpha(float f) {
		this.alpha = f;
	}

	public float getAlpha() {
		return alpha;
	}

	public LTexture getBitmap() {
		return null;
	}

	public Polygon getArrow() {
		return arrow;
	}

	public RectBox getCollisionBox() {
		if (rect == null) {
			if (arrow != null) {
				rect = new RectBox(arrow.getX(), arrow.getY(),
						arrow.getWidth(), arrow.getHeight());
			} else {
				rect = new RectBox(x(), y(), 15, 15);
			}
		} else {
			if (arrow != null) {
				rect.setBounds(arrow.getX(), arrow.getY(), arrow.getWidth(),
						arrow.getHeight());
			} else {
				rect.setBounds(x(), y(), 15, 15);
			}
		}
		return rect;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean v) {
		this.visible = v;
	}

	public GLColor getColor() {
		return color;
	}

	public void setColor(GLColor color) {
		this.color = color;
	}

	public void update(long elapsedTime) {

	}

	public void dispose() {
		if (arrow != null) {
			arrow = null;
		}
		if (rect != null) {
			rect = null;
		}
	}

}
