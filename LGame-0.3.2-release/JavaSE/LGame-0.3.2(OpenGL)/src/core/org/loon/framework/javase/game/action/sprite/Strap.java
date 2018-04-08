package org.loon.framework.javase.game.action.sprite;

import org.loon.framework.javase.game.core.LObject;
import org.loon.framework.javase.game.core.geom.RectBox;
import org.loon.framework.javase.game.core.graphics.opengl.GLColor;
import org.loon.framework.javase.game.core.graphics.opengl.GLEx;
import org.loon.framework.javase.game.core.graphics.opengl.LTexture;
import org.loon.framework.javase.game.core.input.LTouch;

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
// 自0.3.2版新增类，用以模拟拖拽弹弓所产生的弹弓线轨迹（仿某iPhone游戏,当然，一个Strap仅能象征【一根】弹弓线）
public class Strap extends LObject implements ISprite {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private float[] xs = new float[4];

	private float[] ys = new float[4];

	private int dx, dy;

	private int px, py;

	private boolean visible, isClose;

	private boolean initialized;

	private boolean streached;

	private int size;

	private int count = 3;

	private GLColor color;

	private float alpha = 1f;

	public Strap() {
		this(GLColor.red);
	}

	public Strap(GLColor c) {
		this(4, c);
	}

	public Strap(int s, GLColor c) {
		this.size = s;
		this.color = c;
		this.visible = true;
	}

	public void update(long elapsedTime) {
		if (!this.initialized || isClose) {
			initialize();
		}
		if (this.streached) {
			update();
			this.dx = (this.px - x());
			this.dy = (this.py - y());
			this.xs[this.count] = (this.size + this.dx);
			this.ys[this.count] = (this.size + this.dy);
		}
	}

	public void onDrag(LTouch e) {
		onDrag(e.getX(), e.getY());
	}

	public void onDrag(float x, float y) {
		this.px = (int) x;
		this.py = (int) y;
		this.streached = true;
		if (getX() - this.px < 0) {
			if (getY() - this.py < 0) {
				this.count = 2;
			} else {
				this.count = 1;
			}
		} else if (getY() - this.py < 0) {
			this.count = 3;
		} else {
			this.count = 0;
		}
	}

	public void update() {
		this.xs[0] = -this.size;
		this.xs[1] = +this.size;
		this.xs[2] = +this.size;
		this.xs[3] = -this.size;
		this.ys[0] = -this.size;
		this.ys[1] = -this.size;
		this.ys[2] = +this.size;
		this.ys[3] = +this.size;
	}

	public void reset() {
		this.initialized = false;
	}

	public void initialize() {
		this.xs[0] = -this.size;
		this.xs[1] = +this.size;
		this.xs[2] = +this.size;
		this.xs[3] = -this.size;
		this.ys[0] = -this.size;
		this.ys[1] = -this.size;
		this.ys[2] = +this.size;
		this.ys[3] = +this.size;
		this.initialized = true;
		this.streached = false;
	}

	public void createUI(GLEx g) {
		if (!visible || isClose) {
			return;
		}
		if (alpha > 0 && alpha < 1) {
			g.setAlpha(alpha);
		}
		g.translate(x(), y());
		g.setColor(color);
		g.fillPolygon(this.xs, this.ys, 4);
		g.setColor(GLColor.black);
		g.drawPolygon(this.xs, this.ys, 4);
		g.translate(-x(), -y());
		g.resetColor();
		if (alpha > 0 && alpha < 1) {
			g.setAlpha(1);
		}
	}

	public int getWidth() {
		return size;
	}

	public int getHeight() {
		return size;
	}

	public void setAlpha(float a) {
		this.alpha = a;
	}

	public float getAlpha() {
		return alpha;
	}

	public LTexture getBitmap() {
		return null;
	}

	public RectBox getCollisionBox() {
		return getRect(x(), y(), size, size);
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isClose() {
		return isClose;
	}

	public void dispose() {
		this.isClose = true;
		if (xs != null) {
			xs = null;
		}
		if (ys != null) {
			ys = null;
		}
	}

}
