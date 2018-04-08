package org.loon.framework.javase.game.core.geom;

import org.loon.framework.javase.game.action.sprite.Bind;
import org.loon.framework.javase.game.core.LSystem;
import org.loon.framework.javase.game.utils.MathUtils;

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
public class Camera {

	private RectBox cameraRect;

	public float cameraX, cameraY;

	public float speedX, speedY;

	private float renderWidth;

	private float renderHeight;

	private Vector2f maxSpeed;

	private int horBorderPixel;

	private int vertBorderPixel;

	private RectBox visibleRect;

	private RectBox moveRect;

	private Bind follow;

	public Camera(Object f, int width, int height) {
		this(LSystem.screenRect, f, width, height, -1, -1, null);
	}

	public Camera(RectBox r, Object f, int width, int height) {
		this(r, f, width, height, -1, -1, null);
	}

	public Camera(RectBox r, Object f, int width, int height,
			int horBorderPixel, int vertBorderPixel, Vector2f maxSpeed) {
		this.cameraX = 0;
		this.cameraY = 0;
		this.renderWidth = width;
		this.renderHeight = height;
		this.follow = new Bind(f);
		this.horBorderPixel = horBorderPixel;
		this.vertBorderPixel = vertBorderPixel;
		this.maxSpeed = maxSpeed;
		if (follow != null) {
			this.cameraX = follow.getX() - (this.renderWidth / 2);
			this.cameraY = follow.getY() - (this.renderHeight / 2);
		}
		this.cameraRect = r;
		this.visibleRect = new RectBox(cameraX - horBorderPixel, cameraY
				- vertBorderPixel, renderWidth + horBorderPixel, renderHeight
				+ vertBorderPixel);
		this.moveRect = new RectBox(cameraX - horBorderPixel, cameraY
				- vertBorderPixel, renderWidth + horBorderPixel, renderHeight
				+ vertBorderPixel);
		this.updateCamera();
	}

	public void updateCamera() {
		if (follow != null
				&& !moveRect.contains(follow.getX() + follow.getWidth() / 2,
						follow.getY() + follow.getHeight() / 2)) {
			final float targetCX = follow.getX() - (this.renderWidth / 2);
			final float targetCY = follow.getY() - (this.renderHeight / 2);
			if (maxSpeed != null) {
				if (MathUtils.abs(targetCX - cameraX) > maxSpeed.x) {
					if (targetCX > cameraX) {
						cameraX += maxSpeed.x * 2;
					} else {
						cameraX -= maxSpeed.x * 2;
					}
				} else {
					cameraX = targetCX;
				}
				if (MathUtils.abs(targetCY - cameraY) > maxSpeed.y) {
					if (targetCY > cameraY) {
						cameraY += maxSpeed.y * 2;
					} else {
						cameraY -= maxSpeed.y * 2;
					}
				} else {
					cameraY = targetCY;
				}
			} else {
				cameraX = targetCX;
				cameraY = targetCY;
			}
		}
		if (cameraX < 0) {
			cameraX = 0;
		}
		if (cameraX + renderWidth > cameraRect.getWidth()) {
			cameraX = cameraRect.getWidth() - renderWidth + 1;
		}
		if (cameraY < 0) {
			cameraY = 0;
		}
		if (cameraY + renderHeight > cameraRect.getHeight()) {
			cameraY = cameraRect.getHeight() - renderHeight + 1;
		}
		visibleRect.setBounds(cameraX - horBorderPixel, cameraY
				- vertBorderPixel, renderWidth + horBorderPixel, renderHeight
				+ vertBorderPixel);
		moveRect.setBounds(cameraX + horBorderPixel / 2 - speedX, cameraY
				+ vertBorderPixel / 2, renderWidth - horBorderPixel + speedY,
				renderHeight - vertBorderPixel);
	}

	public float getCameraX() {
		return cameraX;
	}

	public void setCameraX(float cameraX) {
		this.cameraX = cameraX;
	}

	public float getCameraY() {
		return cameraY;
	}

	public void setCameraY(float cameraY) {
		this.cameraY = cameraY;
	}

	public float getSpeedX() {
		return speedX;
	}

	public void setSpeedX(float speedX) {
		this.speedX = speedX;
	}

	public float getSpeedY() {
		return speedY;
	}

	public void setSpeedY(float speedY) {
		this.speedY = speedY;
	}

	public RectBox getMoveRect() {
		return moveRect;
	}

	public boolean contains(float x, float y, float w, float h) {
		return visibleRect.contains(x, y, w, h);
	}

	public boolean intersects(float x, float y, float w, float h) {
		return visibleRect.intersects(x, y, w, h);
	}

	public boolean includes(float x, float y) {
		return visibleRect.includes(x, y);
	}

}
