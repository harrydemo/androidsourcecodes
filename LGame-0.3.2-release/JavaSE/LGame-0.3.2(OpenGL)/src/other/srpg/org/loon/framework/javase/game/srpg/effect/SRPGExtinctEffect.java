package org.loon.framework.javase.game.srpg.effect;

import org.loon.framework.javase.game.core.LSystem;
import org.loon.framework.javase.game.core.graphics.LFont;
import org.loon.framework.javase.game.core.graphics.opengl.GLColor;
import org.loon.framework.javase.game.core.graphics.opengl.GLEx;
import org.loon.framework.javase.game.srpg.SRPGDelta;
import org.loon.framework.javase.game.srpg.SRPGType;

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

public class SRPGExtinctEffect extends SRPGEffect {

	private final String mes;

	private int t_x;

	private int t_y;

	private int w, h;

	private GLColor color;

	private SRPGDelta[][] de;

	public SRPGExtinctEffect(int x, int y, GLColor color, String mes) {
		this.mes = mes;
		this.w = SRPGType.DEFAULT_BIG_FONT.stringWidth(mes);
		this.h = SRPGType.DEFAULT_BIG_FONT.getSize();
		t_x = x;
		this.t_y = y;
		this.color = color;
		double[][] res = { { 0.0D, 30D }, { 24D, -15D }, { -24D, -15D } };
		this.de = new SRPGDelta[2][0];
		this.de[0] = new SRPGDelta[8];
		this.de[1] = new SRPGDelta[16];
		for (int i = 0; i < de.length; i++) {
			for (int j = 0; j < de[i].length; j++) {
				double d = j;
				d *= 360D / (double) de[i].length;
				d = (d * 3.1415926535897931D) / 180D;
				double d1 = Math.cos(d) * (double) (i * 5 + 2);
				double d2 = Math.sin(d) * (double) (i * 5 + 2);
				de[i][j] = new SRPGDelta(res, d1, d2, 36D);
			}
		}
		setExist(true);
	}

	public void draw(GLEx g, int x, int y) {
		next();
		g.setColor(color);
		if (super.frame < 120) {
			g.fillRect(0, 0, LSystem.screenRect.width,
					LSystem.screenRect.height);
			LFont old = g.getFont();
			g.setFont(SRPGType.DEFAULT_BIG_FONT);
			g
					.drawStyleString(mes, (LSystem.screenRect.width - w) / 2,
							(LSystem.screenRect.height - h) / 2, GLColor.white,
							GLColor.red);
			g.setFont(old);
		} else {
			g.setColor(color);
			for (int j = 0; j < de.length; j++) {
				for (int i = 0; i < de[j].length; i++)
					if (j != 0 || super.frame > 120) {
						de[j][i].drawPaint(g, t_x - x,
								LSystem.screenRect.height - (t_y - y));
					}

			}
		}
		if (super.frame >= 230) {
			setExist(false);
		}
	}

}
