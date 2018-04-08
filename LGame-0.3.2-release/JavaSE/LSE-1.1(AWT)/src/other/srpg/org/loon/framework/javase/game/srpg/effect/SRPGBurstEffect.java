package org.loon.framework.javase.game.srpg.effect;

import java.awt.Color;

import org.loon.framework.javase.game.core.LSystem;
import org.loon.framework.javase.game.core.graphics.device.LGraphics;

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
// 用以模拟一个角色的爆炸效果
public class SRPGBurstEffect extends SRPGEffect {

	private int value;

	private int wait;

	private Color color;

	private float[][] pos;

	private float[][] vector;

	public SRPGBurstEffect(int x, int y) {
		this(x, y, Color.red);
	}

	public SRPGBurstEffect(int x, int y, Color color) {
		this(x, y, 150, 32, color);
	}

	public SRPGBurstEffect(int x1, int y1, int v, int wait, Color color) {
		this.setExist(true);
		this.value = v;
		this.wait = wait;
		this.color = color;
		this.pos = new float[v][2];
		this.vector = new float[v][2];
		for (int i = 0; i < v; i++) {
			pos[i][0] = x1;
			pos[i][1] = y1;
			float f = LSystem.random.nextInt(50);
			float f1 = LSystem.random.nextInt(50);
			int j1 = LSystem.random.nextInt(50);
			f += j1;
			f1 += 49 - j1;
			if (LSystem.random.nextInt(2) == 0) {
				f *= -1F;
			}
			if (LSystem.random.nextInt(2) == 0) {
				f1 *= -1F;
			}
			vector[i][0] = f / 100F;
			vector[i][1] = f1 / 100F;
		}

	}

	public void draw(LGraphics g, int x, int y) {
		next();
		g.setColor(color);
		for (int j = 0; j < value; j++) {
			pos[j][0] += vector[j][0];
			pos[j][1] += vector[j][1];
			int nx = (int) (pos[j][0]) - x;
			int ny = (int) (pos[j][1]) - y;
			g.drawLine(nx, ny, nx, ny);
		}
		if (super.frame > wait) {
			setExist(false);
		}
	}

}
