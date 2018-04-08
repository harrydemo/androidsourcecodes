package org.loon.framework.javase.game.stg.enemy;

import org.loon.framework.javase.game.stg.STGScreen;
import org.loon.framework.javase.game.stg.STGObject;
import org.loon.framework.javase.game.utils.MathUtils;

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
public abstract class EnemyFive extends STGObject {

	float a;

	float p;

	float q;

	float ix;

	float iy;

	float direction;

	public EnemyFive(STGScreen stg, int no, int x, int y, int tpno) {
		super(stg, no, x, y, tpno);
		super.attribute = STGScreen.ENEMY;
		super.scorePoint = 20;
		if (x > getWidth() / 3) {
			this.direction = -2.0f;
		} else {
			this.direction = 2.0f;
		}
		this.q = (float) getY(tpno);
		this.p = (float) getX(tpno);
		if ((float) y == this.q) {
			++this.q;
		}
		if ((float) x == this.p) {
			++this.p;
		}
		this.a = ((float) y - this.q) / MathUtils.pow((float) x - this.p, 2.0f);
		this.direction = MathUtils.abs(this.p - (float) x) * 149.0f / 2990.0f + 0.05016722408026756f;
		if ((float) x - this.p > 0.0f) {
			this.direction = -this.direction;
		}
		this.ix = (float) x;
		this.iy = (float) y;
	}

	public void update() {
		this.ix += this.direction;
		this.iy += 2.0f * this.a * (this.ix - this.p) * this.direction;
		setLocation((int) this.ix, (int) this.iy);
		if (getY() > getScreenHeight() || getY() < -32 || getX() < -32
				|| getX() > getScreenWidth()) {
			delete();
		}
	}

}
