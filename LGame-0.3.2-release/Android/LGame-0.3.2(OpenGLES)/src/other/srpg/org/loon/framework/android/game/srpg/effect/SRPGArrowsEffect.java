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
// 一组箭头划过特效，也可用于刀剑等效果.
public class SRPGArrowsEffect extends SRPGEffect {

	private int s_x;

	private int s_y;

	private GLColor color;

	private int[] max;

	private boolean[] exist;

	private SRPGDelta[][] arrow;

	public SRPGArrowsEffect(int x1, int y1, int x2, int y2) {
		this(x1, y1, x2, y2, GLColor.white);
	}

	public SRPGArrowsEffect(int x1, int y1, int x2, int y2, GLColor color) {
		this.setExist(true);
		this.s_x = x1;
		this.s_y = y1;
		this.color = color;
		this.arrow = new SRPGDelta[9][3];
		this.max = new int[9];
		this.exist = new boolean[9];
		int[][] result = { { 0, 0 }, { 32, 0 }, { -32, 0 }, { 0, 32 },
				{ 0, -32 }, { 16, 16 }, { -16, 16 }, { 16, -16 }, { -16, -16 } };
		double[][][] arrays = { { { -16D, 0.0D }, { 8D, 2D }, { 8D, -2D } },
				{ { -16D, 0.0D }, { -10D, 6D }, { -10D, -6D } },
				{ { 4D, 0.0D }, { 8D, 6D }, { 8D, -6D } } };
		double[] res = { 0.0D, 0.0D };
		for (int j = 0; j < arrow.length; j++) {
			max[j] = 0;
			exist[j] = true;
			int x = x2 + result[j][0];
			int y = y2 + result[j][1];
			double d = x - x1;
			double d1 = y - y1;
			double d2 = Math.sqrt(Math.pow(d, 2D) + Math.pow(d1, 2D));
			double d3 = (d / d2) * 8D;
			double d4 = (d1 / d2) * 8D;
			max[j] = (int) (d2 / 8D + 0.5D);
			for (int i = 0; i < arrays.length; i++) {
				arrow[j][i] = new SRPGDelta(arrays[i], res, d3, d4 * -1D, 0.0D);
				arrow[j][i].setVector((int) (SRPGDelta.getDegrees(x1 - x,
						(y1 - y) * -1) + 0.5D));
			}

		}

	}

	public void draw(GLEx g, int x, int y) {
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
			if (j * 3 > super.frame) {
				continue;
			}
			for (int i = 0; i < arrow[j].length; i++) {
				arrow[j][i].drawPaint(g, s_x - x, LSystem.screenRect.height
						- (s_y - y));
			}
			max[j]--;
		}
		if (!flag) {
			setExist(false);
		}
	}

}
