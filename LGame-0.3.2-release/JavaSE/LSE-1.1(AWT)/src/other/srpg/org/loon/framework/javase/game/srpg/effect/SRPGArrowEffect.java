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
// 攻箭划过特效，也可用于刀剑等效果.
public class SRPGArrowEffect extends SRPGEffect {

	private int s_x;

	private int s_y;

	private int max;

	private SRPGDelta[] arrow;

	private Color color;

	public SRPGArrowEffect(int x1, int y1, int x2, int y2) {
		this(x1, y1, x2, y2, Color.white);
	}

	public SRPGArrowEffect(int x1, int y1, int x2, int y2, Color color) {
		setExist(true);
		this.s_x = x1;
		this.s_y = y1;
		this.color = color;
		this.max = 0;
		double d = x2 - x1;
		double d1 = y2 - y1;
		double d2 = Math.sqrt(Math.pow(d, 2D) + Math.pow(d1, 2D));
		double d3 = (d / d2) * 8D;
		double d4 = (d1 / d2) * 8D;
		max = (int) (d2 / 8D + 0.5D);
		double[][][] sizes = { { { -16D, 0.0D }, { 8D, 2D }, { 8D, -2D } },
				{ { -16D, 0.0D }, { -10D, 6D }, { -10D, -6D } },
				{ { 4D, 0.0D }, { 8D, 6D }, { 8D, -6D } } };
		double[] result = { 0.0D, 0.0D };
		arrow = new SRPGDelta[3];
		for (int i1 = 0; i1 < sizes.length; i1++) {
			arrow[i1] = new SRPGDelta(sizes[i1], result, d3, d4 * -1D, 0.0D);
			arrow[i1].setVector((int) (SRPGDelta
					.getDegrees(x1 - x2, (y1 - y2) * -1) + 0.5D));
		}
	}

	public void draw(LGraphics g, int x, int y) {
		next();
		g.setColor(color);
		for (int j = 0; j < arrow.length; j++) {
			arrow[j].drawPaint(g, s_x - x, LSystem.screenRect.height - (s_y - y));
		}
		if (super.frame >= max) {
			setExist(false);
		}
	}

}
