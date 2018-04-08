package org.loon.framework.android.game;

import org.loon.framework.android.game.core.LSystem;
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
final class LGameLogo {

	private long elapsed;

	private int type, centerX, centerY;

	private LTimer timer = new LTimer(1000);

	private float alpha = 0.0f;

	private long beforeTimer;

	boolean finish;

	LTexture logo;

	private long innerClock() {
		long now = System.currentTimeMillis();
		long ret = now - beforeTimer;
		beforeTimer = now;
		return ret;
	}

	public LGameLogo(LTexture texture) {
		this.logo = texture;
	}

	public void draw(final GLEx gl) {
		if (logo == null || finish) {
			return;
		}
		if (!logo.isLoaded()) {
			this.logo.loadTexture();
			this.centerX = LSystem.screenRect.width / 2 - logo.getWidth() / 2;
			this.centerY = LSystem.screenRect.height / 2 - logo.getHeight() / 2;
		}
		if (logo == null || !logo.isLoaded()) {
			return;
		}
		elapsed = innerClock();
		gl.reset(true);
		gl.setAlpha(alpha);
		gl.drawTexture(logo, centerX, centerY);
		switch (type) {
		case 0:
			if (alpha >= 1f) {
				alpha = 1f;
				type = 1;
			}
			if (alpha < 1.0f) {
				if (timer.action(elapsed)) {
					alpha += 0.015;
				}
			}
			break;
		case 1:
			if (timer.action(elapsed)) {
				alpha = 1f;
				type = 2;
			}
			break;
		case 2:
			if (alpha <= 0f) {
				if (logo != null) {
					logo.destroy();
					logo = null;
				}
				alpha = 0;
				type = 3;
				finish = true;
				return;
			}
			if (alpha > 0.0f) {
				if (timer.action(elapsed)) {
					alpha -= 0.015;
				}
			}
			break;
		}

	}

}
