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
// 默认为冰弹魔法效果，当然也可以通过改变颜色作为其他效果使用.
public class SRPGIceEffect extends SRPGEffect {

	private int s_x;

	private int s_y;

	private int t_x;

	private int t_y;

	private int max;

	private GLColor color;

	private SRPGDelta arrow;

	private SRPGDelta[] dif;

	private SRPGDelta[][] child;

	public SRPGIceEffect(int x1, int y1, int x2, int y2) {
		this(x1, y1, x2, y2, GLColor.white);
	}

	public SRPGIceEffect(int x1, int y1, int x2, int y2, GLColor color) {
		this.setExist(true);
		this.s_x = x1;
		this.s_y = y1;
		this.t_x = x2;
		this.t_y = y2;
		this.color = color;
		this.max = 0;
		double d = x2 - x1;
		double d1 = y2 - y1;
		double d2 = Math.sqrt(Math.pow(d, 2D) + Math.pow(d1, 2D));
		double d3 = (d / d2) * 8D;
		double d4 = (d1 / d2) * 8D;
		this.max = (int) (d2 / 8D + 0.5D);
		double res[][] = { { 32D, 0.0D }, { -16D, 24D }, { -16D, -24D } };
		double res1[][] = { { 16D, 0.0D }, { -8D, 12D }, { -8D, -12D } };
		double res2[][] = { { 8D, 0.0D }, { -4D, 6D }, { -4D, -6D } };
		double res3[][] = { { 4D, 0.0D }, { 0.0D, 4D }, { -4D, 0.0D },
				{ 0.0D, -4D }, { 2.8300000000000001D, 2.8300000000000001D },
				{ 2.8300000000000001D, -2.8300000000000001D },
				{ -2.8300000000000001D, 2.8300000000000001D },
				{ -2.8300000000000001D, -2.8300000000000001D } };
		this.arrow = new SRPGDelta(res, d3, d4 * -1D, 36D);
		this.dif = new SRPGDelta[8];
		this.child = new SRPGDelta[dif.length][8];
		for (int i = 0; i < dif.length; i++) {
			dif[i] = new SRPGDelta(res1, res3[i][0], res3[i][1] * -1D, 36D);
			for (int j1 = 0; j1 < child[i].length; j1++) {
				child[i][j1] = new SRPGDelta(res2, res3[j1][0] / 2D,
						(res3[j1][1] / 2D) * -1D, 36D);
				child[i][j1].setPos(res3[i][0] * 20D, res3[i][1] * 20D);
			}
		}
	}

	public void draw(GLEx g, int tx, int ty) {
		next();
		g.setColor(color);
		if (super.frame < max) {
			arrow.drawPaint(g, s_x - tx, LSystem.screenRect.height - s_y - ty);
		} else if (super.frame < max + 20) {
			for (int j = 0; j < dif.length; j++) {
				dif[j].drawPaint(g, t_x - tx, LSystem.screenRect.height - (t_y
						- ty));
			}
		} else {
			for (int j = 0; j < child.length; j++) {
				for (int i = 0; i < child[j].length; i++) {
					child[j][i].drawPaint(g, t_x - tx,
							LSystem.screenRect.height - (t_y - ty));
				}
			}
		}
		if (super.frame >= max + 50) {
			setExist(false);
		}
	}

}
