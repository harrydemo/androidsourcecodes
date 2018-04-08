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
 * Unless req(uired by applicable law or agreed to in writing, software
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
public class SRPGUpperEffect extends SRPGEffect {

	private int t_x;

	private int t_y;

	private SRPGDelta[] force;

	private GLColor color;

	public SRPGUpperEffect(int x, int y, GLColor color) {
		this.t_x = x;
		this.t_y = y;
		this.color = color;
		double[][] res = { { 8D, 0.0D }, { -4D, 6D }, { -4D, -6D } };
		this.force = new SRPGDelta[16];
		for (int k = 0; k < force.length; k++) {
			double d = LSystem.random.nextInt(256) + 10;
			d /= 50D;
			force[k] = new SRPGDelta(res, 0.0D, d, 36D);
			force[k].setPosX(LSystem.random.nextInt(32) - 15);
		}
		this.setExist(true);
	}

	public void draw(GLEx g, int x, int y) {
		next();
		GLColor c = g.getColor();
		g.setColor(color);
		if (super.frame < 20) {
			for (int i = 0; i < force.length; i++) {
				force[i]
						.draw(g, t_x - x, LSystem.screenRect.height - (t_y - y));
			}
		} else {
			setExist(false);
		}
		g.setColor(c);
	}

}
