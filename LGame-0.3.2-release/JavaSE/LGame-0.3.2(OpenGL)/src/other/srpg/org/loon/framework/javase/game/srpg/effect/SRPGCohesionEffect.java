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
// 某些事物内聚的效果
public class SRPGCohesionEffect extends SRPGEffect {

	private int t_x;

	private int t_y;

	private GLColor color;

	private SRPGDelta[] de;

	public SRPGCohesionEffect(int x, int y) {
		this(x, y, GLColor.orange);
	}

	public SRPGCohesionEffect(int x, int y, GLColor color) {
		this.t_x = x;
		this.t_y = y;
		this.color = color;
		double[][] res = { { 0.0D, 30D }, { 24D, -15D }, { -24D, -15D } };
		this.de = new SRPGDelta[8];
		for (int k = 0; k < de.length; k++) {
			double d = 360D / (double) de.length;
			d *= k;
			d = (d * 3.1415926535897931D) / 180D;
			double d1 = Math.cos(d);
			double d2 = Math.sin(d);
			de[k] = new SRPGDelta(res, d1, d2, 36D);
			de[k].setPos(d1 * -40D, d2 * -40D);
		}

		setExist(true);
	}

	public void draw(GLEx g, int tx, int ty) {
		next();
		g.setColor(color);
		for (int j = 0; j < de.length; j++) {
			de[j]
					.drawPaint(g, t_x - tx,( LSystem.screenRect.height
							- (t_y - ty)));
		}
		if (super.frame > 40) {
			setExist(false);
		}
	}

}
