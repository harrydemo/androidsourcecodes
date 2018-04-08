package org.loon.framework.android.game.srpg.effect;

import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.core.graphics.opengl.GLColor;
import org.loon.framework.android.game.core.graphics.opengl.GLEx;
import org.loon.framework.android.game.srpg.SRPGDelta;
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
// 默认的魔法特效之一，适合于风雪魔法效果
public class SRPGSnowEffect extends SRPGEffect {

	private int t_x, t_y;

	private SRPGDelta first;

	private SRPGDelta[] force;

	private SRPGDelta[] dif;

	private GLColor[] colors;

	private final double[][] fdelta = { { 0.0D, 3D },
			{ 2.3999999999999999D, -1.5D }, { -2.3999999999999999D, -1.5D } };

	public SRPGSnowEffect() {
		this(LSystem.screenRect.width / 2, LSystem.screenRect.height / 2);
	}

	public SRPGSnowEffect(int x, int y) {
		this.t_x = x;
		this.t_y = y;
		this.first = new SRPGDelta(fdelta, 0.0D, 0.0D, 3D);
		double[][] vector = { { 8D, 0.0D }, { -4D, 6D }, { -4D, -6D } };
		this.force = new SRPGDelta[240];
		for (int j = 0; j < force.length; j++) {
			double d1 = LSystem.random.nextInt(200) - 100;
			d1 /= 25D;
			double d2 = LSystem.random.nextInt(200) - 100;
			d2 /= 25D;
			force[j] = new SRPGDelta(vector, d1, d2, 9D);
		}
		double res[][] = { { 32D, 0.0D }, { -16D, 24D }, { -16D, -24D } };
		dif = new SRPGDelta[256];
		colors = new GLColor[256];
		for (int j = 0; j < dif.length; j++) {
			double d1 = LSystem.random.nextInt(9000);
			d1 /= 100D;
			double d3 = LSystem.random.nextInt(8000) + 2000;
			double d4 = LSystem.random.nextInt(8000) + 2000;
			d3 /= 100D;
			d4 /= 100D;
			d3 *= Math.cos((d1 * 3.1415926535897931D) / 180D);
			d4 *= Math.sin((d1 * 3.1415926535897931D) / 180D);
			if (LSystem.random.nextInt(2) == 1) {
				d3 *= -1D;
			}
			if (LSystem.random.nextInt(2) == 1) {
				d4 *= -1D;
			}
			d3 /= 15D;
			d4 /= 15D;
			dif[j] = new SRPGDelta(res, d3, d4, LSystem.random.nextInt(30) + 3);
			int r = LSystem.random.nextInt(64) + 192;
			colors[j] = new GLColor(r, r, r);
		}

		setExist(true);
	}

	public void draw(GLEx g, int tx, int ty) {
		next();
		int x = t_x - tx;
		int y = t_y - ty;
		g.setColor(GLColor.white);
		if (super.frame < 120) {
			double[][] delta = first.getDelta();
			for (int j = 0; j < delta.length; j++) {
				for (int i = 0; i < delta[j].length; i++) {
					delta[j][i] += fdelta[j][i] / 25D;
				}
			}
			first.setDelta(delta);
			first.resetAverage();
			first.drawPaint(g, x, LSystem.screenRect.height - y);
			for (int j = 0; j < super.frame * 2; j++) {
				force[j].draw(g, x, LSystem.screenRect.height - y);
			}
		} else if (super.frame < 125)
			g.fillRect(0, 0, LSystem.screenRect.width,
					LSystem.screenRect.height);
		else if (super.frame < 240) {
			for (int i = 0; i < dif.length; i++) {
				g.setColor(colors[i]);
				dif[i].drawPaint(g, x, LSystem.screenRect.height - y);
			}
		} else {
			setExist(false);
		}
	}

}
