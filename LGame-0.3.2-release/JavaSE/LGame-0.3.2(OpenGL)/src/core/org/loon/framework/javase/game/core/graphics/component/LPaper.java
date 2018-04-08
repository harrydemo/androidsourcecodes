package org.loon.framework.javase.game.core.graphics.component;

import org.loon.framework.javase.game.action.sprite.Animation;
import org.loon.framework.javase.game.core.graphics.LComponent;
import org.loon.framework.javase.game.core.graphics.LContainer;
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
public class LPaper extends LContainer {

	private Animation animation = new Animation();

	public LPaper(LTexture background, int x, int y) {
		super(x, y, background.getWidth(), background.getHeight());
		this.customRendering = true;
		this.setBackground(background);
		this.setElastic(true);
		this.setLocked(true);
		this.setLayer(100);
	}

	public LPaper(LTexture background) {
		this(background, 0, 0);
	}

	public LPaper(String fileName, int x, int y) {
		this(new LTexture(fileName), x, y);
	}

	public LPaper(String fileName) {
		this(fileName, 0, 0);
	}

	public LPaper(int x, int y, int w, int h) {
		this(new LTexture(w < 1 ? w = 1 : w, h < 1 ? h = 1 : h, true), x, y);
	}

	public Animation getAnimation() {
		return this.animation;
	}

	public void setAnimation(Animation animation) {
		this.animation = animation;
	}

	public void addAnimationFrame(String fileName, long timer) {
		animation.addFrame(fileName, timer);
	}

	public void addAnimationFrame(LTexture image, long timer) {
		animation.addFrame(image, timer);
	}

	public void doClick() {
	}

	public void downClick() {
	}

	public void upClick() {
	}

	protected void processTouchClicked() {
		if (!input.isMoving()) {
			this.doClick();
		}
	}

	protected void processKeyPressed() {
		if (this.isSelected()) {
			this.doClick();
		}
	}

	protected void createCustomUI(GLEx g, int x, int y, int w, int h) {
		if (visible) {
			if (animation.getSpriteImage() != null) {
				g.drawTexture(animation.getSpriteImage(), x, y);
			}
			if (x != 0 && y != 0) {
				g.translate(x, y);
				paint(g);
				g.translate(-x, -y);
			} else {
				paint(g);
			}
		}
	}

	public void paint(GLEx g) {

	}

	public void update(long elapsedTime) {
		if (visible) {
			super.update(elapsedTime);
			animation.update(elapsedTime);
		}
	}

	protected void processTouchDragged() {
		if (!locked) {
			if (getContainer() != null) {
				getContainer().sendToFront(this);
			}
			this.move(this.input.getTouchDX(), this.input.getTouchDY());
		}
	}

	protected void processTouchPressed() {
		if (!input.isMoving()) {
			this.downClick();
		}
	}

	protected void processTouchReleased() {
		if (!input.isMoving()) {
			this.upClick();
		}
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public void createUI(GLEx g, int x, int y, LComponent component,
			LTexture[] buttonImage) {

	}

	public String getUIName() {
		return "Paper";
	}

	public void dispose() {
		super.dispose();
		if (animation != null) {
			animation.dispose();
			animation = null;
		}
	}

}
