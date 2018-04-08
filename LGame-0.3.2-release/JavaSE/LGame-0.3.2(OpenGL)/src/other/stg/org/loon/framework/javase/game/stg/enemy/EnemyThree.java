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
public abstract class EnemyThree extends STGObject {

	int direction;

	String explosion;

	String enemyShot;

	public EnemyThree(STGScreen stg, int no, int x, int y, int tpno) {
		super(stg, no, x, y, tpno);
		super.attribute = STGScreen.ENEMY;
		super.countUpdate = 20;
		super.scorePoint = 30;
		super.speed = 8;
		if (x > getWidth() / 3) {
			this.direction = -1;
		} else {
			this.direction = 1;
		}
	}

	public void update() {
		if (super.attribute != STGScreen.ENEMY) {
			this.beDestroyed();
		} else {
			if (getX() < getX(super.targetPlnNo)) {
				++this.direction;
			} else {
				--this.direction;
			}
			move(this.direction, speed);
			if ((int) (MathUtils.random() * 10.0f) == 0) {
				if (enemyShot != null) {
					addClass(enemyShot, getX(), getY() + 16, super.targetPlnNo);
				} else {
					onShot();
				}
			}
			if (getY() > getScreenHeight()) {
				delete();
			}
		}
	}

	public void beDestroyed() {
		this.scrollMove();
		if (this.count == 0) {
			if (explosion != null) {
				addClass(explosion, getX(), getY(), super.plnNo);
			} else {
				onExplosion();
			}
		} else if (this.count > countUpdate || getY() > getScreenHeight()) {
			delete();
		}
		++this.count;
		if (this.count % 2 == 0) {
			setPlaneView(false);
		} else {
			setPlaneView(true);
		}
	}

	public abstract void onExplosion();

	public abstract void onShot();

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public String getEnemyShot() {
		return enemyShot;
	}

	public void setEnemyBow(String enemy) {
		this.enemyShot = enemy;
	}

	public String getExplosion() {
		return explosion;
	}

	public void setExplosion(String explosion) {
		this.explosion = explosion;
	}

}
