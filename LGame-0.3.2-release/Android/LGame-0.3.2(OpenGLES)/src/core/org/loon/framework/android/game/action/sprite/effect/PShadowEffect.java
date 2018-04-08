package org.loon.framework.android.game.action.sprite.effect;

import org.loon.framework.android.game.action.sprite.ISprite;
import org.loon.framework.android.game.core.LObject;
import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.core.geom.RectBox;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.graphics.opengl.GLColor;
import org.loon.framework.android.game.core.graphics.opengl.GLEx;
import org.loon.framework.android.game.core.graphics.opengl.LTexture;
import org.loon.framework.android.game.core.timer.LTimer;
import org.loon.framework.android.game.utils.CollectionUtils;
import org.loon.framework.android.game.utils.GraphicsUtils;
import org.loon.framework.android.game.utils.ScreenUtils;

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
// 这是一个根据导入的图片黑白象素分布来完成渐变效果的特殊类，根据导入的渐变图不同，能够衍生出无穷多的渐变效果。
// 此种方式在吉里吉里(krkr)，nscript等AVG游戏引擎中较常使用。（因此，也可以直接套用它们的渐变图）
public class PShadowEffect extends LObject implements ISprite {

	private static final long serialVersionUID = 1L;

	private LTimer timer = new LTimer(100);

	private int x, y, width, height, scaleWidth, scaleHeight, count, layer;

	static int[] deasilTrans, widdershinTrans;

	private int[] nowDrawPixels, finalDrawPixels;

	private int[] backgroundPixels, finalBackgroundPixels;

	private boolean visible = true, flag = true, isUpdate, isClose;

	private float alpha = 1f;

	private LTexture texture;

	private LImage image;

	private int size;

	public final static PShadowEffect newScreenEffect(String fileName) {
		return new PShadowEffect(fileName, ScreenUtils.toScreenCaptureImage(),
				0, 0, LSystem.screenRect.width, LSystem.screenRect.height);
	}

	public PShadowEffect(String fileName) {
		this(GraphicsUtils.load8888Image(fileName));
	}

	public PShadowEffect(LImage img) {
		this(img, 0, 0);
	}

	public PShadowEffect(String fileName, String backFile) {
		this(GraphicsUtils.load8888Image(fileName), GraphicsUtils
				.loadImage(backFile), 0, 0, LSystem.screenRect.width,
				LSystem.screenRect.height);
	}

	public PShadowEffect(LImage img, int x, int y) {
		this(img, null, x, y, img.getWidth(), img.getHeight());
	}

	public PShadowEffect(String fileName, int x, int y, int w, int h) {
		this(GraphicsUtils.load8888Image(fileName), null, x, y, w, h);
	}

	public PShadowEffect(LImage img, LImage back, int x, int y) {
		this(img, back, x, y, img.getWidth(), img.getHeight());
	}

	public PShadowEffect(String fileName, String bacFile, int x, int y, int w,
			int h) {
		this(GraphicsUtils.load8888Image(fileName), GraphicsUtils
				.loadImage(bacFile), x, y, w, h);
	}

	public PShadowEffect(String fileName, LImage back, int x, int y, int w,
			int h) {
		this(GraphicsUtils.load8888Image(fileName), back, x, y, w, h);
	}

	public PShadowEffect(LImage img, LImage back, int x, int y, int w, int h) {
		if (deasilTrans == null || widdershinTrans == null) {
			deasilTrans = new int[256];
			for (int i = 0; i < 256; i++) {
				deasilTrans[i] = GLColor.getRGB(i, i, i);
			}
			int flag = 0;
			widdershinTrans = new int[256];
			for (int i = 0; i < 256; i++) {
				widdershinTrans[flag++] = deasilTrans[i];
			}
		}
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
		this.visible = true;
		LImage temp = null;
		if (back == null) {
			this.scaleWidth = GLEx.toPowerOfTwo(img.getWidth() / 4);
			this.scaleHeight = GLEx.toPowerOfTwo(img.getHeight() / 4);
			temp = img.scaledInstance(scaleWidth, scaleHeight);
			this.texture = new LTexture(scaleWidth, scaleHeight, true);
			this.image = new LImage(scaleWidth, scaleHeight, true);
			this.finalDrawPixels = temp.getPixels();
			this.nowDrawPixels = CollectionUtils.copyOf(finalDrawPixels);
			if (temp != null) {
				temp.dispose();
				temp = null;
			}
		} else {
			// PS:Android版由于象素(更准确说是数组)处理速度较慢，所以复杂图形必须减低解析度(当然，理想的方式是利用JNI进行较大的循环处理，这部分会等LGame
			// 结构稳定后统一修正)
			this.scaleWidth = img.getWidth() / 2;
			this.scaleHeight = img.getHeight() / 2;
			temp = img.scaledInstance(scaleWidth, scaleHeight);
			this.texture = new LTexture(scaleWidth, scaleHeight, true);
			this.image = new LImage(scaleWidth, scaleHeight, true);
			if (back.getWidth() == scaleWidth
					&& back.getHeight() == scaleHeight) {
				this.finalBackgroundPixels = back.getPixels();
				this.backgroundPixels = CollectionUtils
						.copyOf(finalBackgroundPixels);
			} else {
				LImage tmp = back.scaledInstance(scaleWidth, scaleHeight);
				this.finalBackgroundPixels = tmp.getPixels();
				if (tmp != null) {
					tmp.dispose();
					tmp = null;
				}
				this.backgroundPixels = CollectionUtils
						.copyOf(finalBackgroundPixels);
			}
			this.finalDrawPixels = temp.getPixels();
			this.nowDrawPixels = CollectionUtils.copyOf(finalDrawPixels);
		}
		this.size = finalDrawPixels.length;
		if (temp != null) {
			temp.dispose();
			temp = null;
		}
		if (img != null) {
			img.dispose();
			img = null;
		}
		if (back != null) {
			back.dispose();
			back = null;
		}
		if (flag) {
			count = -1;
		} else {
			count = 255;
		}

	}

