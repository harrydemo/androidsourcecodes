package org.loon.framework.javase.game.stg.effect;

import org.loon.framework.javase.game.core.graphics.opengl.GLColor;
import org.loon.framework.javase.game.core.graphics.opengl.GLEx;
import org.loon.framework.javase.game.stg.STGPlane;

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
public class PictureRect implements Picture {

	int w;

	int h;

	GLColor color;

	PictureRect(int w, int h, GLColor c) {
		this.w = w;
		this.h = h;
		this.color = c;
	}

	public boolean paint(GLEx g, STGPlane p) {
		g.setColor(this.color);
		g.fillRect(p.posX, p.posY, this.w, this.h);
		g.resetColor();
		return true;
	}

	public void darker() {
		this.color = this.color.darker();
	}

	public void dispose() {

	}

}
