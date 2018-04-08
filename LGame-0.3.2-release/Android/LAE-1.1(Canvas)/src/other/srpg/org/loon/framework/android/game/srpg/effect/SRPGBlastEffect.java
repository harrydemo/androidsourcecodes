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
// 默认的爆炸效果
public class SRPGBlastEffect extends SRPGEffect {

	private int t_x, t_y;

	private LColor color;

	private SRPGDelta[][] de;

	public SRPGBlastEffect(int x, int y) {
		this(x, y, LColor.red);
	}

	public SRPGBlastEffect(int x, int y, LColor color) {
		this.t_x = x;
		this.t_y = y;
		this.color = color;
		double[][] res = { { 0.0D, 30D }, { 24D, -15D }, { -24D, -15D } };
		de = new SRPGDelta[2][0];
		de[0] = new SRPGDelta[8];
		de[1] = new SRPGDelta[16];
		for (int j = 0; j < de.length; j++) {
			for (int i = 0; i < de[j].length; i++) {
				double d = i;
				d *= 360D / (double) de[j].length;
				d = (d * 3.1415926535897931D) / 180D;
				double d1 = Math.cos(d) * (double) (j * 5 + 2);
				double d2 = Math.sin(d) * (double) (j * 5 + 2);
				de[j][i] = new SRPGDelta(res, d1, d2, 36D);
			}
		}
		setExist(true);
	}

	public void draw(LGraphics g, int x, int y) {
		next();
		g.setColor(color);
		for (int j = 0; j < de.length; j++) {
			for (int l = 0; l < de[j].length; l++) {
				if (j != 0 || super.frame > 20) {
					de[j][l].drawPaint(g, t_x - x, LSystem.screenRect.height
							- (t_y - y));
				}
			}
		}
		if (super.frame > 80) {
			setExist(false);
		}
	}

}
