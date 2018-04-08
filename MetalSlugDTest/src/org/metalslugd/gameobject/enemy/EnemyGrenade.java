package org.metalslugd.gameobject.enemy;

import org.redengine.game.entity.REntityPool;

/**
 * 敌人装备的手雷
 * @author xujun
 */

public class EnemyGrenade {

	protected REntityPool pool;
//	protected RAnimation anima;
	
	/**
	 * 装载武器所有的资源
	 * @param player
	 * @param pool
	 * @param anima
	 */
	public void initGrenade(REntityPool pool){
		this.pool=pool;
	}
	
	public void attack(EnemyA enemy){}
	
	public static class GeneralGrenade extends EnemyGrenade{
		public void attack(EnemyA enemy){
			pool.getEntity(enemy);
		}
	}
	
}
