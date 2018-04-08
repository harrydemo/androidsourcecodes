package org.metalslugd.gameobject.enemy;

import org.metalslug.C;
import org.metalslugd.gameobject.EnemyKnifeAttack;
import org.redengine.game.entity.REntity;
import org.redengine.game.entity.REntityPool;
import org.redengine.systems.actionsystem.RActionManager;

public class EnemyAPool extends REntityPool {

	RActionManager am;
	EnemyGrenade.GeneralGrenade gG;
	EnemyKnifeAttack knife;
	
	public EnemyAPool(RActionManager m,EnemyGrenade.GeneralGrenade gG,
			EnemyKnifeAttack knife) {
		super(C.ENEMY_CAPACITY);
		am=m;
		this.gG=gG;
		this.knife=knife;
	}

	@Override
	public REntity createPoolObject() {
		EnemyA e=new EnemyA();
		e.init(am,gG,knife,-100,-100);
		return e;
	}

}
