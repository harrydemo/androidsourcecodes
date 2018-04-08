package org.loon.framework.android.game.stg.item;

import org.loon.framework.android.game.stg.STGHero;
import org.loon.framework.android.game.stg.STGScreen;

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
public abstract class MpUp extends Item {

	String effectName;

	public MpUp(STGScreen stg, int no, int x, int y, int tpno) {
		super(stg, no, x, y, tpno);
		super.attribute = STGScreen.ITEM;
		super.hitX = 0;
		super.hitY = 0;
		super.scorePoint = 0;
		super.speed = 8;
	}

	public void update() {
		if (super.attribute != STGScreen.ITEM) {
			if (effectName != null) {
				addBombHero(effectName);
			} else {
				onEffect();
			}
			delete();
		} else {
			move(0, speed);
			if (getY() > getScreenHeight()) {
				delete();
			}
		}
	}

	public void giveHeroEvent(STGHero hero) {
		hero.upLastMp(1);
	}

	public String getEffectName() {
		return effectName;
	}

	public void setEffectName(String effectName) {
		this.effectName = effectName;
	}

	public abstract void onEffect();
}
