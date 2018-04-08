package org.loon.framework.javase.game.srpg.view;

import java.awt.Color;

import org.loon.framework.javase.game.core.graphics.LFont;
import org.loon.framework.javase.game.core.graphics.LGradation;
import org.loon.framework.javase.game.core.graphics.device.LGraphics;
import org.loon.framework.javase.game.srpg.actor.SRPGStatus;

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
public class SRPGMiniStatusView extends SRPGDrawView {

	private SRPGStatus status;

	private int[] spans;

	private int[] values;

	public SRPGMiniStatusView(SRPGStatus status) {
		this.set(status);
	}

	public SRPGMiniStatusView(SRPGStatus status, int w, int h) {
		this.set(status, w, h);
	}

	public void set(SRPGStatus status) {
		this.set(status, 150, 85);
	}

	public void set(SRPGStatus status, int w, int h) {
		this.setExist(true);
		this.setLock(false);
		super.width = w;
		super.height = h;
		this.status = status;
		this.values = new int[6];
		this.spans = new int[6];
		this.setSpans(true);
	}

	private void setSpans(boolean flag) {
		LFont font = LFont.getDefaultFont();
		int[] res = { status.level, status.exp, status.hp, status.max_hp,
				status.mp, status.max_mp };
		for (int i = 0; i < res.length; i++) {
			if (values[i] != res[i] || flag) {
				spans[i] = font.stringWidth(String.valueOf(res[i]));
				values[i] = res[i];
			}
		}
	}

	public void draw(LGraphics g) {
		if (!exist) {
			return;
		}
		Color color = Color.blue;
		if (status.team != 0) {
			color = Color.red;
		}
		LGradation.getInstance(color, Color.black, 150, super.height)
				.drawHeight(g, super.left, super.top);
		int width = 140;
		int hp = 0;
		if (status.max_hp > 0) {
			hp = (140 * status.hp) / status.max_hp;
		}
		g.setColor(Color.black);
		g.fillRect(5 + super.left, 2 + super.top, width, 3);
		g.setColor(new Color(96, 128, 255));
		g.fillRect(5 + super.left, 2 + super.top, hp, 3);
		int mp = 0;
		if (status.max_mp > 0) {
			mp = (width * status.mp) / status.max_mp;
		}
		g.setColor(Color.black);
		g.fillRect(5 + super.left, 6 + super.top, width, 3);
		g.setColor(new Color(255, 128, 96));
		g.fillRect(5 + super.left, 6 + super.top, mp, 3);
		g.setColor(Color.white);
		g.drawString(status.name, 5 + super.left, 20 + super.top);
		g.drawString(status.jobname, 5 + super.left, 35 + super.top);
		g.drawString("LV", 5 + super.left, 50 + super.top);
		g.drawString(String.valueOf(status.level),
				(55 - spans[0]) + super.left, 50 + super.top);
		g.drawString("EXP", 65 + super.left, 50 + super.top);
		g.drawString(String.valueOf(status.exp), (110 - spans[1]) + super.left,
				50 + super.top);
		g.drawString("HP", 5 + super.left, 65 + super.top);
		g.drawString(String.valueOf(status.hp), (55 - spans[2]) + super.left,
				65 + super.top);
		g.drawString("/", 65 + super.left, 65 + super.top);
		g.drawString(String.valueOf(status.max_hp), (110 - spans[3])
				+ super.left, 65 + super.top);
		g.drawString("MP", 5 + super.left, 80 + super.top);
		g.drawString(String.valueOf(status.mp), (55 - spans[4]) + super.left,
				80 + super.top);
		g.drawString("/", 65 + super.left, 80 + super.top);
		g.drawString(String.valueOf(status.max_mp), (110 - spans[5])
				+ super.left, 80 + super.top);

	}

}
