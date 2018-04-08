package org.loon.framework.android.game.stg;

import java.lang.reflect.Constructor;
import java.util.Enumeration;
import java.util.Hashtable;

import org.loon.framework.android.game.stg.item.Item;
import org.loon.framework.android.game.core.LRelease;
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
class STGObjects extends Hashtable<Integer, STGObject> implements LRelease {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;

	private STGScreen stg;

	private int count;

	private int overCount;

	protected int heroPlnNo;

	protected int firstPlnNo;

	protected int score = 0;

	STGObjects(STGScreen stg, int no) {
		this.firstPlnNo = this.count = no;
		this.heroPlnNo = this.firstPlnNo + 100;
		this.overCount = this.heroPlnNo + 1;
		this.stg = stg;
	}

	boolean clearScore() {
		this.score = 0;
		return true;
	}

	int getScore() {
		return this.score;
	}

	boolean isAliveHero() {
		return this.get(this.heroPlnNo) != null;
	}

	STGHero getHero() {
		return ((STGHero) this.get(heroPlnNo));
	}

	int getHP() {
		return ((STGHero) this.get(heroPlnNo)).getHP();
	}

	int getMP() {
		return ((STGHero) this.get(heroPlnNo)).getMP();
	}

	boolean clearObjects() {
		this.clearScore();
		Enumeration<STGObject> e = this.elements();
		for (; e.hasMoreElements();) {
			STGObject o = ((STGObject) e.nextElement());
			this.delObj(o.plnNo);
		}
		this.count = this.firstPlnNo;
		return true;
	}

	int addBombHero(String packageName) {
		STGHero hero = (STGHero) this.get(heroPlnNo);
		if (hero != null) {
			final int x = hero.getX();
			final int y = hero.getY();
			int id = this.addPlane(packageName, x, y, this.heroPlnNo,
					this.overCount);
			STGObject obj = get(id);
			if (obj != null) {
				obj.setX(x + (hero.getHitW() - obj.getHitW()) / 2);
				obj.setY(y + (hero.getHitH() - obj.getHitH()) / 2);
			}
			++this.overCount;
			return this.overCount - 1;
		} else {
			return 0;
		}
	}

	int addBombHero(String packageName, int x, int y) {
		this.addPlane(packageName, x, y, this.heroPlnNo, this.overCount);
		++this.overCount;
		return this.overCount - 1;
	}

	int addHero(String packageName, int x, int y, int no) {
		this.heroPlnNo = no;
		this.overCount = this.heroPlnNo + 1;
		this.addPlane(packageName, x, y, this.heroPlnNo, this.heroPlnNo);
		return no;
	}

	int addClass(String packageName, int x, int y, int tpno) {
		int count = this.count;
		this.addPlane(packageName, x, y, tpno, this.count);
		++this.count;
		if (this.count >= this.heroPlnNo) {
			this.count = this.firstPlnNo;
		}
		return count;
	}

	static Class<?> newClass(String packageName) throws ClassNotFoundException {
		return Class.forName(packageName);
	}

	@SuppressWarnings("unchecked")
	int addPlane(String packageName, int x, int y, int tpno, int no) {
		try {
			Class[] clazz = new Class[] { STGScreen.class, Integer.TYPE,
					Integer.TYPE, Integer.TYPE, Integer.TYPE };
			Constructor constructor = newClass(packageName).getConstructor(
					clazz);
			Object[] args = new Object[] { this.stg, no, x, y, tpno };
			Object newObject = constructor.newInstance(args);
			this.put(no, (STGObject) newObject);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return no;
	}

	void delObj(int index) {
		this.stg.deletePlane(index);
		this.remove(index);
	}

	void running() {
		Enumeration<STGObject> e = this.elements();
		for (; e.hasMoreElements();) {
			e.nextElement().update();
		}
	}

	void hitCheckHeroShot() {
		STGObject o = null;
		Enumeration<STGObject> e = this.elements();
		for (; e.hasMoreElements();) {
			STGObject shot =  e.nextElement();
			if (shot.attribute == STGScreen.HERO_SHOT) {
				o = shot;
				Enumeration<STGObject> enumeration = this.elements();
				while (enumeration.hasMoreElements()) {
					shot = (STGObject) enumeration.nextElement();
					if (shot.attribute == STGScreen.ENEMY
							&& MathUtils.abs(this.stg.getPlanePosX(o.plnNo)
									+ o.hitX + o.getHitW() / 2
									- this.stg.getPlanePosX(shot.plnNo)
									- shot.hitX - shot.getHitW() / 2) < (o
									.getHitW() + shot.getHitW()) / 2
							&& MathUtils.abs(this.stg.getPlanePosY(o.plnNo)
									+ o.hitY + o.getHitH() / 2
									- this.stg.getPlanePosY(shot.plnNo)
									- shot.hitY - shot.getHitH() / 2) < (o
									.getHitH() + shot.getHitH()) / 2) {
						--o.hitPoint;
						if (o.hitPoint == 0) {
							this.delObj(o.plnNo);
						}
						--shot.hitPoint;
						shot.hitFlag = true;
						if (shot.hitPoint == 0) {
							this.score += shot.scorePoint;
							shot.attribute = STGScreen.ENEMY_SHOT;
						}
						break;
					}
				}
			}
		}

	}

	void hitCheckHero() {
		STGObject o = null;
		Enumeration<STGObject> e = this.elements();
		STGObject shot;
		for (; e.hasMoreElements();) {
			shot = e.nextElement();
			if (shot.attribute == STGScreen.HERO) {
				o = shot;
				break;
			}
		}
		if (o != null) {
			e = this.elements();
			for (; e.hasMoreElements();) {
				shot = (STGObject) e.nextElement();
				if ((shot.attribute == STGScreen.ENEMY
						|| shot.attribute == STGScreen.ITEM
						|| shot.attribute == STGScreen.ENEMY_SHOT || shot.attribute == STGScreen.ALL_HIT)
						&& MathUtils.abs(this.stg.getPlanePosX(o.plnNo)
								+ o.hitX + o.getHitW() / 2
								- this.stg.getPlanePosX(shot.plnNo) - shot.hitX
								- shot.getHitW() / 2) < (o.getHitW() + shot
								.getHitW()) / 2
						&& MathUtils.abs(this.stg.getPlanePosY(o.plnNo)
								+ o.hitY + o.getHitH() / 2
								- this.stg.getPlanePosY(shot.plnNo) - shot.hitY
								- shot.getHitH() / 2) < (o.getHitH() + shot
								.getHitH()) / 2) {
					if (shot.attribute != STGScreen.ITEM) {
						--shot.hitPoint;
						shot.hitFlag = true;
						if (shot.hitPoint == 0) {
							this.score += shot.scorePoint;
							shot.attribute = STGScreen.NO_HIT;
							if (shot.attribute == STGScreen.ENEMY_SHOT) {
								this.delObj(shot.plnNo);
							}
						}
						o.attribute = STGScreen.NO_HIT;
					} else {
						Item i = (Item) shot;
						i.giveHeroEvent((STGHero) o);
						i.attribute = STGScreen.NO_HIT;
					}
					break;
				}
			}

		}
	}

	public void dispose() {
		clear();
	}

}
