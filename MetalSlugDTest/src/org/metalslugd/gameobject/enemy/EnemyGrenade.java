package org.metalslugd.gameobject.enemy;

import org.redengine.game.entity.REntityPool;

/**
 * ����װ��������
 * @author xujun
 */

public class EnemyGrenade {

	protected REntityPool pool;
//	protected RAnimation anima;
	
	/**
	 * װ���������е���Դ
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
