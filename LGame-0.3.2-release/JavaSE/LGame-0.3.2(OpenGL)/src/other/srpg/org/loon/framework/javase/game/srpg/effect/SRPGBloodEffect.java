package org.loon.framework.javase.game.srpg.effect;

import org.loon.framework.javase.game.core.LSystem;
import org.loon.framework.javase.game.core.graphics.opengl.GLColor;
import org.loon.framework.javase.game.core.graphics.opengl.GLEx;
import org.loon.framework.javase.game.srpg.SRPGDelta;

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
// 默认的魔法效果之一，用以模拟吸血蝙蝠吸血
public class SRPGBloodEffect extends SRPGEffect {

	private int t_x, t_y;

	private SRPGDelta[] force;

	private GLColor color;

	public SRPGBloodEffect(int x, int y) {
		this(x, y, GLColor.black);
	}

	public SRPGBloodEffect(int x, int y, GLColor color) {
		this.t_x = x;
		this.t_y = y;
		this.color = color;
		double[][] res = { { 8D, 0.0D }, { -4D, 6D }, { -4D, -6D } };
		this.force = new SRPGDelta[960];
		for (int k = 0; k < force.length; k++) {
			double d = LSystem.random.nextInt(100) + 100;
			d /= 30D;
			double d1 = LSystem.random.nextInt(360);
			double d2 = Math.cos((d1 * 3.1415926535897931D) / 180D) * d;
			double d3 = Math.sin((d1 * 3.1415926535897931D) / 180D) * d;
			force[k] = new SRPGDelta(res, d2, d3, 36D);
		}
		setExist(true);
	}

	public void draw(GLEx g, int tx, int ty) {
		next();
		int x = t_x - tx;
		int y = t_y - ty;
		g.setColor(color);
		if (super.frame < 120) {
			for (int j = 0; j < super.frame * 8; j++) {
				if (j + 120 > super.frame * 8) {
					force[j].drawPaint(g, x, (LSystem.screenRect.height - y));
				}
			}
		}
		if (super.frame >= 130) {
			setExist(false);
		}
	}

}
