package org.loon.framework.javase.game.srpg.effect;

import java.awt.Color;
import java.awt.Font;

import org.loon.framework.javase.game.core.graphics.device.LGraphics;
import org.loon.framework.javase.game.srpg.SRPGScreen;


/**
 * 
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
 * @version 0.1.1
 */
public class SRPGPopupEffect extends SRPGEffect {

	private String str;

	private Color color;

	private int len;

	private final static Font deffont = new Font("Monospaced", 0, 12);

	public SRPGPopupEffect(int x, int y, Color color, String message) {
		super(0, 0, x, y);
		this.setExist(true);
		this.color = color;
		this.str = message;
		this.len = (SRPGScreen.TILE_WIDTH - str.length() * 6) / 2;
	}

	public void draw(LGraphics g, int x, int y) {
		next();
		int frame = super.frame;
		if (frame <= 15) {
			frame = 15;
		}
		Font font = g.getFont();
		g.setFont(deffont);
		for (int j = 0; j < str.length(); j++) {
			char c = str.charAt(j);
			int nx = (super.target[0] + len + j * 6) - x;
			int ny = super.target[1] - 3 - y;
			if (super.frame < 10) {
				ny = (ny += 5) - super.frame / 2;
			}
			g.drawStyleString(String.valueOf(c), nx, ny, Color.white, color);
		}

		g.setFont(font);
		if (super.frame > 20) {
			setExist(false);
		}
	}

}
