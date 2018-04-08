package org.loon.framework.javase.game.action.sprite;

import org.loon.framework.javase.game.core.LRelease;
import org.loon.framework.javase.game.core.graphics.opengl.GLEx;
import org.loon.framework.javase.game.core.graphics.opengl.LTexture;

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
public class SpriteSheet implements LRelease {

	private int margin, spacing;

	private int tw, th;

	private int width, height;

	private LTexture[][] subImages;

	private LTexture target;

	public SpriteSheet(String fileName, int tw, int th, int s, int m) {
		this(new LTexture(fileName), tw, th, s, m);
	}

	public SpriteSheet(String fileName, int tw, int th) {
		this(new LTexture(fileName), tw, th, 0, 0);
	}

	public SpriteSheet(LTexture image, int tw, int th) {
		this(image, tw, th, 0, 0);
	}

	public SpriteSheet(LTexture img, int tw, int th, int s, int m) {
		this.width = img.getWidth();
		this.height = img.getHeight();
		this.target = img;
		this.tw = tw;
		this.th = th;
		this.margin = m;
		this.spacing = s;
	}

	private void update() {
		if (subImages != null) {
			return;
		}
		target.loadTexture();
		int tilesAcross = ((width - (margin * 2) - tw) / (tw + spacing)) + 1;
		int tilesDown = ((height - (margin * 2) - th) / (th + spacing)) + 1;
		if ((height - th) % (th + spacing) != 0) {
			tilesDown++;
		}
		subImages = new LTexture[tilesAcross][tilesDown];
		for (int x = 0; x < tilesAcross; x++) {
			for (int y = 0; y < tilesDown; y++) {
				subImages[x][y] = getImage(x, y);
			}
		}
	}

	public LTexture[][] getTextures() {
		return subImages;
	}

	private void checkImage(int x, int y) {
		update();
		if ((x < 0) || (x >= subImages.length)) {
			throw new RuntimeException("SubImage out of sheet bounds " + x
					+ "," + y);
		}
		if ((y < 0) || (y >= subImages[0].length)) {
			throw new RuntimeException("SubImage out of sheet bounds " + x
					+ "," + y);
		}
	}

	public LTexture getImage(int x, int y) {
		checkImage(x, y);
		if ((x < 0) || (x >= subImages.length)) {
			throw new RuntimeException("SubTexture2D out of sheet bounds: " + x
					+ "," + y);
		}
		if ((y < 0) || (y >= subImages[0].length)) {
			throw new RuntimeException("SubTexture2D out of sheet bounds: " + x
					+ "," + y);
		}
		return target.getSubTexture(x * (tw + spacing) + margin, y
				* (th + spacing) + margin, tw, th);
	}

	public int getHorizontalCount() {
		update();
		return subImages.length;
	}

	public int getVerticalCount() {
		update();
		return subImages[0].length;
	}

	public LTexture getSubImage(int x, int y) {
		checkImage(x, y);
		return subImages[x][y];
	}

	public void draw(GLEx g, float x, float y, int sx, int sy) {
		checkImage(sx, sy);
		g.drawTexture(subImages[sx][sy], x, y);
	}

	public int getMargin() {
		return margin;
	}

	public void setMargin(int margin) {
		this.margin = margin;
	}

	public int getSpacing() {
		return spacing;
	}

	public void setSpacing(int spacing) {
		this.spacing = spacing;
	}

	public LTexture getTarget() {
		return target;
	}

	public void setTarget(LTexture target) {
		if (this.target != null) {
			this.target.dispose();
			this.target = null;
		}
		this.target = target;
	}

	public void dispose() {
		if (target != null) {
			target.dispose();
			target = null;
		}
		if (subImages != null) {
			synchronized (subImages) {
				for (int i = 0; i < subImages.length; i++) {
					for (int j = 0; j < subImages[i].length; j++) {
						subImages[i][j].dispose();
					}
				}
				this.subImages = null;
			}
		}
	}
}
