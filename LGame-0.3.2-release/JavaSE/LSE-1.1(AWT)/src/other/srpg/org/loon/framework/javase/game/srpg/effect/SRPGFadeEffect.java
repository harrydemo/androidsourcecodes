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
// 角色消失或出现用特效，较适合魔法传送等时机使用
public class SRPGFadeEffect extends SRPGEffect {

	private int t_x;

	private int t_y;

	private SRPGDelta[] de;

	public SRPGFadeEffect(int x, int y) {
		this.t_x = x;
		this.t_y = y;
		double[][] res1 = { { 0.0D, 30D }, { 24D, -15D }, { -24D, -15D } };
		double[][] res2 = { { 24D, 15D }, { -24D, 15D }, { 0.0D, -30D } };
		this.de = new SRPGDelta[4];
		this.de[0] = new SRPGDelta(res1, 0.0D, 0.0D, -9D);
		this.de[1] = new SRPGDelta(res2, 0.0D, 0.0D, -9D);
		this.de[2] = new SRPGDelta(res1, 0.0D, 0.0D, -9D);
		this.de[3] = new SRPGDelta(res2, 0.0D, 0.0D, -9D);
		this.setExist(true);
	}

	public void draw(LGraphics g, int tx, int ty) {
		next();
		int x = t_x - tx;
		int y = t_y - ty;
		if (super.frame == 40) {
			de[0].setMoveX(5D);
			de[1].setMoveX(-5D);
			de[2].setMoveY(5D);
			de[3].setMoveY(-5D);
		}
		g.setColor(new Color((255 * super.frame) / 80,
				(255 * super.frame) / 80, (255 * super.frame) / 80));
		for (int j = 0; j < de.length; j++) {
			de[j].drawPaint(g, x, LSystem.screenRect.height - y);
		}
		if (super.frame >= 80) {
			setExist(false);
		}
	}

}
