package org.loon.framework.android.game.srpg.effect;

import org.loon.framework.android.game.core.graphics.LColor;
import org.loon.framework.android.game.core.graphics.LFont;
import org.loon.framework.android.game.core.graphics.device.LGraphics;
import org.loon.framework.android.game.srpg.SRPGScreen;

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
 * @version 0.1
 */
public class SRPGNumberEffect extends SRPGEffect {

	private String str;

	private LColor color;

	private int len;

	public SRPGNumberEffect(int x, int y, LColor color, String s) {
		super(0, 0, x, y);
		this.setExist(true);
		this.color = color;
		this.str = s;
		this.len = (SRPGScreen.TILE_WIDTH - str.length() * 6) / 2;
	}

	public void draw(LGraphics g, int tx, int ty) {
		next();
		if (super.frame - str.length() * 2 > 20) {
			setExist(false);
		}
		if (super.frame - str.length() * 2 > 15) {
			return;
		}
		LFont font = g.getFont();
		g.setFont(LFont.getDefaultFont());
		int frame = super.frame;
		for (int j = 0; j < str.length() && frame >= 1; j++) {
			char c = str.charAt(j);
			int x = (super.target[0] + len + j * 6) - tx;
			int y = super.target[1] - 3 - ty;
			if (frame < 3) {
				y -= (frame + 3) * 4;
			} else if (frame < 7) {
				y -= (10 - (frame + 3)) * 4;
			}
			g.drawStyleString(String.valueOf(c), x, y, LColor.white, color);
			frame -= 2;
		}

		g.setFont(font);
	}

}
