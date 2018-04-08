package org.loon.framework.android.game.physics;

import org.loon.framework.android.game.action.sprite.ISprite;
import org.loon.framework.android.game.action.sprite.SpriteImage;
import org.loon.framework.android.game.action.sprite.SpriteRotate;
import org.loon.framework.android.game.core.geom.Polygon;
import org.loon.framework.android.game.core.geom.RectBox;
import org.loon.framework.android.game.core.geom.Vector2D;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.graphics.device.LGraphics;
import org.loon.framework.android.game.utils.CollectionUtils;
import org.loon.framework.android.game.utils.GraphicsUtils;

import android.graphics.Bitmap;

import com.badlogic.gdx.physics.box2d.PolygonShape;

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
public class PolygonSprite implements ISprite {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int layer, width, height;

	private Vector2D location;

	private SpriteImage spriteImage;

	private float alpha;

	private int oldAngle;

	private Bitmap imageBuffer;

	private PhysicsPolygon physicsPolygon;

	private int[] pixels;

	private RectBox rect;

	private boolean showImage, showPolygon, visible, lockUpdate;

	private Polygon finalPolygon, movePolygon;

	private Thread rotateThread;

	public PolygonSprite(String fileName) {
		this(fileName, 0, 0);
	}

	public PolygonSprite(String fileName, int x, int y) {
		this(GraphicsUtils.loadImage(fileName).getBitmap(), x, y);
	}

	public PolygonSprite(Bitmap image, int x, int y) {
		this.visible = true;
		this.showImage = true;
		this.showPolygon = false;
		this.spriteImage = new SpriteImage(image);
		this.location = new Vector2D(x, y);
		this.width = spriteImage.getWidth();
		this.height = spriteImage.getHeight();
		this.imageBuffer = spriteImage.getBitmap();
		this.movePolygon = new Polygon();
		this.pixels = GraphicsUtils.getPixels(imageBuffer);
		this.makePolygon();
	}

	public void rotate(final int angle) {
		if (oldAngle == angle) {
			return;
		}
		if (rotateThread != null) {
			return;
		}
		Runnable runnable = new Runnable() {
			public void run() {
				if (imageBuffer != null) {
					synchronized (imageBuffer) {
						if (spriteImage != null) {
							synchronized (spriteImage) {
								SpriteRotate sr = spriteImage.rotate(angle);
								Bitmap tmp = sr.getBitmap(0);
								if (showImage) {
									imageBuffer = tmp;
								}
								pixels = GraphicsUtils.getPixels(tmp);
								finalPolygon = spriteImage.makePolygon(pixels,
										0, 0, 0, 0, tmp.getWidth(), tmp
												.getHeight());
								makePolygon();
							}
						}
					}
				}
				rotateThread = null;
			}
		};
		rotateThread = new Thread(runnable);
		rotateThread.start();
	}

	private void makePolygon() {
		if (!lockUpdate) {
			if (finalPolygon == null) {
				finalPolygon = spriteImage.makePolygon(pixels, 0, 0, 0, 0,
						width, height);
			}
			movePolygon.npoints = finalPolygon.npoints;
			movePolygon.xpoints = CollectionUtils.copyOf(finalPolygon.xpoints,
					finalPolygon.npoints);
			movePolygon.ypoints = CollectionUtils.copyOf(finalPolygon.ypoints,
					finalPolygon.npoints);
			movePolygon.translate(location.x(), location.y());
			if (physicsPolygon == null) {
				physicsPolygon = new PhysicsPolygon(movePolygon);
			} else {
				physicsPolygon.setPolygon(movePolygon);
			}
		}
	}

	public void setPolygonInterval(int i) {
		spriteImage.setMakePolygonInterval(i);
	}

	public int getPolygonInterval() {
		return spriteImage.getMakePolygonInterval();
	}

	public void createUI(LGraphics g) {
		if (visible) {
			if (alpha > 0.1 && alpha < 1.0) {
				g.setAlpha(alpha);
				if (showImage) {
					if (imageBuffer != null) {
						synchronized (imageBuffer) {
							g.drawBitmap(imageBuffer, location.x(), location
									.y());
						}
					}
				}
				if (showPolygon) {
					g.drawPolygon(movePolygon);
				}
				g.setAlpha(1.0F);
			} else {
				if (showImage) {
					if (imageBuffer != null) {
						synchronized (imageBuffer) {
							g.drawBitmap(imageBuffer, location.x(), location
									.y());
						}
					}
				}
				if (showPolygon) {
					g.drawPolygon(movePolygon);
				}
			}
		}
	}

	public PhysicsPolygon getPhysicsPolygon() {
		return physicsPolygon;
	}

	public PolygonShape getPolygonShape() {
		return physicsPolygon.getPolygonShape();
	}

	public Polygon getPolygon() {
		return physicsPolygon.getPolygon();
	}

	public void setPolygon(int xpoints[], int ypoints[], int npoints) {
		physicsPolygon.setPolygon(xpoints, ypoints, npoints);
	}

	public void setPolygon(Polygon polygon) {
		physicsPolygon.setPolygon(polygon);
	}

	public void dispose() {
		if (imageBuffer != null) {
			imageBuffer.recycle();
			imageBuffer = null;
		}
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public float getAlpha() {
		return alpha;
	}

	public RectBox getCollisionBox() {
		if (rect == null) {
			rect = new RectBox(x(), y(), width, height);
		} else {
			rect.setBounds(x(), y(), width, height);
		}
		return rect;
	}

	public int getHeight() {
		return width;
	}

	public int getWidth() {
		return width;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void update(long elapsedTime) {

	}

	public boolean isShowImage() {
		return showImage;
	}

	public void setShowImage(boolean showImage) {
		this.showImage = showImage;
	}

	public boolean isShowPolygon() {
		return showPolygon;
	}

	public void setShowPolygon(boolean showPolygon) {
		if (lockUpdate) {
			showPolygon = false;
			return;
		}
		this.showPolygon = showPolygon;
	}

	public SpriteImage getSpriteImage() {
		return spriteImage;
	}

	public float[] getVertex(int index) {
		return physicsPolygon.getVertex(index);
	}

	public void setLocation(int x, int y) {
		location.setLocation(x, y);
		this.makePolygon();
	}

	public void setX(int x) {
		setLocation(x, y());
	}

	public void setY(int y) {
		setLocation(x(), y);
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}

	public int getLayer() {
		return layer;
	}

	public int x() {
		return location.x();
	}

	public int y() {
		return location.y();
	}

	public double getX() {
		return location.getX();
	}

	public double getY() {
		return location.getY();
	}

	public LImage getImage() {
		return new LImage(imageBuffer);
	}

	public boolean isLockUpdate() {
		return lockUpdate;
	}

	public void setLockUpdate(boolean lockUpdate) {
		this.lockUpdate = lockUpdate;
		if (lockUpdate) {
			showPolygon = false;
		}
	}

	public Vector2D getLocation() {
		return location;
	}

	public Bitmap getBitmap() {
		return imageBuffer;
	}

}
