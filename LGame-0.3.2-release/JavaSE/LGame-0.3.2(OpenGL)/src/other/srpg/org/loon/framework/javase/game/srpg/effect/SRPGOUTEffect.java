package org.loon.framework.javase.game.srpg.effect;

import org.loon.framework.javase.game.core.LSystem;
import org.loon.framework.javase.game.core.graphics.opengl.GLColor;
import org.loon.framework.javase.game.core.graphics.opengl.GLEx;
import org.loon.framework.javase.game.srpg.SRPGDelta;

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
public class SRPGOUTEffect extends SRPGEffect {

	private int t_x;

	private int t_y;

	private GLColor color;

	private SRPGDelta[] de;

	public SRPGOUTEffect(int x, int y) {
		this(x, y, GLColor.black);
	}

	public SRPGOUTEffect(int x, int y, GLColor color) {
		this.t_x = x;
		this.t_y = y;
		this.color = color;
		double[][] res = { { 0.0D, 30D }, { 24D, -15D }, { -24D, -15D } };
		this.de = new SRPGDelta[8];
		this.de[0] = new SRPGDelta(res, 2D, 0.0D, 36D);
		this.de[1] = new SRPGDelta(res, 0.0D, 2D, 36D);
		this.de[2] = new SRPGDelta(res, -2D, 0.0D, 36D);
		this.de[3] = new SRPGDelta(res, 0.0D, -2D, 36D);
		this.de[4] = new SRPGDelta(res, 1.3999999999999999D,
				1.3999999999999999D, 36D);
		this.de[5] = new SRPGDelta(res, -1.3999999999999999D,
				1.3999999999999999D, 36D);
		this.de[6] = new SRPGDelta(res, 1.3999999999999999D,
				-1.3999999999999999D, 36D);
		this.de[7] = new SRPGDelta(res, -1.3999999999999999D,
				-1.3999999999999999D, 36D);
		this.setExist(true);
	}

	public void draw(GLEx g, int x, int y) {
		next();
		g.setColor(color);
		for (int i = 0; i < de.length; i++) {
			de[i].drawPaint(g, t_x - x, LSystem.screenRect.height - (t_y - y));
		}
		if (super.frame > 40) {
			setExist(false);
		}
	}

}
