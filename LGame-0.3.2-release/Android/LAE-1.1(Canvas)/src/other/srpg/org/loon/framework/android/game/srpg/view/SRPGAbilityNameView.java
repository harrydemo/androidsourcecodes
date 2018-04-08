package org.loon.framework.android.game.srpg.view;


import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.core.graphics.LColor;
import org.loon.framework.android.game.core.graphics.LFont;
import org.loon.framework.android.game.core.graphics.LGradation;
import org.loon.framework.android.game.core.graphics.device.LGraphics;
import org.loon.framework.android.game.srpg.ability.SRPGAbilityFactory;
import org.loon.framework.android.game.srpg.actor.SRPGStatus;

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
public class SRPGAbilityNameView extends SRPGDrawView {

	private final LFont defFont = LFont.getDefaultFont();

	private SRPGAbilityFactory ab;

	private SRPGStatus status;

	private int namelen;

	public SRPGAbilityNameView(SRPGAbilityFactory ability, SRPGStatus status) {
		this.setExist(true);
		this.setLock(false);
		super.width = LSystem.screenRect.width;
		super.height = defFont.getHeight();
		this.ab = ability;
		this.status = status;
		this.namelen = defFont.stringWidth(status.name);
	}

	public void draw(LGraphics g) {
		LFont old = g.getFont();
		g.setColor(LColor.white);
		g.setFont(defFont);
		LColor color = LColor.blue;
		if (status.team != 0) {
			color = LColor.red;
		}
		LColor color1 = LColor.black;
		LGradation.getInstance(color, color1, super.width, super.height)
				.drawWidth(g, super.left, super.top);
		g.setColor(LColor.white);
		g.drawString(ab.getAbilityName(), 5 + super.left, 12 + super.top);
		g.drawString(status.name, (super.width - namelen - 5) + super.left,
				12 + super.top);
		g.setFont(old);
	}

}
