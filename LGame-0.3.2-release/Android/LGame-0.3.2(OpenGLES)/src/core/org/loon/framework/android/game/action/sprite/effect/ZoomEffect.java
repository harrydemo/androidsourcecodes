package org.loon.framework.android.game.action.sprite.effect;

import org.loon.framework.android.game.action.sprite.ISprite;
import org.loon.framework.android.game.core.LObject;
import org.loon.framework.android.game.core.geom.RectBox;
import org.loon.framework.android.game.core.graphics.opengl.GLEx;
import org.loon.framework.android.game.core.graphics.opengl.LTexture;
import org.loon.framework.android.game.core.timer.LTimer;


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
/**
 * 0.3.2新增特效类，缩放并还原指定图像
 */
public class ZoomEffect extends LObject implements ISprite {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private float alpha;

	private int width, height;

	private boolean visible, complete;

	private LTimer timer;

	private LTexture texture;

	private int count;

	private int maxcount = 20;

	private int centerx = -1, centery = -1;

	public ZoomEffect(String fileName) {
		this(new LTexture(fileName));
	}

	public ZoomEffect(LTexture t) {
		this.texture = t;
		this.width = t.getWidth();
		this.height = t.getHeight();
		this.timer = new LTimer(100);
		this.visible = true;
	}

	public void setDelay(long delay) {
		timer.setDelay(delay);
	}

	public long getDelay() {
		return timer.getDelay();
	}

	public boolean isComplete() {
		return complete;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public void update(long elapsedTime) {
		if (complete) {
			return;
		}
		if (this.count >= this.maxcount) {
			this.complete = true;
		}
		if (timer.action(elapsedTime)) {
			count++;
		}
	}

	public void createUI(GLEx g) {
		if (!visible) {
			return;
		}
		if (complete) {
			if (alpha > 0 && alpha < 1) {
				g.setAlpha(alpha);
			}
			g.drawTexture(this.texture, x(), y(), width, height);
			if (alpha > 0 && alpha < 1) {
				g.setAlpha(1f);
			}
			return;
		}
		if (alpha > 0 && alpha < 1) {
			g.setAlpha(alpha);
		}
		if (this.centerx < 0) {
			this.centerx = (width / 2);
		}
		if (this.centery < 0) {
			this.centery = (height / 2);
		}
		if (this.count < this.maxcount / 2) {
			final float partxs = this.centerx / (this.maxcount / 2 + 1);
			final float partys = this.centery / (this.maxcount / 2 + 1);
			final float partxe = (width - this.centerx)
					/ (this.maxcount / 2 + 1);
			final float partye = (height - this.centery)
					/ (this.maxcount / 2 + 1);
			int dxs = (int) (this.count * partxs);
			int dys = (int) (this.count * partys);
			int dxe = (int) (width - this.count * partxe);
			int dye = (int) (height - this.count * partye);
			g.drawTexture(this.texture, x(), y(), width, height, dxs, dys, dxe,
					dye);
		} else {
			final float partxs = this.centerx / (this.maxcount / 2 + 1);
			final float partys = this.centery / (this.maxcount / 2 + 1);
			final float partxe = (width - this.centerx)
					/ (this.maxcount / 2 + 1);
			final float partye = (height - this.centery)
					/ (this.maxcount / 2 + 1);
			int dxs = (int) ((this.maxcount - this.count) * partxs);
			int dys = (int) ((this.maxcount - this.count) * partys);
			int dxe = (int) (width - (this.maxcount - this.count) * partxe);
			int dye = (int) (height - (this.maxcount - this.count) * partye);
			g.drawTexture(this.texture, x(), y(), width, height, dxs, dys, dxe,
					dye);
		}
		if (alpha > 0 && alpha < 1) {
			g.setAlpha(1f);
		}
	}

	public void reset() {
		this.complete = false;
		this.count = 0;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public float getAlpha() {
		return alpha;
	}

	public LTexture getBitmap() {
		return texture;
	}

	public RectBox getCollisionBox() {
		return getRect(x(), y(), width, height);
	}

	public int getCenterX() {
		return centerx;
	}

	public void setCenterX(int centerx) {
		this.centerx = centerx;
	}

	public int getCenterY() {
		return centery;
	}

	public void setCenterY(int centery) {
		this.centery = centery;
	}

	public int getMaxCount() {
		return maxcount;
	}

	public void setMaxCount(int maxcount) {
		this.maxcount = maxcount;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void dispose() {
		if (texture != null) {
			texture.destroy();
			texture = null;
		}
	}
}

