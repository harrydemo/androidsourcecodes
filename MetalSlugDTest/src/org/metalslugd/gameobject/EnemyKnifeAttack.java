package org.metalslugd.gameobject;

import org.metalslug.C;
import org.metalslugd.gameobject.enemy.Enemy;
import org.metalslugd.gameobject.player.Player;
import org.redengine.game.entity.REntity;
import org.redengine.game.entity.REntityPool;
import org.redengine.game.entity.RPhysicsEntity;
import org.redengine.systems.common.V2;
import org.redengine.systems.physicssystem.RPhysicsObject;
import org.redengine.systems.physicssystem.physicshandler.BContactFilter;

public class EnemyKnifeAttack extends REntityPool {
	
	public EnemyKnifeAttack() {
		super(C.ENEMY_BULLET_A_CAPACITY);
	}
	public REntity createPoolObject() {
		EnemyKnife temp=new EnemyKnife();
		temp.init();
		return temp;
	}
}

class EnemyKnife extends RPhysicsEntity {

	static long nextTime;
	
	public void init(){
		this.setEntityID(C.ENEMY_BULLET);
		RPhysicsObject phy=new RPhysicsObject();
		phy.setAsAABB(500,500,60,70);phy.registerMove(0);
		phy.registerCollide(0, 0, false,new BContactFilter(C.ENEMY_BULLET_BIT,C.ENEMY_BULLET_FILTER));
		this.addPhysicsObject(phy);
		phy.sleep=true;
	}
	
	@Override
	public boolean cycleCondition() {
		return System.currentTimeMillis()>nextTime;
	}

	@Override
	public void onGetFromPool(Object obj) {
		super.onGetFromPool();
		nextTime=System.currentTimeMillis()+50;
		Enemy player=(Enemy)obj;
		final V2 v=player.getPhysicsObject().getPosition();
		final int rate=player.gen_right?25:-25;
		getPhysicsObject().setPosition(v.x+rate, v.y);
	}
	
}
