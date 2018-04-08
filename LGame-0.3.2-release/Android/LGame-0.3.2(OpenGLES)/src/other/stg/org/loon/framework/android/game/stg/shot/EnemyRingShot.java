package org.loon.framework.android.game.stg.shot;

import org.loon.framework.android.game.stg.STGObject;
import org.loon.framework.android.game.stg.STGScreen;
import org.loon.framework.android.game.utils.MathUtils;

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
public abstract class EnemyRingShot extends STGObject {

	float vx;

	float vy;

	float fx;

	float fy;

	public EnemyRingShot(STGScreen stg, int no, int x, int y, int tpno) {
		super(stg, no, x, y, tpno);
		this.vx = MathUtils.cos(0.0174533f * (float) tpno) * 5.0f;
		this.vy = MathUtils.sin(0.0174533f * (float) tpno) * 5.0f;
	}

	public void update() {
		this.fx += this.vx;
		this.fy += this.vy;
		setLocation((int) this.fx, (int) this.fy);
		if (getY() > getScreenHeight() || getY() < -6 || getX() < -6
				|| getX() > getScreenWidth()) {
			delete();
		}
	}

}
