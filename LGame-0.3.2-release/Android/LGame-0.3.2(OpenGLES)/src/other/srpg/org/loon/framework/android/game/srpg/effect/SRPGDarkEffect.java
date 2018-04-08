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
// 一个默认的黑暗魔法特效
public class SRPGDarkEffect extends SRPGEffect {

	private int t_x, t_y;

	private SRPGDelta[] sd;

	public SRPGDarkEffect(int x, int y) {
		t_x = x;
		t_y = y;
		double[][][] res = { { { 0.0D, 30D }, { 24D, -15D }, { -24D, -15D } },
				{ { -120D, 30D }, { -96D, -15D }, { -144D, -15D } },
				{ { 120D, 30D }, { 144D, -15D }, { 96D, -15D } },
				{ { 0.0D, -90D }, { 24D, -135D }, { -24D, -135D } },
				{ { 0.0D, 150D }, { 24D, 105D }, { -24D, 105D } } };
		sd = new SRPGDelta[5];
		sd[0] = new SRPGDelta(res[0], 0.0D, 0.0D, 3D);
		sd[1] = new SRPGDelta(res[1], 3D, 0.0D, 9D);
		sd[2] = new SRPGDelta(res[2], -3D, 0.0D, 9D);
		sd[3] = new SRPGDelta(res[3], 0.0D, 3D, 9D);
		sd[4] = new SRPGDelta(res[4], 0.0D, -3D, 9D);
		setExist(true);
	}

	public void draw(GLEx g, int tx, int ty) {
		next();
		int x = t_x - tx;
		int y = t_y - ty;
		g.setColor(GLColor.black);
		if (super.frame < 40) {
			for (int i = 0; i < sd.length; i++) {
				sd[i].drawPaint(g, x, LSystem.screenRect.height - y);
			}
		} else {
			int r = ((super.frame - 40) * 220) / 40;
			g.setColor(r, 0, 0);
			sd[0].drawPaint(g, x, LSystem.screenRect.height - y);
		}
		if (super.frame > 80) {
			setExist(false);
		}
	}

}