	public void createUI(GLEx g) {
		if (isClose) {
			return;
		}
		if (!visible) {
			return;
		}
		if (alpha > 0 && alpha < 1) {
			g.setAlpha(alpha);
		}
		if (!isComplete() && isUpdate) {
			g.drawTexture(texture, x, y, width, height);
			g.copyImageToTexture(texture, image, false, false);
			isUpdate = false;
		} else if (!isComplete()) {
			g.drawTexture(texture, x, y, width, height);
		}
		if (alpha > 0 && alpha < 1) {
			g.setAlpha(1f);
		}
	}

	public void reset() {
		if (isClose) {
			return;
		}
		if (flag) {
			count = -1;
		} else {
			count = 255;
		}
		this.visible = true;
		this.nowDrawPixels = CollectionUtils.copyOf(finalDrawPixels);
		this.backgroundPixels = CollectionUtils.copyOf(finalBackgroundPixels);
	}

	public void update(long elapsedTime) {
		if (isClose) {
			return;
		}
		if (isUpdate) {
			return;
		}
		if (visible && timer.action(elapsedTime) && !isComplete() && !isUpdate) {
			if (backgroundPixels == null) {
				if (flag) {
					count += 2;
					if (count > 255) {
						return;
					}
					for (int i = 0; i < size; i++) {
						if (finalDrawPixels[i] == widdershinTrans[count]
								|| finalDrawPixels[i] == widdershinTrans[count - 1]) {
							nowDrawPixels[i] = 0xffffff;
						}
					}
				} else {
					for (int i = 0; i < size; i++) {
						if (finalDrawPixels[i] == deasilTrans[count]
								|| finalDrawPixels[i] == deasilTrans[count - 1]) {
							nowDrawPixels[i] = 0xffffff;
						}
					}
					count -= 2;
					if (count < 0) {
						return;
					}
				}
				image.setPixels(nowDrawPixels, scaleWidth, scaleHeight);
			} else {
				if (flag) {
					count += 2;
					if (count > 255) {
						return;
					}
					for (int i = 0; i < size; i++) {
						if (finalDrawPixels[i] == widdershinTrans[count]
								|| finalDrawPixels[i] == widdershinTrans[count - 1]) {
							backgroundPixels[i] = 0xffffff;
						}
					}
				} else {
					for (int i = 0; i < size; i++) {
						if (finalDrawPixels[i] == deasilTrans[count]
								|| finalDrawPixels[i] == deasilTrans[count - 1]) {
							backgroundPixels[i] = 0xffffff;
						}
					}
					count -= 2;
					if (count < 0) {
						return;
					}
				}
				image.setPixels(backgroundPixels, scaleWidth, scaleHeight);
			}
			isUpdate = true;
		}
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isComplete() {
		return flag ? (count >= 255) : (count <= 0);
	}

	public void setDelay(long delay) {
		timer.setDelay(delay);
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public boolean isBlackToWhite() {
		return flag;
	}

	public void setBlackToWhite(boolean flag) {
		this.flag = flag;
	}

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public RectBox getCollisionBox() {
		return getRect(x, y, width, height);
	}

	public int x() {
		return x;
	}

	public int y() {
		return y;
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

	public boolean isClose() {
		return isClose;
	}

	public void dispose() {
		this.isClose = true;
		this.finalDrawPixels = null;
		this.nowDrawPixels = null;
		if (texture != null) {
			texture.destroy();
			texture = null;
		}
		if (image != null) {
			image.dispose();
			image = null;
		}
	}

}
