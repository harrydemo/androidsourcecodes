package org.loon.framework.android.game.srpg.effect;


import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.core.graphics.LColor;
import org.loon.framework.android.game.core.graphics.device.LGraphics;
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
// 时钟特效，用以启动时间魔法等效果
public class SRPGClockEffect extends SRPGEffect {

	private int t_x;

	private int t_y;

	private SRPGDelta[] clock;

	public SRPGClockEffect(int x, int y) {
		this.setExist(true);
		this.t_x = x;
		this.t_y = y;
		double[][][] res = { { { -150D, 0.0D }, { 0.0D, 5D }, { 0.0D, -5D } },
				{ { -75D, 0.0D }, { 0.0D, 10D }, { 0.0D, -10D } } };
		double[] arrays = { 0.0D, 0.0D };
		clock = new SRPGDelta[2];
		for (int k = 0; k < res.length; k++) {
			clock[k] = new SRPGDelta(res[k], arrays, 0.0D, 0.0D, -1D);
			clock[k].setVector(270D);
		}
	}

	public void draw(LGraphics g, int tx, int ty) {
		next();
		g.setColor(LColor.red);
		int x = t_x - tx;
		int y = t_y - ty;
		for (int j = 0; j < 10; j++) {
			g.drawOval(x - j + 5, y - j - 50, 200 + j * 2, 200 + j * 2);
		}
		double d = super.frame;
		d /= 16D;
		clock[0].setVectorSpeed(360D - d * 4D);
		clock[1].setVectorSpeed(360D - d);
		for (int j1 = 0; j1 < clock.length; j1++) {
			clock[j1].drawPaint(g, x + 100, LSystem.screenRect.height - y - 50);
		}
		if (super.frame > 200) {
			setExist(false);
		}
	}

}
