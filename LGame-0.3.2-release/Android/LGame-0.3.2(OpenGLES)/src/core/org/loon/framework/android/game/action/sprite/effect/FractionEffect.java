package org.loon.framework.android.game.action.sprite.effect;

import org.loon.framework.android.game.action.sprite.ISprite;
import org.loon.framework.android.game.core.LObject;
import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.core.geom.RectBox;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.graphics.LPixmapData;
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
public class FractionEffect extends LObject implements ISprite {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private LTimer timer = new LTimer(50);

	private int width, height, size;

	private float expandLimit = 1.2f, alpha = 1;

	private float offsetX, offsetY, exWidth, exHeigth;

	private Fraction[] fractions;

	private LPixmapData pixmap;

	private boolean isUpdate, isClose, isVisible;

	final class Fraction {

		float x, y, vx, vy;

		int color, countToCrush;
	}

	public FractionEffect(String fileName) {
		setImage(LImage.createImage(fileName), 1.1f);
	}

	public FractionEffect(String fileName, float limit) {
		setImage(LImage.createImage(fileName), limit);
	}

	public void setImage(LImage image, float limit) {
		this.isVisible = true;
		this.expandLimit = limit;
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.fractions = new Fraction[width * height];
		this.exWidth = width * expandLimit;
		this.exHeigth = height * expandLimit;
		int[] pixels = image.getPixels();
		this.size = pixels.length;
		this.pixmap = new LPixmapData((int) exWidth, (int) exHeigth, true);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int n = y * width + x;
				fractions[n] = new Fraction();
				float angle = LSystem.random.nextInt(360);
				float speed = 10f / LSystem.random.nextInt(30);
				fractions[n].x = x;
				fractions[n].y = y;
				fractions[n].vx = (float) Math.cos(angle * Math.PI / 180)
						* speed;
				fractions[n].vy = (float) Math.sin(angle * Math.PI / 180)
						* speed;
				fractions[n].color = pixels[n] == 65280 ? 0xffffff : pixels[n];
				fractions[n].countToCrush = x / 6 + LSystem.random.nextInt(10);
			}
		}
		if (image != null) {
			image.dispose();
			image = null;
		}
	}

	public void update(long elapsedTime) {
		if (isClose) {
			return;
		}
		if (!isVisible) {
			return;
		}
		if (timer.action(elapsedTime) && !isUpdate) {
			pixmap.reset();
			for (int n = 0; n < size; n++) {
				if (fractions[n].color != 0xffffff) {
					if (fractions[n].countToCrush <= 0) {
						fractions[n].x += fractions[n].vx;
						fractions[n].y += fractions[n].vy;
						fractions[n].vy += 0.1;
					} else {
						fractions[n].countToCrush--;
					}
					offsetX = fractions[n].x;
					offsetY = fractions[n].y;
					pixmap
							.put((int) offsetX, (int) offsetY,
									fractions[n].color);
				}
			}
			isUpdate = true;
		}
	}

	public void createUI(GLEx g) {
		if (isClose) {
			return;
		}
		if (!isVisible) {
			return;
		}
		if (isComplete()) {
			return;
		}
		if (alpha > 0 && alpha < 1) {
			g.setAlpha(alpha);
		}
		pixmap.draw(g, x(), y());
		if (alpha > 0 && alpha < 1) {
			g.setAlpha(1f);
		}
		isUpdate = false;
	}

	public boolean isComplete() {
		return offsetX < 2 || offsetY < 2 || offsetX > exWidth - 2
				|| offsetY > exHeigth - 2;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public LTexture getBitmap() {
		return null;
	}

	public RectBox getCollisionBox() {
		return getRect(x(), y(), width, height);
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean visible) {
		this.isVisible = true;
	}

	public void dispose() {
		this.isClose = true;
	}

}
