package org.metalslugd.gameobject;

import org.metalslug.C;
import org.metalslugd.gameobject.player.Player;
import org.metalslugd.mission.stages.Stage_2;
import org.redengine.game.entity.REntity;
import org.redengine.game.entity.REntityPool;
import org.redengine.game.entity.RPhysicsEntity;
import org.redengine.systems.common.V2;
import org.redengine.systems.physicssystem.RPhysicsObject;
import org.redengine.systems.physicssystem.physicshandler.BContactFilter;

/**
 * µ¶×ÓµÄÅÐ¶¨³Ø
 * @author xujun
 */
public class KnifeAttack extends REntityPool {

	public KnifeAttack() {
		super(2);
	}
	public REntity createPoolObject() {
		knife temp=new knife();
		temp.init();
		return temp;
	}
}

class knife extends RPhysicsEntity {

	static long nextTime;
	
	public void init(){
		this.setEntityID(C.BULLET);
		RPhysicsObject phy=new RPhysicsObject();
		phy.setAsAABB(300,600,80,70);
		phy.registerMove(0);
		phy.registerCollide(0, 0, false,new BContactFilter(C.BULLET_BIT,C.BULLET_FILTER));
		this.addPhysicsObject(phy);
		//phy.sleep=true;
	}
	
	@Override
	public boolean cycleCondition() {
		return System.currentTimeMillis()>nextTime;
	}
	
	public void onCycle(){
		getPhysicsObject().setPosition(300, 600);
	}


	@Override
	public void onGetFromPool(Object obj) {
		super.onGetFromPool();
		nextTime=System.currentTimeMillis()+C.KNIFE_DELAY;
		Player player=(Player)obj;
		final V2 v=player.getPhysicsObject().getPosition();
		final int rate=player.gen_right?25:-25;
		getPhysicsObject().setPosition(v.x+rate, v.y);
	}
	
	/*public void contactCreated(final REntity e,final V2 MTD){
		if(e.getEntityID()==C.ENEMYA){
			Stage_2.score+=400;
		}
	}*/
	
}