package org.metalslugd.stage;

import org.metalslug.C;
import org.redengine.game.entity.RPhysicsEntity;
import org.redengine.systems.physicssystem.RPhysicsObject;
import org.redengine.systems.physicssystem.physicshandler.BContactFilter;

public class PlayerGround extends RPhysicsEntity{
	
	public PlayerGround(){
		this.setEntityID(C.GROUD);
	}
	
	public void init(int x,int y,int width,int height){
		RPhysicsObject phy=new RPhysicsObject();
		phy.registerCollide(0, 0, true,new BContactFilter(C.PLAYER_GROUND_BIT,C.PLAYER_GROUND_FILTER));
		phy.setAsBox(x, y, width, height);
		this.addPhysicsObject(phy);
	}
	
	@Override
	public boolean cycleCondition() {
		return false;
	}

	@Override
	public void onGetFromPool() {
		
	}

	@Override
	public void onCycle() {
		
	}

	@Override
	public void onGetFromPool(Object obj) {
		// TODO Auto-generated method stub
		
	}
	
}

