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
// 默认的风系魔法特效
public class SRPGWindEffect extends SRPGEffect {

	private int rand;

	private int time;

	private SRPGDelta[] de;

	private Color[] colors;

	public SRPGWindEffect() {
		this(128, 80, 120);
	}

	public SRPGWindEffect(int s, int r, int t) {
		this.rand = r;
		this.time = t;
		this.de = new SRPGDelta[s];
		this.colors = new Color[s];
		this.setExist(true);
	}

	public void draw(LGraphics g, int x, int y) {
		next();
		for (int i = 0; i < de.length; i++) {
			if (de[i] != null) {
				continue;
			}
			if (LSystem.random.nextInt(100) >= rand) {
				break;
			}
			double[][] res = { { 0.0D, 10D }, { 8D, -5D }, { -8D, -5D } };
			int index = LSystem.random.nextInt(3) + 1;
			for (int j = 0; j < res.length; j++) {
				for (int c = 0; c < res[j].length; c++) {
					res[j][c] *= index;
				}
			}
			int r = LSystem.random.nextInt(32) + 16;
			double d = r / 10;
			de[i] = new SRPGDelta(res, r, d, LSystem.random.nextInt(24) + 24);
			de[i].setPosY(LSystem.random.nextInt(LSystem.screenRect.height));
			de[i].setPosX(-30D);
			colors[i] = new Color(0, 128 + LSystem.random.nextInt(128),
					LSystem.random.nextInt(128));
		}

		for (int j = 0; j < de.length; j++) {
			if(colors[j]==null){
				continue;
			}
			g.setColor(colors[j]);
			if (de[j] == null) {
				continue;
			}
			de[j].drawPaint(g, 0, 0);
			if (de[j].getPosX() > LSystem.screenRect.width) {
				de[j] = null;
			}
		}

		if (super.frame > time) {
			setExist(false);
		}
	}

}
