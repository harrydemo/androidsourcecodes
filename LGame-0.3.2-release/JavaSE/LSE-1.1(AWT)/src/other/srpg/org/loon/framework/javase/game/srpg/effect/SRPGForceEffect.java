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
// 掠夺某些事物的效果
public class SRPGForceEffect extends SRPGEffect {

	private int s_x;

	private int s_y;

	private Color color;

	private int[] max;

	private boolean[] exist;

	private SRPGDelta[] arrow;

	public SRPGForceEffect(int x1, int y1, int x2, int y2) {
		this(x1, y1, x2, y2, 100, Color.red);
	}

	public SRPGForceEffect(int x1, int y1, int x2, int y2, int v, Color color) {
		this.setExist(true);
		this.s_x = x1;
		this.s_y = y1;
		this.color = color;
		this.arrow = new SRPGDelta[v];
		this.max = new int[v];
		this.exist = new boolean[v];
		double[][] res = { { -16D, -12D }, { -8D, -16D }, { -8D, -8D } };
		double[] res1 = { 0.0D, 0.0D };
		for (int i = 0; i < arrow.length; i++) {
			max[i] = 0;
			exist[i] = true;
			int r1 = (x2 + (LSystem.random.nextInt(64)) - 32);
			int r2 = (y2 + (LSystem.random.nextInt(64)) - 32);
			double d = r1 - x1;
			double d1 = r2 - y1;
			double d2 = Math.sqrt(Math.pow(d, 2D) + Math.pow(d1, 2D));
			double d3 = (d / d2) * 4D;
			double d4 = (d1 / d2) * 4D;
			max[i] = (int) (d2 / 4D + 0.5D);
			arrow[i] = new SRPGDelta(res, res1, d3, d4 * -1D, 36D);
			arrow[i].setVector((int) (SRPGDelta.getDegrees(x1 - r1, (y1 - r2)
					* -1) + 0.5D + (double) (i * -15)));
		}

	}

	public void draw(LGraphics g, int tx, int ty) {
		next();
		g.setColor(color);
		boolean flag = false;
		for (int j = 0; j < arrow.length; j++) {
			if (0 > max[j]) {
				exist[j] = false;
			}
			if (!exist[j]) {
				continue;
			}
			flag = true;
			if (j * 1 <= super.frame) {
				arrow[j].drawPaint(g, s_x - tx, LSystem.screenRect.height
						- (s_y - ty));
				max[j]--;
			}
		}
		if (!flag) {
			setExist(false);
		}
	}

}
