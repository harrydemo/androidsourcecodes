package org.loon.framework.javase.game.srpg.effect;

import java.awt.Color;

import org.loon.framework.javase.game.core.LSystem;
import org.loon.framework.javase.game.core.graphics.device.LGraphics;
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
// 默认的基础魔法技能打击效果
public class SRPGStrikeEffect extends SRPGEffect {

	private int s_x;

	private int s_y;

	private int t_x;

	private int t_y;

	private int max;

	private Color color;

	private SRPGDelta arrow;

	private SRPGDelta[] dif;

	public SRPGStrikeEffect(int x1, int y1, int x2, int y2) {
		this(x1, y1, x2, y2, Color.black);
	}

	public SRPGStrikeEffect(int x1, int y1, int x2, int y2, Color color) {
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
		double res[][] = { { 8D, 0.0D }, { -4D, 6D }, { -4D, -6D } };
		double res1[][] = { { 2D, 0.0D }, { -1D, 2D }, { -1D, -2D } };
		double res2[][] = { { 1.0D, 0.0D }, { 0.0D, 1.0D }, { -1D, 0.0D },
				{ 0.0D, -1D }, { 0.70999999999999996D, 0.70999999999999996D },
				{ 0.70999999999999996D, -0.70999999999999996D },
				{ -0.70999999999999996D, 0.70999999999999996D },
				{ -0.70999999999999996D, -0.70999999999999996D } };
		this.arrow = new SRPGDelta(res, d3, d4 * -1D, 36D);
		this.dif = new SRPGDelta[8];
		for (int i1 = 0; i1 < dif.length; i1++) {
			dif[i1] = new SRPGDelta(res1, res2[i1][0], res2[i1][1] * -1D, 36D);
		}
	}

	public void draw(LGraphics g, int x, int y) {
		next();
		g.setColor(color);
		if (super.frame < max) {
			arrow.drawPaint(g, s_x - x, LSystem.screenRect.height - (s_y - y));
		} else {
			for (int i = 0; i < dif.length; i++) {
				dif[i].drawPaint(g, t_x - x, LSystem.screenRect.height - (t_y
						- y));
			}
		}
		if (super.frame >= max + 60) {
			setExist(false);
		}
	}

}
